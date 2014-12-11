package org.camunda.bpm.modeler.ui.property.tabs.tables;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.ui.property.tabs.util.Events;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
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

	protected Object getEValue(T element) {
		return element.eGet(feature);
	}

	protected void setValue(T element, Object value) {
		Object val = toEValue(value);
		Object oldVal = getEValue(element);
		
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(element);
		
		final T elementRef = element;
		
		if (val == null) {
			editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
				
				@Override
				protected void doExecute() {
					elementRef.eUnset(feature);
				}
			});
		}
		else if ((val != oldVal) || !val.equals(oldVal)) {
			ModelUtil.setValue(editingDomain, element, feature, val);
		}
		
		viewer.update(element, null);
	}
	
	protected Object toEValue(Object value) {
		if (value instanceof String) {
			String str = (String) value;

			if (str.trim().isEmpty()) {
				return null;
			} else {
				return str;
			}
		}
		
		return value;
	}

	/**
	 * Provides update events for cell value
	 */
	@Override
	protected void saveCellEditorValue(CellEditor cellEditor, ViewerCell cell) {
		if (!cellEditor.isDirty()) {
			return;
		}
		
		T element = (T) cell.getElement();
		Object oldValue = getEValue(element);
		
		callSuperCellEditorValue(cellEditor, cell);
		
		Object newValue = getEValue(element);
		
		viewer.getTable().notifyListeners(Events.CELL_VALUE_CHANGED, new Events.CellValueChanged<T>(cell, element, newValue, oldValue));
	}
	
	protected void callSuperCellEditorValue(CellEditor cellEditor, ViewerCell cell) {
		super.saveCellEditorValue(cellEditor, cell);
	}
	
	@Override
	protected void setValue(Object element, Object value) {
		setValue((T) element, value);
	}

	@Override
	protected Object getValue(Object element) {
		return getValue((T) element);
	}
	
	public TableViewer getTableViewer() {
		return viewer;
	}
	
}