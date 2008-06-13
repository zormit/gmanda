package de.fu_berlin.inf.gmanda.gui.visualisation;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import de.fu_berlin.inf.gmanda.gui.docking.DockableView;

public class VisualizationPane extends JPanel implements DockableView {

	public VisualizationPane(VisualisationOptions options, VisualizationCanvas canvas) {
		
		setLayout(new BorderLayout());
		
		add(options, BorderLayout.NORTH);
		add(canvas, BorderLayout.CENTER);
		
	}

	public String getTitle() {
		return "Visualization";
	}

	public String getTooltip() {
		return "Visualization of primary document coding";
	}

	public Component getComponent() {
		return this;
	}

	public String getId() {
		return "vis";
	}
}