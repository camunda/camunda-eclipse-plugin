package org.eclipse.bpmn2.modeler.core.layout.nnew;

import org.eclipse.bpmn2.modeler.core.layout.Docking;
import org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.bpmn2.modeler.core.utils.Tuple;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
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
	protected boolean needsLayoutByDockings(Tuple<Docking, Docking> dockings, Point firstBendpoint, Point lastBendpoint) {
		Sector afterRepairStartSector = LayoutUtil.getSector(ConversionUtil.location(firstBendpoint), startShapeBounds);
		Sector afterRepairEndSector = LayoutUtil.getSector(ConversionUtil.location(lastBendpoint), endShapeBounds);
		
		return afterRepairStartSector != dockings.getFirst().getSector() || 
			   afterRepairEndSector != dockings.getSecond().getSector();
	}
}
