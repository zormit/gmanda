package de.fu_berlin.inf.gmanda.gui.preferences;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;

public class PreferenceWindow extends JFrame {

	@Inject
	CommonService commonService;
	
	public void showPreferences(){
		setLocationRelativeTo(commonService.getForegroundWindowOrNull());

		for (PreferenceUI<?> ui : preferenceUIs){
			ui.refresh();
		}
		
		setVisible(true);
	}

	public void hidePreferences() {

		for (PreferenceUI<?> ui : preferenceUIs){
			ui.store();
		}
		
		setVisible(false);
	}
	
	List<PreferenceUI<?>> preferenceUIs;
	
	@SuppressWarnings("unchecked")
	public PreferenceWindow(List<PreferenceUI> preferenceUIs, 
		ForegroundWindowProxy foreground) {
		super("Preferences");
		
		this.preferenceUIs = (List)preferenceUIs;

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

		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
		
		for (PreferenceUI<?> ui : preferenceUIs){
			center.add(ui.getPreferenceUI());
		}
		master.add(center, BorderLayout.PAGE_START);
		
		JPanel bottomRow = new JPanel();
		bottomRow.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
		bottomRow.add(cancel);
		bottomRow.add(ok);
		master.add(bottomRow, BorderLayout.PAGE_END);

		getContentPane().add(master);

		pack();
	}
}