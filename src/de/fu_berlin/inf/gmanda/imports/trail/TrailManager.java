package de.fu_berlin.inf.gmanda.imports.trail;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ObjectUtils;
import org.joda.time.DateTime;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.manager.UndoManagement;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.startup.CommandLineOptions;
import de.fu_berlin.inf.gmanda.util.CStringBuilder;
import de.fu_berlin.inf.gmanda.util.CStringWriter;
import de.fu_berlin.inf.gmanda.util.StateChangeListener;
import de.fu_berlin.inf.gmanda.util.VariableProxy;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class TrailManager {

	VariableProxy<Project> projectProxy;

	PrimaryDocument selectedPD;

	CommonService common;
	
	boolean noTrail;
	
	Project currentProject;

	public TrailManager(SelectionProxy selection, ProjectProxy projectProxy,
		UndoManagement undoMan, CommonService common, CommandLineOptions options) {

		this.noTrail = options.noTrail;

		if (noTrail)
			return;

		this.projectProxy = projectProxy;
		this.common = common;
		
		selection.add(new VariableProxyListener<Object>() {
			public void setVariable(Object newValue) {
				// Before we leave the currently selected PD, flush the changes (if any)
				if (selectedPD != null) {
					writeChange(selectedPD);
				}

				if (newValue instanceof PrimaryDocument) {
					selectedPD = (PrimaryDocument) newValue;
				}
			}
		});

		projectProxy.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newProject) {

				if (currentProject != null)
					unsubscribe(currentProject);

				currentProject = newProject;

				if (currentProject != null)
					subscribe(currentProject);
			}
		});
		
		undoMan.getSaveNotifier().add(new StateChangeListener<Project>(){
			public void stateChangedNotification(Project t) {
				if (currentProject != t || t == null){
					return;
				}
				
				split();
			}
		});
	}

	HashMap<PrimaryDocument, String> currentCodes = new HashMap<PrimaryDocument, String>();

	protected void update(PrimaryDocument pd) {

		if (!currentCodes.containsKey(pd)) {
			writeNewPd(pd);
			currentCodes.put(pd, pd.getCodeAsString());
		}

		// Code changed:
		if (pd == selectedPD) {
			// Is currently editing, don't do anything...
		} else {
			writeChange(pd);
		}
	}

	public void writeNewPd(PrimaryDocument pd) {
		writer.writeln(new DateTime().toString() + " new     " + pd.getFilename());
		String code = pd.getCodeAsString();
		if (code != null && code.trim().length() > 0) {
			writer.writeln("Code:", 2);
			writer.writeln(code, 4);
		}
		writer.flush();
	}

	public void writeChange(PrimaryDocument pd) {

		String oldCode = currentCodes.get(pd);
		String newCode = pd.getCodeAsString();

		if (ObjectUtils.equals(oldCode, newCode))
			return;

		currentCodes.put(pd, pd.getCodeAsString());

		writer.writeln(new DateTime().toString() + " changed " + pd.getFilename());

		if (oldCode != null && oldCode.trim().length() > 0) {
			writer.writeln("Old:", 2);
			writer.writeln(oldCode, 4);
		} else {
			writer.writeln("No old code", 2);
		}

		if (newCode != null && newCode.trim().length() > 0) {
			writer.writeln("New:", 2);
			writer.writeln(newCode, 4);
		} else {
			writer.writeln("No new code", 2);
		}
		writer.flush();
	}

	boolean tracking = true;

	StateChangeListener<PrimaryDocument> listener = new StateChangeListener<PrimaryDocument>() {
		public void stateChangedNotification(PrimaryDocument t) {
			if (tracking)
				update(t);
		}
	};

	CStringWriter writer;

	protected void subscribe(Project project) {
		final String format = "yyyy-MM-dd-HH'h'mm";

		String fileName;
		if (project.getSaveFile() != null) {
			fileName = FilenameUtils.removeExtension(project.getSaveFile().getAbsolutePath()) + "-"
				+ new DateTime().toString(format);
		} else {
			fileName = "Gmanda-" + new DateTime().toString(format);
		}

		if (new File(fileName + ".trail").exists()){
			int i = 2;
			
			while (new File(fileName + "_" + i + ".trail").exists() && i < 1000){
				i++;
			}
			fileName = fileName + "_" + i;
			// TODO problem if i == 1000
		}
		
		fileName = fileName + ".trail";
		
		common.log("Opening trail log " + fileName);
		writer = new CStringWriter(fileName);

		for (PrimaryDocument pd : PrimaryDocument.getTreeWalker(project.getPrimaryDocuments())) {
			currentCodes.put(pd, pd.getCodeAsString());
		}

		project.getLocalChangeNotifier().add(listener);
	}

	protected void unsubscribe(Project project) {
		project.getLocalChangeNotifier().remove(listener);
		
		// Flush changes of selectedPD (if any)
		if (selectedPD != null) {
			writeChange(selectedPD);
		}

		writer.close();
	}
	
	public void writeRename(String renameFrom, String renameTo, List<PrimaryDocument> pds) {

		writer.writeln(String.format("%s renamed '%s' to '%s'", new DateTime().toString(),
			renameFrom, renameTo));
		
		CStringBuilder sb = writer.indent(2);

		for (PrimaryDocument pd : pds) {
			sb.writeln(pd.getFilename());
			currentCodes.put(pd, pd.getCodeAsString());
		}
		writer.flush();
	}

	public void renameOperationStarted() {
		if (noTrail)
			return;
		
		tracking = false;
	}

	public void renameOperationFinished(String renameFrom, String renameTo,
		List<PrimaryDocument> pds) {
		
		if (noTrail)
			return;
		
		tracking = true;
		writeRename(renameFrom, renameTo, pds);
	}

	public void split() {
		if (currentProject == null)
			return;
		
		// close and directly reopen 
		unsubscribe(currentProject);
		subscribe(currentProject);	
	}

	
}
