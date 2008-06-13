package de.fu_berlin.inf.gmanda.gui.preferences;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.util.Configuration;

public class PreferenceWindow extends JFrame {

	Configuration configuration;
	
	ForegroundWindowProxy windowProxy;
	
	public void showPreferences(){
		setLocationRelativeTo(windowProxy.getAsFrameOrNull());
		
		occurance.setSelected(scrollOnShow.getValue());

		setVisible(true);
	}

	public void hidePreferences() {
		scrollOnShow.setValue(occurance.isSelected());
		
		setVisible(false);
	}
	
	JCheckBox occurance = new JCheckBox("Jump to first occurance of search term");
	
	ScrollOnShowProperty scrollOnShow;
	
	public PreferenceWindow(ScrollOnShowProperty scrollOnShow, 
		ForegroundWindowProxy foreground) {
		super("Preferences");
		
		this.scrollOnShow = scrollOnShow;
		this.windowProxy = foreground;

		setPreferredSize(new Dimension(600, 400));
		
		JButton cancel = new JButton(new AbstractAction("Cancel"){
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		
		JButton ok = new JButton(new AbstractAction("OK"){
			public void actionPerformed(ActionEvent e) {
				hidePreferences();
			}
		});
		
		JPanel master = new JPanel();
		master.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		master.setLayout(new BorderLayout(5, 5));

		master.add(occurance, BorderLayout.PAGE_START);
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
		panel.add(cancel);
		panel.add(ok);
		master.add(panel, BorderLayout.PAGE_END);

		getContentPane().add(master);

		pack();
	}
}