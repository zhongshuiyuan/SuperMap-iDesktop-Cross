package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;

public abstract class MapSelectionChangeListener {
	public abstract void selectionChanged(Selection selection,Layer layer);
}
