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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.gmanda.proxies.CodeDetailProxy;
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
	
	@Inject
	CodeDetailProxy codeDetailProxy;
	
	/* (non-Javadoc)
	 * @see javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.HyperlinkEvent)
	 */
	public void hyperlinkUpdate(HyperlinkEvent arg0) {
		if (arg0.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
			String filename = arg0.getDescription();
			if (filename.startsWith("gmane://")) {

				URI fileurl;
				String query = null;
				try {
					fileurl = new URI(filename);
					filename = fileurl.getAuthority();
					String rawQuery = fileurl.getRawQuery();
					if (rawQuery != null)
						query = URLDecoder.decode(rawQuery, "UTF-8"); 
				} catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
				}

				PrimaryDocument pd = project.getVariable().getCodeModel().getByFilename(
					"gmane://" + filename);

				if (pd != null){
					selection.setVariable(pd);
				
					// Hyperlink to a certain code in a primary document.
					if (query != null){
						if (query.startsWith("jumpTo=")){
							query = query.substring("jumpTo=".length());
							
							if (!codeDetailProxy.getVariable().equals(query)){
								codeDetailProxy.setVariable(query);
							}
						}
					}
				}
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