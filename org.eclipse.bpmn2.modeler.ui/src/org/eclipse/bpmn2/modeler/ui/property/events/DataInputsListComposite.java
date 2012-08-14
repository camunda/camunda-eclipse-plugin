package org.eclipse.bpmn2.modeler.ui.property.events;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.InputSet;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultListComposite;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.tasks.IoParameterMappingColumn;
import org.eclipse.bpmn2.modeler.ui.property.tasks.IoParameterNameColumn;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

public class DataInputsListComposite extends DefaultListComposite {

	ThrowEvent throwEvent;

	public DataInputsListComposite(Composite parent, ThrowEvent throwEvent) {
		super(parent, ADD_BUTTON|REMOVE_BUTTON|EDIT_BUTTON|SHOW_DETAILS);
		this.throwEvent = throwEvent;
	
		columnProvider = new ListCompositeColumnProvider(this,true);
		
		EStructuralFeature f;
		f = PACKAGE.getDataInput_Name();
		columnProvider.add(new IoParameterNameColumn(throwEvent,f));

		f = PACKAGE.getThrowEvent_DataInputAssociation();
		columnProvider.add(new IoParameterMappingColumn(throwEvent,f));
	}

	@Override
	protected EObject addListItem(EObject object, EStructuralFeature feature) {
		InputSet inputSet = throwEvent.getInputSet();
		if (inputSet==null) {
			inputSet = FACTORY.createInputSet();
			throwEvent.setInputSet(inputSet);
			ModelUtil.setID(inputSet);
		}
		// generate a unique parameter name
		String base = "inParam";
		int suffix = 1;
		String name = base + suffix;
		for (;;) {
			boolean found = false;
			for (DataInput p : inputSet.getDataInputRefs()) {
				if (name.equals(p.getName())) {
					found = true;
					break;
				}
			}
			if (!found)
				break;
			name = base + ++suffix;
		}
		
		DataInput param = (DataInput)super.addListItem(object, feature);
		// add the new parameter to the InputSet
		(param).setName(name);
		inputSet.getDataInputRefs().add(param);
		
		// create a DataInputAssociation
		DataInputAssociation inputAssociation = FACTORY.createDataInputAssociation();
		throwEvent.getDataInputAssociation().add(inputAssociation);
		inputAssociation.setTargetRef((DataInput) param);
		ModelUtil.setID(inputAssociation);
		return param;
	}

	@Override
	protected EObject editListItem(EObject object, EStructuralFeature feature) {
		return super.editListItem(object, feature);
	}

	@Override
	protected Object removeListItem(EObject object, EStructuralFeature feature, int index) {
		// remove parameter from inputSets
		EList<EObject> list = (EList<EObject>)object.eGet(feature);
		EObject item = list.get(index);
		InputSet inputSet = throwEvent.getInputSet();
		if (inputSet.getDataInputRefs().contains(item))
			inputSet.getDataInputRefs().remove(item);
		
		// remove Input or Output DataAssociations
		List<DataInputAssociation> dataInputAssociations = throwEvent.getDataInputAssociation();
		List<DataInputAssociation> removed = new ArrayList<DataInputAssociation>();
		for (DataInputAssociation dia : dataInputAssociations) {
			if (item==dia.getTargetRef())
				removed.add(dia);
		}
		dataInputAssociations.removeAll(removed);

		return super.removeListItem(object, feature, index);
	}
}