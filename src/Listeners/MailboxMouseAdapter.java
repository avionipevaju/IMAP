package Listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.mail.Folder;

import Model.Client;
import Viewer.MainFrame;

public class MailboxMouseAdapter extends MouseAdapter {

	private Client mModel;
	private MainFrame mMainFrame;

	public MailboxMouseAdapter(Client model,MainFrame view) {
		mModel = model;
		mMainFrame=view;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			Object source = mModel.getMailboxes().getModel().getElementAt(mModel.getMailboxes().getSelectedIndex());
			Folder mailbox = (Folder) source;
			if (mModel.notifyToSend(mailbox.getName())) {
				mModel.initMailboxMessages();
				mMainFrame.getSplitPane().setRightComponent(mModel.getWorkspace());
			}

		}

	}

}
