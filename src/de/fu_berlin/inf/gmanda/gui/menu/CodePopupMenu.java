package de.fu_berlin.inf.gmanda.gui.menu;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import de.fu_berlin.inf.gmanda.gui.CodeList;
import de.fu_berlin.inf.gmanda.gui.actions.RenameCodesAction;

public class CodePopupMenu extends JPopupMenu  {

	public CodePopupMenu(final CodeList list, RenameCodesAction rename) {

		add(rename);
		
		list.getTable().addMouseListener(new MouseAdapter() {
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
					show(list.getTable(), e.getX(), e.getY());
				}
			}
		});
	}

}
