package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.diagram.BPMN2FeatureProvider;
import org.camunda.bpm.modeler.ui.features.event.EndEventFeatureContainer.CreateEndEventFeature;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddEndEventOperation extends AbstractAddShapeOperation<CreateEndEventFeature, AddEndEventOperation> {

	public AddEndEventOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddEndEventOperation.class);
	}
	
	@Override
	protected CreateEndEventFeature createFeature(CreateContext context) {
		BPMN2FeatureProvider featureProvider = (BPMN2FeatureProvider) diagramTypeProvider.getFeatureProvider();
		IFeature createFeature = featureProvider.getCreateFeatureForBusinessObject(EndEvent.class);
		
		return (CreateEndEventFeature) createFeature;
	}
	
	public static AddEndEventOperation addEndEvent(IDiagramTypeProvider diagramTypeProvider) {
		return new AddEndEventOperation(diagramTypeProvider);
	}
}
