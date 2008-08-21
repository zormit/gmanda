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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.gmanda.proxies.FilterTextProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;

public class GmandaHyperlinkListener implements HyperlinkListener {
	
	@Inject
	ProjectProxy project;
	
	@Inject
	SelectionProxy selection;
	
	@Inject
	FilterTextProxy filter;
	
	public void hyperlinkUpdate(HyperlinkEvent arg0) {
		if (arg0.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
			String filename = arg0.getDescription();
			if (filename.startsWith("gmane://")) {

				PrimaryDocument pd = project.getVariable().getCodeModel().getByFilename(
					filename);

				if (pd != null)
					selection.setVariable(pd);
			}
			if (filename.startsWith("gmaneFilter://")) {
				try {
					filter.setVariable(URLDecoder.decode(filename
						.substring("gmaneFilter://".length()), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					filter.setVariable(filename
						.substring("gmaneFilter://".length()));
				}
			}

		}
	}
}