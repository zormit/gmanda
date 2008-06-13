package de.fu_berlin.inf.gmanda;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.swing.JFileChooser;

public class Test {

	public static void main(String[] args) throws MessagingException {
		
		Session session = Session.getDefaultInstance(new Properties());

		JFileChooser fc = new JFileChooser("d:/svn/sci/mail.ti/");

		switch (fc.showOpenDialog(null)) {
		case JFileChooser.APPROVE_OPTION:
			Store store = session.getStore(new URLName("mstor:" + fc.getSelectedFile().getAbsolutePath()));
			store.connect();

			// read messages from Inbox..
			Folder folder = store.getDefaultFolder();
			folder.open(Folder.READ_ONLY);

			Message[] messages = folder.getMessages();

			System.out.println(messages.length);

			for (Message message : messages) {
				System.out.println(message.getMessageNumber() + " "
					+ message.getFrom()[0].toString() + ": " + message.getSubject());
			}
		}
	}

}
