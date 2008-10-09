package de.fu_berlin.inf.gmanda.util.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import de.fu_berlin.inf.gmanda.util.CStringUtils;

/**
 * Taken from Salma Hayek by software@jessies.org
 * 
 * Published under the LGPL
 * 
 * http://software.jessies.org/salma-hayek/
 */
public class HighlightSupport {
    
	DefaultHighlighter.DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);

	JTextComponent textComponent;
	
	public void goToSelection(final int startOffset, final int endOffset) {
        if (EventQueue.isDispatchThread()) {
            doGoToSelection(startOffset, endOffset);
        } else {
            try {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        doGoToSelection(startOffset, endOffset);
                    }
                });
            } catch (Throwable th) {
            	// Should warn
            }
        }
    }
	
	public void ensureVisibilityOfOffset(int offset) {
        JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, textComponent);
        if (viewport == null) {
            return;
        }
        int height = viewport.getExtentSize().height;
        try {
            Rectangle viewRectangle = textComponent.modelToView(offset);
            if (viewRectangle == null) {
                return;
            }
            int y = viewRectangle.y - height/2;
            y = Math.max(0, y);
            y = Math.min(y, textComponent.getHeight() - height);
            viewport.setViewPosition(new Point(0, y));
        } catch (javax.swing.text.BadLocationException ex) {
            // should warn
        }
    }
    
    /**
     * Use goToSelection instead. This method is not thread-safe.
     */
    private void doGoToSelection(int startOffset, int endOffset) {
        ensureVisibilityOfOffset(startOffset);
        textComponent.select(startOffset, endOffset);
    }
	
	public HighlightSupport(JTextComponent component) {
		this.textComponent = component;
	}
    
    public void removeHighlights() {
        Highlighter highlighter = textComponent.getHighlighter();
        for (Highlighter.Highlight highlight : highlighter.getHighlights()) {
            if (highlight.getPainter() == painter) {
                highlighter.removeHighlight(highlight);
            }
        }
    }
    
    public void findNextHighlight(Position.Bias bias) {
        Highlighter highlighter = textComponent.getHighlighter();
        Highlighter.Highlight[] highlights = highlighter.getHighlights();
        boolean forwards = (bias == Position.Bias.Forward);
        int start = forwards ? 0 : highlights.length - 1;
        int stop = forwards ? highlights.length : -1;
        int step = forwards ? 1 : -1;
        for (int i = start; i != stop; i += step) {
            if (highlights[i].getPainter() == painter) {
                if (highlighterIsNext(forwards, highlights[i])) {
                    goToSelection(highlights[i].getStartOffset(), highlights[i].getEndOffset());
                    return;
                }
            }
        }
    }
    
    public boolean highlighterIsNext(boolean forwards, Highlighter.Highlight highlight) {
        final int minOffset = Math.min(highlight.getStartOffset(), highlight.getEndOffset());
        final int maxOffset = Math.max(highlight.getStartOffset(), highlight.getEndOffset());
        if (forwards) {
            return minOffset > textComponent.getSelectionEnd();
        } else {
            return maxOffset < textComponent.getSelectionStart();
        }
    }
    
    public void findAllMatches(Pattern pattern) {
        removeHighlights();
        
        String content = textComponent.getText();
        if (content == null) {
            return;
        }
        
        Highlighter highlighter = textComponent.getHighlighter();
        
		StringBuilder sb = new StringBuilder();
		
        HTMLDocument document = (HTMLDocument) textComponent.getDocument();
        for (HTMLDocument.Iterator it = document.getIterator(HTML.Tag.CONTENT); it.isValid(); it.next()) {
            try {
            	int start = it.getStartOffset();
            	int end = it.getEndOffset();
            	
            	String fragment = document.getText(start, end - start);
                
                if (sb.length() < start)
                	sb.append(CStringUtils.spaces(start - sb.length()));
		        
                sb.insert(start, fragment);
            } catch (BadLocationException ex) {
            }
        }
        
        String text = sb.toString();
        
        text = text.replaceAll("\\p{Punct}", " ");
        
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            try {
				highlighter.addHighlight(matcher.start(), matcher.end(), painter);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
        }
    }
    
    

}