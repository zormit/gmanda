package de.fu_berlin.inf.gmanda.imports;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

import de.fu_berlin.inf.gmanda.gui.preferences.CacheDirectoryProperty;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;
import de.fu_berlin.inf.gmanda.util.progress.ProgressInputStream;
import de.fu_berlin.inf.gmanda.util.progress.IProgress.ProgressStyle;

public class GmaneMboxFetcher {

	CacheDirectoryProperty cacheDirectory;

	public GmaneMboxFetcher(CacheDirectoryProperty cacheLocation) {
		this.cacheDirectory = cacheLocation;

	}

	public final static int CHUNKSIZE = 500;

	public int getEstimatedStartEmail(IProgress progress, String mailingList, Calendar c) {

		progress.setScale(20);
		progress.setStyle(ProgressStyle.ROTATING);
		progress.start();

		int result = 1;

		while (true) {

			Message m;
			try {
				m = getSingleMail(progress.getSub(1), mailingList, result);

				if (m == null && result > 10000) {
					return 1;
				}

				if (m != null && (m.getSentDate() == null || m.getSentDate().after(c.getTime()))) {
					return Math.max(1, result - CHUNKSIZE);
				}
			} catch (Exception e) {
				return Math.max(1, result - CHUNKSIZE);
			}

			result += CHUNKSIZE;
		}
	}

	public int getEstimatedEndEmail(IProgress progress, String mailingList, int start) {

		progress.setScale(20);
		progress.setStyle(ProgressStyle.ROTATING);
		progress.start();

		int result = start;

		while (true) {

			Message m;
			try {
				m = getSingleMail(progress.getSub(1), mailingList, result);
				if (m == null) {
					return result;
				}
			} catch (Exception e) {
				return result;
			}

			result += CHUNKSIZE;
		}
	}

	public void fetch(IProgress progress, String mailingList, File target, int from, int to)
		throws IOException {

		progress.setScale(100);
		progress.start();

		if (from == -1) {
			Calendar startOfYear07 = Calendar.getInstance();
			startOfYear07.set(2007, 0, 1);
			from = getEstimatedStartEmail(progress.getSub(10), mailingList, startOfYear07);
		}

		if (to == -1) {
			to = getEstimatedEndEmail(progress.getSub(10), mailingList, from);
		}

		IProgress fetchProgress = progress.getSub(80);
		fetchProgress.setScale(to - from);
		fetchProgress.start();

		PrintWriter pw = new PrintWriter(target);
		try {
			for (int i = from; i < to; i += CHUNKSIZE) {
				fetchProgress.setNote(String.format("Fetching Email #%d", i));
				appendFetch(fetchProgress.getSub(CHUNKSIZE), pw, mailingList, i, Math.min(i
					+ CHUNKSIZE, to));
			}
		} finally {
			pw.close();
		}
		fetchProgress.done();

		progress.done();
	}

	public static URL getGmaneURL(String list, int from, int to) {

		if (!list.startsWith("gmane."))
			list = "gmane." + list;

		try {
			return new URL(String.format("http://download.gmane.org/%s/%d/%d", list, from, to));
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public void appendFetch(IProgress progress, PrintWriter pw, String list, int from, int to)
		throws IOException {

		progress.start();

		if (!list.startsWith("gmane."))
			list = "gmane." + list;

		try {
			File storage = new File(cacheDirectory.getValue(), "mboxes");
			storage.mkdirs();

			File mbox = new File(storage, list + "-" + from + "-" + to + ".mbox");
			if (mbox.exists()) {

				BufferedReader in = new BufferedReader(new InputStreamReader(
					new ProgressInputStream(new FileInputStream(mbox), progress, 10000)));

				String line;
				while ((line = in.readLine()) != null) {
					pw.write(line);
					pw.write('\n');
				}
				in.close();
				return;
			}

			progress.setScale(100);
			IProgress subProgress1 = progress.getSub(75);

			URL url = getGmaneURL(list, from, to);

			BufferedReader in = new BufferedReader(new InputStreamReader(new ProgressInputStream(
				url.openStream(), subProgress1, 10000)));

			PrintWriter writeToMbox = new PrintWriter(mbox);

			int lines = 0;
			String line;
			while ((line = in.readLine()) != null) {

				lines++;

				if (line.matches("^From .*$")
					&& !line.matches("^From .*? .*[0-9][0-9]:[0-9][0-9]:[0-9][0-9].*$")) {
					line = ">" + line;
				}

				writeToMbox.write(line);
				writeToMbox.write('\n');
			}
			in.close();
			writeToMbox.close();

			appendFetch(progress.getSub(25), pw, list, from, to);

		} finally {
			progress.done();
		}
	}

	public File fetchToTemp(IProgress pm, String mailingList, int from, int to) throws IOException {
		File target = File.createTempFile("gmanda", "mbox");
		target.deleteOnExit();
		fetch(pm, mailingList, target, from, to);
		return target;
	}

	public Message getSingleMail(IProgress pm, String mailingList, int number)
		throws MessagingException, IOException {

		pm.setScale(100);
		pm.start();

		File tempFile = fetchToTemp(pm.getSub(50), mailingList, number, number + 1);
		Folder folder = MyMimeUtils.getFolder(pm.getSub(50), tempFile);
		tempFile.delete();

		if (folder == null || folder.getMessageCount() != 1)
			return null;

		Message message;
		try {
			message = folder.getMessage(1);
		} finally {
			Store s = folder.getStore();
			folder.close(false);
			s.close();
		}
		pm.done();
		return message;
	}

	public List<Message> getMails(IProgress progress, String mailingList, int from, int to)
		throws MessagingException, IOException {

		progress.setScale(100);
		progress.start();

		File temp = fetchToTemp(progress.getSub(60), mailingList, from, to);
		Folder folder = MyMimeUtils.getFolder(progress.getSub(40), temp);
		temp.delete();

		if (folder == null || folder.getMessageCount() == 0)
			return null;

		try {
			return Arrays.asList(folder.getMessages());
		} finally {
			Store s = folder.getStore();
			folder.close(false);
			s.close();
		}
	}
}
