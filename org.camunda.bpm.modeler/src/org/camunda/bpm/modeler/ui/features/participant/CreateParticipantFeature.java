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

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.camunda.bpm.modeler.core.features.AbstractBpmn2CreateFeature;
import org.camunda.bpm.modeler.core.features.DefaultBpmn2MoveShapeFeature;
import org.camunda.bpm.modeler.core.features.activity.MoveActivityFeature;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.core.utils.ScrollUtil;
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
	
	private static final int paddingX = 50;
	private static final int paddingY = 50;
	
	private static final int marginY = 40;
	private static final int marginX = 40;
	
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
			throw new IllegalStateException("Target container not linked to BPMNDiagram");
		}
		
		Definitions definitions = (Definitions) bpmnDiagram.eContainer();
		
		Participant newParticipant;

		if (rootElement == null) {
			throw new IllegalStateException("Diagram not liked to Process or Collaboration");
		} else
		
		// existing collaboration?
		if (rootElement instanceof Collaboration) {
			newParticipant = createBusinessObject(context);
			((Collaboration) rootElement).getParticipants().add(newParticipant);
			addGraphicalRepresentation(context, newParticipant);
		} else

		// no collaboration
		// need to add collaboration around process
		if (rootElement instanceof Process) {
			
			Process process = (Process) rootElement;
			
			Collaboration newCollaboration = createCollaboration(definitions, bpmnDiagram.getPlane());
			newParticipant = createParticipant(process, newCollaboration);
			
			// link diagram to collaboration and plane
			link(diagram, new Object[] { newCollaboration, bpmnDiagram });
			
			// create graphiti representation
			createGraphitiRepresentation(context, newParticipant);
		} else {
			throw new IllegalStateException("Diagram liked to unrecognized element: " + rootElement);
		}
		
		ScrollUtil.addScrollShape(getDiagram());
		
		newParticipant.setName("Pool");
		return new Object[] { newParticipant };
	}


	private void createGraphitiRepresentation(ICreateContext context, Participant newParticipant) {
		Diagram diagram = getDiagram();
		
		IRectangle bounds = LayoutUtil.getChildrenBBox(diagram, null, paddingX, paddingY);
		
		// in case we have no children
		if (bounds == null) {
			bounds = rectangle(context.getX(), context.getY(), minWidth, minHeight);
		}
		
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
		Shape scrollShape = ScrollUtil.getScrollShape(getDiagram());
		
		for (final Shape childShape : childShapes) {
			// dont move scrollshape into participant
			if (childShape.equals(scrollShape)) {
				continue;
			}
			
			GraphicsAlgorithm ga = childShape.getGraphicsAlgorithm();
			
			if (!(childShape instanceof ContainerShape)) {
				continue;
			}
			
			MoveShapeContext moveShapeContext = new MoveShapeContext(childShape);
			
			int newX = ga.getX() - bounds.getX() + marginX;
			int newY = ga.getY() - bounds.getY() + marginY;
			
			moveShapeContext.setX(newX);
			moveShapeContext.setY(newY);
			moveShapeContext.setTargetContainer(childShape.getContainer());
			moveShapeContext.setSourceContainer(childShape.getContainer());

			ContextUtil.set(moveShapeContext, DefaultBpmn2MoveShapeFeature.SKIP_MOVE_LABEL);
			ContextUtil.set(moveShapeContext, DefaultBpmn2MoveShapeFeature.SKIP_REPAIR_CONNECTIONS_AFTER_MOVE);
			ContextUtil.set(moveShapeContext, DefaultBpmn2MoveShapeFeature.SKIP_MOVE_BENDPOINTS);
			
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
		
		newContext.setX(bounds.getX() - marginX);
		newContext.setY(bounds.getY() - marginY);
		newContext.setWidth(bounds.getWidth() + marginX);
		newContext.setHeight(bounds.getHeight() + marginY);
		
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
	 * @see org.camunda.bpm.modeler.features.AbstractBpmn2CreateFeature#getBusinessObjectClass()
	 */
	@Override
	public EClass getBusinessObjectClass() {
		return Bpmn2Package.eINSTANCE.getParticipant();
	}
}
