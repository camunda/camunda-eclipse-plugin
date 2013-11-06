package org.camunda.bpm.modeler.core.features.activity;

import org.camunda.bpm.modeler.core.features.AbstractFlowElementDecorateFeature;
import org.camunda.bpm.modeler.core.features.api.IDecorateFeature;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class ActivityDecorateFeature extends AbstractFlowElementDecorateFeature<RoundedRectangle> implements IDecorateFeature {

	public ActivityDecorateFeature(IFeatureProvider fp) {
		super(fp);
	}

	protected Shape getDecorateShape(ContainerShape shape) {
		return FeatureSupport.getShape(shape, AbstractAddActivityFeature.ACTIVITY_RECT, "true");
	}
}
