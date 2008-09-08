package de.fu_berlin.inf.gmanda.util.gui;

import java.awt.Component;

public interface InfoBox<T> {

	public void show(T t);

	public Component getComponent();
	
}
