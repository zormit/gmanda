package de.fu_berlin.inf.gmanda.qda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import de.fu_berlin.inf.gmanda.util.StateChangeListener;

public class CodeModel {

	HashMap<String, PrimaryDocument> allDocs = new HashMap<String, PrimaryDocument>();
	
	HashMap<PrimaryDocument, String> currentlyStoredCodes = new HashMap<PrimaryDocument, String>();

	Map<String, List<PrimaryDocument>> codeMap = new HashMap<String, List<PrimaryDocument>>();

	EventList<String> internalList = new BasicEventList<String>();

	SortedList<String> sortedList = new SortedList<String>(internalList);

	public CodeModel(Project project) {

		for (PrimaryDocument pd : PrimaryDocument.getTreeWalker(project.getPrimaryDocuments())) {
			add(pd);
		}

		project.getLocalChangeNotifier().add(new StateChangeListener<PrimaryDocument>() {
			public void stateChangedNotification(PrimaryDocument t) {
				update(t);
			}
		});
	}
	
	public PrimaryDocument getByFilename(String filename){
		return allDocs.get(filename);
	}

	public void remove(PrimaryDocument t) {

		if (t.filename != null){
			allDocs.remove(t.filename);
		}
		
		// Remove if existing
		if (currentlyStoredCodes.containsKey(t)) {

			for (String partialQualifiedCode : CodedStringFactory.parse(currentlyStoredCodes.get(t))
				.getAllVariations()) {
				if (codeMap.containsKey(partialQualifiedCode)) {
					boolean removed = codeMap.get(partialQualifiedCode).remove(t);
					if (removed && codeMap.get(partialQualifiedCode).size() == 0) {
						internalList.remove(partialQualifiedCode);
					}
				}
			}

			currentlyStoredCodes.remove(t);
		}
	}

	public void add(PrimaryDocument t) {
		
		if (t.filename != null){
			allDocs.put(t.filename, t);
		}
		
		if (t.getCode() != null && t.getCode().trim().length() > 0) {
			// Add again
			String codes = t.getCode();
			currentlyStoredCodes.put(t, codes);

			for (String partialQualifiedCode : CodedStringFactory.parse(codes).getAllVariations()) {
				if (!codeMap.containsKey(partialQualifiedCode)) {
					codeMap.put(partialQualifiedCode, new LinkedList<PrimaryDocument>());
				}
				List<PrimaryDocument> list = codeMap.get(partialQualifiedCode);
				if (!list.contains(t)) {
					list.add(t);
					if (codeMap.get(partialQualifiedCode).size() == 1) {
						internalList.add(partialQualifiedCode);
					}
				}
			}
		}
	}

	public void update(PrimaryDocument t) {
		remove(t);
		add(t);
	}

	public Set<PrimaryDocument> getPrimaryDocuments() {
		return currentlyStoredCodes.keySet();
	}
	
	public int getDocumentCount(String s) {
		return getPrimaryDocuments(s).size();
	}
	
	public int getSubCodeCount(String s) {
		return expand(s).size();
	}

	public List<PrimaryDocument> getPrimaryDocuments(String s) {
		if (codeMap.containsKey(s)) {
			return codeMap.get(s);
		} else {
			return Collections.emptyList();
		}
	}
	
	public List<PrimaryDocument> rename(String renameFrom, String renameTo) {
		
		if (!codeMap.containsKey(renameFrom)){
			return Collections.emptyList();
		}

		List<PrimaryDocument> codes = codeMap.get(renameFrom);

		// make copy to prevent concurrent modification
		codes = new LinkedList<PrimaryDocument>(codes);

		List<PrimaryDocument> result = new ArrayList<PrimaryDocument>(codes.size());
		
		for (PrimaryDocument codeable : codes) {
			if (codeable.renameCodes(renameFrom, renameTo))
				result.add(codeable);
		}
		return result;
	}
	
	
	public List<String> expand(String s){
		return expand(s, Integer.MAX_VALUE);
	}
	
	/**
	 * Takes the given code and returns all subcodes up to the given depth. The code itself is NOT included in the result.
	 * @param code
	 * @param maxdepth
	 * @return
	 */
	public List<String> expand(String code, int maxdepth){
		
		List<String> result = new LinkedList<String>();
		
		int index = sortedList.indexOf(code);
		if (index == -1 && !code.equals("")) {
			return result;
		}
		
		if (!code.equals(""))
			index++;
		
		while (index < sortedList.size()) {
			String next = sortedList.get(index);
			
			if (!next.startsWith(code))
				return result;
	
			if (StringUtils.countMatches(next.substring(code.length()), ".") <= maxdepth)
				result.add(next);
			
			index++;
		} 
		return result;
	}

	public EventList<String> getList() {
		return sortedList;
	}
}
