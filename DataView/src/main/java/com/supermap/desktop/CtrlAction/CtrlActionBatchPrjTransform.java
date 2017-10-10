package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogBatchPrjTransform;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created by yuanR on 2017/10/10 0010.
 * 批量投影转换
 */
public class CtrlActionBatchPrjTransform extends CtrlAction {

	public CtrlActionBatchPrjTransform(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		JDialogBatchPrjTransform dialogBatchPrjTransform = new JDialogBatchPrjTransform();
		dialogBatchPrjTransform.showDialog();
	}

	@Override
	public boolean enable() {
		return null != DatasetUtilities.getDefaultDataset();
	}
}