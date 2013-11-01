package org.camunda.bpm.modeler.core.features;

import org.camunda.bpm.modeler.core.features.api.container.IFeatureContainer;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.DecoratorUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * A feature that dynamically updates the decoration of a shape
 * if its {@link IFeatureContainer} changes.
 *  
 * @author nico.rehwaldt
 */
public class UpdateDecorationFeature extends AbstractUpdateFeature {

	public UpdateDecorationFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();

		// TODO: Label property not correctly set
		if (LabelUtil.isLabel(pictogramElement) || 
				BusinessObjectUtil.getFirstElementOfType(pictogramElement, BPMNShape.class) == null) {
			return false;
		}

		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);

		return businessObject != null;
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {

		if (isDecoratorChanged(context, getFeatureProvider())) {
			return Reason.createTrueReason("Decorator changed");
		} else {
			return Reason.createFalseReason();
		}
	}

	private boolean isDecoratorChanged(IUpdateContext context, IFeatureProvider featureProvider) {

		PictogramElement pictogramElement = context.getPictogramElement();
		
		IFeatureContainer decorator = DecoratorUtil.getMatchingFeatureContainer(getFeatureProvider(), context);
		return !DecoratorUtil.isElementDecorator(pictogramElement, decorator);
	}

	@Override
	public boolean update(IUpdateContext context) {
		DecoratorUtil.decorate(context.getPictogramElement(), getFeatureProvider());
		return true;
	}
}
