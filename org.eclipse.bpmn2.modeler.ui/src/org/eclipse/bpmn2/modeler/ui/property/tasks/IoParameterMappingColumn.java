package org.eclipse.bpmn2.modeler.ui.property.tasks;

import java.util.List;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Assignment;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.TableColumn;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;

public class IoParameterMappingColumn extends TableColumn {

	protected DataAssociation association = null;
	
	public IoParameterMappingColumn(EObject o, EStructuralFeature f) {
		super(o, f);
	}

	@Override
	public String getHeaderText() {
		return ModelUtil.getLabel(
				Bpmn2Package.eINSTANCE.getDataAssociation(),
				feature.getName().startsWith("dataInput") ?
					Bpmn2Package.eINSTANCE.getDataAssociation_SourceRef() :
					Bpmn2Package.eINSTANCE.getDataAssociation_TargetRef()
		);
	}

	@Override
	public String getText(Object element) {
		String text = null;
		ItemAwareElement source = (ItemAwareElement)element;
		DataAssociation da = getDataAssociation(source);
		if (da!=null) {
			ItemAwareElement target = getTargetElement(da);
			if (target!=null)
				text = ModelUtil.getDisplayName(target);
			else {
				if (da.getTransformation()!=null) {
					text = "Transform: " + ModelUtil.getDisplayName(da.getTransformation());
				}
				if (!da.getAssignment().isEmpty()) {
					String text2 = null;
					for ( Assignment assign : da.getAssignment()) {
						FormalExpression expr  = getTargetExpression(da, assign);
						String body = ModelUtil.getDisplayName(expr);
						if (text2==null)
							text2 = "Expression: " + body;
						else
							text2 += ",\n" + body;
					}
					if (text==null)
						text = text2;
					else
						text += " + " + text2;
				}
			}
		}
		return text==null ? "" : text;
	}

	private FormalExpression getTargetExpression(DataAssociation da, Assignment assign) {
		return (FormalExpression) ((da instanceof DataInputAssociation) ? assign.getFrom() : assign.getTo());
	}

	@Override
	public Object getValue(Object element, String property) {
		Integer value = new Integer(-1);
		ItemAwareElement source = (ItemAwareElement)element;
		DataAssociation da = getDataAssociation(source);
		if (da!=null) {
			ItemAwareElement target = getTargetElement(da);
			association = da;
			EStructuralFeature f = getTargetFeature(source);
			((CustomComboBoxCellEditor)cellEditor).setValue(da,f,target);
			value = (Integer)cellEditor.getValue();
		}
		return value;
	}
	
	@Override
	protected CellEditor createCellEditor(Object element, String property) {
		CellEditor ce = new CustomComboBoxCellEditor(getParent(), object, feature);
		setCellEditor(ce);
		return ce;
	}
	
	@Override
	public boolean canModify(Object element, String property) {
		if (super.canModify(element, property)) {
			// only allow the combobox cell editor to work if the DataAssociation is
			// with a Property (no Assignments or Transformations please!)
			// Other types of associations must be done in the Detail section
			DataAssociation da = getDataAssociation((ItemAwareElement)element);
			if (da!= null) {
				if (getTargetElement(da) == null) {
					if (!da.getAssignment().isEmpty() || da.getTransformation()!=null)
						return false;
				}
				return true;
			}
		}
		return false;
	}

	public void modify(Object element, String property, Object value) {
		// the real object to be modified is the DataAssociation
		EStructuralFeature f = getTargetFeature((ItemAwareElement)element);
		super.modify(association, f, value);
	}
	
	protected List<DataAssociation> getDataAssociations(ItemAwareElement element) {
		if (element instanceof DataInput)
			return getDataInputAssociations();
		if (element instanceof DataOutput)
			return getDataOutputAssociations();
		return null;
	}
	
	public List getDataInputAssociations() {
		if (object instanceof Activity) {
			return ((Activity)object).getDataInputAssociations();
		}
		else if (object instanceof ThrowEvent) {
			return ((ThrowEvent)object).getDataInputAssociation();
		}
		return null;
	}
	
	public List getDataOutputAssociations() {
		if (object instanceof Activity) {
			return ((Activity)object).getDataOutputAssociations();
		}
		else if (object instanceof CatchEvent) {
			return ((CatchEvent)object).getDataOutputAssociation();
		}
		return null;
	}

	protected DataAssociation getDataAssociation(ItemAwareElement element) {
		for (DataAssociation da : getDataAssociations(element)) {
			if (element==getSourceElement(da)) {
				return da;
			}
		}
		return null;
	}
	
	protected ItemAwareElement getSourceElement(DataAssociation da) {
		if (da instanceof DataOutputAssociation) {
			if (da.getSourceRef().size()==1)
				return da.getSourceRef().get(0);
		}
		else if (da instanceof DataInputAssociation) {
			return da.getTargetRef();
		}
		return null;
	}
	
	protected ItemAwareElement getTargetElement(DataAssociation da) {
		if (da instanceof DataInputAssociation) {
			if (da.getSourceRef().size()==1)
				return da.getSourceRef().get(0);
		}
		else if (da instanceof DataOutputAssociation) {
			return da.getTargetRef();
		}
		return null;
	}
	
	protected EStructuralFeature getTargetFeature(ItemAwareElement element) {
		return element instanceof DataInput ?
				Bpmn2Package.eINSTANCE.getDataAssociation_SourceRef() :
				Bpmn2Package.eINSTANCE.getDataAssociation_TargetRef();
	}
}