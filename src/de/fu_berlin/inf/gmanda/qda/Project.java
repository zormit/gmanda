package de.fu_berlin.inf.gmanda.qda;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import de.fu_berlin.inf.gmanda.gui.manager.UndoManagement;
import de.fu_berlin.inf.gmanda.gui.misc.LockManager;
import de.fu_berlin.inf.gmanda.imports.GmaneImporter;
import de.fu_berlin.inf.gmanda.imports.GmaneMboxFetcher;
import de.fu_berlin.inf.gmanda.util.StateChangeListener;
import de.fu_berlin.inf.gmanda.util.StateChangeNotifier;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;

public class Project {

	public File saveFile;

	public StateChangeNotifier<Project> globalChangeNotifier = new StateChangeNotifier<Project>();

	public StateChangeNotifier<Project> nonLocalChangeNotifier = new StateChangeNotifier<Project>();

	public StateChangeNotifier<PrimaryDocument> childChangeNotifier = new StateChangeNotifier<PrimaryDocument>();

	StateChangeListener<PrimaryDocument> childChangeListener = new StateChangeListener<PrimaryDocument>() {
		public void stateChangedNotification(PrimaryDocument t) {
			childChangeNotifier.notify(t);
		}
	};

	Map<String, Map<String, List<PrimaryDocument>>> metadata = new HashMap<String, Map<String, List<PrimaryDocument>>>();

	List<PrimaryDocument> rootPDs = new ArrayList<PrimaryDocument>();

	public Project() {
		// Hook up the Global Change Notifier to both nonLocal and
		// childChangeNotifier
		getGlobalChangeNotifier().chain(nonLocalChangeNotifier);
		getGlobalChangeNotifier().chain(childChangeNotifier, this);
	}

	/**
	 * 
	 * You should only call set SaveFile if you are sure that you have
	 * understood how locking works. See {@link UndoManagement#load()} and
	 * {@link UndoManagement#save()} and {@link LockManager}.
	 * 
	 * @param saveFile
	 */
	public void setSaveFile(File saveFile) {
		this.saveFile = saveFile;
		nonLocalChangeNotifier.notify(this);
	}

	public File getSaveFile() {
		return saveFile;
	}

	public void addRootPDDs(List<PrimaryDocumentData> pdds) {
		for (PrimaryDocument document : PrimaryDocumentData.toPrimaryDocuments(pdds)) {
			add(document);
		}
		nonLocalChangeNotifier.notify(this);
	}

	public Map<String, Map<String, List<PrimaryDocument>>> getMetaData() {
		return metadata;
	}

	public Map<String, PrimaryDocument> pathsToDocuments = new HashMap<String, PrimaryDocument>();

	public void add(PrimaryDocument pd) {

		if (pd.filename != null) {
			if (pathsToDocuments.containsKey(pd.filename)) {
				System.err.println("Warning: " + pd.filename + " is already referenced");
			}
			pathsToDocuments.put(pd.filename, pd);
		}

		if (pd.getParent() == null)
			rootPDs.add(pd);

		// Store Metadata for faster access
		for (Map.Entry<String, String> meta : pd.getMetaData().entrySet()) {
			if (!metadata.containsKey(meta.getKey())) {
				metadata.put(meta.getKey(), new HashMap<String, List<PrimaryDocument>>());
			}
			Map<String, List<PrimaryDocument>> map = metadata.get(meta.getKey());
			if (!map.containsKey(meta.getValue())) {
				map.put(meta.getValue(), new ArrayList<PrimaryDocument>());
			}
			List<PrimaryDocument> set = map.get(meta.getValue());
			if (!set.contains(pd))
				set.add(pd);
		}

		// Register change listener
		pd.getNonTextChangeNotifier().add(childChangeListener);

		// Recurse to children
		for (PrimaryDocument child : pd.getChildren()) {
			add(child);
		}

		childChangeNotifier.notify(pd);
	}

	/**
	 * Return a Notifier that Listeners can subscribe to if the want to be
	 * informed if anything in the project has changed. For more fine grained
	 * information use the getLocalChangeNotifier()
	 * 
	 * @return
	 */
	public StateChangeNotifier<Project> getGlobalChangeNotifier() {
		return globalChangeNotifier;
	}

	/**
	 * Return a Notifier that Listeners can subscribe to if the want to be
	 * informed if individual Codeables in this Project change.
	 * 
	 * @return
	 */
	public StateChangeNotifier<PrimaryDocument> getLocalChangeNotifier() {
		return childChangeNotifier;
	}

	/**
	 * Return a Notifier that Listeners can subscribe to if they want to be
	 * informed if there are any changes in the project that are not related to
	 * the codeables in this project.
	 * 
	 * @return
	 */
	public StateChangeNotifier<Project> getNonLocalChangeNotifier() {
		return nonLocalChangeNotifier;
	}

	CodeModel cm;

	public CodeModel getCodeModel() {
		if (cm == null)
			cm = new CodeModel(this);

		return cm;
	}

	public List<PrimaryDocument> getPrimaryDocuments() {
		return rootPDs;
	}

	public void refetch(PrimaryDocument root, IProgress progress,
		GmaneMboxFetcher gmaneMboxFetcher, GmaneImporter importer) {
		if (!(root.getParent() == null && rootPDs.contains(root)))
			throw new IllegalArgumentException("Can only refetch Root PDs");

		if (!root.hasMetaData("list"))
			throw new IllegalArgumentException("Root PD needs 'list' metadata to be set");

		progress.setScale(100);
		progress.start();

		try {

			String list = root.metadata.get("list");

			int min = Integer.MAX_VALUE;
			int max = Integer.MIN_VALUE;

			IProgress sub1 = progress.getSub(10, IProgress.ProgressStyle.DOUBLING);
			sub1.setScale(1000);
			sub1.start();

			HashMap<String, PrimaryDocument> pdmap = new HashMap<String, PrimaryDocument>();

			for (PrimaryDocument pd : PrimaryDocument.getTreeWalker(root)) {

				if (pd == root)
					continue;

				sub1.work(1);

				String otherList = pd.metadata.get("list");

				String id = pd.metadata.get("id");

				if ((otherList != null && !StringUtils.equals(list, otherList)) || id == null) {
					throw new RuntimeException("Cannot refetch list that has emails without id");
				}

				int number;

				try {
					number = Integer.parseInt(id);
				} catch (NumberFormatException e) {
					throw new RuntimeException(
						"Cannot refetch list that has emails without numerical id");
				}

				min = Math.min(min, number);
				max = Math.max(max, number);
				pdmap.put(id, pd);
			}
			sub1.done();

			if (min != Integer.MAX_VALUE) {

				// Fetch from Gmane to temporary mbox file
				File target = gmaneMboxFetcher.fetchToTemp(progress.getSub(45), list, min, max + 1);

				// Read mbox file
				List<PrimaryDocumentData> newPDDs = importer.importPrimaryDocuments(list, 1,
					target, progress.getSub(45), false);

				List<PrimaryDocument> newPDs = PrimaryDocumentData.toPrimaryDocuments(newPDDs);

				for (PrimaryDocument pd : PrimaryDocument.getTreeWalker(newPDs)) {

					String id = pd.metadata.get("id");

					if (id == null)
						continue;

					if (pdmap.containsKey(id)) {
						PrimaryDocument source = pdmap.get(id);
						if (pd.getCode() != null)
							throw new RuntimeException();
						pd.setCode(source.getCode());

						pdmap.remove(id);
					}
				}

				if (pdmap.isEmpty()
					|| (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Found "
						+ pdmap.size() + " documents that are not in refetch. Continue?"))) {
					root.children = newPDs;
					root.getNonTextChangeNotifier().notify(root);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			progress.done();
		}

	}

	public void removePD(PrimaryDocument pd) {

		if (pd.getParent() == null) {
			if (!rootPDs.remove(pd))
				return;
		} else {
			pd.getParent().getChildren().remove(pd);
		}

		if (cm != null)
			for (PrimaryDocument child : PrimaryDocument.getTreeWalker(pd)) {
				cm.remove(child);
			}

		globalChangeNotifier.notify(this);
	}
}
