package de.fu_berlin.inf.gmanda.util.preferences;

import de.fu_berlin.inf.gmanda.gui.preferences.CodedColorProperty;
import de.fu_berlin.inf.gmanda.gui.preferences.MatchColorProperty;
import de.fu_berlin.inf.gmanda.gui.preferences.SeenColorProperty;
import de.fu_berlin.inf.gmanda.gui.preferences.SelectedColorProperty;

public class ColorProperties {

	public ColorPreferenceItem coded, seen, selected, match;

	public ColorProperties(CodedColorProperty coded, SeenColorProperty seen,
		MatchColorProperty match, SelectedColorProperty selected) {
		this.coded = coded;
		this.seen = seen;
		this.selected = selected;
		this.match = match;
	}
	
	public ColorPreferenceItem getCoded() {
		return coded;
	}

	public ColorPreferenceItem getSeen() {
		return seen;
	}

	public ColorPreferenceItem getSelected() {
		return selected;
	}

	public ColorPreferenceItem getMatch() {
		return match;
	}
}
