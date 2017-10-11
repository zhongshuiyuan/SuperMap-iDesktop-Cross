package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogDatasetPrjTransform;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created by yuanR on 2017/10/10 0010.
 * 数据集投影转换 CtrlAction
 */
public class CtrlActionDatasetPrjTransform extends CtrlAction {

	public CtrlActionDatasetPrjTransform(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		JDialogDatasetPrjTransform dialogPrjTransform = new JDialogDatasetPrjTransform();
		dialogPrjTransform.showDialog();
	}

	@Override
	public boolean enable() {
		return null != DatasetUtilities.getDefaultDataset();
	}

}