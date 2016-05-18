package OldIMAP;
import java.util.ArrayList;

import javax.swing.text.AttributeSet.CharacterAttribute;

public class MessageCreator {

	private ArrayList<Message> mFakeMessages;
	private int mMessageSequenceNumber=0;
	
	public MessageCreator() {
		mFakeMessages=new ArrayList<>();
		create();
	}

	public void create() {
		
		String mDate="Wed, 17 Jul 1996 02:23:25 -0700 (PDT";
		String mFrom="Terry Gray <gray@cac.washington.edu>";
		String mSubject="IMAP4rev1 WG mtg summary and minutes";
		String mTo="imap@cac.washington.edu";
		String mCc="minutes@CNRI.Reston.VA.US, John Klensin <KLENSIN@MIT.EDU>";
		String mMessageId="<B27397-0100000@cac.washington.edu>";
		String mMime="1.0";
		String mContentType="TEXT/PLAIN; CHARSET=US-ASCII";
		
		MessageHeader header=new MessageHeader(mDate, mFrom, mSubject, mTo, mCc, mMessageId, mMime, mContentType);
		
		ArrayList<String> body=new ArrayList<>();
		body.add("Hello, ");
		body.add(" ");
		body.add("I am a dummy. BLA BLA BLA linija teksta");
		body.add(" ");
		body.add("Best regards,");
		body.add("Dummy");
		
		Message temp=new Message(header, body,mMessageSequenceNumber);
		mFakeMessages.add(temp);
		mMessageSequenceNumber++;
		
		
		mDate="Mon, 12 Feb 2006 03:21:22 -0700 (PDT";
		mFrom="Jerry Grahm <nikola@raf.edu.rs>";
		mSubject="Fake Messaging protocol on the loose";
		mTo="neko@cyberpolice.rs";
		mCc="jfk@MUP.BELGRADE.RS, Milovan Stepandic <Glavoja@MUP.GOV>";
		mMessageId="<B273923-01005780@raf.edu.rs>";
		mMime="1.0";
		mContentType="TEXT/PLAIN; CHARSET=US-ASCII";
		
		header=new MessageHeader(mDate, mFrom, mSubject, mTo, mCc, mMessageId, mMime, mContentType);
		
		body=new ArrayList<>();
		body.add("Postovani, ");
		body.add(" ");
		body.add("Prosledjujem vam kriticno obavestenje vezano za bezbednost naseg internog protokola i tako to na tu temu.");
		body.add("X1 probi kontra napad, borba bre, sta je ovo! Sledeci napad kruga za ovog....Urosa.");
		body.add(" ");
		body.add("Pozdrav,");
		body.add("Dzoni aka Mnogo sam dLu");
		
		temp=new Message(header, body,mMessageSequenceNumber);
		mFakeMessages.add(temp);
		mMessageSequenceNumber++;
		
		
	}
	
	public void setMessageInboxFlags(Message message){
		ArrayList<Flag> tempFlags=new ArrayList<>();
		tempFlags.add(Flag.SEEN);
		tempFlags.add(Flag.RECENT);
		message.setFlags(tempFlags);
	}
	
	public void setMessageSentFlags(Message message){
		ArrayList<Flag> tempFlags=new ArrayList<>();
		tempFlags.add(Flag.SEEN);
		tempFlags.add(Flag.ANSWERED);
		message.setFlags(tempFlags);
	}
	
	public void setMessageTrashFlags(Message message){
		ArrayList<Flag> tempFlags=new ArrayList<>();
		tempFlags.add(Flag.DELETED);
		message.setFlags(tempFlags);
	}
	
	public void setMessageDraftFlags(Message message){
		ArrayList<Flag> tempFlags=new ArrayList<>();
		tempFlags.add(Flag.DRAFT);
		message.setFlags(tempFlags);
	}


	public ArrayList<Message> getFakeMessages() {
		return mFakeMessages;
	}
	
	
}
