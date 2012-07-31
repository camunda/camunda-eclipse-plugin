package org.eclipse.bpmn2.modeler.ui.property.tasks;

import java.util.List;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.modeler.ui.property.AbstractListComposite;
import org.eclipse.bpmn2.modeler.ui.property.TableColumn;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class IoParameterNameColumn extends TableColumn {
	public ItemAwareElement currentElement = null;
	public IoParameterNameColumn(AbstractListComposite abstractListComposite, EObject o, EStructuralFeature f) {
		super(abstractListComposite, o, f);
	}

	@Override
	public String getText(Object element) {
		currentElement = (ItemAwareElement) element;
		return super.getText(element);
	}
	
	public List<DataInputAssociation> getDataInputAssociations() {
		if (object instanceof Activity) {
			return ((Activity)object).getDataInputAssociations();
		}
		else if (object instanceof ThrowEvent) {
			return ((ThrowEvent)object).getDataInputAssociation();
		}
		return null;
	}
	
	public List<DataOutputAssociation> getDataOutputAssociations() {
		if (object instanceof Activity) {
			return ((Activity)object).getDataOutputAssociations();
		}
		else if (object instanceof CatchEvent) {
			return ((CatchEvent)object).getDataOutputAssociation();
		}
		return null;
	}
}