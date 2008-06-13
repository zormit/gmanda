package de.fu_berlin.inf.gmanda.util.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import de.fu_berlin.inf.gmanda.util.VariableProxy;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

/**
 * Connect a TextComponent and a String Proxy for bidirectional updates.
 */
public class ProxyTextComponentBridge {
	
	boolean sendChanges = true;
	
	public ProxyTextComponentBridge(final JTextComponent textComponent, final VariableProxy<String> proxy){
		
		final VariableProxyListener<String> listener = new VariableProxyListener<String>(){
			public synchronized void setVariable(String newValue) {
				sendChanges = false;
				textComponent.setText(newValue);
				sendChanges = true;
			}
		};
		
		proxy.add(listener);
		
		textComponent.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {
				update();
			}

			public void insertUpdate(DocumentEvent arg0) {
				update();
			}

			public void removeUpdate(DocumentEvent arg0) {
				update();
			}

			public void update() {
				if (sendChanges)
					proxy.setVariable(textComponent.getText(), listener);
			}
		});
	}
}
