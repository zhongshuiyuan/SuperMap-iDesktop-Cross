package com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics.StatisticsField;

import com.supermap.analyst.spatialstatistics.StatisticsType;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldInfos;
import com.supermap.data.FieldType;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CellRenders.ListStatisticsTypeCellRender;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.FieldTypeUtilities;
import com.supermap.desktop.utilities.StatisticsTypeUtilities;
import com.supermap.desktop.utilities.TableUtilities;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import static com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics.StatisticsField.StatisticsFieldTableModel.COLUMN_FIELDTYPE;
import static com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics.StatisticsField.StatisticsFieldTableModel.COLUMN_STATISTICSTYPE;

/**
 * Created by hanyz on 2017/5/3.
 */
public class StatisticsFieldPanel extends JPanel {
	private DatasetVector dataset;
	private ArrayList<StatisticsFieldInfo> statisticsFieldInfoAll = new ArrayList<>();
	private ArrayList<StatisticsFieldInfo> statisticsFieldInfoIncluded = new ArrayList<>();
	private StatisticsFieldTableModel tableModel = new StatisticsFieldTableModel(null);

	//#region component
	private JScrollPane scrollPane;
	private SmSortTable table;
	private JToolBar toolBar;
	private SmButton buttonAdd;
	private SmButton buttonSelectAll;
	private SmButton buttonSelectInvert;
	private SmButton buttonDel;
	private JLabel labelStatisticsType;
	private JComboBox<StatisticsType> comboBoxStatisticsType;
	private boolean isSelecting = true;
	//#enregion component

	public StatisticsFieldPanel(DatasetVector dataset) {
		super();
		this.dataset = dataset;
		init();
	}

	public StatisticsType[] getStatisticsType() {
		ArrayList<StatisticsType> statisticsTypes = new ArrayList<>();
		if (tableModel != null) {
			ArrayList<StatisticsFieldInfo> statisticsFieldInfos = tableModel.getStatisticsFieldInfos();
			for (StatisticsFieldInfo statisticsFieldInfo : statisticsFieldInfos) {
				statisticsTypes.add(statisticsFieldInfo.getStatisticsType());
			}
		}
		return statisticsTypes.toArray(new StatisticsType[statisticsTypes.size()]);
	}

	public String[] getStatisticsFieldNames() {
		ArrayList<String> fieldNames = new ArrayList<>();
		if (tableModel != null) {
			ArrayList<StatisticsFieldInfo> statisticsFieldInfos = tableModel.getStatisticsFieldInfos();
			for (StatisticsFieldInfo statisticsFieldInfo : statisticsFieldInfos) {
				fieldNames.add(statisticsFieldInfo.getFieldInfo().getName());
			}
		}
		return fieldNames.toArray(new String[fieldNames.size()]);
	}

	public ArrayList<StatisticsFieldInfo> getStatisticsFieldInfos() {
		if (tableModel != null) {
			return tableModel.getStatisticsFieldInfos();
		}
		return null;
	}

	public DatasetVector getDataset() {
		return dataset;
	}

	public void setDataset(DatasetVector dataset) {
		this.dataset = dataset;
		//initTableModel重新设置model会触发combobox重构，其中用到了选择行，是无效的值
		this.table.clearSelection();
		this.initTableModel();
	}

	private void init() {
		initComponent();
		initResource();
		initLayout();
		registerListener();
	}

	private void initComponent() {
		toolBar = new JToolBar();
		buttonAdd = new SmButton();
		buttonSelectAll = new SmButton();
		buttonSelectInvert = new SmButton();
		buttonDel = new SmButton();
		labelStatisticsType = new JLabel(CommonProperties.getString("String_StatisticType"));
		comboBoxStatisticsType = new JComboBox<>();
		comboBoxStatisticsType.setPreferredSize(new Dimension(150, 23));
		comboBoxStatisticsType.setMaximumSize(new Dimension(150, 23));
		comboBoxStatisticsType.setRenderer(new ListStatisticsTypeCellRender());
		scrollPane = new JScrollPane();
		table = new SmSortTable();
		initTable();
	}

	private void initTable() {
		scrollPane.setViewportView(table);
		table.setModel(tableModel);
		//table render
		TableColumn column_statisticsType = table.getColumnModel().getColumn(COLUMN_STATISTICSTYPE);
		DefaultCellEditor cellEditorStatisticsType = new StatisticsTypeCellEditor();
		cellEditorStatisticsType.setClickCountToStart(2);
		column_statisticsType.setCellEditor(cellEditorStatisticsType);
		column_statisticsType.setCellRenderer(statisticsTypeCellRenderer);
		TableColumn column_fieldType = table.getColumnModel().getColumn(COLUMN_FIELDTYPE);
		column_fieldType.setCellRenderer(fieldTypeCellRenderer);
		initTableModel();
	}

	public void initTableModel() {
		//初始化 属性--统计类型 信息
		statisticsFieldInfoAll.clear();
		statisticsFieldInfoIncluded.clear();
		if (dataset != null) {
			FieldInfos fieldInfos = dataset.getFieldInfos();
			for (int i = 0; i < fieldInfos.getCount(); i++) {
				FieldInfo fieldInfo = fieldInfos.get(i);
				//只处理非系统字段，不包含二进制、日期型--参考.net来的
				if (!fieldInfo.isSystemField() && fieldInfo.getType() != FieldType.DATETIME && fieldInfo.getType() != FieldType.LONGBINARY) {
					boolean fieldInfoAddOnce = false;
					ArrayList<StatisticsType> supportedStatisticsType = getSupportedStatisticsType(fieldInfo.getType());
					for (StatisticsType statisticsType : supportedStatisticsType) {
						StatisticsFieldInfo statisticsFieldInfo = new StatisticsFieldInfo(fieldInfo, statisticsType);
						this.statisticsFieldInfoAll.add(statisticsFieldInfo);//添加所有合理的字段、统计组合
						if (!fieldInfoAddOnce) {
							this.statisticsFieldInfoIncluded.add(statisticsFieldInfo);//默认初始化表格只显示每个字段一次
							fieldInfoAddOnce = true;
						}
					}
				}
			}
		}
		tableModel.setStatisticsFieldInfos(statisticsFieldInfoIncluded);
		if (tableModel.getRowCount() > 0) {
			table.setRowSelectionInterval(0, 0);
			initStatisticsTypeComboBox();
		}
	}

	/**
	 * 获取字段类型所支持的统计方法
	 */
	public static ArrayList<StatisticsType> getSupportedStatisticsType(FieldType type) {
		ArrayList<StatisticsType> statisticsTypes = new ArrayList<>();
		if (type == FieldType.CHAR || type == FieldType.TEXT || type == FieldType.WTEXT) {
			statisticsTypes.add(StatisticsType.FIRST);
			statisticsTypes.add(StatisticsType.LAST);
		} else {
			statisticsTypes.add(StatisticsType.MAX);
			statisticsTypes.add(StatisticsType.MIN);
			statisticsTypes.add(StatisticsType.SUM);
			statisticsTypes.add(StatisticsType.MEAN);
			statisticsTypes.add(StatisticsType.MEDIAN);
			statisticsTypes.add(StatisticsType.FIRST);
			statisticsTypes.add(StatisticsType.LAST);
		}
		return statisticsTypes;
	}

	/**
	 * 获取字段类型所支持的统计方法，排除表格中已经添加的方法
	 */
	private ArrayList<StatisticsType> getStatisticsTypes(StatisticsFieldInfo statisticsFieldInfo) {
		ArrayList<StatisticsType> statisticsTypes = getSupportedStatisticsType(statisticsFieldInfo.getFieldInfo().getType());
		ArrayList<StatisticsFieldInfo> statisticsFieldInfos = ((StatisticsFieldTableModel) table.getModel()).getStatisticsFieldInfos();
		for (StatisticsFieldInfo statisticsFieldInfo1 : statisticsFieldInfos) {
			if (statisticsFieldInfo1 != statisticsFieldInfo
					&& statisticsFieldInfo1.getFieldInfo().getName().equals(statisticsFieldInfo.getFieldInfo().getName())) {
				statisticsTypes.remove(statisticsTypes.indexOf(statisticsFieldInfo1.getStatisticsType()));
			}
		}
		return statisticsTypes;
	}

	DefaultTableCellRenderer statisticsTypeCellRenderer = new DefaultTableCellRenderer() {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (component instanceof JLabel && value instanceof StatisticsType) {
				((JLabel) component).setText(StatisticsTypeUtilities.getStatisticsTypeName(((StatisticsType) value)));
			}
			return component;
		}
	};
	DefaultTableCellRenderer fieldTypeCellRenderer = new DefaultTableCellRenderer() {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (component instanceof JLabel && value instanceof FieldType) {
				((JLabel) component).setText(FieldTypeUtilities.getFieldTypeName(((FieldType) value)));
			}
			return component;
		}
	};

	private class StatisticsTypeCellEditor extends DefaultCellEditor {
		JComboBox comboBox;

		public StatisticsTypeCellEditor() {
			super(new JComboBox());
		}

		@Override
		public Object getCellEditorValue() {
			if (comboBox != null) {
				return comboBox.getSelectedItem();
			}
			return null;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			int realRow = table.convertRowIndexToModel(row);
			StatisticsFieldInfo statisticsFieldInfo = ((StatisticsFieldTableModel) table.getModel()).getRow(realRow);
			ArrayList<StatisticsType> statisticsTypes = getStatisticsTypes(statisticsFieldInfo);
			comboBox = (JComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
			Object item = comboBox.getSelectedItem();
			comboBox.setRenderer(new ListStatisticsTypeCellRender());
			comboBox.removeAllItems();
			for (StatisticsType statisticsType : statisticsTypes) {
				comboBox.addItem(statisticsType);
			}
			if (item != null) {
				comboBox.setSelectedItem(item);
			}
			return comboBox;
		}
	}

	private void initResource() {
		buttonAdd.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_AddItem.png"));
		buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		buttonSelectInvert.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		buttonDel.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));
	}

	private void initLayout() {
		initToolbar();
		this.setLayout(new GridBagLayout());
		this.add(toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));
		this.add(scrollPane, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
	}

	private void initToolbar() {
		toolBar.setFloatable(false);
		toolBar.add(buttonAdd);
		toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
		toolBar.add(buttonSelectAll);
		toolBar.add(buttonSelectInvert);
		toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
		toolBar.add(buttonDel);
		toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
		toolBar.add(labelStatisticsType);
		toolBar.add(comboBoxStatisticsType);
	}

	private void registerListener() {
		buttonAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilities.stopEditing(table);
				JDialogAdd jDialogAdd = new JDialogAdd();
				if (jDialogAdd.showDialog() == DialogResult.OK) {
					ArrayList<StatisticsFieldInfo> selectedInfos = jDialogAdd.getSelectedStatisticsFieldInfo();
					int count = 0;
					for (StatisticsFieldInfo selectedInfo : selectedInfos) {
						((StatisticsFieldTableModel) table.getModel()).addRow(selectedInfo);
						count++;
					}
					if (count != 0) {
						table.clearSelection();
						for (int i = table.getRowCount() - count; i <= table.getRowCount() - 1; i++) {
							int viewRow = table.convertRowIndexToView(i);
							table.addRowSelectionInterval(viewRow, viewRow);
						}
//						table.setRowSelectionInterval(table.getRowCount() - count, table.getRowCount() - 1);
						TableUtilities.scrollToLastSelectedRow(table);
					}
				}
			}
		});
		buttonSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				table.setRowSelectionInterval(0, table.getRowCount() - 1);
			}
		});
		buttonSelectInvert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilities.invertSelection(table);
			}
		});
		buttonDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedModelRows = table.getSelectedModelRows();
				if (selectedModelRows.length == 0) {
					return;
				}
//				Arrays.sort(selectedModelRows);
				for (int i = selectedModelRows.length - 1; i >= 0; i--) {
					tableModel.removeRow(selectedModelRows[i]);
				}
				int row = selectedModelRows[0];
				if (tableModel.getRowCount() <= row) {
					row = tableModel.getRowCount() - 1;
				}
				if (row != -1) {
					table.setRowSelectionInterval(row, row);
				}
			}
		});
		comboBoxStatisticsType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (isSelecting && e.getStateChange() == ItemEvent.SELECTED) {
					TableUtilities.stopEditing(table);
					StatisticsType statisticsType = (StatisticsType) comboBoxStatisticsType.getSelectedItem();
					int[] selectedModelRows = table.getSelectedModelRows();
					for (int selectedModelRow : selectedModelRows) {
						StatisticsFieldInfo statisticsFieldInfo = tableModel.getRow(selectedModelRow);
						ArrayList<StatisticsType> statisticsTypes = getStatisticsTypes(statisticsFieldInfo);
						if (statisticsTypes.contains(statisticsType)) {
							tableModel.setValueAt(statisticsType, selectedModelRow, COLUMN_STATISTICSTYPE);
						} else {
							tableModel.setValueAt(statisticsTypes.get(0), selectedModelRow, COLUMN_STATISTICSTYPE);
						}
					}
				}
			}
		});

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				checkState();
			}
		});
		tableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() != TableModelEvent.DELETE) {
					checkState();
				}
			}
		});
	}

	private void checkState() {
		buttonSelectAll.setEnabled(table.getRowCount() > 0);
		buttonSelectInvert.setEnabled(table.getRowCount() > 0);
		buttonDel.setEnabled(table.getSelectedRowCount() > 0);
		comboBoxStatisticsType.setEnabled(table.getSelectedRowCount() > 0);
		initStatisticsTypeComboBox();
	}

	private void initStatisticsTypeComboBox() {
		isSelecting = false;
		try {
			this.comboBoxStatisticsType.removeAllItems();
			int[] selectedModelRows = table.getSelectedModelRows();
			if (selectedModelRows.length == 0) {
				return;
			}
			StatisticsFieldInfo statisticsFieldInfoFirst = tableModel.getRow(selectedModelRows[0]);
			ArrayList<StatisticsType> statisticsTypesFirst = getStatisticsTypes(statisticsFieldInfoFirst);
			for (int selectedModelRow : selectedModelRows) {
				StatisticsFieldInfo statisticsFieldInfo = tableModel.getRow(selectedModelRow);
				ArrayList<StatisticsType> statisticsTypes = getStatisticsTypes(statisticsFieldInfo);
				statisticsTypesFirst.retainAll(statisticsTypes);
			}
			for (StatisticsType statisticsType : statisticsTypesFirst) {
				comboBoxStatisticsType.addItem(statisticsType);
			}
		} finally {
			isSelecting = true;
		}

	}


	private class JDialogAdd extends SmDialog {
		private SmSortTable table;
		private JToolBar toolBar;
		private SmButton buttonSelectAll;
		private SmButton buttonSelectInvert;
		private SmButton buttonOK = new SmButton();
		private SmButton buttonCancel = new SmButton();
		private JScrollPane scrollPane;
		private StatisticsFieldTableModel tableModel;

		private ArrayList<StatisticsFieldInfo> selectedInfos = new ArrayList<>();

		public JDialogAdd() {
			this.initComponent();
			this.initLayout();
			this.registerListener();
			this.pack();
			this.setLocationRelativeTo(null);
			buttonOK.requestFocus();
		}

		public ArrayList<StatisticsFieldInfo> getSelectedStatisticsFieldInfo() {
			return selectedInfos;
		}

		private void initComponent() {
			toolBar = new JToolBar();
			buttonAdd = new SmButton();
			buttonSelectAll = new SmButton();
			buttonSelectInvert = new SmButton();
			buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
			buttonSelectInvert.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
			buttonOK.setText(CommonProperties.getString(CommonProperties.OK));
			buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
			scrollPane = new JScrollPane();
			table = new SmSortTable();
			this.initTable();
		}

		private void initTable() {
			ArrayList<StatisticsFieldInfo> shownInfos = ((StatisticsFieldTableModel) StatisticsFieldPanel.this.table.getModel()).getStatisticsFieldInfos();
			ArrayList<StatisticsFieldInfo> unShownInfos = subtract(statisticsFieldInfoAll, shownInfos);
			this.tableModel = new StatisticsFieldTableModel(unShownInfos);
			this.tableModel.setColumnStatisticsTypeEditable(false);
			this.scrollPane.setViewportView(table);
			this.table.setModel(this.tableModel);

			//table render
			TableColumn column_statisticsType = this.table.getColumnModel().getColumn(COLUMN_STATISTICSTYPE);
			DefaultCellEditor cellEditorStatisticsType = new StatisticsTypeCellEditor();
			cellEditorStatisticsType.setClickCountToStart(2);
			column_statisticsType.setCellRenderer(statisticsTypeCellRenderer);
			TableColumn column_fieldType = this.table.getColumnModel().getColumn(COLUMN_FIELDTYPE);
			column_fieldType.setCellRenderer(fieldTypeCellRenderer);
		}

		private ArrayList<StatisticsFieldInfo> subtract(ArrayList<StatisticsFieldInfo> all, ArrayList<StatisticsFieldInfo> shown) {
			ArrayList<StatisticsFieldInfo> unShown = new ArrayList<>();
			unShown.addAll(all);
			if (shown == null) return unShown;
			for (StatisticsFieldInfo statisticsFieldInfo : shown) {
				if (unShown.indexOf(statisticsFieldInfo) > -1) {
					unShown.remove(unShown.indexOf(statisticsFieldInfo));
				}
			}
			return unShown;
		}

		private void initLayout() {
			this.initToolbar();

			JPanel panelButton = new JPanel();
			panelButton.setLayout(new GridBagLayout());
			panelButton.add(this.buttonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.EAST));
			panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.EAST).setInsets(0, 5, 0, 0));

			this.setLayout(new GridBagLayout());
			this.add(this.toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));
			this.add(this.scrollPane, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
			this.add(panelButton, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 20, 10, 10));
		}

		private void initToolbar() {
			this.toolBar.setFloatable(false);
			this.toolBar.add(this.buttonSelectAll);
			this.toolBar.add(this.buttonSelectInvert);
		}

		private void registerListener() {
			this.buttonSelectAll.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					table.setRowSelectionInterval(0, table.getRowCount() - 1);
				}
			});
			this.buttonSelectInvert.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					TableUtilities.invertSelection(table);
				}
			});
			this.buttonOK.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dialogResult = DialogResult.OK;
					int[] selectedModelRows = table.getSelectedModelRows();
					StatisticsFieldTableModel model = (StatisticsFieldTableModel) table.getModel();
					selectedInfos.clear();
					for (int selectedModelRow : selectedModelRows) {
						selectedInfos.add(model.getRow(selectedModelRow));
					}
					dispose();
				}
			});
			buttonCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dialogResult = DialogResult.CANCEL;
					dispose();
				}
			});
		}
	}

	/*public static void main(String[] args) {
		DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
		connectionInfo.setServer("C:\\Users\\hanyz\\Desktop\\test.udb");
		connectionInfo.setEngineType(EngineType.UDB);
		connectionInfo.setReadOnly(true);
		Datasource datasoure = new Datasource(EngineType.UDB);
		datasoure.open(connectionInfo);
		System.out.println("UDB connected:" + datasoure.isConnected());
		DatasetVector dataset = (DatasetVector) datasoure.getDatasets().get("BaseMap_R");

		JFrame jFrame = new JFrame();
		jFrame.setSize(new Dimension(600, 400));
		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		StatisticsFieldPanel panel = new StatisticsFieldPanel(dataset);
		jFrame.add(panel);
		jFrame.setVisible(true);
	}*/
}
