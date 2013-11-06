package org.camunda.bpm.modeler.ui.features.activity;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.ui.diagram.Bpmn2FeatureProvider;
import org.camunda.bpm.modeler.ui.features.AbstractMorphNodeFeature;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.UpdateContext;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.jface.viewers.ILabelProvider;

public class MorphActivityFeature extends AbstractMorphNodeFeature<Activity> {

	public MorphActivityFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public String getName() {
		return "Morph Activity";
	}

	@Override
	public String getDescription() {
		return "Change the Activity type";
	}

	@Override
	public EClass getBusinessObjectClass() {
		return Bpmn2Package.eINSTANCE.getActivity();
	}
	
	@Override
	public List<EClass> excludeTypes(EObject bo) {
		List<EClass> exclusions = new ArrayList<EClass>();
		
		exclusions.add(Bpmn2Package.eINSTANCE.getSubProcess());
		exclusions.add(Bpmn2Package.eINSTANCE.getAdHocSubProcess());
		exclusions.add(Bpmn2Package.eINSTANCE.getTransaction());
		
		return exclusions;
	}
	
	@Override
	protected Shape updateShape(MorphOption option, Shape oldShape, Activity newObject) {
		UpdateContext updateContext = new UpdateContext(oldShape);
		
		IFeature updateFeature = getFeatureProvider().getUpdateFeature(updateContext);
		if (updateFeature != null && updateFeature.canExecute(updateContext)) {
			updateFeature.execute(updateContext);
		}
		
		return oldShape;
	}
	
	@Override
	protected ILabelProvider getLabelProvider() {
		if (labelProvider == null) {
			labelProvider = new CreateLabelProvider((Bpmn2FeatureProvider) getFeatureProvider());
		}
		return labelProvider;
	}
}
