package org.camunda.bpm.modeler.test.util.operations;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IResizeFeature;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.impl.ResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * Operation for resizing shapes.
 * 
 * @author nico.rehwaldt
 */
public class ResizeShapeOperation extends Operation<ResizeShapeContext, IResizeFeature> {

	public ResizeShapeOperation(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
		
		this.context = createContext(shape);
	}

	public ResizeShapeOperation to(IRectangle bounds, int direction) {

		context.setLocation(bounds.getX(), bounds.getY());
		context.setSize(bounds.getWidth(), bounds.getHeight());
		context.setDirection(direction);
		
		return this;
	}

	public Operation<ResizeShapeContext, IResizeFeature> fromTopLeftBy(Point amount) {

		IRectangle participantShapeBounds = LayoutUtil.getRelativeBounds(context.getShape());
		IRectangle postResizeRectangle = rectangle(participantShapeBounds.getX() + amount.getX(), participantShapeBounds.getY() + amount.getY(), participantShapeBounds.getWidth() - amount.getX(), participantShapeBounds.getHeight() - amount.getY());
		
		return to(postResizeRectangle, IResizeShapeContext.DIRECTION_NORTH_WEST);
	}

	public Operation<ResizeShapeContext, IResizeFeature> fromTopRightBy(Point amount) {

		IRectangle participantShapeBounds = LayoutUtil.getRelativeBounds(context.getShape());
		IRectangle postResizeRectangle = rectangle(participantShapeBounds.getX(), participantShapeBounds.getY() + amount.getY(), participantShapeBounds.getWidth() + amount.getX(), participantShapeBounds.getHeight() - amount.getY());
		
		return to(postResizeRectangle, IResizeShapeContext.DIRECTION_NORTH_EAST);
	}
	
	public Operation<ResizeShapeContext, IResizeFeature> fromBottomRightBy(Point amount) {

		IRectangle participantShapeBounds = LayoutUtil.getRelativeBounds(context.getShape());
		IRectangle postResizeRectangle = rectangle(participantShapeBounds.getX(), participantShapeBounds.getY(), participantShapeBounds.getWidth() + amount.getX(), participantShapeBounds.getHeight() + amount.getY());
		
		return to(postResizeRectangle, IResizeShapeContext.DIRECTION_SOUTH_EAST);
	}

	public Operation<ResizeShapeContext, IResizeFeature> fromBottomLeftBy(Point amount) {

		IRectangle participantShapeBounds = LayoutUtil.getRelativeBounds(context.getShape());
		IRectangle postResizeRectangle = rectangle(participantShapeBounds.getX(), participantShapeBounds.getY() + amount.getY(), participantShapeBounds.getWidth() + amount.getX(), participantShapeBounds.getHeight() - amount.getY());
		
		return to(postResizeRectangle, IResizeShapeContext.DIRECTION_SOUTH_WEST);
	}
	
	protected ResizeShapeContext createContext(Shape shape) {
		return new ResizeShapeContext(shape);
	}

	@Override
	protected ResizeShapeContext createContext() {
		throw new UnsupportedOperationException("Use #createContext(Shape)");
	}

	@Override
	protected IResizeFeature createFeature(ResizeShapeContext context) {
		return diagramTypeProvider.getFeatureProvider().getResizeShapeFeature(context);
	}
	
	///// static helpers ////////////////////////////////////
	
	public static ResizeShapeOperation resize(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		return new ResizeShapeOperation(shape, diagramTypeProvider);
	}
}
