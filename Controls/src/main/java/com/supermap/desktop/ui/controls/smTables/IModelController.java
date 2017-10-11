package com.supermap.desktop.ui.controls.smTables;

/**
 * Created by lixiaoyao on 2017/8/10.
 */
public interface IModelController {
	void selectedAll();

	void selectedIInverse();

	void delete(int row);

	void selectAllOrNull(boolean value);

	void selectedSystemField();

	void selectedNonSystemField();
}
