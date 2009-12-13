package de.fu_berlin.inf.gmanda.gui.manager;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.commons.lang.ObjectUtils;

import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.exceptions.WrappedException;
import de.fu_berlin.inf.gmanda.gui.misc.LockManager;
import de.fu_berlin.inf.gmanda.gui.misc.ProjectFileChooser;
import de.fu_berlin.inf.gmanda.gui.misc.LockManager.LockHandle;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.qda.ProjectLoader;
import de.fu_berlin.inf.gmanda.util.StateChangeListener;
import de.fu_berlin.inf.gmanda.util.StateChangeNotifier;
import de.fu_berlin.inf.gmanda.util.VetoableVariableProxy;
import de.fu_berlin.inf.gmanda.util.VetoableVariableProxyListener;

public class UndoManagement {

	VetoableVariableProxy<Project> projectProxy;
	ProjectFileChooser chooser;
	ProjectLoader loader;

	LockManager lockManager;

	Project currentProject;

	public UndoManagement(VetoableVariableProxy<Project> projectProxy, ProjectFileChooser chooser,
		ProjectLoader loader, LockManager lockManager) {

		this.projectProxy = projectProxy;
		this.chooser = chooser;
		this.loader = loader;
		this.lockManager = lockManager;

		currentProject = projectProxy.getVariable();

		projectProxy.addAndNotify(new VetoableVariableProxyListener<Project>() {

			StateChangeListener<Project> listener = new StateChangeListener<Project>() {
				public void stateChangedNotification(Project t) {
					modify();
				}
			};

			public boolean setVariable(Project newProject, Project oldProject) {

				assert oldProject == currentProject;

				if (newProject == oldProject)
					return true;

				boolean shouldChange = true;

				boolean sameSaveFile = oldProject != null && newProject != null
					&& ObjectUtils.equals(oldProject.saveFile, newProject.saveFile);

				if (!(oldProject == null || sameSaveFile)) {
					// on reloading we do not perform the close check
					shouldChange = isCloseAllowed();
				}

				if (shouldChange) {
					if (currentProject != null)
						currentProject.getGlobalChangeNotifier().remove(listener);

					currentProject = newProject;

					if (currentProject != null)
						currentProject.getGlobalChangeNotifier().add(listener);
				}
				return shouldChange;
			}
		});
	}

	public StateChangeNotifier<Boolean> notifier = new StateChangeNotifier<Boolean>();

	public StateChangeNotifier<Boolean> getModifiedNotifier() {
		return notifier;
	}
	
	public StateChangeNotifier<Project> saveNotifier = new StateChangeNotifier<Project>();
	
	public StateChangeNotifier<Project> getSaveNotifier() {
		return saveNotifier;
	}

	boolean isModified = false;

	public boolean isModified() {
		return isModified;
	}

	public void modify() {
		setIsModified(true);
	}

	public void save() {

		/*
		 * We cannot assert the following, since it might be that we come into
		 * save() from isCloseAllowed(), which is called by the project variable
		 * proxy. This might cause the value in the projectProxy to be not the
		 * same as the currently opened project.
		 * 
		 * assert currentProject == projectProxy.getVariable();
		 */

		if (currentProject == null)
			return;

		if (currentProject.getSaveFile() == null) {
			saveAs();
			return;
		}

		try {
			loader.save(currentProject.getSaveFile(), currentProject, null);
		} catch (IOException e) {
			throw new ReportToUserException(e);
		}

		saveNotifier.notify(currentProject);
		
		setIsModified(false);
	}

	public void saveAs() {
		assert currentProject == projectProxy.getVariable();

		if (currentProject == null)
			return;

		File file = null;

		askForFile: while (true) {

			file = chooser.getSaveFile();

			// Canceled by user
			if (file == null)
				return;

			// User selected the current file...
			if (file.equals(currentProject.getSaveFile())) {
				// ... thus: just save
				save();
				return;
			}

			// If file exists, we need to do an overwrite check
			if (file.exists()) {

				switch (JOptionPane.showConfirmDialog(null,
					"File already exists. Do you want to override?")) {

				case JOptionPane.YES_OPTION:
					// got file, just continue after the while loop
					break askForFile;

				case JOptionPane.NO_OPTION:
					// repeat loop
					file = null;
					break;

				case JOptionPane.CANCEL_OPTION:
				default:
					// abort
					return;
				}
			} else
				break askForFile;
		}

		if (!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// This will cause an exception later
			}
		}
		
		// Try to get lock, this will throw an exception if a lock could not be
		// gotten.
		LockHandle lock = lockManager.getLock(file);

		try {
			loader.save(file, currentProject, null);
		} catch (IOException e) {
			// We failed to save the file, release the lock and report error to
			// user.
			lock.release();
			throw new ReportToUserException(e);
		}

		currentProject.setSaveFile(file);

		lockManager.putLock(currentProject, lock);
		
		saveNotifier.notify(currentProject);

		setIsModified(false);
	}

	public void setIsModified(boolean newValue) {
		if (newValue != isModified) {
			isModified = newValue;
			notifier.notify(isModified);
		}
	}

	public boolean isCloseAllowed() {
		if (isModified()) {
			switch (JOptionPane.showConfirmDialog(null,
				"Project has been changed, do you want to save?")) {

			case JOptionPane.YES_OPTION:
				try {
					save();
				} catch (Exception e) {
					JOptionPane
						.showMessageDialog(null, "Could not save project: " + e.getMessage());
					return false;
				}

				return true;
			case JOptionPane.NO_OPTION:
				return true;

			case JOptionPane.CANCEL_OPTION:
			default:
				return false;
			}
		} else {
			return true;
		}
	}

	public boolean close() {

		assert currentProject == projectProxy.getVariable();

		if (currentProject == null)
			return true;

		return projectProxy.setVariable(null);
	}

	public void load(File file) {

		LockHandle lockHandle;

		if (currentProject != null && file.equals(currentProject.getSaveFile()) && isModified()) {
			// Okay, the user asks us to load the same file that is currently
			// open
			if (JOptionPane.showConfirmDialog(null,
				"Do you want to reload your current project file? All changes will be lost!") != JOptionPane.YES_OPTION) {
				return;
			}
		}

		// Try to acquire lock for the file, this will throw an
		// exception if a lock could not be gotten.
		lockHandle = lockManager.getLock(file);

		try {
			Project project = loader.load(file, null);

			lockManager.putLock(project, lockHandle);

			if (project != null) {
				// Set the new project as the active one

				boolean successful = projectProxy.setVariable(project);

				if (!successful) {
					lockHandle.release();
				}
			}

		} catch (Exception e) {
			lockHandle.release();

			if (e instanceof WrappedException) {
				throw (WrappedException) e;
			} else {
				throw new ReportToUserException(e);
			}
		}
	}

	public void load() {

		// First warn the user that s/he is closing
		if (!isCloseAllowed())
			return; // we were denied to close old project

		File file = chooser.getOpenFile();

		// User canceled file open dialog
		if (file == null)
			return;

		load(file);
	}
}
