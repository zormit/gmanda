package de.fu_berlin.inf.gmanda.gui.misc;

import java.awt.Color;
import java.awt.Dimension;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.google.common.base.Function;

import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.qda.ToHtmlHelper;
import de.fu_berlin.inf.gmanda.util.DeferredVariableProxyListener;
import de.fu_berlin.inf.gmanda.util.VariableProxy;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.gui.InfoBox;

/**
 * Displays the definition of a code beside the CodeCompleter.
 */
public class GmandaInfoBox implements InfoBox<String> {

	Function<String, String> additionalInfo;
	
	JTextPane pane = new JTextPane();
	
	JScrollPane scroll;
	
	JComponent parent;
	
	public JComponent getComponent(){
		return scroll;
	}
	
	public VariableProxy<String> codeToDisplayProxy = new VariableProxy<String>(null);
	
	public VariableProxy<String> getProxy(){
		return codeToDisplayProxy;
	}

	public String tag2html(String tag){
		Project p = project.getVariable();
		if (p == null)
			return "";
		else 
			return ToHtmlHelper.definition(p.getCodeModel(), tag);
	}
	
	ProjectProxy project;
	
	public GmandaInfoBox(ProjectProxy project, GmandaHyperlinkListener linkListener){

		this.project = project;
		
		scroll = new JScrollPane(pane);
		
		scroll.setBorder(null);
		scroll.getVerticalScrollBar().setFocusable(false);
		scroll.getHorizontalScrollBar().setFocusable(false);
		
		scroll.setPreferredSize(new Dimension(300, 200));
		pane.setBackground(new Color(255, 255, 140));
		pane.setContentType("text/html");
		pane.setEditable(false);
		pane.addHyperlinkListener(linkListener);
		
		codeToDisplayProxy.add(new DeferredVariableProxyListener<String>(new VariableProxyListener<String>(){
			public void setVariable(String newValue) {
				pane.setText(tag2html(newValue));
			}
		}, 500, TimeUnit.MILLISECONDS));
	}
	
	public void show(String t){
		pane.setText("Calculating...");
		codeToDisplayProxy.setVariable(t);
	}
}
