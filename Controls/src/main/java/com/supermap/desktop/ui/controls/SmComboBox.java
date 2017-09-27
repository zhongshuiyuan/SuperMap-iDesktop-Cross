package com.supermap.desktop.ui.controls;

import com.supermap.desktop.implement.DefaultComboBoxUI;

import javax.swing.*;
import java.util.Vector;

/**
 * Created by lixiaoyao on 2017/9/26.
 *
 * The JComboBox style is not flattened for style optimization
 */
public class SmComboBox<E> extends JComboBox {

	public SmComboBox() {
		super();
		setUI();
	}

	public SmComboBox(ComboBoxModel<E> aModel) {
		super(aModel);
		setUI();
	}

	public SmComboBox(E[] items) {
		super(items);
		setUI();
	}

	public SmComboBox(Vector<E> items) {
		super(items);
		setUI();
	}

	private void setUI() {
		this.setUI(new DefaultComboBoxUI());
	}
}
