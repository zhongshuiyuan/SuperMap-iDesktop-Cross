package com.supermap.desktop.lbs.params;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-06-26.
 */
public class CommonSettingCombine {
	private String name;
	private String value;
	private ArrayList<CommonSettingCombine> values = new ArrayList<>();

	public CommonSettingCombine(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public void add(CommonSettingCombine... commonSettingCombines) {
		for (CommonSettingCombine commonSettingCombine : commonSettingCombines) {
			values.add(commonSettingCombine);
		}
	}

	private String getJson() {
		StringBuilder stringBuffer = new StringBuilder();
		stringBuffer.append("\"");
		stringBuffer.append(name);
		stringBuffer.append("\":");
		stringBuffer.append(getJsonValue());
		return stringBuffer.toString();
	}

	private String getJsonValue() {
		StringBuilder stringBuffer = new StringBuilder();

		if (values.size() <= 0) {
			stringBuffer.append("\"");
			stringBuffer.append(value);
			stringBuffer.append("\"");

		} else {
			stringBuffer.append("{");
			for (int i = 0; i < values.size(); i++) {
				CommonSettingCombine commonSettingCombine = values.get(i);
				stringBuffer.append(commonSettingCombine.getJson());
				if (i != values.size() - 1) {
					stringBuffer.append(",");
				}
			}
			stringBuffer.append("}");
		}
		return stringBuffer.toString();
	}

	public String getFinalJSon() {
		return getJsonValue();
	}
}
