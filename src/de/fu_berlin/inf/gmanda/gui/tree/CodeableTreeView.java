package de.fu_berlin.inf.gmanda.gui.tree;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.fu_berlin.inf.gmanda.gui.docking.DockableView;

public class CodeableTreeView extends JPanel implements DockableView {

	public CodeableTreeView(CodeableControl textField,
		CodeableTree tree) {

		setLayout(new BorderLayout(5,5));
		
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		JScrollPane sTree = new JScrollPane(tree);
		sTree.setBorder(BorderFactory.createEmptyBorder());
		
		add(textField, BorderLayout.PAGE_START);
		add(sTree, BorderLayout.CENTER);
	}

	public String getTitle() {
		return "Codeable Tree";
	}

	public String getTooltip() {
		return "The codeable tree shows all things that can be coded";
	}

	public Component getComponent() {
		return this;
	}

	public String getId() {
		return "cTree";
	}
}
