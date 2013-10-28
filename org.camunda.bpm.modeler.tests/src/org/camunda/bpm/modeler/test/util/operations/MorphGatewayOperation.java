package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.features.gateway.MorphGatewayFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class MorphGatewayOperation extends AbstractMorphFlowNodeOperation<MorphGatewayFeature, MorphGatewayOperation> {

	public MorphGatewayOperation(IDiagramTypeProvider diagramTypeProvider, PictogramElement gateway) {
		super(diagramTypeProvider, gateway, MorphGatewayOperation.class);
	}

	@Override
	protected MorphGatewayFeature createFeature(CustomContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return new MorphGatewayFeature(featureProvider);
	}
	
	public static MorphGatewayOperation morphGateway(PictogramElement gateway, IDiagramTypeProvider typeProvider) {
		return new MorphGatewayOperation(typeProvider, gateway);
	}

}
