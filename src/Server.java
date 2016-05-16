import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	private ArrayList<Mailbox> mMailboxes;
	private ServerSocket mServerSocket;
	private Socket mSocket;
	private BufferedReader mInput;
	private PrintWriter mOutput;
	private final String mGreeting = "*OK IMAP Service Ready";
	private final String mConst = "S: ";
	private String mCommand, mMessage;
	
	public Server() throws Exception {
		mMailboxes = new ArrayList<>();
		mServerSocket = new ServerSocket(2015); //143
		mSocket = mServerSocket.accept();
		System.out.println("Address" + mSocket.getInetAddress());
		mInput = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		mOutput = new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream()),true);
		
		//Not Authenticated State
		//AUTHENTICATE, LOGIN, STARTTLS valid commands
		
		mMessage = mConst.concat(mGreeting);
		System.out.println(mMessage);
		
		mOutput.println(mMessage);
		mCommand = mInput.readLine();
		
		System.out.println(mCommand);
		
		
		
		
		
		mSocket.close();
		
	}
	
	
	public static void main(String[] args) {
		try {
			new Server();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
