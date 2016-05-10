package com.supermap.desktop.CtrlAction;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.io.InputStream;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;

import org.apache.commons.io.IOUtils;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTableModel;
import com.supermap.desktop.utilties.CursorUtilties;

public class JDialogHDFSFiles extends SmDialog {

	public static String webURL = "http://192.168.12.103:50070/webhdfs/v1/data/";
	public static String webFile = "test0.csv"; // mobile0426095637.csv"

	public static void main(String[] args) {
		String aaa = getFilePath();
		JDialogHDFSFiles f = new JDialogHDFSFiles();
		f.showDialog();
	}
	
	public static String getFilePath() {
		String serverPath = webURL;
		serverPath = serverPath.replace("webhdfs/v1/", "");
		serverPath = serverPath.replace("http", "hdfs");
		serverPath += webFile;
		return serverPath;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final int COLUMN_INDEX_Permission = 0;
	private static final int COLUMN_INDEX_Owner = 1;
	private static final int COLUMN_INDEX_Group = 2;
	private static final int COLUMN_INDEX_Size = 3;
	private static final int COLUMN_INDEX_Replication = 4;
	private static final int COLUMN_INDEX_BlockSize = 5;
	private static final int COLUMN_INDEX_Name = 6;
	
	private JLabel labelServerURL;
	private JTextField textServerURL;
	private JButton buttonBrowser;
	private JTable table;
	private SmButton buttonOK;
	private SmButton buttonCancel;

	/**
	 * Create the dialog.
	 */
	public JDialogHDFSFiles() {
		initializeComponents();
		initializeResources();
	}

	public void initializeComponents() {
		this.setSize(900, 600);

		this.labelServerURL = new JLabel("服务器地址:");
		this.textServerURL = new JTextField(webURL);
		this.buttonBrowser = new JButton("浏览");

		this.buttonOK = new SmButton(CommonProperties.getString("String_Button_OK"));
		this.buttonCancel = new SmButton(CommonProperties.getString("String_Button_Cancel"));
		this.getRootPane().setDefaultButton(this.buttonOK);

		GroupLayout gLayout = new GroupLayout(this.getContentPane());
		gLayout.setAutoCreateContainerGaps(true);
		gLayout.setAutoCreateGaps(true);
		this.getContentPane().setLayout(gLayout);

		this.buttonBrowser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonBrowserActionPerformed();
			}
		});

		this.buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonOKActionPerformed();
			}
		});

		this.buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonCancelActionPerformed();
			}
		});

		this.table = new JTable();
		this.table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		HDFSTableModel tableModel = new HDFSTableModel(//
				new String[] { "Permission", "Owner", "Group", "Size", "Replication", "BlockSize", "Name" }) {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] { false, false, false, false, false, false, false };

			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		this.table.setModel(tableModel);
		this.table.putClientProperty("terminateEditOnFocusLost", true);

		this.table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				table_MouseClicked(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				table_MouseReleased(e);
			}
		});
		this.table.setRowHeight(23);

		JScrollPane scrollPaneTable = new JScrollPane(table);
		// @formatter:off
		gLayout.setHorizontalGroup(gLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(gLayout.createSequentialGroup().addComponent(this.labelServerURL)
						.addComponent(this.textServerURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addComponent(this.buttonBrowser, 32, 32, 32))
				.addComponent(scrollPaneTable, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gLayout.createSequentialGroup().addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOK, 75, 75, 75).addComponent(this.buttonCancel, 75, 75, 75)));

		gLayout.setVerticalGroup(gLayout.createSequentialGroup()
				.addGroup(gLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelServerURL)
						.addComponent(this.textServerURL, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonBrowser))
				// .addGap(7)
				.addComponent(scrollPaneTable, 100, 200, Short.MAX_VALUE)
				// .addGap(7)
				.addGroup(gLayout.createParallelGroup(Alignment.CENTER).addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on
	}

	public DialogResult showDialog() {
		this.setVisible(true);
		return this.dialogResult;
	}

	private void initializeResources() {
		if (table != null) {
			this.setTitle(("Browse Directory"));
			this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
			this.buttonOK.setText(CommonProperties.getString("String_Button_OK"));

			this.table.getColumnModel().getColumn(COLUMN_INDEX_Permission).setHeaderValue("Permission");
			this.table.getColumnModel().getColumn(COLUMN_INDEX_Owner).setHeaderValue("Owner");
			this.table.getColumnModel().getColumn(COLUMN_INDEX_Group).setHeaderValue(("Group"));
			this.table.getColumnModel().getColumn(COLUMN_INDEX_Size).setHeaderValue("Size");
			this.table.getColumnModel().getColumn(COLUMN_INDEX_Replication).setHeaderValue("Replication");
			this.table.getColumnModel().getColumn(COLUMN_INDEX_BlockSize).setHeaderValue(("BlockSize"));
			this.table.getColumnModel().getColumn(COLUMN_INDEX_Name).setHeaderValue("Name");
		}
	}

	/**
	 * 添加一行记录
	 *
	 * @param rowIndex
	 * 
	 */
	private void addFileInfo(HDFSDefine hdfsDefine) {
		try {
			HDFSTableModel tableModel = (HDFSTableModel)this.table.getModel();
			tableModel.addRow(hdfsDefine);
//			if (hdfsDefine.isDir()) {
//				int rowIndex = this.table.getRowCount() - 1;
//				TableCellEditor tableCellEditor = this.table.getCellEditor(rowIndex, COLUMN_INDEX_Name);
//				Component component = tableCellEditor.getTableCellEditorComponent(table, hdfsDefine.getName(), false, rowIndex, COLUMN_INDEX_Name);			
//				Font font = new Font(component.getFont().getName(), Font.BOLD + Font.ITALIC, component.getFont().getSize());
//				component.setFont(font);
//			}
					
			this.table.updateUI();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public void table_MouseClicked(MouseEvent e) {
		
		if (table.getSelectedRow() != -1 && e.getClickCount() == 2) {
			HDFSDefine define = (HDFSDefine)((HDFSTableModel)this.table.getModel()).getRowTagAt(table.getSelectedRow());
			if (define != null) {
				// if mouse double click foler, list folder files
				if (define.isDir()) {
					String name = (String)this.table.getModel().getValueAt(table.getSelectedRow(), COLUMN_INDEX_Name);
					String url = this.listDirectory(this.textServerURL.getText(), name);
					this.textServerURL.setText(url);
				} else {
					this.buttonOKActionPerformed();
				}
			} 
		}
	}

	public void table_MouseReleased(MouseEvent e) {		
		
	}

	private String listDirectory(String urlPath, String childFolder) {
		
//		for (int i = 0; i < 6; i++) {
//			HDFSDefine hdfsDefine = new HDFSDefine("permission", "owner", "group", "length", "replication", "blockSize", "pathSuffix", true);	
//			this.addFileInfo(hdfsDefine);
//		}
		
		// 删除后设置第0行被选中
		if (0 < table.getRowCount()) {
			HDFSTableModel tableModel = (HDFSTableModel) table.getModel();
			tableModel.removeRows(0, tableModel.getRowCount());
			table.updateUI();			
		}

		int itemsCount = 26;
		if (!urlPath.endsWith("/")) {
			urlPath += "/";
		}
		
		if (!"".equals(childFolder)) {
			if (!childFolder.endsWith("/")) {
				childFolder += "/";
			}
			
			if (childFolder.startsWith("/")) {
				childFolder.substring(1, childFolder.length() - 1);
			}
			
			urlPath += childFolder;
		}
		
		String result = GetContentUsingHttp(urlPath + "?op=LISTSTATUS", "GET");

		String[] temps = result.split("\"|,|:|\\[|\\]|\\{|\\}|\\\r|\\\n");
		ArrayList<String> results = new ArrayList<String>();
		for (String temp : temps) {
			if (!temp.trim().equals("")) {
				results.add(temp.trim());
			}
		}

		// 开始：最前面两个节点 {"FileStatuses":{"FileStatus":[
		// 结尾：]}}
		results.remove(1);
		results.remove(0);
		// "accessTime":0,"blockSize":0,"childrenNum":24,"fileId":16386,"group":"supergroup","length":0,"modificationTime":1461949836347,
		// "owner":"root","pathSuffix":"data","permission":"755","replication":0,"storagePolicy":0,"type":"DIRECTORY"
		String permission = "", owner = "", group = "", length = "", replication = "", blockSize = "", pathSuffix = "", type = "";
		int count = results.size() / itemsCount;
		for (int i = 0; i < results.size(); i += itemsCount) {
			for (int j = 0; j < itemsCount; j += 2) {
				if (results.get(i + j).equalsIgnoreCase("permission")) {
					permission = results.get(i + j + 1);
				} else if (results.get(i + j).equalsIgnoreCase("owner")) {
					owner = results.get(i + j + 1);
				} else if (results.get(i + j).equalsIgnoreCase("group")) {
					group = results.get(i + j + 1);
				} else if (results.get(i + j).equalsIgnoreCase("length")) {
					length = results.get(i + j + 1);
				} else if (results.get(i + j).equalsIgnoreCase("replication")) {
					replication = results.get(i + j + 1);
				} else if (results.get(i + j).equalsIgnoreCase("blockSize")) {
					blockSize = results.get(i + j + 1);
				} else if (results.get(i + j).equalsIgnoreCase("pathSuffix")) {
					pathSuffix = results.get(i + j + 1);
				} else if (results.get(i + j).equalsIgnoreCase("type")) {
					type = results.get(i + j + 1);
				}
			}

			Boolean isDir = false;
			if (type.equalsIgnoreCase("DIRECTORY")) {
				isDir = true;
			}
			
			HDFSDefine hdfsDefine = new HDFSDefine(permission, owner, group, length, replication, blockSize, pathSuffix, isDir);
			this.addFileInfo(hdfsDefine);
			
			if (0 < table.getRowCount()) {
				table.setRowSelectionInterval(0, 0);
			}
		}
		
		return urlPath;
	}

	public String GetContentUsingHttp(String urlPath, String method) {
		String result = "";
		try {
			if (method.equalsIgnoreCase("GET")) {
				InputStream inputStream = http_Get.getInputStream(urlPath);
				try {
					result = IOUtils.toString(inputStream);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
			// using (HttpWebResponse response = request.GetResponse() as
			// HttpWebResponse)
			// {
			// //if(response.StatusCode == HttpStatusCode.OK)
			// {
			// using (Stream responseStream = response.GetResponseStream())
			// {
			// using (StreamReader read = new StreamReader(responseStream,
			// Encoding.UTF8))
			// {
			// ret = read.ReadToEnd();
			// }
			// }
			// }
			// response.Close();
			// }
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	/**
	 * 浏览按钮点击事件
	 */
	private void buttonBrowserActionPerformed() {
		try {
			listDirectory(this.textServerURL.getText(), "");
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilties.setDefaultCursor();
		}
	}

	/**
	 * 确定按钮点击事件
	 */
	private void buttonOKActionPerformed() {
		try {			
			if (table.getSelectedRow() != -1) {
				HDFSDefine define = (HDFSDefine)((HDFSTableModel)this.table.getModel()).getRowTagAt(table.getSelectedRow());
				if (define != null && !define.isDir()) {
					JDialogHDFSFiles.webFile = define.getName();
					JDialogHDFSFiles.webURL = this.textServerURL.getText();
					
					this.dispose();
					this.dialogResult = DialogResult.OK;
				} 
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilties.setDefaultCursor();
		}
	}

	/**
	 * 关闭按钮点击事件
	 */
	private void buttonCancelActionPerformed() {
		this.dispose();
		this.dialogResult = DialogResult.CANCEL;
	}
	
	/**
	 * describe a HDFS file
	 *
	 * @author huchenpu
	 */
	public class HDFSDefine {
		private String fullPath = "";
		String permission = "", owner = "", group = "", length = "", replication = "", blockSize = "", pathSuffix = "";
		Boolean isDir = false;

		public HDFSDefine(String permission, String owner, String group, String size, String replication,
				String blockSize, String name, Boolean isDir) {
			this.permission = permission;
			this.owner = owner;
			this.group = group;
			this.length = size;
			this.replication = replication;
			this.blockSize = blockSize;
			this.pathSuffix = name;
			this.isDir = isDir;
		}
		
		public String getPermission() {
			return permission;
		}

		public void setPermission(String permission) {
			this.permission = permission;
		}	
		
		public String getOwner() {
			return owner;
		}

		public void setOwner(String owner) {
			this.owner = owner;
		}	
		
		public String getGroup() {
			return this.group;
		}

		public void setGroup(String group) {
			this.group = group;
		}	
		
		public String getSize() {
			return this.length;
		}

		public void setSize(String length) {
			this.length = length;
		}	
		
		public String getReplication() {
			return this.replication;
		}

		public void setReplication(String replication) {
			this.replication = replication;
		}	
		
		public String getBlockSize() {
			return this.blockSize;
		}

		public void setBlockSize(String blockSize) {
			this.blockSize = blockSize;
		}	
		
		public String getName() {
			return this.pathSuffix;
		}

		public void setName(String pathSuffix) {
			this.pathSuffix = pathSuffix;
		}	
		
		public Boolean isDir() {
			return this.isDir;
		}

		public void setIsDir(Boolean isDir) {
			this.isDir = isDir;
		}	

		public String getFullPath() {
			return fullPath;
		}

		public void setFullPath(String fullPath) {
			this.fullPath = fullPath;
		}		

		@Override
		public String toString() {
			return this.fullPath;
		}
	}
	
	/**
	 * describe a HDFS file DataModel
	 *
	 * @author huchenpu
	 */
	private class HDFSTableModel extends MutiTableModel {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

//	    /**
//	     * 构造函数。
//	     */
//	    public HDFSTableModel() {
//	    }

	    /**
	     * 构造函数。
	     * @param columnNames
	     */
	    public HDFSTableModel(String[] columnNames) {
	        super(columnNames);
	    }
		
		/**
		 * 添加指定数据的一行。<br>
		 * 
		 * @param define　数据
		 * @throws Exception 抛出数据数不正确的异常
		 */
		public void addRow(HDFSDefine define) {
			if (null == define) {
				return;
			}

			// 初始化内容存储
			Vector<Object> content = new Vector<Object>(this.columnNames.size());
			content.add(define.getPermission());
			content.add(define.getOwner());
			content.add(define.getGroup());
			content.add(define.getSize());
			content.add(define.getReplication());
			content.add(define.getBlockSize());
			content.add(define.getName());	
			
			// 追加内容
			contents.add(content);	
			this.setRowTagAt(define, this.getRowCount() - 1);
		}
	}
}
