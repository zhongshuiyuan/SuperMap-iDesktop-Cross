package com.supermap.desktop.ui.controls.prjcoordsys.prjCoordSysTransPanels;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.button.SmButton;
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
 * 提供三种方式选择投影坐标系：来自数据源、选择、导入文件
 * 面板主要提供设置好的坐标系
 */
public class PanelTargetCoordSys extends JPanel {
	/**
	 *
	 */
	private JRadioButton radioButtonFromDatasource;
	private JRadioButton radioButtonPrjSetting;
	private JRadioButton radioButtonImportPrjFile;
	private DatasourceComboBox datasource;
	private SmButton buttonPrjSetting;
	private JFileChooserControl fileChooser;
	private PanelCoordSysInfo panelCoordSysInfo;
	private PrjCoordSys targetPrjCoordSys = null;
	private PrjCoordSys buttonSetPrjCoordSys = null;
	private PrjCoordSys importFilePrjCoordSys = null;
	private DoSome doSome;

	/**
	 * 获得设置好的坐标系
	 *
	 * @return
	 */
	public PrjCoordSys getTargetPrjCoordSys() {
		return targetPrjCoordSys;
	}

	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(radioButtonFromDatasource) || e.getSource().equals(radioButtonPrjSetting) || e.getSource().equals(radioButtonImportPrjFile)) {
				datasource.setEnabled(radioButtonFromDatasource.isSelected());
				buttonPrjSetting.setEnabled(radioButtonPrjSetting.isSelected());
				fileChooser.setEnabled(radioButtonImportPrjFile.isSelected());
				// 坐标系来自数据源
				if (radioButtonFromDatasource.isSelected()) {
					if (datasource.getSelectedDatasource() != null) {
						targetPrjCoordSys = datasource.getSelectedDatasource().getPrjCoordSys();
					} else {
						targetPrjCoordSys = null;
					}
				} else if (radioButtonPrjSetting.isSelected()) {
					// 坐标系来自设置，当设置过一次后用buttonSetprjCoordSys记录上次设置的坐标系
					targetPrjCoordSys = buttonSetPrjCoordSys;
				} else if (radioButtonImportPrjFile.isSelected()) {
					// 坐标系来自导入的文件
					targetPrjCoordSys = importFilePrjCoordSys;
				}
				// 点击单选框，
				setPrjCoordSysInfo(targetPrjCoordSys);
			} else if (e.getSource().equals(buttonPrjSetting)) {
				// 当点击了投影设置，并且设置了投影
				JDialogPrjCoordSysSettings dialogPrjCoordSysSettings = new JDialogPrjCoordSysSettings();
				if (dialogPrjCoordSysSettings.showDialog() == DialogResult.OK) {
					buttonSetPrjCoordSys = dialogPrjCoordSysSettings.getPrjCoordSys();
					setPrjCoordSysInfo(buttonSetPrjCoordSys);
				}
			}
		}
	};


	/**
	 * 当导入文件路径改变时触发监听事件，此时重新获得路径下文件的坐标系
	 */
	private FileChooserPathChangedListener exportPathDocumentListener = new FileChooserPathChangedListener() {
		@Override
		public void pathChanged() {
			importFilePrjCoordSys = getPrjCoordSysFromImportFile();
			setPrjCoordSysInfo(importFilePrjCoordSys);
		}
	};

	public PanelTargetCoordSys(DoSome doSome) {
		this.doSome = doSome;
		initializeComponents();
		initializeResources();
		initializeLayout();
		initListener();
		initStates();
	}


	private void initializeComponents() {
		this.radioButtonFromDatasource = new JRadioButton();
		this.radioButtonPrjSetting = new JRadioButton();
		this.radioButtonImportPrjFile = new JRadioButton();

		ButtonGroup bufferTypeButtonGroup = new ButtonGroup();
		bufferTypeButtonGroup.add(this.radioButtonFromDatasource);
		bufferTypeButtonGroup.add(this.radioButtonPrjSetting);
		bufferTypeButtonGroup.add(this.radioButtonImportPrjFile);

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
		this.buttonPrjSetting = new SmButton();

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
		this.radioButtonFromDatasource.setText(ControlsProperties.getString("String_Label_FromDatasource"));
		this.radioButtonPrjSetting.setText(ControlsProperties.getString("String_Label_CustomPrjCoordSysSetting"));
		this.radioButtonImportPrjFile.setText(ControlsProperties.getString("String_Label_ImportPrjCoordSysFile"));
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
								.addComponent(this.radioButtonFromDatasource)
								.addComponent(this.radioButtonPrjSetting)
								.addComponent(this.radioButtonImportPrjFile))
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.datasource)
								.addComponent(this.buttonPrjSetting)
								.addComponent(this.fileChooser)))
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(this.panelCoordSysInfo)));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.radioButtonFromDatasource)
						.addComponent(this.datasource, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.radioButtonPrjSetting)
						.addComponent(this.buttonPrjSetting, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.radioButtonImportPrjFile)
						.addComponent(this.fileChooser, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(this.panelCoordSysInfo))
		);
		// @formatter:on
	}

	private void initListener() {
		removeListener();
		this.radioButtonFromDatasource.addActionListener(actionListener);
		this.radioButtonPrjSetting.addActionListener(actionListener);
		this.radioButtonImportPrjFile.addActionListener(actionListener);
		this.buttonPrjSetting.addActionListener(actionListener);
		this.fileChooser.addFileChangedListener(exportPathDocumentListener);
	}

	private void removeListener() {
		this.radioButtonFromDatasource.removeActionListener(actionListener);
		this.radioButtonPrjSetting.removeActionListener(actionListener);
		this.radioButtonImportPrjFile.removeActionListener(actionListener);
		this.buttonPrjSetting.removeActionListener(actionListener);
		this.fileChooser.removePathChangedListener(exportPathDocumentListener);

	}


	/**
	 * 初始值设置
	 * 默认使用数据源的坐标系，当打开的数据源没有坐标系时，坐标系获取方式为设置坐标系
	 */
	private void initStates() {
		if (this.datasource.getSelectedDatasource() != null) {
			this.radioButtonFromDatasource.setSelected(true);
			this.buttonPrjSetting.setEnabled(false);
			setPrjCoordSysInfo(this.datasource.getSelectedDatasource().getPrjCoordSys());
		} else {
			this.radioButtonPrjSetting.setSelected(true);
			setPrjCoordSysInfo(null);
		}
	}

	/**
	 * 设置投影信息
	 *
	 * @param prjCoordSysInfo
	 */
	private void setPrjCoordSysInfo(PrjCoordSys prjCoordSysInfo) {
		this.targetPrjCoordSys = prjCoordSysInfo;
		this.panelCoordSysInfo.setCoordInfo(PrjCoordSysUtilities.getDescription(prjCoordSysInfo));
		// 通过doSome，将改变后的targetPrjCoordSys值传给主面板
		this.doSome.setTargetPrjCoordSys(this.targetPrjCoordSys);
		// 如果值为空，控制主面板确定按钮不可用
		this.doSome.setOKButtonEnabled(this.targetPrjCoordSys != null);
	}

	/**
	 * 通过导入的投影文件获得文件投影信息
	 *
	 * @return
	 */
	private PrjCoordSys getPrjCoordSysFromImportFile() {

		if (!new File(this.fileChooser.getPath()).exists()) {
			return null;
		} else {
			PrjCoordSys newPrjCoorSys = new PrjCoordSys();
			String fileType = FileUtilities.getFileType(this.fileChooser.getPath());
			boolean isPrjFile;
			if (fileType.equalsIgnoreCase(".prj")) {
				isPrjFile = newPrjCoorSys.fromFile(this.fileChooser.getPath(), PrjFileType.ESRI);
			} else {
				isPrjFile = newPrjCoorSys.fromFile(this.fileChooser.getPath(), PrjFileType.SUPERMAP);
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
	public void setPanelEnabled(Boolean isEnable) {
		this.radioButtonFromDatasource.setEnabled(isEnable);
		this.radioButtonPrjSetting.setEnabled(isEnable);
		this.radioButtonImportPrjFile.setEnabled(isEnable);
		this.datasource.setEnabled(isEnable);
		this.buttonPrjSetting.setEnabled(isEnable);
		this.fileChooser.setEnabled(isEnable);
		this.panelCoordSysInfo.setEnabled(isEnable);
	}
}
