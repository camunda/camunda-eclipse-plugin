package org.eclipse.bpmn2.modeler.core.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;

/**
 * does not do anything
 * 
 * @author smirnov
 *
 */
@Deprecated
public class DefaultLayoutBPMNShapeFeature extends AbstractLayoutFeature {

	public DefaultLayoutBPMNShapeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		return true;
	}

	@Override
	public boolean layout(ILayoutContext context) {
		return true;
	}
	
}
