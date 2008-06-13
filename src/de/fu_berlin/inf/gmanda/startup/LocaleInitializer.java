package de.fu_berlin.inf.gmanda.startup;

import java.util.Locale;


public class LocaleInitializer implements Initializer {

	public void initialize() {
		// Currently only US english is supported
		Locale.setDefault(Locale.US);
	}

}
