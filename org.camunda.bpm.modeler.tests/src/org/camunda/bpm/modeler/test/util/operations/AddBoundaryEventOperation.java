package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.diagram.BPMN2FeatureProvider;
import org.camunda.bpm.modeler.ui.features.event.CreateBoundaryEventFeature;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddBoundaryEventOperation extends AbstractAddOperation<CreateBoundaryEventFeature, AddBoundaryEventOperation> {

	public AddBoundaryEventOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddBoundaryEventOperation.class);
	}
	
	@Override
	protected CreateBoundaryEventFeature createFeature(CreateContext context) {
		BPMN2FeatureProvider featureProvider = (BPMN2FeatureProvider) diagramTypeProvider.getFeatureProvider();
		IFeature createFeature = featureProvider.getCreateFeatureForBusinessObject(BoundaryEvent.class);
		
		return (CreateBoundaryEventFeature) createFeature;
	}
	
	public static AddBoundaryEventOperation addBoundaryEvent(IDiagramTypeProvider diagramTypeProvider) {
		return new AddBoundaryEventOperation(diagramTypeProvider);
	}
}
