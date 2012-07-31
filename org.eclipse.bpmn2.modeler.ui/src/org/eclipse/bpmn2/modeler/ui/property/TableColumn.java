package org.eclipse.bpmn2.modeler.ui.property;

import org.eclipse.bpmn2.modeler.core.utils.ErrorUtils;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.property.providers.ColumnTableProvider;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor.EDataTypeCellEditor;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

public class TableColumn extends ColumnTableProvider.Column implements ILabelProvider, ICellModifier {
	/**
	 * 
	 */
	private final AbstractListComposite abstractListComposite;
	protected TableViewer tableViewer;
	protected EStructuralFeature feature;
	protected EObject object;
	
	public TableColumn(AbstractListComposite abstractListComposite, EObject o, EStructuralFeature f) {
		this.abstractListComposite = abstractListComposite;
		object = o;
		feature = f;
	}
	
	public void setTableViewer(TableViewer t) {
		tableViewer = t;
	}
	
	@Override
	public String getHeaderText() {
		String text = "";
		if (feature!=null) {
			if (feature.eContainer() instanceof EClass) {
				EClass eclass = this.abstractListComposite.getListItemClass();
				text = PropertyUtil.getLabel(eclass, feature);
			}
			else
				text = ModelUtil.toDisplayName(feature.getName());
		}
		return text;
	}

	@Override
	public String getProperty() {
		if (feature!=null)
			return feature.getName(); //$NON-NLS-1$
		return "";
	}

	@Override
	public int getInitialWeight() {
		return 10;
	}

	public String getText(Object element) {
		if (element instanceof EObject) {
			return PropertyUtil.getDisplayName((EObject)element,feature);
		}
		Object value = ((EObject)element).eGet(feature);
		return value==null ? "" : value.toString();
	}
	
	public CellEditor createCellEditor (Composite parent) {
		if (feature!=null) {
			EClassifier ec = feature.getEType();
			if (ec instanceof EDataType) {
				return new EDataTypeCellEditor((EDataType)ec, parent);
			}
		}
		return null;
	}
	
	public boolean canModify(Object element, String property) {
		return this.abstractListComposite.columnProvider.canModify(object, feature, (EObject)element);
	}

	public void modify(Object element, String property, Object value) {
		BPMN2Editor bpmn2Editor = this.abstractListComposite.getDiagramEditor();
		final EObject target = (EObject)element;
		final Object newValue = value;
		final Object oldValue = target.eGet(feature); 
		if (oldValue==null || !oldValue.equals(value)) {
			TransactionalEditingDomain editingDomain = bpmn2Editor.getEditingDomain();
			editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
				@Override
				protected void doExecute() {
					target.eSet(feature, newValue);
				}
			});
			if (bpmn2Editor.getDiagnostics()!=null) {
				// revert the change and display error status message.
				ErrorUtils.showErrorMessage(bpmn2Editor.getDiagnostics().getMessage());
			}
			else
				ErrorUtils.showErrorMessage(null);
			tableViewer.refresh();
		}
	}

	@Override
	public Object getValue(Object element, String property) {
		return getText(element);
	}
}