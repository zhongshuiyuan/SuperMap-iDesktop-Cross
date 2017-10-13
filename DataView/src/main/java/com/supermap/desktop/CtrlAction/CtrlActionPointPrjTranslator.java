package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogPointPrjTranslator;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created by yuanR on 2017/10/12 0012.
 */
public class CtrlActionPointPrjTranslator extends CtrlAction {


	public CtrlActionPointPrjTranslator(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		JDialogPointPrjTranslator dialogPointPrjTranslator = new JDialogPointPrjTranslator();
		dialogPointPrjTranslator.showDialog();
	}

	@Override
	public boolean enable() {
		return null != DatasetUtilities.getDefaultDataset();
	}
}
