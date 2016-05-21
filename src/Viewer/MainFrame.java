package Viewer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import Listeners.MailboxMouseAdapter;
import Model.Client;

public class MainFrame extends JFrame {

	private JSplitPane mSplitPane;
	private FetchToolbar mToolbar;
	private JMenuBar mMenuBar;
	private JPanel mToolPanel;
	private Client mModel;
	private MailboxMouseAdapter mAdapter;
	private AbstractAction mLogout;

	public MainFrame(Client client, String user, String pass) {
		mModel = client;
		setSize(1200, 640);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(new BorderLayout());
		setTitle("IMAP Client Server Demonstration");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		initListeners();
		initComponent();

	}

	private void initListeners() {

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				mModel.closeConnection();
			}

		});

		mLogout = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mModel.logout();

			}
		};

	}

	private void initComponent() {
		setIconImage(new ImageIcon(this.getClass().getResource("/res/mail.png")).getImage());
		
		mToolPanel = new JPanel();
		mToolPanel.setLayout(new BorderLayout());
		add(mToolPanel, BorderLayout.NORTH);

		mSplitPane = new JSplitPane();
		mSplitPane.setDividerLocation(250);

		mSplitPane.setRightComponent(mModel.getWorkspace());

		mAdapter = new MailboxMouseAdapter(mModel, this);
		mModel.getMailboxes().addMouseListener(mAdapter);
		mSplitPane.setLeftComponent(mModel.getMailboxes());

		add(mSplitPane, BorderLayout.CENTER);

		mMenuBar = new JMenuBar();
		JMenu menu = new JMenu("Profile");
		JMenuItem menuItem = new JMenuItem("Log Out");
		menuItem.addActionListener(mLogout);
		menu.add(menuItem);
		mMenuBar.add(menu);
		mToolPanel.add(mMenuBar, BorderLayout.NORTH);
		mToolbar = new FetchToolbar(mModel);
		mToolPanel.add(mToolbar, BorderLayout.CENTER);

	}

	public JSplitPane getSplitPane() {
		return mSplitPane;
	}

}
