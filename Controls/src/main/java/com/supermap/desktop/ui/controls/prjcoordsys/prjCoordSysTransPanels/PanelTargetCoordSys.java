package com.supermap.desktop.ui.controls.prjcoordsys.prjCoordSysTransPanels;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogPrjCoordSysSettings;
import com.supermap.desktop.utilities.FileUtilities;
import com.supermap.desktop.utilities.PrjCoordSysUtilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by yuanR on 2017/9/25 0025.
 * 投影转换.目标坐标系面板
 */
public class PanelTargetCoordSys extends JPanel {
	/**
	 *
	 */
	private JRadioButton fromDatasource;
	private JRadioButton prjSetting;
	private JRadioButton importPrjFile;
	private DatasourceComboBox datasource;
	private JButton buttonPrjSetting;
	private JFileChooserControl fileChooser;
	private PanelCoordSysInfo panelCoordSysInfo;
	private PrjCoordSys prjCoordSys = null;
	private PrjCoordSys buttonSetprjCoordSys = null;


	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(fromDatasource) || e.getSource().equals(prjSetting) || e.getSource().equals(importPrjFile)) {
				datasource.setEnabled(fromDatasource.isSelected());
				buttonPrjSetting.setEnabled(prjSetting.isSelected());
				fileChooser.setEnabled(importPrjFile.isSelected());
				// 坐标系来自数据源
				if (fromDatasource.isSelected()) {
					if (datasource.getSelectedDatasource() != null) {
						prjCoordSys = datasource.getSelectedDatasource().getPrjCoordSys();
					} else {
						prjCoordSys = null;
					}
				} else if (prjSetting.isSelected()) {
					prjCoordSys = buttonSetprjCoordSys;
				} else if (importPrjFile.isSelected()) {
					prjCoordSys = getPrjCoordSysFromImportFile();
				}
				// 点击单选框，
				setPrjCoordSysInfo(prjCoordSys);
			} else if (e.getSource().equals(buttonPrjSetting)) {
				// 当点击了投影设置，并且设置了投影
				JDialogPrjCoordSysSettings dialogPrjCoordSysSettings = new JDialogPrjCoordSysSettings();
				if (dialogPrjCoordSysSettings.showDialog() == DialogResult.OK) {
					setPrjCoordSysInfo(dialogPrjCoordSysSettings.getPrjCoordSys());
				}
			}
		}
	};


	/**
	 * 当路径
	 */
	private FileChooserPathChangedListener exportPathDocumentListener = new FileChooserPathChangedListener() {
		@Override
		public void pathChanged() {
			setPrjCoordSysInfo(getPrjCoordSysFromImportFile());
		}
	};

	public PanelTargetCoordSys() {
		initializeComponents();
		initializeResources();
		initializeLayout();
		initListener();
		initStates();

	}


	private void initializeComponents() {
		this.fromDatasource = new JRadioButton();
		this.prjSetting = new JRadioButton();
		this.importPrjFile = new JRadioButton();

		ButtonGroup bufferTypeButtonGroup = new ButtonGroup();
		bufferTypeButtonGroup.add(this.fromDatasource);
		bufferTypeButtonGroup.add(this.prjSetting);
		bufferTypeButtonGroup.add(this.importPrjFile);

		// 获得有投影坐标系的数据源
		ArrayList<Datasource> datasourceArray = new ArrayList<>();
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		if (null != datasources) {
			for (int i = 0; i < datasources.getCount(); i++) {
				if (!datasources.get(i).getPrjCoordSys().getType().equals(PrjCoordSysType.PCS_NON_EARTH)) {
					datasourceArray.add(datasources.get(i));
				}
			}
		}
		this.datasource = new DatasourceComboBox(datasourceArray);
		this.buttonPrjSetting = new JButton();


		String moduleName = "ImportPrjFile";
		if (!SmFileChoose.isModuleExist(moduleName)) {
			String fileFilters = SmFileChoose.buildFileFilters(
					SmFileChoose.createFileFilter(ControlsProperties.getString("String_ImportPrjFiles"), "prj", "xml"),
					SmFileChoose.createFileFilter(ControlsProperties.getString("String_ImportPrjFileShape"), "prj"),
					SmFileChoose.createFileFilter(ControlsProperties.getString("String_ImportPrjFileXml"), "xml"));
			SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
					ControlsProperties.getString("String_ImportPrjFile"), moduleName, "OpenMany");
		}
		SmFileChoose smFileChoose = new SmFileChoose(moduleName);
		this.fileChooser = new JFileChooserControl();
		this.fileChooser.setFileChooser(smFileChoose);

		this.panelCoordSysInfo = new PanelCoordSysInfo("");
	}

	private void initializeResources() {
		this.fromDatasource.setText(ControlsProperties.getString("String_Label_FromDatasource"));
		this.prjSetting.setText(ControlsProperties.getString("String_Label_CustomPrjCoordSysSetting"));
		this.importPrjFile.setText(ControlsProperties.getString("String_Label_ImportPrjCoordSysFile"));
		this.buttonPrjSetting.setText(ControlsProperties.getString("String_SetProjection_Caption"));
		this.panelCoordSysInfo = new PanelCoordSysInfo("");
	}

	private void initializeLayout() {
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);
		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.fromDatasource)
								.addComponent(this.prjSetting)
								.addComponent(this.importPrjFile))
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.datasource)
								.addComponent(this.buttonPrjSetting)
								.addComponent(this.fileChooser)))
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(this.panelCoordSysInfo)));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.fromDatasource)
						.addComponent(this.datasource, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.prjSetting)
						.addComponent(this.buttonPrjSetting, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.importPrjFile)
						.addComponent(this.fileChooser, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(this.panelCoordSysInfo))
		);
		// @formatter:on
	}

	private void initListener() {
		removeListener();
		this.fromDatasource.addActionListener(actionListener);
		this.prjSetting.addActionListener(actionListener);
		this.importPrjFile.addActionListener(actionListener);
		this.buttonPrjSetting.addActionListener(actionListener);
		this.fileChooser.addFileChangedListener(exportPathDocumentListener);
	}

	private void removeListener() {
		this.fromDatasource.removeActionListener(actionListener);
		this.prjSetting.removeActionListener(actionListener);
		this.importPrjFile.removeActionListener(actionListener);
		this.buttonPrjSetting.removeActionListener(actionListener);
		this.fileChooser.removePathChangedListener(exportPathDocumentListener);

	}


	/**
	 * 初始值设置
	 * 默认使用数据源的坐标系，当打开的数据源没有坐标系时，坐标系获取方式为设置坐标系
	 */
	private void initStates() {
		if (datasource.getSelectedDatasource() != null) {
			fromDatasource.setSelected(true);
			setPrjCoordSysInfo(datasource.getSelectedDatasource().getPrjCoordSys());
		} else {
			prjSetting.setSelected(true);
		}
	}

	/**
	 * 设置投影信息
	 *
	 * @param prjCoordSysInfo
	 */
	private void setPrjCoordSysInfo(PrjCoordSys prjCoordSysInfo) {
		prjCoordSys = prjCoordSysInfo;
		panelCoordSysInfo.setCoordInfo(PrjCoordSysUtilities.getDescription(prjCoordSysInfo));
	}

	/**
	 * 通过导入的投影文件获得文件投影信息
	 *
	 * @return
	 */
	private PrjCoordSys getPrjCoordSysFromImportFile() {

		if (!new File(fileChooser.getPath()).exists()) {
			return null;
		} else {
			PrjCoordSys newPrjCoorSys = new PrjCoordSys();
			String fileType = FileUtilities.getFileType(fileChooser.getPath());
			boolean isPrjFile;
			if (fileType.equalsIgnoreCase(".prj")) {
				isPrjFile = newPrjCoorSys.fromFile(fileChooser.getPath(), PrjFileType.ESRI);
			} else {
				isPrjFile = newPrjCoorSys.fromFile(fileChooser.getPath(), PrjFileType.SUPERMAP);
			}
			if (isPrjFile) {
				return newPrjCoorSys;
			} else {
				return null;
			}
		}
	}

	/**
	 * 设置面板是否可用
	 *
	 * @param isEnable
	 */
	private void setPanelEnabled(Boolean isEnable) {
		this.fromDatasource.setEnabled(isEnable);
		this.prjSetting.setEnabled(isEnable);
		this.importPrjFile.setEnabled(isEnable);
		this.datasource.setEnabled(isEnable);
		this.buttonPrjSetting.setEnabled(isEnable);
		this.fileChooser.setEnabled(isEnable);
		this.panelCoordSysInfo.setEnabled(isEnable);

	}
}
