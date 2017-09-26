package com.supermap.desktop.ui.controls.borderPanel;

import com.supermap.data.Datasource;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.event.*;

/**
 * Created by yuanR on 2017/9/26 0026.
 * 结果数据集面板，支持自定义默认结果数据集名称、边框添加CheckBox控件控制面板是否可用
 */
public class PanelResultDataset extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JCheckBox checkBoxUsed;
	private JLabel labelDatasource;
	private DatasourceComboBox comboBoxResultDataDatasource;
	private JLabel labelResultDatasetName;
	private JTextField textFieldResultDatasetName;

	private CompTitledPane compTitledPane;

	private String resultName;
	private Boolean isAddCheckBox;

	/**
	 * 获得面板，获得两种类型的面板
	 *
	 * @return
	 */
	public JPanel getPanel() {
		if (isAddCheckBox) {
			return compTitledPane;
		} else {
			return this;
		}
	}

	/**
	 * 默认构造方法
	 */
	public PanelResultDataset(String defaultResultDatasetName, Boolean isAddChecoBox) {
		this.resultName = defaultResultDatasetName;
		this.isAddCheckBox = isAddChecoBox;
		initComponent();
		initResources();
		resetDatasetName();
		initComboBoxResultDataDatasource();
		setPanelResultDataLayout();
		setComponentName();
		if (this.isAddCheckBox) {
			this.compTitledPane = new CompTitledPane(this.checkBoxUsed, this);
		} else {
			this.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_Label_ResultDataSetting")));
		}
	}


	private void initComponent() {
		this.checkBoxUsed = new JCheckBox();
		this.checkBoxUsed.setSelected(true);
		this.labelResultDatasetName = new JLabel();
		this.labelDatasource = new JLabel();
		this.comboBoxResultDataDatasource = new DatasourceComboBox();
		this.textFieldResultDatasetName = new JTextField();
		//this.textFieldResultDatasetName.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);

		this.checkBoxUsed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setPanelEnable(checkBoxUsed.isSelected());
			}
		});
		this.comboBoxResultDataDatasource.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				resetDatasetName();
			}
		});

		// 给数据集名称输入框添加焦点监听，当文本框为空的时候，给予正确的数据集名称--yuanR 2017.3.3
		this.textFieldResultDatasetName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (StringUtilities.isNullOrEmpty(textFieldResultDatasetName.getText())) {
					resetDatasetName();
				}
			}
		});

	}

	private void initResources() {
		this.checkBoxUsed.setText(ControlsProperties.getString("String_Label_ResultDataSetting"));
		this.labelDatasource.setText(ControlsProperties.getString("String_Label_ResultDatasource"));
		this.labelResultDatasetName.setText(ControlsProperties.getString("String_Label_ResultDataset"));
	}

	private void setComponentName() {
		ComponentUIUtilities.setName(this.comboBoxResultDataDatasource, "PanelResultData_comboBoxResultDataDatasource");
		ComponentUIUtilities.setName(this.textFieldResultDatasetName, "PanelResultData_textFieldResultDataDataset");
	}

	private void setPanelResultDataLayout() {
		GroupLayout panelResultDataLayout = new GroupLayout(this);
		panelResultDataLayout.setAutoCreateGaps(true);
		panelResultDataLayout.setAutoCreateContainerGaps(true);
		this.setLayout(panelResultDataLayout);

		//@formatter:off
		panelResultDataLayout.setHorizontalGroup(panelResultDataLayout.createSequentialGroup()
				.addGroup(panelResultDataLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelDatasource)
						.addComponent(this.labelResultDatasetName))
				.addGroup(panelResultDataLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.comboBoxResultDataDatasource)
						.addComponent(this.textFieldResultDatasetName)));

		panelResultDataLayout.setVerticalGroup(panelResultDataLayout.createSequentialGroup()
				.addGroup(panelResultDataLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelDatasource)
						.addComponent(this.comboBoxResultDataDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(panelResultDataLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelResultDatasetName)
						.addComponent(this.textFieldResultDatasetName, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		//@formatter:on
	}

	private void initComboBoxResultDataDatasource() {

		for (int i = this.comboBoxResultDataDatasource.getItemCount() - 1; i >= 0; i--) {
			if (this.comboBoxResultDataDatasource.getItemAt(i) instanceof Datasource && this.comboBoxResultDataDatasource.getItemAt(i).isReadOnly()) {
				this.comboBoxResultDataDatasource.removeItemAt(i);
			}
		}
	}

	private void resetDatasetName() {
		String name = this.resultName;
		if (this.comboBoxResultDataDatasource.getSelectedDatasource() != null) {
			this.textFieldResultDatasetName.setText(this.comboBoxResultDataDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(name));
		}
	}

	/**
	 * 设置结果数据集名称
	 *
	 * @param resultName
	 */
	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	/**
	 * 设置结果数据集控件是否可用
	 * 2017.9.26 yuanR
	 *
	 * @param isEnable
	 */
	public void setPanelEnable(boolean isEnable) {
		this.comboBoxResultDataDatasource.setEnabled(isEnable);
		this.textFieldResultDatasetName.setEnabled(isEnable);
	}

	/**
	 * 设置整个面板是否可用
	 *
	 * @param isEnable
	 */
	public void setEnable(boolean isEnable) {
		this.checkBoxUsed.setSelected(isEnable);
		this.checkBoxUsed.setEnabled(isEnable);
	}

	public DatasourceComboBox getComboBoxResultDataDatasource() {
		return comboBoxResultDataDatasource;
	}

	public JTextField getTextFieldResultDataDataset() {
		return textFieldResultDatasetName;
	}
}
