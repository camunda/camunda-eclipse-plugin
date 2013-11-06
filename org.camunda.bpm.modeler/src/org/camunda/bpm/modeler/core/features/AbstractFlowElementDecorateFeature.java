package org.camunda.bpm.modeler.core.features;

import org.camunda.bpm.modeler.core.features.api.IDecorateContext;
import org.camunda.bpm.modeler.core.features.api.IDecorateFeature;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * A {@link IDecorateFeature} implementation that allows the decoration of a
 * well defined decorate container that is a child shape of the candidate to be
 * decorated.
 * 
 * Subclasses may override {@link #decorate(GraphicsAlgorithm)} or {@link #decorate(Shape, GraphicsAlgorithm)}
 * to perform decoration.
 * 
 * @author nico.rehwaldt
 *
 * @param <T> the type of graphics algorithm that is the container decorated by this feature
 * 
 * @see #getDecorateContainer(ContainerShape)
 * @see #decorate(IDecorateContext)
 */
public abstract class AbstractFlowElementDecorateFeature<T extends GraphicsAlgorithm> extends DefaultBpmn2DecorateFeature {

	public AbstractFlowElementDecorateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public void decorate(IDecorateContext context) {
		super.decorate(context);
		
		PictogramElement pictogramElement = context.getPictogramElement();
		
		if (pictogramElement instanceof ContainerShape) {

			ContainerShape shape = (ContainerShape) pictogramElement;
			
			T decorateContainer = getDecorateContainer(shape);
			
			clearDecorations(decorateContainer);
			
			decorate(shape, decorateContainer);
		} else {
			System.out.println("Not decorating " + pictogramElement);
		}
	}

	private void clearDecorations(T decorateContainer) {
		decorateContainer.setLineWidth(1);
		decorateContainer.getGraphicsAlgorithmChildren().clear();
	}

	/**
	 * Decorates the given decoration container within the decoration candidate.
	 * 
	 * @param shape
	 * @param decorateContainer
	 */
	protected void decorate(Shape shape, T decorateContainer) {
		
		// per default, perform the default decoration
		// via style util
		BaseElement businessObject = BusinessObjectUtil.getFirstBaseElement(shape);
		StyleUtil.applyStyle(decorateContainer, businessObject);
		
		decorate(decorateContainer);
	}

	/**
	 * Decorate the decoration rectangle provided by an activity.
	 * 
	 * Should be overridden by subclasses to perform actual behavior.
	 * 
	 * @param decorateContainer
	 */
	protected void decorate(T decorateContainer) { }

	protected T getDecorateContainer(ContainerShape shape) {
		
		// get decorate rectangle for activity
		Shape decorateShape = getDecorateShape(shape);
		
		@SuppressWarnings("unchecked")
		T decorateContainer = (T) decorateShape.getGraphicsAlgorithm();
		
		return decorateContainer;
	}

	protected abstract Shape getDecorateShape(ContainerShape shape);
}
