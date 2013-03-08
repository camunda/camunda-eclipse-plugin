package org.eclipse.bpmn2.modeler.core.layout.nnew;

import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.modeler.core.layout.Docking;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
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
	protected boolean needsLayoutByDockings(Tuple<Docking, Docking> dockings, Sector postRepairStartSector, Sector postRepairEndSector) {
		Sector preRepairStartSector = dockings.getFirst().getSector();
		Sector preRepairEndSector = dockings.getSecond().getSector();
		
		if (connection.getBendpoints().size() > 1) {
			return false;
		} else 
		if (postRepairStartSector != preRepairStartSector ||  
	        postRepairEndSector != preRepairEndSector) {
			
			return true;
		}
		
		return false;
	}
	
	@Override
	protected boolean overlapsWithStartShape(Point p) {
		return super.overlapsWithStartShape(p) || overlapsAttachedShape(p, startShape);
	}
	
	/**
	 * Returns true if the given point overlaps with the shape the 
	 * boundary shape is attached to.
	 * 
	 * @param point 
	 * @param boundaryShape
	 * 
	 * @return
	 */
	protected boolean overlapsAttachedShape(Point point, Shape boundaryShape) {
		BoundaryEvent boundaryEvent = (BoundaryEvent) BusinessObjectUtil.getBusinessObjectForPictogramElement(boundaryShape);
		Diagram diagram = LayoutUtil.getDiagram(boundaryShape);
		PictogramElement attachedToElement = BusinessObjectUtil.getLinkingPictogramElement(boundaryEvent.getAttachedToRef(), diagram);
		
		IRectangle attachedToBounds = LayoutUtil.getAbsoluteBounds((Shape) attachedToElement);
		
		return isContained(attachedToBounds, point);
	}
}
