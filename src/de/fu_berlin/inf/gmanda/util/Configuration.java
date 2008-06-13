/*
 * Created on 27.12.2004
 *
 */
package de.fu_berlin.inf.gmanda.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import de.fu_berlin.inf.gmanda.startup.Stoppable;


/**
 * @author oezbek
 * 
 */
public class Configuration extends Properties implements Stoppable {

    // Use this StateChangeNotifier if you want to be informed before the
    // Properties are saved.
    public StateChangeNotifier<Configuration> beforeCloseNotifier;

    public Configuration() {
        super();
        beforeCloseNotifier = new StateChangeNotifier<Configuration>();
        try {
            load(new FileInputStream("configuration.dat"));
        } catch (IOException e) {
            // The configuration is not valid then.
            // But that does not matter.
        }
    }

    public void save() {
        // Tell everybody that we are going to save ourselves.
        beforeCloseNotifier.notify(this);
        try {
            store(new FileOutputStream("configuration.dat"), null);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

	public void stop() {
		save();
	}

}