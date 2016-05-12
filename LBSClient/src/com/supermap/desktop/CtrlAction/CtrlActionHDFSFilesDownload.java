package com.supermap.desktop.CtrlAction;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.CtrlAction.CtrlActionFindTrack.WorkThead;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;

public class CtrlActionHDFSFilesDownload extends CtrlAction {

	public CtrlActionHDFSFilesDownload(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			
//			JFrame parent = (JFrame)Application.getActiveApplication().getMainFrame();
//			JDialogFindTrack dialog = new JDialogFindTrack(parent, true);
//			DialogResult result = dialog.showDialog();
//			if (result == DialogResult.OK || result == DialogResult.APPLY) {
				WorkThead thread = new WorkThead();
				thread.start();
//			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}

	class WorkThead extends Thread {

		@Override
		public void run() {
			try {
				String hdfsFileURL = webHDFS.getHDFSFileURL();
				String localPath = "/home/huchenpu/demo/result/" + webHDFS.webFile;
				webHDFS.getFile(hdfsFileURL, localPath);
			} finally {
			}
		}
	}
}