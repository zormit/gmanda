package de.fu_berlin.inf.gmanda.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import de.fu_berlin.inf.gmanda.gui.docking.DockableView;
import de.fu_berlin.inf.gmanda.gui.search.SearchPanel;

public class PrimaryDocumentView extends JPanel implements DockableView {

	public PrimaryDocumentView(CodeBoxView codeBoxView, TextView textView, SearchPanel searchPanel) {

		setLayout(new BorderLayout(5, 5));

		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, codeBoxView,
			textView);
		splitPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		add(searchPanel, BorderLayout.PAGE_START);
		add(splitPane, BorderLayout.CENTER);
	}

	public Component getComponent() {
		return this;
	}

	public String getId() {
		return "view";
	}

	public String getTitle() {
		return "Document View";
	}

	public String getTooltip() {
		return "<html>" + "The Document View shows detail information about the currently<br>"
			+ "selected document from the Document Tree and allows you to write<br>"
			+ "codes for the document.</html>";
	}

}
