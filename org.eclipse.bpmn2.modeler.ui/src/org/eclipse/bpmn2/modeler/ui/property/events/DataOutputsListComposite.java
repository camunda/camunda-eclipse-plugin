package org.eclipse.bpmn2.modeler.ui.property.events;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.OutputSet;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultListComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.ListCompositeColumnProvider;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.tasks.IoParameterMappingColumn;
import org.eclipse.bpmn2.modeler.ui.property.tasks.IoParameterNameColumn;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

public class DataOutputsListComposite extends DefaultListComposite {

	CatchEvent catchEvent;
	
	public DataOutputsListComposite(Composite parent, CatchEvent catchEvent) {
		super(parent, ADD_BUTTON|REMOVE_BUTTON|EDIT_BUTTON|SHOW_DETAILS);
		this.catchEvent = catchEvent;
		
		columnProvider = new ListCompositeColumnProvider(this,true);
		
		EStructuralFeature f;
		f = PACKAGE.getDataOutput_Name();
		columnProvider.add(new IoParameterNameColumn(catchEvent,f));

		f = PACKAGE.getCatchEvent_DataOutputAssociation();
		columnProvider.add(new IoParameterMappingColumn(catchEvent,f));
	}

	@Override
	protected EObject addListItem(EObject object, EStructuralFeature feature) {
		OutputSet outputSet = catchEvent.getOutputSet();
		if (outputSet==null) {
			outputSet = FACTORY.createOutputSet();
			catchEvent.setOutputSet(outputSet);
			ModelUtil.setID(outputSet);
		}
		// generate a unique parameter name
		String base = "outParam";
		int suffix = 1;
		String name = base + suffix;
		for (;;) {
			boolean found = false;
			for (DataOutput p : outputSet.getDataOutputRefs()) {
				if (name.equals(p.getName())) {
					found = true;
					break;
				}
			}
			if (!found)
				break;
			name = base + ++suffix;
		}

		DataOutput param = (DataOutput)super.addListItem(object, feature);
		// add the new parameter to the OutputSet
		param.setName(name);
		outputSet.getDataOutputRefs().add(param);

		
		// create a Data OutputAssociation
		DataOutputAssociation outputAssociation = FACTORY.createDataOutputAssociation();
		catchEvent.getDataOutputAssociation().add(outputAssociation);
		outputAssociation.getSourceRef().clear();
		outputAssociation.getSourceRef().add( param);
		ModelUtil.setID(outputAssociation);

		return param;
	}

	@Override
	protected EObject editListItem(EObject object, EStructuralFeature feature) {
		return super.editListItem(object, feature);
	}

	@Override
	protected Object removeListItem(EObject object, EStructuralFeature feature, int index) {
		// remove parameter from outputSets
		EList<EObject> list = (EList<EObject>)object.eGet(feature);
		EObject item = list.get(index);
		OutputSet outputSet = catchEvent.getOutputSet();
		if (outputSet.getDataOutputRefs().contains(item))
			outputSet.getDataOutputRefs().remove(item);
		
		// remove Input or Output DataAssociations
		List<DataOutputAssociation> dataOutputAssociations = catchEvent.getDataOutputAssociation();
		List<DataOutputAssociation> removed = new ArrayList<DataOutputAssociation>();
		for (DataOutputAssociation doa : dataOutputAssociations) {
			for (ItemAwareElement e : doa.getSourceRef()) {
				if (e == item)
					removed.add(doa);
			}
		}
		dataOutputAssociations.removeAll(removed);

		return super.removeListItem(object, feature, index);
	}
	
	
}