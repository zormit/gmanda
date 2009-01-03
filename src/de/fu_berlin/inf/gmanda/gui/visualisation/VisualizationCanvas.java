package de.fu_berlin.inf.gmanda.gui.visualisation;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.gmanda.gui.search.SearchService;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.qda.CodedString;
import de.fu_berlin.inf.gmanda.qda.CodedStringFactory;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.HashMapUtils;
import de.fu_berlin.inf.gmanda.util.Pair;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolox.swing.PScrollPane;

public class VisualizationCanvas extends PScrollPane {

	@Inject
	SearchService searchService;
	
	ProjectProxy project;

	ColorMapper mapper;

	PCanvas canvas;

	PCamera cam;

	PNode root;

	PNode timeline, partition;

	TrackCompareManager trackCompareManager;

	SelectionProxy selection;

	public VisualizationCanvas(ProjectProxy project, ColorMapper mapper,
		TrackCompareManager trackCompareManager, final SelectionProxy selection) {

		getVerticalScrollBar().setUnitIncrement(10);
		
		setBorder(null);

		this.project = project;
		this.mapper = mapper;
		this.trackCompareManager = trackCompareManager;
		this.selection = selection;

		canvas = new PCanvas();

		cam = canvas.getCamera();

		root = new PNode();

		canvas.setPreferredSize(new Dimension(1000, 300));
		canvas.addInputEventListener(new PBasicInputEventHandler() {

			public void keyPressed(PInputEvent aEvent) {
				if (aEvent.getKeyCode() == KeyEvent.VK_UP) {
					PAffineTransform a = cam.getViewTransform();
					a = new PAffineTransform(a.getScaleX() * 1.1, 0.0, 0.0, a.getScaleY(), a
						.getTranslateX() * 1.1, a.getTranslateY());
					cam.animateViewToTransform(a, 0);
				}
				if (aEvent.getKeyCode() == KeyEvent.VK_DOWN) {
					PAffineTransform a = cam.getViewTransform();
					a = new PAffineTransform(a.getScaleX() * 0.9, 0.0, 0.0, a.getScaleY(), a
						.getTranslateX() * 0.9, a.getTranslateY());
					cam.animateViewToTransform(a, 0);
				}
				if (aEvent.getKeyCode() == KeyEvent.VK_LEFT) {
					PAffineTransform a = cam.getViewTransform();
					a.translate(-100.0, 0.0);
					cam.animateViewToTransform(a, 0);

				}
				if (aEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
					PAffineTransform a = cam.getViewTransform();
					a.translate(+100.0, 0.0);
					cam.animateViewToTransform(a, 0);
				}
			}

			public void mousePressed(PInputEvent event) {
				if (event.getClickCount() == 2) {
					resetView();
				}
				event.getInputManager().setKeyboardFocus(this);
			}
		});

		timeline = new PNode();
		partition = new PNode();

		canvas.setZoomEventHandler(null);
		canvas.addInputEventListener(new XZoomEventHandler());

		canvas.getCamera().addChild(timeline);
		canvas.getCamera().addChild(partition);

		setViewportView(canvas);

		/**
		 * Add Picking Support
		 */
		canvas.getCamera().addInputEventListener(new PBasicInputEventHandler() {
			public void mousePressed(PInputEvent event) {
				PNode p = event.getPickedNode();
				if ((p instanceof PPath && p.getParent() instanceof PrimaryDocumentDot)) {
					event.setHandled(true);
					selection.setVariable(((PrimaryDocumentDot) p.getParent()).pd);
				}
			}
		});
		
		canvas.getCamera().addPropertyChangeListener(PCamera.PROPERTY_VIEW_TRANSFORM, new PropertyChangeListener(){

			public void propertyChange(PropertyChangeEvent evt) {
				updateHeaders();
			}
		});
		
		selectionListener = new VariableProxyListener<Object>() {

			PrimaryDocument oldValue = null;

			public void setVariable(Object newValue) {
				if (newValue instanceof PrimaryDocument && oldValue != newValue) {

					if (oldValue != null) {
						List<PrimaryDocumentDot> dots = allDots.get(oldValue);
						if (dots != null){
							for (PrimaryDocumentDot dot : dots)
								dot.setSelected(false);
						}
					}
					
					oldValue = (PrimaryDocument) newValue;
				
					if (newValue != null) {
						List<PrimaryDocumentDot> dots = allDots.get(newValue);
						if (dots != null){
							for (PrimaryDocumentDot dot : dots)
								dot.setSelected(true);
						}
					}
				}
			}
		};

	}
	
	public PCanvas getCanvas(){
		return this.canvas;
	}
	
	VariableProxyListener<Object> selectionListener;
	
	double aspectRatio;

	public void resetView() {
		// PDebug.debugFullBounds = true;

		PBounds rBounds = root.getGlobalFullBounds();
		PBounds pBounds = partition.getGlobalFullBounds();
		PBounds tBounds = timeline.getGlobalFullBounds();
		PBounds vBounds = cam.getViewBounds();
		PAffineTransform vTrans = cam.getViewTransform();

		double scaleX = (vBounds.getWidth() * vTrans.getScaleX() - pBounds.getWidth() - 20.0)
			/ rBounds.getWidth();

		if (scaleX <= 0 || scaleX == Double.POSITIVE_INFINITY)
			scaleX = 1.0;

		cam.animateViewToTransform(new PAffineTransform(scaleX, 0.0, 0.0, 1.0,
			pBounds.getWidth() + 10.0, tBounds.getHeight() + 10.0), 1000);
	}

	public void updateHeaders() {
		AffineTransform a = cam.getViewTransform();

		timeline.setTransform(new AffineTransform(a.getScaleX(), 0.0, 0.0, 1.0, a.getTranslateX(),
			timeline.getY()));
		partition.setTransform(new AffineTransform(1.0, 0.0, 0.0, a.getScaleY(), partition.getX(),
			a.getTranslateY()));
	}

	static final float trackDistance = 10.0f;

	

	public HashMap<PrimaryDocument, List<PrimaryDocumentDot>> allDots;

	public void update(String filterCode, String partitionCode, String rank, String colorField) {

		Project project = this.project.getVariable();
		
		CodedString colors = CodedStringFactory.parse(colorField);

		root.removeAllChildren();
		timeline.removeAllChildren();
		partition.removeAllChildren();

		allDots = new HashMap<PrimaryDocument, List<PrimaryDocumentDot>>();
		
		List<PrimaryDocument> allEvents = searchService.filter(filterCode, null, project);
		
		if (allEvents.size() == 0){
			
			PNode noMatchLabel = new PFixedText("No matching results", true, true, false);
			PPath rect = PPath.createRectangle(0.0f, 0.0f, (float)(noMatchLabel.getWidth() * 5.0), (float) noMatchLabel.getHeight() * 2);
			rect.setPaint(null);
			rect.setStroke(null);
			noMatchLabel.setOffset(rect.getWidth() / 2, 0.0);
			root.addChild(rect);
			root.addChild(noMatchLabel);
			
		} else {
			List<Pair<String, List<PrimaryDocument>>> tracks = project.getCodeModel().partition(allEvents, partitionCode);
	
			if (!rank.trim().equals(""))
				Collections.sort(tracks, trackCompareManager.getComparator(rank));
	
			buildVisualization(tracks, colors);
			
			// Install selection listener to highlight the currently selected PD
			selection.remove(selectionListener);
			selection.addAndNotify(selectionListener);
		}

		canvas.getLayer().addChild(root);

		aspectRatio = cam.getHeight() / cam.getWidth();

		resetView();
	}

	public void buildVisualization(List<Pair<String, List<PrimaryDocument>>> tracks, CodedString colors) {

		double start = Long.MAX_VALUE;
		double end = Long.MIN_VALUE;

		for (Pair<String, List<PrimaryDocument>> p : tracks) {
			for (PrimaryDocument pd : p.v) {
				DateTime d = PrimaryDocumentDot.getDate(pd);
				if (d == null)
					continue;
				start = Math.min(start, d.getMillis());
				end = Math.max(end, d.getMillis());
			}
		}
		if (start == Long.MAX_VALUE || end == Long.MIN_VALUE){
			start = new DateTime().getMillis();
			end = start + 60000;
		}
		
		start = start / 1000 / 60;
		end = end / 1000 / 60;

		int trackId = 0;

		DateTime currentDate = new DateTime((long)(start * 1000 * 60)).monthOfYear().roundFloorCopy();
		DateTime startDate = currentDate;
		start = startDate.getMillis() / 1000 / 60;
		
		PNode dayGrids = new PNode();
		while (currentDate.getMillis() / 1000 / 60 < end){
			
			Interval month = new Interval(currentDate, currentDate.plus(Period.months(1)));
			
			int days = Days.daysIn(month).getDays();
			
			MonthNode monthNode = new MonthNode(days, currentDate.toString("MMM"), month.getStart().getDayOfWeek());
			monthNode.offset(currentDate.getMillis() / 1000 / 60 - start, 0.0);
			
			SemanticGrid dayGrid = new SemanticGrid(0.0f, 24.0f * 60, days, 0.0f, trackDistance * 2 * tracks.size(), false, 0.05f); 
			dayGrid.offset(currentDate.getMillis() / 1000 / 60 - start, 0.0);
			dayGrids.addChild(dayGrid);
			
			timeline.addChild(monthNode);
						
			currentDate = currentDate.plus(Period.months(1));
		}
		SemanticNode grid = new SemanticNode();
		float startMillis = (float)(startDate.getMillis() / 1000 / 60 - start);
		float width = (float)(currentDate.getMillis() / 1000 / 60 - start) - startMillis;
		PPath all = PPath.createRectangle(startMillis, 0.0f, width, trackDistance * 2 * tracks.size());
		all.setPaint(null);
		grid.setLargest(all);
		grid.add(0.0005, dayGrids);
		root.addChild(grid);
		
		List<PNode> allTextNodes = new ArrayList<PNode>(tracks.size());
		
		double partitionOffset = 0.0;
		double currentNoTimeOffset = 0.0;
		
		HashMap<PrimaryDocument, Double> noTimeOffsets = new HashMap<PrimaryDocument, Double>();
		
		for (Pair<String, List<PrimaryDocument>> p : tracks) {

			PNode track = new PNode();
			track.offset(0.0, trackId++ * trackDistance);
			root.addChild(track);

			PText text = new PFixedText(p.p, false, false, true);
			partitionOffset = Math.max(partitionOffset, text.getWidth());
			text.offset(0.0, trackId++ * trackDistance - 0.5 * text.getHeight());
			partition.addChild(text);
			allTextNodes.add(text);

			for (PrimaryDocument pd : p.v) {

				PrimaryDocumentDot dot = new PrimaryDocumentDot(pd, colors, mapper);

				DateTime date = dot.getDate();
				if (date != null){
					dot.offset(date.getMillis() / 1000 / 60 - start, 10.0f);
				} else {
					if (noTimeOffsets.containsKey(pd)){
						dot.offset(noTimeOffsets.get(pd), 10.0f);
					} else {
						dot.offset(currentNoTimeOffset, 10.0f);
						noTimeOffsets.put(pd, currentNoTimeOffset);
						
						currentNoTimeOffset += 6.0 * 60.0;
					}
				}

				track.addChild(dot);
				HashMapUtils.putList(allDots, pd, dot);
			}
		}
		
		for (PNode text : allTextNodes) {
			text.offset(partitionOffset - text.getWidth(), 0.0);
		}
	
		partition.setX(0.0);
	}

	
}