package com.supermap.desktop.WorkflowView.meta.dataconversion;

import com.supermap.data.conversion.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.implement.UserDefineType.ImportSettingExcel;
import com.supermap.desktop.implement.UserDefineType.ImportSettingGPX;

/**
 * Created by xie on 2017/3/31.
 * 根据类型获得相应的导入类
 */
public class ImportSettingCreator implements IImportSettingCreator {
	private final String importSetting = "com.supermap.data.conversion.ImportSetting";

	@Override
	public ImportSetting create(Object o) {
		ImportSetting result = null;
		// o is an import file type
		try {
			String type = o.toString();
			if ("GPS".equalsIgnoreCase(type)) {
				return new ImportSettingGPX();
			}
			if ("EXCEL".equalsIgnoreCase(type)) {
				return new ImportSettingExcel();
			}

			if ("B".equalsIgnoreCase(type)) {
				type = "TEMSClutter";
			} else if ("DEM".equalsIgnoreCase(type)) {
				type = "GBDEM";
			} else if ("3DS".equalsIgnoreCase(type) || "X".equalsIgnoreCase(type)) {
				type = "Model" + type.toUpperCase();
			} else if ("OSGB".equalsIgnoreCase(type)) {
				type = "ModelOSG";
			} else if ("TXT".equalsIgnoreCase(type)) {
				type = "GRD";
			} else if ("WAL".equalsIgnoreCase(type) || "WAP".equalsIgnoreCase(type) || "WAT".equalsIgnoreCase(type) || "WAN".equalsIgnoreCase(type)) {
				type = "MAPGIS";
			} else if ("JPK".equalsIgnoreCase(type)) {
				type = "JP2";
			} else if ("SID".equalsIgnoreCase(type)) {
				type = "MrSID";
			} else if ("TIFF".equalsIgnoreCase(type)) {
				type = "TIF";
			} else if ("JPEG".equalsIgnoreCase(type)) {
				type = "JPG";
			} else if ("GRD_DEM".equalsIgnoreCase(type)) {
				type = "GRD";
			} else if ("GEOJSON".equalsIgnoreCase(type)) {
				type = "GeoJson";
			} else if ("SIMPLEJSON".equalsIgnoreCase(type)) {
				// 增加SimpleJson类型-yuanR2017.9.1
				type = "SimpleJson";
			}
			Class importClass = Class.forName(importSetting + type);
			result = (ImportSetting) importClass.newInstance();
			if (result instanceof ImportSettingWOR) {
				((ImportSettingWOR) result).setTargetWorkspace(Application.getActiveApplication().getWorkspace());
			} else if (result instanceof ImportSettingKML) {
				((ImportSettingKML) result).setImportEmptyDataset(true);
			} else if (result instanceof ImportSettingKMZ) {
				((ImportSettingKMZ) result).setImportEmptyDataset(true);
			} else if (result instanceof ImportSettingCSV) {
				((ImportSettingCSV) result).setImportEmptyDataset(true);
			} else if (result instanceof ImportSettingDGN) {
				((ImportSettingDGN) result).setImportEmptyDataset(true);
			} else if (result instanceof ImportSettingGeoJson) {
				((ImportSettingGeoJson) result).setImportEmptyDataset(true);
			} else if (result instanceof ImportSettingSimpleJson) {
				((ImportSettingSimpleJson) result).setImportEmptyDataset(true);
			}
		} catch (ClassNotFoundException e) {
			// 没有找到该类
			Application.getActiveApplication().getOutput().output(e);
		} catch (IllegalAccessException e) {
			//安全权限异常,一般来说,是由于java在反射时调用了private方法所导致的
		} catch (InstantiationException e) {
			// 出现这种异常的原因通常情况下是由于要实例化的对象是一个接口或者是抽象类等无法被实例化的类
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

}
