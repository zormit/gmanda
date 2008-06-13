package de.fu_berlin.inf.gmanda.gui.visualisation;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

public class SemanticGrid extends SemanticNode {
	
	public static PPath createEmptyRect(float left, float top, float right, float bottom){
		PPath result = PPath.createRectangle(left, top, right-left, bottom-top);
		result.setPaint(null);
		return result;
	}
	
	public static Float[] toSteps(float startX, float stepX, int numberOfSteps){
		
		Float[] steps = new Float[numberOfSteps + 1];
		steps[0] = startX;
		for (int i = 1; i < steps.length; i++){
			steps[i] = steps[i-1] + stepX;
		}
	
		return steps;
	}
	
	public SemanticGrid(float startX, float stepX, int numberOfSteps, float yMin, float yMax, boolean transpose, float scale){
		this(toSteps(startX, stepX, numberOfSteps), yMin, yMax, transpose, scale);
	}
	
	public SemanticGrid(Float[] xSteps, float yMin, float yMax, boolean transpose, float scale){ 
		float xMin = xSteps[0];
		float xMax = xSteps[xSteps.length-1];
		
		if (!transpose)
			setLargest(createEmptyRect(xMin, yMin, xMax, yMax));
		else 
			setLargest(createEmptyRect(yMin, xMin, yMax, xMax));
		
		PNode grid = new PNode();
		
		for (int i = 0; i < xSteps.length - 1; i++) {
			if (!transpose)
				grid.addChild(createEmptyRect(xSteps[i], yMin, xSteps[i+1], yMax));
			else 
				grid.addChild(createEmptyRect(yMin, xSteps[i], yMax, xSteps[i+1]));
		}
		
		add(scale, grid);
	}
}