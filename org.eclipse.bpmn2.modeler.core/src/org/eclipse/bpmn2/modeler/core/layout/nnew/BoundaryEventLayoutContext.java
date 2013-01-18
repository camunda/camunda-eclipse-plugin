package org.eclipse.bpmn2.modeler.core.layout.nnew;

import org.eclipse.bpmn2.modeler.core.layout.Docking;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.bpmn2.modeler.core.utils.Tuple;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;

/**
 * Special layouting context for boundary events.
 * 
 * @author nico.rehwaldt
 */
public class BoundaryEventLayoutContext extends DefaultLayoutContext {

	public BoundaryEventLayoutContext(FreeFormConnection connection) {
		super(connection, true);
	}
	
	@Override
	protected boolean needsLayoutByDockings(Tuple<Docking, Docking> dockings, Sector afterRepairStartSector, Sector afterRepairEndSector) {
		if (connection.getBendpoints().size() > 1) {
			return false;
		}
		else if (afterRepairStartSector != dockings.getFirst().getSector() ||  
			   afterRepairEndSector != dockings.getSecond().getSector()) {
			return true;
		}
		
		return false;
		
	}
}