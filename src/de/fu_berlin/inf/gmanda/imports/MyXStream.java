package de.fu_berlin.inf.gmanda.imports;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

import com.thoughtworks.xstream.XStream;

import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.imports.FileVersion.GmpXStream;
import de.fu_berlin.inf.gmanda.qda.ProjectData;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;
import de.fu_berlin.inf.gmanda.util.progress.ProgressInputStream;

public class MyXStream {

	public void toXML(ProjectData obj, Writer out) {
		FileVersion.V4.save(obj, out);
	} 

	/**
	 * @throws RuntimeException
	 *             wrapping the causing file exception if no ProjectData could
	 *             be loaded from the file (for instance the file could not be
	 *             found)
	 */
	public ProjectData fromXML(File f, IProgress progress) {

		ProjectData result = null;

		progress.setScale(100);
		progress.start();

		try {
			IProgress detectProgress = progress.getSub(5);

			FileVersion version;
			try {
				version = probeFileVersion(f, detectProgress);
			} finally {
				detectProgress.done();
			}
			result = version.load(f, progress.getSub(95));

		} finally {
			progress.done();
		}
		return result;
	}

	private FileVersion probeFileVersion(File f, IProgress detectProgress) {

		XStream x = new GmpXStream();

		InputStream in;
		FileVersion version;
		try {
			in = new ProgressInputStream(f, detectProgress);

			version = FileVersion.getVersionInfo(x
				.createObjectInputStream(new InputStreamReader(in)));
		} catch (IOException e1) {
			throw new ReportToUserException(e1);
		}

		detectProgress.done();
		return version;
	}

	// @Override
	// public void toXML(Object obj, OutputStream out) {
	// if (COMPACT){
	// marshal(obj, new CompactWriter(new OutputStreamWriter(out)));
	// } else {
	// 
	// Some code for compaction:
	// if (COMPACT) {
	// x.marshal(obj, new CompactWriter(out));
	// } else {
}