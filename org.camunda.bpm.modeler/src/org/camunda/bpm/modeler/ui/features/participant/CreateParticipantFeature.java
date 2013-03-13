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
 * @author Innar Made
 ******************************************************************************/
package org.camunda.bpm.modeler.ui.features.participant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.camunda.bpm.modeler.core.features.AbstractBpmn2CreateFeature;
import org.camunda.bpm.modeler.core.features.DefaultMoveBPMNShapeFeature;
import org.camunda.bpm.modeler.core.features.activity.MoveActivityFeature;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.core.utils.ScrollUtil.ScrollShapeHolder;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.camunda.bpm.modeler.ui.features.event.MoveBoundaryEventFeature;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class CreateParticipantFeature extends AbstractBpmn2CreateFeature<Participant> {

	private static final int minWidth = 500;
	private static final int minHeight = 175;
	
	private static final int xpadding = 70;
	private static final int ypadding = 40;
	
	private static final int topMargin = 40;
	private static final int leftMargin = 40;
	
	public CreateParticipantFeature(IFeatureProvider fp) {
	    super(fp, "Pool", "Container for partitioning a set of activities");
    }

	@Override
    public boolean canCreate(ICreateContext context) {
		return context.getTargetContainer() instanceof Diagram;
    }

	@Override
    public Object[] create(ICreateContext context) {
		
		Diagram diagram = getDiagram();
		BaseElement rootElement = BusinessObjectUtil.getFirstBaseElement(diagram);
		
		BPMNDiagram bpmnDiagram = BusinessObjectUtil.getFirstElementOfType(context.getTargetContainer(), BPMNDiagram.class);
		
		if (bpmnDiagram == null) {
			throw new IllegalStateException("Participant should be in the context of a Graphiti Diagram linked to a BPMNDiagram");
		}
		
		Definitions definitions = (Definitions) bpmnDiagram.eContainer();
		
		Participant newParticipant;

		if (rootElement == null) {
			throw new IllegalStateException("Diagram not liked to Process or Collaboration");
		} else
		
		// existing collaboration?
		if (rootElement instanceof Collaboration) {
			
			newParticipant = createBusinessObject(context);
			addGraphicalRepresentation(context, newParticipant);
		} else

		// no collaboration
		// need to add collaboration around process
		if (rootElement instanceof Process) {
			
			Process process = (Process) rootElement;
			
			Collaboration newCollaboration = createCollaboration(definitions, bpmnDiagram.getPlane());
			newParticipant = createParticipant(process, newCollaboration);
			
			ScrollShapeHolder scrollShapeHolder = BusinessObjectUtil.getFirstElementOfType(diagram, ScrollShapeHolder.class);
			
			// link diagram to collaboration and plane
			link(diagram, new Object[] { newCollaboration, bpmnDiagram });

			// relink scroll shape
			diagram.getLink().getBusinessObjects().add(scrollShapeHolder);
			
			// create graphiti representation
			createGraphitiRepresentation(context, newParticipant);
		} else {
			throw new IllegalStateException("Diagram liked to unrecognized element: " + rootElement);
		}

		newParticipant.setName("Pool ");
		return new Object[] { newParticipant };
    }


	private void createGraphitiRepresentation(ICreateContext context, Participant newParticipant) {
		Diagram diagram = getDiagram();
		
		IRectangle bounds = LayoutUtil.getBounds(diagram, minWidth, minHeight, xpadding, ypadding);
		ContainerShape participantContainer = (ContainerShape) addGraphicalRepresentation(contextFromBounds(context, bounds), newParticipant);
		
		Iterator<Shape> childrenIterator = diagram.getChildren().iterator();
		while (childrenIterator.hasNext()) {
			Shape c = childrenIterator.next();
			
			if (c == participantContainer) {
				continue;
			}
			
			if (LabelUtil.isLabel(c)) {
				continue;
			}
			
			// we need to move the child shape 
			// to the newly created participant
			childrenIterator.remove();
			
			participantContainer.getChildren().add(c);
		}

		offsetChildrenPosition(bounds, participantContainer);
		
		// make sure participant container does not 
		// hide labels
		Graphiti.getPeService().sendToBack(participantContainer);
	}

	private void offsetChildrenPosition(IRectangle bounds, ContainerShape participantContainer) {
		
		List<Shape> childShapes = new ArrayList<Shape>(participantContainer.getChildren());
		
		for (final Shape childShape : childShapes) {
			GraphicsAlgorithm ga = childShape.getGraphicsAlgorithm();
			
			if (!(childShape instanceof ContainerShape)) {
				continue;
			}
			
			MoveShapeContext moveShapeContext = new MoveShapeContext(childShape);
			
			int newX = ga.getX() - bounds.getX() + leftMargin;
			int newY = ga.getY() - bounds.getY() + topMargin;
			
			moveShapeContext.setX(newX);
			moveShapeContext.setY(newY);
			moveShapeContext.setTargetContainer(childShape.getContainer());
			moveShapeContext.setSourceContainer(childShape.getContainer());

			ContextUtil.set(moveShapeContext, DefaultMoveBPMNShapeFeature.SKIP_MOVE_LABEL);
			ContextUtil.set(moveShapeContext, DefaultMoveBPMNShapeFeature.SKIP_REPAIR_CONNECTIONS_AFTER_MOVE);
			ContextUtil.set(moveShapeContext, DefaultMoveBPMNShapeFeature.SKIP_MOVE_BENDPOINTS);
			
			// make sure boundary events behave as if moved with the activity and
			// activities do not move their boundary events (because they move themselves)
			ContextUtil.set(moveShapeContext, MoveActivityFeature.SKIP_MOVE_BOUNDARY_EVENTS);
			ContextUtil.set(moveShapeContext, MoveBoundaryEventFeature.MOVE_WITH_ACTIVITY);
			
			IMoveShapeFeature moveFeature = getFeatureProvider().getMoveShapeFeature(moveShapeContext);
			
			if (moveFeature.canMoveShape(moveShapeContext)) {
				moveFeature.execute(moveShapeContext);
			}
		}
		
		layoutPictogramElement(participantContainer);
	}

	private CreateContext contextFromBounds(ICreateContext oldContext, IRectangle bounds) {
		
		CreateContext newContext = new CreateContext();
		newContext.setTargetConnection(oldContext.getTargetConnection());
		newContext.setTargetContainer(oldContext.getTargetContainer());
		newContext.setX(bounds.getX() - leftMargin);
		newContext.setY(bounds.getY() - topMargin);
		newContext.setWidth(bounds.getWidth());
		newContext.setHeight(bounds.getHeight());
		return newContext;
	}
	
	private Participant createParticipant(Process process, Collaboration collaboration) {
		Participant newParticipant;
		newParticipant = Bpmn2Factory.eINSTANCE.createParticipant();
		ModelUtil.setID(newParticipant);

		newParticipant.setProcessRef(process);
		collaboration.getParticipants().add(newParticipant);
		return newParticipant;
	}

	private Collaboration createCollaboration(Definitions definitions, BPMNPlane bpmnPlane) {
		Collaboration newCollaboration = Bpmn2Factory.eINSTANCE.createCollaboration();
		ModelUtil.setID(newCollaboration);
		
		definitions.getRootElements().add(0, newCollaboration);
		bpmnPlane.setBpmnElement(newCollaboration);
		
		return newCollaboration;
	}
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_PARTICIPANT;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); // FIXME
	}

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateFeature#getBusinessObjectClass()
	 */
	@Override
	public EClass getBusinessObjectClass() {
		return Bpmn2Package.eINSTANCE.getParticipant();
	}
}
