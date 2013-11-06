package org.camunda.bpm.modeler.core.features.event;

import org.camunda.bpm.modeler.core.features.AbstractFlowElementDecorateFeature;
import org.camunda.bpm.modeler.core.features.api.IDecorateFeature;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * A {@link IDecorateFeature} to be used for event shapes.
 * 
 * @author nico.rehwaldt
 */
public class EventDecorateFeature extends AbstractFlowElementDecorateFeature<Ellipse>{

	public EventDecorateFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	protected Shape getDecorateShape(ContainerShape shape) {
		return FeatureSupport.getShape(shape, AddEventFeature.DECORATE_SHAPE, "true");
	}
}
