package com.supermap.desktop.ui.mdi.MdiTabsContextMenu;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

/**
 * Created by lixiaoyao on 2017/10/11.
 */
public class CtrlActionCloseAllTab extends CtrlAction {

	public CtrlActionCloseAllTab(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		Application.getActiveApplication().getMainFrame().getFormManager().closeAll(true);
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (Application.getActiveApplication().getMainFrame().getFormManager().getCount() > 0) {
			enable = true;
		}
		return enable;
	}
}
