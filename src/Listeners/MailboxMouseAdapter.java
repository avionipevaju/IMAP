package Listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.mail.Folder;

import Model.Client;

public class MailboxMouseAdapter extends MouseAdapter{
	
	private Client mModel;
	
	public MailboxMouseAdapter(Client model) {
		mModel=model;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount()==2){
		Object source=mModel.getMailboxes().getModel().getElementAt(mModel.getMailboxes().getSelectedIndex());
		Folder mailbox=(Folder)source;
		mModel.notifyToSend(mailbox.getName());
		System.out.println(mailbox.getName());
		}
		
	}

	
	
	
}
