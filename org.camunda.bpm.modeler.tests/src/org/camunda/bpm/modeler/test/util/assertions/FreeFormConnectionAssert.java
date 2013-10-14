/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.camunda.bpm.modeler.test.util.assertions;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Assertions;

public class FreeFormConnectionAssert extends AbstractAssert<FreeFormConnectionAssert, FreeFormConnection> {

	protected FreeFormConnectionAssert(FreeFormConnection actual) {
		super(actual, FreeFormConnectionAssert.class);
	}
	
	public FreeFormConnectionAssert hasNoDiagonalEdges() {
		
		EList<Point> bendpoints = actual.getBendpoints();
		
		Point start = getPosition(actual.getStart());
		Point end = getPosition(actual.getEnd());
		
		List<Point> allPoints = new ArrayList<Point>();
		allPoints.add(start);
		allPoints.addAll(bendpoints);
		allPoints.add(end);
		
		Point previous = start;
		
		for (int i = 1; i < allPoints.size(); i++)  {
			Point current = allPoints.get(i);
			
			assertConnectionNotDiagonal(previous, current);
			
			previous = current;
		}
		
		return myself;
	}
	
	private void assertConnectionNotDiagonal(Point p1, Point p2) {
		int tolerance = 1;
		
		if (Math.abs(p1.getX() - p2.getX()) > tolerance && 
			Math.abs(p1.getY() - p2.getY()) > tolerance) {
			
			Assertions.fail(String.format("Expected points <(%s,%s)> and <(%s,%s)> to form a non-diagonal connection", p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		}
	}

	public FreeFormConnectionAssert hasBendpointCount(int size) {
		EList<Point> bendpoints = actual.getBendpoints();
		
		Assertions.assertThat(bendpoints).hasSize(size);
		
		return myself;
	}
	
	/**
	 * asserts that there is a bendpoint with the given index and coordinates
	 *  
	 * @param bendpointIndex
	 * @param x
	 * @param y
	 * @return
	 */
	public FreeFormConnectionAssert hasBendpoint(int bendpointIndex, int x, int y) {
		return hasBendpoint(bendpointIndex, point(x, y));
	}
	
	/**
	 * asserts that there is a bendpoint with the given index and coordinates
	 *  
	 * @param bendpointIndex
	 * @param x
	 * @param y
	 * @return
	 */
	public FreeFormConnectionAssert hasBendpoint(int bendpointIndex, Point expectedPoint) {
		EList<Point> bendpoints = actual.getBendpoints();
		
		Point bendpoint = bendpoints.get(bendpointIndex);
		
		Bpmn2ModelAssertions.assertThat(bendpoint).isEqualTo(expectedPoint);
		
		return myself;
	}
	
	public FreeFormConnectionAssert hasExactBendpoints(List<Point> expectedBendpoints) {

		Assertions
			.assertThat(toString(actual.getBendpoints()))
			.as("actual bendpoints")
			.isEqualTo(toString(expectedBendpoints));
		
		return myself;
	}
	
	private Point getPosition(Anchor anchor) {
		ILocation location = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(anchor);
		return Graphiti.getGaCreateService().createPoint(location.getX(), location.getY());
	}

	public FreeFormConnectionAndShapeAssert anchorPointOn(Shape connectedShape) {
		
		FreeFormConnectionAndShapeAssert connectionAndShapeAssert = new FreeFormConnectionAndShapeAssert(this, actual, connectedShape);
		
		return connectionAndShapeAssert.exists();
	}

	public FreeFormConnectionAndShapeAssert anchorPointOn(AnchorContainer container) {
		
		Assertions.assertThat(container).isInstanceOf(Shape.class);
		
		return anchorPointOn((Shape) container);
	}
	
	public FreeFormConnectionAndShapeAssert anchor(Anchor anchor) {
		return anchorPointOn(anchor.getParent());
	}
	
	private String toString(List<Point> points) {
		
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		
		for (Point p: points) {
			if (builder.length() > 1) {
				builder.append(", ");
			}
			
			builder.append(String.format("(%s, %s)", p.getX(), p.getY()));
		}
		
		builder.append("]");
		
		return builder.toString();
	}
}
