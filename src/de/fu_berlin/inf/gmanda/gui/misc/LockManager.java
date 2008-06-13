package de.fu_berlin.inf.gmanda.gui.misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fu_berlin.inf.gmanda.exceptions.CouldNotGetLockException;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.HashMapUtils;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.VetoableVariableProxy;

public class LockManager {

	Map<Project, LockHandle> openLocks = new HashMap<Project, LockHandle>();

	Map<File, GmandaLock> locksByFile = new HashMap<File, GmandaLock>();
	
	Map<GmandaLock, List<LockHandle>> handles = new HashMap<GmandaLock, List<LockHandle>>();
	
	VetoableVariableProxy<Project> projectProxy;

	public class LockHandle {
		
		GmandaLock backingLock;
		
		public LockHandle(GmandaLock backingLock){
			this.backingLock = backingLock;
			
			HashMapUtils.putList(handles, backingLock, this);
		}
		
		public File getFile(){
			return backingLock.getFile();
		}
		
		public void release(){
			
			if (handles.containsKey(backingLock)){
				List<LockHandle> handlesForBackingLock = handles.get(backingLock);
				handlesForBackingLock.remove(this);
				
				if (handlesForBackingLock.size() == 0){
					handles.remove(handlesForBackingLock);
					backingLock.release();
				}
			}
		}
	}
	
	public class GmandaLock {

		public FileChannel lockingChannel;

		public FileLock javalock;
		
		public File file;

		public void release() {

			try {
				javalock.release();
			} catch (IOException e) {
				// Maybe notify the user
			}

			try {
				lockingChannel.close();
			} catch (IOException e) {
				// Maybe notify the user
			}
			
			locksByFile.remove(file);
		}
		
		public File getFile(){
			return file;
		}

		public void acquire(File file) {
			
			this.file = file;
			try {
				lockingChannel = new RandomAccessFile(file, "rw").getChannel();
			} catch (FileNotFoundException e) {
				throw new CouldNotGetLockException("Could not get lock for file " + file.getPath()
					+ " because it does not exist.");
			}

			try {
				// Obtain a exclusive lock on the last possible byte.
				javalock = lockingChannel.tryLock(Long.MAX_VALUE - 1, 1, false);
			} catch (IOException e) {
				throw new CouldNotGetLockException(
					"Could not get lock for file "
						+ file.getPath()
						+ " because "
						+ e.getMessage()
						+ "\n\nTry restarting GmanDA and make sure that you only have a single instance of GmanDA running.");
			}

			if (javalock == null || !javalock.isValid()) {
				throw new CouldNotGetLockException(
					"Could not get lock for file "
						+ file.getPath()
						+ ".\n\nTry restarting GmanDA and make sure that you only have a single instance of GmanDA running.");
			}
			
			locksByFile.put(file, this);

		}

	}
	
	public LockHandle getLock(File file) {

		// First check if we already hold a lock to this file
		if (locksByFile.containsKey(file)){
			
			// Return a new handle to this lock
			return new LockHandle(locksByFile.get(file));
		}

		// We need to create a new Lock to the file
		GmandaLock lockForFile = new GmandaLock();

		// Acquiring the lock will also link it to the file
		lockForFile.acquire(file);

		return new LockHandle(lockForFile);
	}

	public LockManager(VetoableVariableProxy<Project> projectProxy) {

		this.projectProxy = projectProxy;

		projectProxy.addAndNotify(new VariableProxyListener<Project>() {
			
			Project oldProject;
			
			public void setVariable(Project newProject) {

				if (newProject == oldProject)
					return;
				
				// First check old project
				if (oldProject != null){
					
					LockHandle lock = openLocks.get(oldProject);
					
					if (lock == null){
						assert oldProject.saveFile == null;
					} else {
						// release lock
						lock.release();
						openLocks.remove(oldProject);
					}
				}
				
				// We do not have to do anything really for new projects, as
				// they should be locked in any case by the loader
				if (newProject != null && newProject.getSaveFile() != null){
					LockHandle lock = openLocks.get(newProject);
					assert lock != null && lock.getFile().equals(newProject.getSaveFile()); 
				}
				
				oldProject = newProject;
			}
		});
	}
		
	/**
	 * PutLock associates the given project with the given lock.
	 * 
	 * The lock is released when the active project changes (LockManager
	 * monitors the ProjectProxy for this) or the savefile of the project
	 * changes.
	 * 
	 */
	public void putLock(Project project, LockHandle lock) {

		assert lock.getFile().equals(project.getSaveFile());
		
		LockHandle existingLock = openLocks.get(project);
		
		if (existingLock != null){
			existingLock.release();
			openLocks.remove(project);
		}

		openLocks.put(project, lock);
	}
}
