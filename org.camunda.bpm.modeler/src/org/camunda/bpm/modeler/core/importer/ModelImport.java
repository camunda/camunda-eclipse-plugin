/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.camunda.bpm.modeler.core.importer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.importer.handlers.AbstractDiagramElementHandler;
import org.camunda.bpm.modeler.core.importer.handlers.AbstractShapeHandler;
import org.camunda.bpm.modeler.core.importer.handlers.ArtifactShapeHandler;
import org.camunda.bpm.modeler.core.importer.handlers.AssociationShapeHandler;
import org.camunda.bpm.modeler.core.importer.handlers.DataInputAssociationShapeHandler;
import org.camunda.bpm.modeler.core.importer.handlers.DataInputShapeHandler;
import org.camunda.bpm.modeler.core.importer.handlers.DataObjectReferenceShapeHandler;
import org.camunda.bpm.modeler.core.importer.handlers.DataOutputAssociationShapeHandler;
import org.camunda.bpm.modeler.core.importer.handlers.DataOutputShapeHandler;
import org.camunda.bpm.modeler.core.importer.handlers.DatastoreReferenceShapeHandler;
import org.camunda.bpm.modeler.core.importer.handlers.EventShapeHandler;
import org.camunda.bpm.modeler.core.importer.handlers.FlowNodeShapeHandler;
import org.camunda.bpm.modeler.core.importer.handlers.GatewayShapeHandler;
import org.camunda.bpm.modeler.core.importer.handlers.LaneShapeHandler;
import org.camunda.bpm.modeler.core.importer.handlers.MessageFlowShapeHandler;
import org.camunda.bpm.modeler.core.importer.handlers.ParticipantShapeHandler;
import org.camunda.bpm.modeler.core.importer.handlers.SequenceFlowHandler;
import org.camunda.bpm.modeler.core.importer.handlers.SubProcessShapeHandler;
import org.camunda.bpm.modeler.core.importer.handlers.TaskShapeHandler;
import org.camunda.bpm.modeler.core.importer.util.ErrorLogger;
import org.camunda.bpm.modeler.core.importer.util.ModelHelper;
import org.camunda.bpm.modeler.core.layout.util.ConversionUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.core.utils.ScrollUtil;
import org.camunda.bpm.modeler.core.utils.transform.Transformer;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Artifact;
import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.DataObjectReference;
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.DataStoreReference;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.InputOutputSpecification;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.xmi.XMIException;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.platform.IDiagramContainer;
import org.xml.sax.SAXException;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 */
public class ModelImport {
	
	protected IDiagramContainer diagramContainer;

	protected Bpmn2Resource bpmnResource;
	protected Resource diagramResource;	
	
	// map collecting all DiagramElements (BPMN DI) indexed by the IDs of the ProcessElements they reference. 
	protected Map<String, DiagramElement> diagramElementMap = new HashMap<String, DiagramElement>();
	
	// list collecting DI elements that do not reference bpmn model elements. (for instance, labels only)
	protected List<DiagramElement> nonModelElements = new ArrayList<DiagramElement>();
	
	// list collecting the created PictogramElements (Graphiti) indexed by bpmn model elements
	protected HashMap<BaseElement, PictogramElement> pictogramElements = new HashMap<BaseElement, PictogramElement>();
	
	// list of exceptions classified as warnings which occurred during the import
	protected List<ImportException> warnings = new ArrayList<ImportException>();
	
	// list of deferred actions
	protected List<DeferredAction<?>> deferredActions = new ArrayList<DeferredAction<?>>();
	
	// the collarboration element  if present in current definitions
	protected Collaboration collaboration = null;
	
	// the process elements found in current definitions
	protected ArrayList<Process> processes = new ArrayList<Process>();
	
	// initial bounds of the diagram
	IRectangle importBounds = ConversionUtil.rect(0, 0, 0, 0); 
	
	// the resulting diagram
	protected Diagram rootDiagram; 
	
	// flag to decide if the import should add a scroll shape
	protected boolean withScrollShape = true;
	
	public ModelImport(IDiagramContainer diagramContainer, Bpmn2Resource bpmnResource, Resource diagramResource, boolean withScrollShape) {
		this(diagramContainer, bpmnResource, diagramResource);
		this.withScrollShape = withScrollShape;
	}
	
	public ModelImport(IDiagramContainer diagramContainer, Bpmn2Resource bpmnResource, Resource diagramResource) {
		
		this.diagramContainer = diagramContainer;
		
		this.bpmnResource = bpmnResource;
		this.diagramResource = diagramResource;

		// log xml loading errors
		logResourceErrors(bpmnResource);
	}
	
	public void execute() {
		long time = System.currentTimeMillis();
		
		try {
			EList<EObject> contents = bpmnResource.getContents();
			
			if (contents.isEmpty()) {
				throw new ResourceImportException("No document root in resource bundle");
			} else {
				DocumentRoot documentRoot = (DocumentRoot) contents.get(0);
				handleDocumentRoot(documentRoot);
				
				if (contents.size() > 1) {
					// TODO: is there a possibility for a resource to have multiple DocumentRoots?
					InvalidContentException exception = new InvalidContentException("Multiple document roots in resource");
					log(exception);
				}
			}
		} finally {
			time = System.currentTimeMillis() - time;
			// System.out.println(String.format("Bpmn2Editor import model in %sms", time));
		}
	}
		
	protected void handleDocumentRoot(DocumentRoot documentRoot) {
		Definitions definitions = documentRoot.getDefinitions();
		if (definitions == null) {
			throw new InvalidContentException("Document Root has no definitions", documentRoot);
		} else {
			handleDefinitions(definitions);
		}
	}

	protected void handleDefinitions(Definitions definitions) {
		// first we get the root elements
		
		List<RootElement> rootElements = definitions.getRootElements();
		
		for (RootElement rootElement: rootElements) {
			if (rootElement instanceof Process) {
				processes.add((Process) rootElement);
			} else if (rootElement instanceof Collaboration) {
				if (collaboration != null) {
					UnsupportedFeatureException exception = new UnsupportedFeatureException("Multiple collaborations not supported. Displaying first one only", definitions);
					log(exception);
				} else {
					collaboration = (Collaboration) rootElement;
				}
			} else {
				// are there unhandeled root elements?
			}
		}
		
		// next we process the DI diagrams and associate them with the process elements
		List<BPMNDiagram> diagrams = definitions.getDiagrams();
		for (BPMNDiagram bpmnDiagram : diagrams) {
			handleDIBpmnDiagram(bpmnDiagram);
		}
		
		// iterates over all elements in the diagram -> may be bad but is there another solution?
		
		// add all ids to the mapping table so that they won't be used when 
		// new ids are generated later
		TreeIterator<EObject> iter = definitions.eAllContents();
		while (iter.hasNext()) {
			ModelUtil.addID(iter.next());
		}

		// we create the bpmn diagram to work on
		BPMNDiagram bpmnDiagram = getOrCreateDiagram(diagrams);
		
		// we have no root process or collaboration
		// so we are going to create one
		if (collaboration == null && processes.isEmpty()) {
			Process defaultProcess = createDefaultDiagramContent(definitions, bpmnDiagram);
			processes.add(defaultProcess);
		}
		
		// at this point we have either a collaboration or 
		// at minimum one process to work with
		
		ensureBPMNDiagramLinked(bpmnDiagram, collaboration, processes);
		
		// and create the graphiti diagram
		this.rootDiagram = createEditorRootDiagram(bpmnDiagram, collaboration, processes, definitions);
		
		// next, process the BPMN model elements and start building the Graphiti diagram
		// first check if we display a single process or collaboration

		if (collaboration != null) {
			// we display a collaboration
			handleCollaboration(collaboration, rootDiagram);
		} else {
			// we display one or more processes
			for (Process process : processes) {
				handleProcess(process, rootDiagram);
			}
		}
		
		// handle deferred rendering of, e.g. associations and data associations
		handleDeferredActions();
		
		// finally layout all elements
		performLayout();
		
		if (withScrollShape) {
			addScrollShape();
		}
	}
	
	protected Shape addScrollShape() {
		return ScrollUtil.addScrollShape(rootDiagram, importBounds, false, 0, 0);
	}
	
	protected void handleDeferredActions() {
		for (DeferredAction<?> action: deferredActions) {
			action.handle();
		}
	}

	/**
	 * Ensure that the plane is properly linked with collaboration or process, respectively.
	 * 
	 * @param bpmnDiagram
	 * @param collaboration
	 * @param processes
	 */
	protected void ensureBPMNDiagramLinked(BPMNDiagram bpmnDiagram, Collaboration collaboration, List<Process> processes) {
		BPMNPlane bpmnPlane = bpmnDiagram.getPlane();
		
		BaseElement bpmnElement = bpmnPlane.getBpmnElement();
		
		if (collaboration != null) {
			if (!collaboration.equals(bpmnElement)) {
				log(new ImportException("BPMNPlane not associated with collaboration"));
			}
		} else {
			Process process = processes.get(0);
			if (!process.equals(bpmnElement)) {
				log(new ImportException("BPMNPlane not associated with process"));
			}
		}
	}
	
	protected BPMNDiagram getOrCreateDiagram(List<BPMNDiagram> diagrams) {

		if (diagrams.isEmpty()) {
			BPMNDiagram newDiagram = ModelHelper.create(bpmnResource, BPMNDiagram.class);
			diagrams.add(newDiagram);
		}

		BPMNDiagram bpmnDiagram = diagrams.get(0);
		BPMNPlane bpmnPlane = bpmnDiagram.getPlane();
		
		if (bpmnPlane == null || bpmnPlane.eIsProxy()) {
			bpmnPlane = ModelHelper.create(bpmnResource, BPMNPlane.class);
			bpmnDiagram.setPlane(bpmnPlane);
		}
		
		return bpmnDiagram;
	}

	protected Process createDefaultDiagramContent(Definitions definitions, BPMNDiagram bpmnDiagram) {

		// create process
		Process process = ModelHelper.create(bpmnResource, Process.class);
		definitions.getRootElements().add(process);
		
		// associate process with bpmn plane
		bpmnDiagram.getPlane().setBpmnElement(process);
		
		return process;
	}

	protected Diagram createEditorRootDiagram(BPMNDiagram bpmnDiagram, Collaboration collaboration, List<Process> processes, Definitions definitions) {
		
		// retrieve diagram from resource
		Diagram diagram = getDiagram(diagramResource);

		// remove old scroll util (if present)
		ScrollUtil.removeScrollShape(diagram);
		
		// clear it
		DIUtils.clearDiagram(diagram);
		
		// set active
		diagram.setActive(true);
		
		// get correct business object based on process or collaboration diagram
		BaseElement businessObject = collaboration != null ? collaboration : processes.get(0);
		
		// link it with correct business object
		getFeatureProvider().link(diagram, new Object[] { businessObject, bpmnDiagram, definitions });
		
		return diagram;
	}

	/**
	 * Parse diagram form a diagram resource.
	 * 
	 * @param diagramResource
	 * @return
	 */
	private Diagram getDiagram(Resource diagramResource) {

		// assume diagram is already part of diagram resource
		// this must be done, because we do not want to create any diagrams
		// on the fly, nor do we want to speculate on the diagram type
		
		EList<EObject> contents = diagramResource.getContents();
		
		Diagram diagram = null;
		
		for (EObject o : contents) {
			if (o instanceof Diagram) {
				Assert.isTrue(diagram == null, "Resource contains only one diagram");
				
				diagram = (Diagram) o;
			}
		}
		
		Assert.isNotNull(diagram, "Resource contains a diagram");
		
		return diagram;
	}

	protected void performLayout() {
		// do nothing
	}
	
	// handling of BPMN Model Elements ///////////////////////////////////////////////////////////////
	
	protected void handleCollaboration(Collaboration collaboration, ContainerShape container) {
		List<Participant> participants = collaboration.getParticipants();
		
		if (participants.isEmpty()) {
			InvalidContentException exception = new InvalidContentException("No participants in collaboration", collaboration);
			logAndThrow(exception);
		}
		
		for (Participant participant : participants) {
			handleParticipant(participant, container);
		}
		
		for (MessageFlow messageFlow: collaboration.getMessageFlows()) {
			handleMessageFlow(messageFlow, container);
		}
	}

	/**
	 * This draws a participant (Pool) in a Collaboration 
	 * 
	 * @param participant
	 * @param container
	 */
	protected void handleParticipant(Participant participant, ContainerShape container) {
		
		Process process = participant.getProcessRef();
		if (process != null && process.eIsProxy()) {
			throw new InvalidContentException("Invalid process referenced by participant", participant);
		}
		
		// TODO: process.isIsClosed == !collapsed ?
		// TODO: or rather bpmnShape.isIsExpanded()
		
		// if (process == null || !bpmnShape.isIsExpanded()) {
		// BPMNShape bpmnShape = (BPMNShape) getDiagramElement(participant);
		if (process == null) {
			// collapsed pool
			handleCollapsedParticipant(participant, container);
		} else {
			handleExpandedParticipant(participant, process, container);
		}
	}

	protected void handleExpandedParticipant(Participant participant, Process process, ContainerShape container) {
		
		// draw the participant (pool)
		ParticipantShapeHandler shapeHander = new ParticipantShapeHandler(this);
		ContainerShape participantContainer = (ContainerShape) handleDiagramElement(participant, container, shapeHander);
		
		List<LaneSet> laneSets = process.getLaneSets();
		if (laneSets.isEmpty()) {
			// if there are no lanes, simply draw the process into the pool (including sequence flows)
			handleProcess(process, participantContainer);
			
		} else {
			
			//  draw the lanes (possibly nested). The lanes reference the task elements they contain, but not the sequence flows.
			for (LaneSet laneSet: laneSets) {
				handleLaneSet(laneSet, process, participantContainer);
			}
			
			// draw flow elements not referenced from lanes
			List<FlowElement> flowElements = process.getFlowElements();
			
			handleUnreferencedFlowElements(participantContainer, flowElements);
			
			// handle io specification (data input and output)
			handleInputOutputSpecification(process, participantContainer);
			
			// draw the sequence flows:
			handleSequenceFlows(participantContainer, flowElements);
			
			// draw artifacts (e.g. groups)
			List<Artifact> artifacts = process.getArtifacts();		
			handleArtifacts(container, artifacts);
		}
	}

	protected void handleUnreferencedFlowElements(ContainerShape containerShape, List<FlowElement> flowElements) {
		List<FlowElement> unreferencedFlowElements = new ArrayList<FlowElement>();
		
		for (FlowElement e: flowElements) {
			// sequence flows handled after handling of unreferenced elements
			if (e instanceof SequenceFlow) {
				continue;
			} else {
				if (getPictogramElementOrNull(e) == null) {
					if (e instanceof FlowNode) {
						log(new UnmappedElementException("element not assigned to lane", e));
					}
					
					unreferencedFlowElements.add(e);
				}
			}
		}
	
		// render 
		handleFlowElements(containerShape, unreferencedFlowElements);
	}

	protected void handleCollapsedParticipant(Participant participant, ContainerShape container) {
		// draw the participant (pool)
		ParticipantShapeHandler shapeHander = new ParticipantShapeHandler(this);
		handleDiagramElement(participant, container, shapeHander);
	}

	protected void handleSequenceFlows(ContainerShape participantContainer, List<FlowElement> flowElements) {
		for (FlowElement flowElement : flowElements) {
			if (flowElement instanceof SequenceFlow) {
				handleSequenceFlow((SequenceFlow) flowElement, participantContainer);				
			}
		}
	}

	protected void handleDataOutputAssociations(List<DataOutputAssociation> dataOutputAssociations, ContainerShape container) {
		for (DataOutputAssociation outputAssociation: dataOutputAssociations) {
			handleDataOutputAssociation(outputAssociation, container);
		}
	}

	private void handleDataOutputAssociation(DataOutputAssociation flowElement, ContainerShape container) {
		handleLater(new DeferredAction<DataOutputAssociation>(flowElement, container, new DataOutputAssociationShapeHandler(this)));
	}

	protected void handleDataInputAssociations(List<DataInputAssociation> dataInputAssociations, ContainerShape container) {
		for (DataInputAssociation inputAssociation: dataInputAssociations) {
			handleDataInputAssociation(inputAssociation, container);
		}
	}
	
	private void handleDataInputAssociation(DataInputAssociation flowElement, ContainerShape container) {
		handleLater(new DeferredAction<DataInputAssociation>(flowElement, container, new DataInputAssociationShapeHandler(this)));
	}

	protected void handleLaneSet(LaneSet laneSet, FlowElementsContainer scope, ContainerShape container) {
		
		List<Lane> lanes = laneSet.getLanes();
		if (lanes.isEmpty()) {
			log(new InvalidContentException("LaneSet has no lanes specified", laneSet));
		}
		
		for (Lane lane: lanes) {
			handleLane(lane, scope, container);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void handleLane(Lane lane, FlowElementsContainer scope, ContainerShape container) {
		AbstractShapeHandler<Lane> shapeHandler = new LaneShapeHandler(this);
		
		// TODO: Draw lane the right way
		DiagramElement diagramElement = getDiagramElement(lane);
		if (diagramElement == null) {
			return;
		}
		
		ContainerShape thisContainer = (ContainerShape) shapeHandler.handleDiagramElement(lane, diagramElement, container);
		pictogramElements.put(lane, thisContainer);
		
		LaneSet childLaneSet = lane.getChildLaneSet();
		if (childLaneSet != null) {
			handleLaneSet(childLaneSet, scope, thisContainer);
		} else {
			List<FlowNode> referencedNodes = lane.getFlowNodeRefs();
			handleFlowElements(thisContainer, (List) referencedNodes);
		}
	}

	protected void handleProcess(Process process, ContainerShape container) {
		
		// handle direct children of the process element (not displaying lanes)
		List<FlowElement> flowElements = process.getFlowElements();
		handleFlowElements(container, flowElements);
		
		handleSequenceFlows(container, flowElements);

		// data store, data input, data output
		handleInputOutputSpecification(process, container);
		
		// e.g. groups, text annotation, ...
		List<Artifact> artifacts = process.getArtifacts();		
		handleArtifacts(container, artifacts);
	}
	
	protected void handleInputOutputSpecification(Process process, ContainerShape container) {
		InputOutputSpecification inputOutputSpecification = process.getIoSpecification();
		if (inputOutputSpecification != null) {
			handleInputOutputSpecification(inputOutputSpecification, container);
		}
	}

	protected void handleInputOutputSpecification(Activity activity, ContainerShape container) {
		InputOutputSpecification inputOutputSpecification = activity.getIoSpecification();
		if (inputOutputSpecification != null) {
			handleInputOutputSpecification(inputOutputSpecification, container);
		}
	}
	
	private void handleInputOutputSpecification(InputOutputSpecification inputOutputSpecification, ContainerShape container) {
		
		// handle data inputs
		handleDataInputs(container, inputOutputSpecification.getDataInputs());
		
		// and data outputs
		handleDataOutputs(container, inputOutputSpecification.getDataOutputs());
	}

	protected void handleDataOutputs(ContainerShape container, List<DataOutput> dataOutputs) {
		if (dataOutputs == null) {
			return;
		}
		
		for (DataOutput output: dataOutputs) {
			handleDataOutput(output, container);
		}
	}

	protected void handleDataOutput(DataOutput output, ContainerShape container) {
		handleDiagramElement(output, container, new DataOutputShapeHandler(this));
	}

	protected void handleDataInputs(ContainerShape container, List<DataInput> dataInputs) {
		if (dataInputs == null) {
			return;
		}
		
		for (DataInput input: dataInputs) {
			handleDataInput(input, container);
		}
	}

	private void handleDataInput(DataInput input, ContainerShape container) {
		handleDiagramElement(input, container, new DataInputShapeHandler(this));
	}

	protected void handleArtifacts(ContainerShape container, List<Artifact> artifacts) {

		for (Artifact artifact: artifacts) {
			if (artifact instanceof Association) {
				// association rendering is done deferred
				handleAssociation((Association) artifact, container);
			} else {
				handleArtifact(artifact, container);
			}
		}
	}
	
	protected void handleAssociation(Association association, ContainerShape container) {
		
		handleLater(new DeferredAction<Association>(association, container, new AssociationShapeHandler(this)));
	}

	/**
	 * processes all {@link FlowElement FlowElements} in a given container.
	 * 
	 * @param container
	 * @param flowElementsToBeDrawn
	 */
	protected void handleFlowElements(ContainerShape container, List<FlowElement> flowElementsToBeDrawn) {

		List<BoundaryEvent> boundaryEvents = new ArrayList<BoundaryEvent>();
		List<DataObject> dataObjects = new ArrayList<DataObject>();

		for (FlowElement flowElement : flowElementsToBeDrawn) {

			if (flowElement instanceof BoundaryEvent) {
				// defer handling of boundary events
				// until the elements they are attached to are
				// rendered
				boundaryEvents.add((BoundaryEvent) flowElement);

			} else if (flowElement instanceof Gateway) {
				handleGateway((Gateway) flowElement, container);

			} else if (flowElement instanceof SubProcess) {
				handleSubProcess((SubProcess) flowElement, container);

			} else if (flowElement instanceof CallActivity) {
				handleCallActivity((CallActivity) flowElement, container);

			} else if (flowElement instanceof Task) {
				handleTask((Task) flowElement, container);

			} else if (flowElement instanceof Event) {
				handleEvent((Event) flowElement, container);

			} else if (flowElement instanceof DataObjectReference) {
				handleDataObjectReference((DataObjectReference) flowElement, container);
			} else if (flowElement instanceof DataObject) {
				// handle dataobjects because they may need
				// conversion to dataObjecReferences
				dataObjects.add((DataObject) flowElement);

			} else if (flowElement instanceof DataStoreReference) {
				handleDataStoreReference((DataStoreReference) flowElement, container);

			} else {
				// yea, unhandled element				
			}
			
			if (flowElement instanceof Activity) {
				Activity activity = (Activity) flowElement;

				handleDataInputAssociations(activity.getDataInputAssociations(), container);
				handleDataOutputAssociations(activity.getDataOutputAssociations(), container);
			} else if (flowElement instanceof CatchEvent) {
				CatchEvent catchEvent = (CatchEvent) flowElement;
				handleDataOutputAssociations(catchEvent.getDataOutputAssociation(), container);
			} else if (flowElement instanceof ThrowEvent) {
				ThrowEvent throwEvent = (ThrowEvent) flowElement;
				handleDataInputAssociations(throwEvent.getDataInputAssociation(), container);				
			}
		}

		for (DataObject dataObject : dataObjects) {
			// legacy import for data object as flow element
			// (should be data object reference instead)
			// we did that wrong in the old camunda modeler days
			handleDataObject(dataObject, container);
		}

		// handle boundary events last
		for (BoundaryEvent boundaryEvent : boundaryEvents) {
			handleEvent(boundaryEvent, container);
		}
	}

	private void handleDataObjectReference(DataObjectReference flowElement, ContainerShape container) {
		
		handleDiagramElement(flowElement, container, new DataObjectReferenceShapeHandler(this));
	}

	@SuppressWarnings("unchecked")
	private void handleDataObject(DataObject dataObject, ContainerShape container) {

		// import only data objects for which actual DI data exists 
		// (all others must have been referenced by dataObjectReferences)
		
		if (getDiagramElementMap().get(dataObject.getId()) != null) {
			
			DataObjectReference dataObjectReference = Bpmn2Factory.eINSTANCE.createDataObjectReference();

			Transformer transformer = new Transformer(dataObject);

			dataObjectReference.setName(dataObject.getName());

			// assign id to newly created reference
			ModelUtil.setID(dataObjectReference, dataObject.eResource());
			
			// swap pointers for data object reference
			transformer.swapCrossReferences(dataObjectReference);
			
			// link to original data object
			dataObjectReference.setDataObjectRef(dataObject);
			
			// add reference as dataObject sibling to resource
			List<EObject> flowElements = (List<EObject>) dataObject.eContainer().eGet(Bpmn2Package.eINSTANCE.getFlowElementsContainer_FlowElements());
			flowElements.add(dataObjectReference);
			
			// update di
			DiagramElement bpmnShape = getDiagramElementMap().remove(dataObject.getId());
			getDiagramElementMap().put(dataObjectReference.getId(), bpmnShape);
			
			log(new AutomaticConversionWarning("DataObjectReference has been inserted for DataObject", dataObject, dataObjectReference));
			
			// render data object reference instead
			handleDataObjectReference(dataObjectReference, container);
		}
	}

	protected void handleSubProcess(SubProcess subProcess, ContainerShape container) {
		
		// draw subprocess shape
		ContainerShape subProcessContainer = (ContainerShape) handleDiagramElement(subProcess, container, new SubProcessShapeHandler(this));

		// input and output associations for sub process
		handleInputOutputSpecification(subProcess, subProcessContainer);
		
		// descend into scope
		List<FlowElement> flowElements = subProcess.getFlowElements();
		handleFlowElements(subProcessContainer, flowElements);
		
		handleSequenceFlows(subProcessContainer, flowElements);
		
		// TODO: handle artifacts?
	}
	
	protected void handleArtifact(Artifact artifact, ContainerShape container) {
		handleDiagramElement(artifact, container, new ArtifactShapeHandler(this));
	}

	protected void handleGateway(Gateway flowElement, ContainerShape container) {
		handleDiagramElement(flowElement, container, new GatewayShapeHandler(this));
	}

	protected void handleMessageFlow(MessageFlow flowElement, ContainerShape container) {
		handleDiagramElement(flowElement, container, new MessageFlowShapeHandler(this));
	}
	
	protected void handleSequenceFlow(SequenceFlow flowElement, ContainerShape container) {
		handleDiagramElement(flowElement, container, new SequenceFlowHandler(this));
	}

	protected void handleEvent(Event flowElement, ContainerShape container) {
		handleDiagramElement(flowElement, container, new EventShapeHandler(this));
	}
	
	protected void handleCallActivity(CallActivity flowElement, ContainerShape container) {
		handleDiagramElement(flowElement, container, new FlowNodeShapeHandler(this));		
	}

	protected void handleTask(Task flowElement, ContainerShape container) {
		handleDiagramElement(flowElement, container, new TaskShapeHandler(this));
	}
	
	protected void handleDataStoreReference(DataStoreReference flowElement, ContainerShape container) {
		handleDiagramElement(flowElement, container, new DatastoreReferenceShapeHandler(this));
	}
	
	public <T extends BaseElement> PictogramElement handleDiagramElement(T flowElement, ContainerShape container,
			AbstractDiagramElementHandler<T> flowNodeShapeHandler) {
		
		DiagramElement diagramElement = getDiagramElement(flowElement);
		
		if (diagramElement == null) {
			return null;
		}
		
		PictogramElement pictogramElement = flowNodeShapeHandler.handleDiagramElement(flowElement, diagramElement, container);
		
		if (pictogramElement != null) {
			pictogramElements.put(flowElement, pictogramElement);
		}
		
		return pictogramElement;
	}

	protected void handleLater(DeferredAction<?> deferredAction) {
		deferredActions.add(deferredAction);
	}
	
	// handling of DI Elements ///////////////////////////////////////////////////////////////

	protected void handleDIBpmnDiagram(BPMNDiagram bpmnDiagram) {
		
		BPMNPlane plane = bpmnDiagram.getPlane();
		if (plane == null) {
			throw new InvalidContentException("BPMNDiagram has no BPMNPlane", bpmnDiagram);
		} else {
			handleDIBpmnPlane(plane);
		}
	}

	protected void handleDIBpmnPlane(BPMNPlane plane) {
		
		BaseElement bpmnElement = plane.getBpmnElement();
		if (bpmnElement == null || bpmnElement.eIsProxy()) {
			// if we have a plane with missing bpmnElement, we can make the following assumption
			if (collaboration == null && processes.size() == 1) {
				bpmnElement = processes.get(0);
			} else {
				throw new UnmappedElementException("BPMNPlane references unexisting bpmnElement", plane);	
			}
		}
		
		List<DiagramElement> planeElement = plane.getPlaneElement();
		for (DiagramElement diagramElement : planeElement) {
			handleDIDiagramElement(diagramElement);
		}		
	}

	protected void handleDIDiagramElement(DiagramElement diagramElement) {
		if (diagramElement instanceof BPMNShape) {
			handleDIShape((BPMNShape) diagramElement);			
		} else if(diagramElement instanceof BPMNEdge) {
			handleDIEdge((BPMNEdge) diagramElement);
		} else {
			nonModelElements.add(diagramElement);
		}
	}

	protected void handleDIEdge(BPMNEdge diagramElement) {
		BaseElement bpmnElement = diagramElement.getBpmnElement();
		if (bpmnElement == null || bpmnElement.eIsProxy()) {
			ImportException exception = new UnmappedElementException("BPMNEdge references unexisting bpmnElement", diagramElement);
			log(exception);
		} else {
			linkInDiagramElementMap(diagramElement, bpmnElement);
		}
	}

	protected void handleDIShape(BPMNShape diagramElement) {
		BaseElement bpmnElement = diagramElement.getBpmnElement();
		if (bpmnElement == null || bpmnElement.eIsProxy()) {
			ImportException exception = new UnmappedElementException("BPMNShape references unexisting bpmnElement", diagramElement);
			log(exception);
		} else {
			linkInDiagramElementMap(diagramElement, bpmnElement);
			Bounds bounds = diagramElement.getBounds();
			if (bounds != null) {
				importBounds.setRectangle(
					(int) Math.min(bounds.getX(), importBounds.getX()), 
					(int) Math.min(bounds.getY(), importBounds.getY()),
					(int) Math.max(bounds.getX() + bounds.getWidth(), importBounds.getWidth()),
					(int) Math.max(bounds.getY() + bounds.getHeight(), importBounds.getHeight())
				);
			}
		}
	}
	
	protected void linkInDiagramElementMap(DiagramElement diagramElement, BaseElement bpmnElement) {
		// if it does not have a id, it can't be shown
		if (bpmnElement.getId() == null) {
			return;
		}
		
		diagramElementMap.put(bpmnElement.getId(), diagramElement);
	}
	
	// Error logging ////////////////////////////////////////////
	
	public void log(ImportException e) {
		warnings.add(e);
		ErrorLogger.log(e);
	}
	
	/**
	 * Log without outputting to eclipse console
	 * @param e
	 */
	public void logSilently(ImportException e) {
		warnings.add(e);
	}
	
	public void logAndThrow(ImportException e) throws ImportException {
		ErrorLogger.logAndThrow(e);
	}
	
	public void logResourceErrors(Bpmn2Resource resource) {
		List<Diagnostic> resourceErrors = resource.getErrors();
		
		// scan for xml load error
		for (Diagnostic diagnostic : resourceErrors) {
			if (diagnostic instanceof XMIException) {
				XMIException ex = (XMIException) diagnostic;
				// see if we deal with a xml load error
				if (ex.getCause() instanceof SAXException) {
					ImportException e = new ResourceImportException("Failed to load model xml", diagnostic);
					logAndThrow(e);
				}
			}
		}
		
		// log all other warnings
		for (Diagnostic diagnostic: resourceErrors) {
			logSilently(new ResourceImportException("Import warning", diagnostic));
		}
	}
	
	// Getters //////////////////////////////////////////////////
	
	public IFeatureProvider getFeatureProvider() {
		return getDiagramTypeProvider().getFeatureProvider();
	}
	
	public IDiagramTypeProvider getDiagramTypeProvider() {
		return diagramContainer.getDiagramTypeProvider();
	}

	public Bpmn2Resource getBpmnResource() {
		return bpmnResource;
	}
	
	public Map<String, DiagramElement> getDiagramElementMap() {
		return diagramElementMap;
	}

	public List<DiagramElement> getNonModelElements() {
		return nonModelElements;
	}

	public List<ImportException> getImportWarnings() {
		return warnings;
	}

	/**
	 * 
	 * @param bpmnElement
	 * @return null if no diagram element was found, a warning will be added in this case
	 */
	public DiagramElement getDiagramElement(BaseElement bpmnElement) {
		DiagramElement element = diagramElementMap.get(bpmnElement.getId());
		if (element == null) {
			UnmappedElementException exception = new UnmappedElementException("Diagram element not found, element will not be shown", bpmnElement);
			log(exception);
			return null;
		}
		return element;
	}
	
	public PictogramElement getPictogramElementOrNull(BaseElement node) {
		return pictogramElements.get(node);
	}
	
	public PictogramElement getPictogramElement(BaseElement node) {
		PictogramElement element = getPictogramElementOrNull(node);
		if (element == null) {
			UnmappedElementException exception = new UnmappedElementException("Container or Pictogram element not yet processed, containment might be invalid", node);
			log(exception);
			return null;
		}
		
		return element;
	}

	// Deferred diagram element handling /////////////////////////////////////////////////////
	
	public class DeferredAction<T extends BaseElement>  {

		private T flowElement;
		private ContainerShape container;
		private AbstractDiagramElementHandler<T> handler;

		public DeferredAction(T flowElement, ContainerShape container, AbstractDiagramElementHandler<T> handler) {
			
			this.flowElement = flowElement;
			this.container = container;
			this.handler = handler;
		}
		
		/**
		 * Handle the deferred diagram action
		 */
		public void handle() {
			handleDiagramElement(flowElement, container, handler);
		}
	}
}
