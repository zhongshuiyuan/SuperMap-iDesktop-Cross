package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogBatchPrjTranslator;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created by yuanR on 2017/10/10 0010.
 * 批量投影转换
 */
public class CtrlActionBatchPrjTranslator extends CtrlAction {

	public CtrlActionBatchPrjTranslator(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		JDialogBatchPrjTranslator dialogBatchPrjTranslator = new JDialogBatchPrjTranslator();
		dialogBatchPrjTranslator.showDialog();
	}

	@Override
	public boolean enable() {
		return null != DatasetUtilities.getDefaultDataset();
	}
}