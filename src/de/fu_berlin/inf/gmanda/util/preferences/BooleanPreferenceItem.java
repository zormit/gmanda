/**
 * 
 */
package de.fu_berlin.inf.gmanda.util.preferences;

import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.CStringUtils.FromConverter;
import de.fu_berlin.inf.gmanda.util.CStringUtils.PlainConverter;

public class BooleanPreferenceItem extends ConfigurationPreferenceItem<Boolean> {
	public BooleanPreferenceItem(Configuration conf, String key, Boolean defaultValue) {
		super(conf, key, Boolean.valueOf(defaultValue), new PlainConverter<Boolean>(),
			new FromConverter<Boolean>() {
				public Boolean fromString(String s) {
					return Boolean.valueOf(s);
				}
			});
	}
}