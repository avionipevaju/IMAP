package Viewer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JTextArea;

import Model.Client;

public class EmailViewer extends JDialog {
	
	private String mData;
	private JTextArea mArea;
	private Client mModel;
	
	public EmailViewer(String data,Client client) {
		mModel=client;
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
		
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				mModel.closeMail();
			}

			
			
		});
		
	}
	
	

}
