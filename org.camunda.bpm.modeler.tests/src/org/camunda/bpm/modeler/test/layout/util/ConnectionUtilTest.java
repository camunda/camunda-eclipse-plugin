package org.camunda.bpm.modeler.test.layout.util;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.vector;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.camunda.bpm.modeler.core.layout.util.ConnectionUtil;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.junit.Test;

public class ConnectionUtilTest {

	private static List<Point> SIMPLE_CONNECTION = 
		Arrays.asList(
				point(20, 20), point(50, 20), point(50, 70));

	private static List<Point> SIMPLE_CONNECTION2 = 
		Arrays.asList(
				point(20, 20), point(40, 20), point(40, 40));
	
	private static List<Point> LAYOUTED_CONNECTION = 
		Arrays.asList(
				point(50, 70), point(80, 70), 
				point(80, 90), point(110, 90), 
				point(110, 60));
	
	private static List<Point> DIAGONAL_CONNECTION = 
		Arrays.asList(point(20, 20), point(80, 80));
	
	
	@Test
	public void testConnectionLength() {
		
		// when
		double length = ConnectionUtil.getLength(SIMPLE_CONNECTION);
		
		// then
		assertThat(length).isEqualTo(80.0);
	}

	@Test
	public void testConnectionLengthDiagonal() {
		
		// given 
		double expectedLength = Math.sqrt(Math.pow(60, 2.0) * 2);
		
		// when
		double length = ConnectionUtil.getLength(DIAGONAL_CONNECTION);
		
		// then
		assertThat(length).isEqualTo(expectedLength);
	}

	@Test
	public void testConnectionMidPoint() {
		
		// when
		Point midPoint = ConnectionUtil.getMidPoint(SIMPLE_CONNECTION);
		
		// then
		assertThat(midPoint).isEqualTo(point(50, 30));
	}
	
	@Test
	public void testConnectionMidPointIsWaypoint() {
		
		// when
		Point midPoint = ConnectionUtil.getMidPoint(SIMPLE_CONNECTION2);
		
		// then
		assertThat(midPoint).isEqualTo(point(40, 20));
	}

	@Test
	public void testConnectionMidPointDiagonal() {
		
		// when
		Point midPoint = ConnectionUtil.getMidPoint(DIAGONAL_CONNECTION);
		
		// then
		assertThat(midPoint).isEqualTo(point(50, 50));
	}
	
	@Test
	public void testPointAtLengthStart() {
		
		// when
		Point point = ConnectionUtil.getPointAtLength(SIMPLE_CONNECTION, 0);
		
		// then
		assertThat(point).isEqualTo(SIMPLE_CONNECTION.get(0));
	}
	
	@Test
	public void testPointAtLengthEnd() {
		
		// when
		Point point = ConnectionUtil.getPointAtLength(SIMPLE_CONNECTION, 80.0);
		
		// then
		assertThat(point).isEqualTo(SIMPLE_CONNECTION.get(2));
	}

	@Test
	public void testPointAtLength() {
		
		// when
		Point point = ConnectionUtil.getPointAtLength(SIMPLE_CONNECTION, 50.0);
		
		// then
		assertThat(point).isEqualTo(point(50, 40));
	}

	@Test
	public void testPointRelativeToLengthStart() {
		
		// when
		Point point = ConnectionUtil.getPointRelativeToLength(SIMPLE_CONNECTION, 0);
		
		// then
		assertThat(point).isEqualTo(SIMPLE_CONNECTION.get(0));
	}
	
	@Test
	public void testPointRelativeToLengthEnd() {
		
		// when
		Point point = ConnectionUtil.getPointRelativeToLength(SIMPLE_CONNECTION, 100.0);
		
		// then
		assertThat(point).isEqualTo(SIMPLE_CONNECTION.get(2));
	}

	@Test
	public void testPointRelativeToLength() {
		
		// when
		Point point = ConnectionUtil.getPointRelativeToLength(SIMPLE_CONNECTION, 50.0);
		
		// then
		assertThat(point).isEqualTo(point(50, 30));
	}
	
	@Test
	public void testPointRelativeToLengthDiagonal() {
		
		// when
		Point point = ConnectionUtil.getPointRelativeToLength(DIAGONAL_CONNECTION, 50.0);
		
		// then
		assertThat(point).isEqualTo(point(50, 50));
	}

	@Test
	public void testContainedInSegmentStart() {
		
		// given
		Vector v1 = vector(50.0, 50.0);
		Vector v2 = vector(40.0, 40.0);
		
		// when
		boolean contained = ConnectionUtil.isContainedInSegment(v1, v2, v1);
		
		// then
		assertThat(contained).isTrue();
	}
	
	@Test
	public void testContainedInSegmentEnd() {
		
		// given
		Vector v1 = vector(50.0, 50.0);
		Vector v2 = vector(40.0, 40.0);
		
		// when
		boolean contained = ConnectionUtil.isContainedInSegment(v1, v2, v2);
		
		// then
		assertThat(contained).isTrue();
	}

	@Test
	public void testContainedInSegmentNot() {
		
		// given
		Vector v1 = vector(50.0, 50.0);
		Vector v2 = vector(40.0, 40.0);
		
		// when
		boolean contained = ConnectionUtil.isContainedInSegment(v1, v2, vector(51.0, 51.0));
		
		// then
		assertThat(contained).isFalse();
	}
	
	@Test
	public void testContainedInSegment() {
		
		// given
		Vector v1 = vector(50.0, 50.0);
		Vector v2 = vector(40.0, 40.0);
		
		// when
		boolean contained = ConnectionUtil.isContainedInSegment(v1, v2, vector(43, 43));
		
		// then
		assertThat(contained).isTrue();
	}
	
	@Test
	public void testContainedInSegmentShouldHandleRoundingErrors() {
		
		// given
		Vector v1 = vector(118.0, 59.0);
		Vector v2 = vector(510.0, 264.0);
		
		// when
		boolean contained = ConnectionUtil.isContainedInSegment(v1, v2, vector(360.0, 185.0));
		
		// then
		assertThat(contained).isTrue();
	}
	
	@Test
	public void testContainedInSegmentShouldHandleRoundingErrors2() {
		
		// given
		Vector v1 = vector(390.0, 300.0);
		Vector v2 = vector(600.0, 242.0);
		
		// when
		boolean contained = ConnectionUtil.isContainedInSegment(v1, v2, vector(540.0, 258.0));
		
		// then
		assertThat(contained).isTrue();
	}
	
	@Test
	public void testContainedInSegmentBehindSegmentEnd() {
		
		// given
		Vector v1 = vector(20.0, 20.0);
		Vector v2 = vector(50.0, 20.0);
		
		// when
		boolean contained = ConnectionUtil.isContainedInSegment(v1, v2, vector(60, 20));
		
		// then
		assertThat(contained).isFalse();
	}
	
	@Test
	public void testContainedInSegmentBeforeSegmentStart() {
		
		// given
		Vector v1 = vector(20.0, 20.0);
		Vector v2 = vector(50.0, 20.0);
		
		// when
		boolean contained = ConnectionUtil.isContainedInSegment(v1, v2, vector(10, 20));
		
		// then
		assertThat(contained).isFalse();
	}
	
	@Test
	public void testClosestPointOnConnectionDiagonal() {
		
		// when
		Point point = ConnectionUtil.getClosestPointOnConnection(DIAGONAL_CONNECTION, point(40, 60));
		
		// then
		assertThat(point).isEqualTo(point(50, 50));
	}
	
	@Test
	public void testClosestPointOnConnectionCorner() {
		
		// when
		Point point = ConnectionUtil.getClosestPointOnConnection(SIMPLE_CONNECTION, point(60, 10));
		
		// then
		assertThat(point).isEqualTo(point(50, 20));
	}

	@Test
	public void testClosestPointOnConnectionCornerClose() {
		
		// when
		Point point = ConnectionUtil.getClosestPointOnConnection(SIMPLE_CONNECTION, point(50, 15));
		
		// then
		assertThat(point).isEqualTo(point(50, 20));
	}
	
	@Test
	public void testClosestPointOnConnectionAboveConnectionPart() {
		
		// when
		Point point = ConnectionUtil.getClosestPointOnConnection(SIMPLE_CONNECTION, point(30, 15));
		
		// then
		assertThat(point).isEqualTo(point(30, 20));
	}

	@Test
	public void testClosestPointOnConnectionInnerCorner() {
		
		// when
		Point point = ConnectionUtil.getClosestPointOnConnection(SIMPLE_CONNECTION, point(40, 30));
		
		// then
		assertThat(point).isEqualTo(point(40, 20));
	}

	@Test
	public void testClosestPointOnConnectionConflictingConnectionParts() {
		
		// when
		// point has same distance to two independent connection parts
		Point point = ConnectionUtil.getClosestPointOnConnection(LAYOUTED_CONNECTION, point(95, 70));
		
		// then
		// choose first close part
		assertThat(point).isEqualTo(point(80, 70));
	}
	
	@Test
	public void testClosestPointOnConnectionLeftOfConnectionPart() {
		
		// when
		Point point = ConnectionUtil.getClosestPointOnConnection(SIMPLE_CONNECTION, point(40, 60));
		
		// then
		assertThat(point).isEqualTo(point(50, 60));
	}
	
	@Test
	public void testClosestPointOnConnectionRightOfConnectionPart() {
		
		// when
		Point point = ConnectionUtil.getClosestPointOnConnection(SIMPLE_CONNECTION, point(100, 50));
		
		// then
		assertThat(point).isEqualTo(point(50, 50));
	}
}
