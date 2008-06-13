package de.fu_berlin.inf.gmanda.gui.manager;

import java.io.File;

import de.fu_berlin.inf.gmanda.gui.MainFrame;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.StateChangeListener;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.VetoableVariableProxy;

public class TitleManager {
	
	VetoableVariableProxy<Project> projectProxy;
	
	UndoManagement manager;
	
	MainFrame frame;
	
	public TitleManager(MainFrame frame, UndoManagement undoManager, ProjectProxy projectProxy){

		this.projectProxy = projectProxy;
		this.manager = undoManager;
		this.frame = frame;
		
		projectProxy.add(new VariableProxyListener<Project>(){
			public void setVariable(Project newValue) {
				update();
			}
		});
		
		undoManager.getModifiedNotifier().add(new StateChangeListener<Boolean>(){
			public void stateChangedNotification(Boolean t) {
				update();
			}
		});
		
		update();
	}
	
	public void update(){
		if (projectProxy.getVariable() == null){
			frame.setTitle("Gmanda - No Project Open");
		} else {
			
			File f = projectProxy.getVariable().getSaveFile();
			
			String projectName = (f == null ? "Undefined" : f.getAbsolutePath());
			
			if (manager.isModified()){
				frame.setTitle("Gmanda - " + projectName + "*");
			} else {
				frame.setTitle("Gmanda - " + projectName);
			}
		}
		
	}
}
