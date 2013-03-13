package org.camunda.bpm.modeler.ui.features.context;

import org.eclipse.graphiti.features.context.ILocationContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * Context for the repositioning of an element after another element has been updated.
 * 
 * @author nico.rehwaldt
 */
public interface IRepositionContext extends ILocationContext, IPictogramElementContext {

	/**
	 * Returns the element the reposition operation should base on
	 * 
	 * @return
	 */
	public PictogramElement getReferenceElement();
	

	/**
	 * Returns the element the reposition operation should be executed upon
	 * 
	 * @return
	 */
	public Shape getShape();
}
