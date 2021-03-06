package de.fu_berlin.inf.gmanda.gui.visualisation;

import java.awt.Color;

import org.joda.time.DateTimeConstants;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

public class MonthNode extends SemanticNode {
	
	public final static Color TOOLTIPCOLOR = new Color(255, 255, 128);
	
	public MonthNode(final int days, String name, int dayOfWeek) {
		super();
		
		PNode monthLabel = new PFixedText(name, true, true, false);
		PPath rect = PPath.createRectangle(0.0f, 0.0f, 24 * 60 * days, (float) monthLabel.getHeight() * 2);
		rect.setPaint(null);
		monthLabel.setOffset(rect.getWidth() / 2, 0.0);
		
		PNode monthNode = new PNode();
		monthNode.addChild(rect);
		monthNode.addChild(monthLabel);
		
		PNode dayNode = new PNode();
		for (int i = 0; i < days; i++){
			final int currentI = i;

			PPath dayRect = PPath.createRectangle((float)rect.getWidth() / days * currentI, (float) monthLabel.getHeight(), 24 * 60, (float) monthLabel.getHeight());
			if (dayOfWeek == DateTimeConstants.SATURDAY || dayOfWeek == DateTimeConstants.SUNDAY){
				dayRect.setPaint(TOOLTIPCOLOR);
			} else {
				dayRect.setPaint(null);
			}
			// Go to next day (dayOfWeek is 1-indexed)
			dayOfWeek = (dayOfWeek % 7) + 1;
			
			PNode dayLabel = new PFixedText("" + (i + 1), true, true, false);
			dayLabel.setOffset(rect.getWidth() / days * (currentI + 0.5), monthLabel.getHeight());
			dayNode.addChild(dayRect);
			dayNode.addChild(dayLabel);
		}
		PNode monthLabelForDays = new PFixedText(name, true, true, false);
		monthLabelForDays.setOffset(rect.getWidth() / 2, 0.0);
		dayNode.addChild(monthLabelForDays);

		setLargest(monthNode);
		add(0.01, dayNode);
	}
}
