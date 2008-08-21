/**
 * 
 */
package de.fu_berlin.inf.gmanda.gui;

import java.awt.AWTKeyStroke;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.joda.time.DateTime;

import de.fu_berlin.inf.gmanda.gui.actions.AutoIndentAction;
import de.fu_berlin.inf.gmanda.gui.misc.CodeCompleter;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.qda.CodeModel;
import de.fu_berlin.inf.gmanda.qda.Codeable;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.StateChangeListener;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.gui.AutoCompleter;

public class CodeBox extends JTextArea {

	CodeBoxView codeBoxView;

	Codeable currentlyShowing;

	boolean sendChanges = false;

	boolean receiveChanges = false;

	public AutoCompleter<String> completer;

	public boolean isManagingFocus() {
		return false;
	}

	CodeModel currentModel;

	DocumentListener completerListener;

	public CodeBox(CodeBoxView codeBoxView, ProjectProxy project, SelectionProxy selection) {
		super();
		this.codeBoxView = codeBoxView;

		codeBoxView.setViewportView(this);

		// setBorder(BorderFactory.createEmptyBorder());
		setWrapStyleWord(true);
		setLineWrap(true);

		Set<AWTKeyStroke> newForwardKeys = new HashSet<AWTKeyStroke>();
		newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
		newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));

		this.codeBoxView.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
			newForwardKeys);
		this.codeBoxView.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);

		Action action = new AutoIndentAction(":{", 2); 
		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), action); 
		getActionMap().put(action, action);
		
		project.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				if (newValue == null) {
					currentModel = null;
				} else {
					currentModel = newValue.getCodeModel();
				}
			}
		});

		getDocument().addDocumentListener(new DocumentListener() {

			public void doIt() {
				if (sendChanges) {
					receiveChanges = false;
					currentlyShowing.setCode(getText());
					receiveChanges = true;
				}
			}

			public void changedUpdate(DocumentEvent e) {
				doIt();
				if (completerListener != null)
					completerListener.changedUpdate(e);
			}

			public void insertUpdate(DocumentEvent e) {
				doIt();
				if (completerListener != null)
					completerListener.insertUpdate(e);
			}

			public void removeUpdate(DocumentEvent e) {
				doIt();
				if (completerListener != null)
					completerListener.removeUpdate(e);
			}
		});

		/*
		 * Create Completer
		 */
		completer = new AutoCompleter<String>(new CodeCompleter(this, project));

		selection.addAndNotify(new VariableProxyListener<Object>() {

			StateChangeListener<Codeable> listener = new StateChangeListener<Codeable>() {
				public void stateChangedNotification(Codeable t) {

					assert t == currentlyShowing;

					if (!receiveChanges)
						return;

					sendChanges = false;
					setText(currentlyShowing.getCode());
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							CodeBox.this.codeBoxView.getVerticalScrollBar().setValue(0);
						}
					});
					sendChanges = true;
				}
			};

			public void setVariable(Object newSelected) {

				if (currentlyShowing != null) {
					currentlyShowing.getCodeChangeNotifier().remove(listener);
				}

				if (newSelected instanceof Codeable) {
					currentlyShowing = (Codeable) newSelected;
					receiveChanges = true;
					currentlyShowing.getCodeChangeNotifier().addAndNotify(listener,
						currentlyShowing);
					CodeBox.this.codeBoxView.setEnabled(true);
				} else {
					currentlyShowing = null;
					sendChanges = false;
					CodeBox.this.codeBoxView.setEnabled(false);
					setText("");
				}
			}
		});
	}

	public void insertAtCaret(String beforeCaretInsert, String afterCaretInsert) {
		if (currentlyShowing == null)
			return;

		int caret = getCaretPosition();

		String text = getText();

		String before = text.substring(0, caret);
		String after = text.substring(caret);
		
		int lineStart = before.lastIndexOf('\n') + 1; 
		String whiteSpace = de.fu_berlin.inf.gmanda.util.CStringUtils.getLeadingWhiteSpace(before.substring(lineStart, caret));

		beforeCaretInsert = beforeCaretInsert.replace("\n", "\n" + whiteSpace);
		afterCaretInsert = afterCaretInsert.replace("\n", "\n" + whiteSpace);
		
		setText(before + beforeCaretInsert + afterCaretInsert + after);
		setCaretPosition(before.length() + beforeCaretInsert.length());
	}

	public void insertSubCodeTemplate() {
		insertAtCaret(": {\n  date: \"" + new DateTime().toString("YYYY-MM-dd'T'HH:mm") + "\",\n  desc: \"",
			"\"\n},");
	}

	public void insertDateAction() {
		insertAtCaret(new DateTime().toString("YYYY-MM-dd'T'HH:mm"), "");
	}

	public void insertSessionLogTemplate() {
		insertAtCaret("session : {\n  start : \""
			+ new DateTime().toString("YYYY-MM-dd'T'HH:mm") + "\", end : \"\", revision : \"\",\n"
			+ "  memo : \"", "\"\n},");
	}
}