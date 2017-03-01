package com.supermap.desktop.process;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

/**
 * @author XiaJT
 */
public class ParameterManager extends JPanel {
	private JPanel mainPanel = new JPanel();

	public ParameterManager() {
		Field[] fields = mainPanel.getClass().getFields();
		this.setLayout(new GridBagLayout());
		mainPanel.setLayout(new GridBagLayout());
		this.add(mainPanel, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
	}

	public void setProcess(IProcess process) {
		mainPanel.removeAll();
		if (process != null) {
			JComponent component = process.getComponent();
			mainPanel.add(component, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
		}
		mainPanel.revalidate();
		mainPanel.repaint();
		this.revalidate();
		this.repaint();
	}
}