package de.fu_berlin.inf.gmanda.qda;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class SliceImpl implements Slice {

	public static Code c = CodedStringFactory.parseOne("<no code>");
	
	HashMultimap<PrimaryDocument, Code> contents = new HashMultimap<PrimaryDocument, Code>();
	
	Map<Code, Slice> selected = new HashMap<Code, Slice>();
	Map<Code, Slice> filter = new HashMap<Code, Slice>();
	Map<Code, Slice> rejects = new HashMap<Code, Slice>();
	
	public SliceImpl(){
		// Default contructor for empty Slice;
	}
	
	public SliceImpl(List<PrimaryDocument> primaryDocuments) {
		for (PrimaryDocument pd : primaryDocuments){
			contents.putAll(pd, CodedStringFactory.parse(pd.getCode()).getAllCodes());
		}
	}

	public Slice filter(Code toFilterFor) {
	 
		if (filter.containsKey(toFilterFor))
			return filter.get(toFilterFor);
		
		SliceImpl result = new SliceImpl();
		SliceImpl rejected = new SliceImpl();

		next: 
		for (Entry<PrimaryDocument, Collection<Code>> entry : contents.asMap().entrySet()){

			for (Code c : entry.getValue()){
				if (toFilterFor.matches(c)){
					result.contents.putAll(entry.getKey(), entry.getValue());
					continue next;
				}
				rejected.contents.putAll(entry.getKey(), entry.getValue());
			}
		}
		
		filter.put(toFilterFor, result);
		rejects.put(toFilterFor, rejected);
		
		return result;
	}
	
	public Slice select(Code toSelect) {
		
		if (selected.containsKey(toSelect))
			return selected.get(toSelect);
	
		SliceImpl result = new SliceImpl();
		
		for (Entry<PrimaryDocument, Collection<Code>> entry : contents.asMap().entrySet()){

			PrimaryDocument pd = entry.getKey();

			for (Code c : entry.getValue()){
				if (toSelect.matches(c)){
					result.contents.putAll(pd, c.getProperties());
				} 
			}
		}
		selected.put(toSelect, result);
		
		return result;
	}
	
	public Multimap<PrimaryDocument, Code> getDocuments() {
		return contents;
	}

	public Map<String, Slice> slice() {
		
		Map<String, SliceImpl> result = new TreeMap<String, SliceImpl>();
		
		for (Entry<PrimaryDocument, Collection<Code>> entry : contents.asMap().entrySet()){

			PrimaryDocument pd = entry.getKey();

			for (Code c : entry.getValue()){
				String tag = c.getTag();
				if (tag.equals("desc") && c.getProperties().size() == 1){
					tag = c.getValue();
				} 
				
				tag = StringUtils.strip(tag, " \"'\n\r\t\f");
				
				if (!result.containsKey(tag)){
					result.put(tag, new SliceImpl());
				}
				SliceImpl s = result.get(tag);

				s.contents.putAll(pd, c.getProperties());
			}
		}
		
		return (Map)result;
	}
}
