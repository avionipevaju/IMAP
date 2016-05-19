package Listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.mail.Folder;

import Model.Client;

public class MailMouseAdapter extends MouseAdapter {
	
	private Client mModel;
	private int mMsgNumber;
	
	public MailMouseAdapter(Client client) {
		mModel=client;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
			mMsgNumber=mModel.getWorkspace().getSelectedIndex();
			mModel.setCurrentMsg(mMsgNumber);
			System.out.println(mModel.getCurrentMsg());
	}
	
}
