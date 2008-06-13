package de.fu_berlin.inf.gmanda.qda;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;

import de.fu_berlin.inf.gmanda.util.tree.Childrenable;
import de.fu_berlin.inf.gmanda.util.tree.ChildrenableTreeWalker;

public class PrimaryDocumentDataV1 implements Childrenable<PrimaryDocumentDataV1> {

	public String filename;

	public String id;

	public String codes;

	public Properties metadata = new Properties();

	public List<PrimaryDocumentDataV1> children = new LinkedList<PrimaryDocumentDataV1>();

	public static List<PrimaryDocumentData> convert(List<PrimaryDocumentDataV1> rootDocuments) {

		Iterable<PrimaryDocumentDataV1> allDocuments = new ChildrenableTreeWalker<PrimaryDocumentDataV1>(
			rootDocuments);

		Map<PrimaryDocumentDataV1, PrimaryDocumentData> pds = new HashMap<PrimaryDocumentDataV1, PrimaryDocumentData>();

		for (PrimaryDocumentDataV1 source : allDocuments) {
			PrimaryDocumentData target = new PrimaryDocumentData();
			target.code = StringEscapeUtils.unescapeJava(source.codes);
			target.metadata = source.metadata;
			// Dropped id field

			// Big operation ahead: Filenames need fixing!

			String listName = null;
			String tempFilename = source.filename;

			if (tempFilename != null) {
				if (target.metadata.containsKey("list"))
					listName = target.metadata.getProperty("list");

				if (listName == null) {
					Matcher m = Pattern.compile("(comp(\\.[a-z][a-z0-9-]*)+?)\\.(?=\\d|mbox)").matcher(
						tempFilename);

					if (m.find())
						listName = "gmane." + m.group(1);
				}

				if (listName == null) {
					System.out.println("Not a gmane file: " + source.filename);
					target.filename = source.filename;
				} else {
					target.filename = "gmane://" + listName + "." + target.metadata.get("id");
					target.metadata.setProperty("list", listName);
				}
			}

			pds.put(source, target);
		}

		// Reparent
		for (PrimaryDocumentDataV1 source : allDocuments) {
			PrimaryDocumentData target = pds.get(source);
			if (source.children != null)
				for (PrimaryDocumentDataV1 childSource : source.children) {
					target.children.add(pds.get(childSource));
				}
		}

		List<PrimaryDocumentData> result = new LinkedList<PrimaryDocumentData>();

		for (PrimaryDocumentDataV1 rootDD : rootDocuments) {
			result.add(pds.get(rootDD));
		}

		return result;
	}

	public Collection<PrimaryDocumentDataV1> getChildren() {
		return children;
	}

}
