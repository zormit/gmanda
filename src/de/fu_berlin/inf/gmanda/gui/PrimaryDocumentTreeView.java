package de.fu_berlin.inf.gmanda.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.fu_berlin.inf.gmanda.gui.docking.DockableView;
import de.fu_berlin.inf.gmanda.gui.preferences.ColorProperties;

public class PrimaryDocumentTreeView extends JPanel implements DockableView {

	String tooltip;
	
	public PrimaryDocumentTreeView(PrimaryDocumentTreeFilterTextField textField,
		PrimaryDocumentTree tree, ColorProperties colors) {

		setLayout(new BorderLayout(5,5));
		
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		JScrollPane sTree = new JScrollPane(tree);
		sTree.setBorder(BorderFactory.createEmptyBorder());
		
		add(textField, BorderLayout.PAGE_START);
		add(sTree, BorderLayout.CENTER);
		
		tooltip = "<html>" +
		"The document tree is in charge of showing all documents of the current<br>" +
		"project. You can use the interface at the top to restrict the view to a<br>" +
		"subset of all documents." +
		"<p>The following colors are used in the tree:" +
		"<ul>" +
		"<li><span style=\"color:white;background-color:" + colors.selected.getValueAsString() + "\">The currently selected document</span></li>" +
		"<li><span style=\"background-color:" + colors.match.getValueAsString() + "\">Documents matching your filter</span></li>" +
		"<li><span style=\"background-color:" + colors.coded.getValueAsString() + "\">Documents that have been coded</span></li>" +				
		"<li><span style=\"background-color:" + colors.seen.getValueAsString() + "\">Documents that have been looked at</span></li>" +
		"</ul></p></html>";
	}

	public Component getComponent() {
		return this;
	}

	public String getId() {
		return "tree";
	}

	public String getTitle() {
		return "Document Tree";
	}

	public String getTooltip() {
		return tooltip;
	}
}
