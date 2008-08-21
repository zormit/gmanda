package de.fu_berlin.inf.gmanda.util.gui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import de.fu_berlin.inf.gmanda.util.MutableList;

/**
 * 
 * Based on http://www.jroller.com/santhosh/entry/file_path_autocompletion
 * 
 * by Santhosh Kumar T - santhosh@in.fiorano.com
 * 
 * Modified by Christopher Oezbek.
 * 
 */
public class AutoCompleter<T> {

	/**
	 * To use the autocompleter implement this interface and pass it to the
	 * constructor.
	 */
	public interface AutoCompleterControl<T> {

		/**
		 * The AutoCompleter will call this method before showing the popup, so
		 * that the control can initialize the list of entries to be offered
		 * based on the text entered so far.
		 * 
		 * @param listForModel
		 *            Modify this list as you wish and fill it with the values
		 *            you wish to be shown in the popup. You can expect the list
		 *            to be empty initially and unchanged between calls.<br>
		 *            If the popup should be shown, the list should contain the
		 *            items to be shown to the user.<br>
		 *            If the popup should not be shown, the list should be
		 *            empty.
		 */
		public boolean initializeList(List<T> listForModel);

		/**
		 * Will be called by the autocompleter if the user has chosen an item
		 * from the list. The control can then update the text component
		 * accordingly.
		 * 
		 * @param selected
		 */
		public void acceptedListItem(T selected);

		/**
		 * Will be called by the autocompleter if the user has pressed CTRL-SPACE.
		 * The control can use this to update the text component.
		 * 
		 * For instance if the list currently contains three item 'hello world', 
		 * 'hello space' and 'hello moon' the text component might be updated 
		 * on CTRL-SPACE to contain the common prefix 'hello ' 
		 * 
		 * @param currentList
		 * 
		 * @return Should return true iff the popup should be closed 
		 */
		public boolean probeAccept(List<T> currentList);
		
		/**
		 * Should return the text component this auto completer belongs to. Is
		 * not expected to change over time.
		 * 
		 * Must be a cheap call except for the first invocation.
		 * 
		 * @return The text component, which the AutoCompleter should register
		 *         with.
		 */
		public JTextComponent getTextComponent();

		/**
		 * If the AutoCompleter wants to listen to document changes, it will
		 * call this method.
		 * 
		 * The control might just pass this directly to the text component or
		 * generate the calls manually.
		 * 
		 * @param listener
		 *            The listener to call if the text in the component changes.
		 */
		public void addDocumentListener(DocumentListener listener);

		/**
		 * Removes the listener previously registered with the control
		 * 
		 * @param listener
		 *            The listener to remove.
		 */
		public void removeDocumentListener(DocumentListener listener);
	}

	protected JList list = new JList();

	protected JPopupMenu popup = new JPopupMenu();

	protected MutableList<T> listModel = new MutableList<T>();

	protected JTextComponent textComp;

	protected AutoCompleterControl<T> control;

	/**
	 * If autoShow is true the popup is shown without pressing CTRL-SPACE
	 */
	protected boolean autoShow = false;
	
	/**
	 * If probeAcceptOnOpen is true, then opening the pop-up will already 
	 * try to complete. If set to false, an additional CTRL-SPACE is necessary
	 * to probe.
	 */
	protected boolean probeAcceptOnOpen = true;
	
	protected InputMap inputMap = new InputMap();

	public AutoCompleter(AutoCompleterControl<T> comp) {
		control = comp;
		textComp = comp.getTextComponent();

		JScrollPane scroll = new JScrollPane(list);
		scroll.setBorder(null);

		list.setFocusable(false);
		list.setModel(listModel);

		scroll.getVerticalScrollBar().setFocusable(false);
		scroll.getHorizontalScrollBar().setFocusable(false);

		popup.setBorder(BorderFactory.createLineBorder(Color.black));
		popup.add(scroll);

		textComp.getActionMap().put(downAction, downAction);
		textComp.getActionMap().put(upAction, upAction);
		textComp.getActionMap().put(pageDownAction, pageDownAction);
		textComp.getActionMap().put(pageUpAction, pageUpAction);
		textComp.getActionMap().put(acceptAction, acceptAction);
		textComp.getActionMap().put(hideAction, hideAction);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), downAction);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), upAction);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0),
			pageDownAction);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0),
			pageUpAction);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
			acceptAction);		
		
		if (autoShow) {
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				hideAction);
			comp.addDocumentListener(showPopupListener);
		} else {
			textComp.registerKeyboardAction(autoCompleteAction, KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,
				KeyEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
		}

		popup.addPopupMenuListener(new PopupMenuListener() {
			
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				inputMap.setParent(textComp.getInputMap());
				textComp.setInputMap(JComponent.WHEN_FOCUSED, inputMap);

				list.getSelectionModel().clearSelection();

				if (autoShow == false) {
					control.addDocumentListener(showPopupListener);
				}
			}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				
				textComp.setInputMap(JComponent.WHEN_FOCUSED, inputMap.getParent());

				if (autoShow == false) {
					control.removeDocumentListener(showPopupListener);
				}
			}

			public void popupMenuCanceled(PopupMenuEvent e) {
			}
		});

		list.setRequestFocusEnabled(false);
	}

	Action acceptAction = new AbstractAction() {
		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent e) {
			popup.setVisible(false);

			T selectedValue = (T) list.getSelectedValue();
			if (selectedValue != null)
				control.acceptedListItem(selectedValue);
		}
	};

	DocumentListener showPopupListener = new DocumentListener() {
		public void insertUpdate(DocumentEvent e) {
			showPopupLater();
		}

		public void removeUpdate(DocumentEvent e) {
			showPopupLater();
		}

		void showPopupLater() {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					showPopup();
				}
			});
		}

		public void changedUpdate(DocumentEvent e) {
		}
	};

	private void showPopup() {
		
		boolean oldIsVisible = popup.isVisible();

		if (textComp.isEnabled() && control.initializeList(listModel) && listModel.size() > 0) {

			int size = list.getModel().getSize();
			
			list.setVisibleRowCount(size < 10 ? size : 10);

			// If configured to probeAcceptOnOpen... 
			if (probeAcceptOnOpen && !oldIsVisible && control.probeAccept(listModel)) {
				// we will not show the popup, if the probe answers true
			} else {
				// in all other cases, show it
				int x = 0;
				int y = 0;
				int pos = Math.min(textComp.getCaret().getDot(), textComp.getCaret().getMark());
				try {
					Rectangle r = textComp.getUI().modelToView(textComp, pos);
					x = r.x;
					y = r.y;
				} catch (BadLocationException e) {
					// this should never happen!!!
					e.printStackTrace();
				}
				
				popup.show(textComp, x, y + textComp.getFontMetrics(textComp.getFont()).getHeight()
					+ 1);

				if (list.getSelectedIndices().length != 1) {
					list.setSelectedIndices(new int[] { 0 });
				}
				list.ensureIndexIsVisible(list.getSelectedIndex());
			}
			
		} else {
			popup.setVisible(false);
		}
		textComp.requestFocus();
	}

	abstract class AutoCompleterAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if (textComp.isEnabled()) {
				perform();
			}
		}

		public abstract void perform();
	}
	
	abstract class EnablingAutoCompleterAction extends AutoCompleterAction {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (textComp.isEnabled()) {
				if (popup.isVisible())
					perform();
				else
					showPopup();
			}
		}
	}

	Action autoCompleteAction = new EnablingAutoCompleterAction() {
		public void perform() {
			control.probeAccept(listModel);
		}
	};
	
	Action downAction = new EnablingAutoCompleterAction() {
		public void perform() {
			select(1);
		}
	};
	
	Action pageDownAction = new AutoCompleterAction() {
		public void perform() {
			select(list.getVisibleRowCount());
		}
	};

	Action upAction = new AutoCompleterAction() {
		public void perform() {
			if (popup.isVisible())
				select(-1);
		}

	};

	Action pageUpAction = new AutoCompleterAction() {
		public void perform() {
			if (popup.isVisible())
				select(-list.getVisibleRowCount());
		}
	};

	Action hideAction = new AutoCompleterAction() {
		public void perform() {
			popup.setVisible(false);
		}
	};

	protected void select(int jumpSize) {
		int si = list.getSelectedIndex();
		int listSize = list.getModel().getSize();

		if (listSize == 0)
			return;

		if (si < 0) {
			si = 0;
		} else {
			if (si > listSize - 1) {
				si = listSize - 1;
			} else {
				if (jumpSize > 0) {
					if (si < listSize - 1) {
						si = Math.min(si + jumpSize, listSize - 1);
					} else {
						si = 0;
					}
				}
				if (jumpSize < 0) {
					if (si > 0) {
						si = Math.max(si + jumpSize, 0);
					} else {
						si = listSize - 1;
					}
				}
			}
		}

		list.setSelectedIndex(si);
		list.ensureIndexIsVisible(si);

	}
}
