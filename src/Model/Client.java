package Model;

import javax.swing.SwingUtilities;

import Viewer.LoginScreen;

public class Client {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new LoginScreen();
				
			}
		});

	}

}
