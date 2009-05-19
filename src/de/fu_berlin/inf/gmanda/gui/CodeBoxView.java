/*
 * Created on 04.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui;

import java.awt.AWTKeyStroke;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;

public class CodeBoxView extends JScrollPane {

	public CodeBoxView(final CodeBox codeBox, ProjectProxy project,
			SelectionProxy selection) {
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		setBorder(null);
		setMinimumSize(new Dimension(200, 90));

		Set<AWTKeyStroke> newForwardKeys = new HashSet<AWTKeyStroke>();
		newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
		newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));

		setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
				newForwardKeys);
		setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
				null);

		setViewportView(codeBox);
	}

}
