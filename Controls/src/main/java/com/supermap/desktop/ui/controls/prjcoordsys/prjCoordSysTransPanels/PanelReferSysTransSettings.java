package com.supermap.desktop.ui.controls.prjcoordsys.prjCoordSysTransPanels;

import com.supermap.data.CoordSysTransMethod;
import com.supermap.data.CoordSysTransParameter;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogPrjCoordSysTranslatorSettings;
import com.supermap.desktop.utilities.CoordSysTransMethodUtilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by yuanR on 2017/9/25 0025.
 * 参照系转换参数设置
 */
public class PanelReferSysTransSettings extends JPanel {
	private JLabel labelMethod;
	private JComboBox<String> comboBoxMethod;
	private JLabel labelPrjTransParameterset;
	private JButton buttonSet;
	private transient CoordSysTransMethod method = CoordSysTransMethod.MTH_GEOCENTRIC_TRANSLATION;
	private transient CoordSysTransParameter parameter = new CoordSysTransParameter();

	private JDialogPrjCoordSysTranslatorSettings dialogPrjCoordSysTranslatorSettings;

	public PanelReferSysTransSettings() {

		initComponents();
		initializeResources();
		initLayout();
		initListener();
	}


	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (dialogPrjCoordSysTranslatorSettings == null) {
				dialogPrjCoordSysTranslatorSettings = new JDialogPrjCoordSysTranslatorSettings(parameter);
			} else {
				dialogPrjCoordSysTranslatorSettings.setVisible(true);
			}
			if (dialogPrjCoordSysTranslatorSettings.getDialogResult() == DialogResult.OK) {
				System.out.println("669");
				//parameter = dialogPrjCoordSysTranslatorSettings.getParameter();
			}
		}
	};

	private transient ItemListener itemListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				method = CoordSysTransMethodUtilities.valueOf(comboBoxMethod.getSelectedItem().toString());
			}
		}
	};


	private void initComponents() {
		this.labelMethod = new JLabel("Method:");
		this.labelPrjTransParameterset = new JLabel("PrjTransParameterset:");

		this.comboBoxMethod = new JComboBox<>();
		this.comboBoxMethod.removeAllItems();
		this.comboBoxMethod.addItem(CoordSysTransMethodUtilities.toString(CoordSysTransMethod.MTH_GEOCENTRIC_TRANSLATION));
		this.comboBoxMethod.addItem(CoordSysTransMethodUtilities.toString(CoordSysTransMethod.MTH_MOLODENSKY));
		this.comboBoxMethod.addItem(CoordSysTransMethodUtilities.toString(CoordSysTransMethod.MTH_MOLODENSKY_ABRIDGED));
		this.comboBoxMethod.addItem(CoordSysTransMethodUtilities.toString(CoordSysTransMethod.MTH_POSITION_VECTOR));
		this.comboBoxMethod.addItem(CoordSysTransMethodUtilities.toString(CoordSysTransMethod.MTH_COORDINATE_FRAME));
		this.comboBoxMethod.addItem(CoordSysTransMethodUtilities.toString(CoordSysTransMethod.MTH_BURSA_WOLF));
		this.comboBoxMethod.setSelectedItem(CoordSysTransMethodUtilities.toString(this.method));

		this.buttonSet = new JButton();
	}

	private void initializeResources() {
		this.labelMethod.setText(ControlsProperties.getString("String_Label_CoordSysTranslatorMethod"));
		this.labelPrjTransParameterset.setText(ControlsProperties.getString("String_Label_CoordSysTranslatorParam"));
		this.buttonSet.setText(ControlsProperties.getString("String_SetProjection_Caption"));
	}

	private void initLayout() {

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelMethod)
						.addComponent(this.labelPrjTransParameterset))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.comboBoxMethod)
						.addComponent(this.buttonSet)));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelMethod)
						.addComponent(this.comboBoxMethod, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelPrjTransParameterset)
						.addComponent(this.buttonSet, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		// @formatter:on
	}

	private void initListener() {
		removeListener();
		this.comboBoxMethod.addItemListener(itemListener);
		this.buttonSet.addActionListener(actionListener);
	}

	private void removeListener() {
		this.comboBoxMethod.removeItemListener(itemListener);
		this.buttonSet.removeActionListener(actionListener);
	}
}
