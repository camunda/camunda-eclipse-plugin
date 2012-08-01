package org.eclipse.bpmn2.modeler.ui.property.tasks;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.CallableElement;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.InputOutputSpecification;
import org.eclipse.bpmn2.InputSet;
import org.eclipse.bpmn2.OutputSet;
import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.DefaultListComposite;
import org.eclipse.bpmn2.modeler.ui.property.TableColumn;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class IoParametersListComposite extends DefaultListComposite {

	/**
	 * 
	 */
	Activity activity;
	CallableElement element;
	InputOutputSpecification ioSpecification;
	EStructuralFeature ioFeature;
	boolean isInput;
	
	public IoParametersListComposite(IoParametersDetailComposite detailComposite, EObject container, InputOutputSpecification ioSpecification, EStructuralFeature ioFeature) {
		super(detailComposite, ADD_BUTTON|REMOVE_BUTTON|EDIT_BUTTON|SHOW_DETAILS);
		this.ioFeature = ioFeature;
		this.ioSpecification = ioSpecification;
		isInput = ("dataInputs".equals(ioFeature.getName()));
		if (container instanceof Activity) {
			this.activity = (Activity)container;
			columnProvider = new ListCompositeColumnProvider(this, true);
			EClass listItemClass = (EClass)ioFeature.getEType();
			setListItemClass(listItemClass);
			
			EStructuralFeature f;
			f = (EAttribute)listItemClass.getEStructuralFeature("name");
			columnProvider.add(new IoParameterNameColumn(activity,f));
			if (isInput) {
				columnProvider.add(new TableColumn(activity,PACKAGE.getDataInput_IsCollection()));
				f = PACKAGE.getActivity_DataInputAssociations();
				columnProvider.add(new IoParameterMappingColumn(activity,f));
			}
			else {
				columnProvider.add(new TableColumn(activity,PACKAGE.getDataOutput_IsCollection()));
				f = PACKAGE.getActivity_DataOutputAssociations();
				columnProvider.add(new IoParameterMappingColumn(activity,f));
			}
		}
		else if (container instanceof CallableElement) {
			this.element = (CallableElement)container;
		}
	}

	@Override
	protected EObject addListItem(EObject object, EStructuralFeature feature) {
		EObject param = null;
		
		// Make sure that the ioSpecification is contained in our Activity.
		InsertionAdapter.executeIfNeeded(ioSpecification);
		
		param = super.addListItem(object, feature);
		if (param instanceof DataInput) {
			// add the new parameter to the InputSet
			List<InputSet> inputSets = ioSpecification.getInputSets();
			if (inputSets.size()==0) {
				inputSets.add(FACTORY.createInputSet());
			}
			InputSet inputSet = inputSets.get(0);
			
			// generate a unique parameter name
			String base = "input";
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
			((DataInput)param).setName(name);

			inputSet.getDataInputRefs().add((DataInput) param);
			ModelUtil.setID(inputSet);
		}
		else if (param instanceof DataOutput)
		{
			// add the new parameter to the OutputSet
			List<OutputSet> outputSets = ioSpecification.getOutputSets();
			if (outputSets.size()==0) {
				outputSets.add(FACTORY.createOutputSet());
			}
			OutputSet outputSet = outputSets.get(0);
			
			// generate a unique parameter name
			String base = "output";
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
			((DataOutput)param).setName(name);

			outputSet.getDataOutputRefs().add((DataOutput) param);
			ModelUtil.setID(outputSet);
		}
		
		if (activity!=null) {
			// this is an Activity - create an Input or Output DataAssociation
			if (param instanceof DataInput) {
				DataInputAssociation inputAssociation = FACTORY.createDataInputAssociation();
				activity.getDataInputAssociations().add(inputAssociation);
				inputAssociation.setTargetRef((DataInput) param);
				ModelUtil.setID(inputAssociation);
			}
			else if (param instanceof DataOutput)
			{
				DataOutputAssociation outputAssociation = FACTORY.createDataOutputAssociation();
				activity.getDataOutputAssociations().add(outputAssociation);
				outputAssociation.getSourceRef().clear();
				outputAssociation.getSourceRef().add((DataOutput) param);
				ModelUtil.setID(outputAssociation);
			}
		}
		else if (element!=null) {
			// this is a CallableElement - it has no DataAssociations so we're all done
		}
		return param;
	}

	@Override
	protected EObject editListItem(EObject object, EStructuralFeature feature) {
		return super.editListItem(object, feature);
	}

	@Override
	protected Object removeListItem(EObject object, EStructuralFeature feature, int index) {
		EList<EObject> list = (EList<EObject>)object.eGet(feature);
		EObject item = list.get(index);

		if (item instanceof DataInput) {
			// remove parameter from inputSets
			List<InputSet> inputSets = ioSpecification.getInputSets();
			for (InputSet is : inputSets) {
				if (is.getDataInputRefs().contains(item))
					is.getDataInputRefs().remove(item);
			}
		}
		else if (item instanceof DataOutput) {
			// remove parameter from outputSets
			List<OutputSet> OutputSets = ioSpecification.getOutputSets();
			for (OutputSet is : OutputSets) {
				if (is.getDataOutputRefs().contains(item))
					is.getDataOutputRefs().remove(item);
			}
		}
		
		if (activity!=null) {
			// this is an Activity
			// remove Input or Output DataAssociations
			if (item instanceof DataInput) {
				List<DataInputAssociation> dataInputAssociations = activity.getDataInputAssociations();
				List<DataInputAssociation> removed = new ArrayList<DataInputAssociation>();
				for (DataInputAssociation dia : dataInputAssociations) {
					if (dia.getTargetRef()!=null && dia.getTargetRef().equals(item))
						removed.add(dia);
				}
				dataInputAssociations.removeAll(removed);
			}
			else if (item instanceof DataOutput) {
				List<DataOutputAssociation> dataOutputAssociations = activity.getDataOutputAssociations();
				List<DataOutputAssociation> removed = new ArrayList<DataOutputAssociation>();
				for (DataOutputAssociation doa : dataOutputAssociations) {
					if (doa.getTargetRef()!=null && doa.getTargetRef().equals(item))
						removed.add(doa);
				}
				dataOutputAssociations.removeAll(removed);
			}
		}
		else if (element!=null) {
			// this is a CallableElement
		}
		else
			return false;

		return super.removeListItem(object, feature, index);
	}
	
}