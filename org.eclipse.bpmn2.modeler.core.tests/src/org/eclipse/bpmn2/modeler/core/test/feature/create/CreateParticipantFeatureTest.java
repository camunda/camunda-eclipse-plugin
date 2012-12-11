package org.eclipse.bpmn2.modeler.core.test.feature.create;

import static org.eclipse.bpmn2.modeler.core.test.util.operations.CreateParticipantOperation.createParticipant;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.ShapeUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * @author adrobisch
 */
public class CreateParticipantFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource
	public void testParticipantToEmptyDefinitions() {
		createAndCheckSingleParticipant();
	}

	private void createAndCheckSingleParticipant() {
		assertThat(ModelUtil.getAllRootElements(getDefinitions(), Collaboration.class)).hasSize(0);
		
		createParticipant(20, 20, 400, 200, getDiagram(), getDiagramTypeProvider()).execute();
		
		assertThat(ModelUtil.getAllRootElements(getDefinitions(), Collaboration.class)).hasSize(1);
		
		Collaboration collaboration = ModelUtil.getAllRootElements(getDefinitions(), Collaboration.class).get(0);
		assertThat(collaboration.getParticipants()).hasSize(1);
		
		BPMNDiagram bpmnDiagram = BusinessObjectUtil.getFirstElementOfType(getDiagram(), BPMNDiagram.class);
		
		assertThat(bpmnDiagram).isNotNull();
		assertThat(bpmnDiagram.getPlane().getBpmnElement()).isEqualTo(collaboration);
	}
	
	@Test
	@DiagramResource
	public void testMultipleParticipants() {
		createParticipant(20, 20, 400, 100, getDiagram(), getDiagramTypeProvider()).execute();

		assertThat(ModelUtil.getAllRootElements(getDefinitions(), Collaboration.class)).hasSize(1);
		
		Collaboration collaboration = ModelUtil.getAllRootElements(getDefinitions(), Collaboration.class).get(0);
		assertThat(collaboration.getParticipants()).hasSize(2);

		List<Process> processes = ModelUtil.getAllRootElements(getDefinitions(), org.eclipse.bpmn2.Process.class);
		assertThat(processes).hasSize(1); // process will be created when the first flow element is added
		
		Shape participantShape = ShapeUtil.findShapeByBusinessObjectId(getDiagram(), "Participant_1");
		DiagramElement participantDi = DIUtils.findDiagramElement(getDefinitions().getDiagrams(), BusinessObjectUtil.getFirstBaseElement(participantShape));
		assertThat(participantDi).isNotNull();
	}
	
	@Test
	@DiagramResource
	public void testParticipantToNonCollaborationProcess() {
		List<Process> beforeProcesses = ModelUtil.getAllRootElements(getDefinitions(), org.eclipse.bpmn2.Process.class);
		assertThat(beforeProcesses).hasSize(1);
		
		Process process = beforeProcesses.get(0);
		
		createAndCheckSingleParticipant();
		
		List<Process> afterProcesses = ModelUtil.getAllRootElements(getDefinitions(), org.eclipse.bpmn2.Process.class);
		assertThat(afterProcesses).hasSize(1);
		assertThat(afterProcesses.get(0)).isEqualTo(process);
		
		Shape taskShape = ShapeUtil.findShapeByBusinessObjectId(getDiagram(), "Task_1");
		DiagramElement taskDi = DIUtils.findDiagramElement(getDefinitions().getDiagrams(), BusinessObjectUtil.getFirstBaseElement(taskShape));
		assertThat(taskDi).isNotNull();

		// Shapes should be moved into partcipant shape
		Shape participantShape = getDiagram().getChildren().get(0);
		assertThat(taskShape.getContainer()).isEqualTo((ContainerShape) participantShape);
	}
}
