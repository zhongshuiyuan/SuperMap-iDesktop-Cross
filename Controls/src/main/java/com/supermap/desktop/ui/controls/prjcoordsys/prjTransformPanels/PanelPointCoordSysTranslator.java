package com.supermap.desktop.ui.controls.prjcoordsys.prjTransformPanels;

import com.supermap.desktop.Interface.ISmTextFieldLegit;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.TextFields.DMSTextField;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by yuanR on 2017/10/12 0012.
 * 坐标点转换功能.坐标点键入面板
 * 面板一共有三态：x/y、经纬度（度形式）、经纬度（度分秒形式）
 */
public class PanelPointCoordSysTranslator extends JPanel {

	private final static int METERMODEL = 1;
	private final static int DEGREEMODEL = 2;
	private final static int DMSMODEL = 3;
	private int currentModel = METERMODEL;

	private JLabel labelX = new JLabel("X:");
	private JLabel labelY = new JLabel("Y:");

	private JLabel labelLongtitudeDegree = new JLabel(ControlsProperties.getString("String_Label_LongitudeValue"));
	private JLabel labelLatitudeDegree = new JLabel(ControlsProperties.getString("String_Label_LatitudeValue"));

	private JLabel labelLongtitude = new JLabel(ControlsProperties.getString("String_Label_LongitudeValue"));
	private JLabel labelLatitude = new JLabel(ControlsProperties.getString("String_Label_LatitudeValue"));

	// 单位为“米”的textField
	private UnitMeterTextField unitMeterTextFieldX = new UnitMeterTextField();
	private UnitMeterTextField unitMeterTextFieldY = new UnitMeterTextField();
	// 单位为“度、分、秒”的textField
	private DMSTextField textFieldLongitudeValue = new DMSTextField();
	private DMSTextField textFieldLatitudeValue = new DMSTextField();
	// 单位为“度”的textField
	private UnitDegreeTextField unitDegreeTextFieldLongtitude = new UnitDegreeTextField();
	private UnitDegreeTextField unitDegreeTextFieldLatitude = new UnitDegreeTextField();

	private JCheckBox checkBoxShowAsDMS = new JCheckBox(ControlsProperties.getString("String_CheckBox_ShowAsDMS"));

	///**
	// * 两种以度为单位的经度textField，内容改变监听
	// */
	//private CaretListener LongtitudeTextFieldCaretListener = new CaretListener() {
	//	@Override
	//	public void caretUpdate(CaretEvent e) {
	//		if (e.getSource().equals(unitDegreeTextFieldLongtitude.getTextField())) {
	//			// 当以度为单位的经度值改变时，同时设置以度分秒为单位的经度值textField
	//			textFieldLongitudeValue.getTextFieldD().removeCaretListener(LongtitudeTextFieldCaretListener);
	//			textFieldLongitudeValue.getTextFieldM().removeCaretListener(LongtitudeTextFieldCaretListener);
	//			textFieldLongitudeValue.getTextFieldS().removeCaretListener(LongtitudeTextFieldCaretListener);
	//			textFieldLongitudeValue.setDMSValue(unitDegreeTextFieldLongtitude.getTextField().getText());
	//			textFieldLongitudeValue.getTextFieldD().addCaretListener(LongtitudeTextFieldCaretListener);
	//			textFieldLongitudeValue.getTextFieldM().addCaretListener(LongtitudeTextFieldCaretListener);
	//			textFieldLongitudeValue.getTextFieldS().addCaretListener(LongtitudeTextFieldCaretListener);
	//		} else if (e.getSource().equals(textFieldLongitudeValue.getTextFieldD())) {
	//			unitDegreeTextFieldLongtitude.getTextField().removeCaretListener(LongtitudeTextFieldCaretListener);
	//			unitDegreeTextFieldLongtitude.getTextField().setText(DoubleUtilities.getFormatString(textFieldLongitudeValue.getDMSValue()));
	//			unitDegreeTextFieldLongtitude.getTextField().addCaretListener(LongtitudeTextFieldCaretListener);
	//		} else if (e.getSource().equals(textFieldLongitudeValue.getTextFieldM())) {
	//			unitDegreeTextFieldLongtitude.getTextField().removeCaretListener(LongtitudeTextFieldCaretListener);
	//			unitDegreeTextFieldLongtitude.getTextField().setText(DoubleUtilities.getFormatString(textFieldLongitudeValue.getDMSValue()));
	//			unitDegreeTextFieldLongtitude.getTextField().addCaretListener(LongtitudeTextFieldCaretListener);
	//		} else if (e.getSource().equals(textFieldLongitudeValue.getTextFieldS())) {
	//			unitDegreeTextFieldLongtitude.getTextField().removeCaretListener(LongtitudeTextFieldCaretListener);
	//			unitDegreeTextFieldLongtitude.getTextField().setText(DoubleUtilities.getFormatString(textFieldLongitudeValue.getDMSValue()));
	//			unitDegreeTextFieldLongtitude.getTextField().addCaretListener(LongtitudeTextFieldCaretListener);
	//		}
	//	}
	//};
	//
	///**
	// * 两种以度为单位的经度textField，内容改变监听
	// */
	//private CaretListener LatitudeTextFieldCaretListener = new CaretListener() {
	//	@Override
	//	public void caretUpdate(CaretEvent e) {
	//		if (e.getSource().equals(unitDegreeTextFieldLatitude)) {
	//
	//		} else if (e.getSource().equals(textFieldLatitudeValue)) {
	//
	//		}
	//	}
	//};


	public PanelPointCoordSysTranslator() {
		initLayout();
		initListener();
		initStates();
	}

	private void initLayout() {
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);
		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.labelX)
								.addComponent(this.labelY)
								.addComponent(this.labelLongtitudeDegree)
								.addComponent(this.labelLatitudeDegree)
								.addComponent(this.labelLongtitude)
								.addComponent(this.labelLatitude))
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.unitMeterTextFieldX)
								.addComponent(this.unitMeterTextFieldY)
								.addComponent(this.unitDegreeTextFieldLongtitude)
								.addComponent(this.unitDegreeTextFieldLatitude)
								.addComponent(this.textFieldLongitudeValue)
								.addComponent(this.textFieldLatitudeValue)))
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(this.checkBoxShowAsDMS)));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelX)
						.addComponent(this.unitMeterTextFieldX, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelY)
						.addComponent(this.unitMeterTextFieldY, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelLongtitudeDegree)
						.addComponent(this.unitDegreeTextFieldLongtitude, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelLatitudeDegree)
						.addComponent(this.unitDegreeTextFieldLatitude, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelLongtitude)
						.addComponent(this.textFieldLongitudeValue, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelLatitude)
						.addComponent(this.textFieldLatitudeValue, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(this.checkBoxShowAsDMS))
		);
		// @formatter:on
	}

	private void initListener() {
		removeListener();
		this.checkBoxShowAsDMS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 当点击checkBoxShowAsDMS时，算值，并显示
				if (checkBoxShowAsDMS.isSelected()) {
					textFieldLongitudeValue.setDMSValue(unitDegreeTextFieldLongtitude.getTextField().getText());
					textFieldLatitudeValue.setDMSValue(unitDegreeTextFieldLatitude.getTextField().getText());
				} else {
					unitDegreeTextFieldLongtitude.getTextField().setText(DoubleUtilities.getFormatString(textFieldLongitudeValue.getDMSValue()));
					unitDegreeTextFieldLatitude.getTextField().setText(DoubleUtilities.getFormatString(textFieldLatitudeValue.getDMSValue()));
				}
				setComponentVisible();
			}
		});

		//this.textFieldLongitudeValue.getTextFieldD().addCaretListener(LongtitudeTextFieldCaretListener);
		//this.textFieldLongitudeValue.getTextFieldM().addCaretListener(LongtitudeTextFieldCaretListener);
		//this.textFieldLongitudeValue.getTextFieldS().addCaretListener(LongtitudeTextFieldCaretListener);
		//this.unitDegreeTextFieldLongtitude.getTextField().addCaretListener(LongtitudeTextFieldCaretListener);

	}

	private void removeListener() {
		//this.textFieldLongitudeValue.getTextFieldD().removeCaretListener(LongtitudeTextFieldCaretListener);
		//this.textFieldLongitudeValue.getTextFieldM().removeCaretListener(LongtitudeTextFieldCaretListener);
		//this.textFieldLongitudeValue.getTextFieldS().removeCaretListener(LongtitudeTextFieldCaretListener);
		//this.unitDegreeTextFieldLongtitude.getTextField().removeCaretListener(LongtitudeTextFieldCaretListener);
	}

	public void initStates() {
		setComponentVisible();
		setComponentEnabled(true);
	}


	/**
	 * 设置控件是否可见
	 */
	private void setComponentVisible() {

		this.labelX.setVisible(!this.checkBoxShowAsDMS.isSelected() && getCurrentModel() == METERMODEL);
		this.labelY.setVisible(!this.checkBoxShowAsDMS.isSelected() && getCurrentModel() == METERMODEL);
		this.unitMeterTextFieldX.setVisible(!this.checkBoxShowAsDMS.isSelected() && getCurrentModel() == METERMODEL);
		this.unitMeterTextFieldY.setVisible(!this.checkBoxShowAsDMS.isSelected() && getCurrentModel() == METERMODEL);

		this.labelLongtitudeDegree.setVisible(!this.checkBoxShowAsDMS.isSelected() && getCurrentModel() == DEGREEMODEL);
		this.labelLatitudeDegree.setVisible(!this.checkBoxShowAsDMS.isSelected() && getCurrentModel() == DEGREEMODEL);
		this.unitDegreeTextFieldLongtitude.setVisible(!this.checkBoxShowAsDMS.isSelected() && getCurrentModel() == DEGREEMODEL);
		this.unitDegreeTextFieldLatitude.setVisible(!this.checkBoxShowAsDMS.isSelected() && getCurrentModel() == DEGREEMODEL);

		this.labelLongtitude.setVisible(this.checkBoxShowAsDMS.isSelected());
		this.labelLatitude.setVisible(this.checkBoxShowAsDMS.isSelected());
		this.textFieldLongitudeValue.setVisible(this.checkBoxShowAsDMS.isSelected());
		this.textFieldLatitudeValue.setVisible(this.checkBoxShowAsDMS.isSelected());
	}


	public void setComponentEnabled(Boolean enabled) {
		this.unitMeterTextFieldX.setEnabled(enabled);
		this.unitMeterTextFieldY.setEnabled(enabled);
		this.unitDegreeTextFieldLongtitude.setEnabled(enabled);
		this.unitDegreeTextFieldLatitude.setEnabled(enabled);
		this.textFieldLongitudeValue.setEnabled(enabled);
		this.textFieldLatitudeValue.setEnabled(enabled);
		this.checkBoxShowAsDMS.setEnabled(!(getCurrentModel() == METERMODEL));
	}


	/**
	 * 获得当前model
	 *
	 * @return
	 */
	public int getCurrentModel() {
		return currentModel;
	}

	/**
	 * 设置当前model
	 * 当改变model时，设置面板显示也改变
	 *
	 * @param currentModel
	 */
	public void setCurrentModel(int currentModel) {
		this.currentModel = currentModel;
		this.checkBoxShowAsDMS.setSelected(false);
		this.checkBoxShowAsDMS.setEnabled(false);
		if (this.currentModel == DEGREEMODEL) {
			this.checkBoxShowAsDMS.setEnabled(true);
			unitDegreeTextFieldLongtitude.getTextField().setText(unitMeterTextFieldX.getTextField().getText());
			unitDegreeTextFieldLatitude.getTextField().setText(unitMeterTextFieldY.getTextField().getText());
		} else if (this.currentModel == METERMODEL) {
			unitMeterTextFieldX.getTextField().setText(unitDegreeTextFieldLongtitude.getTextField().getText());
			unitMeterTextFieldY.getTextField().setText(unitDegreeTextFieldLatitude.getTextField().getText());
		}
		setComponentVisible();
	}


	/**
	 * 自带“米”单位的textField
	 */
	class UnitMeterTextField extends JPanel {
		public SmTextFieldLegit getTextField() {
			return textField;
		}

		private SmTextFieldLegit textField;
		private JLabel labelUnitMeter;

		// 键盘限制输入事件
		private KeyListener keyAdapter = new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int keyChar = e.getKeyChar();
				// 度textField键盘输入限定
				String text = textField.getText();
				//“-”负号在首位，并且只能输入一次
				if (!StringUtilities.isNullOrEmpty(text) && keyChar == KeyEvent.VK_MINUS) {// keyChar == 45代表负号
					e.consume();
				}
				//“.”不能在首位，并且只能输入一次
				if (StringUtilities.isNullOrEmpty(text) && keyChar == KeyEvent.VK_PERIOD || text.contains(".") && keyChar == KeyEvent.VK_PERIOD) {//keyChar == 46代表小数点
					e.consume();
				}
				// 负号后面不能跟小数点
				if (keyChar == KeyEvent.VK_PERIOD && text.equals("-")) {
					e.consume();
				}
				// 限制只能输入数字、负号、小数点
				if (keyChar < 45 || keyChar > 57) {
					e.consume();
				}
				if (keyChar == KeyEvent.VK_SLASH) {
					e.consume();
				}
			}
		};

		/**
		 * 分、秒文本框正确值范围设置
		 */
		private ISmTextFieldLegit iSmTextFieldLegit = new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (StringUtilities.isNullOrEmpty(textFieldValue)) {
					return false;
				}
				try {
					// 判断是否为数字就好
					Double.valueOf(textFieldValue.replace("-", ""));
				} catch (Exception e) {
					return false;
				}
				return true;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		};


		public UnitMeterTextField() {
			initComponent();
			initLayout();
			registEvents();
		}

		private void initComponent() {
			this.textField = new SmTextFieldLegit("0");
			this.labelUnitMeter = new JLabel(CommonProperties.getString("String_DistanceUnit_Meter"));
		}

		private void initLayout() {
			this.setPreferredSize(new Dimension(100, 23));
			GroupLayout groupLayoutPanel = new GroupLayout(this);
			this.setLayout(groupLayoutPanel);
			// @formatter off
			groupLayoutPanel.setHorizontalGroup(groupLayoutPanel.createSequentialGroup()
					.addComponent(this.textField)
					.addComponent(this.labelUnitMeter));
			groupLayoutPanel.setVerticalGroup(groupLayoutPanel.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(this.textField)
					.addComponent(this.labelUnitMeter));
			// @formatter on
		}


		private void registEvents() {
			removeListener();
			this.textField.addKeyListener(this.keyAdapter);
			this.textField.setSmTextFieldLegit(iSmTextFieldLegit);

		}

		private void removeListener() {
			this.textField.removeKeyListener(this.keyAdapter);
			this.textField.removeEvents();
		}
	}

	/**
	 * 自带“度”单位的textField
	 */
	class UnitDegreeTextField extends JPanel {
		public SmTextFieldLegit getTextField() {
			return textField;
		}

		private SmTextFieldLegit textField;
		private JLabel labelUnitDegree;

		// 键盘限制输入事件
		private KeyListener keyAdapter = new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int keyChar = e.getKeyChar();
				// 度textField键盘输入限定
				String text = textField.getText();
				//“-”负号在首位，并且只能输入一次
				if (!StringUtilities.isNullOrEmpty(text) && keyChar == KeyEvent.VK_MINUS) {// keyChar == 45代表负号
					e.consume();
				}
				//“.”不能在首位，并且只能输入一次
				if (StringUtilities.isNullOrEmpty(text) && keyChar == KeyEvent.VK_PERIOD || text.contains(".") && keyChar == KeyEvent.VK_PERIOD) {//keyChar == 46代表小数点
					e.consume();
				}
				// 负号后面不能跟小数点
				if (keyChar == KeyEvent.VK_PERIOD && text.equals("-")) {
					e.consume();
				}
				// 限制只能输入数字、负号、小数点
				if (keyChar < 45 || keyChar > 57) {
					e.consume();
				}
				if (keyChar == KeyEvent.VK_SLASH) {
					e.consume();
				}
			}
		};

		/**
		 * 分、秒文本框正确值范围设置
		 */
		private ISmTextFieldLegit iSmTextFieldLegit = new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (StringUtilities.isNullOrEmpty(textFieldValue)) {
					return false;
				}
				try {
					Double value = Double.valueOf(textFieldValue.replace("-", ""));
					if (value > 180) {
						return false;
					}
				} catch (Exception e) {
					return false;
				}
				return true;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		};


		public UnitDegreeTextField() {
			initComponent();
			initLayout();
			registEvents();
		}

		private void initComponent() {
			this.textField = new SmTextFieldLegit("0");
			this.textField.setToolTipText(ControlsProperties.getString("String_RangeSection") + "[-180,180]");
			this.labelUnitDegree = new JLabel(CommonProperties.getString("String_AngleUnit_Degree"));
		}

		private void initLayout() {
			this.setPreferredSize(new Dimension(100, 23));
			GroupLayout groupLayoutPanel = new GroupLayout(this);
			this.setLayout(groupLayoutPanel);
			// @formatter off
			groupLayoutPanel.setHorizontalGroup(groupLayoutPanel.createSequentialGroup()
					.addComponent(this.textField)
					.addComponent(this.labelUnitDegree));
			groupLayoutPanel.setVerticalGroup(groupLayoutPanel.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(this.textField)
					.addComponent(this.labelUnitDegree));
			// @formatter on
		}


		private void registEvents() {
			removeListener();
			this.textField.addKeyListener(this.keyAdapter);
			this.textField.setSmTextFieldLegit(iSmTextFieldLegit);

		}

		private void removeListener() {
			this.textField.removeKeyListener(this.keyAdapter);
			this.textField.removeEvents();
		}
	}

}
