package Model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.mail.Folder;
import javax.mail.MessagingException;

public class Server {
	
	private ServerSocket mServerSocket;
	private Socket mSocket;
	private BufferedReader mInput;
	private PrintWriter mOutput;
	private final String mGreeting = "*OK IMAP Service Ready";
	private final String mConst = "S: ";
	private String mMessage, mCommand, mTag, mData;
	private final String mUser = "rafdummy@outlook.com";
	private final String mPass = "dummymail12";
	private Mailbox mMailBox;

	
	public Server() throws Exception {
		mServerSocket = new ServerSocket(1992); //143
		mSocket = mServerSocket.accept();
		
		mInput = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		mOutput = new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream()),true);
		
		//Not Authenticated State
		
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
	
	public static void main(String[] args) {
		try {
			new Server();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void parseCommand() {
		int i = 8, startCommand = 8;
		mTag = mMessage.substring(3, 7);
		while (i < mMessage.length() && mMessage.charAt(i) != ' ') {
			i++;
		}
		mCommand = mMessage.substring(startCommand, i);
		if (i != mMessage.length())
			mData = mMessage.substring(i+1);
		else {
			mData = "";
		}
		System.out.println("Tag: " + mTag);
		System.out.println("Command: " + mCommand);
		System.out.println("Data: " + mData);
		
	}
	
	private boolean checkLogIn() {
		if (mData.contains(mUser) && mData.contains(mPass))
			return true;
		return false;
	}
	
	public void doCommand() {
		
		switch (mCommand) {
		case "LOGIN":
			if (checkLogIn()) {
				mMessage = mConst.concat(mTag.concat(" OK LOGIN completed"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
				//Sad ulazi u Authenticated State
				mMailBox = new Mailbox(mUser, mPass);
			}
			else {
				mMessage = mConst.concat(mTag.concat(" NO LOGIN username or password rejected"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
			}
			
			break;
		
		case "SELECT":
			Folder folder = mMailBox.getFolder(mData.trim());
			int counter = 0;
					
			try {
				counter = folder.getMessageCount();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			mMessage = mConst.concat("* " + counter + " EXISTS");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			counter = mMailBox.recentCount(folder);
			mMessage = mConst.concat("* " + counter + " RECENT");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			counter = mMailBox.unseenCount(folder);
			mMessage = mConst.concat("* OK [UNSEEN " + counter + "] Message " + counter + " is first unseen");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			mMessage = mConst.concat("* FLAGS " + mMailBox.definedFlags(folder));
			System.out.println(mMessage);
			mOutput.println(mMessage);

			mMessage = mConst.concat("* OK [PERMANENTFLAGS " + folder.getPermanentFlags().toString() + "]");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			mMessage = mConst.concat(mTag.concat(" OK [READ-WRITE] SELECT completed"));
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			break;
			
		case "FETCH" :
			// Dogovoriti se oko Fetch-a, kako cemo sve fetch-ovati 
			break;

		default:
			mMessage = mConst.concat(mTag.concat("BAD unknown command"));
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			break;
		}
	}



}
