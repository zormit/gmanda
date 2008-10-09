package de.fu_berlin.inf.gmanda.gui.search;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.preferences.CacheDirectoryProperty;
import de.fu_berlin.inf.gmanda.imports.GmaneFacade;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.StateChangeListener;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;

/**
 * Entry point for using Lucene.
 * 
 * Portions of this code was taken from the examples supplied with Lucene.
 */
public class LuceneFacade {

	CommonService ps;

	Directory directory;

	IndexSearcher isearcher;

	Analyzer analyzer;

	ProjectProxy project;
	
	GmaneFacade gmane;
	
	CacheDirectoryProperty cacheDirectory;

	public LuceneFacade(Configuration configuration, 
		CacheDirectoryProperty cacheDirectory, 
		CommonService ps, 
		ProjectProxy project,
		GmaneFacade gmane) {
		
		this.cacheDirectory = cacheDirectory;
		this.gmane = gmane;
		
		configuration.beforeCloseNotifier.add(new StateChangeListener<Configuration>() {
			public void stateChangedNotification(Configuration t) {
				try {
					if (isearcher != null)
						isearcher.close();

					if (directory != null)
						directory.close();

					isearcher = null;
					directory = null;

				} catch (IOException e) {
					// How stupid to make the close operation fail...
					e.printStackTrace();
				}
			}
		});
		this.ps = ps;

		analyzer = new StandardAnalyzer();

		File pdL = cacheDirectory.getValue();
		File storage = new File(pdL, "lucene");

		try {
			directory = FSDirectory.getDirectory(storage);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Make sure that a index is in place to be loaded (even if empty)
		try {
			IndexWriter iwriter = new IndexWriter(directory, analyzer);
			iwriter.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		
		try {
			isearcher = new IndexSearcher(directory);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class Hiterator implements Iterator<PrimaryDocument> {

		Iterator<Hit> hitIt;

		Project project;

		/**
		 * The call to hasNext will construct the next primary document, so we
		 * store this here for returning it later.
		 */
		PrimaryDocument next;

		
		public Hiterator(Project project, Hits hits) {
			
			@SuppressWarnings("unchecked")
			Iterator<Hit> hiterator = hits.iterator();
			
			hitIt = hiterator;
			this.project = project;
		}

		public boolean hasNext() {
			if (next != null)
				return true;

			while (hitIt.hasNext()) {
				Hit hit = (Hit) hitIt.next();
				String path;
				try {
					path = hit.getDocument().get("path");
				} catch (IOException e) {
					// We cannot handle an exception here!! What do they
					// think???
					throw new RuntimeException(e);
				}
				if (project.pathsToDocuments.containsKey(path)) {
					next = project.pathsToDocuments.get(path);
					return true;
				}
			}
			return false;
		}

		public PrimaryDocument next() {
			if (next != null) {
				PrimaryDocument toReturn = next;
				next = null;
				return toReturn;
			}
			if (!hasNext())
				throw new NoSuchElementException();

			assert next != null;

			return next();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public Iterator<PrimaryDocument> search(final Project project, String toSearch) {

		try {
			if (!isearcher.getIndexReader().isCurrent()) {
				isearcher.close();
				isearcher = new IndexSearcher(directory);
			}

			if (isearcher.maxDoc() == 0) {
				JOptionPane.showMessageDialog(ps.getForegroundWindowOrNull(),
					"The lucene index does not contain any documents.\n"
						+ "Run 'Tools -> Reindex using Lucene' first.");
				List<PrimaryDocument> result = Collections.emptyList();
				return result.iterator();
			}
			
			// Parse a simple query that searches for "text":
			QueryParser parser = new QueryParser("contents", new 
				StandardAnalyzer());
			parser.setDefaultOperator(QueryParser.AND_OPERATOR);
			Query query = parser.parse(toSearch);

			Hits hits = isearcher.search(query);

			Hiterator result = new Hiterator(project, hits);

			return result;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return emptyIterator();
	}
	
	public static <T> Iterator<T> emptyIterator(){
		List<T> result = Collections.emptyList(); 
		return result.iterator();
	}

	public void reindex(Iterable<PrimaryDocument> documents) {
		// Count documents in advance...
		int totalNumberOfDocuments = 0;
		Iterator<PrimaryDocument> it = documents.iterator();
		while (it.hasNext()) {
			totalNumberOfDocuments++;
			it.next();
		}

		IProgress progress = ps.getProgressBar("Indexing", totalNumberOfDocuments * 2);

		try {
			if (!isearcher.getIndexReader().isCurrent()) {
				isearcher.close();
				isearcher = new IndexSearcher(directory);
			}
			
			IndexWriter iwriter = new IndexWriter(directory, false, analyzer);
			iwriter.setMaxFieldLength(1000000);
			
			int current = 0;

			for (PrimaryDocument pd : documents) {

				String fileName = pd.getFilename();
				
				if (fileName == null)
					continue;
				
				File file = gmane.getFileForPD(false, pd);
				
				Term term = new Term("path", fileName);
				
				if (file == null || !file.exists())
					continue;
				
				boolean reindex;
				
				Hits hits = isearcher.search(new TermQuery(term));
				
				if (hits.length() > 1){
					reindex = true;
				} else {
					if (hits.length() == 1){
						Document doc = hits.doc(0);
					
						try {
							if (DateTools.stringToDate(doc.getField("lastupdate").stringValue()).compareTo(new Date(file.lastModified())) < 0) {
								reindex = true;
							} else {
								reindex = false;
							}
						} catch (java.text.ParseException e) {
							reindex = true;
						}
					} else {
						reindex = true;
					}
				}
				if (reindex){
					iwriter.deleteDocuments(term);
		
					Document doc = new Document();
					doc.add(new Field("path", fileName, Field.Store.YES, Field.Index.UN_TOKENIZED));
					doc.add(new Field("lastupdate", DateTools.dateToString(new Date(),
						DateTools.Resolution.MINUTE), Field.Store.YES, Field.Index.UN_TOKENIZED));
					doc.add(new Field("contents", new FileReader(file)));
					
					for (Entry<String, String> metadata : pd.getMetaData().entrySet()){
						doc.add(new Field(metadata.getKey(), metadata.getValue(), Field.Store.NO, Field.Index.TOKENIZED));
					}

					iwriter.addDocument(doc);
				}

				// Track progress
				current++;
				progress.work(1);
				progress.setNote("Indexing " + current + " / " + totalNumberOfDocuments);
				if (progress.isCanceled()) {
					iwriter.abort();
					return;
				}
			}
			progress.setNote("Optimizing");
			iwriter.optimize();
			progress.work(totalNumberOfDocuments);
			iwriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			progress.done();
		}
	}

	public void reindex(Project variable) {
		reindex(PrimaryDocument.getTreeWalker(variable.getPrimaryDocuments()));
	}
}
