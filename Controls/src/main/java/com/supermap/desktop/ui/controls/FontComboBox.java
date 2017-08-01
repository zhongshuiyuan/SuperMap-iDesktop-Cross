package com.supermap.desktop.ui.controls;

import com.supermap.desktop.utilities.SystemPropertyUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * 字体下拉列表
 *
 * @author xuzw
 */
public class FontComboBox extends JComboBox {

	private static final long serialVersionUID = 1L;
	private static final String[] LINUX_SUPPORT_FONT_NAME = new String[]{
			"arial", "ARIALUNI", "cour", "estre", "framd", "gautami", "georgia", "gulim", "impact", "l_10546",
			"mingliu", "msgothic", "msmincho", "MSYH", "mvboli", "nina", "raavi", "segoe", "shruti", "smfang",
			"simhei", "simkai", "SIMLI", "simsun", "SIMYOU", "SuperMap 500 Symbols1", "SuperMap 500 Symbols2", "Supermap Animal Symbol", "SuperMap Personage Symbol", "SuperMap Plant Symbol",
			"SuperMap Sports Symbol", "SuperMap Symbol", "SuperMap Tools Symbol", "SuperMap Traffic Symbol", "sylfaen", "tahoma", "times", "tunga", "verdana", "WenQuanyi Micro Hei"
	};

	public FontComboBox() {
		super();
		FontCellRenderer cellRenderer = new FontCellRenderer();
		cellRenderer.setPreferredSize(new Dimension(60, 22));
		this.setRenderer(cellRenderer);

		if (SystemPropertyUtilities.isWindows()) {
			Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
			for (int i = 0; i < fonts.length; i++) {
				if (!fonts[i].getName().contains("Bold")) {
					this.addItem(fonts[i].getName());
				}
			}
		} else {
			for (String fontName : LINUX_SUPPORT_FONT_NAME) {
				this.addItem(fontName);
			}
		}

	}
}

class FontCellRenderer extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = 1L;

	public FontCellRenderer() {
		setOpaque(true);
		setHorizontalAlignment(LEFT);
		setVerticalAlignment(CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		String fontName = (String) value;
		Font font = new Font(fontName, 0, 12);
		this.setText(fontName);
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		// add by xuzw 2010-07-22 有的字体是不能显示的，当不能显示时会出现乱码，所以如果一个
		// 字体不能显示，就指定为Monospaced（必须受所有 Java 运行时环境支持的 Java 平台所定义的一种字体）
		if (font.canDisplayUpTo(font.getName()) == -1) {
			this.setFont(font);
		} else {
			font = new Font("Monospaced", 0, 12);
			this.setFont(font);
		}
		return this;
	}
}
