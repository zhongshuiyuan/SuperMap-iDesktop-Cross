package com.supermap.desktop.CtrlAction.transformationForm.CtrlAction;

import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.PrjCoordSysType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogDatasetPrjTranslator;

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

		boolean enable = false;
		if (null != Application.getActiveApplication().getWorkspace().getDatasources() && Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0) {
			Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
			for (int i = 0; i < datasources.getCount(); i++) {
				Datasource tempDatasource = datasources.get(i);
				if (!tempDatasource.isReadOnly() && tempDatasource.getPrjCoordSys().getType() != PrjCoordSysType.PCS_NON_EARTH) {
					enable = true;
					break;
				}
			}
		}
		return enable;
	}

}