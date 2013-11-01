package org.camunda.bpm.modeler.core.features.lane;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.core.layout.util.RectangleUtil.resizeDiff;
import static org.camunda.bpm.modeler.core.layout.util.RectangleUtil.translate;

import java.util.List;

import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.features.DefaultBpmn2ResizeShapeFeature;
import org.camunda.bpm.modeler.core.layout.util.CollaborationResizeSupport;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.camunda.bpm.modeler.core.layout.util.RectangleUtil.ResizeDiff;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.camunda.bpm.modeler.core.utils.FeatureSupport.LaneSetOperation;
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
public abstract class ResizeLaneSetFeature extends DefaultBpmn2ResizeShapeFeature {

	private List<Shape> visibleLaneShapes;

	public ResizeLaneSetFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected IRectangle getMinimumBounds(IResizeShapeContext context) {
		ContainerShape containerShape = (ContainerShape) context.getShape();
		
		IRectangle containerBounds = LayoutUtil.getRelativeBounds(containerShape);
		
		Sector sector = Sector.fromResizeDirection(context.getDirection());
		
		IRectangle relativeBounds = CollaborationResizeSupport.getRelativeResizeBBox(containerShape, getVisibleLanes(containerShape), sector);

		return translate(relativeBounds, point(containerBounds));
	}
	
	protected void moveChildShapes(IResizeShapeContext context) {
		// do not move children; feature support will do the job
		return;
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
	
	@Override
	protected void preResize(IResizeShapeContext context) {
		super.preResize(context);
		
		ContainerShape container = (ContainerShape) context.getShape();
		ResizeDiff resizeDiff = resizeDiff(LayoutUtil.getRelativeBounds(container), getPostResizeBounds(context));
		
		FeatureSupport.resizeChildren(container, resizeDiff, getFeatureProvider());
	}
	
	@Override
	protected void updateDi(Shape shape) {
		FeatureSupport.redrawLaneSet((ContainerShape) shape, getFeatureProvider());
	}
	
	@Override
	protected void internalResize(IResizeShapeContext context) {
		super.internalResize(context);

		ContainerShape container = (ContainerShape) context.getShape();
		
		FeatureSupport.resizeLaneSet(container);
	}

	@Override
	protected void layout(Shape shape, IResizeShapeContext context) {
		
		ContainerShape rootContainer = FeatureSupport.getRootContainer((ContainerShape) shape);
		
		FeatureSupport.eachLaneExecute(rootContainer, new LaneSetOperation() {
			
			@Override
			public void execute(Shape lane) {
				internalLayout(lane);
			}
		});
	}
	
	@Override
	protected void postResize(IResizeShapeContext context) {
		
		super.postResize(context);
		
//		FeatureSupport.redrawLaneSet(container);
	}
}