package OldIMAP;
import java.util.ArrayList;

public class Mailbox {
	
	private ArrayList<Message> mMessages;
	private String mName;
	private ArrayList<Flag> mDefinedFlags;
	private ArrayList<Flag> mPermanentFlags;
	
	public Mailbox(String name) {
		mMessages = new ArrayList<>();
		mName = name;
		
	}
	
	public int exists() {
		return mMessages.size();
	}
	
	public int recent() {
		int counter = 0;
		for (Message m : mMessages) {
			//TODO provera za Recent flagove
			//if () {
			//	counter++;
			//}
		}
		return counter;
	}
	
	public int unseen() {
		for(int i = mMessages.size()-1; i >= 0; i--) {
			//TODO provera za Unseen flag
			//if (mMessages.get(i))
			//	return i;
		}
		return -1;
	}
	
	public String getName(){
		return mName;
	}
	
	public void setName(String newName) {
		mName = newName;
	}

	public ArrayList<Flag> getDefinedFlags() {
		return mDefinedFlags;
	}

	public void setDefinedFlags(ArrayList<Flag> definedFlags) {
		mDefinedFlags = definedFlags;
	}
	
	public String toStringDefinedFlags() {
		return mDefinedFlags.toString();
	}

	public ArrayList<Flag> getPermanentFlags() {
		return mPermanentFlags;
	}

	public void setPermanentFlags(ArrayList<Flag> permanentFlags) {
		this.mPermanentFlags = permanentFlags;
	}
	
	public String toStringPermanentFlags() {
		return mPermanentFlags.toString();
	}
	
	public void addMessage(Message message){
		mMessages.add(message);
	}

	public ArrayList<Message> getMessages() {
		return mMessages;
	}
	
	
}
