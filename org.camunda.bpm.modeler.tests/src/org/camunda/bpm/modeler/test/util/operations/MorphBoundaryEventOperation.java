package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.ui.features.AbstractMorphNodeFeature;
import org.camunda.bpm.modeler.ui.features.event.MorphBoundaryEventFeature;
import org.camunda.bpm.modeler.ui.features.event.AbstractMorphEventFeature.MorphEventOption;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class MorphBoundaryEventOperation extends AbstractMorphEventOperation<MorphBoundaryEventFeature, MorphBoundaryEventOperation> {
	
	public MorphBoundaryEventOperation(IDiagramTypeProvider diagramTypeProvider, PictogramElement element) {
		super(diagramTypeProvider, element, MorphBoundaryEventOperation.class);
	}

	@Override
	protected MorphBoundaryEventFeature createFeature(CustomContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return new MorphBoundaryEventFeature(featureProvider);
	}
	
	public static MorphBoundaryEventOperation morphBoundaryEvent(PictogramElement activity, IDiagramTypeProvider typeProvider) {
		return new MorphBoundaryEventOperation(typeProvider, activity);
	}

	@Override
	public MorphBoundaryEventOperation to(EClass newType) {
		MorphEventOption newOption = new MorphEventOption(newType.getName(), Bpmn2Package.eINSTANCE.getBoundaryEvent(), newType);
		ContextUtil.set(context, AbstractMorphNodeFeature.CREATE_MODE, newOption);
		
		return myself;
	}
	
}
