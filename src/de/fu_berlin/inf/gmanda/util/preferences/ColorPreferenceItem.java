/**
 * 
 */
package de.fu_berlin.inf.gmanda.util.preferences;

import java.awt.Color;

import de.fu_berlin.inf.gmanda.gui.preferences.ConfigurationPreferenceItem;
import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.StringUtils.FromConverter;
import de.fu_berlin.inf.gmanda.util.StringUtils.StringConverter;

public class ColorPreferenceItem extends ConfigurationPreferenceItem<Color> {
	public ColorPreferenceItem(Configuration conf, String key, Color defaultValue) {
		super(conf, key, defaultValue, new StringConverter<Color>(){
			public String toString(Color t) {
				return "#" + String.format("%06x", t.getRGB() & 0xFFFFFF);
			}
		}, 
		new FromConverter<Color>() {
				public Color fromString(String s) {
					return new Color(Integer.decode(s));
				}
			});
	}
}