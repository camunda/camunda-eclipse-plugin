package org.camunda.bpm.modeler.core.features;

import org.camunda.bpm.modeler.core.features.api.IDecorateContext;
import org.eclipse.graphiti.internal.features.context.impl.base.PictogramElementContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

@SuppressWarnings("restriction")
public class DecorateContext extends PictogramElementContext implements IDecorateContext {

	public DecorateContext(PictogramElement pictogramElement) {
		super(pictogramElement);
	}
}
