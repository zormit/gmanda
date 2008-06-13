package de.fu_berlin.inf.gmanda.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import de.fu_berlin.inf.gmanda.gui.docking.DockableView;

public class CodeDetailView extends JPanel implements DockableView {
	
	public CodeDetailView(CodeAsTextView view, CodeDetailBox box){
		
		setLayout(new BorderLayout(5, 5));

		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		add(box, BorderLayout.PAGE_START);
		add(view, BorderLayout.CENTER);
	}

	public Component getComponent() {
		return this;
	}

	public String getId() {
		return "dView";
	}

	public String getTitle() {
		return "Code Detail";
	}

	public String getTooltip() {
		return 		"<html>" +
		"The Code Detail View shows information<br>" +
		"about the currently filtered for code.</html>";
	}
}
