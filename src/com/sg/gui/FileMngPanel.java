package com.sg.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;
import com.sg.model.MetaData;

public class FileMngPanel extends JPanel {
	// Attribute
	private int width;
	private int height;
	
	/* 	status
	0 : Default		1 : Information		2 : Create	
	3 : Upload		4 : Download		5 : Delete 
	*/
	
	private int status;
	private File file;
	private String filePath;

	// Components
	private Font inputFont;
	private JLabel bgImg[];
	private JLabel label[];
	private JTextField textField[];
	private JButton btn[];
	private ActionHandler handler;

	public FileMngPanel(int w, int h) {

		super();
		this.width = w;
		this.height = h;
		this.status = 1;
		this.file = null;
		this.filePath = null;
		this.setLayout(null);
		this.setBackground(Constants.backColor);
		this.setBorder(new LineBorder(Color.LIGHT_GRAY));

		// 디렉토리를 관리하기 위한 배경 이미지 (Create, Access, Delete,  ...
		bgImg = new JLabel[6];
		bgImg[0] = new JLabel();
		bgImg[1] = new JLabel(new ImageIcon(Constants.BackgroudPath.fileMngBG1.getPath()));
		bgImg[2] = new JLabel(new ImageIcon(Constants.BackgroudPath.fileMngBG2.getPath()));
		bgImg[3] = new JLabel(new ImageIcon(Constants.BackgroudPath.fileMngBG3.getPath()));
		bgImg[4] = new JLabel(new ImageIcon(Constants.BackgroudPath.fileMngBG4.getPath()));
		bgImg[5] = new JLabel(new ImageIcon(Constants.BackgroudPath.fileMngBG5.getPath()));
		
		for(int i=0;i<6;i++) {
			bgImg[i].setBounds(1,1,width-2,height-2);
		}

		// font 설정
		inputFont = Constants.Font1;

		label = new JLabel[5];
		textField = new JTextField[3];
		btn = new JButton[3];

		handler = new ActionHandler();

		for(int i=0;i<5;i++){
			label[i] = new JLabel();
			label[i].setBounds(30,50+(i*30),300,30);
			label[i].setFont(Constants.Font1);
		}
		
		textField[0] = new JTextField();		// directory name
		textField[0].setBounds(30,140,250,30);
		textField[0].setFont(inputFont);

		textField[1] = new JTextField();		// file path
		textField[1].setBounds(30,140,300,30);
		textField[1].setEditable(false);
		textField[1].setFont(inputFont);
		
		textField[2] = new JTextField();		// download Path
		textField[2].setBounds(30,140,300,30);
		textField[2].setEditable(false);
		textField[2].setFont(inputFont);

		handler = new ActionHandler();
		
		// open file 버튼
		btn[0] = new JButton(new ImageIcon(Constants.ButtonPath.fileOpenBtn1.getPath()));
		btn[0].setRolloverIcon(new ImageIcon(Constants.ButtonPath.fileOpenBtn2.getPath()));
		btn[0].setBounds(280,80,40,40);
		btn[0].addActionListener(handler);

		// 확인버튼
		btn[1] = new JButton(new ImageIcon(Constants.ButtonPath.confirmBtn1.getPath()));
		btn[1].setRolloverIcon(new ImageIcon(Constants.ButtonPath.confirmBtn2.getPath()));
		btn[1].setBounds(150,200,80,30);
		btn[1].addActionListener(handler);

		// 취소버튼
		btn[2] = new JButton(new ImageIcon(Constants.ButtonPath.cancelBtn1.getPath()));
		btn[2].setRolloverIcon(new ImageIcon(Constants.ButtonPath.cancelBtn2.getPath()));
		btn[2].setBounds(250,200,80,30);
		btn[2].addActionListener(handler);
		
		this.add(bgImg[1]);
	}

	public void initialize() { 
		this.status=1;
		textField[0].setText("");
		textField[1].setText("");
		textField[2].setText("");
		label[0].setText("");
		label[1].setText("");
		label[2].setText("");
		label[3].setText("");
		label[4].setText("");
		changePanel();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public JLabel[] getLabel() {
		return label;
	}



	public void setLabel(JLabel[] label) {
		this.label = label;
	}
	
	public void setUploadFilePath(){
		String path = ClientLauncher.getFileMgr().loadUploadFile();
		if(path != null){
			textField[1].setText(path);
		}
	}
	
	public void setDownloadFilePath(){
		String path = ClientLauncher.getFileMgr().getDownloadPath();
		if(path != null){
			textField[1].setText(path);
		}
	}

	// 각종 status에 따라  Directory관리 패널을 변경
	public void changePanel() {

		this.removeAll();
		switch(this.status){
		case 0 : 	// Default
			this.add(label[0]);
			this.add(label[1]);
			this.add(label[2]);
			this.add(label[3]);
			this.add(label[4]);
			break;
		case 1 : 	// Information
			break;
		case 2 : 	// Create 
			this.add(textField[0]);
			this.add(btn[1]);
			this.add(btn[2]);
			break;
		case 3 : 	// Upload
			this.add(textField[1]);
			this.add(btn[0]);
			this.add(btn[1]);
			this.add(btn[2]);
			break;
		case 4 : 	//	Download
			textField[2].setText(ClientLauncher.getFileMgr().getDownloadPath());
			this.add(textField[2]);
			this.add(btn[1]);
			this.add(btn[2]);
			break;
		case 5 : 	//	Settings
			this.add(btn[1]);
			this.add(btn[2]);
			break;
		default : 
			break;
		}
		this.add(bgImg[status]);
		this.repaint();
	}

	// 버튼에 따른 이벤트 핸들러
	private class ActionHandler implements ActionListener {
		private String dirName;
		private String localUploadPath;	// upload fileName
		private MetaData m_data;	// upload metaData
		private String localDownloadPath;
		
		@Override
		public void actionPerformed(ActionEvent event) {

			// fileload버튼을 누르면 유저 컴퓨터에서 file을 선택하도록 함
			if(event.getSource()==btn[0]){
				setUploadFilePath();
			}
			
			// 확인버튼을 누르면 해당 상태에 맞는 함수를 call 
			if(event.getSource()==btn[1]){
				
				dirName	= textField[0].getText();
				localUploadPath = textField[1].getText();
				localDownloadPath = textField[2].getText();
				
				// create
				if(status == 2) {
					if(dirName.indexOf('.')!=-1){
						JOptionPane.showMessageDialog(null, "Incorrect dirName");
					}
					else{
						ClientLauncher.getFrame().getFileListPanel().create(dirName);
					}
				}
				// upload
				else if(status == 3) {
					if(localUploadPath.equals("")){
						JOptionPane.showMessageDialog(null, "Load file");
					}
					else{
						String selectedPath = ClientLauncher.getFrame().getFileListPanel().getSelectedPath();

						try {
							// return 0 = success	failure = -1
							if(ClientLauncher.getHybrid().upload(localUploadPath,selectedPath) == 0){
								/* 메타데이터 전송 */
								MetaData m_data = new MetaData();
								ClientLauncher.getFrame().getFileListPanel().upload(localUploadPath, m_data);
							}
							else
								JOptionPane.showMessageDialog(null, "upload failure");
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						//MetaData m_data = new MetaData();
						//ClientLauncher.getFrame().getFileListPanel().upload(localUploadPath, m_data);
						
					}
				}
				// download
				else if(status == 4) {
					if(localDownloadPath.equals("")){
						JOptionPane.showMessageDialog(null, "Load file");
					}
					else{
						String selectedPath = ClientLauncher.getFrame().getFileListPanel().getSelectedPath();
					//	String selectedPath = "/root/hey/test.txt";
						try {
							// return 0 = success	failure = -1
							if(ClientLauncher.getHybrid().download(selectedPath, localDownloadPath) == 0){
								ClientLauncher.getFrame().getFileListPanel().download();
							}
							else
								JOptionPane.showMessageDialog(null, "download failure");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						ClientLauncher.getFrame().getFileListPanel().download();
					}
				}
				// delete
				else if(status == 5) {
					ClientLauncher.getFrame().getFileListPanel().delete();
				}
				btn[2].doClick();
			}

			// 취소버튼을 누르면 초기화
			if(event.getSource()==btn[2]){
				textField[0].setText("");
				textField[1].setText("");
				if(ClientLauncher.getFrame().getDirectoryListPanel().getIsSelected()){
					setStatus(0);
				}
				else {
					label[1].setText("");
					setStatus(1);
				}
				changePanel();
			}
		}
	}
}

