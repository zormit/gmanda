package de.fu_berlin.inf.gmanda.qda;

import java.io.PrintWriter;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;

import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import de.fu_berlin.inf.gmanda.imports.GmaneFacade;
import de.fu_berlin.inf.gmanda.imports.GmaneImporter;
import de.fu_berlin.inf.gmanda.imports.GmaneMboxFetcher;
import de.fu_berlin.inf.gmanda.imports.MyMimeUtils;
import de.fu_berlin.inf.gmanda.util.HashMapUtils;
import de.fu_berlin.inf.gmanda.util.StateChangeNotifier;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;
import de.fu_berlin.inf.gmanda.util.progress.NullProgressMonitor;
import de.fu_berlin.inf.gmanda.util.progress.IProgress.ProgressStyle;
import de.fu_berlin.inf.gmanda.util.tree.TreeMaker;
import de.fu_berlin.inf.gmanda.util.tree.TreeStructure;
import de.fu_berlin.inf.gmanda.util.tree.TreeWalker;

public class PrimaryDocument implements Comparable<PrimaryDocument>, Codeable {

	String filename;

	String code;

	StateChangeNotifier<Codeable> codeableNotifier = new StateChangeNotifier<Codeable>();

	StateChangeNotifier<PrimaryDocument> nonTextNotifier = new StateChangeNotifier<PrimaryDocument>();

	StateChangeNotifier<PrimaryDocument> textNotifier = new StateChangeNotifier<PrimaryDocument>();

	List<PrimaryDocument> children = new ArrayList<PrimaryDocument>();

	PrimaryDocument parent;

	public PrimaryDocument() {
		nonTextNotifier.chain(codeableNotifier, this);
	}

	public PrimaryDocument getParent() {
		return parent;
	}

	public String toString() {
		return getName();
	}

	public boolean hasMetaData(String key) {
		return metadata.containsKey(key);
	}

	public String getMetaData(String key) {
		return metadata.get(key);
	}

	public void setMetaData(String key, String value) {
		metadata.put(key, value);
		nonTextNotifier.notify(this);
	}

	public String getName() {
		if (metadata.containsKey("title"))
			return metadata.get("title");
		else if (metadata.containsKey("list"))
			return metadata.get("list");
		else
			return filename;
	}

	public String getFilename() {
		return filename;
	}

	public static String guessListFromFileName(String filename) {
		Matcher m = Pattern.compile("(comp\\.[a-z.]+?)\\.(?=\\d|mbox)")
				.matcher(filename);

		if (m.find()) {
			return "gmane." + m.group(1);
		}
		return null;
	}

	public String getListGuess() {

		PrimaryDocument pd = this;

		while (pd != null && pd.metadata.get("list") == null)
			pd = pd.getParent();

		if (pd != null)
			return pd.metadata.get("list");

		if (filename != null) {
			return guessListFromFileName(filename);
		}

		return null;
	}

	SoftReference<String> text = new SoftReference<String>(null);

	/**
	 * This is operation is potentially long running as it will refetch from the
	 * web, if the file is not available
	 * 
	 * @param gmane
	 * @return
	 */
	public String getText(GmaneFacade gmane) {
		if (filename == null)
			return "No text associated";

		String result = text.get();
		if (result == null) {
			result = gmane.getText(this);
			text = new SoftReference<String>(result);
		}
		return result;
	}

	Map<String, String> metadata = new HashMap<String, String>();

	public Map<String, String> getMetaData() {
		return metadata;
	}

	public List<PrimaryDocument> getChildren() {
		return children;
	}

	public StateChangeNotifier<PrimaryDocument> getNonTextChangeNotifier() {
		return nonTextNotifier;
	}

	public StateChangeNotifier<PrimaryDocument> getTextChangeNotifier() {
		return textNotifier;
	}

	CodedString cache;

	public CodedString getCode() {
		if (cache == null) {
			cache = CodedStringFactory.parse(getCodeAsString());
		}
		return cache;
	}

	public String getCodeAsString() {
		return code;
	}

	public void setCode(String s) {
		if (s != null && s.trim().length() == 0)
			s = null;

		cache = null;

		code = s;
		codeableNotifier.notify(this);
	}

	static TreeMaker<PrimaryDocument> treeMaker = null;

	public static TreeMaker<PrimaryDocument> getTreeMaker() {

		if (treeMaker == null)
			treeMaker = new TreeMaker<PrimaryDocument>() {
				public TreeStructure<PrimaryDocument> toStructure(
						final PrimaryDocument t) {
					return new TreeStructure<PrimaryDocument>() {
						public PrimaryDocument get() {
							return t;
						}

						public Collection<PrimaryDocument> getChildren() {
							return t.getChildren();
						}
					};
				}
			};
		return treeMaker;
	}

	public static TreeWalker<PrimaryDocument> getTreeWalker(
			PrimaryDocument primaryDocument) {
		return new TreeWalker<PrimaryDocument>(primaryDocument, getTreeMaker());
	}

	public static TreeWalker<PrimaryDocument> getTreeWalker(
			Collection<PrimaryDocument> primaryDocuments) {
		return new TreeWalker<PrimaryDocument>(primaryDocuments, getTreeMaker());
	}

	public void refetchRecursive(IProgress progress,
			GmaneMboxFetcher gmaneMboxFetcher) {
		progress.setScale(100);
		progress.start();

		try {

			String list = metadata.get("list");

			int min = Integer.MAX_VALUE;
			int max = Integer.MIN_VALUE;

			IProgress sub1 = progress.getSub(10,
					IProgress.ProgressStyle.DOUBLING);
			sub1.setScale(1000);
			sub1.start();

			HashMap<Integer, PrimaryDocument> pdmap = new HashMap<Integer, PrimaryDocument>();

			for (PrimaryDocument pd : PrimaryDocument.getTreeWalker(this)) {

				sub1.work(1);

				String otherList = pd.metadata.get("list");

				String id = pd.metadata.get("id");

				if ((otherList != null && !StringUtils.equals(list, otherList))
						|| id == null)
					continue;

				int number;

				try {
					number = Integer.parseInt(id);
				} catch (NumberFormatException e) {
					continue;
				}

				min = Math.min(min, number);
				max = Math.max(max, number);
				pdmap.put(number, pd);
			}
			sub1.done();

			if (pdmap.size() > 0) {
				try {
					List<Message> messages = gmaneMboxFetcher.getMails(progress
							.getSub(50), list, min, max + 1);

					IProgress sub3 = progress.getSub(25, ProgressStyle.NORMAL);
					sub3.setScale(messages.size());

					for (Message m : messages) {

						sub3.work(1);

						int mid = GmaneImporter.getId(m);

						PrimaryDocument document = pdmap.get(mid);
						if (document == null)
							continue;

						document.updateFromMessage(m, mid, list);
					}
					sub3.done();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		} finally {
			progress.done();
		}
	}

	public void updateFromMessage(Message m, int number, String list)
			throws Exception {
		text = new SoftReference<String>(null);

		String newBody = MyMimeUtils.getBody(m);

		PrintWriter pw = new PrintWriter(filename);
		pw.write(newBody);
		pw.close();

		Properties p = new Properties();
		GmaneImporter.fillMetaData(p, m, list, number);
		metadata = HashMapUtils.toHashMap(p);

		textNotifier.notify(this);
		nonTextNotifier.notify(this);
	}

	public void refetch(GmaneMboxFetcher gmaneMboxFetcher) {

		String list = metadata.get("list");

		String id = metadata.get("id");

		if (list == null || id == null)
			return;

		int number = Integer.parseInt(id);

		Message m;
		try {
			m = gmaneMboxFetcher.getSingleMail(new NullProgressMonitor(), list,
					number);

			if (m == null)
				return;

			updateFromMessage(m, number, list);

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public static Comparator<PrimaryDocument> getProjectSensitiveComparator(
			final Project project) {
		return new Comparator<PrimaryDocument>() {
			public int compare(PrimaryDocument o1, PrimaryDocument o2) {

				assert o1 != null && o2 != null;

				PrimaryDocument list1 = o1;
				PrimaryDocument list2 = o2;

				while (list1.getParent() != null)
					list1 = list1.getParent();

				while (list2.getParent() != null)
					list2 = list2.getParent();

				if (list1 == list2)
					return o1.compareTo(o2);

				return Integer.signum(project.getPrimaryDocuments().indexOf(
						list1)
						- project.getPrimaryDocuments().indexOf(list2));
			}
		};
	}

	@SuppressWarnings("unchecked")
	public int compareTo(PrimaryDocument other) {

		String myList = getListGuess();
		String otherList = other.getListGuess();

		// If one of the lists is null
		if (myList == null ^ otherList == null) {
			return ComparatorUtils.nullLowComparator(
					ComparatorUtils.naturalComparator()).compare(myList,
					otherList);
		}

		// If both are non-null
		if (myList != null && otherList != null) {
			int i;
			if ((i = myList.compareTo(otherList)) != 0)
				return i;
		}

		String myId = getMetaData("id");
		String otherId = other.getMetaData("id");

		if (myId == null) {
			return 1;
		}
		int myI, otherI;
		try {
			myI = Integer.parseInt(myId);
		} catch (NumberFormatException e) {
			return 1;
		}
		if (otherId == null)
			return -1;
		try {
			otherI = Integer.parseInt(otherId);
		} catch (NumberFormatException e) {
			return -1;
		}
		return Integer.signum(myI - otherI);
	}

	public static List<PrimaryDocument> filterChildren(
			List<PrimaryDocument> list) {

		List<PrimaryDocument> result = new ArrayList<PrimaryDocument>(list
				.size());

		// Filter all PDs whose parent is in the list as well
		codeable: for (PrimaryDocument c : list) {
			PrimaryDocument pd = c.getParent();

			while (pd != null) {
				if (list.contains(pd))
					continue codeable;
				pd = pd.getParent();
			}
			result.add(c);

		}
		return result;
	}

	public PrimaryDocument getMailingList() {
		if (getParent() == null)
			return this;

		return getParent().getMailingList();
	}

	public PrimaryDocument getThreadStart() {

		if (getParent() == null)
			return this;

		if (getParent().getParent() == null)
			return this;

		return getParent().getThreadStart();
	}

	public boolean renameCodes(String renameFrom, String renameTo) {

		CodedString codes = CodedStringFactory.parse(getCodeAsString());

		CodedString newCodedString = codes.rename(renameFrom, renameTo);

		if (codes != newCodedString) {
			setCode(newCodedString.toString());
			return true;
		} else {
			return false;
		}
	}

	public int countChildrenAndSelf() {
		int children = 1;

		for (PrimaryDocument pdc : this.getChildren()) {
			children += pdc.countChildrenAndSelf();
		}
		return children;
	}

	public boolean hasCode() {
		if (getCodeAsString() != null && getCodeAsString().trim().length() > 0) {
			return true;
		}

		for (PrimaryDocument pdc : getChildren()) {
			if (pdc.hasCode())
				return true;
		}
		return false;
	}

	public StateChangeNotifier<Codeable> getCodeChangeNotifier() {
		return codeableNotifier;
	}

	/**
	 * Returns the DateTime when this primary document was send or created as
	 * via the "date" meta-data field
	 * 
	 * @return null if this PD has no date in the given metaDataField or the
	 *         date is incorrectly formatted.
	 */
	public DateTime getDate(String metaDataField) {
		if (metadata.containsKey(metaDataField)) {
			try {
				return new DateTime(getMetaData(metaDataField));
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * Utility method for getDate("date");
	 */
	public DateTime getDate() {
		return getDate("date");
	}
}
