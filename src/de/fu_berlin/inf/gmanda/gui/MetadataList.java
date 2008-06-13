/*
 * Created on 04.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui;

import javax.swing.JScrollPane;

public class MetadataList extends JScrollPane {
//
//	JList pane = new JList();
//
//	Map<String, List<PrimaryDocument>> currentMetadata = null;
//
//	public void setMetadata(Map<String, List<PrimaryDocument>> objects) {
//		if (objects != null) {
//			currentMetadata = objects;
//
//			final ArrayList<String> keys = new ArrayList<String>(objects.keySet());
//
//			Collections.sort(keys);
//
//			pane.setModel(new AbstractListModel() {
//				public int getSize() {
//					return keys.size();
//				}
//
//				public Object getElementAt(int i) {
//					return keys.get(i);
//				}
//			});
//		} else {
//			pane.setModel(new DefaultListModel());
//		}
//	}
//
//	public MetadataList(final PrimaryDocumentTree tree, final FilterProxy filter, final SelectionProxy selection) {
//		super();
//
//		pane.setBorder(BorderFactory.createEmptyBorder());
//		pane.setFont(new Font("Courier New", 0, 12));
//
//		setViewportView(pane);
//		invalidate();
//
//		final VariableProxyListener<Filter> codeListListener = new VariableProxyListener<Filter>() {
//			public void setVariable(Filter newValue) {
//				// If the filter changes, we should reset the selection
//				pane.setSelectedIndex(-1);
//			}
//		};
//
//		filter.add(codeListListener);
//
//		pane.addListSelectionListener(new ListSelectionListener() {
//			public void valueChanged(ListSelectionEvent arg0) {
//				if (pane.getSelectedIndex() != -1 && currentMetadata != null){
//					List<PrimaryDocument> codeable = currentMetadata.get((String) pane.getSelectedValue()); 
//					filter.setVariable(codeable, codeListListener);
//				} else {
//					filter.setVariable(null, codeListListener);
//				}
//			}
//		});
//
//	}
}
