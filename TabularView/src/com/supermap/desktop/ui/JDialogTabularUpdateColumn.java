package com.supermap.desktop.ui;

import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.tabularview.TabularViewProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.utilities.*;
import com.supermap.desktop.utilties.Convert;
import com.supermap.desktop.utilties.UpdateColumnUtilties;

/**
 * 更新列主界面
 * 
 * @author xie 2016.6.23
 *
 */
public class JDialogTabularUpdateColumn extends SmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel labelUpdataField;// 待更新字段
	private JComboBox<String> comboBoxUpdateField;
	private JLabel labelFieldType;// 字段类型
	private JLabel labelUpdateScope;// 更新范围
	private JRadioButton radioButtonUpdateColumn;// 整列更新
	private JRadioButton checkBoxUpdateSelect;// 更新选中记录
	private JLabel labelSourceOfField;// 数值来源
	private JComboBox<String> comboBoxSourceOfField;
	private JCheckBox checkBoxInversion;// 反向
	private JLabel labelOperationField;// 第一运算字段/运算字段
	private JLabel labelOperationFieldType;// 第一运算字段类型
	private JComboBox<String> comboBoxOperationField;
	private JLabel labelMethod;// 运算方式
	private JComboBox<String> comboBoxMethod;
	private JTextField textFieldX;
	private JTextField textFieldY;
	private JLabel labelSecondField;// 第二运算字段/用来更新的值/运算因子
	private JLabel labelSecondFieldType;// 第二运算字段类型
	private JTextField textFieldSecondField;
	private JComboBox<String> comboBoxSecondField;
	private JLabel labelOperationEQ;// 运算方程式
	private JTextField textFieldOperationEQ;
	private JLabel labelEQTip;// 运算方式提示

	private boolean isExpressionSelect;// 前一个界面是否为表达式界面

	private JButton buttonApply;
	private JButton buttonClose;
	private IFormTabular tabular;

	private Map<Integer, FieldInfo> fieldInfoMap = new HashMap<Integer, FieldInfo>();// 字段信息MAP，用于存放可更新的列
	private JTextArea textAreaExpression;// 存放表达式
	private JButton buttonExpression; // 表达式调用入口

	private final String[] integerExpressions = { "Abs", "Sqrt", "Ln", "Log", "Int", "ObjectCenterX", "ObjectCenterY", "ObjectLeft", "ObjectRight",
			"ObjectTop", "ObjectBottom", "ObjectWidth", "ObjectHeight" };
	private final String[] textExpressions = { "Left", "Right", "Mid", "UCase", "LCase", "Trim", "TrimEnd", "TrimStart", "ObjectCenterX", "ObjectCenterY",
			"ObjectLeft", "ObjectRight", "ObjectTop", "ObjectBottom", "ObjectWidth", "ObjectHeight", "LRemove", "RRemove", "Replace" };
	private final String[] dateTimeExpressions = { "AddDays", "AddHours", "AddMilliseconds", "AddSeconds", "AddMinutes", "AddMonths", "AddYears", "Date", "Now" };

	private final String[] dateMethodExpresssions = { "DaysInMonth", "Second", "Minute", "Hour", "Day", "Month", "Year", "DayOfYear", "DayOfWeek" };

	private JPanel contentPanel;

	private ItemListener updateFieldListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			updateFieldChanged(e);
		}
	};
	private ActionListener checkBoxListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			checkBoxChanged(e);
		}
	};
	private ItemListener comboBoxSourceOfFieldListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			sourceOfFieldChanged(e);
		}

	};
	private ItemListener comboBoxOperationFieldListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			operationFieldChanged(e);
		}

	};
	private ItemListener comboBoxMethodListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			methodChanged(e);
		}

	};
	private DocumentListener textFieldXChangedListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldXChanged();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldXChanged();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			textFieldXChanged();
		}

	};
	private DocumentListener textFieldYChangedListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldYChanged();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldYChanged();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			textFieldYChanged();
		}

	};
	private DocumentListener textFieldSecondFieldListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			secondFieldChanged();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			secondFieldChanged();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			secondFieldChanged();
		}
	};
	private ActionListener expressionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			getSqlExpression();
		}
	};
	private ItemListener comboBoxSecondFieldListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED && null != textFieldOperationEQ) {
				if (fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType().equals(FieldType.BOOLEAN) && !comboBoxOperationField.isEnabled()) {
					textFieldOperationEQ.setText(comboBoxSecondField.getSelectedItem().toString());
				} else {
					textFieldOperationEQ.setText(comboBoxOperationField.getSelectedItem().toString() + comboBoxMethod.getSelectedItem().toString()
							+ comboBoxSecondField.getSelectedItem().toString());
				}
				if (null != tabular.getRecordset().getFieldInfos().get(comboBoxSecondField.getSelectedItem().toString())) {
					labelSecondFieldType.setText(FieldTypeUtilities.getFieldTypeName(tabular.getRecordset().getFieldInfos()
							.get(comboBoxSecondField.getSelectedItem().toString()).getType()));
				}
			}
		}
	};
	private ActionListener buttonApplyListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			buttonApplyClicked();
		}

	};
	private ActionListener buttonCloseListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			disposeDialog();
		}

	};

	public JDialogTabularUpdateColumn(IFormTabular tabular) {
		super();
		this.tabular = tabular;
		setTitle(TabularViewProperties.getString("String_FormTabularUpdataColumn_Title") + tabular.getText());
		setSize(500, 300);
		setLocationRelativeTo(null);
		initComponents();
		initResources();
		registEvents();
	}

	private void initComponents() {
		this.contentPanel = (JPanel) this.getContentPane();
		this.contentPanel.removeAll();
		this.contentPanel.setLayout(new GridBagLayout());
		initComboBoxUpdateField();
		this.labelUpdateScope = new JLabel();
		this.radioButtonUpdateColumn = new JRadioButton();
		this.checkBoxUpdateSelect = new JRadioButton();
		boolean updateColumn = tabular.getjTableTabular().getSelectedRowCount() > 0
				&& tabular.getjTableTabular().getSelectedRowCount() == tabular.getRowCount() ? true : false;
		this.radioButtonUpdateColumn.setSelected(updateColumn);
		this.checkBoxUpdateSelect.setSelected(!updateColumn);
		initComboBoxOperationField();
		initComobBoxMethod();
		initTextFieldOperationEQ();
		initLayout();
		if (fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType().equals(FieldType.BOOLEAN)) {
			replaceSecondField(comboBoxSecondField, textFieldSecondField);
			setComboBoxSecondFieldItems(FieldType.BOOLEAN);
			this.textFieldOperationEQ.setText("True");
		}
	}

	private void initLayout() {
		this.contentPanel.removeAll();
		//@formatter:off
		this.contentPanel.add(this.labelUpdataField,       new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(10,10,5,0));
		this.contentPanel.add(this.comboBoxUpdateField,    new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(60, 1).setInsets(10,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.labelFieldType,         new GridBagConstraintsHelper(5, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(10,10,5,10));
		this.contentPanel.add(this.labelUpdateScope,       new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0,10,5,0));
		this.contentPanel.add(this.radioButtonUpdateColumn,   new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0,10,5,0));
		this.contentPanel.add(this.checkBoxUpdateSelect,   new GridBagConstraintsHelper(3, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0,10,5,10));
		this.contentPanel.add(this.labelSourceOfField,     new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0,10,5,0));
		this.contentPanel.add(this.comboBoxSourceOfField,  new GridBagConstraintsHelper(1, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(55, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.checkBoxInversion,      new GridBagConstraintsHelper(4, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(5, 1).setInsets(0,10,5,10));
		addContentPanel();
		this.contentPanel.add(initButtonPanel(),           new GridBagConstraintsHelper(0, 8, 6, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
		//@formatter:on
	}

	private void addContentPanel() {
		//@formatter:off
		this.contentPanel.add(this.labelOperationField,    new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0,10,5,0));
		this.contentPanel.add(this.comboBoxOperationField, new GridBagConstraintsHelper(1, 3, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(60, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.labelOperationFieldType,new GridBagConstraintsHelper(5, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0,10,5,10));
		this.contentPanel.add(this.labelMethod,            new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0,10,5,0));
		this.contentPanel.add(this.comboBoxMethod,         new GridBagConstraintsHelper(1, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(40, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.textFieldX,             new GridBagConstraintsHelper(3, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.textFieldY,             new GridBagConstraintsHelper(4, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.labelSecondField,       new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.textFieldSecondField,   new GridBagConstraintsHelper(1, 5, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.labelSecondFieldType,   new GridBagConstraintsHelper(5, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,10));
		this.contentPanel.add(this.labelOperationEQ,       new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.textFieldOperationEQ,   new GridBagConstraintsHelper(1, 6, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.labelEQTip,             new GridBagConstraintsHelper(1, 7, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0,10,5,0).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	private void removeContentPanel() {
		this.textAreaExpression = new JTextArea();
		this.buttonExpression = new JButton(ControlsProperties.getString("String_SQLExpression"));
		this.buttonExpression.addActionListener(expressionListener);
		this.contentPanel.remove(labelOperationField);
		this.contentPanel.remove(comboBoxOperationField);
		this.contentPanel.remove(labelOperationFieldType);
		this.contentPanel.remove(labelMethod);
		this.contentPanel.remove(comboBoxMethod);
		this.contentPanel.remove(textFieldX);
		this.contentPanel.remove(textFieldY);
		this.contentPanel.remove(labelSecondField);
		this.contentPanel.remove(textFieldSecondField);
		this.contentPanel.remove(comboBoxSecondField);
		this.contentPanel.remove(labelSecondFieldType);
		this.contentPanel.remove(labelOperationEQ);
		this.contentPanel.remove(textFieldOperationEQ);
		this.contentPanel.remove(labelEQTip);
		//@formatter:off
		this.contentPanel.add(this.labelOperationField, new GridBagConstraintsHelper(0, 3, 1, 4).setAnchor(GridBagConstraints.WEST).setWeight(20, 1).setInsets(0, 10, 5, 0));
		this.contentPanel.add(this.textAreaExpression,  new GridBagConstraintsHelper(1, 3, 4, 4).setAnchor(GridBagConstraints.WEST).setWeight(60, 1).setInsets(5, 10, 5, 0).setIpad(0, 80).setFill(GridBagConstraints.HORIZONTAL));
		this.contentPanel.add(this.buttonExpression,    new GridBagConstraintsHelper(4, 7, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(20, 1).setInsets(0, 10, 5, 0));
		this.labelOperationField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelExpression"));
		//@formatter:on
	}

	private Component initButtonPanel() {
		JPanel panelButton = new JPanel();
		this.buttonApply = ComponentFactory.createButtonApply();
		this.buttonClose = ComponentFactory.createButtonClose();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.buttonApply, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(5, 0, 10, 10));
		panelButton.add(this.buttonClose, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(5, 0, 10, 10));
		return panelButton;
	}

	private void initTextFieldOperationEQ() {
		this.labelOperationEQ = new JLabel();
		this.textFieldOperationEQ = new JTextField();
		initTextFieldOperationEQText(tabular.getRecordset().getFieldInfos().get(0).getType());
		this.textFieldOperationEQ.setEnabled(false);
		this.labelEQTip = new JLabel();
	}

	private void setComboBoxSecondFieldItems(FieldType fieldType) {
		if (fieldType.equals(FieldType.BOOLEAN)) {
			this.comboBoxSecondField.removeAllItems();
			this.comboBoxSecondField.addItem("True");
			this.comboBoxSecondField.addItem("False");
		} else {
			this.comboBoxSecondField.removeAllItems();
			for (int i = 0; i < tabular.getRecordset().getFieldCount(); i++) {
				comboBoxSecondField.addItem(tabular.getRecordset().getFieldInfos().get(i).getName());
			}
		}
	}

	private void initTextFieldOperationEQText(FieldType type) {
		if (FieldTypeUtilities.isNumber(type)) {
			this.textFieldOperationEQ.setText("0");
		} else {
			this.textFieldOperationEQ.setText("");
		}
	}

	private void initComobBoxMethod() {
		this.labelMethod = new JLabel();
		this.comboBoxMethod = new JComboBox<String>();
		this.comboBoxMethod.setEditable(true);
		((JTextField) this.comboBoxMethod.getEditor().getEditorComponent()).setEditable(false);
		this.textFieldX = new JTextField();
		this.textFieldY = new JTextField();
		resetMethodItems();
		this.comboBoxMethod.setEnabled(false);
		this.textFieldX.setEnabled(false);
		this.textFieldY.setEnabled(false);
		this.comboBoxMethod.setPreferredSize(new Dimension(160, 23));
	}

	private void initComboBoxOperationField() {
		// 初始化运算字段
		this.labelOperationField = new JLabel();
		this.comboBoxOperationField = new JComboBox<String>();
		this.comboBoxOperationField.setEditable(true);
		((JTextField) this.comboBoxOperationField.getEditor().getEditorComponent()).setEditable(false);
		this.labelOperationFieldType = new JLabel();
		this.comboBoxOperationField.removeAllItems();
		for (int i = 0; i < tabular.getRecordset().getFieldCount(); i++) {
			this.comboBoxOperationField.addItem(tabular.getRecordset().getFieldInfos().get(i).getName());
		}
		this.comboBoxOperationField.setEnabled(false);
		this.labelOperationField.setPreferredSize(new Dimension(80, 23));
	}

	private void initComboBoxUpdateField() {
		// 初始化待更新字段下拉列表
		this.labelUpdataField = new JLabel();
		this.comboBoxUpdateField = new JComboBox<String>();
		this.comboBoxUpdateField.setEditable(true);
		// 设置comboboxOperationField的样式
		((JTextField) this.comboBoxUpdateField.getEditor().getEditorComponent()).setEditable(false);
		this.labelFieldType = new JLabel();
		this.labelSecondField = new JLabel();
		this.textFieldSecondField = new JTextField();
		this.labelSecondFieldType = new JLabel();
		this.labelSecondField.setPreferredSize(new Dimension(100, 23));
		this.comboBoxSecondField = new JComboBox<String>();
		this.comboBoxSecondField.setEditable(true);
		((JTextField) this.comboBoxSecondField.getEditor().getEditorComponent()).setEditable(false);
		this.comboBoxUpdateField.removeAllItems();
		int count = 0;
		String defualtSelectField = "";
		for (int i = 0; i < tabular.getRecordset().getFieldCount(); i++) {
			if (!tabular.getRecordset().getFieldInfos().get(i).isSystemField()) {
				this.comboBoxUpdateField.addItem(tabular.getRecordset().getFieldInfos().get(i).getName());
				fieldInfoMap.put(count, tabular.getRecordset().getFieldInfos().get(i));
				if (tabular.getjTableTabular().getSelectedColumn() >= 0) {
					defualtSelectField = tabular.getRecordset().getFieldInfos().get(tabular.getjTableTabular().getSelectedColumn()).getName();
				}
				count++;
			}
		}
		boolean hasItem = false;
		Iterator<FieldInfo> values = fieldInfoMap.values().iterator();
		while (values.hasNext()) {
			if (values.next().getName().equals(defualtSelectField)) {
				hasItem = true;
				break;
			}
		}
		FieldType defualtType = fieldInfoMap.get(0).getType();
		if (!StringUtilities.isNullOrEmptyString(defualtSelectField) && hasItem) {
			// 设置默认选中行
			this.comboBoxUpdateField.setSelectedItem(defualtSelectField);
			defualtType = fieldInfoMap.get(this.comboBoxUpdateField.getSelectedIndex()).getType();
			this.labelFieldType.setText(FieldTypeUtilities.getFieldTypeName(defualtType));
		} else {
			this.labelFieldType.setText(FieldTypeUtilities.getFieldTypeName(defualtType));
		}
		this.labelFieldType.setPreferredSize(new Dimension(60, 23));
		// 初始化数值来源下拉列表
		this.labelSourceOfField = new JLabel();
		this.comboBoxSourceOfField = new JComboBox<String>();
		this.comboBoxSourceOfField.setEditable(true);
		((JTextField) this.comboBoxSourceOfField.getEditor().getEditorComponent()).setEditable(false);
		this.checkBoxInversion = new JCheckBox();
		this.checkBoxInversion.setEnabled(false);
		setComboBoxSourceOfFieldItems(defualtType);
	}

	private void setComboBoxSourceOfFieldItems(FieldType defualtType) {
		if (defualtType.equals(FieldType.BOOLEAN)) {
			comboBoxSourceOfField.removeAllItems();
			comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeSetValue"));
			comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeOneField"));
			comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeTwoFields"));
			comboBoxSourceOfField.addItem(CoreProperties.getString("String_ThemeGraphItemExpressionPicker_ButtonExpression"));
		} else if (defualtType.equals(FieldType.DATETIME)) {
			comboBoxSourceOfField.removeAllItems();
			comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeSetValue"));
			comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeMath"));
			comboBoxSourceOfField.addItem(CoreProperties.getString("String_ThemeGraphItemExpressionPicker_ButtonExpression"));
		} else if (defualtType.equals(FieldType.LONGBINARY)) {
			comboBoxSourceOfField.removeAllItems();
			comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeSetValue"));
		} else {
			comboBoxSourceOfField.removeAllItems();
			comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeSetValue"));
			comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeOneField"));
			comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeTwoFields"));
			comboBoxSourceOfField.addItem(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeMath"));
			comboBoxSourceOfField.addItem(CoreProperties.getString("String_ThemeGraphItemExpressionPicker_ButtonExpression"));
		}
	}

	private void initResources() {
		this.labelUpdataField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelUpdataField"));
		this.labelUpdateScope.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelUpdataBounds"));
		this.radioButtonUpdateColumn.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_radioButtonUpdateTotalColumn"));
		this.checkBoxUpdateSelect.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_radioButtonUpdateSelectedRows"));
		this.labelSourceOfField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelUpdataMode"));
		this.checkBoxInversion.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_checkBoxReverse"));
		this.labelOperationField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelField"));
		this.labelMethod.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelOperatorType"));
		this.labelSecondField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelSecondField"));
		this.labelOperationEQ.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelExpression"));
	}

	private void registEvents() {
		// 为控件注册事件
		removeEvents();
		this.comboBoxUpdateField.addItemListener(this.updateFieldListener);
		this.comboBoxSourceOfField.addItemListener(this.comboBoxSourceOfFieldListener);
		this.radioButtonUpdateColumn.addActionListener(this.checkBoxListener);
		this.checkBoxUpdateSelect.addActionListener(this.checkBoxListener);
		this.checkBoxInversion.addActionListener(this.checkBoxListener);
		this.comboBoxOperationField.addItemListener(this.comboBoxOperationFieldListener);
		this.comboBoxMethod.addItemListener(this.comboBoxMethodListener);
		this.textFieldX.getDocument().addDocumentListener(this.textFieldXChangedListener);
		this.textFieldY.getDocument().addDocumentListener(this.textFieldYChangedListener);
		this.textFieldSecondField.getDocument().addDocumentListener(this.textFieldSecondFieldListener);
		this.comboBoxSecondField.addItemListener(this.comboBoxSecondFieldListener);
		this.buttonApply.addActionListener(this.buttonApplyListener);
		this.buttonClose.addActionListener(this.buttonCloseListener);
	}

	private void disposeDialog() {
		removeEvents();
		fieldInfoMap.clear();
		fieldInfoMap = null;
		dispose();
	}

	private void removeEvents() {
		this.comboBoxUpdateField.removeItemListener(this.updateFieldListener);
		this.comboBoxSourceOfField.removeItemListener(this.comboBoxSourceOfFieldListener);
		this.radioButtonUpdateColumn.removeActionListener(this.checkBoxListener);
		this.checkBoxUpdateSelect.removeActionListener(this.checkBoxListener);
		this.checkBoxInversion.removeActionListener(this.checkBoxListener);
		this.comboBoxOperationField.removeItemListener(this.comboBoxOperationFieldListener);
		this.comboBoxMethod.removeItemListener(this.comboBoxMethodListener);
		this.textFieldX.getDocument().removeDocumentListener(this.textFieldXChangedListener);
		this.textFieldY.getDocument().removeDocumentListener(this.textFieldYChangedListener);
		this.textFieldSecondField.getDocument().removeDocumentListener(this.textFieldSecondFieldListener);
		if (null != buttonExpression) {
			this.buttonExpression.removeActionListener(this.expressionListener);
		}
		this.comboBoxSecondField.removeItemListener(this.comboBoxSecondFieldListener);
		this.buttonApply.removeActionListener(this.buttonApplyListener);
		this.buttonClose.removeActionListener(this.buttonCloseListener);
	}

	protected void setExpression() {
		removeContentPanel();
		textAreaExpression.setText(textFieldOperationEQ.getText());
		getSqlExpression();
		contentPanel.updateUI();
		checkBoxInversion.setEnabled(false);
		isExpressionSelect = true;
	}

	private void getSqlExpression() {
		SQLExpressionDialog dialog = new SQLExpressionDialog();
		DialogResult result = dialog.showDialog(textAreaExpression.getText(), tabular.getRecordset().getDataset());
		if (result == DialogResult.OK) {
			String filter = dialog.getQueryParameter().getAttributeFilter();
			if (!StringUtilities.isNullOrEmpty(filter)) {
				textAreaExpression.setText(filter);
			}
		}
	}

	private void resetMethodItems() {
		if (comboBoxUpdateField.getSelectedIndex() >= 0) {
			FieldType tempType = fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType();
			if (FieldTypeUtilities.isNumber(tempType)) {
				comboBoxMethod.removeAllItems();
				comboBoxMethod.addItem("+");
				comboBoxMethod.addItem("-");
				comboBoxMethod.addItem("*");
				comboBoxMethod.addItem("/");
				comboBoxMethod.addItem("%");
			} else if (FieldTypeUtilities.isString(tempType) || tempType.equals(FieldType.CHAR)) {
				comboBoxMethod.removeAllItems();
				comboBoxMethod.addItem("+");
			} else if (tempType.equals(FieldType.BOOLEAN)) {
				comboBoxMethod.removeAllItems();
				comboBoxMethod.addItem("+");
				comboBoxMethod.addItem("-");
				comboBoxMethod.addItem(">");
				comboBoxMethod.addItem(">=");
				comboBoxMethod.addItem("<");
				comboBoxMethod.addItem("<=");
				comboBoxMethod.addItem("==");
				comboBoxMethod.addItem("!=");
			}
		}
	}

	private void setFunctionInfo() {
		// 设置函数运算界面
		addNeededComponents();
		checkBoxInversion.setEnabled(false);
		labelOperationField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelField"));
		comboBoxOperationField.setEnabled(true);
		labelOperationFieldType.setText(FieldTypeUtilities.getFieldTypeName(tabular.getRecordset().getFieldInfos()
				.get(comboBoxOperationField.getSelectedIndex()).getType()));
		labelMethod.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelOperatorFunction"));
		comboBoxMethod.setEnabled(true);
		resetMethodItemsForMathModel();
		labelSecondField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelSecondField"));
		labelSecondField.setEnabled(false);
		replaceSecondField(textFieldSecondField, comboBoxSecondField);
		textFieldSecondField.setEnabled(false);
		labelOperationEQ.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelExpression"));
		labelSecondFieldType.setText("");
	}

	private void resetMethodItemsForMathModel() {
		FieldType updateFieldType = fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType();
		FieldType operationFieldType = tabular.getRecordset().getFieldInfos().get(comboBoxOperationField.getSelectedItem().toString()).getType();
		comboBoxMethod.removeAllItems();
		if (FieldTypeUtilities.isNumber(updateFieldType)) {
			if (operationFieldType.equals(FieldType.DATETIME)) {
				for (String dateMethodExpression : dateMethodExpresssions) {
					comboBoxMethod.addItem(dateMethodExpression);
				}
			} else {
				for (String integerExpression : integerExpressions) {
					comboBoxMethod.addItem(integerExpression);
				}
			}
		}
		if (FieldTypeUtilities.isString(updateFieldType) || updateFieldType.equals(FieldType.CHAR)) {
			if (operationFieldType.equals(FieldType.DATETIME)) {
				for (String dateMethodExpression : dateMethodExpresssions) {
					comboBoxMethod.addItem(dateMethodExpression);
				}
				for (String textExpression : textExpressions) {
					comboBoxMethod.addItem(textExpression);
				}
			} else {
				for (String textExpression : textExpressions) {
					comboBoxMethod.addItem(textExpression);
				}
			}
		}
		if (updateFieldType.equals(FieldType.DATETIME)) {
			for (String dateTimeExpression : dateTimeExpressions) {
				comboBoxMethod.addItem(dateTimeExpression);
			}
			textFieldX.setEnabled(true);
			textFieldX.setText("0");
			textFieldOperationEQ.setText(comboBoxOperationField.getSelectedItem().toString() + "." + dateTimeExpressions[0] + "(0)");
			labelEQTip.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_AddDays"));
		}
	}

	private void setDoubleFieldInfo() {
		// 双字段运算时的界面设置
		addNeededComponents();
		checkBoxInversion.setEnabled(true);
		labelOperationField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelFirstField"));
		comboBoxOperationField.setEnabled(true);
		labelOperationFieldType.setText(FieldTypeUtilities.getFieldTypeName(tabular.getRecordset().getFieldInfos()
				.get(comboBoxOperationField.getSelectedIndex()).getType()));
		labelMethod.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelOperatorType"));
		updateComboboxMethod();
		comboBoxMethod.setEnabled(true);
		labelSecondField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelSecondField1"));
		replaceSecondField(comboBoxSecondField, textFieldSecondField);
		setComboBoxSecondFieldItems(FieldType.INT16);
		labelSecondFieldType.setText(FieldTypeUtilities.getFieldTypeName(tabular.getRecordset().getFieldInfos().get(comboBoxSecondField.getSelectedIndex())
				.getType()));
		labelOperationEQ.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelExpression"));
		textFieldOperationEQ.setText(comboBoxOperationField.getSelectedItem().toString() + comboBoxMethod.getSelectedItem().toString()
				+ comboBoxSecondField.getSelectedItem().toString());
		labelEQTip.setText("");
	}

	private void setSingleFieldInfo() {
		// 单字段运算时的界面设置
		addNeededComponents();
		checkBoxInversion.setEnabled(true);
		labelOperationField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelField"));
		comboBoxOperationField.setEnabled(true);
		labelOperationFieldType.setText(FieldTypeUtilities.getFieldTypeName(tabular.getRecordset().getFieldInfos()
				.get(comboBoxOperationField.getSelectedIndex()).getType()));
		labelMethod.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelOperatorType"));
		updateComboboxMethod();
		comboBoxMethod.setEnabled(true);
		labelSecondField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelFieldValue"));
		replaceSecondField(textFieldSecondField, comboBoxSecondField);
		labelOperationEQ.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelExpression"));
		textFieldSecondField.setEnabled(true);
		labelEQTip.setText("");
		labelSecondFieldType.setText("");
	}

	private void setUnityEvaluationInfo() {
		// 统一赋值时的界面设置
		addNeededComponents();
		checkBoxInversion.setEnabled(false);
		labelOperationField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelField"));
		comboBoxOperationField.setEnabled(false);
		labelMethod.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelOperatorType"));
		updateComboboxMethod();
		comboBoxMethod.setEnabled(false);
		textFieldSecondField.setEnabled(true);
		labelOperationEQ.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelExpression"));
		if (StringUtilities.isNullOrEmptyString(textFieldSecondField.getText())
				&& FieldTypeUtilities.isNumber(fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType())) {
			textFieldOperationEQ.setText("0");
		}
		if (!StringUtilities.isNullOrEmptyString(textFieldSecondField.getText())) {
			textFieldOperationEQ.setText(textFieldSecondField.getText());
		} else {
			textFieldOperationEQ.setText("");
		}
		labelSecondFieldType.setText("");
		labelEQTip.setText("");
		labelSecondField.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_labelSecondField"));
		if (fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType().equals(FieldType.BOOLEAN)) {
			replaceSecondField(comboBoxSecondField, textFieldSecondField);
			setComboBoxSecondFieldItems(FieldType.BOOLEAN);
			textFieldOperationEQ.setText("True");
		} else {
			replaceSecondField(textFieldSecondField, comboBoxSecondField);
		}
	}

	private void addNeededComponents() {
		// 判断当前界面中是否有表达式入口
		if (isExpressionSelect) {
			contentPanel.remove(textAreaExpression);
			contentPanel.remove(buttonExpression);
			addContentPanel();
			contentPanel.updateUI();
			isExpressionSelect = false;
		}
	}

	private void updateComboboxMethod() {
		resetMethodItems();
		textFieldX.setEnabled(false);
		textFieldY.setEnabled(false);
	}

	private void replaceSecondField(JComponent component, JComponent removeComponent) {
		contentPanel.remove(removeComponent);
		contentPanel.add(component, new GridBagConstraintsHelper(1, 5, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(10, 1).setInsets(0, 10, 5, 0)
				.setFill(GridBagConstraints.HORIZONTAL));
		contentPanel.updateUI();
	}

	private void resetTextFieldOperationEQ() {
		String operationField = comboBoxOperationField.getSelectedItem().toString();
		String method = comboBoxMethod.getSelectedItem().toString();
		if (StringUtilities.isNullOrEmptyString(textFieldSecondField.getText()) || !StringUtilities.isNumber(textFieldSecondField.getText())
				&& FieldTypeUtilities.isNumber(fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType())) {
			operationField = operationField + method + "0";
		}
		if (!StringUtilities.isNullOrEmptyString(textFieldSecondField.getText()) && StringUtilities.isNumber(textFieldSecondField.getText())
				&& FieldTypeUtilities.isNumber(fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType())) {
			operationField = operationField + method + textFieldSecondField.getText();
		}
		if (!StringUtilities.isNullOrEmptyString(textFieldSecondField.getText())
				&& !FieldTypeUtilities.isNumber(fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType())) {
			operationField = operationField + method + textFieldSecondField.getText();
		}
		textFieldOperationEQ.setText(operationField);
	}

	private void updateFieldChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			int updateFieldIndex = comboBoxUpdateField.getSelectedIndex();
			FieldType fieldType = fieldInfoMap.get(updateFieldIndex).getType();
			setComboBoxSourceOfFieldItems(fieldType);
			if (fieldType.equals(FieldType.BOOLEAN)) {
				replaceSecondField(comboBoxSecondField, textFieldSecondField);
				setComboBoxSecondFieldItems(fieldType);
			}
			comboBoxSourceOfField.setSelectedIndex(0);
			labelFieldType.setText(FieldTypeUtilities.getFieldTypeName(fieldInfoMap.get(updateFieldIndex).getType()));
			initTextFieldOperationEQText(fieldInfoMap.get(updateFieldIndex).getType());
		}
	}

	private void checkBoxChanged(ActionEvent e) {
		if (e.getSource().equals(radioButtonUpdateColumn)) {
			boolean updateColumn = radioButtonUpdateColumn.isSelected();
			checkBoxUpdateSelect.setSelected(!updateColumn);
			return;
		}
		if (e.getSource().equals(checkBoxUpdateSelect)) {
			boolean updateSelect = checkBoxUpdateSelect.isSelected();
			radioButtonUpdateColumn.setSelected(!updateSelect);
			return;
		}
		if (e.getSource().equals(checkBoxInversion)) {
			String method = comboBoxMethod.getSelectedItem().toString();
			String filter = textFieldOperationEQ.getText();
			String[] expression = filter.split("\\" + method);
			String result = "";
			if (2 == expression.length) {
				result = expression[1].trim() + method + expression[0].trim();
			}
			textFieldOperationEQ.setText(result);
		}
	}

	private void sourceOfFieldChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			String sourceOfField = comboBoxSourceOfField.getSelectedItem().toString();
			if (sourceOfField.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeSetValue"))) {
				setUnityEvaluationInfo();
				return;
			}
			if (sourceOfField.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeOneField"))) {
				setSingleFieldInfo();
				return;
			}
			if (sourceOfField.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeTwoFields"))) {
				setDoubleFieldInfo();
				return;
			}
			if (sourceOfField.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeMath"))) {
				setFunctionInfo();
				return;
			}
			if (sourceOfField.equals(CoreProperties.getString("String_ThemeGraphItemExpressionPicker_ButtonExpression"))) {
				setExpression();
				return;
			}
		}
	}

	private void operationFieldChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			String sourceOfField = comboBoxSourceOfField.getSelectedItem().toString();
			labelOperationFieldType.setText(FieldTypeUtilities.getFieldTypeName(tabular.getRecordset().getFieldInfos()
					.get(comboBoxOperationField.getSelectedItem().toString()).getType()));
			if (sourceOfField.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeOneField"))) {
				// 单字段运算
				resetTextFieldOperationEQ();
				return;
			}
			if (sourceOfField.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeTwoFields"))) {
				// 双字段运算
				String fristField = comboBoxOperationField.getSelectedItem().toString();
				String operation = comboBoxMethod.getSelectedItem().toString();
				String secondField = comboBoxSecondField.getSelectedItem().toString();
				textFieldOperationEQ.setText(fristField + operation + secondField);
				return;
			}
			if (sourceOfField.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeMath"))) {
				// 函数运算
				resetMethodInfo();
				return;
			}

		}
	}

	private void resetMethodInfo() {
		comboBoxMethod.setSelectedIndex(0);
		comboBoxMethod.setEnabled(true);
		resetMethodItemsForMathModel();
	}

	private void methodChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			String tempOperationField = comboBoxOperationField.getSelectedItem().toString();
			String method = comboBoxMethod.getSelectedItem().toString();
			String sourceOfField = comboBoxSourceOfField.getSelectedItem().toString();
			FieldType updateFieldType = fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType();
			if (sourceOfField.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeOneField"))) {
				// 单字段运算
				if ((FieldTypeUtilities.isNumber(updateFieldType) || updateFieldType.equals(FieldType.BOOLEAN))
						&& StringUtilities.isNullOrEmptyString(textFieldSecondField.getText())) {
					textFieldOperationEQ.setText(comboBoxOperationField.getSelectedItem().toString() + method + "0");
				} else {
					textFieldOperationEQ.setText(comboBoxOperationField.getSelectedItem().toString() + method + textFieldSecondField.getText());
				}
				return;
			}
			if (sourceOfField.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeMath"))) {
				// 函数运算
				if (method.equals("Abs") || method.equals("Sqrt") || method.equals("Ln") || method.equals("Int")) {
					setMethodStatus(true, false, false, method + "(" + tempOperationField + ")", method);
				} else if (UpdateColumnUtilties.isObjectConnect(method)) {
					setMethodStatus(false, false, false, "Object." + method + "()", method);
				} else if (method.equals("Log")) {
					textFieldX.setText("10");
					setMethodStatus(true, true, false, method + "(" + tempOperationField + ",10)", method);
				} else if (method.equals("Left") || method.equals("Right")) {
					textFieldX.setText("1");
					setMethodStatus(true, true, false, method + "(" + tempOperationField + ",1)", method);
				} else if (method.equals("LRemove") || method.equals("RRemove")) {
					textFieldX.setText("0");
					setMethodStatus(true, true, false, method + "(" + tempOperationField + ",0)", method);
				} else if (method.equals("Mid")) {
					textFieldX.setText("0");
					textFieldY.setText("0");
					setMethodStatus(true, true, true, method + "(" + tempOperationField + ",0,0)", method);
				} else if (method.equals("UCase") || method.equals("LCase") || method.equals("Trim")) {
					setMethodStatus(true, false, false, method + "(" + tempOperationField + ")", method);
				} else if (method.equals("TrimEnd") || method.equals("TrimStart")) {
					textFieldX.setText("abc");
					setMethodStatus(true, true, false, method + "(" + tempOperationField + ",'a','b','c')", method);
				} else if (method.equals("Replace")) {
					textFieldX.setText("");
					textFieldY.setText("");
					setMethodStatus(true, true, true, method + "(" + tempOperationField + ",'','')", method);
				} else if (method.contains("Add")) {
					textFieldX.setText("0");
					setMethodStatus(true, true, false, tempOperationField + "." + method + "(0)", method);
				} else if (method.equals("Date") || method.equals("Now")) {
					setMethodStatus(true, true, false, method, method);
				} else if (method.equals("DaysInMonth")) {
					setMethodStatus(true, false, false, method + "(" + tempOperationField + ".Year," + tempOperationField + ".Month)", method);
				} else if (method.equals("Second") || method.equals("Minute") || method.equals("Hour") || method.equals("Day") || method.equals("Month")
						|| method.equals("Year") || method.equals("DayOfYear") || method.equals("DayOfWeek")) {
					setMethodStatus(false, false, false, tempOperationField + "." + method, method);
				}
				return;
			}
		}
	}

	private void setMethodStatus(boolean enableField, boolean enableX, boolean enableY, String expression, String method) {
		comboBoxOperationField.setEnabled(enableField);
		textFieldX.setEnabled(enableX);
		textFieldY.setEnabled(enableY);
		textFieldOperationEQ.setText(expression);
		if (!StringUtilities.isNullOrEmptyString(method)) {
			labelEQTip.setText(TabularViewProperties.getString("String_FormTabularUpdataColumn_" + method));
		}
	}

	private void textFieldXChanged() {
		String method = comboBoxMethod.getSelectedItem().toString();
		if ((method.equals("Log") || method.equals("Left") || method.equals("Right") || method.equals("LRemove") || method.equals("RRemove"))
				&& !StringUtilities.isNullOrEmptyString(textFieldX.getText())) {
			textFieldOperationEQ.setText(method + "(" + comboBoxOperationField.getSelectedItem().toString() + "," + textFieldX.getText() + ")");
			return;
		} else if ((method.equals("AddDays") || method.equals("AddHours") || method.equals("AddMilliseconds") || method.equals("AddSeconds")
				|| method.equals("AddMinutes") || method.equals("AddMonths") || method.equals("AddYears"))
				&& !StringUtilities.isNullOrEmptyString(textFieldX.getText()) && StringUtilities.isPositiveInteger(textFieldX.getText())) {
			textFieldOperationEQ.setText(comboBoxOperationField.getSelectedItem().toString() + "." + method + "(" + textFieldX.getText() + ")");
			return;
		} else if (method.equals("Mid") && !StringUtilities.isNullOrEmptyString(textFieldX.getText())
				&& StringUtilities.isPositiveInteger(textFieldX.getText())) {
			textFieldOperationEQ.setText(method + "(" + comboBoxOperationField.getSelectedItem().toString() + "," + textFieldX.getText() + ","
					+ textFieldY.getText() + ")");
			return;
		} else if (method.equals("Replace")) {
			textFieldOperationEQ.setText(method + "(" + comboBoxOperationField.getSelectedItem().toString() + ",\'" + textFieldX.getText() + "\',\'"
					+ textFieldY.getText() + "\')");
			return;
		} else if (method.equals("TrimEnd") || method.equals("TrimStart")) {
			String expression = method + "(" + comboBoxOperationField.getSelectedItem().toString();
			for (int i = 0; i < textFieldX.getText().toCharArray().length; i++) {
				expression += ",\'" + textFieldX.getText().toCharArray()[i] + "\'";
			}
			expression += ")";
			textFieldOperationEQ.setText(expression);
		}

	}

	private void textFieldYChanged() {
		String methodItem = comboBoxMethod.getSelectedItem().toString();
		if (methodItem.equals("Mid") && !StringUtilities.isNullOrEmptyString(textFieldX.getText()) && StringUtilities.isPositiveInteger(textFieldX.getText())) {
			textFieldOperationEQ.setText(methodItem + "(" + comboBoxOperationField.getSelectedItem().toString() + "," + textFieldX.getText() + ","
					+ textFieldY.getText() + ")");
			return;
		}
		if (methodItem.equals("Replace")) {
			textFieldOperationEQ.setText(methodItem + "(" + comboBoxOperationField.getSelectedItem().toString() + ",\'" + textFieldX.getText() + "\',\'"
					+ textFieldY.getText() + "\')");
			return;
		}
	}

	private void secondFieldChanged() {
		String sourceOfField = comboBoxSourceOfField.getSelectedItem().toString();
		FieldType fieldType = fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType();
		if (sourceOfField.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeSetValue"))) {
			updateEQ(fieldType, "");
			// if (fieldType.equals(FieldType.DATETIME) && !StringUtilities.isNullOrEmptyString(textFieldSecondField.getText())) {
			// textFieldOperationEQ.setText(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(textFieldSecondField.getText()));
			// }
		} else if (sourceOfField.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeOneField"))) {
			updateEQ(fieldType, comboBoxOperationField.getSelectedItem().toString() + comboBoxMethod.getSelectedItem().toString());
		}
	}

	private void updateEQ(FieldType fieldType, String info) {
		if (FieldTypeUtilities.isNumber(fieldType) && StringUtilities.isNullOrEmptyString(textFieldSecondField.getText())) {
			textFieldOperationEQ.setText(info + "0");
			return;
		}
		if ((fieldType.equals(FieldType.INT16) || fieldType.equals(FieldType.INT32) || fieldType.equals(FieldType.INT64))
				&& !StringUtilities.isNullOrEmptyString(textFieldSecondField.getText()) && StringUtilities.isNumeric(textFieldSecondField.getText())) {
			textFieldOperationEQ.setText(info + textFieldSecondField.getText());
			return;
		}
		if ((fieldType.equals(FieldType.SINGLE) || fieldType.equals(FieldType.DOUBLE)) && !StringUtilities.isNullOrEmptyString(textFieldSecondField.getText())
				&& StringUtilities.isDecimal(textFieldSecondField.getText())) {
			textFieldOperationEQ.setText(info + textFieldSecondField.getText());
			return;
		}
		if ((FieldTypeUtilities.isString(fieldType) || fieldType.equals(FieldType.CHAR)) && StringUtilities.isNullOrEmptyString(textFieldSecondField.getText())) {
			textFieldOperationEQ.setText(info);
			return;
		}
		if ((FieldTypeUtilities.isString(fieldType) || fieldType.equals(FieldType.CHAR))
				&& !StringUtilities.isNullOrEmptyString(textFieldSecondField.getText())) {
			textFieldOperationEQ.setText(info + textFieldSecondField.getText());
			return;
		}
	}

	private void buttonApplyClicked() {
		String updateModel = comboBoxSourceOfField.getSelectedItem().toString();
		if (updateModel.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeSetValue"))) {
			// 统一赋值
			updateUnitySetValue();
		} else if (updateModel.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeOneField"))) {
			// 单字段运算
			updateOneField();
		} else if (updateModel.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeTwoFields"))) {
			// 双字段运算
			updateTwoField();
		} else if (updateModel.equals(TabularViewProperties.getString("String_FormTabularUpdataColumn_UpdataModeMath"))) {
			// 函数运算
			updateModeMath();
		} else {
			// sql查询结果替换
			updateModeQuery();
		}
	}

	private void updateModeQuery() {
		try {
			QueryParameter parameter = new QueryParameter();
			parameter.setAttributeFilter(textAreaExpression.getText());
			parameter.setCursorType(CursorType.STATIC);
			parameter.setHasGeometry(true);
			parameter.setResultFields(new String[] { textAreaExpression.getText() });
			Recordset result = tabular.getRecordset().getDataset().query(parameter);
			boolean selectAllColumn = radioButtonUpdateColumn.isSelected();
			FieldType fieldType = fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType();
			if (null != result) {
				if (selectAllColumn) {
					// 更新选中列
					int[] selectRows = new int[tabular.getjTableTabular().getRowCount()];
					for (int i = 0; i < selectRows.length; i++) {
						selectRows[i] = i;
					}
					resetFieldForModeExpression(fieldType, selectRows, result, textAreaExpression.getText());
				} else {
					resetFieldForModeExpression(fieldType, tabular.getSelectedRows(), result, textAreaExpression.getText());
				}
			} else {
				Application.getActiveApplication().getOutput().output(TabularViewProperties.getString("String_UpdateColumnFailed"));
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void resetFieldForModeExpression(FieldType fieldType, int[] selectRows, Recordset resultSet, String resultField) {
		Recordset recordset = tabular.getRecordset();
		boolean beyoundMaxLength = false;
		String updateField = comboBoxUpdateField.getSelectedItem().toString();// 更新字段
		recordset.getBatch().setMaxRecordCount(1024);
		recordset.getBatch().begin();
		for (int i = 0; i < selectRows.length; i++) {
			recordset.moveTo(selectRows[i]);
			resultSet.moveTo(selectRows[i]);
			if (UpdateColumnUtilties.isIntegerType(fieldType)) {
				recordset.setFieldValue(updateField, Convert.toInteger(resultSet.getFieldValue(resultField)));
			} else if (fieldType.equals(FieldType.SINGLE) || fieldType.equals(FieldType.DOUBLE)) {
				recordset.setFieldValue(updateField, Convert.toDouble(resultSet.getFieldValue(resultField)));
			} else if (fieldType.equals(FieldType.BOOLEAN)) {
				recordset.setFieldValue(updateField, Convert.toBoolean(resultSet.getFieldValue(resultField)));
			}
		}
		recordset.getBatch().update();
		if (beyoundMaxLength) {
			Application.getActiveApplication().getOutput()
					.output(MessageFormat.format(TabularViewProperties.getString("String_FormTabularUpdataColumn_FieldInfoDesValueIsOverlong"), updateField));
		}
		// 重新查询避免操作后记录集清除的异常
		refreshTabular(selectRows);
	}

	private void updateModeMath() {
		boolean isUpdateAll = radioButtonUpdateColumn.isSelected();
		FieldType fieldType = fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType();
		if (isUpdateAll) {
			// 更新选中列
			int[] selectRows = new int[tabular.getjTableTabular().getRowCount()];
			for (int i = 0; i < selectRows.length; i++) {
				selectRows[i] = i;
			}
			resetFieldForModeMath(fieldType, selectRows);
		} else {
			resetFieldForModeMath(fieldType, tabular.getSelectedRows());
		}
	}

	private void resetFieldForModeMath(FieldType fieldType, int[] selectRows) {
		if (FieldTypeUtilities.isNumber(fieldType) || fieldType.equals(FieldType.BYTE)) {
			// 整型
			updateFieldModeMathNumber(fieldType, selectRows);
		} else if (FieldTypeUtilities.isString(fieldType) || fieldType.equals(FieldType.CHAR)) {
			// 字符串型
			updateModeMathText(fieldType, selectRows);
		} else if (fieldType.equals(FieldType.DATETIME)) {
			// 日期型
		}
	}

	private void updateModeMathText(FieldType fieldType, int[] selectRows) {
		String fristField = comboBoxOperationField.getSelectedItem().toString();// 运算字段
		String updateField = comboBoxUpdateField.getSelectedItem().toString();// 更新字段
		String method = comboBoxMethod.getSelectedItem().toString();
		Recordset recordset = tabular.getRecordset();
		boolean beyoundMaxLength = false;
		recordset.getBatch().setMaxRecordCount(1024);
		recordset.getBatch().begin();
		Object newValue = null;
		for (int i = 0; i < selectRows.length; i++) {
			recordset.moveTo(selectRows[i]);
			if (UpdateColumnUtilties.isObjectConnect(method)) {
				newValue = UpdateColumnUtilties.getObjectInfo(method, recordset.getGeometry(), fieldType);
			} else {
				newValue = UpdateColumnUtilties.getUpdataModeMathValueText(method, recordset.getFieldValue(fristField).toString(), textFieldX.getText(),
						textFieldY.getText());
			}
			if (newValue.toString().length() > recordset.getFieldInfos().get(updateField).getMaxLength()) {
				beyoundMaxLength = true;
				newValue = newValue.toString().substring(0, recordset.getFieldInfos().get(updateField).getMaxLength());
			}
			recordset.setFieldValue(updateField, newValue);
		}
		recordset.getBatch().update();
		if (beyoundMaxLength) {
			Application.getActiveApplication().getOutput()
					.output(MessageFormat.format(TabularViewProperties.getString("String_FormTabularUpdataColumn_FieldInfoDesValueIsOverlong"), updateField));
		}
		// 重新查询避免操作后记录集清除的异常
		refreshTabular(selectRows);
	}

	private void updateFieldModeMathNumber(FieldType fieldType, int[] selectRows) {
		String fristField = comboBoxOperationField.getSelectedItem().toString();// 运算字段
		String updateField = comboBoxUpdateField.getSelectedItem().toString();// 更新字段
		String method = comboBoxMethod.getSelectedItem().toString();
		Recordset recordset = tabular.getRecordset();
		recordset.getBatch().setMaxRecordCount(1024);
		recordset.getBatch().begin();
		Object newValue = null;
		for (int i = 0; i < selectRows.length; i++) {
			recordset.moveTo(selectRows[i]);
			if (UpdateColumnUtilties.isMathInfo(method)) {
				newValue = UpdateColumnUtilties.getMathInfo(method, recordset.getFieldValue(fristField).toString(), textFieldX.getText(), fieldType);
			} else if (UpdateColumnUtilties.isObjectConnect(method)) {
				newValue = UpdateColumnUtilties.getObjectInfo(method, recordset.getGeometry(), fieldType);
			}
			recordset.setFieldValue(updateField, newValue);
		}
		recordset.getBatch().update();
		// 重新查询避免操作后记录集清除的异常
		refreshTabular(selectRows);
	}

	private void updateTwoField() {
		boolean isUpdateAll = radioButtonUpdateColumn.isSelected();
		FieldType fieldType = fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType();
		if (isUpdateAll) {
			// 更新选中列
			int[] selectRows = new int[tabular.getjTableTabular().getRowCount()];
			for (int i = 0; i < selectRows.length; i++) {
				selectRows[i] = i;
			}
			resetFieldForTwoField(fieldType, selectRows);
		} else {
			resetFieldForTwoField(fieldType, tabular.getSelectedRows());
		}
	}

	private void resetFieldForTwoField(FieldType fieldType, int[] selectRows) {
		if (fieldType.equals(FieldType.TEXT) || fieldType.equals(FieldType.WTEXT) || fieldType.equals(FieldType.CHAR)) {
			// 文本型
			resetFieldForTwoField(selectRows, true, fieldType);
		} else {
			resetFieldForTwoField(selectRows, false, fieldType);
		}
	}

	private void resetFieldForTwoField(int[] selectRows, boolean isText, FieldType fieldType) {
		String fristField = comboBoxOperationField.getSelectedItem().toString();
		String updateField = comboBoxUpdateField.getSelectedItem().toString();
		String secondField = comboBoxSecondField.getSelectedItem().toString();
		String method = comboBoxMethod.getSelectedItem().toString();
		Recordset recordset = tabular.getRecordset();
		boolean beyoundMaxLength = false;
		recordset.getBatch().setMaxRecordCount(1024);
		recordset.getBatch().begin();
		for (int i = 0; i < selectRows.length; i++) {
			recordset.moveTo(selectRows[i]);
			if (isText) {
				String newValue = recordset.getFieldValue(fristField).toString().concat(recordset.getFieldValue(secondField).toString());
				if (newValue.length() > recordset.getFieldInfos().get(updateField).getMaxLength()) {
					beyoundMaxLength = true;
					newValue = newValue.substring(0, recordset.getFieldInfos().get(updateField).getMaxLength());
				}
				recordset.setFieldValue(updateField, newValue);
			} else {
				recordset.setFieldValue(updateField,
						UpdateColumnUtilties.getCommonMethodInfo(method, recordset.getFieldValue(fristField), recordset.getFieldValue(secondField), fieldType));
			}
		}
		recordset.getBatch().update();
		if (beyoundMaxLength) {
			Application.getActiveApplication().getOutput()
					.output(MessageFormat.format(TabularViewProperties.getString("String_FormTabularUpdataColumn_FieldInfoDesValueIsOverlong"), updateField));
		}
		// 重新查询避免操作后记录集清除的异常
		refreshTabular(selectRows);
	}

	private void refreshTabular(int[] selectRows) {
		Recordset tempRecordset = tabular.getRecordset().getDataset().getRecordset(false, CursorType.DYNAMIC);
		tabular.setRecordset(tempRecordset);
		// 恢复原来的选中项
		for (int j = 0; j < selectRows.length; j++) {
			tabular.getjTableTabular().addRowSelectionInterval(selectRows[j], selectRows[j]);
		}
		int selectColumn = tabular.getjTableTabular().getSelectedColumn();
		if (selectColumn != -1) {
			tabular.getjTableTabular().setColumnSelectionInterval(selectColumn, selectColumn);
		}
	}

	private void updateOneField() {
		boolean isUpdateAll = radioButtonUpdateColumn.isSelected();
		FieldType fieldType = fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType();
		if (isUpdateAll) {
			// 更新选中列
			int[] selectRows = new int[tabular.getjTableTabular().getRowCount()];
			for (int i = 0; i < selectRows.length; i++) {
				selectRows[i] = i;
			}
			resetFieldForOneField(fieldType, selectRows);
		} else {
			resetFieldForOneField(fieldType, tabular.getSelectedRows());
		}
	}

	private void resetFieldForOneField(FieldType fieldType, int[] selectRows) {
		if (fieldType.equals(FieldType.TEXT) || fieldType.equals(FieldType.WTEXT) || fieldType.equals(FieldType.CHAR)) {
			// 文本型
			resetFieldForOneField(selectRows, true, fieldType);
		} else {
			resetFieldForOneField(selectRows, false, fieldType);
		}
	}

	private void resetFieldForOneField(int[] selectRows, boolean isText, FieldType fieldType) {
		String fristField = comboBoxOperationField.getSelectedItem().toString();
		String updateField = comboBoxUpdateField.getSelectedItem().toString();
		String method = comboBoxMethod.getSelectedItem().toString();
		String value = textFieldSecondField.getText();
		Recordset recordset = tabular.getRecordset();
		boolean beyoundMaxLength = false;
		recordset.getBatch().setMaxRecordCount(1024);
		recordset.getBatch().begin();
		for (int i = 0; i < selectRows.length; i++) {
			recordset.moveTo(selectRows[i]);
			if (isText) {
				String newValue = recordset.getFieldValue(fristField).toString().concat(value);
				if (newValue.length() > recordset.getFieldInfos().get(updateField).getMaxLength()) {
					beyoundMaxLength = true;
					newValue = newValue.substring(0, recordset.getFieldInfos().get(updateField).getMaxLength());
				}
				recordset.setFieldValue(updateField, newValue);
			} else {
				recordset.setFieldValue(updateField, UpdateColumnUtilties.getCommonMethodInfo(method, recordset.getFieldValue(fristField), value, fieldType));
			}
		}
		recordset.getBatch().update();
		if (beyoundMaxLength) {
			Application.getActiveApplication().getOutput()
					.output(MessageFormat.format(TabularViewProperties.getString("String_FormTabularUpdataColumn_FieldInfoDesValueIsOverlong"), updateField));
		}
		refreshTabular(selectRows);
	}

	private void updateUnitySetValue() {
		boolean isUpdateAll = radioButtonUpdateColumn.isSelected();
		String updateField = comboBoxUpdateField.getSelectedItem().toString();
		String expression = textFieldOperationEQ.getText();
		FieldType fieldType = fieldInfoMap.get(comboBoxUpdateField.getSelectedIndex()).getType();
		if (isUpdateAll) {
			// 更新选中列
			int[] selectRows = new int[tabular.getjTableTabular().getRowCount()];
			for (int i = 0; i < selectRows.length; i++) {
				selectRows[i] = i;
			}
			upDateForUnitySet(updateField, expression, fieldType, selectRows, tabular.getjTableTabular().getSelectedColumn());
		} else {
			int[] selectRows = tabular.getSelectedRows();
			upDateForUnitySet(updateField, expression, fieldType, selectRows, tabular.getjTableTabular().getSelectedColumn());
		}
	}

	private void upDateForUnitySet(String updateField, String expression, FieldType fieldType, int[] selectRows, int selectColumn) {
		Object newValue = null;
		if (UpdateColumnUtilties.isIntegerType(fieldType)) {
			// 整型
			if (StringUtilities.isNullOrEmptyString(expression)) {
				newValue = 0;
			} else {
				newValue = Convert.toInteger(expression);
			}
			updateUnitySetValue(selectRows, updateField, newValue, selectColumn);
		} else if (fieldType.equals(FieldType.SINGLE) || fieldType.equals(FieldType.DOUBLE)) {
			// 浮点型
			if (StringUtilities.isNullOrEmptyString(expression)) {
				newValue = 0.0;
			} else {
				newValue = Convert.toDouble(expression);
			}
		} else if (fieldType.equals(FieldType.TEXT) || fieldType.equals(FieldType.WTEXT) || fieldType.equals(FieldType.CHAR)) {
			// 字符型
			if (StringUtilities.isNullOrEmptyString(expression)) {
				newValue = null;
			} else {
				newValue = expression;
			}
			updateUnitySetValue(selectRows, updateField, newValue, selectColumn);
		} else if (fieldType.equals(FieldType.BOOLEAN)) {
			// 布尔型
			if (expression.equalsIgnoreCase("True")) {
				newValue = true;
			} else {
				newValue = false;
			}
			updateUnitySetValue(selectRows, updateField, newValue, selectColumn);
		} else if (fieldType.equals(FieldType.LONGBINARY)) {
			// 二进制型
		} else if (fieldType.equals(FieldType.DATETIME)) {
			if (StringUtilities.isNullOrEmptyString(expression)) {
				newValue = new Date();
			} else {
				newValue = Convert.toDateTime(expression);
			}
			updateUnitySetValue(selectRows, updateField, newValue, selectColumn);
		}
	}

	private void updateUnitySetValue(int[] selectRows, String updateField, Object newValue, int selectColumn) {

		Recordset recordset = tabular.getRecordset();
		recordset.getBatch().setMaxRecordCount(1024);
		recordset.getBatch().begin();
		for (int i = 0; i < selectRows.length; i++) {
			recordset.moveTo(selectRows[i]);
			recordset.setFieldValue(updateField, newValue);
		}
		recordset.getBatch().update();
		// 重新查询避免操作后记录集清除的异常
		refreshTabular(selectRows);
	}
}
