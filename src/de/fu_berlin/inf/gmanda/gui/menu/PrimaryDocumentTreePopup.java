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
import de.fu_berlin.inf.gmanda.gui.actions.CopyLinksAction;
import de.fu_berlin.inf.gmanda.gui.actions.DeletePrimaryDocumentAction;
import de.fu_berlin.inf.gmanda.gui.actions.ExportMessageIDHyperLinks;
import de.fu_berlin.inf.gmanda.gui.actions.RefetchAction;
import de.fu_berlin.inf.gmanda.gui.actions.RefetchListAction;
import de.fu_berlin.inf.gmanda.gui.actions.RefetchRecursiveAction;
import de.fu_berlin.inf.gmanda.gui.actions.ReindexWithLuceneTreeAction;
import de.fu_berlin.inf.gmanda.gui.actions.SocialNetworkThreadAction;

public class PrimaryDocumentTreePopup extends JPopupMenu  {

	public PrimaryDocumentTreePopup(
		final PrimaryDocumentTree tree, 
		RefetchAction refetch,
		CopyLinksAction copyLink,
		RefetchRecursiveAction refetchRecursive,
		RefetchListAction refetchList,
		DeletePrimaryDocumentAction delete,
		ComputeThreadStatisticsAction thread,
		ComputeEmailStatisticsAction email,
		SocialNetworkThreadAction network,
		ReindexWithLuceneTreeAction lucene,
		ExportMessageIDHyperLinks latexID) {

		add(refetch);
		add(refetchRecursive);
		add(refetchList);
		add(lucene);
		add(new JSeparator());
		add(thread);
		add(email);
		add(network);
		add(latexID);
		add(new JSeparator());
		add(copyLink);
		add(delete);

		tree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				maybeShowPopup(e);
			}

			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}

			private void maybeShowPopup(MouseEvent e) {
				if (e.isPopupTrigger()) {
					show(tree, e.getX(), e.getY());
				}
			}
		});
	}
}
