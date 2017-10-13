package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.mdi.MdiTabsContextMenu.MdiTabContextMenuUtilities;

/**
 * Created by lixiaoyao on 2017/10/11.
 */
public class CtrlActionCloseCurrentWindows extends CtrlAction {

	public CtrlActionCloseCurrentWindows(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		MdiTabContextMenuUtilities.getMdiPage().close();
	}

	@Override
	public boolean enable() {
		return MdiTabContextMenuUtilities.isPopupMenuVisible();
	}
}
