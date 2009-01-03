package de.fu_berlin.inf.gmanda.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.fu_berlin.inf.gmanda.gui.misc.CodeCompleter;
import de.fu_berlin.inf.gmanda.proxies.CodeDetailProxy;
import de.fu_berlin.inf.gmanda.proxies.FilterTextProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.gui.AutoCompleter;
import de.fu_berlin.inf.gmanda.util.gui.ProxyTextComponentBridge;

public class CodeDetailBox extends JPanel {

	JTextField textField = new JTextField();

	JButton resetButton = new JButton(new AbstractAction("Reset") {
		public void actionPerformed(ActionEvent arg0) {
			textField.setText("");
		}
	});

	ProxyTextComponentBridge bridge;

	AutoCompleter<String> completer;

	public CodeDetailBox(final CodeList codeList, ProjectProxy projectProxy, CodeDetailProxy proxy,
		FilterTextProxy filter) {
		super();

		setLayout(new BorderLayout(3, 3));

		add(textField, BorderLayout.CENTER);
		add(resetButton, BorderLayout.LINE_END);

		projectProxy.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});

		filter.add(new VariableProxyListener<String>() {
			public void setVariable(String newValue) {
				textField.setText(newValue);
			}
		});

		bridge = new ProxyTextComponentBridge(textField, proxy);

		completer = new AutoCompleter<String>(new CodeCompleter(textField, projectProxy));
	}
}
