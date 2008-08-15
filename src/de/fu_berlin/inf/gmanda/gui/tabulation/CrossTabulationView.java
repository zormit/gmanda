package de.fu_berlin.inf.gmanda.gui.tabulation;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import de.fu_berlin.inf.gmanda.gui.docking.DockableView;

public class CrossTabulationView extends JPanel implements DockableView {

	public CrossTabulationView(TabulationCanvas canvas, TabulationOptionsPanel optionPanel) {

		setLayout(new BorderLayout(5, 5));

		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		add(optionPanel, BorderLayout.PAGE_START);
		add(canvas, BorderLayout.CENTER);
	}

	public Component getComponent() {
		return this;
	}

	public String getId() {
		return "tView";
	}

	public String getTitle() {
		return "Tabulation View";
	}

	public String getTooltip() {
		return "<html>" + "The Tabulation View helps to find intersection and<br>"
			+ "continencies when working with several codes" + "</html>";
	}
}
