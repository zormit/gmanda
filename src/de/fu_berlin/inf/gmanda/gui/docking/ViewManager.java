package de.fu_berlin.inf.gmanda.gui.docking;

import gnu.inet.util.BASE64;

import java.awt.Component;
import java.awt.Window;
import java.io.IOException;
import java.util.Collections;

import bibliothek.extension.gui.dock.theme.EclipseTheme;
import bibliothek.gui.DockController;
import bibliothek.gui.DockFrontend;
import bibliothek.gui.DockStation;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.layout.PredefinedDockSituation;
import bibliothek.gui.dock.station.split.SplitDockGrid;
import bibliothek.gui.dock.util.DockProperties;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import de.fu_berlin.inf.gmanda.gui.docking.TooltipTabPainter.ToolTipGetter;

/**
 * 
 * Tooltip code was written by Benjamin Sigg from DockingFrames.
 */
public class ViewManager {

	PredefinedDockSituation dockSituation;

	SplitDockStation rootDockStation;

	DockFrontend dockFrontend;

	SplitDockGrid reset;

	Component rootComponent;

	BiMap<DockableView, Dockable> dockables = new HashBiMap<DockableView, Dockable>();

	public ViewManager(DockableView[] views) {
		dockSituation = new PredefinedDockSituation();
		rootDockStation = new SplitDockStation();
		dockSituation.put("station", rootDockStation);
		
		// Register all views
		for (DockableView view : views){
			register(view);
		}
	}

	public void toggleFullscreen() {

		Dockable newFullscreenDock = rootDockStation.getFrontDockable();

		if (newFullscreenDock == null) {
			if (rootDockStation.getFullScreen() != null)
				rootDockStation.setFullScreen(null);
		} else {
			Dockable current = rootDockStation.getFullScreen();
			if (current == newFullscreenDock)
				rootDockStation.setFullScreen(null);
			else
				rootDockStation.setFullScreen(newFullscreenDock);
		}
	}

	public void register(DockableView view) {
		if (!dockables.containsKey(view)) {
			Dockable dockable = new DefaultDockable(view.getComponent(), view.getTitle());

			dockSituation.put(view.getId(), dockable);

			dockables.put(view, dockable);
		}
	}

	public void showDockableView(DockableView v) {
		
		Dockable dockable = getDockable(v);
		
		if (dockable != null)
			rootDockStation.drop(dockable);
	}

	public Component getDockStationComponent(Window w) {

		if (rootComponent != null)
			return rootComponent;

		dockFrontend = new DockFrontend(w);
		EclipseTheme theme = new EclipseTheme();
		dockFrontend.getController().setTheme(theme);
		dockFrontend.addRoot(rootDockStation, "station");

		DockController controller = dockFrontend.getController();

		DockProperties props = controller.getProperties();
		props.set(EclipseTheme.TAB_PAINTER, new TooltipTabPainter(props
			.get(EclipseTheme.TAB_PAINTER), new ToolTipGetter() {
			public String getToolTip(Dockable d) {
				DockableView view = dockables.inverse().get(d);
				if (view != null) {
					return view.getTooltip();
				} else {
					return "";
				}
			}
		}));

		rootComponent = rootDockStation.getComponent();
		return rootComponent;
	}

	public void setResetLayout(SplitDockGrid grid) {
		reset = grid;
	}

	public boolean setLayout(SplitDockGrid grid) {
		rootDockStation.dropTree(grid.toTree());
		return true;
	}

	public String getLayout() {
		try {
			return new String(BASE64.encode(dockSituation.write(Collections.singletonMap("station",
				(DockStation) rootDockStation))));
		} catch (IOException e) {
			return null;
		}
	}

	public boolean setLayout(String s) {
		try {
			dockSituation.read(BASE64.decode(s.getBytes()));
			rootDockStation.setFullScreen(null);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void reset() {
		if (reset != null)
			setLayout(reset);
	}

	/**
	 * Returns the dockable previously registered with this component.
	 */
	public Dockable getDockable(DockableView view) {
		return dockables.get(view);
	}

	public void showPerspective(DockingPerspective perspective) {
		setLayout(perspective.getGrid());
	}
}
