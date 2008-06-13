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
package de.fu_berlin.inf.gmanda.gui.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTreeFilterTextField.FilterKind;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.util.HashMapUtils;

public class MailingListNode extends FixNamedNode {

	List<PrimaryDocument> rootFilterList;

	FilterKind filterKind;

	public MailingListNode(JNode parent, String name, List<PrimaryDocument> list, FilterKind filterKind) {
		super(parent, name);
		this.rootFilterList = list;
		this.filterKind = filterKind;
	} 

	List<JNode> children;

	public List<JNode> getChildren() {
		if (children == null) {

			List<PrimaryDocument> lists = new LinkedList<PrimaryDocument>();

			HashMap<PrimaryDocument, List<PrimaryDocument>> threads = new HashMap<PrimaryDocument, List<PrimaryDocument>>();

			if (filterKind != FilterKind.SINGLE){
				rootFilterList = PrimaryDocument.filterChildren(rootFilterList);
			}
			
			for (PrimaryDocument pd : rootFilterList) {
				HashMapUtils.putList(threads, pd.getMailingList(), pd, true);
			}

			lists = new ArrayList<PrimaryDocument>(threads.keySet());

			for (List<PrimaryDocument> list : threads.values()) {
				Collections.sort(list);
			}

			children = new ArrayList<JNode>(lists.size());

			for (PrimaryDocument list : lists) {
				children.add(new PrimaryDocumentWithGivenChildren(this, list, threads.get(list), filterKind));
			}
		}
		return children;
	}
}