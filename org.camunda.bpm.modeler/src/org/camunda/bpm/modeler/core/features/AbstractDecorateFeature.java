package org.camunda.bpm.modeler.core.features;

import org.camunda.bpm.modeler.core.features.api.IDecorateContext;
import org.camunda.bpm.modeler.core.features.api.IDecorateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.impl.AbstractFeature;

/**
 * Abstract {@link IDecorateFeature}
 * @author nico.rehwaldt
 *
 */
public abstract class AbstractDecorateFeature extends AbstractFeature implements IDecorateFeature {

	public AbstractDecorateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public final boolean canExecute(IContext context) {
		if (context instanceof IDecorateContext) {
			return canDecorate((IDecorateContext) context);
		}
		
		return false;
	}
	
	@Override
	public final void execute(IContext context) {
		decorate((IDecorateContext) context);
	}

	public abstract boolean canDecorate(IDecorateContext context);

	public abstract void decorate(IDecorateContext context);
}
