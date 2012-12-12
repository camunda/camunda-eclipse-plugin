package org.eclipse.bpmn2.modeler.ui.property.tabs.tables;

import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;

/**
 * Simple editing support for eobjects
 * 
 * @author nico.rehwaldt
 */
public class EObjectAttributeEditingSupport<T extends EObject> extends EditingSupport {

	private TableViewer viewer;
	private EStructuralFeature feature;

	public EObjectAttributeEditingSupport(TableViewer viewer, EStructuralFeature feature) {
		super(viewer);

		this.viewer = viewer;
		this.feature = feature;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor(viewer.getTable());
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	protected String getValue(T element) {
		Object value = getEValue(element);
		
		if (value == null) {
			return "";
		} else {
			return String.valueOf(value);
		}
	}

	private Object getEValue(T element) {
		return element.eGet(feature);
	}

	protected void setValue(T element, String value) {

		if (value == null || value.trim().isEmpty()) {
			value = null;
		}
		
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(element);

		ModelUtil.setValue(editingDomain, element, feature, value);
		viewer.update(element, null);
	}
	
	/**
	 * Provides update events for cell value
	 */
	@Override
	protected void saveCellEditorValue(CellEditor cellEditor, ViewerCell cell) {
		T element = (T) cell.getElement();
		Object oldValue = getEValue(element);
		
		super.saveCellEditorValue(cellEditor, cell);
		
		Object newValue = getEValue(element);
		
		viewer.getTable().notifyListeners(Events.CELL_VALUE_CHANGED, new Events.CellValueChanged<T>(cell, element, newValue, oldValue));
	}
	
	@Override
	protected final void setValue(Object element, Object value) {
		setValue((T) element, (String) value);
	}

	@Override
	protected final Object getValue(Object element) {
		return getValue((T) element);
	}

}