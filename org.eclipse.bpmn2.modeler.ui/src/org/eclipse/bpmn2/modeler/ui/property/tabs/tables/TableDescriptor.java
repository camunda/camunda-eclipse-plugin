package org.eclipse.bpmn2.modeler.ui.property.tabs.tables;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public abstract class TableDescriptor<T> {
	
	private List<TableColumnDescriptor> columns;

	/**
	 * Sets the table column descriptors
	 * 
	 * @param columns
	 */
	public void setColumns(TableColumnDescriptor ... columns) {
		this.columns = Arrays.asList(columns);
	}
	
	/**
	 * Sets the table column descriptors
	 * 
	 * @param columns
	 */
	public void setColumns(List<TableColumnDescriptor> columns) { 
		this.columns = columns;
	}
	
	/**
	 * Returns the table column descriptors for the table
	 * 
	 * @param columns
	 */
	public List<TableColumnDescriptor> getColumns() {
		return columns;
	}
	
	/**
	 * Returns the content provider for the table.
	 * 
	 * @return
	 */
	public IStructuredContentProvider getContentProvider() {
		return ArrayContentProvider.getInstance();
	}
	
	public abstract List<T> getValues();
	
	public void configure(Table table) {
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}
	
	/**
	 * Creates a new table viewer from the given descriptor
	 * 
	 * @return
	 */
	public TableViewer createTableViewer(Composite parent) {
		return createTableViewer(parent, this);
	}
	

	/**
	 * Creates a table viewer on the specified parent, defined by the 
	 * passed table descriptor.
	 * 
	 * @param parent
	 * @param tableDescriptor
	 * @return
	 */
	private static TableViewer createTableViewer(Composite parent, TableDescriptor<?> tableDescriptor) {

		TableViewer viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER
				| SWT.FULL_SELECTION);
		
		Table table = viewer.getTable();
		
		tableDescriptor.configure(table);
		
		createColumns(viewer, tableDescriptor.getColumns());

		viewer.setContentProvider(tableDescriptor.getContentProvider());
		viewer.setInput(tableDescriptor.getValues());
		
		return viewer;
	}

	/**
	 * Create columns for the table
	 * 
	 * @param viewer
	 * @param tableColumnDescriptors
	 */
	private static <T extends EObject> void createColumns(final TableViewer viewer,
			final List<TableColumnDescriptor> tableColumnDescriptors) {

		for (TableColumnDescriptor descriptor : tableColumnDescriptors) {
			TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);

			TableColumn column = viewerColumn.getColumn();
			descriptor.configure(column);

			viewerColumn.setLabelProvider(descriptor.getColumnLabelProvider());
			viewerColumn.setEditingSupport(descriptor.getEditingSupport(viewer));
		}
	}
}