package org.camunda.bpm.modeler.core.features;

import org.camunda.bpm.modeler.core.features.api.IDecorateContext;
import org.camunda.bpm.modeler.core.features.api.IDecorateFeature;
import org.camunda.bpm.modeler.core.utils.DecoratorUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * Default {@link IDecorateFeature} implementation.
 * 
 * @author nico.rehwaldt
 */
public class DefaultBpmn2DecorateFeature extends AbstractDecorateFeature {

	public DefaultBpmn2DecorateFeature(IFeatureProvider fp) {
		super(fp);
	}

	public boolean canDecorate(IDecorateContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();
		return !LabelUtil.isLabel(pictogramElement);
	}

	public void decorate(IDecorateContext context) {
		
		// set us as the decorator that handled the given element
		PictogramElement pictogramElement = context.getPictogramElement();
		
		DecoratorUtil.setElementDecorator(pictogramElement, DecoratorUtil.getMatchingFeatureContainer(getFeatureProvider(), context));
	}
}
