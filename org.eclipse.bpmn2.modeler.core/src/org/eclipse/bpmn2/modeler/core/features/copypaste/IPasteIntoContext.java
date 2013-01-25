package org.eclipse.bpmn2.modeler.core.features.copypaste;

import java.util.List;

import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;


/**
 * Paste into context.
 * 
 * @author nico.rehwaldt
 */
public interface IPasteIntoContext {

	/**
	 * Returns the container to paste into
	 * @return
	 */
	public ContainerShape getPasteContainer();
	
	/**
	 * Returns the pictogram element to paste
	 * 
	 * @return the pictogram elements to paste
	 */
	public List<PictogramElement> getPictogramElements();
}
