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
	private String mMessage, mCommand, mTag, mData;
	private int mNOOPCount = 1;
	
	public Server() throws Exception {
		mMailboxes = new ArrayList<>();
		mServerSocket = new ServerSocket(2015); //143
		mSocket = mServerSocket.accept();
		
		mInput = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		mOutput = new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream()),true);
		
		//Not Authenticated State
		//AUTHENTICATE, LOGIN, STARTTLS valid commands
		
		mMessage = mConst.concat(mGreeting);
		System.out.println(mMessage);
		
		mOutput.println(mMessage);
		mMessage = mInput.readLine();
		System.out.println(mMessage);
		
		parseCommand();
		System.out.println("Command = " + mCommand);
		
		while (!mCommand.equalsIgnoreCase("LOGOUT")) {
			doCommand();
			
			mMessage = mInput.readLine();
			System.out.println(mMessage);
			parseCommand();
		}
		
		mMessage = mConst.concat("* BYE IMAP server terminating connection");
		System.out.println(mMessage);
		mOutput.println(mMessage);
		
		mMessage = mConst.concat(mTag.concat(" OK LOGOUT completed"));
		System.out.println(mMessage);
		mOutput.println(mMessage);
		
		mSocket.close();
		
	}
	
	public void parseCommand() {
		int i = 8, startCommand = 8;
		mTag = mMessage.substring(3, 7);
		while (i < mMessage.length() && mMessage.charAt(i) != ' ') {
			i++;
		}
		mCommand = mMessage.substring(startCommand, i);
		if (i != mMessage.length())
			mData = mMessage.substring(i);
		else {
			mData = "";
		}
		
	}
		
	
	public static void main(String[] args) {
		try {
			new Server();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doCommand() {
		System.out.println("Tu sam DOCOMMAND");
		
		switch(mCommand) {
		
		case "CAPABILITY" :
			
			mMessage = mConst.concat("* CAPABILITY IMAP AUTH=PLAIN");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			mMessage = mConst.concat(mTag.concat(" OK CAPABILITY completed"));
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			break;
			
		case "NOOP" :
			if (mNOOPCount == 3) {
				//TODO kad se implementira mailbox srediti
				
				mNOOPCount = 1;
			}else {
				mMessage = mConst.concat(mTag.concat(" OK NOOP completed"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
				mNOOPCount++;
			}
			
			break;
		
		case "LOGOUT" :
			
			mMessage = mConst.concat("* BYE IMAP Server logging out");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			mMessage = mConst.concat(mTag.concat(" OK LOGOUT completed"));
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			break;

		case "STARTTLS" :
			break;

		case "AUTHENTICATE" :
			break;
			
		case "LOGIN" :
			break;

		case "SELECT" :
			break;
			
		case "EXAMINE" :
			break;
			
		case "CREATE" :
			break;
			
		case "DELETE" :
			break;
			
		case "RENAME" :
			break;
			
		case "SUBSCRIBE" :
			break;
			
		case "UNSUBSCRIBE" :
			break;
			
		case "LIST" :
			break;
			
		case "LSUB" :
			break;
			
		case "STATUS" :
			break;

		case "APPEND" :
			break;
			
		case "CHECK" :
			break;
			
		case "CLOSE" :
			break;
			
		case "EXPUNGE" :
			break;
			
		case "SEARCH" :
			break;

		case "FETCH" :
			break;

		case "STORE" :
			break;

		case "COPY" :
			break;

		case "UID" :
			break;

		default :
			break;
		
		}
		
	}


}
