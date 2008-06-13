package de.fu_berlin.inf.gmanda.gui.visualisation;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import com.thoughtworks.xstream.XStream;

import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.StateChangeListener;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class VisualisationOptions extends JPanel {

	JPopupMenu recentSearches = new JPopupMenu();
	
	LinkedList<VisualizationSetting> recentSearchStrings = new LinkedList<VisualizationSetting>();
	
	public void buildRecentSearches(){
		recentSearches.removeAll();
		for (final VisualizationSetting i : recentSearchStrings){
			recentSearches.add(new AbstractAction(i.toString()){
				public void actionPerformed(ActionEvent arg0) {
					updateRecentSearchStrings(i);
					fillFields(i);
				}
			});
		}
	}
	
	public void fillFields(VisualizationSetting i) {

		filterField.setText(i.filter);
		partitionField.setText(i.partition);
		rankField.setText(i.rank);
		colorField.setText(i.color);
	}
	
	public void updateRecentSearchStrings(VisualizationSetting o) {
		ListIterator<VisualizationSetting> it = recentSearchStrings.listIterator();
		while (it.hasNext()){
			if (it.next().equals(o))
				it.remove();
		}
		recentSearchStrings.add(0, o);
	}
	
	JTextField filterField = new JTextField(85);
	JTextField partitionField = new JTextField(85);
	JTextField rankField = new JTextField(85);
	JTextField colorField = new JTextField(85);
	
	@SuppressWarnings("unchecked")
	public VisualisationOptions(ProjectProxy projectProxy, final VisualizationCanvas pane,
		final Configuration configuration) {
		
		setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		
		JLabel filterLabel = new JLabel("Filter:");
		JLabel partitionLabel = new JLabel("Partition:");
		JLabel rankLabel = new JLabel("Rank:");
		JLabel colorLabel = new JLabel("Color:");

		configuration.beforeCloseNotifier.add(new StateChangeListener<Configuration>() {
			public void stateChangedNotification(Configuration arg0) {
				
				XStream xstream = new XStream();
				
				configuration.setProperty("VisualizationPresets", xstream.toXML(recentSearchStrings));
			}
		});

		String options = configuration.getProperty("VisualizationPresets", "error");// 
		if (options.equals("error")){
			recentSearchStrings.add(
				new VisualizationSetting("episode","episode","n","propose.*=green, announce.*=yellow, deadend.*=red"));
		} else {

			try {
				XStream xstream = new XStream();
				recentSearchStrings = (LinkedList<VisualizationSetting>) xstream.fromXML(options);
			} catch (Exception e) {
				recentSearchStrings.add(new VisualizationSetting("episode","episode","n","propose.*=green, announce.*=yellow, deadend.*=red"));
			}
		}
		
		if (recentSearchStrings.size() > 0){
			fillFields(recentSearchStrings.get(0));
		}
		
		final JButton drawButton = new JButton(new AbstractAction("Draw"){
			public void actionPerformed(ActionEvent arg0) {
				
				updateRecentSearchStrings(new VisualizationSetting(
					filterField.getText(),
					partitionField.getText(),
					rankField.getText(), 
					colorField.getText()));
				pane.update(filterField.getText(), partitionField.getText(), rankField.getText(), colorField.getText());
			}
		});
		
		final JButton recentSearchesButton = new JButton("Recent");
		
		recentSearchesButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				buildRecentSearches();
				recentSearches.show(recentSearchesButton, e.getX()-5, e.getY()-5);
			}
		});
		
		projectProxy.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				drawButton.setEnabled(newValue != null);
			}
		});
		
		// Layout component
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(		
			layout.createSequentialGroup()
				.addComponent(filterLabel)
		 		.addComponent(filterField, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
		 		.addComponent(partitionLabel)
		 		.addComponent(partitionField, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE,  Short.MAX_VALUE)
		 		.addComponent(rankLabel)
		 		.addComponent(rankField, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE,  Short.MAX_VALUE)
		 		.addComponent(colorLabel)
		 		.addComponent(colorField, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
		 		.addComponent(drawButton)
		 		.addComponent(recentSearchesButton));
		
		layout.setVerticalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(filterLabel)
				.addComponent(filterField)
				.addComponent(partitionLabel)
				.addComponent(partitionField)
				.addComponent(rankLabel)
		 		.addComponent(rankField)
		 		.addComponent(colorLabel)
				.addComponent(colorField)
				.addComponent(drawButton)
				.addComponent(recentSearchesButton));
	}
}
