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
package org.camunda.bpm.modeler.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.model.Bpmn2ModelerFactory;
import org.camunda.bpm.modeler.core.preferences.Bpmn2Preferences;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Artifact;
import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.ChoreographyTask;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.ConditionalEventDefinition;
import org.eclipse.bpmn2.ConversationLink;
import org.eclipse.bpmn2.ConversationNode;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.DataObjectReference;
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.Expression;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.InputOutputSpecification;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.bpmn2.di.BpmnDiPackage;
import org.eclipse.bpmn2.di.ParticipantBandKind;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.core.runtime.Assert;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.dc.DcFactory;
import org.eclipse.dd.dc.Point;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class ModelHandler {

	private Bpmn2ResourceImpl resource;
	private Bpmn2Preferences prefs;
	
	private ModelHandler(Bpmn2ResourceImpl resource) {
		this.resource = resource;
	}

	void createDefinitionsIfMissing() {
		EList<EObject> contents = resource.getContents();

		if (contents.isEmpty() || !(contents.get(0) instanceof DocumentRoot)) {
			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(resource);

			if (domain != null) {
				final DocumentRoot docRoot = create(DocumentRoot.class);
				final Definitions definitions = create(Definitions.class);

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

	public Bpmn2Preferences getPreferences() {
		if (prefs == null)
			prefs = Bpmn2Preferences.getInstance(resource);
		return prefs;
	}
	
	public BPMNDiagram createDiagramType(final Bpmn2DiagramType diagramType, String targetNamespace) {
		BPMNDiagram diagram = null;
		switch (diagramType) {
		case PROCESS:
			diagram = createProcessDiagram("Default");
			break;
		case COLLABORATION:
			diagram = createCollaborationDiagram("Default");
			break;
		case CHOREOGRAPHY:
			diagram = createChoreographyDiagram("Default");
			break;
		}
		if (diagram!=null)
			((Definitions)diagram.eContainer()).setTargetNamespace(targetNamespace);
		
		return diagram;
	}
	
	public BPMNDiagram createProcessDiagram(final String name) {
	
		EList<EObject> contents = resource.getContents();
		ResourceSet rs = resource.getResourceSet();
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
					// the Process ID should be the same as the resource name
					String filename = resource.getURI().lastSegment();
					if (filename.contains("."))
						filename = filename.split("\\.")[0];
					process.setId( ModelUtil.generateID(process,resource,filename) );

					// create StartEvent
					StartEvent startEvent = create(StartEvent.class);
					process.getFlowElements().add(startEvent);
					
					// create SequenceFlow
					SequenceFlow flow = create(SequenceFlow.class);
					process.getFlowElements().add(flow);
					
					// create EndEvent
					EndEvent endEvent = create(EndEvent.class);
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
					getPreferences().applyBPMNDIDefaults(shape, null);
					
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
					getPreferences().applyBPMNDIDefaults(shape, null);

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
					
					Participant initiatingParticipant = create(Participant.class);
					initiatingParticipant.setName("Initiating Pool");
					initiatingParticipant.setProcessRef(initiatingProcess);
					
					Process nonInitiatingProcess = createProcess();
					nonInitiatingProcess.setName(name+" Non-initiating Process");
					
					Participant nonInitiatingParticipant = create(Participant.class);
					nonInitiatingParticipant.setName("Non-initiating Pool");
					nonInitiatingParticipant.setProcessRef(nonInitiatingProcess);
					
					collaboration.getParticipants().add(initiatingParticipant);
					collaboration.getParticipants().add(nonInitiatingParticipant);

					// create DI shapes

					boolean horz = getPreferences().isHorizontalDefault();
					// initiating pool
					BPMNShape shape = BpmnDiFactory.eINSTANCE.createBPMNShape();
					ModelUtil.setID(shape,resource);

					shape.setBpmnElement(initiatingParticipant);
					Bounds bounds = DcFactory.eINSTANCE.createBounds();
					if (horz) {
						bounds.setX(100);
						bounds.setY(100);
						bounds.setWidth(1000);
						bounds.setHeight(200);
					}
					else {
						bounds.setX(100);
						bounds.setY(100);
						bounds.setWidth(200);
						bounds.setHeight(1000);
					}
					shape.setBounds(bounds);
					shape.setIsHorizontal(horz);
					plane.getPlaneElement().add(shape);
					getPreferences().applyBPMNDIDefaults(shape, null);

					// non-initiating pool
					shape = BpmnDiFactory.eINSTANCE.createBPMNShape();
					ModelUtil.setID(shape,resource);

					shape.setBpmnElement(nonInitiatingParticipant);
					bounds = DcFactory.eINSTANCE.createBounds();
					if (horz) {
						bounds.setX(100);
						bounds.setY(400);
						bounds.setWidth(1000);
						bounds.setHeight(200);
					}
					else {
						bounds.setX(400);
						bounds.setY(100);
						bounds.setWidth(200);
						bounds.setHeight(1000);
					}
					shape.setBounds(bounds);
					shape.setIsHorizontal(horz);
					plane.getPlaneElement().add(shape);
					getPreferences().applyBPMNDIDefaults(shape, null);

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
					
					Participant initiatingParticipant = create(Participant.class);
					initiatingParticipant.setName(name+" Initiating Participant");
					
					Participant nonInitiatingParticipant = create(Participant.class);
					nonInitiatingParticipant.setName(name+" Non-initiating Participant");
					
					choreography.getParticipants().add(initiatingParticipant);
					choreography.getParticipants().add(nonInitiatingParticipant);
					
					ChoreographyTask task = create(ChoreographyTask.class);
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
					getPreferences().applyBPMNDIDefaults(taskShape, null);

					BPMNShape participantShape = BpmnDiFactory.eINSTANCE.createBPMNShape();
					ModelUtil.setID(participantShape,resource);
					participantShape.setBpmnElement(initiatingParticipant);
					participantShape.setChoreographyActivityShape(taskShape);
					participantShape.setParticipantBandKind(ParticipantBandKind.TOP_INITIATING);
					bounds = DcFactory.eINSTANCE.createBounds();
					bounds.setX(100);
					bounds.setY(100);
					bounds.setWidth(GraphicsUtil.CHOREOGRAPHY_WIDTH);
					bounds.setHeight(GraphicsUtil.CHOREOGRAPHY_PARTICIPANT_BAND_HEIGHT);
					participantShape.setBounds(bounds);
					plane.getPlaneElement().add(participantShape);
					getPreferences().applyBPMNDIDefaults(participantShape, null);

					participantShape = BpmnDiFactory.eINSTANCE.createBPMNShape();
					ModelUtil.setID(participantShape,resource);
					participantShape.setBpmnElement(nonInitiatingParticipant);
					participantShape.setChoreographyActivityShape(taskShape);
					participantShape.setParticipantBandKind(ParticipantBandKind.BOTTOM_NON_INITIATING);
					bounds = DcFactory.eINSTANCE.createBounds();
					bounds.setX(100);
					bounds.setY(100 + GraphicsUtil.CHOREOGRAPHY_HEIGHT - GraphicsUtil.CHOREOGRAPHY_PARTICIPANT_BAND_HEIGHT);
					bounds.setWidth(GraphicsUtil.CHOREOGRAPHY_WIDTH);
					bounds.setHeight(GraphicsUtil.CHOREOGRAPHY_PARTICIPANT_BAND_HEIGHT);
					participantShape.setBounds(bounds);
					plane.getPlaneElement().add(participantShape);
					getPreferences().applyBPMNDIDefaults(participantShape, null);

					plane.setBpmnElement(choreography);
					bpmnDiagram.setPlane(plane);
					getDefinitions().getDiagrams().add(bpmnDiagram);
				}
			});
		}
		return bpmnDiagram;
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
		
		Assert.isNotNull(target, "Target must not be null");
		Assert.isNotNull(elem, "Element must not be null");
		
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

	public ItemAwareElement addDataInputOutput(Object target, ItemAwareElement element) {
		if (element instanceof DataOutput)
			getOrCreateIOSpecification(target).getDataOutputs().add((DataOutput)element);
		else if (element instanceof DataInput)
			getOrCreateIOSpecification(target).getDataInputs().add((DataInput)element);
		else
			return null;
		return element;
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
			InputOutputSpecification ioSpec = create(InputOutputSpecification.class);
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

	public void moveDataObjectReference(DataObjectReference dataObjectReference, Object source, Object target) {
		FlowElementsContainer sourceContainer = getFlowElementContainer(source);
		FlowElementsContainer targetContainer = getFlowElementContainer(target);
		sourceContainer.getFlowElements().remove(dataObjectReference);
		targetContainer.getFlowElements().add(dataObjectReference);
	}
	
	public Participant addParticipant(BPMNDiagram bpmnDiagram) {
		Participant participant = null;
		Collaboration collaboration = getParticipantContainer(bpmnDiagram);
		if (collaboration!=null) {
			participant = create(Participant.class);
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
		Process process = create(Process.class);
		getDefinitions().getRootElements().add(process);
		return process;
	}
	
	public Process getOrCreateProcess(Participant participant) {
		if (participant == null) {
			participant = getInternalParticipant();
		}
		
		if (participant != null && participant.getProcessRef() != null) {
			return participant.getProcessRef();
		}
		
		Process process = null;
		
		if (participant == null) {
			List<Process> processes = getAll(Process.class);
			// not a collaboration, and only one process -> append it there
			process = processes.size() == 1 ? processes.get(0) : null; 
		}
		
		if (process == null) {
			process = create(Process.class);
			getDefinitions().getRootElements().add(process);
			if (participant != null) {
				participant.setProcessRef(process);
			}
		}

		return process;
	}

	public static Lane createLane(Lane targetLane) {
		Resource resource = targetLane.eResource();
		Lane lane = create(resource, Lane.class);

		if (targetLane.getChildLaneSet() == null) {
			targetLane.setChildLaneSet(create(resource, LaneSet.class));
		}

		LaneSet targetLaneSet = targetLane.getChildLaneSet();
		targetLaneSet.getLanes().add(lane);

		lane.getFlowNodeRefs().addAll(targetLane.getFlowNodeRefs());
		targetLane.getFlowNodeRefs().clear();

		return lane;
	}

	public Lane createLane(Object target) {
		Lane lane = create(Lane.class);
		FlowElementsContainer container = getFlowElementContainer(target);
		if (container.getLaneSets().isEmpty()) {
			LaneSet laneSet = create(LaneSet.class);
			laneSet.setName("Lane Set "+ModelUtil.getIDNumber( laneSet.getId() ));
			container.getLaneSets().add(laneSet);
		}
		container.getLaneSets().get(0).getLanes().add(lane);
		return lane;
	}

	public void laneToTop(Lane lane) {
		LaneSet laneSet = create(LaneSet.class);
		laneSet.getLanes().add(lane);
		Process process = getOrCreateProcess(getInternalParticipant());
		process.getLaneSets().add(laneSet);
	}

	public SequenceFlow createSequenceFlow(FlowNode source, FlowNode target) {
		SequenceFlow sequenceFlow = create(SequenceFlow.class);

		addFlowElement(source.eContainer(), sequenceFlow);
		sequenceFlow.setSourceRef(source);
		sequenceFlow.setTargetRef(target);
		return sequenceFlow;
	}

	public MessageFlow createMessageFlow(InteractionNode source, InteractionNode target) {
		MessageFlow messageFlow = null;
		Participant participant = getParticipant(source);
		if (participant!=null) {
			messageFlow = create(MessageFlow.class);
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
			link = create(ConversationLink.class);
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
		Association association = create(Association.class);
		addArtifact(e, association);
		association.setSourceRef(source);
		association.setTargetRef(target);
		return association;
	}
	
	 public DataAssociation createDataAssociation(BaseElement source, BaseElement target) {
	   DataAssociation dataAssocation = null;
	    if (source instanceof ItemAwareElement) {
	      dataAssocation = create(DataInputAssociation.class);
	      dataAssocation.getSourceRef().add((ItemAwareElement) source);
	      if (target instanceof Activity) {
	        Activity activity = (Activity) target;
	        activity.getDataInputAssociations().add((DataInputAssociation) dataAssocation);
	      } else if (target instanceof ThrowEvent) {
	        ThrowEvent throwEvent = (ThrowEvent) target;
	        throwEvent.getDataInputAssociation().add((DataInputAssociation) dataAssocation);
	      }
	    } else if (target instanceof ItemAwareElement) {
	      dataAssocation = create(DataOutputAssociation.class);
	      dataAssocation.setTargetRef((ItemAwareElement) target);
	       if (source instanceof Activity) {
	          Activity activity = (Activity) source;
	          activity.getDataOutputAssociations().add((DataOutputAssociation) dataAssocation);
	        } else if (source instanceof CatchEvent) {
	          CatchEvent throwEvent = (CatchEvent) source;
	          throwEvent.getDataOutputAssociation().add((DataOutputAssociation) dataAssocation);
	        }
	    }
	    return dataAssocation;
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
		Collaboration collaboration = create(Collaboration.class);
		getDefinitions().getRootElements().add(collaboration);
		return collaboration;
	}
	
	private Collaboration getOrCreateCollaboration() {
		Collaboration c = getCollaboration();
		if (c!=null)
			return c;
		
		final List<RootElement> rootElements = getDefinitions().getRootElements();
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(resource);
		final Collaboration collaboration = create(Collaboration.class);
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

		if (bpmnDiagram == null) {
			// return the first Collaboration or Choreography in the model hierarchy

			Definitions definitions = getDefinitions(bpmnDiagram);
			List<RootElement> rootElements = definitions.getRootElements();
			
			for (RootElement element : rootElements) {
				// yeah, Collaboration and Choreography are both instanceof Collaboration...
				if (element instanceof Collaboration) {
					return (Collaboration)element;
				}
			}
		} else {
			BaseElement be = bpmnDiagram.getPlane().getBpmnElement();
			if (be instanceof Collaboration) {
				return (Collaboration)be;
			}
		}
		
		return null;
	}

	/**
	 * Returns the {@link Definitions} object for a element.
	 * 
	 * @param element
	 * @return
	 */
	private Definitions getDefinitions(EObject element) {
		if (element == null) {
			return getAll(Definitions.class).get(0);
		} else
		if (element instanceof Definitions) {
			return (Definitions) element;
		} else {
			EObject parent = element.eContainer();
			if (parent == null) {
				throw new IllegalArgumentException("Argument <" + element + "> has no parent");
			} else {
				return getDefinitions(parent);
			}
		}
	}

	public Choreography createChoreography() {
		Choreography choreography = create(Choreography.class);
		getDefinitions().getRootElements().add(choreography);
		return choreography;
	}

	private void addCollaborationToRootElements(final List<RootElement> rootElements, final Collaboration collaboration) {
		Participant participant = create(Participant.class);
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
		Participant participant = create(Participant.class);
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
		return getDefinitions(resource);
	}
	
	public static Definitions getDefinitions(Resource resource) {
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
		try {
			resource.save(null);
		} catch (IOException e) {
			Activator.logError(e);
		}
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
		
		// common case that elements are dropped on the diagram in a collaboration
		// (e.g. for data objects / data stores)
		if (o instanceof Collaboration) {
			
			// make the object the definitions object
			// will handle drop on definitions later ...
			o = ((Collaboration) o).eContainer();
 		}
		
		if (o instanceof BPMNDiagram) {

			BPMNDiagram bpmnDiagram = (BPMNDiagram) o;
			BaseElement bpmnElement = bpmnDiagram.getPlane().getBpmnElement();
			
			if (bpmnElement instanceof FlowElementsContainer) {
				return (FlowElementsContainer) bpmnElement;
			} else {
				// we have no directly accessible flow elements container;
				// search the definitions for the next available process...
				// (later) 
				o = ((BPMNDiagram) o).eContainer();
			}
		}
		
		if (o instanceof Definitions) {
			Definitions definitions = (Definitions) o;
			
			// this is the fallback for random additions to the diagram
			Process process = getFirst(definitions.getRootElements(), Process.class);
			if (process == null) {
				process = create(Process.class);
				definitions.getRootElements().add(process);
				
				return process;
			} else { 
				return process;
			}
		}
		
		if (o instanceof Participant) {
			return getOrCreateProcess((Participant) o);
		}
		
		if (o instanceof SubProcess) {
			return (FlowElementsContainer) o;
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
			if (process==null) {
				if (collaboration.getParticipants().size()>0)
					return collaboration.getParticipants().get(0);
			}
			else {
				for (Participant p : collaboration.getParticipants()) {
					if (p.getProcessRef() != null && p.getProcessRef().equals(process)) {
						return p;
					}
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

	/**
	 * 
	 * @param elements
	 * @param cls
	 * @return
	 */
	protected static <T extends BaseElement, V extends BaseElement> T getFirst(List<V> elements, Class<T> cls) {
		
		for (BaseElement e : elements) {
			if (cls.isInstance(e)) {
				return cls.cast(e);
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getAll(final Class<T> class1) {
		return getAll(this.resource, class1);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getAll(Resource resource, final Class<T> class1) {
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

	public static DiagramElement findDIElement(Diagram diagram, BaseElement element) {
		BPMNDiagram bpmnDiagram = BusinessObjectUtil.getFirstElementOfType(diagram, BPMNDiagram.class);
		Assert.isNotNull(bpmnDiagram);
		
		// Process elements correspond to BPMNPlane DI elements
		BPMNPlane bpmnPlane = bpmnDiagram.getPlane();
		
		if (element.equals(bpmnPlane.getBpmnElement())) {
			return bpmnPlane;
		}
		
		List<DiagramElement> planeElement = bpmnPlane.getPlaneElement();
		
		for (DiagramElement diElement : planeElement) {
			if (diElement instanceof BPMNShape) {
				BPMNShape shape = (BPMNShape) diElement;
				if (element.equals(shape.getBpmnElement())) {
					return diElement;
				}
			} else
			if (diElement instanceof BPMNEdge) {
				BPMNEdge edge = (BPMNEdge) diElement;
				if (element.equals(edge.getBpmnElement())) {
					return diElement;
				}
			}
		}
		
		return null;
	}

	public BaseElement findElement(String id) {
		if (id==null || id.isEmpty())
			return null;
		
		List<BaseElement> baseElements = getAll(BaseElement.class);

		for (BaseElement be : baseElements) {
			if (id.equals(be.getId())) {
				return be;
			}
		}

		return null;
	}
	
	/**
	 * General-purpose factory method that sets appropriate default values for new objects.
	 */
	public EObject create(EClass eClass) {
		return create(this.resource, eClass);
	}

	public <T extends EObject> T create(Class<T> clazz) {
		return (T) create(this.resource, clazz);
	}

	public void initialize(EObject newObject) {
		ModelHandler.initialize(this.resource, newObject);
	}
	
	////////////////////////////////////////////////////////////////////////////
	// static versions of the above, for convenience
	
	public static EObject create(Resource resource, EClass eClass) {
		EObject newObject = null;
		EPackage pkg = eClass.getEPackage();
		EFactory factory = pkg.getEFactoryInstance();
		// make sure we don't try to construct abstract objects here!
		if (eClass == Bpmn2Package.eINSTANCE.getExpression())
			eClass = Bpmn2Package.eINSTANCE.getFormalExpression();
		newObject = factory.create(eClass);
		initialize(resource, newObject);
		return newObject;
	}

	public static <T extends EObject> T create(Resource resource, Class<T> clazz) {
		EObject newObject = null;
		EClassifier eClassifier = Bpmn2Package.eINSTANCE.getEClassifier(clazz.getSimpleName());
		if (eClassifier instanceof EClass) {
			EClass eClass = (EClass)eClassifier;
			newObject = Bpmn2ModelerFactory.getInstance().create(eClass);
		}
		else {
			// maybe it's a DI object type?
			eClassifier = BpmnDiPackage.eINSTANCE.getEClassifier(clazz.getSimpleName());
			if (eClassifier instanceof EClass) {
				EClass eClass = (EClass)eClassifier;
				newObject = BpmnDiFactory.eINSTANCE.create(eClass);
			}
		}
		
		if (newObject!=null) {
			initialize(resource, newObject);
		}

		return (T)newObject;
	}

	public static void initialize(Resource resource, EObject newObject) {
		// if the object has an "id", assign it now.
		ModelUtil.setID(newObject,resource);
	}
	
	/**
	 * Return the model 
	 * 
	 * @param e
	 * @param cls
	 * @return
	 */
	private static <T extends EObject> T getModel(PictogramElement e, Class<T> cls) {
		return BusinessObjectUtil.getFirstElementOfType(e, cls);
	}
	
	/**
	 * Return a model handler instance for the given resource
	 * 
	 * @param resource
	 * @return
	 */
	public static ModelHandler getInstance(Bpmn2ResourceImpl resource) {
		return new ModelHandler(resource);
	}
	
	/**
	 * Return a model handler instance for the given diagram
	 * 
	 * @param diagram
	 * @return
	 */
	public static ModelHandler getInstance(Diagram diagram) {

		BPMNDiagram model = getModel(diagram, BPMNDiagram.class);
		return getInstance(model);
	}

	/**
	 * Return model handler instance for the given object
	 * 
	 * @param object
	 * @return
	 */
	public static ModelHandler getInstance(EObject object) {
		
		Resource resource = object.eResource();
		if (resource instanceof Bpmn2ResourceImpl) {
			return new ModelHandler((Bpmn2ResourceImpl) resource);
		} else {
			throw new IllegalArgumentException("Model " + object + " not contained in resource of type " + Bpmn2ResourceImpl.class.getName());
		}
	}
}