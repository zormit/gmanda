/*
 * Created on 04.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui;


import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;

public class CodeBoxView extends JScrollPane {

	public CodeBoxView(ProjectProxy project, SelectionProxy selection) {
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		setBorder(BorderFactory.createTitledBorder("Codes"));
		setMinimumSize(new Dimension(200, 90));
	}

}
