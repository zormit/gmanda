package de.fu_berlin.inf.gmanda.imports;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.gui.preferences.CacheDirectoryProperty;
import de.fu_berlin.inf.gmanda.imports.GmaneImporter.ImportSettings;
import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;
import de.fu_berlin.inf.gmanda.util.progress.NestableProgressMonitor;
import de.fu_berlin.inf.gmanda.util.tree.TreeAcceptor;
import de.fu_berlin.inf.gmanda.util.tree.TreeWalker;

public class GmaneFacade {

	GmaneImporter importer;
	GmaneMboxFetcher fetcher;
	CacheDirectoryProperty cache;
	ForegroundWindowProxy foreground;

	public GmaneFacade(GmaneImporter importer, GmaneMboxFetcher fetcher,
			CacheDirectoryProperty cache, ForegroundWindowProxy foreground) {
		this.importer = importer;
		this.fetcher = fetcher;
		this.cache = cache;
		this.foreground = foreground;
	}

	public boolean isGmaneIdentifier(String s) {

		if (s == null)
			return false;

		if (!s.startsWith("gmane://")) {
			return false;
		}

		s = s.substring("gmane://".length());

		if (s.indexOf('.') == -1)
			return false;

		s = s.substring(StringUtils.lastIndexOf(s, '.') + 1);

		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public String getListFromGmaneIdentifier(String s) {
		s = s.substring("gmane://".length());
		return s.substring(0, StringUtils.lastIndexOf(s, '.'));
	}

	public int getNumberFromGmaneIdentifier(String s) {
		s = s.substring("gmane://".length());
		s = s.substring(StringUtils.lastIndexOf(s, '.') + 1);
		return Integer.parseInt(s);
	}

	public void makeAllAvailable(Iterable<PrimaryDocument> pds, IProgress pm) {
		int i = 0;

		pm.setScale(100);
		pm.start();

		Iterator<PrimaryDocument> it = pds.iterator();
		while (it.hasNext()) {
			it.next();
			i++;
		}

		pm.work(5);

		IProgress pToFetch = pm.getSub(15);
		pToFetch.setScale(i);
		pToFetch.start();

		int chunksToFetch = 0;
		HashMap<String, TreeSet<Integer>> toMakeAvailable = new HashMap<String, TreeSet<Integer>>();
		try {
			for (PrimaryDocument pd : pds) {

				pToFetch.work(1);

				if (!isGmaneIdentifier(pd))
					continue;

				if (isAvailable(pd))
					continue;

				String list = getListFromGmaneIdentifier(pd.getFilename());
				int number = getNumberFromGmaneIdentifier(pd.getFilename());
				int from = ((number - 1) / GmaneMboxFetcher.CHUNKSIZE)
						* GmaneMboxFetcher.CHUNKSIZE + 1;

				if (!toMakeAvailable.containsKey(list))
					toMakeAvailable.put(list, new TreeSet<Integer>());

				if (!toMakeAvailable.get(list).contains(from)) {
					toMakeAvailable.get(list).add(from);
					chunksToFetch++;
				}

				if (pToFetch.isCanceled())
					return;
			}
		} finally {
			pToFetch.done();
		}

		if (chunksToFetch == 0) {
			pm.done();
			return;
		}

		IProgress pFetching = pm.getSub(60);
		pFetching.setScale(chunksToFetch * 2);
		pFetching.start();
		try {
			for (Map.Entry<String, TreeSet<Integer>> entry : toMakeAvailable
					.entrySet()) {
				String listName = entry.getKey();

				for (Integer from : entry.getValue()) {
					int to = from + GmaneMboxFetcher.CHUNKSIZE;
					System.out.println(String.format("Downloading %s %d to %d",
							entry.getKey(), from, to));
					
					try {
						ImportSettings settings = new ImportSettings();
						settings.listName = listName;
						settings.rangeStart = from;
						settings.rangeEnd = to + 1;
						settings.onlyStoreBodies = true;
						
						fetcher.fetch(pFetching.getSub(1), settings);
						
						if (pFetching.isCanceled())
							return;
						
						importer.importPrimaryDocuments(pFetching.getSub(1), settings);
						
						if (pFetching.isCanceled())
							return;
						
						settings.mboxFile.delete();
					} catch (Exception e) {
						throw new ReportToUserException(e);
					}
				}
			}
		} finally {
			pFetching.done();
		}

		IProgress pVerify = pm.getSub(20);
		pVerify.setScale(i);

		try {
			for (PrimaryDocument pd : pds) {

				pVerify.work(1);
				if (pVerify.isCanceled())
					return;

				if (!isGmaneIdentifier(pd))
					continue;

				if (isAvailable(pd))
					continue;

				// Ah this is bad
				String list = getListFromGmaneIdentifier(pd.getFilename());
				int number = getNumberFromGmaneIdentifier(pd.getFilename());
				System.out.println(String.format("Failed to refetch %s %d",
						list, number));
			}
		} finally {
			pVerify.done();
		}
		pm.done();
	}

	public void printStatistics(Project p, IProgress pm) {

		pm.setScale(100);
		pm.start();

		int numberOfPds = 0;
		for (@SuppressWarnings("unused") PrimaryDocument pd : PrimaryDocument.getTreeWalker(p
				.getPrimaryDocuments())) {
			numberOfPds++;
		}
		System.out.println("# of PDs: " + numberOfPds);
		System.out.println("# of Codes: " + p.getCodeModel().getList().size());
		
		pm.work(5);

		IProgress pToFetch = pm.getSub(75);
		pToFetch.setScale(numberOfPds);
		pToFetch.start();

		int nLists = 0;
		
		int ntEmails = 0;
		int ntEmailsAvailable = 0;
		int ntSeen = 0;
		int ntCoded = 0;
		
		int ntThreads = 0;
		int ntSeenThreads = 0;
		int ntCodedThreads = 0;
		
		int ntMailsInCodedThread = 0;
		int ntMailsInSeenThread = 0;
		
		TreeAcceptor<PrimaryDocument> seen = new TreeAcceptor<PrimaryDocument>(){
			public boolean accept(PrimaryDocument t) {
				return t.hasMetaData("lastseen") || t.getCodeAsString() != null;
			}
		};

		TreeAcceptor<PrimaryDocument> coded = new TreeAcceptor<PrimaryDocument>(){
			public boolean accept(PrimaryDocument t) {
				return t.getCodeAsString() != null	&& t.getCodeAsString().trim().length() > 0;
			}
		};

		System.out.println(String.format("%-50s\t%10s\t%10s\t%10s\t%10s\t%10s\t%10s\t%10s\t%10s\t%10s", 
				"Name of List", 
				"ntEmails", 
				"ntAvailable",
				"ntSeen",
				"ntCoded",
				"ntThreads",
				"ntCodedTh",
				"ntSeenTh",
				"ntMailsCT",
				"ntMailsST"));
		
		try {
			for (PrimaryDocument list : p.getPrimaryDocuments()) {
				nLists++;

				int nEmails = 0;
				int nEmailsAvailable = 0;
				int nSeen = 0;
				int nCoded = 0;
				
				int nThreads = 0;
				int nSeenThreads = 0;
				int nCodedThreads = 0;
				
				int nMailsInCodedThread = 0;
				int nMailsInSeenThread = 0;
				
				for (PrimaryDocument pd : PrimaryDocument.getTreeWalker(list)) {

					if (isGmaneIdentifier(pd))
						nEmails++;

					if (isAvailable(pd))
						nEmailsAvailable++;

					if (seen.accept(pd))
						nSeen++;

					if (coded.accept(pd))
						nCoded++;

					pToFetch.work(1);
					if (pToFetch.isCanceled())
						return;
				}
				
				for (PrimaryDocument pd : list.getChildren()) {

					nThreads++;
					
					TreeWalker<PrimaryDocument> t = PrimaryDocument.getTreeWalker(pd); 
					
					if (t.find(seen) != null){
					  nSeenThreads++;
					  nMailsInSeenThread += t.size();
					}
					  
					if (t.find(coded) != null){
						nCodedThreads++;
						nMailsInCodedThread += t.size();
					}
				}
				
				System.out.println(String.format("%-50s\t%10d\t%10d\t%10d\t%10d\t%10d\t%10d\t%10d\t%10d\t%10d", 
						list.getListGuess(), 
						nEmails, 
						nEmailsAvailable,
						nSeen,
						nCoded,
						nThreads,
						nCodedThreads,
						nSeenThreads,
						nMailsInCodedThread,
						nMailsInSeenThread));
				
				ntEmails += nEmails;
				ntEmailsAvailable += nEmailsAvailable;
				ntSeen += nSeen;
				ntCoded += nCoded;
				ntThreads += nThreads;
				ntCodedThreads += nCodedThreads;
				ntSeenThreads += nSeenThreads;
				ntMailsInCodedThread += nMailsInCodedThread;
				ntMailsInSeenThread += nMailsInSeenThread;
			}
			System.out.println(String.format("%-50s\t%10d\t%10d\t%10d\t%10d\t%10d\t%10d\t%10d\t%10d\t%10d", 
					"All lists", 
					ntEmails, 
					ntEmailsAvailable,
					ntSeen,
					ntCoded,
					ntThreads,
					ntCodedThreads,
					ntSeenThreads,
					ntMailsInCodedThread,
					ntMailsInSeenThread));
		} finally {
			pToFetch.done();
		}
		
		pm.done();
	}

	public boolean isGmaneIdentifier(PrimaryDocument pd) {
		return isGmaneIdentifier(pd.getFilename());
	}

	public boolean isAvailable(PrimaryDocument pd) {
		String filename = pd.getFilename();

		if (filename == null)
			return false;
		
		if (isGmaneIdentifier(filename)) {
			filename = filename.substring("gmane://".length());
			File fromCache = new File(new File(cache.getValue(), "messages"),
					filename + ".txt");
			return fromCache.exists();
		} else {
			return new File(filename).exists();
		}
	}

	public void makeAvailable(PrimaryDocument pd, IProgress pm) {

		pm.setScale(100);
		pm.start();
		pm.setNote(pd.getFilename());

		try {
			if (!isGmaneIdentifier(pd))
				return;

			if (isAvailable(pd))
				return;

			String list = getListFromGmaneIdentifier(pd.getFilename());
			int number = getNumberFromGmaneIdentifier(pd.getFilename());

			int from = ((number - 1) / GmaneMboxFetcher.CHUNKSIZE)
					* GmaneMboxFetcher.CHUNKSIZE + 1;
			int to = from + GmaneMboxFetcher.CHUNKSIZE;

			System.out.println(String.format("Fetching %s %d to %d", list,
					from, to));

			try {
				ImportSettings settings = new ImportSettings();
				settings.listName = list;
				settings.rangeStart = from;
				settings.rangeEnd = to + 1;
				settings.onlyStoreBodies = true;
				
				fetcher.fetch(pm.getSub(50), settings);
				importer.importPrimaryDocuments(pm.getSub(50), settings);
				settings.mboxFile.delete();
				
			} catch (Exception e) {
				throw new ReportToUserException(e);
			}

			if (!isAvailable(pd))
				System.out.println(String.format("Failed to fetch %s %d",
						list, number));

		} finally {
			pm.done();
		}
	}

	public File getFileForPD(boolean refetchIfNotFound, PrimaryDocument pd) {

		String filename = pd.getFilename();

		if (filename.startsWith("gmane://")) {
			filename = filename.substring("gmane://".length());

			File fromCache = new File(new File(cache.getValue(), "messages"),
					filename + ".txt");

			if (fromCache.exists()) {
				return fromCache;
			} else {
				if (refetchIfNotFound) {
					// Not in cache -> Refetch
					NestableProgressMonitor pm = new NestableProgressMonitor(
							foreground.getAsFrameOrNull(), "Refetching...");
					makeAvailable(pd, pm);

					return getFileForPD(false, pd);
				} else {
					return null;
				}
			}
		} else {
			return new File(filename);
		}
	}

	public String getText(PrimaryDocument pd) {
		File file = getFileForPD(true, pd);

		try {
			if (file == null) {
				return "An error occurred while making this document available.";
			}
			return FileUtils.readFileToString(file);

		} catch (Exception e) {
			return e.getMessage();
		}
	}
}
