package Viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;

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
		
		mSplitPane.setRightComponent(mModel.getWorkspace());
		
		mAdapter=new MailboxMouseAdapter(mModel,this);
		mModel.getMailboxes().addMouseListener(mAdapter);
		mSplitPane.setLeftComponent(mModel.getMailboxes());
		
		add(mSplitPane, BorderLayout.CENTER);

		mMenuBar = new JMenuBar();
		JMenu menu= new JMenu("Profile");
		menu.add(new JMenuItem("Log Out"));
		mMenuBar.add(menu);
		mToolPanel.add(mMenuBar, BorderLayout.NORTH);

		mToolbar = new JToolBar();
		mToolPanel.add(mToolbar, BorderLayout.CENTER);

	}


	public JSplitPane getSplitPane() {
		return mSplitPane;
	}

	
	

}
