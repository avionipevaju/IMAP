import java.util.ArrayList;

public class Response {

	public ArrayList<String> mResponses;
	private final String mClientConst = "C: ";

	public Response() {
		mResponses = new ArrayList<>();
	}

	public void addResponse(String response) {
		mResponses.add(response);
	}

	public void showResponse() {

		if (mResponses.size() > 1) {

			System.out.print(mClientConst);

			for (int i = 0; i < mResponses.size() - 1; i++) {
				System.out.println(mResponses.get(i));
			}
		}
		System.out.println(mResponses.get(mResponses.size() - 1));
	}

}
