/*
 * Created on 05.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTree;
import de.fu_berlin.inf.gmanda.gui.search.LuceneFacade;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class SearchWithLuceneAction extends AbstractAction {

	Project project;
	PrimaryDocumentTree documentTree;
	SelectionProxy selection;
	LuceneFacade lucene;
	
	public SearchWithLuceneAction(ProjectProxy projectProxy, 
		SelectionProxy selection, 
		LuceneFacade lucene) {
		super("Search with Lucene...");
	
		this.lucene = lucene;
		this.selection = selection;
		
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
		
		String input = JOptionPane.showInputDialog(null, "Enter search query");
		
		if (input == null)
			return;
		
		input = input.trim();
		
		Iterator<PrimaryDocument> hits = lucene.search(project, input);
		
		if (hits.hasNext()){
			selection.setVariable(hits.next());
		}
		
	}

};
