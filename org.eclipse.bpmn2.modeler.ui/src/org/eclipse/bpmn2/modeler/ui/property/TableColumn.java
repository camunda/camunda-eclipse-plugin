package org.eclipse.bpmn2.modeler.ui.property;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.ComboBoxEditor;

import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.modeler.core.utils.ErrorUtils;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.property.providers.ColumnTableProvider;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor.EDataTypeCellEditor;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class TableColumn extends ColumnTableProvider.Column implements ILabelProvider, ICellModifier {

	protected AbstractListComposite abstractListComposite;
	protected TableViewer tableViewer;
	// the underlying EObject of the table row
	protected EObject object;
	// the EStructuralFeature being managed for this table column
	protected EStructuralFeature feature;
	// The column cell editor
	protected CellEditor cellEditor = null;
	// list of choices as constructed by ExtendedPropertiesAdapter.FeatureDescriptor#getChoiceOfValues()
	// only valid if the cell editor is a ComboBoxCellEditor
	protected Hashtable<String,Object> choices = null;

	public TableColumn(EObject o, EStructuralFeature f) {
		this(null,o,f);
	}

	public TableColumn(AbstractListComposite abstractListComposite, EObject o, EStructuralFeature f) {
		this.abstractListComposite = abstractListComposite;
		object = o;
		feature = f;
	}
	
	public void setOwner(AbstractListComposite abstractListComposite) {
		this.abstractListComposite = abstractListComposite;
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
			String value = PropertyUtil.getDisplayName((EObject)element,feature);
//			EClassifier ec = feature.getEType();
//			Class ic = ec.getInstanceClass();
//			System.out.println("feature: "+ec.getName()+"."+feature.getName()+
//					" class: "+ic.getSimpleName()+
//					" value="+value);
			return value;
		}
		return "";
	}
	
	public CellEditor createCellEditor (Composite parent) {
		if (cellEditor==null && feature!=null) {
			EClassifier ec = feature.getEType();
			Class ic = ec.getInstanceClass();
//			System.out.println("feature: "+ec.getName()+"."+feature.getName()+" class: "+ic.getSimpleName());
			if (boolean.class.equals(ic)) {
				cellEditor = new CustomCheckboxCellEditor(parent);
			}
			else if (ec instanceof EEnum) {
				cellEditor = new ComboBoxCellEditor(parent, new String[] {""}, SWT.READ_ONLY);
			}
			else if (ec instanceof EDataType) {
				cellEditor = new EDataTypeCellEditor((EDataType)ec, parent);
			}
			else if (ic==EObject.class) {
				cellEditor = new StringWrapperCellEditor(parent);
			}
			else if (PropertyUtil.isMultiChoice(ec, feature)) {
				cellEditor = new ComboBoxCellEditor(parent, new String[] {""});
			}
		}
		return cellEditor;
	}
	
	public boolean canModify(Object element, String property) {
		return this.abstractListComposite.columnProvider.canModify(object, feature, (EObject)element);
	}

	public void modify(Object element, String property, Object value) {
		modify((EObject)element, feature, value);
	}

	protected void modify(EObject object, EStructuralFeature feature, Object value) {
		if (cellEditor instanceof CustomCheckboxCellEditor) {
			;
		}
		else if (cellEditor instanceof ComboBoxCellEditor) {
			// for combobox cell editors, the returned value is an Integer
			int index = ((Integer)value).intValue();
			if (index>=0) {
				// look up the real value from the list of choices created by getValue()
				String[] items = ((ComboBoxCellEditor)cellEditor).getItems();
				value = choices.get(items[index]);
			}
			else
				value = null;
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
				cellEditor.getValue();
			}
			else if (cellEditor instanceof ComboBoxCellEditor) {
				// for combobox cell editors, the returned value is a list of strings
				int index = -1;
				// build the list of valid choices for this object/feature and cache it;
				// we'll need it again later in modify()
				choices = null;
				List<String> items = new ArrayList<String>();
				choices = PropertyUtil.getChoiceOfValues((EObject)element, feature);
				items.addAll(choices.keySet());
				((ComboBoxCellEditor)cellEditor).setItems(items.toArray(new String[items.size()]));
				Object target = ((EObject)element).eGet(feature);
				for (int i=0; i<items.size(); ++i) {
					if ( choices.get(items.get(i)) == target) {
						index = i;
						break;
					}
				}
				return new Integer(index);
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
		return abstractListComposite.getDiagramEditor();
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