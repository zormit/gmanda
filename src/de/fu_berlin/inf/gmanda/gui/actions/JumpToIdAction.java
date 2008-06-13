/*
 * Created on 05.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTree;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.tree.TreeWalker;

public class JumpToIdAction extends AbstractAction {

	Project project;
	PrimaryDocumentTree documentTree;
	SelectionProxy selection;
	
	public JumpToIdAction(ProjectProxy projectProxy, 
		PrimaryDocumentTree primaryDocumentTree, 
		SelectionProxy selectionProxy) {
		super("Jump to Id...");
	
		this.documentTree = primaryDocumentTree;
		this.selection = selectionProxy;
		
		projectProxy.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newProject) {
				project = newProject;
				setEnabled(newProject != null);
			}
		});

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_J, KeyEvent.CTRL_MASK));
		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_J));
	}

	public void actionPerformed(ActionEvent arg0) {
		
		if (project == null)
			return;
		
		String input = JOptionPane.showInputDialog(null, "Enter Id @ list");
		
		if (input == null)
			return;
		
		input = input.trim();
		
		String[] checkForAt = input.split("@");
		
		final String id = checkForAt[0];
		String p;
		
		if (checkForAt.length == 2){
			p = checkForAt[1];
		} else { 
			p = null;
		}
		
		List<PrimaryDocument> roots = project.getPrimaryDocuments();
		
		if (roots.size() == 0)
			return;
		
		PrimaryDocument root = null;
		
		if (p != null){
			
			for (PrimaryDocument pd : project.getPrimaryDocuments()){
				if (pd.getName().contains(p)){
					root = pd;
					break;
				}
			}
		} else {
			Object selected = selection.getVariable();
			if (selected instanceof PrimaryDocument) {
				root = ((PrimaryDocument)selected);
				while (root.getParent() != null)
					root = root.getParent(); 
			} else {
				root = roots.get(0);
			}
		}
		if (root == null)
			return;
		
		// Call update on each primary document
		for (PrimaryDocument t : new TreeWalker<PrimaryDocument>(root, PrimaryDocument.getTreeMaker())){
			String pdId = t.getMetaData().get("id");
			
			if (pdId != null && pdId.equals(id)){
				selection.setVariable(t);
				return;
			}
		}
		JOptionPane.showMessageDialog(null, "Could not find primary document with the given id");
		
	}

};
