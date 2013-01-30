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
package org.eclipse.bpmn2.modeler.ui.features.participant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.rectangle;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateFeature;
import org.eclipse.bpmn2.modeler.core.features.DefaultMoveBPMNShapeFeature;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ContextUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.features.context.impl.LayoutContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.algorithms.AlgorithmsFactory;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
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
			
			// link diagram to collaboration and plane
			link(diagram, new Object[] { newCollaboration, bpmnDiagram });
			
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
		
		IRectangle bounds = getChildBounds(diagram);
		ContainerShape participantContainer = (ContainerShape) addGraphicalRepresentation(contextFromBounds(context, bounds), newParticipant);
		
		IRectangle participantContainerBounds = LayoutUtil.getAbsoluteBounds(participantContainer);
		
		Iterator<Shape> childrenIterator = diagram.getChildren().iterator();
		while (childrenIterator.hasNext()) {
			Shape c = childrenIterator.next();
			
			if (c == participantContainer) {
				continue;
			}
			
			if (GraphicsUtil.isLabel(c)) {
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
			GraphicsAlgorithm childShapeGa = childShape.getGraphicsAlgorithm();
			
			if ( (childShapeGa instanceof Polyline) || (childShapeGa instanceof Text) ) {
				continue;
			}
			
			MoveShapeContext moveShapeContext = new MoveShapeContext(childShape);
			
			int newX = childShapeGa.getX() - bounds.getX() + leftMargin;
			int newY = childShapeGa.getY() - bounds.getY() + topMargin;
			
			moveShapeContext.setX(newX);
			moveShapeContext.setY(newY);
			moveShapeContext.setTargetContainer(childShape.getContainer());
			moveShapeContext.setSourceContainer(childShape.getContainer());
			
			moveShapeContext.putProperty(DefaultMoveBPMNShapeFeature.SKIP_MOVE_LABEL, true);
			ContextUtil.set(moveShapeContext, DefaultMoveBPMNShapeFeature.SKIP_MOVE_BENDPOINTS);
			
			IMoveShapeFeature moveFeature = getFeatureProvider().getMoveShapeFeature(moveShapeContext);
			
			if (moveFeature.canMoveShape(moveShapeContext)) {
				moveFeature.execute(moveShapeContext);
			}
		}
		
		LayoutContext layoutContext = new LayoutContext(participantContainer);
		getFeatureProvider().getLayoutFeature(layoutContext).execute(layoutContext);
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
	
	private class BoundsCompareResult {
		Integer XMinimum = 0; 
		Integer XMaximum = 0;
		Integer YMinimum = 0;
		Integer YMaximum = 0;
		
		public BoundsCompareResult() {
		}
		
		public Integer getXMinimum() {
			return XMinimum;
		}
		public void setXMinimum(Integer xMinimum) {
			XMinimum = xMinimum;
		}
		public Integer getXMaximum() {
			return XMaximum;
		}
		public void setXMaximum(Integer xMaximum) {
			XMaximum = xMaximum;
		}
		public Integer getYMinimum() {
			return YMinimum;
		}
		public void setYMinimum(Integer yMinimum) {
			YMinimum = yMinimum;
		}
		public Integer getYMaximum() {
			return YMaximum;
		}
		public void setYMaximum(Integer yMaximum) {
			YMaximum = yMaximum;
		}
		
	}
	
	private IRectangle getChildBounds(Diagram diagram) {
		IRectangle resultRectangle = rectangle(50, 50, minWidth, minHeight);
		
		if (diagram.getChildren().isEmpty()) {
			return resultRectangle;
		}

		GraphicsAlgorithm firstChildGa = diagram.getChildren().get(0).getGraphicsAlgorithm();
		BoundsCompareResult compareResult = new BoundsCompareResult();
		
		if (firstChildGa != null) {
			compareResult.setXMinimum(firstChildGa.getX());
			compareResult.setXMaximum(firstChildGa.getX() + firstChildGa.getWidth());
			compareResult.setYMinimum(firstChildGa.getY());
			compareResult.setYMaximum(firstChildGa.getY() + firstChildGa.getHeight());
		}
		
		for (Shape childShape : diagram.getChildren()) {
			compareAndUpdateMax(childShape, compareResult);
		}
		
		Integer newWidth = compareResult.getXMaximum() - compareResult.getXMinimum() + xpadding;
		Integer newHeight = compareResult.getYMaximum() - compareResult.getYMinimum() + ypadding;
		
		resultRectangle.setX(compareResult.getXMinimum());
		resultRectangle.setY(compareResult.getYMinimum());
		resultRectangle.setWidth(newWidth > minWidth ? newWidth : minWidth);
		resultRectangle.setHeight(newHeight > minHeight ? newHeight : minHeight);
		
		return resultRectangle;
	}
	
	private void compareAndUpdateMax(Shape childShape, BoundsCompareResult result) {
		GraphicsAlgorithm childShapeGa = childShape.getGraphicsAlgorithm();
		if (childShapeGa != null) {
			
			if (result.getXMinimum() > childShapeGa.getX()) {
				result.setXMinimum(childShapeGa.getX());
			}
			
			int xPlusWidth = childShapeGa.getX() + childShapeGa.getWidth();
			
			if (result.getXMaximum() < xPlusWidth) {
				result.setXMaximum(xPlusWidth);
			}
			
			if (result.getYMinimum() > childShapeGa.getY()) {
				result.setYMinimum (childShapeGa.getY());
			}
			
			int yPlusHeight = childShapeGa.getY() + childShapeGa.getHeight();
			
			if (result.getYMaximum() < yPlusHeight) {
				result.setYMaximum(yPlusHeight);
			}
		}
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
