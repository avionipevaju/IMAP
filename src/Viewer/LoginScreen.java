package Viewer;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class LoginScreen extends JDialog {

	private JLabel mUserLabel, mPassLabel;
	private JTextField mUserField;
	private JPasswordField mPassField;
	private JButton mLoginBtn, mExitBtn;

	public LoginScreen() {
		
		try {
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setTitle("IMAP Login");
		setResizable(false);
		setLayout(new GridLayout(3, 1));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		initComponents();

		pack();
		setLocationRelativeTo(null);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setVisible(true);

	}

	private void initComponents() {
		JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		mUserLabel = new JLabel("Username:");
		mUserField = new JTextField(25);
		top.add(mUserLabel);
		top.add(mUserField);
		add(top);

		JPanel middle = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		mPassLabel = new JLabel("Password: ");
		mPassField = new JPasswordField(25);
		middle.add(mPassLabel);
		middle.add(mPassField);
		add(middle);

		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		mExitBtn = new JButton("Exit");
		mExitBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(1);

			}
		});
		mLoginBtn = new JButton("Login");
		mLoginBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
	
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						new MainFrame(mUserField.getText(),String.valueOf(mPassField.getPassword()));
						
					}
				});
				
				dispose();

			}
		});
		bottom.add(mLoginBtn);
		bottom.add(mExitBtn);
		add(bottom);

	}

}
