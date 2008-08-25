package de.fu_berlin.inf.gmanda.gui.tabulation;

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

public class TabulationOptionsPanel extends JPanel {

	JPopupMenu recentSearches = new JPopupMenu();
	
	LinkedList<TabulationSettings> recentSearchStrings = new LinkedList<TabulationSettings>();
	
	public void buildRecentSearches(){
		recentSearches.removeAll();
		for (final TabulationSettings i : recentSearchStrings){
			recentSearches.add(new AbstractAction(i.toString()){
				public void actionPerformed(ActionEvent arg0) {
					updateRecentSearchStrings(i);
					fillFields(i);
				}
			});
		}
	}
	
	public void fillFields(TabulationSettings i) {

		x.setText(i.xDim);
		y.setText(i.yDim);
		by.setText(i.groupBy);
	}
	
	public void updateRecentSearchStrings(TabulationSettings o) {
		ListIterator<TabulationSettings> it = recentSearchStrings.listIterator();
		while (it.hasNext()){
			if (it.next().equals(o))
				it.remove();
		}
		recentSearchStrings.add(0, o);
	}
	
	JTextField x = new JTextField(85);
	JTextField y = new JTextField(85);
	JTextField by = new JTextField(85);
	
	ProjectProxy project;
	
	@SuppressWarnings("unchecked")
	public TabulationOptionsPanel(ProjectProxy projectProxy, 
		final Configuration configuration, final TabulationCanvas canvas) {
		
		project = projectProxy;
		
		setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		
		JLabel xLabel = new JLabel("X:");
		JLabel yLabel = new JLabel("Y:");
		JLabel byLabel = new JLabel("Group by:");
		

		configuration.beforeCloseNotifier.add(new StateChangeListener<Configuration>() {
			public void stateChangedNotification(Configuration arg0) {
				
				XStream xstream = new XStream();
				
				configuration.setProperty("TabulationPresets", xstream.toXML(recentSearchStrings));
			}
		});

		String options = configuration.getProperty("TabulationPresets", "error");// 
		if (options.equals("error")){
			recentSearchStrings.add(
				new TabulationSettings("activity","episode",""));
		} else {

			try {
				XStream xstream = new XStream();
				recentSearchStrings = (LinkedList<TabulationSettings>) xstream.fromXML(options);
			} catch (Exception e) {
				recentSearchStrings.add(new TabulationSettings("activity","episode",""));
			}
		}
		
		if (recentSearchStrings.size() > 0){
			fillFields(recentSearchStrings.get(0));
		}
		
		final JButton drawButton = new JButton(new AbstractAction("Update"){
			public void actionPerformed(ActionEvent arg0) {
				
				TabulationSettings t = new TabulationSettings(
					x.getText(),
					y.getText(),
					by.getText());
				
				updateRecentSearchStrings(t);
				
				canvas.update(t);
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
				.addComponent(xLabel)
		 		.addComponent(x, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
		 		.addComponent(yLabel)
		 		.addComponent(y, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE,  Short.MAX_VALUE)
		 		.addComponent(byLabel)
		 		.addComponent(by, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE,  Short.MAX_VALUE)
		 		.addComponent(drawButton)
		 		.addComponent(recentSearchesButton));
		
		layout.setVerticalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(xLabel)
				.addComponent(x)
				.addComponent(yLabel)
				.addComponent(y)
				.addComponent(byLabel)
		 		.addComponent(by)
				.addComponent(drawButton)
				.addComponent(recentSearchesButton));
	}

}