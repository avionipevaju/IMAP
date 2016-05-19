package Viewer;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JToolBar;

import Model.Client;

public class FetchToolbar extends JToolBar {

	private JButton mFetchAll, mFetchBody, mFetchHeader;
	private AbstractAction mAll, mBody, mHeader;
	private Client mModel;

	public FetchToolbar(Client client) {
		mModel = client;
		initListeners();
		initComponents();

	}

	private void initListeners() {

		mAll = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					mModel.notifyToSend("full", mModel.getCurrentMsg());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		mBody = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					mModel.notifyToSend("body", mModel.getCurrentMsg());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		mHeader = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					mModel.notifyToSend("header", mModel.getCurrentMsg());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

	}

	private void initComponents() {
		setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 0));
		mFetchAll = new JButton("Fetch All");
		mFetchAll.addActionListener(mAll);
		mFetchBody = new JButton("Fetch Body");
		mFetchBody.addActionListener(mBody);
		mFetchHeader = new JButton("Fetch Header");
		mFetchHeader.addActionListener(mHeader);
		add(mFetchAll);
		add(mFetchBody);
		add(mFetchHeader);

	}

}
