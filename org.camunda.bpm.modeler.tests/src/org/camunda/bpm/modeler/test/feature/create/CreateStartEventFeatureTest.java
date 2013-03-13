package org.camunda.bpm.modeler.test.feature.create;


import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.AddStartEventOperation.addStartEvent;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.camunda.bpm.modeler.test.util.operations.AddStartEventOperation;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.di.BPMNLabel;
import org.eclipse.bpmn2.di.BPMNShape;
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
		
		IRectangle laneBounds = LayoutUtil.getAbsoluteBounds(laneShape);
		
		// when
		addStartEvent(getDiagramTypeProvider())
			.atLocation(40, 20)
			.sized(36, 36)
			.toContainer((ContainerShape) laneShape)
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
