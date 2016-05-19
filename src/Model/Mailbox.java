package Model;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JOptionPane;

import com.sun.mail.auth.MD4;
import com.sun.mail.imap.protocol.FLAGS;

public class Mailbox {
	
	private String mUser,mPass;
	private Folder mInbox,mSent,mTrash;
	
	public Mailbox(String user,String pass) {
		mUser=user;
		mPass=pass;
		read();
	}
	

	
	private void read() {
		
		Properties properties=new Properties();
		properties.put("mail.imap.port", "993");
		properties.put("mail.imaps.ssl.trust", "*");
		properties.setProperty("mail.store.protocol", "imaps");
		
		try {
            Session session = Session.getInstance(properties, null);
            Store store = session.getStore();
            store.connect("imap-mail.outlook.com", mUser, mPass);
            mInbox = store.getFolder("INBOX");
            mSent=store.getFolder("SENT");
            mTrash=store.getFolder("DELETED");
            
            //U konzoli ispisuje prvi mail
//            mInbox.open(Folder.READ_ONLY);
//            Message msg = mInbox.getMessage(mInbox.getMessageCount());
//            Address[] in = msg.getFrom();
//            for (Address address : in) {
//                System.out.println("FROM:" + address.toString());
//            }
//            Multipart mp = (Multipart) msg.getContent();
//            BodyPart bp = mp.getBodyPart(0);
//            System.out.println("SENT DATE:" + msg.getSentDate());
//            System.out.println("SUBJECT:" + msg.getSubject());
//            System.out.println("CONTENT:" + bp.getContent());
        } catch (Exception mex) {
        	mex.printStackTrace();
        	JOptionPane.showMessageDialog(null, "LOGIN FAILED");
        	System.exit(1);
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
			for (int i = 0; i < folder.getMessageCount(); i++) {
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
			for (int i = 0; i < folder.getMessageCount(); i++) {
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
