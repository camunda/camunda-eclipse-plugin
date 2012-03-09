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
package org.eclipse.bpmn2.modeler.core.features;

import java.io.IOException;
import java.util.List;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.bpmn2.di.BpmnDiPackage;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.di.DIImport;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.dc.DcFactory;
import org.eclipse.dd.dc.Point;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ITargetContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public abstract class AbstractAddBPMNShapeFeature extends AbstractAddShapeFeature {

	public AbstractAddBPMNShapeFeature(IFeatureProvider fp) {
		super(fp);
	}

	protected BPMNShape findDIShape(BaseElement elem) {
		try {
			return (BPMNShape) ModelHandlerLocator.getModelHandler(getDiagram().eResource()).findDIElement(elem);
		} catch (IOException e) {
			Activator.logError(e);
		}
		return null;
	}
	
	protected BPMNShape createDIShape(Shape gShape, BaseElement elem) {
		return createDIShape(gShape, elem, findDIShape(elem));
	}

	protected BPMNShape createDIShape(Shape gShape, BaseElement elem, BPMNShape shape) {
		ILocation loc = Graphiti.getLayoutService().getLocationRelativeToDiagram(gShape);
		if (shape == null) {
			EList<EObject> businessObjects = Graphiti.getLinkService().getLinkForPictogramElement(getDiagram())
					.getBusinessObjects();
			for (EObject eObject : businessObjects) {
				if (eObject instanceof BPMNDiagram) {
					BPMNDiagram bpmnDiagram = (BPMNDiagram) eObject;

					shape = BpmnDiFactory.eINSTANCE.createBPMNShape();
//					shape.setId(EcoreUtil.generateUUID());
					shape.setBpmnElement(elem);
					Bounds bounds = DcFactory.eINSTANCE.createBounds();
					if (elem instanceof Activity) {
						bounds.setHeight(gShape.getGraphicsAlgorithm().getHeight());
					} else {
						bounds.setHeight(gShape.getGraphicsAlgorithm().getHeight());
					}
					bounds.setWidth(gShape.getGraphicsAlgorithm().getWidth());
					bounds.setX(loc.getX());
					bounds.setY(loc.getY());
					shape.setBounds(bounds);
					
					// TODO: get default orientation from Preferences
					shape.setIsHorizontal(true);

					addShape(shape, bpmnDiagram);
					ModelUtil.setID(shape);
				}
			}
		}
		link(gShape, new Object[] { elem, shape });
		return shape;
	}
	
	private void addShape(DiagramElement elem, BPMNDiagram bpmnDiagram) {
		List<DiagramElement> elements = bpmnDiagram.getPlane().getPlaneElement();
		elements.add(elem);
	}

	protected BPMNEdge createDIEdge(Connection connection, BaseElement conElement) {
		try {
			BPMNEdge edge = (BPMNEdge) ModelHandlerLocator.getModelHandler(getDiagram().eResource()).findDIElement(conElement);
			return createDIEdge(connection, conElement, edge);
		} catch (IOException e) {
			Activator.logError(e);
		}
		return null;
	}

	protected BPMNEdge createDIEdge(Connection connection, BaseElement conElement, BPMNEdge edge) throws IOException {
		ModelHandler modelHandler = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
		if (edge == null) {
			EList<EObject> businessObjects = Graphiti.getLinkService().getLinkForPictogramElement(getDiagram())
					.getBusinessObjects();
			for (EObject eObject : businessObjects) {
				if (eObject instanceof BPMNDiagram) {
					BPMNDiagram bpmnDiagram = (BPMNDiagram) eObject;

					edge = BpmnDiFactory.eINSTANCE.createBPMNEdge();
//					edge.setId(EcoreUtil.generateUUID());
					edge.setBpmnElement(conElement);

					if (conElement instanceof Association) {
						edge.setSourceElement(modelHandler.findDIElement(
								((Association) conElement).getSourceRef()));
						edge.setTargetElement(modelHandler.findDIElement(
								((Association) conElement).getTargetRef()));
					} else if (conElement instanceof MessageFlow) {
						edge.setSourceElement(modelHandler.findDIElement(
								(BaseElement) ((MessageFlow) conElement).getSourceRef()));
						edge.setTargetElement(modelHandler.findDIElement(
								(BaseElement) ((MessageFlow) conElement).getTargetRef()));
					} else if (conElement instanceof SequenceFlow) {
						edge.setSourceElement(modelHandler.findDIElement(
								((SequenceFlow) conElement).getSourceRef()));
						edge.setTargetElement(modelHandler.findDIElement(
								((SequenceFlow) conElement).getTargetRef()));
					}

					ILocation sourceLoc = Graphiti.getPeService().getLocationRelativeToDiagram(connection.getStart());
					ILocation targetLoc = Graphiti.getPeService().getLocationRelativeToDiagram(connection.getEnd());

					Point point = DcFactory.eINSTANCE.createPoint();
					point.setX(sourceLoc.getX());
					point.setY(sourceLoc.getY());
					edge.getWaypoint().add(point);

					point = DcFactory.eINSTANCE.createPoint();
					point.setX(targetLoc.getX());
					point.setY(targetLoc.getY());
					edge.getWaypoint().add(point);

					addShape(edge, bpmnDiagram);
					ModelUtil.setID(edge);
				}
			}
		}
		link(connection, new Object[] { conElement, edge });
		return edge;
	}
	
	protected void prepareAddContext(IAddContext context, int width, int height) {
		context.putProperty(ContextConstants.LABEL_CONTEXT, true);
		context.putProperty(ContextConstants.WIDTH, width);
		context.putProperty(ContextConstants.HEIGHT, height);
		context.putProperty(ContextConstants.BASE_ELEMENT, context.getNewObject());
	}
	
	protected int getHeight(IAddContext context) {
		return context.getHeight() > 0 ? context.getHeight() :
			(isHorizontal(context) ? getHeight() : getWidth());
	}
	
	protected int getWidth(IAddContext context) {
		return context.getWidth() > 0 ? context.getWidth() :
			(isHorizontal(context) ? getWidth() : getHeight());
	}

	protected boolean isHorizontal(ITargetContext context) {
		if (context.getProperty(DIImport.IMPORT_PROPERTY) == null) {
			// not importing - set isHorizontal to be the same as parent Pool
			if (FeatureSupport.isTargetParticipant(context)) {
				Participant targetParticipant = FeatureSupport.getTargetParticipant(context);
				BPMNShape participantShape = findDIShape(targetParticipant);
				if (participantShape!=null)
					return participantShape.isIsHorizontal();
			}
			else if (FeatureSupport.isTargetLane(context)) {
				Lane targetLane = FeatureSupport.getTargetLane(context);
				BPMNShape laneShape = findDIShape(targetLane);
				if (laneShape!=null)
					return laneShape.isIsHorizontal();
			}
		}
		// TODO: set default orientation from Preferences
		return true;
	}
	
	protected abstract int getHeight();
	protected abstract int getWidth();
}