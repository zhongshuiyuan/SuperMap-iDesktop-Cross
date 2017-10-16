package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.FormManager;

/**
 * Created by lixiaoyao on 2017/10/11.
 */
public class CtrlActionCloseOtherWindows extends CtrlAction {

	public CtrlActionCloseOtherWindows(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		int formsCount = Application.getActiveApplication().getMainFrame().getFormManager().getCount();
		IForm[] otherForms = new IForm[formsCount - 1];
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		int t = 0;
		for (int i = 0; i < formsCount; i++) {
			if (Application.getActiveApplication().getMainFrame().getFormManager().get(i).equals(activeForm)) {
				continue;
			}
			otherForms[t++] = Application.getActiveApplication().getMainFrame().getFormManager().get(i);
		}
		((FormManager) Application.getActiveApplication().getMainFrame().getFormManager()).closeForms(otherForms, true);
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (Application.getActiveApplication().getMainFrame().getFormManager().getCount() > 1) {
			enable = true;
		}
		return enable;
	}
}
