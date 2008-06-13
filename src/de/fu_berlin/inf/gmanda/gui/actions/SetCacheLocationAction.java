/*
 * Created on 05.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;

import de.fu_berlin.inf.gmanda.gui.preferences.CacheDirectoryProperty;
import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;

public class SetCacheLocationAction extends AbstractAction {

	CacheDirectoryProperty cacheDirectory;
	
	ForegroundWindowProxy windowProxy;

	public SetCacheLocationAction(
		CacheDirectoryProperty cacheDirectory,
		ForegroundWindowProxy windowProxy) {
		super("Set global cache location...");

		this.cacheDirectory = cacheDirectory;
		this.windowProxy = windowProxy;

		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
	}

	JFileChooser fileChooser;

	public void actionPerformed(ActionEvent arg0) {

		// Run in Background...
		new Thread(new Runnable() {
			public void run() {
				
				if (fileChooser == null) {
					File location = cacheDirectory.getValue();
					fileChooser = new JFileChooser(location);
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				}

				int returnVal = fileChooser.showOpenDialog(windowProxy.getVariable());

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					
					File selectedFile = fileChooser.getSelectedFile();
					if (selectedFile != null) {
						cacheDirectory.setValue(selectedFile.getAbsoluteFile());
					}
				}
			}
		}).start();
	}
}
