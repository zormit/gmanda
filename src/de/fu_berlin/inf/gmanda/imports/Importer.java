package de.fu_berlin.inf.gmanda.imports;

import java.util.List;

import de.fu_berlin.inf.gmanda.qda.PrimaryDocumentData;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;

public interface Importer {
	
	List<PrimaryDocumentData> importPrimaryDocuments(String filename, IProgress progress);

}
