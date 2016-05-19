package Model;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.SwingUtilities;

import Viewer.EmailViewer;
import Viewer.LoginScreen;
import Viewer.MainFrame;

public class Client {

	private Socket mSocket;
	private BufferedReader mSocketIn;
	private PrintWriter mSocketOut;
	private String mTag;
	private final String mConst = "C: ";
	private String mCommand, mRecievedData, mRecievedMailbox;
	private char mChar;
	private int mNumeric;
	private Client mInstance;
	private String mUser, mPass;
	private LoginScreen mLoginView;
	private Mailbox mMailbox;
	private JList<Folder> mMailboxes;
	private JList<String> mWorkspace;
	private int mCurrentMsg = 0;
	private int mMsgCount = 0;
	boolean flag = true;

	public Client() throws Exception {

		mChar = 'a';
		mNumeric = 0;
		mInstance = this;
		mMailboxes = new JList<>();
		mMailboxes.setMinimumSize(new Dimension(250, 0));
		mWorkspace = new JList<>();

		mSocket = new Socket("localhost", 1992);

		mSocketIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		mSocketOut = new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream()), true);

		mRecievedData = mSocketIn.readLine();
		System.out.println(mRecievedData);

		mLoginView = new LoginScreen(mInstance);

		if (flag) {
			mCommand = "login" + " " + mUser + " " + mPass;
			sendCommand(mCommand);
			System.out.println(mSocketIn.readLine());
			initMailboxes();
		} else {
			mCommand = "logout";
			sendCommand(mCommand);
			System.out.println(mSocketIn.readLine());
			System.out.println(mSocketIn.readLine());
			mSocket.close();
			System.exit(1);
		}

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new MainFrame(mInstance, mUser, mPass);

			}
		});

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

	private void fillCommand(String command) {
		mSocketOut.println(mConst + command);
	}

	private void makeTag(char c, int n) {
		String numString = String.format("%03d", n);
		mTag = new String("" + mChar + numString);
		mNumeric++;

		if (mNumeric > 999) {
			mChar++;
			mNumeric = 0;
		}

	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setUser(String mUser) {
		this.mUser = mUser;
	}

	public void setPass(String mPass) {
		this.mPass = mPass;
	}

	public JList<Folder> getMailboxes() {
		return mMailboxes;
	}

	private void initMailboxes() {
		mMailbox = new Mailbox(mUser, mPass);
		Folder inbox = mMailbox.getInbox();
		Folder sent = mMailbox.getSent();
		Folder deleted = mMailbox.getTrash();

		int k = 0;
		try {
			inbox.open(Folder.READ_ONLY);
			k = inbox.getMessageCount();
		} catch (MessagingException e1) {
			e1.printStackTrace();
		}

		DefaultListModel<Folder> model = new DefaultListModel<>();
		model.addElement(inbox);
		model.addElement(sent);
		model.addElement(deleted);

		mMailboxes.setModel(model);
	}

	public void initMailboxMessages() {
		mWorkspace = new JList<>();
		Folder mailbox = null;

		switch (mRecievedMailbox) {
		case "INBOX":
			mailbox = mMailbox.getInbox();
			break;
		case "SENT":
			mailbox = mMailbox.getSent();
			break;
		case "DELETED":
			mailbox = mMailbox.getTrash();
			break;
		default:
			mailbox = mMailbox.getInbox();
		}

		try {
			mMsgCount = mailbox.getMessageCount();
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DefaultListModel<String> mailModel = new DefaultListModel<>();
		for (int i = 0; i < mMsgCount; i++) {
			Message msg = null;
			String temp = " ";
			try {

				msg = mailbox.getMessage(mMsgCount - i);
				Address[] in = msg.getFrom();
				temp = temp.concat(in[0].toString());
				temp = temp.concat("      ");
				temp = temp.concat(msg.getSubject());
				temp = temp.concat("      ");
				temp = temp.concat(msg.getSentDate().toString());

			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mailModel.addElement(temp);
		}
		mWorkspace.setModel(mailModel);
	}

	public boolean notifyToSend(String name, int msgNumber) throws IOException {

		mCommand = "fetch " + msgNumber + " " + name;
		sendCommand(mCommand);

		String temp = "";

		while (!(mRecievedData = mSocketIn.readLine()).contains("FETCH completed")) {
			if (!mRecievedData.contains("*")) {
				StringBuilder sb = new StringBuilder(mRecievedData);
				sb.delete(0, 2);
				mRecievedData = sb.toString();
				temp = temp.concat(mRecievedData + "\n");
			}
		}

		new EmailViewer(temp,this);
		return false;
	}

	public boolean notifyToSend(String name) {
		mRecievedMailbox = name;
		mCommand = "SELECT " + name;
		sendCommand(mCommand);
		try {
			System.out.println(mSocketIn.readLine());
			System.out.println(mSocketIn.readLine());
			System.out.println(mSocketIn.readLine());
			System.out.println(mSocketIn.readLine());
			System.out.println(mSocketIn.readLine());
			System.out.println(mSocketIn.readLine());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public JList<String> getWorkspace() {
		return mWorkspace;
	}

	public int getCurrentMsg() {
		return mCurrentMsg;
	}

	public void setCurrentMsg(int mCurrentMsg) {
		this.mCurrentMsg = mMsgCount - mCurrentMsg;
	}

	public void closeConnection() {
		mCommand = mConst + " * Closing connection";
		mSocketOut.println(mCommand);
		try {
			mSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void logout() {
		mCommand = "logout";
		sendCommand(mCommand);
		try {
			System.out.println(mSocketIn.readLine());
			System.out.println(mSocketIn.readLine());
			mSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(1);

	}

	public void closeMail() {
		mCommand="close";
		sendCommand(mCommand);
		try {
			System.out.println(mSocketIn.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
