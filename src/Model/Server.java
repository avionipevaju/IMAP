package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

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
	private Folder mCurrentFolder = null;
	private State mCurrentState;

	
	public Server() throws Exception {
		mServerSocket = new ServerSocket(1992); //143
		mSocket = mServerSocket.accept();
		
		mInput = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		mOutput = new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream()),true);
		
		//Not Authenticated State
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
		if (mData.contains(mUser.toUpperCase()) && mData.contains(mPass.toUpperCase()))
			return true;
		return false;
	}
	
	public void doCommand() throws Exception {
		
		switch (mCommand) {
		case "LOGIN":
			if (checkLogIn()) {
				mMessage = mConst.concat(mTag.concat(" OK LOGIN completed"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
				//Sad ulazi u Authenticated State
				mCurrentState = State.Authenticated;
				mMailBox = new Mailbox(mUser, mPass);
			}
			else {
				mMessage = mConst.concat(mTag.concat(" NO LOGIN username or password rejected"));
				System.out.println(mMessage);
				mOutput.println(mMessage);
			}
			
			break;
		
		case "SELECT":
			if (mCurrentFolder != null){
				mCurrentFolder.close(true);
			}
			mCurrentFolder = mMailBox.getFolder(mData.trim());
			mCurrentFolder.open(Folder.READ_ONLY);
			int counter = mCurrentFolder.getMessageCount();

			mMessage = mConst.concat("* " + counter + " EXISTS");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			counter = mMailBox.recentCount(mCurrentFolder);
			mMessage = mConst.concat("* " + counter + " RECENT");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			counter = mMailBox.unseenCount(mCurrentFolder);
			mMessage = mConst.concat("* OK [UNSEEN " + counter + "] Message " + counter + " is first unseen");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			mMessage = mConst.concat("* FLAGS " + mMailBox.definedFlags(mCurrentFolder));
			System.out.println(mMessage);
			mOutput.println(mMessage);

			mMessage = mConst.concat("* OK [PERMANENTFLAGS " + mCurrentFolder.getPermanentFlags().toString() + "]");
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			mMessage = mConst.concat(mTag.concat(" OK [READ-WRITE] SELECT completed"));
			System.out.println(mMessage);
			mOutput.println(mMessage);
			//Sad ulazi u Selected State
			mCurrentState = State.Selected;
			
			break;
			
		case "FETCH" :
			mData = mData.trim();
			int numOfMessage = Character.getNumericValue(mData.charAt(0));
			Message msg = mCurrentFolder.getMessage(numOfMessage);
			
			if (mData.contains("FULL")) {
				//celu poruku i header i body
				mMessage = mConst.concat("* " + numOfMessage + " FETCH FULL");
				System.out.println(mMessage);
				mOutput.println(mMessage);
				//Header
				sendHeader(msg);
				//Body
				sendBody(msg);
				
			}else
				if (mData.contains("BODY")) {
					//samo body, sadrzaj
					mMessage = mConst.concat("* " + numOfMessage + " FETCH BODY");
					System.out.println(mMessage);
					mOutput.println(mMessage);
					
					//Body
					sendBody(msg);
				}else
					if (mData.contains("HEADER")) {
						mMessage = mConst.concat("* " + numOfMessage + " FETCH HEADER");
						System.out.println(mMessage);
						mOutput.println(mMessage);
						
						//Header
						sendHeader(msg);
					}
			
			mMessage = mConst.concat(mTag.concat(" OK FETCH completed"));
			System.out.println(mMessage);
			mOutput.println(mMessage);	
			
			break;

		default:
			mMessage = mConst.concat(mTag.concat("BAD unknown command"));
			System.out.println(mMessage);
			mOutput.println(mMessage);
			
			break;
		}
	}
	
	private void sendHeader(Message msg) throws Exception {
		
		//Date
		if (msg.getSentDate() == null) {
			mMessage = mConst.concat("Date: null");
			System.out.println(mMessage);
			mOutput.println(mMessage);
		}
		else {
			mMessage = mConst.concat("Date: " + msg.getSentDate().toString());
			System.out.println(mMessage);
			mOutput.println(mMessage);
		}
		
		//From
		if (msg.getFrom() == null) {
			mMessage = mConst.concat("From: null");
			System.out.println(mMessage);
			mOutput.println(mMessage);
		}
		else {
			mMessage = mConst.concat("From: " + msg.getFrom().toString());
			System.out.println(mMessage);
			mOutput.println(mMessage);
		}
		
		//Subject
		if (msg.getSubject() == null) {
			mMessage = mConst.concat("Subject: null");
			System.out.println(mMessage);
			mOutput.println(mMessage);
		}
		else {
			mMessage = mConst.concat("Subject: " + msg.getSubject());
			System.out.println(mMessage);
			mOutput.println(mMessage);
		}
		
		//To
		if (msg.getRecipients(RecipientType.TO) == null) {
			mMessage = mConst.concat("To: null");
			System.out.println(mMessage);
			mOutput.println(mMessage);
		}
		else {
			mMessage = mConst.concat("To: " + msg.getRecipients(RecipientType.TO).toString());
			System.out.println(mMessage);
			mOutput.println(mMessage);
		}
		
		//Cc
		if (msg.getRecipients(RecipientType.CC) == null) {
			mMessage = mConst.concat("Cc: null");
			System.out.println(mMessage);
			mOutput.println(mMessage);
		}
		else {
			mMessage = mConst.concat("Cc: " + msg.getRecipients(RecipientType.CC).toString());
			System.out.println(mMessage);
			mOutput.println(mMessage);
		}
		
		//Content type
		if (msg.getContentType() == null) {
			mMessage = mConst.concat("Content-Type: null");
			System.out.println(mMessage);
			mOutput.println(mMessage);
		}
		else {
			mMessage = mConst.concat("Content-Type: " + msg.getContentType().toString());
			System.out.println(mMessage);
			mOutput.println(mMessage);
		}

		
	}
	
	private void sendBody(Part msg) throws Exception, IOException {
	    if (msg.isMimeType("text/plain")) {
	        mMessage = mConst.concat("Body: " + (String)msg.getContent());
			System.out.println(mMessage);
			mOutput.println(mMessage);
	      } 
	      else if (msg.isMimeType("multipart/*")) {
	        Multipart mp = (Multipart) msg.getContent();
	        int count = mp.getCount();
	        for (int i = 0; i < count; i++)
	            sendBody(mp.getBodyPart(i));
	      }  

	}



}
