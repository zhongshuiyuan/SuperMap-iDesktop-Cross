package com.supermap.desktop.CtrlAction.LayerSetting;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapview.MapViewProperties;

/**
 * Created by lixiaoyao on 2017/10/11.
 */
public class CtrlActionCreateLayerGroup extends CtrlAction{

	public CtrlActionCreateLayerGroup(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_CurrentFunctionDeveloping"));
	}

	@Override
	public boolean enable() {
		return true;
	}
}
