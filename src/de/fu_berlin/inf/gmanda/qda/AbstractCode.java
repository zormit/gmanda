package de.fu_berlin.inf.gmanda.qda;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractCode implements Code {

	protected String tag;

	protected String tagTrimmed;

	protected List<String> tagLevels;

	protected void setTag(String newTag) {
		this.tag = newTag;
		this.tagTrimmed = newTag.trim();
		this.tagLevels = new LinkedList<String>();

		for (String level : tagTrimmed.split("\\.")) {
			if (level.trim().length() > 0)
				tagLevels.add(level);
		}
	}

	public String getTag() {
		return tagTrimmed;
	}

	public String toString() {
		return toString(true, false);
	}
	
	public Collection<String> getTagVariations() {
		Collection<String> variations = new LinkedList<String>();

		StringBuilder sb = new StringBuilder();

		for (String string : tagLevels) {
			if (sb.length() > 0) {
				sb.append('.');
			}
			sb.append(string);
			variations.add(sb.toString());
		}
		return variations;
	}

	public boolean hasValue() {
		String value = getValue();

		return value != null && value.trim().length() > 0;
	}

	public boolean matchesAny(Iterable<? extends Code> codes) {
		for (Code c : codes) {
			if (this.matches(c))
				return true;
		}
		return false;
	}
	

	public boolean renameTag(String fromRename, String toRename) {

		if (!tag.contains(fromRename))
			return false;

		String newTag = tag.replace(fromRename, toRename);

		if (newTag.trim().equals("") && hasValue()) {
			setTag("???.orphaned description");
		} else {
			setTag(newTag);
		}

		return true;
	}

	/**
	 * Will return true if both codes are identical by their code Levels or if
	 * the codes match up to a * of this
	 * 
	 * <pre>
	 * hello.world matches hello.world
	 * '*' matches everything
	 * hello.* matches hello.world and hello
	 * </pre>
	 * 
	 * @param otherCode
	 * @return
	 */
	public boolean matches(Code otherCode) {

		if (tagLevels.size() == 0)
			return tagLevels.size() == 0;

		Iterator<String> thisIterator = tagLevels.iterator();
		Iterator<String> otherIterator = ((AbstractCode) otherCode).tagLevels.iterator();

		do {
			String mine = thisIterator.next();
			if (mine.trim().equals("*"))
				return true;

			if (!otherIterator.hasNext())
				return false;

			String other = otherIterator.next();

			if (!mine.trim().equals(other.trim()))
				return false;
		} while (thisIterator.hasNext());

		return true;
	}
	
	public Collection<? extends Code> getProperties(String propName){
		return AbstractCodedString.getProperties(Collections.singletonList(this), propName);
	}

}
