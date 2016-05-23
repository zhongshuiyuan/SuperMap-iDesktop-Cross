package com.supermap.desktop.geometryoperation.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilties.ComponentFactory;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilties.StringUtilties;

public class JDialogRegionExtractCenter extends SmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String DEFAULT_DATASET_NAME = "ConvertResult";

	private JLabel labelDesDatasource;
	private JLabel labelMax;
	private JLabel labelMin;
	private JLabel labelNewDataset;
	private SmTextFieldLegit textFieldNewDataset;
	private SmTextFieldLegit textFieldMax;
	private SmTextFieldLegit textFieldMin;
	private DatasourceComboBox comboBoxDatasource;
	private JCheckBox checkBoxRemoveSrc;
	private JButton buttonOK;
	private JButton buttonCancel;

	private double max = 30d;
	private double min = 0d;
	private Datasource desDatasource;
	private String newDatasetName;
	private boolean isRemoveSrc;

	private ItemListener itemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == JDialogRegionExtractCenter.this.comboBoxDatasource) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					comboBoxDesDatasourceSelectedChange();
				}
			} else if (e.getSource() == JDialogRegionExtractCenter.this.checkBoxRemoveSrc) {
				checkBoxRemoveSrcCheckedChange();
			}
		}
	};

	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == JDialogRegionExtractCenter.this.buttonOK) {
				buttonOKClick();
			} else if (e.getSource() == JDialogRegionExtractCenter.this.buttonCancel) {
				buttonCancelClick();
			}
		}
	};

	private DocumentListener textFieldNewDatasetListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldNewDatasetTextChange();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldNewDatasetTextChange();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			textFieldNewDatasetTextChange();
		}
	};

	private DocumentListener textFieldMaxListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldMaxTextChange();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldMaxTextChange();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			textFieldMaxTextChange();
		}
	};

	private DocumentListener textFieldMinListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldMinTextChange();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldMinTextChange();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			textFieldMinTextChange();
		}
	};

	public JDialogRegionExtractCenter() {
		initializeDatas();
		initializeComponents();
		initializeComponentsValue();
		registerEvents();
	}

	public Datasource getDesDatasource() {
		return this.desDatasource;
	}

	public String getNewDatasetName() {
		return this.newDatasetName;
	}

	public boolean isRemoveSrc() {
		return this.isRemoveSrc;
	}

	public double getMax() {
		return this.max;
	}

	public double getMin() {
		return this.min;
	}

	private void initializeDatas() {

		// 目标数据源
		if (Application.getActiveApplication().getActiveDatasources() != null && Application.getActiveApplication().getActiveDatasources().length > 0) {
			this.desDatasource = Application.getActiveApplication().getActiveDatasources()[0];
		}

		// 其他
		this.isRemoveSrc = false;

		if (this.desDatasource != null) {
			this.newDatasetName = this.desDatasource.getDatasets().getAvailableDatasetName(DEFAULT_DATASET_NAME);
		}
	}

	private void initializeComponents() {
		setTitle(MapEditorProperties.getString("String_GeometryOperation_RegionExtractCenter"));
		this.labelDesDatasource = new JLabel(ControlsProperties.getString("String_Label_TargetDatasource"));
		this.labelMax = new JLabel(ControlsProperties.getString(ControlsProperties.Label_Max));
		this.labelMin = new JLabel(ControlsProperties.getString(ControlsProperties.Label_Min));
		this.labelNewDataset = new JLabel(ControlsProperties.getString("String_Label_NewDataset"));
		this.textFieldMax = ComponentFactory.createNumericTextField(30, this.min, Double.MAX_VALUE);
		this.textFieldMin = ComponentFactory.createNumericTextField(0, 0, this.max);
		this.comboBoxDatasource = new DatasourceComboBox();
		this.textFieldNewDataset = new SmTextFieldLegit();
		this.checkBoxRemoveSrc = new JCheckBox(MapEditorProperties.getString("String_RemoveSrcObj"));
		this.buttonOK = new JButton(CommonProperties.getString(CommonProperties.OK));
		this.buttonCancel = new JButton(CommonProperties.getString(CommonProperties.Cancel));

		GroupLayout gl = new GroupLayout(getContentPane());
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);
		getContentPane().setLayout(gl);

		// @formatter:off
		gl.setHorizontalGroup(gl.createParallelGroup(Alignment.LEADING)
				.addGroup(gl.createSequentialGroup()
						.addGroup(gl.createParallelGroup(Alignment.LEADING)
								.addComponent(this.labelDesDatasource)
								.addComponent(this.labelNewDataset)
								.addComponent(this.labelMax)
								.addComponent(this.labelMin)
								.addComponent(this.checkBoxRemoveSrc))
						.addGroup(gl.createParallelGroup(Alignment.LEADING)
								.addComponent(this.comboBoxDatasource, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
								.addComponent(this.textFieldNewDataset, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
								.addComponent(this.textFieldMax, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
								.addComponent(this.textFieldMin, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)))
				.addGroup(gl.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDesDatasource)
						.addComponent(this.comboBoxDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelNewDataset)
						.addComponent(this.textFieldNewDataset, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelMax)
						.addComponent(this.textFieldMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelMin)
						.addComponent(this.textFieldMin, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(this.checkBoxRemoveSrc)
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on

		setSize(450, 220);
		setLocationRelativeTo(null);
	}

	private void initializeComponentsValue() {
		this.comboBoxDatasource.setSelectedDatasource(this.desDatasource);
		this.checkBoxRemoveSrc.setSelected(this.isRemoveSrc);
		this.textFieldNewDataset.setText(this.newDatasetName);
		setComponentsEnabled();
	}

	private void registerEvents() {
		this.comboBoxDatasource.addItemListener(this.itemListener);
		this.checkBoxRemoveSrc.addItemListener(this.itemListener);
		this.buttonOK.addActionListener(this.actionListener);
		this.buttonCancel.addActionListener(this.actionListener);
		this.textFieldNewDataset.getDocument().addDocumentListener(this.textFieldNewDatasetListener);
		this.textFieldMax.getDocument().addDocumentListener(this.textFieldMaxListener);
		this.textFieldMin.getDocument().addDocumentListener(this.textFieldMinListener);
	}

	private void comboBoxDesDatasourceSelectedChange() {
		try {
			this.desDatasource = this.comboBoxDatasource.getSelectedDatasource();

			// 重新初始化文本框
			this.textFieldNewDataset.getDocument().removeDocumentListener(this.textFieldNewDatasetListener);
			this.textFieldNewDataset.setText(this.desDatasource.getDatasets().getAvailableDatasetName(this.newDatasetName));

			setComponentsEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			this.textFieldNewDataset.getDocument().addDocumentListener(this.textFieldNewDatasetListener);
		}
	}

	private void textFieldMaxTextChange() {
		if (this.textFieldMax.isLegitValue(this.textFieldMax.getText())) {
			this.max = Double.valueOf(this.textFieldMax.getText());
		} else if (!StringUtilties.isNullOrEmpty(this.textFieldMax.getText())) {
			Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_GeometryOperation_RegionExtractCenterMaxError"));
		}
	}

	private void textFieldMinTextChange() {
		if (this.textFieldMin.isLegitValue(this.textFieldMin.getText())) {
			this.min = Double.valueOf(this.textFieldMin.getText());
		} else if (!StringUtilties.isNullOrEmpty(this.textFieldMin.getText())) {
			Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_GeometryOperation_RegionExtractCenterMinError"));
		}
	}

	private void textFieldNewDatasetTextChange() {
		if (this.textFieldNewDataset.isLegitValue(this.textFieldNewDataset.getText())) {
			this.newDatasetName = this.textFieldNewDataset.getText();
		}
		setComponentsEnabled();
	}

	private void checkBoxRemoveSrcCheckedChange() {
		this.isRemoveSrc = this.checkBoxRemoveSrc.isSelected();
	}

	private void setComponentsEnabled() {
		this.textFieldNewDataset.setEnabled(this.desDatasource != null);

		// @formatter:off
		this.buttonOK.setEnabled(this.desDatasource != null 
				&& this.textFieldNewDataset.isLegitValue(this.textFieldNewDataset.getText())
				&& this.textFieldMax.isLegitValue(this.textFieldMax.getText())
				&& this.textFieldMin.isLegitValue(this.textFieldMin.getText()));
		// @formatter:on
	}

	private void buttonOKClick() {
		setDialogResult(DialogResult.OK);
		setVisible(false);
	}

	private void buttonCancelClick() {
		setDialogResult(DialogResult.CANCEL);
		setVisible(false);
	}
}
