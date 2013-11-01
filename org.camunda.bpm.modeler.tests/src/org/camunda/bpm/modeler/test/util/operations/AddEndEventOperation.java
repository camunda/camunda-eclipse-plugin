package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.diagram.BpmnFeatureProvider;
import org.camunda.bpm.modeler.ui.features.event.EndEventFeatureContainer.CreateEndEventFeature;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddEndEventOperation extends AbstractAddShapeOperation<CreateEndEventFeature, AddEndEventOperation> {

	public AddEndEventOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddEndEventOperation.class);
	}
	
	@Override
	protected CreateEndEventFeature createFeature(CreateContext context) {
		BpmnFeatureProvider featureProvider = (BpmnFeatureProvider) diagramTypeProvider.getFeatureProvider();
		IFeature createFeature = featureProvider.getCreateFeatureForBusinessObject(Bpmn2Package.eINSTANCE.getEndEvent());
		
		return (CreateEndEventFeature) createFeature;
	}
	
	public static AddEndEventOperation addEndEvent(IDiagramTypeProvider diagramTypeProvider) {
		return new AddEndEventOperation(diagramTypeProvider);
	}
}
