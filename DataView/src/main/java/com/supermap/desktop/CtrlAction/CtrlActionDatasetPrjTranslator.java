package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogDatasetPrjTranslator;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created by yuanR on 2017/10/10 0010.
 * 数据集投影转换 CtrlAction
 */
public class CtrlActionDatasetPrjTranslator extends CtrlAction {

	public CtrlActionDatasetPrjTranslator(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		JDialogDatasetPrjTranslator dialogPrjTransform = new JDialogDatasetPrjTranslator();
		dialogPrjTransform.showDialog();
	}

	@Override
	public boolean enable() {
		return null != DatasetUtilities.getDefaultDataset();
	}

}