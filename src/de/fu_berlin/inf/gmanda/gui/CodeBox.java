/**
 * 
 */
package de.fu_berlin.inf.gmanda.gui;

import java.awt.AWTKeyStroke;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.qda.CodeModel;
import de.fu_berlin.inf.gmanda.qda.Codeable;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.StateChangeListener;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.gui.AutoCompleter;
import de.fu_berlin.inf.gmanda.util.gui.AutoCompleter.AutoCompleterControl;

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
		completer = new AutoCompleter<String>(new AutoCompleterControl<String>() {

			public void insertText(String selected) {
				int caret = getCaretPosition();

				String text = getText();

				String before = text.substring(0, caret);
				String after = text.substring(caret);
				String leadingText, followingText;

				int last = StringUtils.lastIndexOfAny(before, new String[] { ",", ";", "\n" });
				int first = StringUtils.indexOfAny(after, new String[] { ",", ";" });

				if (last < 0) {
					leadingText = before;
					before = "";
				} else {
					leadingText = text.substring(last + 1, caret);
					before = text.substring(0, last + 1);
					if (text.charAt(last) != '\n')
						before += " ";
				}

				/*
				 * If the selected item is identical to the current code, then
				 * we do not do anything but put the caret at the end of the
				 * code
				 */
				if (first < 0) {
					followingText = after;
				} else {
					followingText = after.substring(0, first);
				}

				if ((leadingText.trim() + followingText.trim()).equals(selected)) {
					after = after.substring(followingText.length());
				}

				setText(before + selected + after);
				setCaretPosition(before.length() + selected.length());
			}

			public void acceptedListItem(String selected) {
				insertText(selected);
			}

			public boolean probeAccept(List<String> currentList) {

				if (currentList.size() == 0)
					return true;

				if (currentList.size() == 1) {
					acceptedListItem(currentList.get(0));
					return true;
				}

				Iterator<String> it = currentList.iterator();

				String prefix = it.next();

				while (it.hasNext()) {
					prefix = de.fu_berlin.inf.gmanda.util.StringUtils.commonPrefix(prefix, it
						.next());
				}

				if (prefix.length() > 0)
					insertText(prefix);

				return false;
			}

			public boolean initializeList(List<String> list) {

				if (currentModel == null)
					return false;

				String text = getText();

				int caret = Math.min(getCaretPosition(), text.length());

				if (caret == -1)
					return false;

				String before = text.substring(0, caret);
				String after = text.substring(caret);

				String leadingText, followingText;

				int last = StringUtils.lastIndexOfAny(before, new String[] { ",", ";" });
				int first = StringUtils.indexOfAny(after, new String[] { ",", ";" });

				if (last < 0) {
					leadingText = before;
					before = "";
				} else {
					leadingText = text.substring(last + 1, caret);
					before = text.substring(0, last + 1);
				}

				if (first < 0) {
					followingText = after;
				} else {
					followingText = after.substring(0, first);
				}

				list.clear();

				if (leadingText.trim().length() == 0) {
					list.addAll(currentModel.getList());
				} else {
					String currentWholeText = (leadingText + followingText).trim();

					for (String s : currentModel.getList()) {

						if (s.startsWith(leadingText.trim()) && !currentWholeText.startsWith(s)) {
							// System.out.println(String.format("%s - %s - %s",
							// StringEscapeUtils.escapeJava(s),
							// StringEscapeUtils.escapeJava(leadingText.trim()),
							// StringEscapeUtils.escapeJava((leadingText +
							// followingText).trim())));

							list.add(s);
						}
					}
				}

				return true;
			}

			public JTextComponent getTextComponent() {
				return CodeBox.this;
			}

			public void addDocumentListener(DocumentListener listener) {
				completerListener = listener;
			}

			public void removeDocumentListener(DocumentListener listener) {
				completerListener = null;
			}

		});

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

	public void insertDateAction() {

		if (currentlyShowing == null)
			return;

		int caret = getCaretPosition();

		String text = getText();

		String before = text.substring(0, caret);
		String after = text.substring(caret);

		String toInsert = new DateTime().toString("YYYY-MM-dd'T'HH:mm");

		setText(before + toInsert + after);
		setCaretPosition(before.length() + toInsert.length());
	}
}