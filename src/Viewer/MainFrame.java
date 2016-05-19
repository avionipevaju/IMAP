package Viewer;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import Listeners.MailboxMouseAdapter;
import Model.Client;


public class MainFrame extends JFrame {

	private JSplitPane mSplitPane;
	private JToolBar mToolbar;
	private JMenuBar mMenuBar;
	private JPanel mToolPanel;
	private JList<String> mWorkspace;
	private Client mModel;
	private MailboxMouseAdapter mAdapter;


	public MainFrame(Client client,String user, String pass) {
		mModel=client;
		setSize(1200, 640);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(new BorderLayout());
		setTitle("IMAP Client Server Demonstration");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		initComponent();
		//initLookAndFeel();
		
		
		
	}


	private void initComponent() {
		
		mToolPanel = new JPanel();
		mToolPanel.setLayout(new BorderLayout());
		add(mToolPanel, BorderLayout.NORTH);

		mSplitPane = new JSplitPane();
		mSplitPane.setDividerLocation(250);
		mWorkspace=new JList<>();
		mSplitPane.setRightComponent(mWorkspace);
		
		mAdapter=new MailboxMouseAdapter(mModel);
		mModel.getMailboxes().addMouseListener(mAdapter);
		mSplitPane.setLeftComponent(mModel.getMailboxes());
		
		
		
		//prikazivanje mejlova
//		DefaultListModel<String> mailModel=new DefaultListModel<>();
//		for(int i=0;i<k;i++){
//		Message msg=null;
//		String temp=" ";
//		try {
//			
//			msg = inbox.getMessage(inbox.getMessageCount()-i);
//			Address[] in = msg.getFrom();      
//	        temp=temp.concat(in[0].toString());
//			temp=temp.concat("      ");
//			temp=temp.concat(msg.getSubject());
//			temp=temp.concat("      ");
//			temp=temp.concat(msg.getSentDate().toString());
//			 
//		} catch (MessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		mailModel.addElement(temp);
//		}
//		mWorkspace.setModel(mailModel);
		
		add(mSplitPane, BorderLayout.CENTER);

		mMenuBar = new JMenuBar();
		JMenu menu= new JMenu("Profile");
		menu.add(new JMenuItem("Log Out"));
		mMenuBar.add(menu);
		mToolPanel.add(mMenuBar, BorderLayout.NORTH);

		mToolbar = new JToolBar();
		mToolPanel.add(mToolbar, BorderLayout.CENTER);

	}

	private void initLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//UIManager.setLookAndFeel ( "com.alee.laf.WebLookAndFeel" );
			//WebLookAndFeel.install();
			//UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
			//UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");
			//UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	

}
