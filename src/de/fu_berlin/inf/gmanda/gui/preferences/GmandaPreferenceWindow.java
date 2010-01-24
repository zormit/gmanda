package de.fu_berlin.inf.gmanda.gui.preferences;

import java.util.Arrays;
import java.util.List;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;

/**
 * Window for showing preference items for GmanDA
 */
public class GmandaPreferenceWindow extends PreferenceUIFrame {

	public GmandaPreferenceWindow(ScrollOnShowPreferenceUI sosPUI,
			DebugModePreferenceUI dmPUI, CommonService commonService) {
		super("Preferences", asList(sosPUI, dmPUI), commonService);
	}

	public static List<PreferenceUI<?>> asList(PreferenceUI<?>... uis) {
		return Arrays.asList(uis);
	}
}