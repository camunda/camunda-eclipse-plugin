package org.eclipse.bpmn2.modeler.core.layout.util;

import java.util.List;


public class SegmentInfo {
	List<Segment> segments;
	double fullLength;
	
	public List<Segment> getSegments() {
		return segments;
	}
	
	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}
	
	public double getFullLength() {
		return fullLength;
	}
	
	public void setFullLength(double fullLength) {
		this.fullLength = fullLength;
	}
	
	public SegmentInfo(List<Segment> segments, double fullLength) {
		super();
		this.segments = segments;
		this.fullLength = fullLength;
	}
	
}
