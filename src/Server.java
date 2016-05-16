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
	private Mailbox mCurrentMailbox = null;
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
		initMailboxes();
		
		mServerSocket = new ServerSocket(2015); //143
		mSocket = mServerSocket.accept();
		
		mInput = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		mOutput = new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream()),true);
		
		mCurrentState = State.Not_Authenticated;
		
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
	
	private void initMailboxes() {
		mMailboxes = new ArrayList<>();
		
		Mailbox inbox = new Mailbox("Inbox");
		Mailbox sent = new Mailbox("Sent");
		Mailbox drafts = new Mailbox("Drafts");
		Mailbox trash = new Mailbox("Trash");
		
		mMailboxes.add(inbox);
		mMailboxes.add(sent);
		mMailboxes.add(drafts);
		mMailboxes.add(trash);
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
				mMessage = mConst.concat(mTag.concat(" BAD AUTHENTICATE unknown command"));
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
				mMessage = mConst.concat(mTag.concat(" BAD LOGIN unknown command"));
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
				mMessage = mConst.concat(mTag.concat("BAD SELECT unknown command"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
				return ;
			}
			if (!checkMailbox(mData)){
				mMessage = mConst.concat(mTag.concat(" NO SELECT no such mailbox or can't access"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
				return ;
			}
			
			int counter = mCurrentMailbox.exists();
			mMessage = mConst.concat("* " + counter + " EXISTS");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			counter = mCurrentMailbox.recent();
			mMessage = mConst.concat("* " + counter + " RECENT");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			counter = mCurrentMailbox.unseen();
			mMessage = mConst.concat("* OK [UNSEEN " + counter + "] Message " + counter + " is first unseen");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			mMessage = mConst.concat("* FLAGS " + mCurrentMailbox.toStringDefinedFlags());
			System.out.println(mMessage);
			mOutput.println(mMessage);

			mMessage = mConst.concat("* OK [PERMANENTFLAGS " + mCurrentMailbox.toStringPermanentFlags() + "]");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			mMessage = mConst.concat(mTag.concat(" OK [READ-WRITE] SELECT completed"));
			mCurrentState = State.Selected;
			System.out.println(mMessage);
			mOutput.println(mMessage);

			break;
			
		case "EXAMINE" :
			if (mCurrentState != State.Authenticated) {
				mMessage = mConst.concat(mTag.concat("BAD EXAMINE unknown command"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
				return ;
			}
			if (!checkMailbox(mData)){
				mMessage = mConst.concat(mTag.concat(" NO EXAMINE no such mailbox or can't access"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
				return ;
			}
			
			int examineCounter = mCurrentMailbox.exists();
			mMessage = mConst.concat("* " + examineCounter + " EXISTS");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			examineCounter = mCurrentMailbox.recent();
			mMessage = mConst.concat("* " + examineCounter + " RECENT");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			examineCounter = mCurrentMailbox.unseen();
			mMessage = mConst.concat("* OK [UNSEEN " + examineCounter + "] Message " + examineCounter + " is first unseen");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			mMessage = mConst.concat("* FLAGS " + mCurrentMailbox.toStringDefinedFlags());
			System.out.println(mMessage);
			mOutput.println(mMessage);

			mMessage = mConst.concat("* OK [PERMANENTFLAGS ()] No permanent flags permitted");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			mMessage = mConst.concat(mTag.concat(" OK [READ-ONLY] EXAMINE completed"));
			mCurrentState = State.Selected;
			System.out.println(mMessage);
			mOutput.println(mMessage);

			break;
			
		case "CREATE" :
			if (mCurrentState != State.Authenticated) {
				mMessage = mConst.concat(mTag.concat("BAD CREATE unknown command"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
				return ;
			}
			if (checkMailbox(mData)){
				mMessage = mConst.concat(mTag.concat(" NO CREATE can't create mailbox with that name"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
				return ;
			}
			
			String name = mData.trim();
			Mailbox newMailbox = new Mailbox(name);
			mMailboxes.add(newMailbox);
			mMessage = mConst.concat(mTag.concat("OK CREATE completed"));
			System.out.println(mMessage);
			mOutput.println(mMessage);

			break;
			
		case "DELETE" :
			if (mCurrentState != State.Authenticated) {
				mMessage = mConst.concat(mTag.concat("BAD DELETE unknown command"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
				return ;
			}
			if (!checkMailbox(mData)){
				mMessage = mConst.concat(mTag.concat(" NO DELETE can't delete mailbox with that name"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
				return ;
			}

			mMailboxes.remove(mCurrentMailbox);
			mCurrentMailbox = null;
			
			mMessage = mConst.concat(mTag.concat("OK DELETE completed"));
			System.out.println(mMessage);
			mOutput.println(mMessage);

			break;
			
		case "RENAME" :
			if (mCurrentState != State.Authenticated) {
				mMessage = mConst.concat(mTag.concat("BAD RENAME unknown command"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
				return ;
			}
			String newName = getNewName();
			if (!checkMailbox(mData)){
				mMessage = mConst.concat(mTag.concat(" NO RENAME rename failure"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
				return ;
			}
			
			mCurrentMailbox.setName(newName.toLowerCase());
			mMessage = mConst.concat(mTag.concat("OK RENAME completed"));
			System.out.println(mMessage);
			mOutput.println(mMessage);

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
		//System.out.println(username);
		//System.out.println(password);
		
		if (mUsername.equalsIgnoreCase(username) && mPassword.equalsIgnoreCase(password))
			return true;

		return false;
	}
	
	private boolean checkMailbox(String mData) {
		for (Mailbox m : mMailboxes) {
			if (mData.trim().equalsIgnoreCase(m.getName()))
				mCurrentMailbox = m;
				return true;
		}
		return false;
	}
	
	private String getNewName() {
		Scanner temp = new Scanner(mData);
		
		String toRename = temp.next();
		String result = temp.next();
		System.out.println(toRename);
		System.out.println(result);
		
		mData = toRename;
		return result;
	}


}
