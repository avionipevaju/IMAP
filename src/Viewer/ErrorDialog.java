package Viewer;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ErrorDialog extends JDialog{

	private static ErrorDialog mInstance;
	
	public static ErrorDialog getInstance(){
		if(mInstance==null){
			mInstance=new ErrorDialog();
		}
		return mInstance;
	}
	
	private ErrorDialog() {
		
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
	}
	
	public void showDialog(){
		
		JOptionPane optionPane = new JOptionPane(new JLabel("Failed to login, try again.",JLabel.CENTER));
		JDialog dialog = optionPane.createDialog("Login Failed");
		dialog.setAlwaysOnTop(true);
		dialog.setVisible(true);
		
	}
	
}
