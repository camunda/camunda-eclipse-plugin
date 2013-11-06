package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.diagram.Bpmn2FeatureProvider;
import org.camunda.bpm.modeler.ui.features.gateway.ExclusiveGatewayFeatureContainer.CreateExclusiveGatewayFeature;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddExclusiveGatewayOperation extends AbstractAddShapeOperation<CreateExclusiveGatewayFeature, AddExclusiveGatewayOperation> {

	public AddExclusiveGatewayOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddExclusiveGatewayOperation.class);
	}
	
	@Override
	protected CreateExclusiveGatewayFeature createFeature(CreateContext context) {
		Bpmn2FeatureProvider featureProvider = (Bpmn2FeatureProvider) diagramTypeProvider.getFeatureProvider();
		IFeature createFeature = featureProvider.getCreateFeatureForBusinessObject(Bpmn2Package.eINSTANCE.getExclusiveGateway());
		
		return (CreateExclusiveGatewayFeature) createFeature;
	}
	
	public static AddExclusiveGatewayOperation addExclusiveGateway(IDiagramTypeProvider diagramTypeProvider) {
		return new AddExclusiveGatewayOperation(diagramTypeProvider);
	}
}
