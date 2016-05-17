import java.util.ArrayList;
import java.util.Date;

public class Message {
	
	
	private MessageBody mInstance;	
	
	//---------------------------Ne koristimo-----------------------------------------
//	private int mUID;//int je 32 bitni, ne znam kako drugacije da cuvamo ovo?
//	private int mUIDValidity,mNextUID;
//	//TODO 64 bitna vrednost koja je kombinacija UID i UIDValidity? Long?
//	private Date mTimeAndDate;//formatiranje?
//	private int mSize;//broj okteta
//	private ArrayList<String> mEnvelopeStructure;//parsirani heder
//	private ArrayList<String> mBodyStructure;//parsirana body struktura
	//--------------------------------------------------------------------------------
	
	private int mMessageSequenceNumber;
	private ArrayList<Flag> mFlags;
	
	
	public Message(MessageHeader header, ArrayList<String> body, int identfier) {
		
		mInstance=new MessageBody(header, body);
		mMessageSequenceNumber=identfier;
		mFlags=new ArrayList<>();
//		mEnvelopeStructure=header.getHeaderStructure();
//		mBodyStructure=mInstance.getBodyStructure();
	
	}

	public MessageBody getInstance() {
		return mInstance;
	}

	public ArrayList<Flag> getFlags() {
		return mFlags;
	}

	public void setFlags(ArrayList<Flag> mFlags) {
		this.mFlags = mFlags;
	}

	public int getMessageSequenceNumber() {
		return mMessageSequenceNumber;
	}

	
	
	
}
