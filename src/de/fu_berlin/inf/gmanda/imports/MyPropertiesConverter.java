/*
 * Copyright (C) 2004, 2005 Joe Walnes.
 * Copyright (C) 2006, 2007 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 23. February 2004 by Joe Walnes
 */
package de.fu_berlin.inf.gmanda.imports;

import java.util.Properties;
import java.util.TreeSet;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Special converter for java.util.Properties that stores
 * properties in a more compact form than java.util.Map.
 * <p/>
 * <p>Because all entries of a Properties instance
 * are Strings, a single element is used for each property
 * with two attributes; one for key and one for value.</p>
 * <p>Additionally, default properties are also serialized, if they are present.</p>
 *
 * @author Joe Walnes
 * @author Kevin Ring
 */
public class MyPropertiesConverter implements Converter {

    @SuppressWarnings("unchecked")
	public  boolean canConvert(Class type) {
        return Properties.class == type;
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        Properties properties = (Properties) source;
        
        TreeSet<String> s = new TreeSet<String>(properties.stringPropertyNames());
        
        for (String key : s){
            String value = properties.getProperty(key);
            writer.startNode("property");
            writer.addAttribute("name", key);
            writer.addAttribute("value", value.replaceAll("\r\n", "\r"));
            writer.endNode();
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Properties properties = new Properties();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String name = reader.getAttribute("name");
            String value = reader.getAttribute("value");
            properties.setProperty(name, value);
            reader.moveUp();
        }
        return properties;
    }

}