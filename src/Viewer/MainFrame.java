package Viewer;

import java.awt.BorderLayout;

import javax.mail.*;
import javax.swing.DefaultListModel;
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

import Model.Mailbox;


public class MainFrame extends JFrame {

	private JSplitPane mSplitPane;
	private JToolBar mToolbar;
	private JMenuBar mMenuBar;
	private JPanel mToolPanel;
	private JList<String> mWorkspace;
	private JList<Folder> mList;
	private String mUser,mPass;


	public MainFrame(String user, String pass) {
		setSize(1200, 640);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(new BorderLayout());
		setTitle("IMAP Client Server Demonstration");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		mUser=user;
		mPass=pass;
		initComponent();
		//initLookAndFeel();
		
		
		
	}


	private void initComponent() {
		
		mToolPanel = new JPanel();
		mToolPanel.setLayout(new BorderLayout());
		add(mToolPanel, BorderLayout.NORTH);

		mSplitPane = new JSplitPane();
		mList=new JList<>();
		mList.setName("FOLDERS");
		mSplitPane.setDividerLocation(250);
		mWorkspace=new JList<>();
		mSplitPane.setRightComponent(mWorkspace);
		
		Mailbox mail=new Mailbox(mUser, mPass);
		Folder inbox=mail.getInbox();
		Folder sent=mail.getSent();
		Folder deleted=mail.getTrash();
	
		DefaultListModel<String> mailModel=new DefaultListModel<>();
		int k = 0;
		try {
			inbox.open(Folder.READ_ONLY);
			k = inbox.getMessageCount();
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(int i=0;i<k;i++){
		Message msg=null;
		String temp=" ";
		try {
			
			msg = inbox.getMessage(inbox.getMessageCount()-i);
			Address[] in = msg.getFrom();      
	        temp=temp.concat(in[0].toString());
			temp=temp.concat("      ");
			temp=temp.concat(msg.getSubject());
			temp=temp.concat("      ");
			temp=temp.concat(msg.getSentDate().toString());
			 
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mailModel.addElement(temp);
		}
		mWorkspace.setModel(mailModel);
		
		
		
		DefaultListModel<Folder> model=new DefaultListModel<>();
		model.addElement(inbox);
		model.addElement(sent);
		model.addElement(deleted);
		
		mList.setModel(model);
		mSplitPane.setLeftComponent(mList);
		
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
