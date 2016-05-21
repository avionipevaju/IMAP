package Viewer;

import java.awt.Color;
import java.awt.Component;

import javax.mail.Folder;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

public class MailListCellRenderer extends DefaultListCellRenderer{

	@Override
	public Component getListCellRendererComponent(JList<?> arg0, Object arg1, int arg2, boolean arg3, boolean arg4) {
		super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4);
		setBackground(new Color(152,173,203));
		return this;
	}

	
	
	
}
