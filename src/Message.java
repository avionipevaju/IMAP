
public class Message {
	
	
	private String mText;
	private int mUID;//int je 32 bitni, ne znam kako drugacije da cuvamo ovo?
	private int mUIDValidity,mNextUID;
	//TODO 64 bitna vrednost koja je kombinacija UID i UIDValidity? Long?
	private int mMessageSequenceNumber;
	private Flag mFlag;
	
	
	public Message() {
		// TODO Auto-generated constructor stub
	}

}
