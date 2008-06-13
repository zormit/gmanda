/*
 * Copyright (c) 2002-@year@, University of Maryland
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of the University of Maryland nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Piccolo was written at the Human-Computer Interaction Laboratory www.cs.umd.edu/hcil by Jesse Grosjean
 * under the supervision of Ben Bederson. The Piccolo website is www.cs.umd.edu/hcil/piccolo.
 */
package de.fu_berlin.inf.gmanda.gui.visualisation;

import java.awt.event.InputEvent;
import java.awt.geom.Point2D;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.util.PAffineTransform;

/**
 * Derived from PZoomEventHandler by Jesse Grosjean.
 */
public class XZoomEventHandler extends PDragSequenceEventHandler {
	
	private double minScale = 0;
	private double maxScale = Double.MAX_VALUE;
	private Point2D viewZoomPoint;

	/**
	 * Creates a new zoom handler.
	 */
	public XZoomEventHandler() {
		super();
		setEventFilter(new PInputEventFilter(InputEvent.BUTTON3_MASK));
	}

	//****************************************************************
	// Zooming
	//****************************************************************

	/**
	 * Returns the minimum view magnification factor that this event handler is bound by.
	 * The default is 0.
	 * @return the minimum camera view scale
	 */
	public double getMinScale() {
		return minScale;
	}

	/**
	 * Sets the minimum view magnification factor that this event handler is bound by.
	 * The camera is left at its current scale even if <code>minScale</code> is larger than
	 * the current scale.
	 * @param minScale the minimum scale, must not be negative.
	 */
	public void setMinScale(double minScale) {
		this.minScale = minScale;
	}

	/**
	 * Returns the maximum view magnification factor that this event handler is bound by.
	 * The default is Double.MAX_VALUE.
	 * @return the maximum camera view scale
	 */
	public double getMaxScale() {
		return maxScale;
	}

	/**
	 * Sets the maximum view magnification factor that this event handler is bound by.
	 * The camera is left at its current scale even if <code>maxScale</code> is smaller than
	 * the current scale. Use Double.MAX_VALUE to specify the largest possible scale. 
	 * @param maxScale the maximum scale, must not be negative.
	 */
	public void setMaxScale(double maxScale) {
		this.maxScale = maxScale;
	}

	protected void dragActivityFirstStep(PInputEvent aEvent) {
		viewZoomPoint = aEvent.getPosition();
		super.dragActivityFirstStep(aEvent);
	}

	protected void dragActivityStep(PInputEvent aEvent) {
		PCamera camera = aEvent.getCamera();
		double dx = aEvent.getCanvasPosition().getX() - getMousePressedCanvasPoint().getX();
		double scaleDelta = (1.0 + (0.001 * dx));

		double currentScale = camera.getViewScale();
		double newScale = currentScale * scaleDelta;

		if (newScale < minScale) {
			scaleDelta = minScale / currentScale;
		}
		if ((maxScale > 0) && (newScale > maxScale)) {
			scaleDelta = maxScale / currentScale;
		}

		
		double x = viewZoomPoint.getX();
		double y = viewZoomPoint.getY();
		PAffineTransform t = camera.getViewTransformReference();
		t.translate(x, y);
		t.scale(scaleDelta, 1.0);
		t.translate(-x, -y);
		// Just call so everybody gets informed about the change
		camera.scaleViewAboutPoint(1.0, 0.0, 0.0);
	}
	
	//****************************************************************
	// Debugging - methods for debugging
	//****************************************************************

	/**
	 * Returns a string representing the state of this node. This method is
	 * intended to be used only for debugging purposes, and the content and
	 * format of the returned string may vary between implementations. The
	 * returned string may be empty but may not be <code>null</code>.
	 *
	 * @return  a string representation of this node's state
	 */
	protected String paramString() {
		StringBuffer result = new StringBuffer();

		result.append("minScale=" + minScale);
		result.append(",maxScale=" + maxScale);
		result.append(",viewZoomPoint=" + (viewZoomPoint == null ? "null" : viewZoomPoint.toString()));
		result.append(',');
		result.append(super.paramString());

		return result.toString();
	}	
}
