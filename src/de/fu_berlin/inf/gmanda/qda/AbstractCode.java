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

	public AbstractCode(String tag) {
		setTag(tag);
	}

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
	
	public Collection<String> getTagLevels(){
		return Collections.unmodifiableList(tagLevels);
	}

	public Collection<String> getTagVariations() {
		Collection<String> variations = new LinkedList<String>();

		StringBuilder sb = new StringBuilder();

		for (String string : tagLevels) {
			sb.append(string);
			variations.add(sb.toString());

			// Don't worry about adding a . too many, we don't touch the sb
			// afterwards.
			sb.append('.');
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

		return !otherIterator.hasNext();
	}

	public Collection<? extends Code> getProperties(String propName) {
		return AbstractCodedString.getProperties(Collections.singletonList(this), propName);
	}

}
