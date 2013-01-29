package org.eclipse.bpmn2.modeler.core.layout.util;

import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.graphiti.mm.algorithms.styles.Point;

public class Segment {
	Vector vector;
	double accummulatedLength;
	private Point segmentStart;
	private double startLength;
	private Point segmentEnd;
	
	public Segment(Point segmentStart, Point segmentEnd, Vector vector, double startLength, double accummulatedLength) {
		super();
		this.vector = vector;
		this.setStartLength(startLength);
		this.accummulatedLength = accummulatedLength;
		this.setSegmentStart(segmentStart);
		this.setSegmentEnd(segmentEnd);
	}

	public Vector getVector() {
		return vector;
	}

	public void setVector(Vector vector) {
		this.vector = vector;
	}
	/**
	 * @return length of connection up to this segment, including this segments length
	 */
	public double getAccummulatedLength() {
		return accummulatedLength;
	}

	public void setAccummulatedLength(double accummulatedLength) {
		this.accummulatedLength = accummulatedLength;
	}

	public Point getSegmentStart() {
		return segmentStart;
	}

	public void setSegmentStart(Point segmentStart) {
		this.segmentStart = segmentStart;
	}

	/**
	 * @return length of connection at start point of this segment
	 */
	public double getStartLength() {
		return startLength;
	}

	public void setStartLength(double startLength) {
		this.startLength = startLength;
	}

	public Point getSegmentEnd() {
		return segmentEnd;
	}

	public void setSegmentEnd(Point segmentEnd) {
		this.segmentEnd = segmentEnd;
	}
	
}
