package de.fu_berlin.inf.gmanda.gui.search;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class SearchPanel extends JPanel {

	public class Button extends JButton {
		public Button(Action a, String path, String name){
			super(a);
			setBorderPainted(false);
			setMinimumSize(new Dimension(24,24));
			setPreferredSize(new Dimension(28,28));
			setPreferredSize(new Dimension(28,28));
			setFocusPainted(false);
			setFocusable(false);
			setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(path + name)));
			setDisabledIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(path + "disabled/" + name)));
		}
	}

	public SearchPanel(
		ProjectProxy projectProxy, 
		RepeatSearchAction repeatSearchAction,
		final SearchField searchField) {
		
		setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		
		JLabel label = new JLabel("Highlight:");
		
		final JButton resetButton = new JButton(new AbstractAction("Reset"){
			public void actionPerformed(ActionEvent arg0) {
				searchField.setText("");
			}
		});
		
		projectProxy.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				resetButton.setEnabled(newValue != null);
			}
		});
		
		// Hook up actions
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
			KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK), repeatSearchAction);
		getActionMap().put(repeatSearchAction, repeatSearchAction);
	
		// Layout component
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(		
			layout.createSequentialGroup()
				.addComponent(label)
		 		.addComponent(searchField, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE,  Short.MAX_VALUE)
		 		.addComponent(resetButton));
		
		layout.setVerticalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(label)
				.addComponent(searchField)
				.addComponent(resetButton));
	}
}
