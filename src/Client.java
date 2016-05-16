import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	private Socket mSocket;
	private BufferedReader mSocketIn;
	private PrintWriter mSocketOut;
	private String mTag;
	private char mChar;
	private int mNumeric;
	private final String mConst="C: ";
	private String mCommand,mRecievedData;
	private Scanner in;
	

	public Client() throws Exception {
		mSocket =new Socket("localhost", 2015);
		
		mSocketIn=new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		mSocketOut=new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream()),true);
		
		in=new Scanner(System.in);
		
		mChar='a';
		mNumeric=0;
		
		mRecievedData=mSocketIn.readLine();
		System.out.println(mRecievedData);
		
		mCommand=in.nextLine().toUpperCase();
		sendCommand(mCommand);
	
		mSocket.close();
		
	}

	public static void main(String[] args) {
		try {
			new Client();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void sendCommand(String command){
		makeTag(mChar, mNumeric);
		mSocketOut.println(mConst+mTag+" "+command);
		//System.out.println(mConst+mTag+" "+command);
	}
	
	private void makeTag(char c, int n){
		String numString=String.format("%03d",n);
		mTag=new String(""+mChar+numString);
		mNumeric++;
	}

}
