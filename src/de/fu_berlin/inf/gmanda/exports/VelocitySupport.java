package de.fu_berlin.inf.gmanda.exports;

import java.io.File;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.gmanda.exceptions.DoNotShowToUserException;
import de.fu_berlin.inf.gmanda.gui.preferences.DebugModeProperty;
import de.fu_berlin.inf.gmanda.util.VelocityWhitespaceRepair;

public class VelocitySupport {

	@Inject
	DebugModeProperty debugMode;

	public String run(String key, Object value, String velocityFile){
		return run(new HashMap<String,Object>(Collections.singletonMap(key, value)), velocityFile);
	}
	
	public String run(Map<String, Object> data, String velocityFile) {

		Template latexTemplate;

		Properties p = new Properties();

		if (debugMode.getValue()) {
			// Enable auto reload
			p.setProperty("resource.loader", "file");
			p.setProperty("file.resource.loader.cache", "false");
			p.setProperty("velocimacro.library.autoreload", "true");
		} else {
			p.setProperty("resource.loader", "class, file");
			p.setProperty("class.resource.loader.class",
					"org.apache.velocity.runtime"
							+ ".resource.loader.ClasspathResourceLoader");
		}

		VelocityEngine engine;
		VelocityContext context;
		try {
			engine = new VelocityEngine(p);
			engine.init();

			context = new VelocityContext(data);

			FileUtils.writeStringToFile(new File("resources/templates/"
					+ velocityFile + "Whitespace.vm"),
					new VelocityWhitespaceRepair().fixWhitespace(FileUtils
							.readFileToString(new File("resources/templates/"
									+ velocityFile + ".vm"))));

			latexTemplate = engine.getTemplate("resources/templates/"
					+ velocityFile + "Whitespace.vm");

		} catch (Exception e) {
			throw new DoNotShowToUserException(e);
		}

		StringWriter writer = new StringWriter();
		try {
			latexTemplate.merge(context, writer);
			writer.close();
		} catch (Exception e) {
			throw new DoNotShowToUserException(e);
		}

		return writer.toString();
	}
}
