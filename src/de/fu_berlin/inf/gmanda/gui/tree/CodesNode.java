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
import java.util.List;

import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTreeFilterTextField.FilterKind;
import de.fu_berlin.inf.gmanda.qda.CodeModel;

public class CodesNode extends FixNamedNode {
	
	CodeModel p;
	
	FilterKind f;
	
	public CodesNode(JNode parent, CodeModel p, FilterKind f){
		super(parent, "Codes");
		this.p = p;
		this.f = f;
	}
	
	List<JNode> children;
	
	public List<JNode> getChildren() {
		
		if (children == null){
			children = new ArrayList<JNode>(p.getList().size());
			
			for (String code : p.getList()){
				children.add(new MailingListNode(this, code, p.getPrimaryDocuments(code), f));
			}
		}
		return children;
	}
}