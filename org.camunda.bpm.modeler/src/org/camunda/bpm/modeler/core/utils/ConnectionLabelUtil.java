package org.camunda.bpm.modeler.core.utils;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.vector;
import static org.camunda.bpm.modeler.core.layout.util.VectorUtil.add;
import static org.camunda.bpm.modeler.core.layout.util.VectorUtil.substract;
import static org.camunda.bpm.modeler.core.utils.PictogramElementPropertyUtil.get;
import static org.camunda.bpm.modeler.core.utils.PictogramElementPropertyUtil.set;

import java.util.List;

import org.camunda.bpm.modeler.core.features.PropertyNames;
import org.camunda.bpm.modeler.core.layout.util.ConnectionUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class ConnectionLabelUtil {

	public static void updateLabelRefAfterMove(Connection connection) {
		updateConnectionLabelRef(connection);
	}

	public static void updateLabelRefAfterAdd(Connection connection) {
		updateConnectionLabelRef(connection);
	}
	
	private static void updateConnectionLabelRef(Connection connection) {

		Shape labelShape = LabelUtil.getLabelShape(connection, connection.getParent());
		if (labelShape == null) {
			return;
		}
		
		Point labelPosition = LabelUtil.getLabelPosition(labelShape);
		List<Point> waypoints = LayoutUtil.getConnectionWaypoints(connection);
		
		Point refPointOnConnection = ConnectionUtil.getClosestPointOnConnection(waypoints, labelPosition);
		double refLengthOnConnection = ConnectionUtil.getLengthAtPoint(waypoints, refPointOnConnection);
		
		Point diff = point(substract(vector(labelPosition), vector(refPointOnConnection)));
		
		LabelUtil.storeLabelOffset(labelShape, diff);
		setLabelRefLength(labelShape, refLengthOnConnection);
	}

	/**
	 * Sets the label ref length on a given shape
	 * 
	 * @param labelShape
	 * @param length
	 */
	public static void setLabelRefLength(Shape labelShape, double length) {
		set(labelShape, PropertyNames.CONNECTION_LABEL_REF_LENGTH, length);
	}
	
	/**
	 * Returns the label ref length for a given shape
	 * 
	 * @param labelShape
	 * @return
	 */
	public static double getLabelRefLength(Shape labelShape) {
		Double length = get(labelShape, PropertyNames.CONNECTION_LABEL_REF_LENGTH, Double.class);
		
		if (length == null) {
			throw new IllegalArgumentException(String.format("No CONNECTION_LABEL_REF_LENGTH set for shape <%s>", labelShape));
		}
		
		return length;
	}

	/**
	 * Returns the default label position for a given connection.
	 * 
	 * @param connection
	 * @return
	 */
	public static Point getDefaultLabelPosition(Connection connection) {		
		List<Point> waypoints = LayoutUtil.getConnectionWaypoints(connection);
		return ConnectionUtil.getMidPoint(waypoints);
	}

	public static Point getAdjustedLabelPosition(Shape labelShape, Connection labeledConnection) {
		Point storedLabelOffset = LabelUtil.getStoredLabelOffset(labelShape);
		double labelRefLength = getLabelRefLength(labelShape);
		
		Point point = getPointAtLength(labeledConnection, labelRefLength);
		if (point == null) {
			return null;
		}
		
		return point(add(vector(point), vector(storedLabelOffset)));
	}

	protected static Point getPointAtLength(Connection connection, double length) {
		List<Point> waypoints = LayoutUtil.getConnectionWaypoints(connection);
		return ConnectionUtil.getPointAtLength(waypoints, length);
	}
}
