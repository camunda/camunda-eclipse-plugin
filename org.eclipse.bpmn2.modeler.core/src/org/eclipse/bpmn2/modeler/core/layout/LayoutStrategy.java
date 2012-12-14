package org.eclipse.bpmn2.modeler.core.layout;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;


public abstract class LayoutStrategy {
	protected FreeFormConnection connection;
	protected boolean unchanged;

	protected void unchanged() {
		unchanged = true;
	}
	
	public void execute() {
		if (unchanged) {
			return;
		} else {
			doExecute();	
		}
	}
	
	protected abstract void doExecute();
	
	public boolean appliesTo(FreeFormConnection connection) {
		if (connection.getBendpoints().size() > 2
				|| LayoutUtil.getLength(connection) > LayoutUtil.MAGIC_LENGTH) {
			return false;
		}

		if (isConnectionBpmnType(connection, Association.class)
				|| isConnectionBpmnType(connection, DataAssociation.class)) {
			return false;
		}
		
		if (LayoutUtil.getSourceBaseElement(connection) instanceof BoundaryEvent && connection.getBendpoints().size() > 1) {
			return false;
		}

		return true;
	}
	
	protected boolean isConnectionBpmnType(Connection connection, Class<?> type) {
		if (connection.getLink().getBusinessObjects().size() == 0) {
			return false;
		}
		
		return type.isInstance(connection.getLink().getBusinessObjects().get(0));
	}
	
	public static <T extends LayoutStrategy> T build (Class<T> strategy, FreeFormConnection connection) {
		Sector sector = LayoutUtil.getEndShapeSector(connection);
		BaseElement sourceElement = LayoutUtil.getSourceBaseElement(connection);
		BaseElement targetElement = LayoutUtil.getTargetBaseElement(connection);
		
		try {
			T strategyInstance = strategy.getConstructor(FreeFormConnection.class).newInstance(connection);
			T subStrategy = (T) strategyInstance.getSubStrategy(connection, sourceElement);
			
			if (subStrategy.appliesTo(connection)) {
				
				// FIXME what if sector is undefined? Should this be allowed ? skipping sector switch in this case for now 
				if (sector != Sector.UNDEFINED) {
					subStrategy.sectorSwitch(sector);
				} else {
					org.eclipse.bpmn2.modeler.core.Activator.logError(new IllegalStateException("Dont know how to handle sector for "+connection));
				}
				
				subStrategy.typeSwitch(sector, sourceElement, targetElement);
			} else {
				subStrategy.unchanged();
			}
			
			return subStrategy;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public LayoutStrategy getSubStrategy(FreeFormConnection connection, BaseElement sourceElement) {
		return this;
	}
	
	/**
	 * switch behavior of the strategy based on the target element sector
	 * 
	 * @param targetElementSector
	 * @param sourceElement
	 */
	protected abstract void sectorSwitch(Sector targetElementSector);
	
	/**
	 * switch behavior of the strategy based on the element types
	 * 
	 * @param targetElementSector
	 * @param sourceElement
	 * @param targetElement
	 */
	protected abstract void typeSwitch(Sector targetElementSector, BaseElement sourceElement, BaseElement targetElement);
	
	public FreeFormConnection getConnection() {
		return connection;
	}

	public void setConnection(FreeFormConnection connection) {
		this.connection = connection;
	}
	
	protected Shape getStartShape() {
		return (Shape) getConnection().getStart().getParent();
	}

	protected Shape getEndShape() {
		return (Shape) getConnection().getEnd().getParent();
	}
}
