/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.camunda.bpm.modeler.test.importer.collaboration;

import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.elementOfType;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.test.importer.AbstractImportBpmnModelTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.StringUtil;
import org.eclipse.bpmn2.Participant;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportCollaborationTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testImportNoLanes() {
		// when
		importDiagram();
		
		EList<Shape> children = diagram.getChildren();
		assertThat(diagram).hasContainerShapeChildCount(2);
		
		Shape pool1 = children.get(0);
		Shape pool2 = children.get(1);

		assertThat(pool1)
			.isContainerShape()
			.isLinkedTo(elementOfType(Participant.class));
		
		assertThat(pool2)
			.isContainerShape()
			.isLinkedTo(elementOfType(Participant.class));
	}

	@Test
	@DiagramResource
	public void testImportNoLanesFlowNodes() {
		// when
		importDiagram();

		EList<Shape> children = diagram.getChildren();
		
		// then
		// diagram should have 2 participants + 2 shape labels + 3 sequence flow labels 
		assertThat(diagram)
			.hasContainerShapeChildCount(7);
		
		// pools should be first, labels should be later
		Shape pool1 = children.get(0);
		Shape pool2 = children.get(1);

		assertThat(pool1)
			.isContainerShape()
			.isLinkedTo(elementOfType(Participant.class));
		
		assertThat(pool2)
			.isContainerShape()
			.isLinkedTo(elementOfType(Participant.class));
	}
	
	@Test
	@DiagramResource
	public void testImportNestedLanes() {
		// when
		importDiagram();

		assertThat(diagram).hasContainerShapeChildCount(2);
	}

	@Test
	@DiagramResource
	public void testImportCollapsedPool() {
		// when
		importDiagram();

		assertThat(diagram).hasContainerShapeChildCount(2);
	}

	@Test
	@DiagramResource
	public void testImportCollapsedPoolProcess() {
		// when
		importDiagram();

		assertThat(diagram).hasContainerShapeChildCount(2);
	}
	
	@Test
	@DiagramResource
	public void testImportNestedLanesFlowNodes() {
		// when
		importDiagram();

		EList<Shape> children = diagram.getChildren();
		
		// then
		// diagram should have 2 participants + 3 shape labels + 5 sequence flow labels
		assertThat(diagram)
			.hasContainerShapeChildCount(10);
		
		ContainerShape pool1 = (ContainerShape) children.get(0);
		assertThat(pool1.getChildren()).hasSize(3);
	}
	
	@Test
	@DiagramResource
	public void testImportNestedLanesUnreferencedFlowNodes() {
		// when
		importDiagramIgnoreWarnings();
		
		// Unreferenced nodes are supposed to be drawn on the participant
		
		// then
		// diagram should have 2 participants + 3 shape labels + 5 sequence flow labels
		assertThat(diagram)
			.hasContainerShapeChildCount(10);

		// Assert that unreferenced flow nodes are in the diagram
		assertThat(StringUtil.toDetailsString(diagram))
			.contains("UserTask_2")
			.contains("EndEvent_1");
	}
	
	@Test
	@DiagramResource
	public void testImportLanes() {
		// when
		importDiagram();

		assertThat(diagram).hasContainerShapeChildCount(2);
	}
	
	@Test
	@DiagramResource
	public void testImportLanesFlowNodes() {
		// when
		importDiagram();

		// then
		// diagram should have 2 participants + 3 shape labels + 3 sequence flow labels
		assertThat(diagram)
			.hasContainerShapeChildCount(8);
	}

	@Test
	@DiagramResource
	public void testImportLanesUnreferencedFlowNodes() {
		// when
		importDiagramIgnoreWarnings();

		// then
		// diagram should have 2 participants + 3 shape labels + 3 sequence flow labels
		assertThat(diagram)
			.hasContainerShapeChildCount(8);
		
		// Assert that unreferenced flow nodes are in the diagram
		assertThat(StringUtil.toDetailsString(diagram))
			.contains("EndEvent_1");
	}
}
