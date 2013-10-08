package org.camunda.bpm.modeler.test.util.assertions;

import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.dd.dc.Bounds;
import org.fest.assertions.api.AbstractAssert;

public class BpmnShapeAssert extends AbstractAssert<BpmnShapeAssert, BPMNShape> {

	protected BpmnShapeAssert(BPMNShape actual) {
		super(actual, BpmnShapeAssert.class);
	}
	
	public BoundsAssert bounds() {
		Bounds bounds = actual.getBounds();
		
		return new BoundsAssert(bounds);
	}
	
	

}
