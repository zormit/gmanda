/*
 * Created on 25.12.2004
 *
 */
package de.fu_berlin.inf.gmanda.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import bibliothek.gui.DockStation;
import de.fu_berlin.inf.gmanda.gui.actions.ExitAction;
import de.fu_berlin.inf.gmanda.gui.docking.DefaultPerspective;
import de.fu_berlin.inf.gmanda.gui.docking.ViewManager;
import de.fu_berlin.inf.gmanda.gui.menu.MainWindowMenuBar;
import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.startup.Stoppable;
import de.fu_berlin.inf.gmanda.util.CUtils;
import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.StateChangeListener;

public class MainFrame extends JFrame implements Stoppable {

	DockStation station;

	public MainFrame(MainWindowMenuBar menu,
		final ExitAction exitAction, 
		final Configuration configuration, ForegroundWindowProxy foreground,
		final ViewManager manager, DefaultPerspective perspective) {

		super("Gmanda - GMANE Mailinglist Qualitative Editor");
		
		List<Image> icons = new LinkedList<Image>();
		icons.add(CUtils.loadImageResource("resources/gmanda128.png"));
		icons.add(CUtils.loadImageResource("resources/gmanda64.png"));
		icons.add(CUtils.loadImageResource("resources/gmanda32.png"));
		icons.add(CUtils.loadImageResource("resources/gmanda16.png"));

		setIconImages(icons);

		setPreferredSize(new Dimension(835, 720));
		setJMenuBar(menu);
		
		manager.setResetLayout(perspective.getGrid());
		
		// Handle Configuration
		configuration.beforeCloseNotifier.add(new StateChangeListener<Configuration>() {
			public void stateChangedNotification(Configuration arg0) {
				Rectangle bounds = getBounds();
				configuration.setProperty("MainWindow-Width", "" + bounds.width);
				configuration.setProperty("MainWindow-Height", "" + bounds.height);
				configuration.setProperty("MainWindow-Top", "" + bounds.y);
				configuration.setProperty("MainWindow-Left", "" + bounds.x);
				configuration.setProperty("MainWindow-Docking", manager.getLayout());
			}
		});

		int width = Integer.valueOf(configuration.getProperty("MainWindow-Width", "640"))
			.intValue();
		int height = Integer.valueOf(configuration.getProperty("MainWindow-Height", "480"))
			.intValue();
		int top = Integer.valueOf(configuration.getProperty("MainWindow-Top", "100"))
			.intValue();
		int left = Integer.valueOf(configuration.getProperty("MainWindow-Left", "100"))
			.intValue();

		String dockingConfiguration = configuration.getProperty("MainWindow-Docking", "");
		if (dockingConfiguration == null || dockingConfiguration.equals("")
			|| !manager.setLayout(dockingConfiguration)) {
			manager.reset();
		}
	
			
		{ // Take care of closing
			// Do not close the window by itself, but wait for stop to be called by GmandaMain.
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					exitAction.actionPerformed(null);
				}
			});
		}

		add(manager.getDockStationComponent(this));
		
		pack();

		foreground.setVariable(this);

		setBounds(left, top, width, height);
	}

	public void stop()  {
		dispose();
	}
}