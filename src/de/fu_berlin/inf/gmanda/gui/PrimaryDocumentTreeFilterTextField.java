package de.fu_berlin.inf.gmanda.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.fu_berlin.inf.gmanda.gui.misc.CodeCompleter;
import de.fu_berlin.inf.gmanda.proxies.Filter;
import de.fu_berlin.inf.gmanda.proxies.FilterKindProxy;
import de.fu_berlin.inf.gmanda.proxies.FilterProxy;
import de.fu_berlin.inf.gmanda.proxies.FilterTextProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.gui.AutoCompleter;
import de.fu_berlin.inf.gmanda.util.gui.ProxyTextComponentBridge;

public class PrimaryDocumentTreeFilterTextField extends JPanel {

	JLabel label = new JLabel("Filter by code:");
	JLabel filterDescriptor = new JLabel("Currently displaying:");
	
	JTextField textField = new JTextField();

	JButton filterKindButton;
	JButton resetButton = new JButton(new AbstractAction("Reset") {
		public void actionPerformed(ActionEvent arg0) {
			textField.setText("");
		}
	});
	JButton nextHitButton;
	JButton previousHitButton;

	AutoCompleter<String> completer;
	
	public enum FilterKind {
		SINGLE("Single all hits out", "public_co.gif"),
		ROOT("Use hits as Root", "sub_co.gif"), 
		THREAD("Display whole Thread of hits", "hierarchy_co.gif");

		String tooltip;
		public ImageIcon normal;
		public ImageIcon disabled;

		private FilterKind(String tooltip, String iconName) {
			this.tooltip = tooltip;
			normal = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"resources/icons/" + iconName));
			disabled = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"resources/icons/disabled/" + iconName));
		}
	}

	FilterKind filterKind = FilterKind.THREAD;

	ProxyTextComponentBridge bridge;
	
	public class Button extends JButton {
		public Button(Action a){
			super(a);
			setBorderPainted(false);
			setMinimumSize(new Dimension(24,24));
			setPreferredSize(new Dimension(28,28));
			setPreferredSize(new Dimension(28,28));
			setFocusPainted(false);
			setFocusable(false);
		}
	}
	
	String tooltip = "<html>Only show documents that match the codes entered entered.<br>" +
	"To find documents that contain several codes separated them by <code>,</code> (works like AND).<br>" +
	"The following special syntax may be used:" +
	"<ul>" +
	"  <li><code>search: \"search text\"</code> Search the text (instead of the codes) of the message<br>" +
	"		for the given keywords. The syntax is the same as Google's.</li>" + 
	"  <li><code>*</code> Show all documents that have any code at all.</li>" +
	"  <li><code>-</code> Show all documents that do not contain the given code or search.<br>Caution: This might be slow.</li>" +
	"  <li><code>|</code> Add the documents to the results (works like OR)." +
	"</ul></html>";
	
	public PrimaryDocumentTreeFilterTextField(
		ProjectProxy projectProxy, 
		FilterTextProxy filter,
		final FilterKindProxy filterKind,
		FilterProxy filterProxy,
		final PrimaryDocumentTree tree) {
		super();
		
		setToolTipText(tooltip);
		textField.setToolTipText(tooltip);
		resetButton.setToolTipText(tooltip);
		
		nextHitButton = new Button(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				tree.nextFilterItem();
			}
		});
		nextHitButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("resources/icons/search_next.gif")));
		nextHitButton.setDisabledIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("resources/icons/disabled/search_next.gif")));
		
		previousHitButton = new Button(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				tree.previousFilterItem();
			}
		});
		previousHitButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("resources/icons/search_prev.gif")));
		previousHitButton.setDisabledIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("resources/icons/disabled/search_prev.gif")));

		filterKindButton = new Button(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {

				FilterKind filterK = filterKind.getVariable();

				switch (filterK) {
				case SINGLE:
					filterK = FilterKind.ROOT;
					break;
				case ROOT:
					filterK = FilterKind.THREAD;
					break;
				case THREAD:
					filterK = FilterKind.SINGLE;
					break;
				}
				filterKind.setVariable(filterK);
			}
		});
		
		filterKind.addAndNotify(new VariableProxyListener<FilterKind>() {
			public void setVariable(FilterKind newValue) {
				filterKindButton.setIcon(newValue.normal);
				filterKindButton.setDisabledIcon(newValue.disabled);
				filterKindButton.setToolTipText(newValue.tooltip);
			}
		});
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(		
			
			layout.createParallelGroup()
				.addGroup(
					layout.createSequentialGroup()
						.addComponent(label)
						.addComponent(textField, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE,  Short.MAX_VALUE)
				 		.addComponent(filterKindButton, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
				 		.addComponent(nextHitButton, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
						.addComponent(previousHitButton, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
				 		.addComponent(resetButton)
				).addComponent(filterDescriptor, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE,  Short.MAX_VALUE));
		
		layout.setVerticalGroup(
			layout.createSequentialGroup().addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(label)
					.addComponent(textField)
					.addComponent(filterKindButton)
					.addComponent(nextHitButton)
					.addComponent(previousHitButton)
					.addComponent(resetButton))
				.addComponent(filterDescriptor));
					
		bridge = new ProxyTextComponentBridge(textField, filter);
		
		completer = new AutoCompleter<String>(new CodeCompleter(textField, projectProxy));

		// Only enable textfield and reset button if there is a project
		projectProxy.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				textField.setEnabled(newValue != null);
				resetButton.setEnabled(newValue != null);
			}
		});
		
		// Only enable next and previous buttons if we have a list of filter results... 
		filterProxy.add(new VariableProxyListener<Filter>(){
			public void setVariable(Filter newValue) {
				
				String filterDescriptorLabel = "";
				
				if (newValue != null){
					if (newValue.fresh){
						textField.setBackground(Color.WHITE);
					} else {
						textField.setBackground(Color.LIGHT_GRAY);
					}
					
					if (newValue.filterText != null){
						filterDescriptorLabel += String.format("Filtering for term '%s' (%d results", newValue.filterText, newValue.filterResult.size());
						if (!newValue.fresh){
							filterDescriptorLabel += " from cache)";
						} else {
							filterDescriptorLabel += ")";
						}
					} else {
						filterDescriptorLabel += "No search string";
					}
					
				} else {
					filterDescriptorLabel += "No filter";
				}
				
				filterDescriptor.setText(filterDescriptorLabel);
				
				boolean enable = newValue != null && newValue.filterResult.size() > 0;
				
				nextHitButton.setEnabled(enable);
				previousHitButton.setEnabled(enable);
			}
		});
	}
}
