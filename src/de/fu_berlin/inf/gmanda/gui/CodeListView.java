package de.fu_berlin.inf.gmanda.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import de.fu_berlin.inf.gmanda.gui.docking.DockableView;

public class CodeListView extends JPanel implements DockableView {

	public CodeListView(CodeList codeList, CodeListFilterTextBox textBox) {
		super();

		setLayout(new BorderLayout(5, 5));

		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		add(textBox, BorderLayout.PAGE_START);
		add(codeList, BorderLayout.CENTER);
	}

	public Component getComponent() {
		return this;
	}

	public String getId() {
		return "code";
	}

	public String getTitle() {
		return "Codes";
	}

	public String getTooltip() {
		return "<html>" + "The Code View shows all codes present in the current project.<br>"
			+ "You can filter by name, number of documents containing the code<br>"
			+ "and number of sub-codes by clicking on the column headers in the<br>" + "table."
			+ "</html>";
	}
}
