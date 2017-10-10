package com.supermap.desktop.WorkflowView.meta.dataconversion;

import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.ipls.ParameterCombine;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/9/28.
 */
public interface IImportParameterCreator<T> {
	//界面构建集合
	CopyOnWriteArrayList<IParameter> getParameterCombineArray(T o,String type);
	//方法，控件集合
	CopyOnWriteArrayList<ReflectInfo> getReflectInfoArray();
	//返回结果控件
	ParameterCombine getParameterCombineResultSet();
}
