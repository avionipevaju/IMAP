import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

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
	private final String mUsername = "Pavle";
	private final String mPassword = "Nikola";
	private State mCurrentState;
	
	public Server() throws Exception {
		mMailboxes = new ArrayList<>();
		mServerSocket = new ServerSocket(2015); //143
		mSocket = mServerSocket.accept();
		
		mInput = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		mOutput = new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream()),true);
		
		mCurrentState = State.Not_Authenticated;
		
		//Not Authenticated State
		//AUTHENTICATE, LOGIN, STARTTLS valid commands
		
		mMessage = mConst.concat(mGreeting);
		System.out.println(mMessage);
		
		mOutput.println(mMessage);
		mMessage = mInput.readLine();
		System.out.println(mMessage);
		
		parseCommand();
		
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
	
	public void doCommand() throws Exception {
		
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
			//TODO na kraju
			break;

		case "AUTHENTICATE" :
			if (mCurrentState != State.Not_Authenticated) {
				mMessage = mConst.concat(mTag.concat(" BAD unknown command"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
				return ;
			}
			if (mData.contains("PLAIN")) {
				mMessage = mConst.concat("+");
				System.out.println(mMessage);
				mOutput.println(mMessage);
				
				mMessage = mInput.readLine();
				mData = mMessage.substring(3, mMessage.length());
				if (checkLogIn(mData)) {
					mMessage = mConst.concat(mTag.concat(" OK PLAIN authentication successful"));
					mCurrentState = State.Authenticated;
					System.out.println(mMessage);
					mOutput.println(mMessage);
				}
				else {
					mMessage = mConst.concat(mTag.concat(" NO PLAIN authentication rejected"));
					System.out.println(mMessage);
					mOutput.println(mMessage);
				}

			}
			else {
				mMessage = mConst.concat(mTag.concat(" NO " + mData + " unsupported authentication mechanism"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
			}
			break;
			
		case "LOGIN" :
			if (mCurrentState != State.Not_Authenticated) {
				mMessage = mConst.concat(mTag.concat(" BAD unknown command"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
				return ;
			}
			if (checkLogIn(mData)) {
				mMessage = mConst.concat(mTag.concat(" OK LOGIN completed"));
				mCurrentState = State.Authenticated;
				System.out.println(mMessage);
				mOutput.println(mMessage);
			}
			else {
				mMessage = mConst.concat(mTag.concat(" NO LOGIN username or password rejected"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
			}

			break;

		case "SELECT" :
			if (mCurrentState != State.Authenticated) {
				mMessage = mConst.concat(mTag.concat("BAD unknown command"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
				return ;
			}
			

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
	
	private boolean checkLogIn(String data) {
		String username, password;
		
		Scanner temp = new Scanner(data);
		username = temp.next();
		password = temp.next();
		System.out.println(username);
		System.out.println(password);
		
		if (mUsername.equalsIgnoreCase(username) && mPassword.equalsIgnoreCase(password))
			return true;

		return false;
	}


}
