package OldIMAP;
import java.util.ArrayList;

public class Response {

	public ArrayList<String> mResponses;

	public Response() {
		mResponses = new ArrayList<>();
	}

	public void addResponse(String response) {
		mResponses.add(response);
	}

	public void showResponse() {

		if (mResponses.size() > 1) {

			for (int i = 0; i < mResponses.size() - 1; i++) {
				System.out.println(mResponses.get(i));
			}
		}
		System.out.println(mResponses.get(mResponses.size() - 1));
	}

}
