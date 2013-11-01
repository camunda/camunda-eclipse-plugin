package org.camunda.bpm.modeler.core.features.activity;

import org.camunda.bpm.modeler.core.features.AbstractFlowElementDecorateFeature;
import org.camunda.bpm.modeler.core.features.api.IDecorateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;

public class ActivityDecorateFeature extends AbstractFlowElementDecorateFeature<RoundedRectangle> implements IDecorateFeature {

	public ActivityDecorateFeature(IFeatureProvider fp) {
		super(fp);
	}
}
