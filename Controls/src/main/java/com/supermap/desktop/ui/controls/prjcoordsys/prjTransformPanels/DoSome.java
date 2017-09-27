package com.supermap.desktop.ui.controls.prjcoordsys.prjTransformPanels;

import com.supermap.data.PrjCoordSys;

/**
 * Created by yuanR on 2017/9/27 0027.
 * 主面板和目标坐标系面板間联动实现
 *
 */
public interface DoSome {
	void setOKButtonEnabled(boolean isEnabled);

	void setTargetPrjCoordSys(PrjCoordSys targetPrjCoordSys);

}
