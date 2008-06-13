package de.fu_berlin.inf.gmanda.imports;

import gnu.mail.providers.mbox.MboxStore;
import gnu.mail.treeutil.StatusEvent;
import gnu.mail.treeutil.StatusListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import de.fu_berlin.inf.gmanda.util.progress.IProgress;
import de.fu_berlin.inf.gmanda.util.progress.IProgress.ProgressStyle;

public class MyMimeUtils {

	public static String getBody(Message message) throws MessagingException {
		
		StringBuilder sb = new StringBuilder();
		try {
			String subject = message.getSubject();
			String from = getFrom(message);  
			String to = getRecipients(message,  Message.RecipientType.TO);
			String cc = getRecipients(message, Message.RecipientType.CC);
			String bcc = getRecipients(message, Message.RecipientType.BCC);
			String date = (message.getSentDate() != null ? new DateTime(message.getSentDate()).toString() : "unknown");
	
			// Build text blob
			sb.append("From: " + from).append("\n");
			if (to != null && to.trim().length() > 0) {
				sb.append("To  : " + to).append("\n");
			}
			if (cc != null && cc.trim().length() > 0) {
				sb.append("CC  : " + cc).append("\n");
			}
			if (bcc != null && bcc.trim().length() > 0) {
				sb.append("BCC : " + bcc).append("\n");
			}
			sb.append("Date: " + date).append("\n");
			sb.append("Subj: " + subject).append("\n");
			sb.append("\n");
			sb.append(toString(message));
		} catch (Exception e) {
			sb.append("Error reading mail content").append("\n");
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			sb.append(sw.toString()).append("\n");
			
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static Folder getFolder(File mboxFile) throws MessagingException{
		return getFolder(mboxFile, null);
	}

	public static Folder getFolder(final IProgress progress, File mboxFile) throws MessagingException {

		progress.setScale(100);
		progress.setStyle(ProgressStyle.DOUBLING);
		progress.setNote("Loading Gmane " + mboxFile.getAbsolutePath());

		Folder folder = MyMimeUtils.getFolder(mboxFile, new StatusListener() {
			public void statusOperationEnded(StatusEvent arg0) {
				progress.done();
			} 

			public void statusOperationStarted(StatusEvent arg0) {
				progress.start();
			}

			public void statusProgressUpdate(StatusEvent arg0) {
				int status = arg0.getValue();
				progress.setProgress(status);
				if (status % 100 == 0)
					progress.setNote("Reading message: " + status);
			}
		});
		
		return folder;
	}
	
	public static Folder getFolder(File mboxFile, StatusListener listener) throws MessagingException {
	
		Session session = Session.getDefaultInstance(new Properties());
		MboxStore store = (MboxStore) session.getStore(new URLName("mbox:" + mboxFile.getAbsolutePath()));
		store.connect();
		if (listener != null)
			store.addStatusListener(listener);
	
		// read messages from Inbox..
		Folder folder = store.getDefaultFolder();
		folder.open(Folder.READ_ONLY);
	
		return folder;
	}

	public static String getFrom(Message m) throws MessagingException{
		try {
			return toString(m.getFrom());
		} catch (AddressException e){
			return toString(m.getHeader("From"));
		}
	}

	public static String getRecipients(Message m, Message.RecipientType type) throws MessagingException{
		try {
			return toString(m.getRecipients(type));
		} catch (AddressException e){
			return toString(m.getHeader(type.toString()));
		}
	}

	public static String toString(Address[] ads) {
		StringBuilder sb = new StringBuilder();
	
		if (ads == null)
			return null;
	
		boolean first = true;
		for (Address a : ads) {
			if (!first)
				sb.append(", ");
			else
				first = false;
	
			if (a instanceof InternetAddress) {
				InternetAddress i = (InternetAddress) a;
				sb.append(i.toUnicodeString());
			} else {
				sb.append(a.toString());
			}
		}
		return sb.toString();
	}

	public static String toString(Multipart mp) throws MessagingException, IOException {
	
		if (mp.getCount() == 0)
			return "";
		
		StringBuilder sb = new StringBuilder();
		
		// We treat everything as multipart/mixed except multipart/alternative
		if (StringUtils.indexOf(mp.getContentType(), "multipart/alternative") == 0){
			// http://tools.ietf.org/html/rfc2046#section-5.1.4 
			
			int bestChoice = -1;
			
			// We prefer the "last" text/plain
			for (int i = 0; i < mp.getCount(); i++) {
				if (StringUtils.indexOf(mp.getBodyPart(i).getContentType(), "text/plain") == 0){
					bestChoice = i;
				}
			}
			// If not found, we just take the last
			if (bestChoice == -1)
				bestChoice = mp.getCount() - 1;
				
			return sb.append(toString(mp.getBodyPart(bestChoice))).append('\n').toString();
		} 
		
		for (int i = 0; i < mp.getCount(); i++) {
			sb.append(toString(mp.getBodyPart(i))).append('\n');
		}
		return sb.toString();
	}

	public static String toString(Part part) throws MessagingException, IOException {
	
		StringBuilder sb = new StringBuilder();
	
		try {
			if (part.getDisposition() != null && !part.getDisposition().equals("inline"))
				sb.append(">-- " + part.getDisposition()).append('\n');
		} catch (ParseException e){
			sb.append(">-- Disposition decoding problem\n");
		}
			
		Object content;
		try {
			content = part.getContent();
		} catch (UnsupportedEncodingException e) {
			content = "Message could not be decoded: " + e.getMessage() + "\n";
		}
	
		if (content instanceof String) {
			sb.append((String) content);
		} else if (content instanceof Message){
			sb.append(getBody((Message)content));
		} else if (content instanceof Multipart) {
			sb.append(toString((Multipart) content));
		} else if (content instanceof Part){
			sb.append(toString((Part)content));
		} else if (content instanceof InputStream) {
			sb.append(IOUtils.toString((InputStream)content));
		} else {
			sb.append("UNKNOWN CONTENT: " + content);
		}
		sb.append('\n');
		
		return sb.toString();
	}

	public static String toString(String[] ss) {
	
		if (ss == null)
			return "";
	
		StringBuilder sb = new StringBuilder();
	
		boolean first = true;
		for (String s : ss) {
			if (!first)
				sb.append("\n");
			else
				first = false;
	
			try {
				sb.append(MimeUtility.decodeText(s));
			} catch (UnsupportedEncodingException e) {
				sb.append(s);
			}
		}
		return sb.toString();
	}

}
