package org.eclipse.bpmn2.modeler.core.test.feature.create;


import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;

import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.di.BPMNLabel;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.test.util.operations.AddStartEventOperation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class CreateStartEventFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource
	public void testCreateOnLane() {
		
		// given
		Shape laneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_1");
		
		IRectangle laneBounds = LayoutUtil.getAbsoluteRectangle(laneShape);
		
		// when
		new AddStartEventOperation<StartEvent>(getDiagramTypeProvider())
			.atLocation(40, 20)
			.sized(36, 36)
			.inContainer((ContainerShape) laneShape)
			.execute();
		
		// then
		Point expectedPosition = point(
				laneBounds.getX() + 40 - 36 / 2, 
				laneBounds.getY() + 20 - 36 / 2);
		
		Shape startEventShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		BPMNShape bpmnShape = DIUtils.getShape(startEventShape);
		
		assertThat(startEventShape)
			.isContainedIn(laneShape)
			.position()
				.isEqualTo(expectedPosition);
		
		assertThat(bpmnShape)
			.isNotNull();
		
		assertThat(bpmnShape.getId())
			.isNotNull();
	}
}
