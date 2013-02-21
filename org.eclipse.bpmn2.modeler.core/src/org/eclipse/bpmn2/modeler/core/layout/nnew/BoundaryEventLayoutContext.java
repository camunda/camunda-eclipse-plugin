package org.eclipse.bpmn2.modeler.core.layout.nnew;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.modeler.core.layout.Docking;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.Tuple;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

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
	
	@Override
	protected IRectangle getOverlapStartBounds() {
		BoundaryEvent boundaryEvent = (BoundaryEvent) BusinessObjectUtil.getBusinessObjectForPictogramElement(startShape);
		Diagram diagram = BusinessObjectUtil.getDiagram(startShape);
		PictogramElement attachedToElement = BusinessObjectUtil.getLinkingPictogramElement(diagram, boundaryEvent.getAttachedToRef());
		
		IRectangle attachedTo = LayoutUtil.getAbsoluteBounds((Shape) attachedToElement);
		return attachedTo;
	}
	
	@Override
	protected boolean isRepeatNeeded() {
		Tuple<Point, Point> bendpoints = getFirstAndLastBendpoints();
		return super.isRepeatNeeded() && (isContained(startShapeBounds, bendpoints.getFirst()) ||
		 isContained(endShapeBounds, bendpoints.getSecond()));
	}
}
