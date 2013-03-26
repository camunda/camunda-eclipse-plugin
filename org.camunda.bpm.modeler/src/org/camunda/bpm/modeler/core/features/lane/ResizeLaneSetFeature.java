package org.camunda.bpm.modeler.core.features.lane;

import java.util.List;

import org.camunda.bpm.modeler.core.features.DefaultResizeBPMNShapeFeature;
import org.camunda.bpm.modeler.core.layout.util.CollaborationResizeSupport;
import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * Common super class for resize behavior in pools and lanes.
 * 
 * @author nico.rehwaldt
 */
public class ResizeLaneSetFeature extends DefaultResizeBPMNShapeFeature {

	private static final String PROGRAMATIC_RESIZE = "DefaultResizeBPMNShapeFeature.PROGRAMATIC_RESIZE";
	
	private List<Shape> visibleLaneShapes;

	public ResizeLaneSetFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected IRectangle getMinimumBounds(IResizeShapeContext context) {
	
		// all resizing is allowed if we are triggering the resize
		// programmaticaly (because we know what we are doing
		if (isProgramaticResize(context)) {
			return null;
		}
		
		ContainerShape containerShape = (ContainerShape) context.getShape();
		
		return CollaborationResizeSupport.getResizeBBox(containerShape, getVisibleLanes(containerShape), context);
	}
	
	protected boolean isProgramaticResize(IResizeShapeContext context) {
		return ContextUtil.is(context, PROGRAMATIC_RESIZE);
	}
	
	/**
	 * Caching visible lanes accessor.
	 * 
	 * @param containerShape
	 * @return
	 */
	private List<Shape> getVisibleLanes(ContainerShape containerShape) {
		if (visibleLaneShapes == null) {
			visibleLaneShapes = FeatureSupport.getTopLevelLanes(containerShape);
		}
		
		return visibleLaneShapes;
	}

}