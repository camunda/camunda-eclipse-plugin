package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.ui.features.AbstractMorphNodeFeature;
import org.camunda.bpm.modeler.ui.features.event.MorphStartEventFeature;
import org.camunda.bpm.modeler.ui.features.event.AbstractMorphEventFeature.MorphEventOption;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class MorphStartEventOperation extends AbstractMorphEventOperation<MorphStartEventFeature, MorphStartEventOperation> {

	public MorphStartEventOperation(IDiagramTypeProvider diagramTypeProvider, PictogramElement element) {
		super(diagramTypeProvider, element, MorphStartEventOperation.class);
	}

	@Override
	protected MorphStartEventFeature createFeature(CustomContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return new MorphStartEventFeature(featureProvider);
	}
	
	public static MorphStartEventOperation morphStartEvent(PictogramElement activity, IDiagramTypeProvider typeProvider) {
		return new MorphStartEventOperation(typeProvider, activity);
	}
	
	@Override
	public MorphStartEventOperation to(EClass newType) {
		MorphEventOption newOption = new MorphEventOption(newType.getName(), Bpmn2Package.eINSTANCE.getStartEvent(), newType);
		ContextUtil.set(context, AbstractMorphNodeFeature.CREATE_MODE, newOption);
		
		return myself;
	}
	
}
