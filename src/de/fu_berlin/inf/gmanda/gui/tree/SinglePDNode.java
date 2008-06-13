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

import java.util.Collections;
import java.util.List;

import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;

public class SinglePDNode extends PrimaryDocumentNode {

	public SinglePDNode(JNode parent, PrimaryDocument d){
		super(parent, d);
	}
	
	public List<JNode> getChildren(){
		return Collections.emptyList();
	}
	
	public int getChildCount(){
		return 0;
	}
}