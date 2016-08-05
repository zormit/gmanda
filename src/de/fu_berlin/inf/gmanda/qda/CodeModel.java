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

import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import de.fu_berlin.inf.gmanda.util.Pair;
import de.fu_berlin.inf.gmanda.util.StateChangeListener;

public class CodeModel {

	HashMap<String, PrimaryDocument> allDocs = new HashMap<String, PrimaryDocument>();

	HashMap<PrimaryDocument, String> currentlyStoredCodes = new HashMap<PrimaryDocument, String>();

	Map<String, List<PrimaryDocument>> codeMap = new HashMap<String, List<PrimaryDocument>>();

	EventList<String> internalList = new BasicEventList<String>();

	SortedList<String> sortedList = new SortedList<String>(internalList);

	Project project;

	public CodeModel(Project project) {

		this.project = project;

		for (PrimaryDocument pd : PrimaryDocument.getTreeWalker(project
				.getPrimaryDocuments())) {
			add(pd);
		}

		project.getLocalChangeNotifier().add(
				new StateChangeListener<PrimaryDocument>() {
					public void stateChangedNotification(PrimaryDocument t) {
						update(t);
					}
				});
	}

	public PrimaryDocument getByFilename(String filename) {
		return allDocs.get(filename);
	}

	public void remove(PrimaryDocument t) {

		if (t.filename != null) {
			allDocs.remove(t.filename);
		}

		// Remove if existing
		if (currentlyStoredCodes.containsKey(t)) {

			for (String partialQualifiedCode : CodedStringFactory.parse(
					currentlyStoredCodes.get(t)).getAllVariationsDeep()) {
				if (codeMap.containsKey(partialQualifiedCode)) {
					boolean removed = codeMap.get(partialQualifiedCode).remove(
							t);
					if (removed
							&& codeMap.get(partialQualifiedCode).size() == 0) {
						internalList.getReadWriteLock().writeLock().lock();
						try {
							internalList.remove(partialQualifiedCode);
						} finally {
							internalList.getReadWriteLock().writeLock().unlock();
						}
					}
				}
			}

			currentlyStoredCodes.remove(t);
		}
	}

	public void add(PrimaryDocument t) {

		if (t.filename != null) {
			allDocs.put(t.filename, t);
		}

		if (t.getCodeAsString() != null
				&& t.getCodeAsString().trim().length() > 0) {
			// Add again
			String codes = t.getCodeAsString();
			currentlyStoredCodes.put(t, codes);

			for (String partialQualifiedCode : CodedStringFactory.parse(codes)
					.getAllVariationsDeep()) {
				if (!codeMap.containsKey(partialQualifiedCode)) {
					codeMap.put(partialQualifiedCode,
							new LinkedList<PrimaryDocument>());
				}
				List<PrimaryDocument> list = codeMap.get(partialQualifiedCode);
				if (!list.contains(t)) {
					list.add(t);
					if (codeMap.get(partialQualifiedCode).size() == 1) {
						internalList.getReadWriteLock().writeLock().lock();
						try {
							internalList.add(partialQualifiedCode);
						} finally {
							internalList.getReadWriteLock().writeLock().unlock();
						}
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

		if (!codeMap.containsKey(renameFrom)) {
			return Collections.emptyList();
		}

		List<PrimaryDocument> codes = codeMap.get(renameFrom);

		// make copy to prevent concurrent modification
		codes = new LinkedList<PrimaryDocument>(codes);

		List<PrimaryDocument> result = new ArrayList<PrimaryDocument>(codes
				.size());

		for (PrimaryDocument codeable : codes) {
			if (codeable.renameCodes(renameFrom, renameTo))
				result.add(codeable);
		}
		return result;
	}

	public List<String> expand(String s) {
		return expand(s, Integer.MAX_VALUE);
	}

	/**
	 * Takes the given code and returns all subcodes up to the given depth. The
	 * code itself is NOT included in the result.
	 * 
	 * @param code
	 * @param maxdepth
	 * @return
	 */
	public List<String> expand(String code, int maxdepth) {

		List<String> result = new LinkedList<String>();

		int index;

		if (code.equals("")) {
			// Start at first
			index = 0;
		} else {
			index = sortedList.indexOf(code);
			if (index == -1) {
				return result;
			}
			// Skip match itself
			index++;
		}

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

	public List<PrimaryDocument> filter(Iterable<PrimaryDocument> pds,
			CodedString searchTerm) {

		List<PrimaryDocument> result = new LinkedList<PrimaryDocument>();

		for (PrimaryDocument pd : pds) {
			if (pd.getCodeAsString() == null)
				continue;

			if (CodedStringFactory.parse(pd.getCodeAsString()).containsAny(
					searchTerm.getAllCodes()))
				result.add(pd);
		}

		return result;
	}

	public Multimap<PrimaryDocument, Code> getAllCodesDeep(
			Iterable<PrimaryDocument> it, String code) {
		TreeMultimap<PrimaryDocument, Code> result = TreeMultimap.create(
				Ordering.natural(), Ordering.usingToString());
		for (PrimaryDocument pd : it) {
			result.putAll(pd, pd.getCode().getAllDeep(code));
		}
		return result;
	}

	public Multimap<PrimaryDocument, Code> getAllCodesDeep(String code) {
		return getAllCodesDeep(getPrimaryDocuments(code), code);
	}

	
	public Multimap<PrimaryDocument, Code> getValues(
			Iterable<PrimaryDocument> it, String code, String property) {
		TreeMultimap<PrimaryDocument, Code> result = TreeMultimap.create(
				Ordering.natural(), Ordering.usingToString());
		for (PrimaryDocument pd : it) {
			for (Code c : pd.getCode().getAllDeep(code)) {
				result.putAll(pd, c.getProperties(property));
			}
		}
		return result;
	}

	public Multimap<PrimaryDocument, Code> getValues(String code,
			String property) {
		return getValues(getPrimaryDocuments(code), code, property);
	}

	/**
	 * Given a set of PrimaryDocuments and a partitionCode, will return a
	 * partitioning of the set of documents according to the following rules for
	 * partition codes:
	 * 
	 * - null or "" will return a single partition including all
	 * 
	 * - "**" will return a partition for all codes (deep) found
	 * 
	 * -
	 * 
	 * @param pds
	 * @param partitionCode
	 * @return
	 */
	public List<Pair<String, List<PrimaryDocument>>> partition(
			List<PrimaryDocument> pds, String partitionCode) {

		if (partitionCode == null || partitionCode.trim().length() == 0) {
			return new LinkedList<Pair<String, List<PrimaryDocument>>>(
					Collections
							.singletonList(new Pair<String, List<PrimaryDocument>>(
									"All codes", pds)));
		}

		List<Pair<String, PrimaryDocument>> list = new LinkedList<Pair<String, PrimaryDocument>>();

		if (partitionCode.trim().equals("**")) {
			for (PrimaryDocument pd : pds) {
				for (String code : CodedStringFactory.parse(
						pd.getCodeAsString()).getAllDeep()) {
					list.add(new Pair<String, PrimaryDocument>(code, pd));
				}
			}
		} else {
			int maxDepth;

			if (partitionCode.trim().equals("*")) {
				maxDepth = 0;
			} else {
				maxDepth = StringUtils.countMatches(partitionCode, ".*");

				if (maxDepth == 0)
					maxDepth = Integer.MAX_VALUE;
				else
					partitionCode = partitionCode.substring(0, partitionCode
							.indexOf(".*"));
			}

			if (partitionCode.trim().equals("*"))
				partitionCode = "";

			List<String> codes = project.getCodeModel().expand(partitionCode,
					maxDepth);

			if (codes.size() == 0) {
				codes.add(partitionCode);
			}

			for (PrimaryDocument pd : pds) {

				boolean containedInNone = true;

				CodedString c = pd.getCode();

				if (c != null) {

					for (String code : codes) {
						if (c.containsAnyDeep(code + ".*")) {
							list.add(new Pair<String, PrimaryDocument>(
									("<partition>".length() < partitionCode
											.length() + 2 ? "<partition>"
											+ code.substring(partitionCode
													.length()) : code), pd));
							containedInNone = false;
						}
					}
					if (c.containsAnyDeep(partitionCode)) {
						list.add(new Pair<String, PrimaryDocument>(
								("<partition>".length() < partitionCode
										.length() + 2 ? "<partition>"
										: partitionCode), pd));
						containedInNone = false;
					}
				}
				if (containedInNone) {
					list.add(new Pair<String, PrimaryDocument>("<other>", pd));
				}
			}
		}

		List<Pair<String, List<PrimaryDocument>>> l = Pair
				.disjointPartition(list);

		Collections.reverse(l);

		return l;
	}

	/**
	 * Returns a un-centered filter slice consisting of all documents
	 * which contain the given code (as returned by getPrimaryDocuments(code))
	 */
	public Slice<PrimaryDocument> getInitialFilterSlice(String code) {
		return SliceImpl.fromPDs(getPrimaryDocuments(code));
	}

	public Slice<PrimaryDocument> getInitialFilterSlice(
			List<PrimaryDocument> documents) {
		return SliceImpl.fromPDs(documents);
	}

}
