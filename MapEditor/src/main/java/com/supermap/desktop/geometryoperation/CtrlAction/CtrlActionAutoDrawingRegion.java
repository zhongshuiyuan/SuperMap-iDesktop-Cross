package com.supermap.desktop.geometryoperation.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometryoperation.editor.AutoDrawingRegionEditor;
import com.supermap.desktop.geometryoperation.editor.IEditor;
/**
 * @author lixiaoyao
 */
public class CtrlActionAutoDrawingRegion extends CtrlActionEditorBase {
	private AutoDrawingRegionEditor editor=new AutoDrawingRegionEditor();
	public CtrlActionAutoDrawingRegion(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public IEditor getEditor() {
		return this.editor;
	}
}
