package OldIMAP;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

	private Socket mSocket;
	private BufferedReader mSocketIn;
	private PrintWriter mSocketOut;
	private String mTag;
	private char mChar;
	private int mNumeric;
	private final String mConst = "C: ";
	private String mCommand, mRecievedData, mStringResponse, mTagResponse;
	private Scanner in;
	private Response mResponse;
	private ArrayList<Response> mResponseArchive;
	private String mToken="Nil";
	
	public Client() throws Exception {
		mSocket = new Socket("localhost", 2015);

		mSocketIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		mSocketOut = new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream()), true);

		in = new Scanner(System.in);
		mResponseArchive=new ArrayList<>();

		mChar = 'a';
		mNumeric = 0;

		mRecievedData = mSocketIn.readLine();
		System.out.println(mRecievedData);
		
		while(true) {
			
			mCommand = in.nextLine();
			sendCommand(mCommand);

			mResponse=new Response();
			
			while(!parseResponse(mRecievedData = mSocketIn.readLine())){
				mResponse.addResponse(mRecievedData);
			}
			mResponse.addResponse(mRecievedData);
			mResponseArchive.add(mResponse);
			mResponse.showResponse();
			
			if(mToken.equals("+")){
				mCommand=in.nextLine();
				fillCommand(mCommand);
				
				mRecievedData=mSocketIn.readLine();
				mResponse=new Response();
				mResponse.addResponse(mRecievedData);
				mResponseArchive.add(mResponse);
				mResponse.showResponse();
				mToken="Nil";
				
			}
			
			if(mRecievedData.contains("LOGOUT"))
				break;

		}

		mSocket.close();

	}

	public static void main(String[] args) {
		try {
			new Client();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void sendCommand(String command) {
		makeTag(mChar, mNumeric);
		mSocketOut.println(mConst + mTag + " " + command.toUpperCase());
	}
	
	private void fillCommand(String command){
		mSocketOut.println(mConst+command);
	}

	private void makeTag(char c, int n) {
		String numString = String.format("%03d", n);
		mTag = new String("" + mChar + numString);
		mNumeric++;

		if(mNumeric>999){
			mChar++;
			mNumeric=0;
		}
		
	}

	private boolean parseResponse(String response) {

		Scanner temp = new Scanner(response);
		temp.next();
		mTagResponse = temp.next();
		if (mTagResponse.length() != 4){
			if(mTagResponse.equals("+")){
				mToken=mTagResponse;
				return true;
			}
			
			return false;
		}
		mStringResponse = temp.next();
		return true;

	}
	
}
