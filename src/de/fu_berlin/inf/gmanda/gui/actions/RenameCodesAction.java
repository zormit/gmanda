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

import de.fu_berlin.inf.gmanda.gui.CodeList;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class RenameCodesAction extends AbstractAction {

	CodeList codelist;
	
	public RenameCodesAction(ProjectProxy project, CodeList codeList) {
		super("Rename Code...");
		this.codelist = codeList;
	
		project.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
	}

	public void actionPerformed(ActionEvent arg0) {
		
		List<String> selected = codelist.getSelection();
		
		if (selected == null || selected.size() != 1)
			return;
		
		String renameFrom = selected.get(0);
		
		String renameTo = JOptionPane.showInputDialog(codelist, "Rename this code to", renameFrom);
		
		if (renameTo != null)
			codelist.rename(renameFrom,renameTo);
		
	}

};
