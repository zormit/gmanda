package de.fu_berlin.inf.gmanda.qda;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;

import com.thoughtworks.xstream.converters.ConversionException;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.imports.MyXStream;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;
import de.fu_berlin.inf.gmanda.util.progress.ProgressWriter;

public class ProjectLoader {

	MyXStream stream;
	CommonService common;

	public ProjectLoader(MyXStream stream, CommonService common) {
		this.stream = stream;
		this.common = common;
	}

	public void jodaConvert(Project p) {
		for (PrimaryDocument pd : PrimaryDocument.getTreeWalker(p.rootPDs)) {
			if (pd.hasMetaData("date")) {
				try {
					Date d = new SimpleDateFormat().parse(pd.getMetaData("date"));
					pd.setMetaData("date", new DateTime(d).toString());
				} catch (ParseException e) {
					System.out.println(pd.getFilename() + " : " + pd.getMetaData("date"));
					pd.getMetaData().remove("date");
				}

			}
			if (pd.hasMetaData("lastseen")) {
				try {
					Date d = new SimpleDateFormat().parse(pd.getMetaData("lastseen"));
					pd.setMetaData("lastseen", new DateTime(d).toString());
				} catch (ParseException e) {
					System.out.println(pd.getFilename() + " : " + pd.getMetaData("lastseen"));
					pd.getMetaData().remove("lastseen");
				}

			}
		}
	}

	public void save(File f, Project p, IProgress parentProgress) throws IOException {

		parentProgress = (parentProgress == null ? common.getProgressBar("Saving Project " + f.getName()) : parentProgress);
		
		parentProgress.setScale(100);
		parentProgress.start();
		
		// One time date conversion to Joda Time
		// jodaConvert(p);
		
		ProjectData project = new ProjectData();
		
		try {
			IProgress convert = parentProgress.getSub(10);
			convert.setScale(p.rootPDs.size());
			convert.start();
			for (PrimaryDocument pd : p.rootPDs) {
				project.rootDocuments.add(PrimaryDocumentData.toData(pd));
				convert.work(1);
			}
			convert.done();

			IProgress write = parentProgress.getSub(90);
			stream.toXML(project, new ProgressWriter(f, write,
				(f.exists() ? (int) (f.length() / 100) : 1000000 / 100)));
		} finally {
			parentProgress.done();
		}
	}

	class UserCancelationException extends RuntimeException {

	}

	public Project load(final File fileToLoadFrom, IProgress progress) throws IOException {

		progress = (progress == null ? common.getProgressBar("Loading Project " + fileToLoadFrom.getName()) : progress);

		try {
			progress.start();

			Project result = new Project();
			result.saveFile = fileToLoadFrom;

			try {
				ProjectData data = stream.fromXML(fileToLoadFrom, progress);
				if (progress.isCanceled())
					return null;

				result.addRootPDDs(data.rootDocuments);
				return result;
			} catch (ConversionException e) {
				if (e.getCause() instanceof UserCancelationException)
					throw (UserCancelationException) e.getCause();
				else
					throw e;
			}
		} finally {
			progress.done();
		}

	}
}
