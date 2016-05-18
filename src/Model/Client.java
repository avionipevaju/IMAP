package Model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.midi.Synthesizer;
import javax.swing.SwingUtilities;

import Viewer.LoginScreen;
import Viewer.MainFrame;

public class Client {
	
	private Socket mSocket;
	private BufferedReader mSocketIn;
	private PrintWriter mSocketOut;
	private String mTag;
	private final String mConst = "C: ";
	private String mCommand, mRecievedData, mStringResponse, mTagResponse;
	private char mChar;
	private int mNumeric;
	private Client mInstance;
	private String mUser,mPass;
	private LoginScreen mLoginView;
	boolean flag=true;

	public Client() throws Exception {
		
		mChar = 'a';
		mNumeric = 0;
		mInstance=this;
		
		mSocket = new Socket("localhost", 1992);

		mSocketIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		mSocketOut = new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream()), true);
		
		mRecievedData = mSocketIn.readLine();
		System.out.println(mRecievedData);
		
		mLoginView=new LoginScreen(mInstance);
		System.out.println("PRSAO GUI");
		
		if(flag){
			mCommand="login"+" "+mUser+" "+mPass;
			sendCommand(mCommand);
			System.out.println(mSocketIn.readLine());
		}else{
			mCommand="logout";
			sendCommand(mCommand);
			System.out.println(mSocketIn.readLine());
			System.out.println(mSocketIn.readLine());
			mSocket.close();
			System.exit(1);
		}
		
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new MainFrame(mUser,mPass);
				
			}
		});	
		
//		
//		
//		while(true){
//			//ocekujem response, parsiram, i reagujem
//			
//			break;
//		}
//		mSocket.close();
		

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
	
	
	
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setUser(String mUser) {
		this.mUser = mUser;
	}

	public void setPass(String mPass) {
		this.mPass = mPass;
	}

}
