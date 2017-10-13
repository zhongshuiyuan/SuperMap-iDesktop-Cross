package com.supermap.desktop.ui.controls.prjcoordsys.prjTransformPanels;

import com.supermap.desktop.controls.ControlsProperties;

import javax.swing.*;

/**
 * Created by yuanR on 2017/10/12 0012.
 * 坐标点转换功能.坐标点键入面板
 */
public class PanelPointCoordSysTranslator extends JPanel {
	private JLabel labelXORLongtitude = new JLabel();
	private JLabel labelYORLatitude = new JLabel();

	private JTextField textFieldXORLongitudeValue = new JTextField();
	private JTextField textFieldYORLatitudeValue = new JTextField();

	private JCheckBox checkBoxShowAsDMS = new JCheckBox(ControlsProperties.getString("String_CheckBox_ShowAsDMS"));

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
								.addComponent(this.labelXORLongtitude)
								.addComponent(this.labelYORLatitude))
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.textFieldXORLongitudeValue)
								.addComponent(this.textFieldYORLatitudeValue)))
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(this.checkBoxShowAsDMS)));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelXORLongtitude)
						.addComponent(this.textFieldXORLongitudeValue, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelYORLatitude)
						.addComponent(this.textFieldYORLatitudeValue, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(this.checkBoxShowAsDMS))
		);
		// @formatter:on
	}

	private void initListener() {
	}

	public void initStates() {
		setLabelText();

	}

	/**
	 * 根据传入的投影坐标系统，设置Label控件的显示
	 */
	private void setLabelText() {
		if (false) {
			labelXORLongtitude.setText(ControlsProperties.getString("String_Label_LongitudeValue"));
			labelYORLatitude.setText(ControlsProperties.getString("String_Label_LatitudeValue"));
		} else {
			labelXORLongtitude.setText("X:");
			labelYORLatitude.setText("Y:");
		}
	}


}
