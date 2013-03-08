package org.eclipse.bpmn2.modeler.ui.features.activity;

import org.eclipse.bpmn2.modeler.core.features.DefaultResizeBPMNShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.event.AbstractBoundaryEventOperation;
import org.eclipse.bpmn2.modeler.core.layout.util.BoundaryEventUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * The abstract resize feature for activities.
 * 
 * @author nico.rehwaldt
 */
public class ResizeActivityFeature extends DefaultResizeBPMNShapeFeature {
	
	public ResizeActivityFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void postResize(IResizeShapeContext context) {
		super.postResize(context);
		
		final Shape shape = context.getShape();
		
		new AbstractBoundaryEventOperation() {
			
			@Override
			protected void applyTo(ContainerShape boundaryShape) {
				BoundaryEventUtil.repositionBoundaryEvent(boundaryShape, shape, getFeatureProvider());
			}
		}.execute(shape);
	}
}