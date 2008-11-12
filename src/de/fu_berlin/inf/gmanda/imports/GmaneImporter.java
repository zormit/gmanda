package de.fu_berlin.inf.gmanda.imports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

import org.joda.time.DateTime;

import de.fu_berlin.inf.gmanda.gui.preferences.CacheDirectoryProperty;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocumentData;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;
import de.fu_berlin.inf.gmanda.util.tree.ChildrenableTreeWalker;

public class GmaneImporter {

	public static class ImportSettings {

		/**
		 * Name of the List to Fetch
		 * 
		 * for instance
		 * 
		 * "gmane.comp.security.oss.general"
		 */
		public String listName;

		/**
		 * The file in mbox format to read from or write to (depending on
		 * operation)
		 */
		public File mboxFile;

		/**
		 * Whether messages should be processed or only stored into the cache
		 * directory
		 */
		public boolean onlyStoreBodies;

		/**
		 * Begin (inclusive) and End (exclusive) of messages to fetch
		 * 
		 * For instance rangeStart==2 and rangeEnd==4 will include the messages
		 * 2 and 3.
		 * 
		 * Set to -1 to indicate all possible
		 */
		public int rangeStart, rangeEnd;

		/**
		 * Begin and end time in between (inclusive) which messages will be
		 * imported.
		 */
		public DateTime startDate, endDate;

		/**
		 * The directory from/to which message bodies will be read/written.
		 */
		public String cacheDirectory;

		public boolean isInDateRange(DateTime sentDate) {

			if (sentDate == null)
				return true;

			if (startDate != null && sentDate.isBefore(startDate))
				return false;

			if (endDate != null && sentDate.isAfter(endDate))
				return false;

			return true;
		}

		public boolean isInIdRange(int id) {
			if (rangeStart != -1 && id < rangeStart)
				return false;

			if (rangeEnd != -1 && rangeEnd < id)
				return false;

			return true;
		}

		public void initialize(IProgress progress, GmaneMboxFetcher fetcher) throws IOException {

			try {
				if (mboxFile == null) {
					mboxFile = File.createTempFile("gmanda", "mbox");
					mboxFile.deleteOnExit();
				}

				listName = listName.trim();
				if (!listName.startsWith("gmane."))
					listName = "gmane." + listName;

				if (rangeStart == -1) {
					rangeStart = 1;
				}

				if (rangeEnd == -1) {
					rangeEnd = fetcher.getEstimatedEndEmail(progress, listName, rangeEnd, null);
				}
			} finally {
				progress.done();
			}
		}
	}

	CacheDirectoryProperty cacheDirectory;

	public GmaneImporter(CacheDirectoryProperty cacheDirectory) {
		this.cacheDirectory = cacheDirectory;
	}

	public List<PrimaryDocumentData> importPrimaryDocuments(IProgress progress,
		ImportSettings settings) {

		if (settings.mboxFile == null)
			throw new IllegalArgumentException();

		PrimaryDocumentData root;
		try {
			root = loadGmane(progress, settings);
		} catch (MessagingException e) {
			// TODO report to user?
			progress.done();
			return null;
		}

		for (PrimaryDocumentData data : new ChildrenableTreeWalker<PrimaryDocumentData>(root)) {
			data.metadata.put("list", settings.listName);
		}

		List<PrimaryDocumentData> result = new LinkedList<PrimaryDocumentData>();

		result.add(root);

		progress.done();
		return result;
	}

	/**
	 * Extract ID from archived-at header.
	 * 
	 * @return -1 if not found.
	 */
	public static int getId(Message message) throws MessagingException {
		String[] archivedS = message.getHeader("Archived-At");

		if (archivedS == null || !archivedS[0].matches(".*(\\d+).*")) {
			return -1;
		}
		try {
			return Integer.parseInt(archivedS[0].replaceFirst("^.*/(?=[0-9]+)", "").replaceAll(
				"[^0-9]", ""));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public static String getMailingList(Message message) throws MessagingException {
		String[] archivedS = message.getHeader("Archived-At");

		if (archivedS == null)
			return null;

		Matcher m = Pattern.compile("/([^/]*?)/\\d+").matcher(archivedS[0]);
		if (m.find() && m.group(1).startsWith("gmane."))
			return m.group(1);
		else
			return null;
	}

	public static void fillMetaData(Properties meta, Message message, String listName, int id) {

		try {
			String subject = message.getSubject();
			String from = MyMimeUtils.getFrom(message);
			String to = MyMimeUtils.getRecipients(message, Message.RecipientType.TO);
			String cc = MyMimeUtils.getRecipients(message, Message.RecipientType.CC);
			String bcc = MyMimeUtils.getRecipients(message, Message.RecipientType.BCC);
			String date = (message.getSentDate() != null ? new DateTime(message.getSentDate())
				.toString() : "unknown");
			String list = getMailingList(message);

			// Store Metadata
			meta.put("id", Integer.toString(id));
			meta.put("subject", message.getSubject());
			meta.put("from", from);
			meta.put("date", date);
			meta.put("title", id + " - " + subject);
			meta.put("list", listName);

			if (to != null && to.trim().length() > 0) {
				meta.put("to", to);
			}
			if (cc != null && cc.trim().length() > 0) {
				meta.put("cc", cc);
			}
			if (bcc != null && bcc.trim().length() > 0) {
				meta.put("bcc", bcc);
			}
			if (list != null) {
				meta.put("list", list);
			}
		} catch (Exception e) {
			System.err.println(String.format("Error in Mail with ID '%d' while parsing metadata",
				id));
			e.printStackTrace(System.err);
		}

	}

	public static Pattern referencePattern = Pattern.compile("<.*?>");

	public PrimaryDocumentData loadGmane(IProgress progress, ImportSettings settings)
		throws MessagingException {

		String absolutePath = settings.mboxFile.getAbsolutePath();

		progress.setScale(100);
		progress.setNote("Loading Gmane " + absolutePath);
		progress.start();

		Folder folder = MyMimeUtils.getFolder(progress.getSub(50), settings.mboxFile);

		if (settings.cacheDirectory == null)
			settings.cacheDirectory = new File(cacheDirectory.getValue(), "/messages/")
				.getAbsolutePath();

		PrimaryDocumentData result = new PrimaryDocumentData();
		result.filename = null;
		result.children = messagesToPDs(progress.getSub(50), settings, Arrays.asList(folder
			.getMessages()));

		Store s = folder.getStore();
		folder.close(false);
		s.close();

		progress.done();

		return result;
	}

	public List<PrimaryDocumentData> messagesToPDs(IProgress progress, ImportSettings settings,
		List<Message> messages) throws MessagingException {

		List<PrimaryDocumentData> result = new LinkedList<PrimaryDocumentData>();

		Map<String, Message> ids = new HashMap<String, Message>();

		Map<Message, PrimaryDocumentData> pds = new HashMap<Message, PrimaryDocumentData>();

		Map<PrimaryDocumentData, PrimaryDocumentData> parents = new HashMap<PrimaryDocumentData, PrimaryDocumentData>();

		progress.setScale(100);
		progress.start();

		IProgress pStore = progress.getSub(80);
		pStore.setScale(messages.size());
		pStore.start();

		int previousId = Math.max(1, settings.rangeStart) - 1;

		for (Message message : messages) {

			pStore.work(1);

			PrimaryDocumentData child = new PrimaryDocumentData();

			int id = getId(message);

			if (id == -1) {
				id = previousId + 1;
				System.err.println(String.format(
					"  Could not find ID in message with estimated ID '%d'", id));
			}

			previousId = id;

			String messageBody = MyMimeUtils.getBody(message);

			fillMetaData(child.metadata, message, settings.listName, id);

			child.filename = "gmane://" + settings.listName + "." + id;

			// Need to write text to file
			try {
				File directory = new File(settings.cacheDirectory);
				directory.mkdirs();

				File target = new File(directory, settings.listName + "." + id + ".txt");
				PrintWriter pw = new PrintWriter(target);
				pw.write(messageBody);
				pw.close();
			} catch (FileNotFoundException e) {
				System.err
					.println("  " + child.filename + " could not save body:" + e.getMessage());
			}

			// Filter
			if (!settings.isInDateRange(new DateTime(message.getSentDate())) || !settings.isInIdRange(id)) {
				continue;
			}

			result.add(child);

			pds.put(message, child);

			String[] mids = message.getHeader("Message-ID");

			if (mids == null || mids.length == 0 || mids[0].trim().length() == 0) {
				System.out.println(Arrays.toString(mids));
				throw new RuntimeException();
			}

			String mid = mids[0];

			mid = mid.replaceAll("__\\d+\\.\\d+\\$\\d+\\$gmane\\$org", "");

			ids.put(mid, message);
		}
		pStore.done();

		if (settings.onlyStoreBodies) {
			result.clear();
			progress.done();
			return result;
		}

		IProgress pParent = progress.getSub(10);
		pParent.setScale(messages.size());
		pParent.start();

		for (Message message : messages) {

			pParent.work(1);

			if (!pds.containsKey(message)) {
				continue;
			}

			PrimaryDocumentData child = pds.get(message);

			String[] refs = message.getHeader("References");

			if (refs == null)
				continue;

			if (refs.length != 1 || refs[0].trim().length() == 0) {
				System.err.println("  " + child.filename + " reference integrity failed");
			}

			// Get last reference
			String reference = null;
			Scanner s = new Scanner(refs[0].trim());
			while (s.hasNext(referencePattern)) {
				reference = s.next();
			}

			if (reference != null && ids.containsKey(reference)) {
				PrimaryDocumentData parent = pds.get(ids.get(reference));

				if (child != parent && !recursiveContains(child, parent)) {
					parents.put(child, parent);
					parent.children.add(child);
				} else {
					System.err.println("  " + child.filename + " parent/child integrity failed");
				}
			}
		}
		pParent.done();

		IProgress pChildren = progress.getSub(10);
		pChildren.setScale(messages.size());
		pChildren.start();
		ListIterator<PrimaryDocumentData> it = result.listIterator();
		while (it.hasNext()) {
			pChildren.work(1);
			if (parents.containsKey(it.next())) {
				it.remove();
			}
		}
		pChildren.done();
		progress.done();
		return result;
	}

	public static boolean recursiveContains(PrimaryDocumentData parent, PrimaryDocumentData child) {

		if (parent.children.contains(child))
			return true;

		for (PrimaryDocumentData childOfParent : parent.children) {
			if (recursiveContains(childOfParent, child))
				return true;
		}
		return false;

	}

}
