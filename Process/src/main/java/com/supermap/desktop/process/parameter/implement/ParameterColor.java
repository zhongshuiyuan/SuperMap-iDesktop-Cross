package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.awt.*;
import java.beans.PropertyChangeEvent;

/**
 * Created by lixiaoyao on 2017/7/6.
 */
public class ParameterColor extends AbstractParameter implements ISelectionParameter {
	@ParameterField(name = "value")
	private Object value;
	private String describe;
	public ParameterColor(String describe) {
		this.describe = describe;
	}

	public ParameterColor() {
		this("");
	}

	@Override
	public String getType() {
		return ParameterType.COLOR;
	}


	@Override
	public void setSelectedItem(Object value) {
		if (value instanceof Color) {
			Object oldValue = this.value;
			this.value = (Object) value;
			firePropertyChangeListener(new PropertyChangeEvent(this,"value", oldValue, this.value));
		}
	}

	@Override
	public Object getSelectedItem() {
		return this.value;
	}

	public String getDescribe() {
		return describe;
	}

	public ParameterColor setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

	@Override
	public void dispose() {

	}

	public Color getInitColor(){
		return (Color) this.value;
	}
}
