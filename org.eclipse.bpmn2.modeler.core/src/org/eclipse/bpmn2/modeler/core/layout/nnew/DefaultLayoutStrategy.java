package org.eclipse.bpmn2.modeler.core.layout.nnew;

import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;

/**
 * Default layouting strategy.
 * 
 * @author nico.rehwaldt
 */
public class DefaultLayoutStrategy {
	
	/**
	 * Create the appropriate layouting context.
	 * 
	 * @param connection
	 * @return
	 */
	public LayoutContext createLayoutingContext(FreeFormConnection connection) {
		return new DefaultLayoutContext(connection);
	}
}
