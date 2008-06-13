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

import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;

public class ProjectNode extends FixNamedNode {
	
	Project p;
	
	public ProjectNode(JNode parent, Project p){
		super(parent, "Project");
		this.p = p;
	}
	
	List<JNode> children;
	
	public List<JNode> getChildren() {
		
		if (children == null){
			children = new ArrayList<JNode>(p.getPrimaryDocuments().size());
			
			for (PrimaryDocument child : p.getPrimaryDocuments()){
				children.add(new PrimaryDocumentAllChildrenNode(this, child));
			}
		}
		return children;
	}
	
	/**
	 * Overriden to use list of root PDs directly if not yet initialized.
	 */
	@Override
	public int getChildCount(){
		if (children == null){
			return p.getPrimaryDocuments().size();
		} else {
			return children.size();
		}
	}
	
	public CellType getType() {
		return CellType.Project;
	}

	
}