package com.supermap.desktop.ui.controls.prjcoordsys;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.ProviderLabel.WarningOrHelpProvider;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.borderPanel.PanelButton;
import com.supermap.desktop.ui.controls.prjcoordsys.prjTransformPanels.PanelCoordSysInfo;
import com.supermap.desktop.ui.controls.prjcoordsys.prjTransformPanels.PanelReferSysTransSettings;
import com.supermap.desktop.utilities.DatasourceUtilities;
import com.supermap.desktop.utilities.PrjCoordSysUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by yuanR on 2017/10/10 0010.
 * 批量投影转换主面板
 */
public class JDialogBatchPrjTransform extends SmDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// 源数据面板块
	private JPanel panelSourceData;
	private JLabel labelSourceDatasource;
	private DatasourceComboBox sourceDatasource;
	private PanelCoordSysInfo sourcePanelCoordSysInfo;

	// 目标数据面板块
	private JPanel panelTargetData;
	private WarningOrHelpProvider labelTargetDatasource;
	private DatasourceComboBox targetDatasource;
	private PanelCoordSysInfo targetPanelCoordSysInfo;
	// 参照系转换设置面板块
	private PanelReferSysTransSettings panelReferSysTransSettings;
	// 数据集列表

	private JPanel panelDatasetList;
	private JTable table;

	// 确定取消按钮；
	private PanelButton panelButton = new PanelButton();


	public CoordSysTransMethod getMethod() {
		return this.panelReferSysTransSettings.getMethod();
	}

	public CoordSysTransParameter getParameter() {
		return this.panelReferSysTransSettings.getParameter();
	}


	/**
	 * 数据源、数据及改变监听
	 */
	private ItemListener datasourceChangedListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource().equals(sourceDatasource) && e.getStateChange() == ItemEvent.SELECTED) {
				sourcePanelCoordSysInfo.setCoordInfo(PrjCoordSysUtilities.getDescription(sourceDatasource.getSelectedDatasource().getPrjCoordSys()));
			} else if (e.getSource().equals(targetDatasource) && e.getStateChange() == ItemEvent.SELECTED) {
				targetPanelCoordSysInfo.setCoordInfo(PrjCoordSysUtilities.getDescription(targetDatasource.getSelectedDatasource().getPrjCoordSys()));
			}
		}
	};

	/**
	 *
	 */
	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(panelButton.getButtonOk()) && Translator()) {
				dialogResult = DialogResult.OK;
			} else {
				dialogResult = DialogResult.CANCEL;
			}
			JDialogBatchPrjTransform.this.dispose();
		}
	};

	/**
	 * 投影转换功能实现
	 */
	private Boolean Translator() {
		//Boolean result;
		//try {
		//	// 进行批量投影转换
		//	//todo 批量投影转换的具体实现
		//	Dataset targetDataset = CoordSysTranslator.convert(getSourceDataset(), getTargetPrj(), getSelectedResultDatasource(), getResultDatasetName(), getParameter(), getMethod());
		//	result = targetDataset != null;
		//	if (result) {
		//		Application
		//				.getActiveApplication()
		//				.getOutput()
		//				.output(MessageFormat.format(ControlsProperties.getString("String_CoordSysTrans_RasterSuccess"),
		//						getSourceDataset().getDatasource().getAlias(), getSourceDataset().getName(), getSelectedResultDatasource().getAlias(), getResultDatasetName()));
		//	} else {
		//		Application
		//				.getActiveApplication()
		//				.getOutput()
		//				.output(MessageFormat.format(ControlsProperties.getString("String_CoordSysTrans_Failed"),
		//						getSourceDataset().getDatasource().getAlias(), getSourceDataset().getName()));
		//	}
		//	// 这种转换方式主要针对非矢量数据，转换之后会生成新的数据集，但是树的显示状态很诡异，这里对目标数据源的节点进行一次刷新
		//	WorkspaceTreeManagerUIUtilities.refreshNode(getSelectedResultDatasource());
		//
		//} catch (Exception e) {
		//	result = false;
		//	Application.getActiveApplication().getOutput().output(e);
		//} finally {
		//	removeListener();
		//	if (getParameter() != null) {
		//		getParameter().dispose();
		//	}
		//}
		//return result;
		return false;
	}

	public JDialogBatchPrjTransform() {
		initializeComponents();
		initializeResources();
		initializeLayout();
		initStates();
		initListener();

		setSize(800, 600);
		setLocationRelativeTo(null);
	}

	private void removeListener() {
		this.sourceDatasource.removeItemListener(this.datasourceChangedListener);
		this.targetDatasource.removeItemListener(this.datasourceChangedListener);

		this.panelButton.getButtonOk().removeActionListener(this.actionListener);
		this.panelButton.getButtonCancel().removeActionListener(this.actionListener);
	}

	private void initListener() {
		removeListener();
		this.sourceDatasource.addItemListener(this.datasourceChangedListener);
		this.targetDatasource.addItemListener(this.datasourceChangedListener);

		this.panelButton.getButtonOk().addActionListener(this.actionListener);
		this.panelButton.getButtonCancel().addActionListener(this.actionListener);
	}

	private void initStates() {
		// 设置源数据的数据源选择以及投影信息
		Datasource ActiveDatasource = DatasourceUtilities.getDefaultResultDatasource();
		this.sourceDatasource.setSelectedDatasource(ActiveDatasource);
		this.sourcePanelCoordSysInfo.setCoordInfo(PrjCoordSysUtilities.getDescription(sourceDatasource.getSelectedDatasource().getPrjCoordSys()));

		// 设置目标数据数据源选择以及投影信息
		// 目标数据源不支持平面无投影数据源
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		for (int i = 0; i < datasources.getCount(); i++) {
			if (datasources.get(i).getPrjCoordSys().getType().equals(PrjCoordSysType.PCS_NON_EARTH)) {
				this.targetDatasource.removeDataSource(datasources.get(i));
			}
		}
		if (this.targetDatasource.getItemCount() > 0) {
			for (int i = 0; i < this.targetDatasource.getItemCount(); i++) {
				if (!this.targetDatasource.getDatasourceAt(i).equals(sourceDatasource.getSelectedDatasource())) {
					this.targetDatasource.setSelectedDatasource(this.targetDatasource.getDatasourceAt(i));
					this.targetPanelCoordSysInfo.setCoordInfo(PrjCoordSysUtilities.getDescription(targetDatasource.getSelectedDatasource().getPrjCoordSys()));
					break;
				} else {
					this.targetDatasource.setSelectedDatasource(this.targetDatasource.getDatasourceAt(i));
					this.targetPanelCoordSysInfo.setCoordInfo(PrjCoordSysUtilities.getDescription(targetDatasource.getSelectedDatasource().getPrjCoordSys()));
				}
			}
		}

		// 初始化JTable显示
		if (null == targetDatasource.getSelectedDatasource()) {
			this.targetPanelCoordSysInfo.setCoordInfo("");
			setPanelEnabled(false);
		}
	}


	private void initializeComponents() {

		// 源数据面板块
		this.panelSourceData = new JPanel();
		this.labelSourceDatasource = new JLabel("sourceDatasource");
		this.sourceDatasource = new DatasourceComboBox();
		this.sourcePanelCoordSysInfo = new PanelCoordSysInfo("");

		// 目标数据面板块
		this.panelTargetData = new JPanel();
		this.labelTargetDatasource = new WarningOrHelpProvider(ControlsProperties.getString("String_TipText_NonsupportNullProjectionDatasource"), false);
		this.targetDatasource = new DatasourceComboBox();
		this.targetPanelCoordSysInfo = new PanelCoordSysInfo("");
		// 参照系转换设置面板块
		this.panelReferSysTransSettings = new PanelReferSysTransSettings();
		//数据集列表
		this.panelDatasetList = new JPanel();
		this.table = new JTable();
	}

	private void initializeResources() {
		this.setTitle(ControlsProperties.getString("String_Title_BatchPrjTransform"));
		this.panelSourceData.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_GroupBox_SourceDataset")));
		this.panelTargetData.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_GroupBox_TargetDataset")));

		this.labelSourceDatasource.setText(ControlsProperties.getString("String_Label_Datasource"));
		this.labelTargetDatasource.setText(ControlsProperties.getString("String_Label_Datasource"));

		this.panelReferSysTransSettings.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_GroupBox_CoordSysTranslatorSetting")));
		this.panelDatasetList.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_GroupBox_DatasetsList")));

	}

	private void initializeLayout() {
		// 原数据面板布局
		GroupLayout sourceDataPanelLayout = new GroupLayout(panelSourceData);
		sourceDataPanelLayout.setAutoCreateContainerGaps(true);
		sourceDataPanelLayout.setAutoCreateGaps(true);
		panelSourceData.setLayout(sourceDataPanelLayout);
		// @formatter:off
		sourceDataPanelLayout.setHorizontalGroup(sourceDataPanelLayout.createParallelGroup()
				.addGroup(sourceDataPanelLayout.createSequentialGroup()
						.addComponent(this.labelSourceDatasource)
						.addComponent(this.sourceDatasource))
				.addGroup(sourceDataPanelLayout.createSequentialGroup()
						.addComponent(this.sourcePanelCoordSysInfo)));

		sourceDataPanelLayout.setVerticalGroup(sourceDataPanelLayout.createSequentialGroup()
				.addGroup(sourceDataPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelSourceDatasource)
						.addComponent(this.sourceDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(sourceDataPanelLayout.createSequentialGroup()
						.addComponent(this.sourcePanelCoordSysInfo)));
		// @formatter:on


		GroupLayout targetDataPanelLayout = new GroupLayout(panelTargetData);
		targetDataPanelLayout.setAutoCreateContainerGaps(true);
		targetDataPanelLayout.setAutoCreateGaps(true);
		panelTargetData.setLayout(targetDataPanelLayout);
		// @formatter:off
		targetDataPanelLayout.setHorizontalGroup(targetDataPanelLayout.createParallelGroup()
				.addGroup(targetDataPanelLayout.createSequentialGroup()
						.addComponent(this.labelTargetDatasource)
						.addComponent(this.targetDatasource))
				.addGroup(targetDataPanelLayout.createSequentialGroup()
						.addComponent(this.targetPanelCoordSysInfo)));

		targetDataPanelLayout.setVerticalGroup(targetDataPanelLayout.createSequentialGroup()
				.addGroup(targetDataPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelTargetDatasource)
						.addComponent(this.targetDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(targetDataPanelLayout.createSequentialGroup()
						.addComponent(this.targetPanelCoordSysInfo)));
		// @formatter:on


		// 主面板布局
		JPanel mianPanel = new JPanel();
		mianPanel.setLayout(new GridBagLayout());
		mianPanel.add(this.panelSourceData, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(10, 5, 0, 0).setWeight(0, 1));
		mianPanel.add(this.panelTargetData, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setWeight(0, 1));
		mianPanel.add(this.panelReferSysTransSettings, new GridBagConstraintsHelper(0, 2, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setWeight(0, 0));
		mianPanel.add(this.panelDatasetList, new GridBagConstraintsHelper(1, 0, 3, 3).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(10, 0, 0, 5).setWeight(1, 1));

		this.setLayout(new GridBagLayout());
		this.add(mianPanel, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1));
		this.add(this.panelButton, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.EAST).setWeight(1, 0));
	}

	/**
	 * 设置面板是否可用
	 *
	 * @param isEnable
	 */
	public void setPanelEnabled(Boolean isEnable) {
		// 参照系转换设置面板块
		this.panelReferSysTransSettings.setPanelEnabled(isEnable);
		// 确定取消按钮；
		this.panelButton.getButtonOk().setEnabled(isEnable);
	}
}
