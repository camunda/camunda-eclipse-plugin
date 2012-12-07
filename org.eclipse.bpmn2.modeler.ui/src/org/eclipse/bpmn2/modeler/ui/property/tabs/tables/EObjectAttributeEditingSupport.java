package org.eclipse.bpmn2.modeler.ui.property.tabs.tables;

import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * Simple editing support
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
		Object value = element.eGet(feature);
		
		if (value == null) {
			return "";
		} else {
			return String.valueOf(value);
		}
	}

	protected void setValue(T element, String value) {

		if (value == null || value.trim().isEmpty()) {
			value = null;
		}
		
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(element);

		ModelUtil.setValue(editingDomain, element, feature, value);
		viewer.update(element, null);
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