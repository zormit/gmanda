package de.fu_berlin.inf.gmanda.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import de.fu_berlin.inf.gmanda.gui.misc.CodeCompleter;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.gui.AutoCompleter;

public class CodeListFilterTextBox extends JPanel {

	JTextField textField = new JTextField();

	AutoCompleter<String> completer;
	
	JButton resetButton = new JButton(new AbstractAction("Reset") {
		public void actionPerformed(ActionEvent arg0) {
			textField.setText("");
		}
	});

	public CodeListFilterTextBox(final CodeList codeList, ProjectProxy projectProxy) {
		super();

		setLayout(new BorderLayout(3, 3));

		add(textField, BorderLayout.CENTER);
		add(resetButton, BorderLayout.LINE_END);

		projectProxy.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
				if (newValue == null) {
					codeList.setModel(null);
				} else {
					codeList.setModel(new FilterList<String>(newValue.getCodeModel().getList(),
						new TextComponentMatcherEditor<String>(textField,
							new TextFilterator<String>() {
								public void getFilterStrings(List<String> arg0, String arg1) {
									arg0.add(arg1);
								}
							})));
				}
			}
		});
		
		completer = new AutoCompleter<String>(new CodeCompleter(textField, projectProxy));
		
	}
}
