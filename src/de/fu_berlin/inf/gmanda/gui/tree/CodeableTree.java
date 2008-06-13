package de.fu_berlin.inf.gmanda.gui.tree;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.imports.GmaneFacade;
import de.fu_berlin.inf.gmanda.proxies.FilterKindProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class CodeableTree extends JTree {

	ProjectProxy project;

	CommonService progress;
	
	GmaneFacade gmane;
	
	Project currentProject; 
	
	FilterKindProxy filter;
	
	SelectionProxy selection;
	
	JNodeSelectionListener selectionListener;

	public CodeableTree(ProjectProxy projectProxy, GmaneFacade gmane,
		JNodeCellRenderer cellRenderer, CommonService progress, 
		FilterKindProxy filter, JNodeSelectionListener selectionListener,
		SelectionProxy selection) {
		super();
		
		this.project = projectProxy;
		this.progress = progress;
		this.gmane = gmane;
		this.filter = filter;
		this.selection = selection;
		this.selectionListener = selectionListener;
		

		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		setEditable(false);
		setRootVisible(false);
		setExpandsSelectedPaths(true);
		setCellRenderer(cellRenderer);
		
		getSelectionModel().addTreeSelectionListener(selectionListener);

		projectProxy.addAndNotify(new VariableProxyListener<Project>() {
			public void setVariable(Project project) {
				currentProject = project;
				
				update();
			}
		});
		
		
	}
	
	public void update(){
		
		if (currentProject == null) {
			setEnabled(false);
			setBackground(Color.LIGHT_GRAY);
			setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));
		} else {
			setEnabled(true);
			setBackground(Color.WHITE);
			
			SequentialNode root = new SequentialNode(null, "Root");
			
			root.add(new ProjectNode(root, currentProject));
			root.add(new CodesNode(root, currentProject.getCodeModel(), filter.getVariable()));
			
			setModel(new JNodeTreeModel(root));
		}
	}
}

