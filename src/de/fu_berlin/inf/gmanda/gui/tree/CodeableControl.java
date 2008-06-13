package de.fu_berlin.inf.gmanda.gui.tree;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class CodeableControl extends JPanel {

	JButton resetButton = new JButton(new AbstractAction("Update") {
		public void actionPerformed(ActionEvent arg0) {
			tree.update();
		}
	});
	
	CodeableTree tree;
	
	public CodeableControl(
		final CodeableTree tree) {
		super();
		
		this.tree = tree;
				
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(		
			layout.createSequentialGroup()
		 		.addComponent(resetButton));
		
		layout.setVerticalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(resetButton));
	}
}
