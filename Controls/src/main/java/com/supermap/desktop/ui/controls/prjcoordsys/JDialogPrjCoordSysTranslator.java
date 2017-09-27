package com.supermap.desktop.ui.controls.prjcoordsys;

import com.supermap.data.PrjCoordSys;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.borderPanel.PanelButton;
import com.supermap.desktop.ui.controls.borderPanel.PanelResultDataset;
import com.supermap.desktop.ui.controls.prjcoordsys.prjCoordSysTransPanels.PanelCoordSysInfo;
import com.supermap.desktop.ui.controls.prjcoordsys.prjCoordSysTransPanels.PanelReferSysTransSettings;
import com.supermap.desktop.ui.controls.prjcoordsys.prjCoordSysTransPanels.PanelTargetCoordSys;

import javax.swing.*;
import java.awt.*;

/**
 * Created by yuanR on 2017/9/25 0025.
 * 投影转换主窗体
 */
public class JDialogPrjCoordSysTranslator extends SmDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// 源数据面板块
	private JPanel panelSourceData;
	private JLabel labelDatasource;
	private JLabel labelDataset;
	private DatasourceComboBox datasource;
	private DatasetComboBox dataset;

	// 坐标系信息面板块
	private PanelCoordSysInfo panelCoordSysInfo;
	// 参照系转换设置面板块
	private PanelReferSysTransSettings panelReferSysTransSettings;
	// 结果另存为面板块
	private PanelResultDataset panelResultDataset;
	// 目标坐标系块
	private PanelTargetCoordSys panelTargetCoordSys;
	// 确定取消按钮；
	private PanelButton panelButton;
	private transient PrjCoordSys targetPrj = null;
	private transient PrjCoordSys beforePrj = null;


	public JDialogPrjCoordSysTranslator(PrjCoordSys beforePrj) {
		this.beforePrj = beforePrj;
		initializeComponents();
		initializeResources();
		initializeLayout();
		initStates();

		setSize(800, 500);
		setLocationRelativeTo(null);
	}

	private void initStates() {
	}


	private void initializeComponents() {
		this.labelDataset = new JLabel("Dataset");
		this.labelDatasource = new JLabel("Datasource");
		this.datasource = new DatasourceComboBox();
		this.dataset = new DatasetComboBox();
		this.panelSourceData = new JPanel();

		this.panelCoordSysInfo = new PanelCoordSysInfo("");
		this.panelReferSysTransSettings = new PanelReferSysTransSettings();
		this.panelResultDataset = new PanelResultDataset("", true);
		this.panelTargetCoordSys = new PanelTargetCoordSys();
		panelButton = new PanelButton();

	}

	private void initializeResources() {
		this.labelDatasource.setText(ControlsProperties.getString("String_Label_ResultDatasource"));
		this.labelDataset.setText(ControlsProperties.getString("String_Label_ResultDataset"));
		this.panelSourceData.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_GroupBox_SourceDataset")));
		this.panelCoordSysInfo.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_GroupBox_SrcCoordSys")));
		this.panelReferSysTransSettings.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_GroupBox_CoordSysTranslatorSetting")));
		this.panelTargetCoordSys.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_GroupBox_TarCoorSys")));
	}

	private void initializeLayout() {
		// 原数据面板布局
		GroupLayout groupLayout = new GroupLayout(panelSourceData);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		panelSourceData.setLayout(groupLayout);

		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelDataset)
						.addComponent(this.labelDatasource))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.dataset)
						.addComponent(this.datasource)));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelDataset)
						.addComponent(this.dataset, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelDatasource)
						.addComponent(this.datasource, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		// @formatter:on

		// 主面板布局
		JPanel mianPanel = new JPanel();
		mianPanel.setLayout(new GridBagLayout());
		mianPanel.add(this.panelSourceData, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(10, 5, 0, 0).setWeight(1, 0));
		mianPanel.add(this.panelCoordSysInfo, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setWeight(1, 1));
		mianPanel.add(this.panelReferSysTransSettings, new GridBagConstraintsHelper(0, 2, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setWeight(1, 0));
		mianPanel.add(this.panelResultDataset.getPanel(), new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(5, 0, 0, 5).setWeight(1, 0));
		mianPanel.add(this.panelTargetCoordSys, new GridBagConstraintsHelper(1, 1, 1, 2).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 5).setWeight(1, 1));

		this.setLayout(new GridBagLayout());
		this.add(mianPanel, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1));
		this.add(this.panelButton, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.EAST).setWeight(1, 0));
	}
}
