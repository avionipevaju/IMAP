import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	
	private Socket mSocket;
	private BufferedReader mSocketIn;
	private PrintWriter mSocketOut;
	private String mTag;
	private char mChar;
	private int mNumeric;
	private final String mConst="C: ";
	

	public Client() throws Exception {
		mSocket =new Socket("localhost", 2015);
		
		mSocketIn=new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		mSocketOut=new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream()),true);
		
		mChar='a';
		mNumeric=0;
		
		System.out.println(mSocketIn.readLine());
		sendCommand("CAPABILITY");
	
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
