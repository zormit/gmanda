package de.fu_berlin.inf.gmanda.imports;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import de.fu_berlin.inf.gmanda.qda.PrimaryDocumentData;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;

public class PlainTextImporter implements Importer {

	public List<PrimaryDocumentData> importPrimaryDocuments(String filename, IProgress progress) {
		
		List<PrimaryDocumentData> result = new LinkedList<PrimaryDocumentData>();
		
		File file = new File(filename);
		progress.setScale(1);
		progress.setNote("Loading file " + filename);
		progress.start();

		PrimaryDocumentData document = new PrimaryDocumentData();
		document.code = "";
		document.filename = file.getAbsolutePath();
		progress.work(1);
		progress.done();
		
		result.add(document);
		return result;

	}

	

}
