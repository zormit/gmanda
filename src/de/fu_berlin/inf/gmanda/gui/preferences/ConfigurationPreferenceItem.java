package de.fu_berlin.inf.gmanda.gui.preferences;

import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.StringUtils.FromConverter;
import de.fu_berlin.inf.gmanda.util.StringUtils.StringConverter;

public class ConfigurationPreferenceItem<T> implements PreferenceItem<T> {

	PreferenceItem<T> defaultValue;

	String key;

	Configuration configuration;

	StringConverter<T> to;

	FromConverter<T> from;

	/**
	 * Create a new preference item, given the configuration key and a static default value.
	 */
	public ConfigurationPreferenceItem(Configuration configuration, String key, T defaultValue,
		StringConverter<T> to, FromConverter<T> from) {

		this.key = key;
		this.defaultValue = new MemoryPreferenceItem<T>(defaultValue);
		this.configuration = configuration;
		this.to = to;
		this.from = from;
	}
	
	/**
	 * Create a new preference item, given the configuration key, but using another 
	 * PreferenceItem as a default value.
	 */
	public ConfigurationPreferenceItem(Configuration configuration, String key, PreferenceItem<T> defaultValue,
		StringConverter<T> to, FromConverter<T> from) {

		this.key = key;
		this.defaultValue = defaultValue;
		this.configuration = configuration;
		this.to = to;
		this.from = from;
	}
	
	public String getValueAsString(){
		String s = this.configuration.getProperty(key);
		if (s == null){
			return to.toString(defaultValue.getValue());
		}
		return s;
	}
	
	public void setValueAsString(String s){
		configuration.setProperty(key, s);
	}

	public T getValue() {
		String s = this.configuration.getProperty(key);
		if (s == null){
			return defaultValue.getValue();
		}
		return from.fromString(s);
	}

	public void setValue(T t) {
		configuration.setProperty(key, to.toString(t));
	}
	
}
