package com.supermap.desktop.ui.controls.prjcoordsys.prjCoordSysTransPanels;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created by yuanR on 2017/9/25 0025.
 * 坐标系信息面板
 */
public class PanelCoordSysInfo extends JPanel {
	private JLabel labelCoordInfo;
	private JTextArea textAreaCoordInfo;
	private String coordInfo = "";

	public PanelCoordSysInfo(String text) {
		this.coordInfo = text;
		initComponents();
		initLayout();
	}

	private void initComponents() {
		this.labelCoordInfo = new JLabel("CoordInfo:");
		this.labelCoordInfo.setText(ControlsProperties.getString("String_ProjectionInfoControl_LabelProjectionInfo"));
		this.textAreaCoordInfo = new JTextArea();
		this.textAreaCoordInfo.setEditable(false);
		setCoordInfo(this.coordInfo);
	}

	private void initLayout() {
		JScrollPane scrollPane = new JScrollPane(this.textAreaCoordInfo);

		this.setLayout(new GridBagLayout());
		this.add(labelCoordInfo, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 0, 10));
		this.add(scrollPane, new GridBagConstraintsHelper(0, 1, 1, 3).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 10, 10));
	}

	/**
	 * 设置坐标系信息
	 *
	 * @param text
	 */
	public void setCoordInfo(String text) {
		this.coordInfo = text;
		textAreaCoordInfo.setText(coordInfo);
	}
}
