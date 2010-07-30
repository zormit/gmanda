/*
 * Created on 05.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.tree.TreePath;

import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTree;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.imports.GmaneMboxFetcher;
import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

/**
 * Action for copying links to the archival address web-site where a
 * PrimaryDocument is from
 * 
 * For instance something such as
 * http://article.gmane.org/gmane.comp.bug-tracking.bugzilla.devel/6321
 */
public class CopyLinksAction extends AbstractAction {

	PrimaryDocumentTree tree;

	ForegroundWindowProxy windowProxy;

	CommonService commonService;

	public CopyLinksAction(ProjectProxy project,
			ForegroundWindowProxy windowProxy, PrimaryDocumentTree tree,
			CommonService progress, GmaneMboxFetcher fetcher) {
		super("Copy link to Gmane.org to clipboard");
		this.tree = tree;
		this.windowProxy = windowProxy;
		this.commonService = progress;

		project.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_R));
	}

	public static String getArchiveURL(PrimaryDocument pd) {

		// This is the preferred and official way to get an Archive URL
		String url = pd.getMetaData("Archived-At");
		if (url != null && url.trim().length() > 0)
			return url;

		// This is the legacy approach of reconstructing it from the ID and
		// list.
		String list = pd.getMetaData().get("list");

		if (list == null)
			list = pd.getListGuess();

		if (list == null)
			return null;

		if (!list.startsWith("gmane."))
			list = "gmane." + list;

		String id = pd.getMetaData("id");
		if (id == null) {
			// If no ID then we return the link to the list.
			return "http://news.gmane.org/" + list;
		} else {
			return "http://article.gmane.org/" + list + "/" + id;
		}
	}

	public void actionPerformed(ActionEvent arg0) {

		TreePath path = tree.getSelectionPath();

		if (path == null)
			return;

		Object o = path.getLastPathComponent();

		if (o == null || !(o instanceof PrimaryDocument))
			return;

		setClipboard(getArchiveURL((PrimaryDocument) o));
	}

	// This method writes a string to the system clipboard.
	// otherwise it returns null.
	public static void setClipboard(String s) {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
				new StringSelection(s), null);
	}
};
