package org.eclipse.bpmn2.modeler.ui.property.tabs.tables;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;

public class TableColumnDescriptor {

	private String title;
	private int width;

	public TableColumnDescriptor(String title, int width) {
		this.title = title;
		this.width = width;
	}

	public TableColumnDescriptor() {
		this.title = null;
		this.width = 50;
	}

	public EditingSupport getEditingSupport(TableViewer viewer) {
		return null;
	}

	/**
	 * Returns the string value of an object
	 * 
	 * @return
	 */
	public ColumnLabelProvider getColumnLabelProvider() {
		ColumnLabelProvider labelProvider = new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				return String.valueOf(element);
			}
		};

		return labelProvider;
	}

	/**
	 * Configures the underlaying column
	 * 
	 * @param column
	 */
	public void configure(TableColumn column) {
		if (title != null) {
			column.setText(title);
		}
		
		column.setWidth(width);
		column.setResizable(true);
		column.setMoveable(true);
	}
}