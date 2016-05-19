package Model;

import java.util.Properties;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JOptionPane;

public class Mailbox {

	private String mUser, mPass;
	private Folder mInbox, mSent, mTrash;
	private Server mServer = null;

	public Mailbox(String user, String pass) {
		mUser = user;
		mPass = pass;
		read();
	}
	
	public Mailbox(String user, String pass, Server server) {
		mUser = user;
		mPass = pass;
		mServer = server;
		read();
		

	}

	private void read() {

		Properties properties = new Properties();
		properties.put("mail.imap.port", "993");
		properties.put("mail.imaps.ssl.trust", "*");
		properties.setProperty("mail.store.protocol", "imaps");

		try {
			Session session = Session.getInstance(properties, null);
			Store store = session.getStore();
			store.connect("imap-mail.outlook.com", mUser, mPass);
			mInbox = store.getFolder("INBOX");
			mSent = store.getFolder("SENT");
			mTrash = store.getFolder("DELETED");
			if (mServer != null)
				mServer.setLoggedIn(true);

		} catch (Exception mex) {
			System.out.println(mServer.isLoggedIn());
			if (mServer != null)
				mServer.setLoggedIn(false);
			JOptionPane.showMessageDialog(null, "LOGIN FAILED");
			
		}
		

	}

	public Folder getInbox() {
		return mInbox;
	}

	public Folder getSent() {
		return mSent;
	}

	public Folder getTrash() {
		return mTrash;
	}

	public Folder getFolder(String name) {
		if (name.equalsIgnoreCase("INBOX"))
			return mInbox;
		if (name.equalsIgnoreCase("SENT"))
			return mSent;
		if (name.equalsIgnoreCase("DELETED"))
			return mTrash;
		return null;
	}

	public int recentCount(Folder folder) {
		int counter = 0;
		try {
			for (int i = 1; i <= folder.getMessageCount(); i++) {
				if (folder.getMessage(i).getFlags().contains(Flag.RECENT))
					counter++;
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return counter;
	}

	public int unseenCount(Folder folder) {
		int counter = 0;
		try {
			for (int i = 1; i <= folder.getMessageCount(); i++) {
				if (!folder.getMessage(i).getFlags().contains(Flag.SEEN))
					counter++;
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return counter;
	}

	public String definedFlags(Folder folder) {
		if (folder.equals(mInbox))
			return "SEEN, DELETE, ANSWERED, FLAGGED, RECENT";
		if (folder.equals(mSent))
			return " DELETE, FLAGGED";
		if (folder.equals(mTrash))
			return "SEEN, DELETE, ANSWERED, FLAGGED";
		return null;

	}

}
