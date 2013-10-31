package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.ui.features.AbstractMorphNodeFeature;
import org.camunda.bpm.modeler.ui.features.event.MorphEndEventFeature;
import org.camunda.bpm.modeler.ui.features.event.AbstractMorphEventFeature.MorphEventOption;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class MorphEndEventOperation extends AbstractMorphEventOperation<MorphEndEventFeature, MorphEndEventOperation> {
	
	public MorphEndEventOperation(IDiagramTypeProvider diagramTypeProvider, PictogramElement element) {
		super(diagramTypeProvider, element, MorphEndEventOperation.class);
	}

	@Override
	protected MorphEndEventFeature createFeature(CustomContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return new MorphEndEventFeature(featureProvider);
	}
	
	public static MorphEndEventOperation morphEndEvent(PictogramElement activity, IDiagramTypeProvider typeProvider) {
		return new MorphEndEventOperation(typeProvider, activity);
	}
	
	@Override
	public MorphEndEventOperation to(EClass newType) {
		MorphEventOption newOption = new MorphEventOption(newType.getName(), Bpmn2Package.eINSTANCE.getEndEvent(), newType);
		ContextUtil.set(context, AbstractMorphNodeFeature.CREATE_MODE, newOption);
		
		return myself;
	}
}
