package com.supermap.desktop.process.parameters.ParameterPanels.PrjTranslator;

import com.supermap.data.PrjCoordSys;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;

/**
 * Created by yuanR on 2017/10/12 0012.
 * 目标坐标系
 */
public class ParameterTargetCoordSys extends AbstractParameter {


	private PrjCoordSys targetPrjCoordSys = null;

	public ParameterTargetCoordSys() {

	}

	public void setTargetPrjCoordSys(PrjCoordSys prjCoordSys) {
		this.targetPrjCoordSys = prjCoordSys;
	}

	public PrjCoordSys getTargetPrjCoordSys() {
		return targetPrjCoordSys;
	}


	@Override
	public String getType() {
		return ParameterType.TARGET_COORDSYS;
	}
}