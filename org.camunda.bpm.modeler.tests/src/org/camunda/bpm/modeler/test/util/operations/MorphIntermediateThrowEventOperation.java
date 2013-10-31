package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.ui.features.AbstractMorphNodeFeature;
import org.camunda.bpm.modeler.ui.features.event.AbstractMorphEventFeature.MorphEventOption;
import org.camunda.bpm.modeler.ui.features.event.MorphIntermediateThrowEventFeature;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class MorphIntermediateThrowEventOperation extends AbstractMorphEventOperation<MorphIntermediateThrowEventFeature, MorphIntermediateThrowEventOperation> {

	public MorphIntermediateThrowEventOperation(IDiagramTypeProvider diagramTypeProvider, PictogramElement element) {
		super(diagramTypeProvider, element, MorphIntermediateThrowEventOperation.class);
	}

	@Override
	protected MorphIntermediateThrowEventFeature createFeature(CustomContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return new MorphIntermediateThrowEventFeature(featureProvider);
	}
	
	public static MorphIntermediateThrowEventOperation morphIntermediateThrowEvent(PictogramElement activity, IDiagramTypeProvider typeProvider) {
		return new MorphIntermediateThrowEventOperation(typeProvider, activity);
	}
	
	public MorphIntermediateThrowEventOperation to(EClass newType, EClass newEventDefinitionType) {
		MorphEventOption newOption = new MorphEventOption(newType.getName(), newType, newEventDefinitionType);
		ContextUtil.set(context, AbstractMorphNodeFeature.CREATE_MODE, newOption);
		
		return myself;
	}
	
}
