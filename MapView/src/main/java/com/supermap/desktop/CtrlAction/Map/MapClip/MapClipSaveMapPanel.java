package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.ProviderLabel.WarningOrHelpProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author YuanR
 *         地图裁剪功能—保存地图面板
 */
public class MapClipSaveMapPanel extends JPanel {


	private CompTitledPane compTitledPane;
	private JCheckBox resultMapCheckBox;
	private JLabel resultMapCaption;
	private WarningOrHelpProvider warningOrHelpProvider;
	private JTextField saveMapTextField;

	public JTextField getSaveMapTextField() {
		return saveMapTextField;
	}

	public JCheckBox getCheckBox() {
		return resultMapCheckBox;
	}

	private ActionListener checkBoxActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (resultMapCheckBox.isSelected()) {
				saveMapTextField.setEnabled(true);
			} else {
				saveMapTextField.setText("");
				saveMapTextField.setEnabled(false);
			}
		}
	};


	public CompTitledPane getCompTitledPane() {
		return compTitledPane;
	}

	public MapClipSaveMapPanel() {
		initComponent();
		initLayout();
		initResources();
		registEvents();

	}

	private void initComponent() {
		this.resultMapCheckBox = new JCheckBox("ResultMap");
		this.resultMapCaption = new JLabel("ResultMapCaption");
		this.warningOrHelpProvider = new WarningOrHelpProvider(MapViewProperties.getString("String_MapClip_SaveMap_Info"), false);
		this.saveMapTextField = new JTextField();
		this.saveMapTextField.setEnabled(false);
		this.compTitledPane = new CompTitledPane(this.resultMapCheckBox, this);
	}

	private void initLayout() {
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		//@formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addComponent(resultMapCaption)
				.addComponent(this.warningOrHelpProvider)
				.addComponent(this.saveMapTextField));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(resultMapCaption)
				.addComponent(this.warningOrHelpProvider)
				.addComponent(this.saveMapTextField,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE));
		//@formatter:on

	}

	private void initResources() {
		this.resultMapCheckBox.setText(MapViewProperties.getString("String_MapClip_SaveMap"));
		this.resultMapCaption.setText(MapViewProperties.getString("String_MapClip_ResultMapCaption"));
	}

	private void registEvents() {
		removeEvents();
		this.resultMapCheckBox.addActionListener(this.checkBoxActionListener);
	}

	private void removeEvents() {
		this.resultMapCheckBox.addActionListener(this.checkBoxActionListener);
	}
}
