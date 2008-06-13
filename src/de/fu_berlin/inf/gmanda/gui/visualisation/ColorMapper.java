package de.fu_berlin.inf.gmanda.gui.visualisation;

import java.awt.Color;
import java.util.HashMap;

public class ColorMapper {

	HashMap<String, Color> colors = new HashMap<String, Color>();

	public ColorMapper() {

		colors.put("green", Color.green);
		colors.put("yellow", Color.yellow);
		colors.put("red", Color.red);
		colors.put("blue", Color.blue);
		colors.put("white", Color.white);
		colors.put("black", Color.black);
		colors.put("orange", Color.orange);
		colors.put("cyan", Color.cyan);
		colors.put("darkgray", Color.darkGray);
		colors.put("gray", Color.gray);
		colors.put("lightgray", Color.lightGray);
		colors.put("magenta", Color.magenta);
		colors.put("pink", Color.pink);
		colors.put("blue", Color.blue);
	}

	public Color getColor(String s, Color defaultColor) {
		try {
			return Color.decode(s);
		} catch (NumberFormatException e) {

		}
		if (colors.containsKey(s))
			return colors.get(s);
		return defaultColor;
	}

}
