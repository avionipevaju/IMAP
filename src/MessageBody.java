import java.util.ArrayList;

public class MessageBody {

	private ArrayList<String> mText;
	private MessageHeader mHeader;

	public MessageBody(MessageHeader header) {
		mText = new ArrayList<>();
		mHeader = header;

	}

	public void addText(String string) {
		mText.add(string);
	}

	public ArrayList<String> getBodyStructure() {
		ArrayList<String> temp = new ArrayList<>();

		for (String headerField : mHeader.getHeaderStructure()) {
			temp.add(headerField);
		}
		
		for (String textLine : mText) {
			temp.add(textLine);
		}
		
		return temp;

	}

}
