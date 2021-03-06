/**
 * 
 */
package de.fu_berlin.inf.gmanda.gui;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.joda.time.DateTime;

import de.fu_berlin.inf.gmanda.gui.actions.AutoIndentAction;
import de.fu_berlin.inf.gmanda.gui.misc.CodeCompleter;
import de.fu_berlin.inf.gmanda.gui.misc.GmandaHyperlinkListener;
import de.fu_berlin.inf.gmanda.gui.misc.GmandaInfoBox;
import de.fu_berlin.inf.gmanda.gui.misc.GmandaHyperlinkListener.JumpToExecutor;
import de.fu_berlin.inf.gmanda.proxies.CodeDetailProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.qda.CodeModel;
import de.fu_berlin.inf.gmanda.qda.Codeable;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.StateChangeListener;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.gui.AutoCompleter;

public class CodeBox extends JTextArea implements JumpToExecutor{

	Codeable currentlyShowing;

	boolean sendChanges = false;

	boolean receiveChanges = false;

	HashMap<Codeable, Integer> caretPositions = new HashMap<Codeable, Integer>();

	public AutoCompleter<String> completer;

	public boolean isManagingFocus() {
		return false;
	}

	CodeModel currentModel;

	DocumentListener completerListener;

	public CodeBox(final ProjectProxy project, SelectionProxy selection,
			GmandaInfoBox gmandaInfoBox, final CodeDetailProxy codeDetailProxy, GmandaHyperlinkListener linkListener) {
		super();
		
		linkListener.addExecutor(this);

		// setBorder(BorderFactory.createEmptyBorder());
		setWrapStyleWord(true);
		setLineWrap(true);

		Action action = new AutoIndentAction(":{", 2);
		getInputMap(JComponent.WHEN_FOCUSED).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), action);
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
		completer.setInfoBox(gmandaInfoBox);

		selection.addAndNotify(new VariableProxyListener<Object>() {

			StateChangeListener<Codeable> listener = new StateChangeListener<Codeable>() {
				public void stateChangedNotification(Codeable t) {

					assert t == currentlyShowing;

					if (!receiveChanges)
						return;

					int i = getCaretPosition();

					sendChanges = false;
					setText(currentlyShowing.getCodeAsString());

					setCaretPosition(Math.max(0, Math
							.min(i, getText().length())));

					// SwingUtilities.invokeLater(new Runnable() {
					// public void run() {
					//CodeBox.this.codeBoxView.getVerticalScrollBar().setValue(0
					// );
					// }
					// });

					sendChanges = true;
				}
			};

			public void setVariable(Object newSelected) {
				if (currentlyShowing != null) {
					currentlyShowing.getCodeChangeNotifier().remove(listener);
					caretPositions.put(currentlyShowing, getCaretPosition());
				}

				if (newSelected instanceof Codeable) {
					currentlyShowing = (Codeable) newSelected;
					receiveChanges = true;
					currentlyShowing.getCodeChangeNotifier().addAndNotify(
							listener, currentlyShowing);

					int i = -1;

					if (i == -1) {
						Integer oldCaret = caretPositions.get(currentlyShowing);
						if (oldCaret != null) {
							i = oldCaret;
						}
					}

					if (i == -1) {
						i = getCodePosition(codeDetailProxy.getVariable());
					}
					
					jumpToPosition(i);

					setEnabled(true);
				} else {
					currentlyShowing = null;
					sendChanges = false;
					setEnabled(false);
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
		String whiteSpace = de.fu_berlin.inf.gmanda.util.CStringUtils
				.getLeadingWhiteSpace(before.substring(lineStart, caret));

		beforeCaretInsert = beforeCaretInsert.replace("\n", "\n" + whiteSpace);
		afterCaretInsert = afterCaretInsert.replace("\n", "\n" + whiteSpace);

		setText(before + beforeCaretInsert + afterCaretInsert + after);
		setCaretPosition(before.length() + beforeCaretInsert.length());
	}

	public void insertSubCodeTemplate() {
		insertAtCaret(": {\n  date: \""
				+ new DateTime().toString("YYYY-MM-dd'T'HH:mm")
				+ "\",\n  desc: \"", "\"\n},");
	}

	public void insertDateAction() {
		insertAtCaret(new DateTime().toString("YYYY-MM-dd'T'HH:mm"), "");
	}

	public void insertSessionLogTemplate() {
		insertAtCaret("session : {\n  start : \""
				+ new DateTime().toString("YYYY-MM-dd'T'HH:mm")
				+ "\", end : \"\", revision : \"\",\n" + "  memo : \"",
				"\"\n},");
	}

	public void jumpTo(String query) {
		jumpToPosition(getCodePosition(query));
	}

	public int getCodePosition(String codeDetail) {
		
		int i = -1;
		if (codeDetail != null
				&& codeDetail.trim().length() > 0) {
			i = getText().indexOf(codeDetail + ":");
			if (i == -1)
				i = getText().indexOf(codeDetail);
		}
		return i;
	}

	public void jumpToPosition(int i) {
		if (i != -1) {
			setCaretPosition(Math.max(0, Math.min(i, getText()
					.length())));
		}
	}
}