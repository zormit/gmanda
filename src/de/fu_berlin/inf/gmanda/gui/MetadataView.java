/*
 * Created on 04.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui;

import javax.swing.JPanel;

public class MetadataView extends JPanel {
//
//	JComboBox combo = new JComboBox();
//
//	JButton noFilter = new JButton();
//	
//	Project currentProject = null;
//	
//	public MetadataView(ProjectProxy projectProxy, final MetadataList list) {
//		super();
//
//		setLayout(new BorderLayout());
//		
//		JPanel northPanel = new JPanel();
//		northPanel.setLayout(new BorderLayout());
//		northPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
//		northPanel.add(combo, BorderLayout.CENTER);
//		northPanel.add(noFilter, BorderLayout.LINE_END);
//		
//		noFilter.setAction(new AbstractAction("Reset"){
//			public void actionPerformed(ActionEvent arg0) {
//				list.setMetadata(null);
//			}
//		});
//		
//		add(northPanel, BorderLayout.NORTH);
//		add(list, BorderLayout.CENTER);
//
//		projectProxy.addAndNotify(new VariableProxyListener<Project>() {
//			
//			StateChangeListener<Project> scl = new StateChangeListener<Project>(){
//				public void stateChangedNotification(Project t) {
//					if (t != null)
//						combo.setModel(new DefaultComboBoxModel(new Vector<Object>(t.getMetaData().keySet())));
//					else 
//						combo.setModel(new DefaultComboBoxModel());
//				}
//			};
//			
//			public void setVariable(Project newProject) {
//				
//				if (currentProject != null)
//					currentProject.getNonLocalChangeNotifier().remove(scl);
//				
//				currentProject = newProject;
//				
//				if (newProject != null)
//					newProject.getNonLocalChangeNotifier().add(scl);
//				
//				scl.stateChangedNotification(newProject);
//			}
//		});
//		
//		combo.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent arg0) {
//				list.setMetadata(currentProject.getMetaData().get((String)combo.getSelectedItem()));
//			}
//		});
//	}
}
