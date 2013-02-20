package org.eclipse.bpmn2.modeler.ui.features.label;

import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.ui.features.context.IRepositionContext;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

/**
 * A context for the repositioning of something after a reference element has been moved.
 * 
 * @author nico.rehwaldt
 */
public abstract class AbstractRepositionFeature extends AbstractCustomFeature {

	public AbstractRepositionFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public boolean canExecute(ICustomContext context) {
		return context instanceof IRepositionContext;
	}
	
	@Override
	public void execute(ICustomContext context) {
		if (context instanceof IRepositionContext) {
			reposition((IRepositionContext) context);
		}
	}
	
	/**
	 * Execute the update of the label position.
	 * 
	 * @param context
	 */
	public void reposition(IRepositionContext context) {
		
		Shape targetShape = context.getShape();
		
		adjustPosition(targetShape, context);
		
		postAdjustPosition(targetShape, context);
		
		updateDi(targetShape);
	}

	/**
	 * Execute an action after the adjustment of the position has been done.
	 * 
	 * May be overridden by subclasses to perform actual behavior.
	 * 
	 * @param targetShape
	 * @param context
	 */
	protected void postAdjustPosition(Shape targetShape, IRepositionContext context) {
		
	}

	protected void adjustPosition(Shape targetShape, IRepositionContext context) {
		Point newTargetPosition = getAdjustedPosition(targetShape, context);

		performMove(targetShape, newTargetPosition);
		
		GraphicsUtil.sendToFront(targetShape);
	}

	/**
	 * Returns the adjustment required to fix the target shape position.
	 * 
	 * @param targetShape
	 * @param context
	 * 
	 * @return
	 */
	protected abstract Point getAdjustedPosition(Shape targetShape, IRepositionContext context);
	
	private void performMove(Shape labelShape, Point position) {

		// always move within the original container
		GraphicsAlgorithm labelGraphicsAlgorithm = labelShape.getGraphicsAlgorithm();
		
		if (labelGraphicsAlgorithm != null) {
			Graphiti.getGaService().setLocation(labelGraphicsAlgorithm, position.getX() - labelGraphicsAlgorithm.getWidth() / 2, position.getY(), true);
		}
	}
	
	/**
	 * Update di of the target shape
	 * 
	 * @param targetShape
	 */
	protected abstract void updateDi(Shape targetShape);
}
