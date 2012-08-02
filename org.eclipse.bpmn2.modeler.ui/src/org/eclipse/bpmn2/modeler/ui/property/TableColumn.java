package org.eclipse.bpmn2.modeler.ui.property;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.utils.ErrorUtils;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.property.providers.ColumnTableProvider;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor.EDataTypeCellEditor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class TableColumn extends ColumnTableProvider.Column implements ILabelProvider, ICellModifier {

	protected AbstractListComposite listComposite;
	protected TableViewer tableViewer;
	// the underlying EObject of the table row
	protected EObject object;
	// the EStructuralFeature being managed for this table column
	protected EStructuralFeature feature;
	// The column cell editor
	protected CellEditor cellEditor = null;
	protected boolean editable = true;

	public TableColumn(EObject o, EStructuralFeature f) {
		this(null,o,f);
	}

	public TableColumn(AbstractListComposite abstractListComposite, EObject o, EStructuralFeature f) {
		this.listComposite = abstractListComposite;
		object = o;
		feature = f;
	}
	
	public void setOwner(AbstractListComposite abstractListComposite) {
		this.listComposite = abstractListComposite;
	}
	
	public void setTableViewer(TableViewer t) {
		tableViewer = t;
	}
	
	@Override
	public String getHeaderText() {
		String text = "";
		if (feature!=null) {
			if (feature.eContainer() instanceof EClass) {
				EClass eclass = this.listComposite.getListItemClass();
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
		return element.toString();
	}
	
	protected int getColumnIndex() {
		return listComposite.getColumnProvider().getColumns().indexOf(this);
	}
	
// NOTE: in certain cases we can't create a cell editor until we have the object
// that the editor will be modifying - this happens very late in the cell editing
// lifecycle in canModify() - this is where the cell editor needs to be constructed.
//
//	public CellEditor createCellEditor (Composite parent) {
//		if (cellEditor==null && feature!=null) {
//			EClassifier ec = feature.getEType();
//			Class ic = ec.getInstanceClass();
//			if (boolean.class.equals(ic)) {
//				cellEditor = new CustomCheckboxCellEditor(parent);
//			}
//			else if (ec instanceof EEnum) {
//				cellEditor = new ComboBoxCellEditor(parent, new String[] {""}, SWT.READ_ONLY);
//			}
//			else if (PropertyUtil.isMultiChoice(feature.eContainer(), feature)) {
//				cellEditor = new ComboBoxCellEditor(parent, new String[] {""});
//			}
//			else if (ec instanceof EDataType) {
//				cellEditor = new EDataTypeCellEditor((EDataType)ec, parent);
//			}
//			else if (ic==EObject.class) {
//				cellEditor = new StringWrapperCellEditor(parent);
//			}
//		}
//		return cellEditor;
//	}

	protected Composite getParent() {
		return tableViewer.getTable();
	}
	
	protected void setCellEditor(CellEditor ce) {
		this.cellEditor = ce;

		// this is the only tricky part: set the cell editor in the
		// table viewer's list
		CellEditor[] cellEditors = tableViewer.getCellEditors();
		int index = getColumnIndex();
		if (index>=0 && index<cellEditors.length) {
			cellEditors[index] = cellEditor;
		}
	}
	
	protected CellEditor createCellEditor(Object element, String property) {
		Composite parent = tableViewer.getTable();
		if (cellEditor==null && feature!=null) {
			EClassifier ec = feature.getEType();
			Class ic = ec.getInstanceClass();

			if (boolean.class.equals(ic)) {
				cellEditor = new CustomCheckboxCellEditor(parent);
			}
			else if (ec instanceof EEnum) {
				cellEditor = new CustomComboBoxCellEditor(parent, (EObject)element, feature);
			}
			else if (PropertyUtil.isMultiChoice((EObject)element, feature)) {
				cellEditor = new CustomComboBoxCellEditor(parent, (EObject)element, feature);
			}
			else if (ec instanceof EDataType) {
				cellEditor = new EDataTypeCellEditor((EDataType)ec, parent);
			}
			else if (ic==EObject.class) {
				cellEditor = new StringWrapperCellEditor(parent);
			}
			setCellEditor(cellEditor);
		}
		return cellEditor;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public boolean canModify(Object element, String property) {
		if (editable && listComposite.getColumnProvider().canModify(object, feature, (EObject)element)) {
			cellEditor = null;
			createCellEditor(element,property);
			return cellEditor!=null;
		}
		return false;
	}

	public void modify(Object element, String property, Object value) {
		modify((EObject)element, feature, value);
	}

	protected void modify(EObject object, EStructuralFeature feature, Object value) {
		if (cellEditor instanceof CustomComboBoxCellEditor) {
			value = ((CustomComboBoxCellEditor) cellEditor).getChoice(value);
		}
		
		boolean result = PropertyUtil.setValue(getEditingDomain(), object, feature, value);
		if (result==false || getDiagramEditor().getDiagnostics()!=null) {
			// revert the change and display error status message.
			ErrorUtils.showErrorMessage(getDiagramEditor().getDiagnostics().getMessage());
		}
		else {
			ErrorUtils.showErrorMessage(null);
			tableViewer.refresh();
		}
	}
	
	@Override
	public Object getValue(Object element, String property) {
		if (element instanceof EObject) {
			if (cellEditor instanceof CustomCheckboxCellEditor) {
				return cellEditor.getValue();
			}
			else if (cellEditor instanceof CustomComboBoxCellEditor) {
				// for combobox cell editors, the returned value is a list of strings
				return cellEditor.getValue();
			}
			else {
				// all other types of cell editors accept the object/feature value
				EObject object = (EObject)element;
				return object.eGet(feature);
			}
		}
		return getText(element);
	}
	
	
	protected BPMN2Editor getDiagramEditor() {
		return listComposite.getDiagramEditor();
	}
	
	protected TransactionalEditingDomain getEditingDomain() {
		return getDiagramEditor().getEditingDomain();
	}
	
	public static class CustomCheckboxCellEditor extends ComboBoxCellEditor {

		private static String[] items = new String[] { "false", "true" };
		
		public CustomCheckboxCellEditor(Composite parent) {
			super(parent, items,SWT.READ_ONLY);
		}
		
		@Override
		public String[] getItems() {
			return items;
		}

		@Override
		public void setItems(String[] items) {
			super.setItems(this.items);
		}

		@Override
		protected Object doGetValue() {
			Integer value = (Integer)super.doGetValue();
			return new Boolean(value.intValue()!=0);
		}

		@Override
		protected void doSetValue(Object value) {
			if (value instanceof Boolean) {
				value = new Integer( ((Boolean)value).booleanValue() ? 1 : 0 );
			}
			else if (value instanceof String) {
				for (int i=0; i<items.length; ++i) {
					if (value.equals(items[i])) {
						value = new Integer(i);
						break;
					}
				}
			}
			super.doSetValue(value);
		}
		
	}
	
	public class CustomComboBoxCellEditor extends ComboBoxCellEditor {
		
		// list of choices as constructed by ExtendedPropertiesAdapter.FeatureDescriptor#getChoiceOfValues()
		protected Hashtable<String,Object> choices = null;

		public CustomComboBoxCellEditor(Composite parent, EObject object, EStructuralFeature feature) {
			super(parent, new String[] {""}, SWT.READ_ONLY);
			Object current = object.eGet(feature);
			setValue(object, feature, current);
		}
		
		public void setValue(EObject object, EStructuralFeature feature, Object current) {
			
			// build the list of valid choices for this object/feature and cache it;
			// we'll need it again later in modify()
			choices = null;
			List<String> items = new ArrayList<String>();
			choices = PropertyUtil.getChoiceOfValues(object, feature);
			items.addAll(choices.keySet());
			this.setItems(items.toArray(new String[items.size()]));
			
			// find the index of the current value in the choices list
			// need to handle both cases where current value matches the
			// choices key (a String) or an EObject
			int index = -1;
			for (int i=0; i<items.size(); ++i) {
				if (current == choices.get(items.get(i))) {
					index = i;
					break;
				}
				if (current instanceof String) {
					if (current.equals(items.get(i))) {
						index = i;
						break;
					}
				}
			}
			this.setValue(new Integer(index));
		}
		
		public Object getChoice(Object value) {
			// for combobox cell editors, getValue() returns an Integer
			assert(choices!=null && value instanceof Integer);
			int index = ((Integer)value).intValue();
			if (index>=0) {
				// look up the real value from the list of choices created by getValue()
				String[] items = ((ComboBoxCellEditor)cellEditor).getItems();
				value = choices.get(items[index]);
			}
			else
				value = null;
			return value;
		}
	}
	public class StringWrapperCellEditor extends TextCellEditor {

		public StringWrapperCellEditor(Composite parent) {
			super(parent);
		}

		@Override
		protected Object doGetValue() {
			String value = (String)super.doGetValue();
			return ModelUtil.createStringWrapper(value);
		}

		@Override
		protected void doSetValue(Object value) {
			if (value==null)
				value = "";
			else
				value = ModelUtil.getStringWrapperValue(value);
			super.doSetValue(value);
		}

	}
}