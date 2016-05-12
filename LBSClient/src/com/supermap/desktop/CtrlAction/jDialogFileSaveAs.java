package com.supermap.desktop.CtrlAction;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.AbstractTableModel;

import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.button.SmButton;

public class jDialogFileSaveAs extends JDialog {
	
	private JLabel labelServerURL;
	private JTextField textServerURL;
	private JButton buttonBrowser;
	
	private JLabel labelLocalPath;
	private JTextField textLocalPath;

	private JButton buttonOK;
	private JButton buttonCancel;
	
	public static void main(String[] args) {
		jDialogFileSaveAs dialog = new jDialogFileSaveAs();
		dialog.setVisible(true);
	}
	
	public jDialogFileSaveAs() {
		initializeComponents();
	}
	
	public void initializeComponents() {
		this.setSize(900, 600);
		this.setLocation(400, 300);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.labelServerURL = new JLabel("服务器地址:");
		this.textServerURL = new JTextField("Web URL");
		this.buttonBrowser = new JButton("浏览");
		
		this.labelLocalPath = new JLabel("本地路径:");
		this.textLocalPath = new JTextField("Local Path");
		
		this.buttonOK = new SmButton(CommonProperties.getString("String_Button_OK"));
		this.buttonCancel = new SmButton(CommonProperties.getString("String_Button_Cancel"));
		
		this.buttonBrowser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		this.buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				buttonCancelActionPerformed();
			}
		});
		
		this.buttonCancel.addActionListener(new ActionListener() {
			
		});
		
		GroupLayout gLayout = new GroupLayout(this.getContentPane());
		gLayout.setAutoCreateContainerGaps(true);
		gLayout.setAutoCreateGaps(true);
		this.getContentPane().setLayout(gLayout);	
		
		// @formatter:off
		gLayout.setHorizontalGroup(gLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(gLayout.createSequentialGroup()
						.addComponent(this.labelServerURL)
						.addComponent(this.textServerURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.buttonBrowser, 32, 32, 32))
				.addGroup(gLayout.createSequentialGroup()
						.addComponent(this.labelLocalPath)
						.addComponent(this.textLocalPath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(gLayout.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOK, 75, 75, 75)
						.addComponent(this.buttonCancel, 75, 75, 75)));
		gLayout.setVerticalGroup(gLayout.createSequentialGroup()
				.addGroup(gLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelServerURL)
						.addComponent(this.textServerURL, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonBrowser, 23, 23, 23))
				.addGroup(gLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelLocalPath)
						.addComponent(this.textLocalPath, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
	}

}
