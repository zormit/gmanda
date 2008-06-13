/**
 * 
 */
package de.fu_berlin.inf.gmanda.util.preferences;

import java.io.File;

public class FileConverter extends XStreamConverter<File> {

	public FileConverter() {
		super(new com.thoughtworks.xstream.converters.extended.FileConverter());
	}

}