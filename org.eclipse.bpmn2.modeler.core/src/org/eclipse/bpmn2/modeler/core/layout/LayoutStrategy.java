package org.eclipse.bpmn2.modeler.core.layout;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * A layouting strategy
 * 
 * @author nico.rehwaldt
 *
 * @param <C> context required for the strategy to work
 * @param <T> return type of the strategy
 */
public abstract class LayoutStrategy<C, T> extends Strategy<T> {
	
	protected FreeFormConnection connection;
	protected boolean unchanged;

	protected void unchanged() {
		unchanged = true;
	}
	
	public T execute() {
		if (unchanged) {
			return null;
		} else {
			return doExecute();	
		}
	}
	
	/**
	 * May be overriden by subclasses to pass over context
	 * 
	 * @param context
	 */
	protected void setContext(C context) {
		
	}
	
	/**
	 * Perform the execution of the strategy
	 * @return
	 */
	protected abstract T doExecute();
	
	public boolean appliesTo(FreeFormConnection connection) {
		if (connection.getBendpoints().size() > 2) {
			
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
	
	public static <T extends LayoutStrategy<C, V>, C, V> T build (Class<T> strategy, FreeFormConnection connection, C context) {
		Sector sector = LayoutUtil.getEndShapeSector(connection);
		BaseElement sourceElement = LayoutUtil.getSourceBaseElement(connection);
		BaseElement targetElement = LayoutUtil.getTargetBaseElement(connection);
		
		T strategyInstance = createStrategy(strategy, connection, context);
		T subStrategy = (T) strategyInstance.getSubStrategy(connection, sourceElement);
		
		if (subStrategy.appliesTo(connection)) {
			
			// FIXME what if sector is undefined? Should this be allowed ? skipping sector switch in this case for now 
			if (sector != Sector.UNDEFINED) {
				subStrategy.sectorSwitch(sector);
			} else {
				// Activator.logError(new IllegalStateException("Dont know how to handle sector for "+connection));
			}
			
			subStrategy.typeSwitch(sector, sourceElement, targetElement);
		} else {
			subStrategy.unchanged();
		}
		
		return subStrategy;
	}

	/**
	 * 
	 * @param strategyCls
	 * @param connection
	 * @return
	 * 
	 * @throws IllegalArgumentException if the strategy could not be instantiated
	 */
	public static <T extends LayoutStrategy<C, V>, C, V> T createStrategy(Class<T> strategyCls, FreeFormConnection connection, C context) throws IllegalArgumentException {
		try {
			T strategy = strategyCls.getConstructor(FreeFormConnection.class).newInstance(connection);
			strategy.setContext(context);
			return strategy;
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot instantiate " + strategyCls.getName(), e);
		}
	}
	
	public LayoutStrategy<C, T> getSubStrategy(FreeFormConnection connection, BaseElement sourceElement) {
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
	
	protected Shape getStartShape() {
		return (Shape) getConnection().getStart().getParent();
	}

	protected Shape getEndShape() {
		return (Shape) getConnection().getEnd().getParent();
	}
}
