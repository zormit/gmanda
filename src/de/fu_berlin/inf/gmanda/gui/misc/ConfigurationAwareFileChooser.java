package de.fu_berlin.inf.gmanda.gui.misc;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import de.fu_berlin.inf.gmanda.util.Configuration;

public class ConfigurationAwareFileChooser<T extends ExtensionDescriptor> extends JFileChooser {

	public class DescriptorFileFilter extends FileFilter {
		protected final T descriptor;

		public DescriptorFileFilter(T descriptor) {
			this.descriptor = descriptor;
		}

		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}
			
			return endsWith(f.getName(), descriptor.getSupportedExtensions());
		}

		@Override
		public String getDescription() {
			return descriptor.getDescription();
		}

		public T getDescriptor(){
			return descriptor;
		}
	}

	Configuration configuration;

	HashMap<FileFilter, T> extensionMap = new HashMap<FileFilter, T>();

	String property;

	public ConfigurationAwareFileChooser(Configuration configuration,
			String configurationPropertyName, String extension,
			String description) {

		this(configuration, configurationPropertyName,
				new ExtensionDescription().append(description, extension));
	}

	@SuppressWarnings("unchecked")
	public ConfigurationAwareFileChooser(Configuration configuration,
			String configurationPropertyName,
			ExtensionDescription descriptionAndExtension) {

		this(configuration, configurationPropertyName,
				(T[])descriptionAndExtension.toDescriptors()
						.toArray(
								new ExtensionDescriptor[descriptionAndExtension
										.size()]));
	}

	public ConfigurationAwareFileChooser(Configuration configuration,
			String configurationPropertyName,
			T... descriptors) {

		super(configuration.getProperty(configurationPropertyName, null));

		this.configuration = configuration;
		this.property = configurationPropertyName;

		for (final T descriptor : descriptors) {

			if (descriptor.getSupportedExtensions().size() == 0)
				throw new IllegalArgumentException();

			FileFilter ff = new DescriptorFileFilter(descriptor);
			
			addChoosableFileFilter(ff);
			extensionMap.put(ff, descriptor);
		}
	}
	
	public T getSelectedFileType(){
		return extensionMap.get(getFileFilter());
	}

	public File getSaveFile() {

		int returnVal = showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			configuration.setProperty(property, getCurrentDirectory()
					.getAbsolutePath());

			// Append extension if not given
			File f = getSelectedFile();

			T descriptor = getSelectedFileType();
			if (descriptor != null && !endsWith(f.getName(), descriptor.getSupportedExtensions())) {
				return new File(f.getParent(), f.getName() + descriptor.getSupportedExtensions().iterator().next());
			}
			return f;
		} else {
			return null;
		}

	}
	
	public static boolean endsWith(String s, Collection<String> extensions){
		
		for (String extension : extensions){
			if (s.endsWith(extension))
				return true;
		}
		return false;
	}
	

	public File getOpenFile() {

		int returnVal = showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			configuration.setProperty(property, getCurrentDirectory()
					.getAbsolutePath());

			return getSelectedFile();
		} else {
			return null;
		}
	}
}
