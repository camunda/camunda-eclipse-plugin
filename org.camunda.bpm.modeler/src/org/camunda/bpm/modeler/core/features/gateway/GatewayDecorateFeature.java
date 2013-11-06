package org.camunda.bpm.modeler.core.features.gateway;

import org.camunda.bpm.modeler.core.features.AbstractFlowElementDecorateFeature;
import org.camunda.bpm.modeler.core.features.api.IDecorateFeature;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class GatewayDecorateFeature extends AbstractFlowElementDecorateFeature<Polygon> implements IDecorateFeature {

	public GatewayDecorateFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	protected Shape getDecorateShape(ContainerShape shape) {
		return FeatureSupport.getShape(shape, AddGatewayFeature.DECORATE_SHAPE, "true");
	}
}
