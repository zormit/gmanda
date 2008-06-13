package de.fu_berlin.inf.gmanda.gui.visualisation;

import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PPaintContext;

public class PFixedText extends PText {
	
	public final boolean center; 
	
	public PFixedText(String text){
		this(text, false, true, true);
	}
	
	boolean xScale;
	boolean yScale;
	
	public PFixedText(String text, boolean center, boolean xScale, boolean yScale){
		super(text);
		this.center = center;
		this.xScale = xScale;
		this.yScale = yScale;
	}
	
	protected void paint(PPaintContext paintContext) {
		PAffineTransform p = new PAffineTransform();
		
		p.scale(
			(xScale ? 1.0 / paintContext.getGraphics().getTransform().getScaleX() : 1.0),
			(yScale ? 1.0 / paintContext.getGraphics().getTransform().getScaleY() : 1.0));
		
		if (center)
			p.setOffset(-getWidth() / (xScale ? 2.0 * paintContext.getGraphics().getTransform().getScaleX() : 2.0), 0.0);

		paintContext.pushTransform(p);
		super.paint(paintContext);
		paintContext.popTransform(p);
	}

}
