/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *
 * @author Ivar Meikas
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Artifact;
import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.ChoreographyTask;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.ConversationLink;
import org.eclipse.bpmn2.ConversationNode;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.InputOutputSpecification;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNLabel;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.bpmn2.di.ParticipantBandKind;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.dc.DcFactory;
import org.eclipse.dd.dc.Point;
import org.eclipse.dd.dc.impl.BoundsImpl;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class ModelHandler {
	public static final Bpmn2Factory FACTORY = Bpmn2Factory.eINSTANCE;

	Bpmn2ResourceImpl resource;

	ModelHandler() {
	}

	void createDefinitionsIfMissing() {
		EList<EObject> contents = resource.getContents();

		if (contents.isEmpty() || !(contents.get(0) instanceof DocumentRoot)) {
			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(resource);

			if (domain != null) {
				final DocumentRoot docRoot = FACTORY.createDocumentRoot();
				final Definitions definitions = FACTORY.createDefinitions();
				ModelUtil.setID(definitions,resource);
				
//				Choreography choreography = FACTORY.createChoreography();
//				ModelUtil.setID(choreography,resource);
//				Participant participant = FACTORY.createParticipant();
//				ModelUtil.setID(participant,resource);
//				participant.setName("Internal");
//				choreography.getParticipants().add(participant);
//				definitions.getRootElements().add(choreography);

				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						docRoot.setDefinitions(definitions);
						resource.getContents().add(docRoot);
					}
				});
				return;
			}
		}
	}

	public BPMNDiagram createDiagramType(final Bpmn2DiagramType diagramType) {
		switch (diagramType) {
		case PROCESS:
			return createProcessDiagram("Default");
		case COLLABORATION:
			return createCollaborationDiagram("Default");
		case CHOREOGRAPHY:
			return createChoreographyDiagram("Default");
		}
		return null;
	}
	
	public BPMNDiagram createProcessDiagram(final String name) {
	
		EList<EObject> contents = resource.getContents();
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(resource);
		final BPMNDiagram bpmnDiagram = BpmnDiFactory.eINSTANCE.createBPMNDiagram();

		if (domain != null) {
			domain.getCommandStack().execute(new RecordingCommand(domain) {
				@Override
				protected void doExecute() {
					BPMNPlane plane = BpmnDiFactory.eINSTANCE.createBPMNPlane();
					ModelUtil.setID(plane,resource);

					Process process = createProcess();
					process.setName(name+" Process");

					// create StartEvent
					StartEvent startEvent = FACTORY.createStartEvent();
					ModelUtil.setID(startEvent,resource);
					startEvent.setName("Start Event");
					process.getFlowElements().add(startEvent);
					
					// create SequenceFlow
					SequenceFlow flow = FACTORY.createSequenceFlow();
					ModelUtil.setID(flow,resource);
					process.getFlowElements().add(flow);
					
					// create EndEvent
					EndEvent endEvent = FACTORY.createEndEvent();
					ModelUtil.setID(endEvent,resource);
					endEvent.setName("End Event");
					process.getFlowElements().add(endEvent);
					
					// hook 'em up
					startEvent.getOutgoing().add(flow);
					endEvent.getIncoming().add(flow);
					flow.setSourceRef(startEvent);
					flow.setTargetRef(endEvent);

					// create DI shapes
					BPMNShape shape = BpmnDiFactory.eINSTANCE.createBPMNShape();
					ModelUtil.setID(shape,resource);

					// StartEvent shape
					shape.setBpmnElement(startEvent);
					Bounds bounds = DcFactory.eINSTANCE.createBounds();
					bounds.setX(100);
					bounds.setY(100);
					bounds.setWidth(GraphicsUtil.EVENT_SIZE);
					bounds.setHeight(GraphicsUtil.EVENT_SIZE);
					shape.setBounds(bounds);
					plane.getPlaneElement().add(shape);
					
					// SequenceFlow edge
					BPMNEdge edge = BpmnDiFactory.eINSTANCE.createBPMNEdge();
					edge.setBpmnElement(flow);
					edge.setSourceElement(shape);
					
					Point wp = DcFactory.eINSTANCE.createPoint();
					wp.setX(100+GraphicsUtil.EVENT_SIZE);
					wp.setY(100+GraphicsUtil.EVENT_SIZE/2);
					edge.getWaypoint().add(wp);
					
					wp = DcFactory.eINSTANCE.createPoint();
					wp.setX(500);
					wp.setY(100+GraphicsUtil.EVENT_SIZE/2);
					edge.getWaypoint().add(wp);
					
					plane.getPlaneElement().add(edge);

					// EndEvent shape
					shape = BpmnDiFactory.eINSTANCE.createBPMNShape();
					ModelUtil.setID(shape,resource);

					shape.setBpmnElement(endEvent);
					bounds = DcFactory.eINSTANCE.createBounds();
					bounds.setX(500);
					bounds.setY(100);
					bounds.setWidth(GraphicsUtil.EVENT_SIZE);
					bounds.setHeight(GraphicsUtil.EVENT_SIZE);
					shape.setBounds(bounds);
					plane.getPlaneElement().add(shape);

					edge.setTargetElement(shape);
					
					// add to BPMNDiagram
					plane.setBpmnElement(process);
					bpmnDiagram.setPlane(plane);
					bpmnDiagram.setName(name+" Process Diagram");
					getDefinitions().getDiagrams().add(bpmnDiagram);
				}
			});
		}
		return bpmnDiagram;
	}

	public BPMNDiagram createCollaborationDiagram(final String name) {
	
		EList<EObject> contents = resource.getContents();
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(resource);
		final BPMNDiagram bpmnDiagram = BpmnDiFactory.eINSTANCE.createBPMNDiagram();

		if (domain != null) {
			domain.getCommandStack().execute(new RecordingCommand(domain) {
				@Override
				protected void doExecute() {
					List<BPMNDiagram> diagrams = getAll(BPMNDiagram.class);
					BPMNPlane plane = BpmnDiFactory.eINSTANCE.createBPMNPlane();
					ModelUtil.setID(plane,resource);

					Collaboration collaboration = createCollaboration();
					collaboration.setName(name+" Collaboration");

					Process initiatingProcess = createProcess();
					initiatingProcess.setName(name+" Initiating Process");
					
					Participant initiatingParticipant = FACTORY.createParticipant();
					ModelUtil.setID(initiatingParticipant,resource);
					initiatingParticipant.setName("Initiating Pool");
					initiatingParticipant.setProcessRef(initiatingProcess);
					
					Process nonInitiatingProcess = createProcess();
					nonInitiatingProcess.setName(name+" Non-initiating Process");
					
					Participant nonInitiatingParticipant = FACTORY.createParticipant();
					ModelUtil.setID(nonInitiatingParticipant,resource);
					nonInitiatingParticipant.setName("Non-initiating Pool");
					nonInitiatingParticipant.setProcessRef(nonInitiatingProcess);
					
					collaboration.getParticipants().add(initiatingParticipant);
					collaboration.getParticipants().add(nonInitiatingParticipant);

					// create DI shapes
					
					// initiating pool
					BPMNShape shape = BpmnDiFactory.eINSTANCE.createBPMNShape();
					ModelUtil.setID(shape,resource);

					shape.setBpmnElement(initiatingParticipant);
					Bounds bounds = DcFactory.eINSTANCE.createBounds();
					bounds.setX(100);
					bounds.setY(100);
					bounds.setWidth(1000);
					bounds.setHeight(200);
					shape.setBounds(bounds);
					plane.getPlaneElement().add(shape);

					// non-initiating pool
					shape = BpmnDiFactory.eINSTANCE.createBPMNShape();
					ModelUtil.setID(shape,resource);

					shape.setBpmnElement(nonInitiatingParticipant);
					bounds = DcFactory.eINSTANCE.createBounds();
					bounds.setX(100);
					bounds.setY(400);
					bounds.setWidth(1000);
					bounds.setHeight(200);
					shape.setBounds(bounds);
					plane.getPlaneElement().add(shape);
					
					plane.setBpmnElement(collaboration);
					bpmnDiagram.setPlane(plane);
					bpmnDiagram.setName(name+" Collaboration Diagram");
					getDefinitions().getDiagrams().add(bpmnDiagram);
				}
			});
		}
		return bpmnDiagram;
	}
	

	public BPMNDiagram createChoreographyDiagram(final String name) {
	
		EList<EObject> contents = resource.getContents();
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(resource);
		final BPMNDiagram bpmnDiagram = BpmnDiFactory.eINSTANCE.createBPMNDiagram();

		if (domain != null) {
			domain.getCommandStack().execute(new RecordingCommand(domain) {
				@Override
				protected void doExecute() {
					List<BPMNDiagram> diagrams = getAll(BPMNDiagram.class);
					BPMNPlane plane = BpmnDiFactory.eINSTANCE.createBPMNPlane();
					ModelUtil.setID(plane,resource);

					Choreography choreography = createChoreography();
					choreography.setName(name+" Choreography");
					
					Participant initiatingParticipant = FACTORY.createParticipant();
					ModelUtil.setID(initiatingParticipant,resource);
					initiatingParticipant.setName(name+" Initiating Participant");

					Process initiatingProcess = createProcess();
					ModelUtil.setID(initiatingProcess,resource);
					initiatingProcess.setName(name+" Initiating Process");
					initiatingParticipant.setProcessRef(initiatingProcess);
					
					Participant nonInitiatingParticipant = FACTORY.createParticipant();
					ModelUtil.setID(nonInitiatingParticipant,resource);
					nonInitiatingParticipant.setName(name+" Non-initiating Participant");

					Process nonInitiatingProcess = createProcess();
					ModelUtil.setID(nonInitiatingProcess,resource);
					nonInitiatingProcess.setName(name+" Non-initiating Process");
					nonInitiatingParticipant.setProcessRef(nonInitiatingProcess);
					
					choreography.getParticipants().add(initiatingParticipant);
					choreography.getParticipants().add(nonInitiatingParticipant);
					
					ChoreographyTask task = FACTORY.createChoreographyTask();
					ModelUtil.setID(task,resource);
					task.setName(name+" Choreography Task");
					task.getParticipantRefs().add(initiatingParticipant);
					task.getParticipantRefs().add(nonInitiatingParticipant);
					task.setInitiatingParticipantRef(initiatingParticipant);
					choreography.getFlowElements().add(task);

					BPMNShape taskShape = BpmnDiFactory.eINSTANCE.createBPMNShape();
					ModelUtil.setID(taskShape,resource);

					taskShape.setBpmnElement(task);
					Bounds bounds = DcFactory.eINSTANCE.createBounds();
					bounds.setX(100);
					bounds.setY(100);
					bounds.setWidth(GraphicsUtil.CHOREOGRAPHY_WIDTH);
					bounds.setHeight(GraphicsUtil.CHOREOGRAPHY_HEIGHT);
					taskShape.setBounds(bounds);
					plane.getPlaneElement().add(taskShape);

					BPMNShape participantShape = BpmnDiFactory.eINSTANCE.createBPMNShape();
					ModelUtil.setID(participantShape,resource);
					participantShape.setBpmnElement(initiatingParticipant);
					participantShape.setChoreographyActivityShape(taskShape);
					participantShape.setParticipantBandKind(ParticipantBandKind.TOP_INITIATING);
					bounds = DcFactory.eINSTANCE.createBounds();
					bounds.setX(100);
					bounds.setY(100);
					bounds.setWidth(GraphicsUtil.CHOREOGRAPHY_WIDTH);
					bounds.setHeight(GraphicsUtil.PARTICIPANT_BAND_HEIGHT);
					participantShape.setBounds(bounds);
					plane.getPlaneElement().add(participantShape);
					
					participantShape = BpmnDiFactory.eINSTANCE.createBPMNShape();
					ModelUtil.setID(participantShape,resource);
					participantShape.setBpmnElement(nonInitiatingParticipant);
					participantShape.setChoreographyActivityShape(taskShape);
					participantShape.setParticipantBandKind(ParticipantBandKind.BOTTOM_NON_INITIATING);
					bounds = DcFactory.eINSTANCE.createBounds();
					bounds.setX(100);
					bounds.setY(100 + GraphicsUtil.CHOREOGRAPHY_HEIGHT - GraphicsUtil.PARTICIPANT_BAND_HEIGHT);
					bounds.setWidth(GraphicsUtil.CHOREOGRAPHY_WIDTH);
					bounds.setHeight(GraphicsUtil.PARTICIPANT_BAND_HEIGHT);
					participantShape.setBounds(bounds);
					plane.getPlaneElement().add(participantShape);

					plane.setBpmnElement(choreography);
					bpmnDiagram.setPlane(plane);
					getDefinitions().getDiagrams().add(bpmnDiagram);
				}
			});
		}
		return bpmnDiagram;
	}
	
	
	public static ModelHandler getInstance(Diagram diagram) throws IOException {
		return ModelHandlerLocator.getModelHandler(diagram.eResource());
	}

	/**
	 * @param <T>
	 * @param target
	 *            object that this element is being added to
	 * @param elem
	 *            flow element to be added
	 * @return
	 */
	public <T extends FlowElement> T addFlowElement(Object target, T elem) {
		FlowElementsContainer container = getFlowElementContainer(target);
		container.getFlowElements().add(elem);
		return elem;
	}

	/**
	 * @param <A>
	 * @param target
	 *            object that this artifact is being added to
	 * @param artifact
	 *            artifact to be added
	 * @return
	 */
	public <T extends Artifact> T addArtifact(Object target, T artifact) {
		Process process = getOrCreateProcess(getParticipant(target));
		process.getArtifacts().add(artifact);
		return artifact;
	}

	public <T extends RootElement> T addRootElement(T element) {
		getDefinitions().getRootElements().add(element);
		return element;
	}

	public DataOutput addDataOutput(Object target, DataOutput dataOutput) {
		getOrCreateIOSpecification(target).getDataOutputs().add(dataOutput);
		return dataOutput;
	}

	public DataInput addDataInput(Object target, DataInput dataInput) {
		getOrCreateIOSpecification(target).getDataInputs().add(dataInput);
		return dataInput;
	}

	public ConversationNode addConversationNode(BPMNDiagram bpmnDiagram, ConversationNode conversationNode) {
		Collaboration collaboration = getParticipantContainer(bpmnDiagram);
		if (collaboration!=null)
			collaboration.getConversations().add(conversationNode);
		return conversationNode;
	}

	private InputOutputSpecification getOrCreateIOSpecification(Object target) {
		Process process = getOrCreateProcess(getParticipant(target));
		if (process.getIoSpecification() == null) {
			InputOutputSpecification ioSpec = FACTORY.createInputOutputSpecification();
//			ioSpec.setId(EcoreUtil.generateUUID());
			ModelUtil.setID(ioSpec,resource);
			process.setIoSpecification(ioSpec);
		}
		return process.getIoSpecification();
	}

	public void moveFlowNode(FlowNode node, Object source, Object target) {
		FlowElementsContainer sourceContainer = getFlowElementContainer(source);
		FlowElementsContainer targetContainer = getFlowElementContainer(target);
		sourceContainer.getFlowElements().remove(node);
		targetContainer.getFlowElements().add(node);
		for (SequenceFlow flow : node.getOutgoing()) {
			sourceContainer.getFlowElements().remove(flow);
			targetContainer.getFlowElements().add(flow);
		}
	}

	public Participant addParticipant(BPMNDiagram bpmnDiagram) {
		Participant participant = null;
		Collaboration collaboration = getParticipantContainer(bpmnDiagram);
		if (collaboration!=null) {
			participant = FACTORY.createParticipant();
	//		participant.setId(EcoreUtil.generateUUID());
			ModelUtil.setID(participant,resource);
			collaboration.getParticipants().add(participant);
		}
		return participant;
	}

	@Deprecated
	public void moveLane(Lane movedLane, Participant targetParticipant) {
		Participant sourceParticipant = getParticipant(movedLane);
		moveLane(movedLane, sourceParticipant, targetParticipant);
	}

	public void moveLane(Lane movedLane, Participant sourceParticipant, Participant targetParticipant) {
		Process sourceProcess = getOrCreateProcess(sourceParticipant);
		Process targetProcess = getOrCreateProcess(targetParticipant);
		for (FlowNode node : movedLane.getFlowNodeRefs()) {
			moveFlowNode(node, sourceProcess, targetProcess);
		}
		if (movedLane.getChildLaneSet() != null && !movedLane.getChildLaneSet().getLanes().isEmpty()) {
			for (Lane lane : movedLane.getChildLaneSet().getLanes()) {
				moveLane(lane, sourceParticipant, targetParticipant);
			}
		}
	}

	public Process createProcess() {
		Process process = FACTORY.createProcess();
//		process.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(process,resource);
		process.setName("Process");
		getDefinitions().getRootElements().add(process);
		return process;
	}
	
	public Process getOrCreateProcess(Participant participant) {
		if (participant==null) {
			participant = getInternalParticipant();
		}
		if (participant!=null && participant.getProcessRef()!=null) {
			return participant.getProcessRef();
		}
		Process process = FACTORY.createProcess();
		ModelUtil.setID(process,resource);
		process.setName("Process for " + participant.getName());
		getDefinitions().getRootElements().add(process);
		if (participant!=null) {
			participant.setProcessRef(process);
		}
		return process;
	}

	public Lane createLane(Lane targetLane) {
		Lane lane = FACTORY.createLane();
//		lane.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(lane,resource);

		if (targetLane.getChildLaneSet() == null) {
			targetLane.setChildLaneSet(ModelHandler.FACTORY.createLaneSet());
		}

		LaneSet targetLaneSet = targetLane.getChildLaneSet();
		targetLaneSet.getLanes().add(lane);

		lane.getFlowNodeRefs().addAll(targetLane.getFlowNodeRefs());
		targetLane.getFlowNodeRefs().clear();

		return lane;
	}

	public Lane createLane(Object target) {
		Lane lane = FACTORY.createLane();
//		lane.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(lane,resource);
		FlowElementsContainer container = getFlowElementContainer(target);
		if (container.getLaneSets().isEmpty()) {
			LaneSet laneSet = FACTORY.createLaneSet();
//			laneSet.setId(EcoreUtil.generateUUID());
			container.getLaneSets().add(laneSet);
		}
		container.getLaneSets().get(0).getLanes().add(lane);
		ModelUtil.setID(lane);
		return lane;
	}

	public void laneToTop(Lane lane) {
		LaneSet laneSet = FACTORY.createLaneSet();
//		laneSet.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(laneSet,resource);
		laneSet.getLanes().add(lane);
		Process process = getOrCreateProcess(getInternalParticipant());
		process.getLaneSets().add(laneSet);
	}

	public SequenceFlow createSequenceFlow(FlowNode source, FlowNode target) {
		SequenceFlow sequenceFlow = FACTORY.createSequenceFlow();
//		sequenceFlow.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(sequenceFlow,resource);

		addFlowElement(source, sequenceFlow);
		sequenceFlow.setSourceRef(source);
		sequenceFlow.setTargetRef(target);
		return sequenceFlow;
	}

	public MessageFlow createMessageFlow(InteractionNode source, InteractionNode target) {
		MessageFlow messageFlow = null;
		Participant participant = getParticipant(source);
		if (participant!=null) {
			messageFlow = FACTORY.createMessageFlow();
			ModelUtil.setID(messageFlow,resource);
			messageFlow.setSourceRef(source);
			messageFlow.setTargetRef(target);
			if (participant.eContainer() instanceof Collaboration)
				((Collaboration)participant.eContainer()).getMessageFlows().add(messageFlow);
		}
		return messageFlow;
	}

	public ConversationLink createConversationLink(InteractionNode source, InteractionNode target) {
		ConversationLink link = null;
		Participant participant = getParticipant(source);
		if (participant!=null) {
			link = FACTORY.createConversationLink();
			link.setSourceRef(source);
			link.setTargetRef(target);
			if (participant.eContainer() instanceof Collaboration)
				((Collaboration)participant.eContainer()).getConversationLinks().add(link);
		}
		return link;
	}

	public Association createAssociation(BaseElement source, BaseElement target) {
		BaseElement e = null;
		if (getParticipant(source) != null) {
			e = source;
		} else if (getParticipant(target) != null) {
			e = target;
		} else {
			e = getInternalParticipant();
		}
		Association association = FACTORY.createAssociation();
		addArtifact(e, association);
//		association.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(association,resource);
		association.setSourceRef(source);
		association.setTargetRef(target);
		return association;
	}

	private Collaboration getCollaboration() {
		final List<RootElement> rootElements = getDefinitions().getRootElements();

		for (RootElement element : rootElements) {
			if (element instanceof Collaboration) {
				return (Collaboration) element;
			}
		}
		return null;
	}
	
	public Collaboration createCollaboration() {
		Collaboration collaboration = FACTORY.createCollaboration();
		ModelUtil.setID(collaboration,resource);
		collaboration.setName("Collaboration");
		getDefinitions().getRootElements().add(collaboration);
		return collaboration;
	}
	
	private Collaboration getOrCreateCollaboration() {
		Collaboration c = getCollaboration();
		if (c!=null)
			return c;
		
		final List<RootElement> rootElements = getDefinitions().getRootElements();
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(resource);
		final Collaboration collaboration = FACTORY.createCollaboration();
//		collaboration.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(collaboration,resource);
		if (domain != null) {
			domain.getCommandStack().execute(new RecordingCommand(domain) {

				@Override
				protected void doExecute() {
					addCollaborationToRootElements(rootElements, collaboration);
				}
			});
		}
		return collaboration;
	}
	
	private Collaboration getParticipantContainer(BPMNDiagram bpmnDiagram) {
		if (bpmnDiagram==null) {
			// return the first Collaboration or Choreography in the model hierarchy
			List<RootElement> rootElements = getDefinitions().getRootElements();
			for (RootElement element : rootElements) {
				// yeah, Collaboration and Choreography are both instanceof Collaboration...
				if (element instanceof Collaboration || element instanceof Choreography) {
					return (Collaboration)element;
				}
			}
		}
		else {
			BaseElement be = bpmnDiagram.getPlane().getBpmnElement();
			if (be instanceof Collaboration || be instanceof Choreography) {
				return (Collaboration)be;
			}
		}
		return null;
	}
	
	public Choreography createChoreography() {
		Choreography choreography = FACTORY.createChoreography();
		ModelUtil.setID(choreography,resource);
		choreography.setName("Choreography");
		getDefinitions().getRootElements().add(choreography);
		return choreography;
	}

	private void addCollaborationToRootElements(final List<RootElement> rootElements, final Collaboration collaboration) {
		Participant participant = FACTORY.createParticipant();
//		participant.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(participant,resource);
		participant.setName("Internal");
		for (RootElement element : rootElements) {
			if (element instanceof Process) {
				participant.setProcessRef((Process) element);
				break;
			}
		}
		collaboration.getParticipants().add(participant);
		rootElements.add(collaboration);
	}

	private void addChoreographyToRootElements(final List<RootElement> rootElements, final Choreography choreography) {
		Participant participant = FACTORY.createParticipant();
//		participant.setId(EcoreUtil.generateUUID());
		ModelUtil.setID(participant,resource);
		participant.setName("Internal");
		for (RootElement element : rootElements) {
			if (element instanceof Process) {
				participant.setProcessRef((Process) element);
				break;
			}
		}
		choreography.getParticipants().add(participant);
		rootElements.add(choreography);
	}

	public Bpmn2ResourceImpl getResource() {
		return resource;
	}

	public Definitions getDefinitions() {
		return (Definitions) resource.getContents().get(0).eContents().get(0);
	}

	public void save() {
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(resource);
		if (domain != null) {
			domain.getCommandStack().execute(new RecordingCommand(domain) {
				@Override
				protected void doExecute() {
					saveResource();
				}
			});
		} else {
			saveResource();
		}
	}

	private void saveResource() {
		fixZOrder();
		try {
			resource.save(null);
		} catch (IOException e) {
			Activator.logError(e);
		}
	}

	private void fixZOrder() {
		final List<BPMNDiagram> diagrams = getAll(BPMNDiagram.class);
		for (BPMNDiagram bpmnDiagram : diagrams) {
			fixZOrder(bpmnDiagram);
		}

	}

	private void fixZOrder(BPMNDiagram bpmnDiagram) {
		EList<DiagramElement> elements = (EList<DiagramElement>) bpmnDiagram.getPlane().getPlaneElement();
		ECollections.sort(elements, new DIZorderComparator());
	}

	void loadResource() {
		try {
			resource.load(null);
		} catch (IOException e) {
			Activator.logError(e);
		}
	}

	public Participant getInternalParticipant() {
		Collaboration collaboration = getParticipantContainer(null);
		if (collaboration!=null && collaboration.getParticipants().size()>0) {
			return collaboration.getParticipants().get(0);
		}
		return null;
	}

	public FlowElementsContainer getFlowElementContainer(Object o) {
		if (o == null) {
			return getOrCreateProcess(getInternalParticipant());
		}
		if (o instanceof Diagram) {
	        o = BusinessObjectUtil.getFirstElementOfType((Diagram)o, BPMNDiagram.class);
		}
		if (o instanceof BPMNDiagram) {
			BaseElement be = ((BPMNDiagram)o).getPlane().getBpmnElement();
			if (be instanceof FlowElementsContainer)
				return (FlowElementsContainer)be;
		}
		if (o instanceof Participant) {
			return getOrCreateProcess((Participant) o);
		}
		return findElementOfType(FlowElementsContainer.class, o);
	}

	public Participant getParticipant(final Object o) {
		if (o == null) {
			return getInternalParticipant();
		}
		
		if (o instanceof Diagram) {
	        BPMNDiagram bpmnDiagram = BusinessObjectUtil.getFirstElementOfType((Diagram)o, BPMNDiagram.class);
	        Collaboration collaboration = getParticipantContainer(bpmnDiagram);
			if (collaboration!=null && collaboration.getParticipants().size()>0) {
				return collaboration.getParticipants().get(0);
			}
			return null;
		}

		Object object = o;
		if (o instanceof Shape) {
			object = BusinessObjectUtil.getFirstElementOfType((PictogramElement) o, BaseElement.class);
		}

		if (object instanceof Participant) {
			return (Participant) object;
		}

		Process process = findElementOfType(Process.class, object);
		
		Collaboration collaboration = getParticipantContainer(null);
		if (collaboration!=null) {
			for (Participant p : collaboration.getParticipants()) {
				if (p.getProcessRef() != null && p.getProcessRef().equals(process)) {
					return p;
				}
			}
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseElement> T findElementOfType(Class<T> clazz, Object from) {
		if (!(from instanceof BaseElement)) {
			return null;
		}

		if (clazz.isAssignableFrom(from.getClass())) {
			return (T) from;
		}

		return findElementOfType(clazz, ((BaseElement) from).eContainer());
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getAll(final Class<T> class1) {
		ArrayList<T> l = new ArrayList<T>();
		TreeIterator<EObject> contents = resource.getAllContents();
		for (; contents.hasNext();) {
			Object t = contents.next();
			if (class1.isInstance(t)) {
				l.add((T) t);
			}
		}
		return l;
	}

	public DiagramElement findDIElement(Diagram diagram, BaseElement element) {
		List<BPMNDiagram> diagrams = getAll(BPMNDiagram.class);

		for (BPMNDiagram d : diagrams) {
			List<DiagramElement> planeElement = d.getPlane().getPlaneElement();

			String id = element.getId();
			if (id!=null) {
				for (DiagramElement elem : planeElement) {
					if (elem instanceof BPMNShape && 
							id.equals(((BPMNShape) elem).getBpmnElement().getId())) {
						return (elem);
					} else if (elem instanceof BPMNEdge &&
							id.equals(((BPMNEdge) elem).getBpmnElement().getId())) {
						return (elem);
					}
				}
			}
		}

		return null;
	}

}
