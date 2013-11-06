package org.camunda.bpm.modeler.ui.features.activity.task;

import org.camunda.bpm.modeler.core.features.activity.ActivityDecorateFeature;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

public abstract class AbstractTaskDecorateFeature extends ActivityDecorateFeature {

	public AbstractTaskDecorateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void decorate(RoundedRectangle decorateContainer) {				
		IGaService service = Graphiti.getGaService();
		Image img = service.createImage(decorateContainer, getIconId());
		service.setLocationAndSize(img, 2, 2, GraphicsUtil.TASK_IMAGE_SIZE, GraphicsUtil.TASK_IMAGE_SIZE);
	}
	
	/**
	 * Returns the icon id that decorates the task
	 * 
	 * @return
	 */
	protected abstract String getIconId();
}
