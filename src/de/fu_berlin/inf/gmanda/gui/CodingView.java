package de.fu_berlin.inf.gmanda.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import de.fu_berlin.inf.gmanda.gui.docking.DockableView;

/**
 * The coding view contains the CodeBox where the user can enter Tags about the
 * current PrimaryDocument
 */
public class CodingView extends JPanel implements DockableView {

	public CodingView(CodeBoxView codeBoxView) {

		setLayout(new BorderLayout(5, 5));

		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		add(codeBoxView);
	}

	public Component getComponent() {
		return this;
	}

	public String getId() {
		return "coding";
	}

	public String getTitle() {
		return "Coding View";
	}

	public String getTooltip() {
		return "<html>" + "The Coding View gives direct access<br>"
				+ "to the codes associated with an document</html>";
	}

}
