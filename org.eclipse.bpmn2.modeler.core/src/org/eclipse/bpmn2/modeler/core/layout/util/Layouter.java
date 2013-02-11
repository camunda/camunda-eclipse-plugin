package org.eclipse.bpmn2.modeler.core.layout.util;

import static org.eclipse.bpmn2.modeler.core.utils.ContextUtil.set;

import org.eclipse.bpmn2.modeler.core.features.PropertyNames;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.impl.LayoutContext;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * Layout connection specific utilities.
 * 
 * @author nico.rehwaldt
 */
public class Layouter {

	/**
	 * Perform layout of the given connection after a docking shape has been moved.
	 * 
	 * @param connection
	 * @param featureProvider
	 */
	public static void layoutConnectionAfterShapeMove(Connection connection, IFeatureProvider featureProvider) {
		
		ILayoutContext layoutContext = new LayoutContext(connection);
		
		// layout on repair fail
		set(layoutContext, PropertyNames.LAYOUT_CONNECTION_ON_REPAIR_FAIL);
		
		// layout 
		featureProvider.layoutIfPossible(layoutContext);
	}

	/**
	 * Layouts the given connection after it has been created.
	 * 
	 * @param connection
	 * @param featureProvider
	 */
	public static void layoutAfterCreate(FreeFormConnection connection, IFeatureProvider featureProvider) {
		
		ILayoutContext layoutContext = new LayoutContext(connection);

		// force layout only if no initial bendpoints are set
		boolean forceLayout = connection.getBendpoints().isEmpty();
		
		if (forceLayout) {
			set(layoutContext, PropertyNames.LAYOUT_CONNECTION_FORCE);
		}
		
		// layout on repair fail
		set(layoutContext, PropertyNames.LAYOUT_CONNECTION_ON_REPAIR_FAIL);
		
		// layout 
		featureProvider.layoutIfPossible(layoutContext);
	}

	/**
	 * Layout the given connection after one of its ends has changed (start or end)
	 * 
	 * @param connection
	 * 
	 * @param forceLayout whether the relayout should be forced
	 * 
	 * @param featureProvider
	 */
	public static void layoutConnectionAfterReconnect(Connection connection, boolean forceLayout, IFeatureProvider featureProvider) {

		ILayoutContext layoutContext = new LayoutContext(connection);
		
		if (forceLayout) {
			set(layoutContext, PropertyNames.LAYOUT_CONNECTION_FORCE);
		}
		
		set(layoutContext, PropertyNames.LAYOUT_CONNECTION_ON_REPAIR_FAIL);
		
		featureProvider.layoutIfPossible(layoutContext);
	}

	/**
	 * Layout shape after it has been resized
	 * 
	 * @param shape
	 * @param featureProvider
	 */
	public static IReason layoutShapeAfterResize(Shape shape, IFeatureProvider featureProvider) {

		ILayoutContext layoutContext = new LayoutContext(shape);

		set(layoutContext, PropertyNames.LAYOUT_ADJUST_LABEL);
		set(layoutContext, PropertyNames.LAYOUT_REPAIR_CONNECTIONS);
		
		return featureProvider.layoutIfPossible(layoutContext);
	}

	/**
	 * Layout shape after it has been moved.
	 * 
	 * @param shape
	 * @param moveLabel
	 * @param repairConnections
	 * @param featureProvider
	 */
	public static void layoutShapeAfterMove(Shape shape, boolean moveLabel, boolean repairConnections, IFeatureProvider featureProvider) {

		ILayoutContext layoutContext = new LayoutContext(shape);
		
		// move label (true per default)
		if (moveLabel) {
			set(layoutContext, PropertyNames.LAYOUT_ADJUST_LABEL);
		}

		// reconnect shape after move (true per default)
		if (repairConnections) {
			set(layoutContext, PropertyNames.LAYOUT_REPAIR_CONNECTIONS);
		}
		
		// try to layout
		featureProvider.layoutIfPossible(layoutContext);
	}
}
