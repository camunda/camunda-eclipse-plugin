package org.eclipse.bpmn2.modeler.core.features.copypaste;

import java.util.List;

import org.eclipse.graphiti.features.context.ILocationContext;
import org.eclipse.graphiti.features.context.impl.LocationContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * A paste into context
 * 
 * @author nico.rehwaldt
 */
public class PasteIntoContext extends LocationContext implements IPasteIntoContext, ILocationContext {

	private ContainerShape container;
	private List<PictogramElement> pictogramElements;

	public PasteIntoContext(ContainerShape container, List<PictogramElement> pictogramElements) {
		this.container = container;
		this.pictogramElements = pictogramElements;
	}
	
	@Override
	public ContainerShape getPasteContainer() {
		return container;
	}

	@Override
	public List<PictogramElement> getPictogramElements() {
		return pictogramElements;
	}
}
