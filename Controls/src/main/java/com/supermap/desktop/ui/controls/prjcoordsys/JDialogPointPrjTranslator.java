package com.supermap.desktop.ui.controls.prjcoordsys;

import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.prjcoordsys.prjTransformPanels.PanelPointCoordSysTranslator;

/**
 * Created by yuanR on 2017/10/12 0012.
 */
public class JDialogPointPrjTranslator extends SmDialog {

	private PanelPointCoordSysTranslator panelPointCoordSysTranslator;

	public JDialogPointPrjTranslator() {

		initializeComponents();
		initializeResources();
		initializeLayout();
		initStates();
		initListener();

		this.add(panelPointCoordSysTranslator);

		setSize(800, 600);
		setLocationRelativeTo(null);
	}


	private void initializeComponents() {
		panelPointCoordSysTranslator = new PanelPointCoordSysTranslator();
	}

	private void initializeResources() {

	}

	private void initializeLayout() {

	}

	private void initStates() {
	}

	private void initListener() {
	}
}
