/**
 * Copyright 2008 Christopher Oezbek
 * 
 * This file is part of GmanDA.
 *
 * GmanDA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GmanDA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GmanDA.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fu_berlin.inf.gmanda.gui.misc;

import java.util.Iterator;
import java.util.List;

import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.StringUtils;

import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.CodeModel;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.gui.AutoCompleter.AutoCompleterControl;

public class CodeCompleter implements AutoCompleterControl<String> {

	JTextComponent textComponent;
	
	ProjectProxy project;
	
	public CodeCompleter(JTextComponent codeBox, ProjectProxy project) {
		this.textComponent = codeBox;
		this.project = project;
	}

	String[] delimCharsBefore = new String[] { ",", ";", ":", "{", "}", "\n" };
	String[] delimCharsAfter = new String[] { ",", ";", ":", "{", "}"};

	public void insertText(String selected) {
		int caret = this.textComponent.getCaretPosition();

		String text = this.textComponent.getText();

		String before = text.substring(0, caret);
		String after = text.substring(caret);
		String leadingText, followingText;

		int last = StringUtils.lastIndexOfAny(before, delimCharsBefore);
		int first = StringUtils.indexOfAny(after, delimCharsAfter);

		if (last < 0) {
			leadingText = before;
			before = "";
		} else {
			leadingText = text.substring(last + 1, caret);
			before = text.substring(0, last + 1);
			if (text.charAt(last) != '\n')
				before += " ";
		}

		/*
		 * If the selected item is identical to the current code, then
		 * we do not do anything but put the caret at the end of the
		 * code
		 */
		if (first < 0) {
			followingText = after;
		} else {
			followingText = after.substring(0, first);
		}

		if ((leadingText.trim() + followingText.trim()).equals(selected)) {
			after = after.substring(followingText.length());
		}

		this.textComponent.setText(before + selected + after);
		this.textComponent.setCaretPosition(before.length() + selected.length());
	}

	public void acceptedListItem(String selected) {
		insertText(selected);
	}

	public boolean probeAccept(List<String> currentList) {

		if (currentList.size() == 0)
			return true;

		if (currentList.size() == 1) {
			acceptedListItem(currentList.get(0));
			return true;
		}

		Iterator<String> it = currentList.iterator();

		String prefix = it.next();

		while (it.hasNext()) {
			prefix = de.fu_berlin.inf.gmanda.util.CStringUtils.commonPrefix(prefix, it
				.next());
		}

		if (prefix.length() > 0)
			insertText(prefix);

		return false;
	}

	public boolean initializeList(List<String> list) {

		Project p = this.project.getVariable();
		
		if (p == null)
			return false;
		
		CodeModel cm = p.getCodeModel();
		
		String text = this.textComponent.getText();

		int caret = Math.min(this.textComponent.getCaretPosition(), text.length());

		if (caret == -1)
			return false;

		String before = text.substring(0, caret);
		String after = text.substring(caret);

		String leadingText, followingText;

		int last = StringUtils.lastIndexOfAny(before, delimCharsBefore);
		int first = StringUtils.indexOfAny(after, delimCharsAfter);

		if (last < 0) {
			leadingText = before;
			before = "";
		} else {
			leadingText = text.substring(last + 1, caret);
			before = text.substring(0, last + 1);
		}

		if (first < 0) {
			followingText = after;
		} else {
			followingText = after.substring(0, first);
		}

		list.clear();

		if (leadingText.trim().length() == 0) {
			list.addAll(cm.getList());
		} else {
			String currentWholeText = (leadingText + followingText).trim();

			String trimmedLeading = leadingText.trim();
			
			for (String s : cm.getList()) {

				if (s.startsWith(trimmedLeading) && !currentWholeText.startsWith(s)) {
					list.add(s);
				}
			}
		}

		return true;
	}

	public JTextComponent getTextComponent() {
		return textComponent;
	}

	public void addDocumentListener(DocumentListener listener) {
		textComponent.getDocument().addDocumentListener(listener);
	}

	public void removeDocumentListener(DocumentListener listener) {
		textComponent.getDocument().removeDocumentListener(listener);
	}
}