package de.fu_berlin.inf.gmanda.qda;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringEscapeUtils;

import de.fu_berlin.inf.gmanda.util.HashMapUtils;
import de.fu_berlin.inf.gmanda.util.tree.Childrenable;
import de.fu_berlin.inf.gmanda.util.tree.ChildrenableTreeWalker;

/**
 * File version history:
 * 
 * V1 -> V2:
 *   * None
 *   
 * V2 -> V3:
 *   - Dropped id field
 *   - codes renamed to code
 *   - Filenames converted to gmane:// prefix   
 *   
 */
public class PrimaryDocumentData implements Childrenable<PrimaryDocumentData>{

	public String filename;

	public String code;

	public Properties metadata = new Properties();
	
	public List<PrimaryDocumentData> children = new LinkedList<PrimaryDocumentData>();
	
	public static PrimaryDocumentData toData(PrimaryDocument pd) {
		PrimaryDocumentData result = new PrimaryDocumentData();
		
		String code = pd.code;
		if (code != null && code.trim().length() == 0)
			code = null;
		
		result.code = StringEscapeUtils.escapeJava(code);
		result.filename = pd.filename;
		result.metadata = HashMapUtils.toProperties(pd.metadata);
		
		for (PrimaryDocument child : pd.children) {
			result.children.add(toData(child));
		}
		return result;
	}
	
	public static List<PrimaryDocument> toPrimaryDocuments(List<PrimaryDocumentData> rootDocuments) {

		Iterable<PrimaryDocumentData> allDocuments = new ChildrenableTreeWalker<PrimaryDocumentData>(rootDocuments);

		Map<PrimaryDocumentData, PrimaryDocument> pds = new HashMap<PrimaryDocumentData, PrimaryDocument>();

		for (PrimaryDocumentData source : allDocuments) {
			PrimaryDocument target = new PrimaryDocument();
			target.code = StringEscapeUtils.unescapeJava(source.code);
			target.metadata = HashMapUtils.toHashMap(source.metadata);
			target.filename = source.filename;

			pds.put(source, target);
		}

		for (PrimaryDocumentData source : allDocuments) {
			PrimaryDocument target = pds.get(source);
			if (source.children != null)
				for (PrimaryDocumentData childSource : source.children) {
					PrimaryDocument childTarget = pds.get(childSource);
					childTarget.parent = target;
					target.children.add(childTarget);
				}
		}

		List<PrimaryDocument> result = new LinkedList<PrimaryDocument>();

		for (PrimaryDocumentData rootDD : rootDocuments) {
			result.add(pds.get(rootDD));
		}

		return result;
	}

	public Collection<PrimaryDocumentData> getChildren() {
		return children;
	}

}
