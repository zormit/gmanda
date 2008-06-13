package de.fu_berlin.inf.gmanda.gui.visualisation;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import de.fu_berlin.inf.gmanda.util.Pair;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PPaintContext;

public class SemanticNode extends PNode {

	List<Pair<Double, PNode>> zoomScales = new LinkedList<Pair<Double, PNode>>();
	
	public void add(double scale, PNode node){
		zoomScales.add(new Pair<Double, PNode>(scale, node));
		
		Comparator<Pair<Double, PNode>> c = Pair.pCompare();
		Collections.sort(zoomScales, c);
		
		setBounds(getBounds().createUnion(node.getFullBounds()));
	}
	
	public PNode finestZoom;
	
	@Override
	protected void paint(PPaintContext arg0) {
		super.paint(arg0);
		
		double scale = arg0.getScale();
		
		for (Pair<Double, PNode> node : zoomScales){
			if (node.p < scale){
				node.v.fullPaint(arg0);
				return;
			}
		}
		
		finestZoom.fullPaint(arg0);
	}
	
	public void setLargest(PNode largest){

		finestZoom = largest;
		
		setBounds(finestZoom.getFullBounds());
		
		zoomScales.clear();
	}
	
	public SemanticNode(){
		super();
	}
}
