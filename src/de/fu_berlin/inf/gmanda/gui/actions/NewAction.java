/*
 * Created on 05.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;

public class NewAction extends AbstractAction {

    ProjectProxy projectProxy;

    public NewAction(ProjectProxy projectProxy) {
        super("New Project");
        this.projectProxy = projectProxy;
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
                KeyEvent.CTRL_MASK));
        putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
    }

    public void actionPerformed(ActionEvent arg0) {
        projectProxy.setVariable(new Project());
    }

}
