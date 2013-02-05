package org.eclipse.bpmn2.modeler.ui.features.context;

import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;


/**
 * Context for the repositioning of an element.
 * 
 * @author nico.rehwaldt
 */
public class RepositionContext extends CustomContext implements IRepositionContext {
	
	private PictogramElement referenceElement;
	private Shape shape;

	public PictogramElement getReferenceElement() {
		return referenceElement;
	}
	
	@Override
	public PictogramElement getPictogramElement() {
		return shape;
	}
	
	public void setShape(Shape shape) {
		this.shape = shape;
	}
	
	@Override
	public Shape getShape() {
		return shape;
	}
	
	public void setReferencedElement(PictogramElement referenceElement) {
		this.referenceElement = referenceElement;
	}
}
