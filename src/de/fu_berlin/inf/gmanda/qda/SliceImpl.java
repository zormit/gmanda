package de.fu_berlin.inf.gmanda.qda;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import de.fu_berlin.inf.gmanda.util.CMultimap;
import de.fu_berlin.inf.gmanda.util.CUtils;

public class SliceImpl<T> implements Slice<T> {

	public static Code c = CodedStringFactory.parseOne("<no code>");
	
	CMultimap<T, Code> contents = new CMultimap<T, Code>();
	
	Map<Code, Slice<T>> selected = new HashMap<Code, Slice<T>>();
	Map<Code, Slice<T>> filter   = new HashMap<Code, Slice<T>>();
	Map<Code, Slice<T>> rejects  = new HashMap<Code, Slice<T>>();
	
	public SliceImpl(){
		// Default contructor for empty Slice;
	}
	
	public static SliceImpl<PrimaryDocument> fromPDs(List<PrimaryDocument> primaryDocuments){
		CMultimap<PrimaryDocument, Code> initial = new CMultimap<PrimaryDocument, Code>();
		
		for (PrimaryDocument pd : primaryDocuments){
			initial.putAll(pd, CodedStringFactory.parse(pd.getCodeAsString()).getAllCodesDeep());
		}	
		return new SliceImpl<PrimaryDocument>(initial);
	}
	
	public SliceImpl(CMultimap<T, Code> contents){
		this.contents = contents;
		
	}

	public Slice<T> filter(Code toFilterFor) {
	 
		if (filter.containsKey(toFilterFor))
			return filter.get(toFilterFor);
		
		SliceImpl<T> result = new SliceImpl<T>();
		//SliceImpl rejected = new SliceImpl();

		next: 
		for (Entry<T, Collection<Code>> entry : contents.entrySet()){

			for (Code c : entry.getValue()){
				if (toFilterFor.matches(c)){
					result.contents.putAll(entry.getKey(), entry.getValue());
					continue next;
				}
			//	rejected.contents.putAll(entry.getKey(), entry.getValue());
			}
		}
		
		filter.put(toFilterFor, result);
		//rejects.put(toFilterFor, rejected);
		
		return result;
	}
	
	public Slice<T> select(Code toSelect) {
		
		if (selected.containsKey(toSelect))
			return selected.get(toSelect);
	
		SliceImpl<T> result = new SliceImpl<T>();
		
		for (Entry<T, Collection<Code>> entry : contents.entrySet()){

			T pd = entry.getKey();

			for (Code c : entry.getValue()){
				if (toSelect.matches(c)){
					result.contents.putAll(pd, c.getProperties());
				} 
			}
		}
		selected.put(toSelect, result);
		
		return result;
	}
	
	public CMultimap<T, Code> getDocuments() {
		return contents;
	}

	public Map<String, Slice<T>> slice(){
		return slice("", 0);
	}
	
	public Slice<String> sliceAndPack(String by, int depth){
		
		Code byCode = null;
		if (by.trim().length() > 0){
			byCode = CodedStringFactory.parseOne(by + ".*");
		}
		
		SliceImpl<String> result = new SliceImpl<String>();
		
		for (Entry<T, Collection<Code>> entry : contents.entrySet()){

			for (Code c : entry.getValue()){
				if (byCode != null && !byCode.matches(c))
					continue;
				
				String tag = c.getTag();
				
				if (depth > 0){
					depth += byCode.getTagLevels().size() - 1;
					tag = StringUtils.join(CUtils.first(CodedStringFactory.parseOne(tag).getTagLevels(), depth), ".");
				}
				
				result.contents.putAll(tag, c.getProperties());
			}
		}
		
		return result;
	}
	
	public Map<String, Slice<T>> slice(String by, int depth) {
		
		Code byCode = null;
		if (by.trim().length() > 0){
			byCode = CodedStringFactory.parseOne(by + ".*");
			depth += byCode.getTagLevels().size() - 1;
		}
		
		Map<String, SliceImpl<T>> result = new TreeMap<String, SliceImpl<T>>();
		
		for (Entry<T, Collection<Code>> entry : contents.entrySet()){

			T pd = entry.getKey();

			for (Code c : entry.getValue()){
				if (byCode != null && !byCode.matches(c))
					continue;
				
				String tag = c.getTag();
				
				if (depth > 0){
					tag = StringUtils.join(CUtils.first(CodedStringFactory.parseOne(tag).getTagLevels(), depth), ".");
				}
				
				if (!result.containsKey(tag)){
					result.put(tag, new SliceImpl<T>());
				}
				SliceImpl<T> s = result.get(tag);

				s.contents.putAll(pd, c.getProperties());
			}
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Slice<T>> result2 = (Map)result;
		
		return result2;
	}
}
