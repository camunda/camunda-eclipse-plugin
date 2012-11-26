package org.eclipse.bpmn2.modeler.core.layout;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;

public class AssocationAnchorPointStrategy extends AnchorPointStrategy {
	
	public AssocationAnchorPointStrategy(FreeFormConnection connection) {
		super(connection);
	}
	
	@Override
	public boolean appliesTo(FreeFormConnection connection) {
		return isConnectionBpmnType(connection, Association.class) || isConnectionBpmnType(connection, DataAssociation.class);
	}
	
	@Override
	protected void typeSwitch(Sector targetElementSector,
			BaseElement sourceElement, BaseElement TargetElement) {
		switch(targetElementSector) {
		case TOP_LEFT:
			start.top();
			end.bottom();
			break;
		case TOP_RIGHT:
			start.top();
			end.bottom();
			break;
		case TOP:
			start.top();
			end.bottom();
			break;
		case LEFT:
			start.top();
			end.top();
			break;
		case RIGHT:
			start.bottom();
			end.bottom();
			break;
		case BOTTOM_LEFT:
			start.bottom();
			end.top();
			break;
		case BOTTOM_RIGHT:
			start.bottom();
			end.top();
			break;
		case BOTTOM:
			start.bottom();
			end.top();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void sectorSwitch(Sector sector) {
		// disable default sector switch
	}
	
}
