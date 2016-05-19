package Viewer;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JTextArea;

public class EmailViewer extends JDialog {
	
	private String mData;
	private JTextArea mArea;
	
	public EmailViewer(String data) {
		mData=data;
		setTitle("Email");
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initComponents();

		pack();
		setLocationRelativeTo(null);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setVisible(true);
		
	}

	private void initComponents() {
		mArea=new JTextArea();
		mArea.setText(mData);
		add(mArea);
	}
	
	

}
