package de.fu_berlin.inf.gmanda.gui.visualisation;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.joda.time.DateTime;

import de.fu_berlin.inf.gmanda.qda.Code;
import de.fu_berlin.inf.gmanda.qda.CodedString;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PPaintContext;

public class PrimaryDocumentDot extends PNode implements Comparable<PrimaryDocumentDot> {

	PrimaryDocument pd;

	DateTime date;

	PPath circle;

	public void setSelected(boolean selected) {

		if (!selected && circle != null) {
			removeChild(circle);
			circle = null;
		}
		if (selected && circle == null) {
			circle = PPath.createEllipse(-10.0f, -6.5f, 20.0f, 20.f);
			circle.setPaint(null);
			circle.setStrokePaint(Color.RED);
			addChild(circle);
		}

	}

	public PrimaryDocumentDot(PrimaryDocument pd, CodedString colors, ColorMapper mapper) {
		super();
		this.pd = pd;

		date = pd.getDate();

		Point2D[] points = new Point2D[] { new Point2D.Double(0.0f, 7.0f),
			new Point2D.Double(-5.0f, 7.0f), new Point2D.Double(0.0f, 0.0f),
			new Point2D.Double(5.0f, 7.0f), new Point2D.Double(0.0f, 7.0f) };

		PPath p = PPath.createPolyline(points);

		boolean gotColor = false;

		CodedString pdCodes = pd.getCode();
		if (pdCodes != null) {
			for (Code c : colors.getAllCodes()) {
				if (c.matchesAny(pdCodes.getAllCodesDeep())) {
					String color = c.getValue();
					gotColor = true;
					p.setPaint(mapper.getColor(color, Color.LIGHT_GRAY));
					break;
				}
			}
		}
		if (!gotColor)
			p.setPaint(Color.LIGHT_GRAY);

		addChild(p);

	}

	public DateTime getDate() {
		return date;
	}

	public int compareTo(PrimaryDocumentDot other) {
		return this.getDate().compareTo(other.getDate());
	}

	@Override
	protected void paint(PPaintContext arg0) {

		AffineTransform t = arg0.getGraphics().getTransform();
		arg0.getGraphics().scale(1.0 / t.getScaleX(), 1.0);
		super.paint(arg0);
	}
}
