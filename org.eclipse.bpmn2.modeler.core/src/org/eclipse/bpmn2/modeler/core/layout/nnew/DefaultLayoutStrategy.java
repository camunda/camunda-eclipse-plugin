package org.eclipse.bpmn2.modeler.core.layout.nnew;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
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
	public LayoutContext createLayoutingContext(FreeFormConnection connection, boolean layoutOnRepairFail) {
		
		AnchorContainer anchorContainer = connection.getStart().getParent();
		
		BaseElement element = BusinessObjectUtil.getFirstBaseElement(anchorContainer);
		if (element instanceof BoundaryEvent) {
			return new BoundaryEventLayoutContext(connection);
		} else {
			return new DefaultLayoutContext(connection, layoutOnRepairFail);
		}
	}
}
