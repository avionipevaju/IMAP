import java.util.ArrayList;
import java.util.Date;

public class Message {
	
	
	private MessageBody mMessageBody;	
	
	//Ne koristimo
	private int mUID;//int je 32 bitni, ne znam kako drugacije da cuvamo ovo?
	private int mUIDValidity,mNextUID;
	//TODO 64 bitna vrednost koja je kombinacija UID i UIDValidity? Long?
	
	
	private int mMessageSequenceNumber;
	private ArrayList<Flag> mFlags;
	private Date mTimeAndDate;//formatiranje?
	private int mSize;//broj okteta
	private ArrayList<String> mEnvelopeStructure;//parsirani heder
	private ArrayList<String> mBodyStructure;//parsirana body struktura
	
	public Message(String mDate, String mFrom, String mSubject, String mTo, String mCc, String mMessageId,
			String mMime, String mContentType) {
		
		MessageHeader header=new MessageHeader(mDate, mFrom, mSubject, mTo, mCc, mMessageId, mMime, mContentType);
		mMessageBody=new MessageBody(header);
		mEnvelopeStructure=header.getHeaderStructure();
		mBodyStructure=mMessageBody.getBodyStructure();
	
	}

}
