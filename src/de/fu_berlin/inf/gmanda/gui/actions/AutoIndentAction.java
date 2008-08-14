package de.fu_berlin.inf.gmanda.gui.actions;
/** 
 * MySwing: Advanced Swing Utilites 
 * Copyright (C) 2005  Santhosh Kumar T 
 * <p/> 
 * This library is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version. 
 * <p/> 
 * This library is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
 * Lesser General Public License for more details. 
 */ 
 
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import de.fu_berlin.inf.gmanda.util.StringUtils;
 
/** 
 * @author Santhosh Kumar T 
 * @email santhosh@fiorano.com 
 */ 
public class AutoIndentAction extends AbstractAction { 
	
	String indentChars;
	
	int indentLevel;
	
	public AutoIndentAction(String indentChars, int indentLevel){
		this.indentChars = indentChars;
		this.indentLevel = indentLevel;
	}
	
	
    public void actionPerformed(ActionEvent ae) { 
        JTextArea comp = (JTextArea)ae.getSource(); 
        Document doc = comp.getDocument(); 
 
        if(!comp.isEditable()) 
            return; 
        try { 
            int lineNumber = comp.getLineOfOffset(comp.getCaretPosition()); 
 
            int start = comp.getLineStartOffset(lineNumber); 
            int end = comp.getLineEndOffset(lineNumber); 
            String str = doc.getText(start, end - start - 1); 
            
            String whiteSpace = getLeadingWhiteSpace(str); 
            
            str = str.trim();
            if (indentChars != null && str.length() > 0){
            	char c = str.charAt(str.length() - 1);
            	if (indentChars.indexOf(c) != -1){
                	whiteSpace = whiteSpace + StringUtils.spaces(indentLevel);
                }
            }
            
            doc.insertString(comp.getCaretPosition(), '\n' + whiteSpace, null); 
        } catch(BadLocationException ex) { 
            try { 
                doc.insertString(comp.getCaretPosition(), "\n", null); 
            } catch(BadLocationException ignore) { 
                // ignore 
            } 
        } 
    } 
 
    /** 
     *  Returns leading white space characters in the specified string. 
     */ 
    private String getLeadingWhiteSpace(String str) { 
        return str.substring(0, getLeadingWhiteSpaceWidth(str)); 
    } 
 
    /** 
     *  Returns the number of leading white space characters in the specified string. 
     */ 
    private int getLeadingWhiteSpaceWidth(String str) { 
        int whitespace = 0; 
        while(whitespace<str.length()) { 
            char ch = str.charAt(whitespace); 
            if(ch==' ' || ch=='\t') 
                whitespace++; 
            else 
                break; 
        } 
        return whitespace; 
    } 
} 