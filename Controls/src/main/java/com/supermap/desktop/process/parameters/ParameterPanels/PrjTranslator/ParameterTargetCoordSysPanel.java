package com.supermap.desktop.process.parameters.ParameterPanels.PrjTranslator;

import com.supermap.data.PrjCoordSys;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.ui.controls.prjcoordsys.prjTransformPanels.DoSome;
import com.supermap.desktop.ui.controls.prjcoordsys.prjTransformPanels.PanelTargetCoordSys;

import java.awt.*;

/**
 * Created by yuanR on 2017/10/12 0012.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.TARGET_COORDSYS)
public class ParameterTargetCoordSysPanel extends PanelTargetCoordSys implements IParameterPanel {

	private ParameterTargetCoordSys parameterTargetCoordSys;

	private DoSome parameterDoSome = new DoSome() {
		@Override
		public void setOKButtonEnabled(boolean isEnabled) {
			//donothing
		}

		@Override
		public void setTargetPrjCoordSys(PrjCoordSys targetPrjCoordSys) {
			parameterTargetCoordSys.setTargetPrjCoordSys(getTargetPrjCoordSys());
		}
	};

	public ParameterTargetCoordSysPanel(IParameter parameterTargetCoordSys) {
		super(null);
		this.doSome = parameterDoSome;
		this.parameterTargetCoordSys = (ParameterTargetCoordSys) parameterTargetCoordSys;
		this.setPreferredSize(new Dimension(this.getWidth(), 280));
		this.setMinimumSize(new Dimension(this.getWidth(), 280));
		this.parameterTargetCoordSys.setTargetPrjCoordSys(getTargetPrjCoordSys());
	}

	@Override
	public Object getPanel() {
		return this;
	}
}

