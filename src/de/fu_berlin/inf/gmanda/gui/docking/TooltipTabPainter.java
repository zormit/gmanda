package de.fu_berlin.inf.gmanda.gui.docking;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.border.Border;

import bibliothek.extension.gui.dock.theme.eclipse.rex.RexTabbedComponent;
import bibliothek.extension.gui.dock.theme.eclipse.rex.tab.TabComponent;
import bibliothek.extension.gui.dock.theme.eclipse.rex.tab.TabPainter;
import bibliothek.gui.DockController;
import bibliothek.gui.DockStation;
import bibliothek.gui.Dockable;

public class TooltipTabPainter implements TabPainter {

	public interface ToolTipGetter {
		public String getToolTip(Dockable d);
	}
	
	TabPainter original;
	
	ToolTipGetter tooltipGetter;

	public TooltipTabPainter(TabPainter wrappedInstance, ToolTipGetter tooltipGetter) {
		this.original = wrappedInstance;
		this.tooltipGetter = tooltipGetter;
	}

	/*
	 * Set the tooltip on component and on its children, that should spread the
	 * tooltip wide enough such that it can be seen (note: that is a wild guess
	 * that might not work in newer versions).
	 */
	public static void setTooltip(Component component, String text) {
		if (component instanceof JComponent) {
			JComponent jcomp = (JComponent) component;
			jcomp.setToolTipText(text);

			for (int i = 0, n = jcomp.getComponentCount(); i < n; i++) {
				Component child = jcomp.getComponent(i);
				if (child instanceof JComponent) {
					((JComponent) child).setToolTipText(text);
				}
			}
		}
	}

	
	public TabComponent createTabComponent(DockController controller, RexTabbedComponent component,
		final Dockable dockable, int index) {

		final TabComponent comp = original.createTabComponent(controller, component, dockable,
			index);
		TabComponent result = new TabComponent() {
			public void bind() {
				comp.bind();
				/*
				 * here we could add some listener to dockable in order to be
				 * informed when the tooltip changes
				 * 
				 * "((MyDockable)dockable).addMyListener( ... );"
				 */
				setTooltip(getComponent(), tooltipGetter.getToolTip(dockable));
			}

			public void unbind() {
				/*
				 * And here we would remove the listener that was added in
				 * "bind".
				 */
				comp.unbind();
			}

			public void addMouseListener(MouseListener listener) {
				comp.addMouseListener(listener);
			}

			public void addMouseMotionListener(MouseMotionListener listener) {
				comp.addMouseMotionListener(listener);
			}

			public Component getComponent() {
				return comp.getComponent();
			}

			public Border getContentBorder() {
				return comp.getContentBorder();
			}

			public int getOverlap() {
				return comp.getOverlap();
			}

			public void removeMouseListener(MouseListener listener) {
				comp.removeMouseListener(listener);
			}

			public void removeMouseMotionListener(MouseMotionListener listener) {
				comp.removeMouseMotionListener(listener);
			}

			public void setFocused(boolean focused) {
				comp.setFocused(focused);
			}

			public void setIndex(int index) {
				comp.setIndex(index);
			}

			public void setPaintIconWhenInactive(boolean paint) {
				comp.setPaintIconWhenInactive(paint);
			}

			public void setSelected(boolean selected) {
				comp.setSelected(selected);
			}

			public void update() {
				comp.update();
			}
		};

		return result;
	}

	public Border getFullBorder(DockController controller, Dockable dockable) {
		return original.getFullBorder(controller, dockable);
	}

	public Border getFullBorder(DockController controller, DockStation station,
		RexTabbedComponent component) {
		return original.getFullBorder(controller, station, component);
	}

	public void paintTabStrip(RexTabbedComponent tabbedComponent, Component tabStrip, Graphics g) {
		original.paintTabStrip(tabbedComponent, tabStrip, g);
	}

}