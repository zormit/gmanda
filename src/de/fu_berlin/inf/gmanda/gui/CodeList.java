package de.fu_berlin.inf.gmanda.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import de.fu_berlin.inf.gmanda.imports.trail.TrailManager;
import de.fu_berlin.inf.gmanda.proxies.FilterTextProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.CodeModel;
import de.fu_berlin.inf.gmanda.qda.CodedString;
import de.fu_berlin.inf.gmanda.qda.CodedStringFactory;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.StringUtils;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class CodeList extends JScrollPane {

	JTable pane = new JTable();

	CodeModel currentModel;

	public JTable getTable(){
		return pane;
	}
	
	public class IssueTableFormat implements TableFormat<String> {

		public int getColumnCount() {
			return 3;
		}

		public String getColumnName(int column) {
			if (column == 0)
				return "Code";
			else if (column == 1)
				return "# PDs";
			else if (column == 2)
				return "# sub";

			throw new IllegalStateException();
		}

		public Object getColumnValue(String value, int column) {
			if (column == 0)
				return value;
			else {
				if (currentModel == null) {
					return 0;
				} else {
					if (column == 1) {
						return currentModel.getDocumentCount(value);
					} else {
						return currentModel.getSubCodeCount(value);
					}
				}
			}
		}
	}

	public List<String> getSelection() {
		if (selectionModel != null)
			return selectionModel.getSelected();
		return null;
	}

	EventSelectionModel<String> selectionModel;
	
	public void setModel(EventList<String> model) {
		if (model == null) {
			pane.setModel(new DefaultTableModel());
			pane.setSelectionModel(new DefaultListSelectionModel());
			selectionModel = null;
		} else {
			SortedList<String> sortedList = new SortedList<String>(model);
			pane.setModel(new EventTableModel<String>(sortedList, new IssueTableFormat()));
			selectionModel = new EventSelectionModel<String>(sortedList);
			pane.setSelectionModel(selectionModel);
			pane.getColumnModel().getColumn(0).setMinWidth(150);
			pane.getColumnModel().getColumn(1).setMaxWidth(50);
			pane.getColumnModel().getColumn(2).setMaxWidth(50);
			
			new TableComparatorChooser<String>(pane, sortedList, false);
		}
	}

	TrailManager trail;
	
	public CodeList(
		final ProjectProxy projectProxy, 
		final FilterTextProxy filter,
		TrailManager trail) {
		super();
		
		this.trail = trail;

		pane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

		setViewportView(pane);
		invalidate();

		projectProxy.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				if (newValue == null) {
					currentModel = null;
				} else {
					currentModel = newValue.getCodeModel();
				}
			}
		});

		pane.addMouseListener(new MouseAdapter() {
			
			public String getSelection(){
				List<String> selected = selectionModel.getSelected();
				return StringUtils.join(selected, ", ");
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);

				if (selectionModel == null)
					return;
				
				if ((e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) > 0) {

					String s = filter.getVariable();
					
					if (s == null || s.trim().length() == 0)
						s = getSelection();
					else {
						CodedString c = CodedStringFactory.parse(s);

						List<String> selected = selectionModel.getSelected();

						if (c.containsAll(selected)){
							c.removeAll(selected);
						} else {
							c.addAll(selected);
						}

						s = c.toString();
					}

					filter.setVariable(s);
				} else {
					filter.setVariable(getSelection());
				}
			}
		});

	}

	public void rename(String renameFrom, String renameTo) {
		if (currentModel != null && renameFrom != null && renameTo != null && !renameFrom.equals(renameTo)){
			
			trail.renameOperationStarted();
			List<PrimaryDocument> pds = currentModel.rename(renameFrom, renameTo);
			trail.renameOperationFinished(renameFrom, renameTo, pds);
		}
	}
}
