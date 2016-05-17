import java.util.ArrayList;

public class MessageBody {

	private ArrayList<String> mBody;
	private MessageHeader mHeader;

	public MessageBody(MessageHeader header,ArrayList<String> body) {
		mBody = body;
		mHeader = header;

	}

	public void addText(String string) {
		mBody.add(string);
	}

	public ArrayList<String> getBodyStructure() {
		ArrayList<String> temp = new ArrayList<>();

		for (String headerField : mHeader.getHeaderStructure()) {
			temp.add(headerField);
		}
		
		for (String textLine : mBody) {
			temp.add(textLine);
		}
		
		return temp;

	}

	public ArrayList<String> getBody() {
		return mBody;
	}

	public MessageHeader getHeader() {
		return mHeader;
	}
	
	

}
