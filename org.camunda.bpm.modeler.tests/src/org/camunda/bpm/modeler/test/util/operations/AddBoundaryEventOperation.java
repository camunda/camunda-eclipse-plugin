package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.diagram.Bpmn2FeatureProvider;
import org.camunda.bpm.modeler.ui.features.event.CreateBoundaryEventFeature;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddBoundaryEventOperation extends AbstractAddShapeOperation<CreateBoundaryEventFeature, AddBoundaryEventOperation> {

	public AddBoundaryEventOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddBoundaryEventOperation.class);
	}
	
	@Override
	protected CreateBoundaryEventFeature createFeature(CreateContext context) {
		Bpmn2FeatureProvider featureProvider = (Bpmn2FeatureProvider) diagramTypeProvider.getFeatureProvider();
		IFeature createFeature = featureProvider.getCreateFeatureForBusinessObject(Bpmn2Package.eINSTANCE.getBoundaryEvent());
		return (CreateBoundaryEventFeature) createFeature;
	}
	
	public static AddBoundaryEventOperation addBoundaryEvent(IDiagramTypeProvider diagramTypeProvider) {
		return new AddBoundaryEventOperation(diagramTypeProvider);
	}
}
