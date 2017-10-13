package com.supermap.desktop.ui.mdi.MdiTabsContextMenu;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IContextMenuManager;
import com.supermap.desktop.ui.mdi.MdiPage;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * Created by lixiaoyao on 2017/10/11.
 */
public class MdiTabContextMenuUtilities {

	private static JPopupMenu mdiTabPopupMenu = null;
	private static MdiPage mdiPage = null;

	public static void showMdiTabsContextMenu(MouseEvent e, MdiPage page) {
		mdiPage = page;
		if (mdiTabPopupMenu == null) {
			IContextMenuManager manager = Application.getActiveApplication().getMainFrame().getContextMenuManager();
			mdiTabPopupMenu = (JPopupMenu) manager.get(getContextMenuId(page));
		}
		mdiTabPopupMenu.show(mdiPage.getGroup(), e.getX(), e.getY());
	}

	private static String getContextMenuId(MdiPage page) {
		// TODO: 2017/10/12 类型区分
		return "SuperMap.Desktop.UI.Mdi.MdiTabContextMenu";
	}

	public static boolean isPopupMenuVisible() {
		return mdiTabPopupMenu != null && mdiTabPopupMenu.isVisible();
	}

	public static MdiPage getMdiPage() {
		return mdiPage;
	}
}
