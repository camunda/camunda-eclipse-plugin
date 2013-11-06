package org.camunda.bpm.modeler.core.utils;

import org.camunda.bpm.modeler.core.features.DecorateContext;
import org.camunda.bpm.modeler.core.features.DefaultBpmn2DecorateFeature;
import org.camunda.bpm.modeler.core.features.UpdateDecorationFeature;
import org.camunda.bpm.modeler.core.features.api.IDecorateFeature;
import org.camunda.bpm.modeler.core.features.api.container.IFeatureContainer;
import org.camunda.bpm.modeler.ui.diagram.Bpmn2FeatureProvider;
import org.eclipse.core.runtime.Assert;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;

/**
 * A utility to help dealing with {@link IDecorateFeature}.
 * 
 * @author nico.rehwaldt
 *
 * @see DefaultBpmn2DecorateFeature
 * @see UpdateDecorationFeature
 */
public class DecoratorUtil {

	public static final String DECORATOR_PROPERTY = DecoratorUtil.class.getName();
	
	/**
	 * Returns the feature container that handles the given context.
	 * 
	 * @param featureProvider
	 * @param context
	 * 
	 * @return
	 */
	public static IFeatureContainer getMatchingFeatureContainer(IFeatureProvider featureProvider, IPictogramElementContext context) {
		
		Assert.isLegal(featureProvider instanceof Bpmn2FeatureProvider);
		
		Bpmn2FeatureProvider bpmn2featureProvider = (Bpmn2FeatureProvider) featureProvider;
		return bpmn2featureProvider.getFeatureContainer(context);
	}
	
	/**
	 * Return true if the given feature container is the decorator for the specified element.
	 * 
	 * @param pictogramElement
	 * @param decorator
	 * 
	 * @return
	 */
	public static boolean isElementDecorator(PictogramElement pictogramElement, IFeatureContainer decorator) {
		String decoratorName = Graphiti.getPeService().getPropertyValue(pictogramElement, DECORATOR_PROPERTY);
		return decorator.getClass().getName().equals(decoratorName);
	}
	
	/**
	 * Set a {@link IFeatureContainer} as the decorator for a given element.
	 * 
	 * @param pictogramElement
	 * @param decorator
	 */
	public static void setElementDecorator(PictogramElement pictogramElement, IFeatureContainer decorator) {
		String decoratorName = decorator.getClass().getName();
		Graphiti.getPeService().setPropertyValue(pictogramElement, DECORATOR_PROPERTY, decoratorName);
	}

	/**
	 * Decorate the given element
	 * 
	 * @param pictogramElement
	 * @param featureProvider
	 */
	public static void decorate(PictogramElement pictogramElement, IFeatureProvider featureProvider) {
		Assert.isLegal(featureProvider instanceof Bpmn2FeatureProvider);
		
		Bpmn2FeatureProvider bpmn2featureProvider = (Bpmn2FeatureProvider) featureProvider;
		
		DecorateContext context = new DecorateContext(pictogramElement);
		
		IDecorateFeature decorateFeature = bpmn2featureProvider.getDecorateFeature(context);
		if (decorateFeature.canExecute(context)) {
			decorateFeature.execute(context);
		}
	}
}
