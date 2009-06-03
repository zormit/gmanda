package de.fu_berlin.inf.gmanda.gui.menu;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTree;
import de.fu_berlin.inf.gmanda.gui.actions.ComputeEmailStatisticsAction;
import de.fu_berlin.inf.gmanda.gui.actions.ComputeThreadStatisticsAction;
import de.fu_berlin.inf.gmanda.gui.actions.DeletePrimaryDocumentAction;
import de.fu_berlin.inf.gmanda.gui.actions.RefetchAction;
import de.fu_berlin.inf.gmanda.gui.actions.RefetchListAction;
import de.fu_berlin.inf.gmanda.gui.actions.RefetchRecursiveAction;
import de.fu_berlin.inf.gmanda.gui.actions.SocialNetworkThreadAction;

public class PrimaryDocumentTreePopup extends JPopupMenu  {

	public PrimaryDocumentTreePopup(
		final PrimaryDocumentTree tree, 
		RefetchAction refetch,
		RefetchRecursiveAction refetchRecursive,
		RefetchListAction refetchList,
		DeletePrimaryDocumentAction delete,
		ComputeThreadStatisticsAction thread,
		ComputeEmailStatisticsAction email,
		SocialNetworkThreadAction network) {

		add(refetch);
		add(refetchRecursive);
		add(refetchList);
		add(new JSeparator());
		add(thread);
		add(email);
		add(network);
		add(new JSeparator());
		add(delete);

		tree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					try {
						Robot robot = new java.awt.Robot();
						robot.mousePress(InputEvent.BUTTON1_MASK);
						robot.mouseRelease(InputEvent.BUTTON1_MASK);
					} catch (AWTException ae) {
						System.out.println(ae);
					}
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()){
					show(tree, e.getX(), e.getY());
				}
			}
		});
	}
}
