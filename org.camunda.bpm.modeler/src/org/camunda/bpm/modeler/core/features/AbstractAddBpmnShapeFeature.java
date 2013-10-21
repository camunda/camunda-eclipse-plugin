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
package org.camunda.bpm.modeler.core.features;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.BoxingStrategy;
import org.camunda.bpm.modeler.core.preferences.Bpmn2Preferences;
import org.camunda.bpm.modeler.core.utils.AnchorUtil;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ITargetContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public abstract class AbstractAddBpmnShapeFeature<T extends BaseElement> extends AbstractAddBpmnElementFeature<T, ContainerShape> {

	public static final int BOX_PADDING = 10;
	
	public AbstractAddBpmnShapeFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	/**
	 * Creates the new shape for the given context with the specified bounds.
	 * 
	 * @param context
	 * @param bounds
	 * @return
	 */
	protected abstract ContainerShape createPictogramElement(IAddContext context, IRectangle bounds);

	/**
	 * Creates anchors for the newly created shape.
	 * 
	 * @param context
	 * @param newShape
	 */
	protected void createAnchors(IAddContext context, ContainerShape newShape) {

		// per default, create chopbox anchor and
		// four fix point anchors on all four sides of the shape (North-East-South-West)
		AnchorUtil.addChopboxAnchor(newShape);
		AnchorUtil.addFixedPointAnchors(newShape);
	}

	/**
	 * Creates the shape including its anchors with the given bounds.
	 * 
	 * @param context
	 * @param newShapeBounds
	 * 
	 * @return
	 */
	protected ContainerShape createShape(IAddContext context, IRectangle newShapeBounds) {
		ContainerShape newShape = createPictogramElement(context, newShapeBounds);
		
		postCreateHook(context, newShapeBounds, newShape);
		
		createAnchors(context, newShape);
		
		setProperties(context, newShape);
		
		return newShape;
	}
	
	/**
	 * Return bounds for the element to be added.
	 * 
	 * @param context
	 * 
	 * @return
	 */
	protected IRectangle getAddBounds(IAddContext context) {
		
		adjustLocationAndSize(context, getWidth(context), getHeight(context));
		
		return rectangle(
			context.getX(), 
			context.getY(), 
			context.getWidth(), 
			context.getHeight());
	}

	/**
	 * Get or create the {@link BPMNShape} for the given shape and baseElement in a given context. 
	 * 
	 * @param shape
	 * @param baseElement
	 * @param context
	 * 
	 * @return the bpmn shape
	 */
	protected BPMNShape createDi(Shape shape, BaseElement baseElement, IAddContext context) {
		boolean isImport = isImport(context);
		
		return createDIShape(shape, baseElement, findDIShape(baseElement), !isImport);
	}

	/**
	 * Creates the di shape for the given element
	 * 
	 * @param shape
	 * @param elem
	 * @param bpmnShape
	 * @param applyDefaults
	 * @return
	 */
	protected BPMNShape createDIShape(Shape shape, BaseElement elem, BPMNShape bpmnShape, boolean applyDefaults) {
		if (bpmnShape == null) {
			IRectangle bounds = LayoutUtil.getAbsoluteBounds(shape);
			bpmnShape = DIUtils.createDIShape(elem, bounds, getDiagram());
		}
		
		link(shape, new Object[] { elem, bpmnShape });
		
		if (applyDefaults) {
			Bpmn2Preferences.getInstance(bpmnShape.eResource()).applyBPMNDIDefaults(bpmnShape, null);
		}
		
		return bpmnShape;
	}

	protected BPMNShape findDIShape(BaseElement element) {
		return (BPMNShape) ModelHandler.findDIElement(getDiagram(), element);
	}

	/**
	 * Perform a post add operation once the shape with the given bounds
	 * has beed added.
	 * 
	 * @param context
	 * @param newShape
	 * @param elementBounds
	 * 
	 * @set {@link #postAddHook(IAddContext, PictogramElement)}
	 */
	protected void postAddHook(IAddContext context, ContainerShape newShape, IRectangle elementBounds) {
		postAddHook(context, newShape);
	}
	
	/**
	 * Return a label add context for the given context and shape bounds.
	 * 
	 * @param context
	 * @param newShapeBounds
	 * @return
	 */
	protected IAddContext getAddLabelContext(IAddContext context, ContainerShape newShape, IRectangle newShapeBounds) {

		GraphicsUtil.prepareLabelAddContext(context, 
			newShape, 
			newShapeBounds, 
			getBusinessObject(context));
		
		return context;
	}
	
	@Override
	public PictogramElement add(IAddContext context) {
		T activity = getBusinessObject(context);

		// compute actual add bounds
		IRectangle elementBounds = getAddBounds(context);
		
		// create graphical element
		ContainerShape newShape = createShape(context, elementBounds);

		// create di
		createDi(newShape, activity, context);
		
		// allow a post add operation
		postAddHook(context, newShape, elementBounds);

		// send element to front
		sendToFront(newShape);
		
		// create label
		createLabel(context, newShape, elementBounds);
		
		// perform update and layouting
		updateAndLayout(newShape, context);
		
		return newShape;
	}

	protected void sendToFront(ContainerShape newShape) {
		GraphicsUtil.sendToFront(newShape);
	}

	/**
	 * Perform an initial update and layouting of the new shape as desired.
	 * 
	 * @param newShape
	 * @param context
	 */
	protected void updateAndLayout(ContainerShape newShape, IAddContext context) {
		// update
		updatePictogramElement(newShape);
		
		// layout
		if (!isImport(context) || isLayoutAfterImport()) {
			layoutPictogramElement(newShape);
		}
	}

	/**
	 * Return true if layout after import is desired for this feature.
	 * 
	 * @return
	 */
	protected boolean isLayoutAfterImport() {
		return true;
	}

	/**
	 * Creates a label for the newly created shape if any.
	 * 
	 * May be overridden by subclasses to perform actual actions.
	 * 
	 * @param context
	 * @param newShape
	 */
	protected void createLabel(IAddContext context, ContainerShape newShape, IRectangle newShapeBounds) {
		
		// create label if the add shape feature wishes to do so
		if (isCreateExternalLabel()) {
			IAddContext addLabelContext = getAddLabelContext(context, newShape, newShapeBounds);
			if (addLabelContext != null) {
				getFeatureProvider().getAddFeature(addLabelContext).add(addLabelContext);
			}
		}
	}

	/**
	 * Adjust the add context location and size 
	 * 
	 * @param context
	 * @param width
	 * @param height
	 */
	protected void adjustLocationAndSize(IAddContext context, int width, int height) {
		
		if (isImport(context)) {
			return;
		}
		
		adjustLocation(context, width, height);
		adjustSize(context, width, height);
		
		BoxingStrategy boxingStrategy = getBoxingStrategy(context);
		if (boxingStrategy != BoxingStrategy.NONE) {
			boxToParent(context, width, height, boxingStrategy);
		}
	}
	
	protected void adjustSize(IAddContext context, int width, int height) {
		if (context instanceof AddContext) {
			AddContext addContext = (AddContext) context;
			addContext.setSize(width, height);
		}
	}

	protected void adjustLocation(IAddContext context, int width, int height) {
		
		if (context instanceof AddContext) {
			AddContext addContext = (AddContext) context;
			
			int x = addContext.getX() - width / 2;
			int y = addContext.getY() - height / 2;
		
			addContext.setLocation(x, y);
		}
	}
	
	/**
	 * Return true if add context
	 * @param context
	 * @return
	 */
	protected BoxingStrategy getBoxingStrategy(IAddContext context) {
		return BoxingStrategy.NONE;
	}
	
	protected void boxToParent(IAddContext context, int width, int height, BoxingStrategy strategy) {

		if (context instanceof AddContext) {
			AddContext addContext = (AddContext) context;

			ContainerShape targetContainer = context.getTargetContainer();
			
			// do not perform boxing on diagram
			if (targetContainer instanceof Diagram) {
				return;
			}
			
			IRectangle targetBounds = LayoutUtil.getRelativeBounds(targetContainer);
			
			IRectangle box = LayoutUtil.box(rectangle(addContext.getX(), addContext.getY(), width, height), targetBounds, BOX_PADDING, strategy);

			if (FeatureSupport.isParticipant(targetContainer)) {
				int offset = GraphicsUtil.PARTICIPANT_LABEL_OFFSET + BOX_PADDING;
				if (addContext.getX() <= offset) {
					box.setX(offset);
				}
			} else if (FeatureSupport.isLane(targetContainer)) {
				int offset = 15 + BOX_PADDING;
				if (addContext.getX() <= offset) {
					box.setX(offset);
				}
			}
			
			addContext.setLocation(box.getX(), box.getY());
			addContext.setSize(box.getWidth(), box.getHeight());
		}
	}
	
	protected int getHeight(IAddContext context) {
		return context.getHeight() > 0 ? context.getHeight() :
			(isHorizontal(context) ? getDefaultHeight() : getDefaultWidth());
	}
	
	protected int getWidth(IAddContext context) {
		return context.getWidth() > 0 ? context.getWidth() :
			(isHorizontal(context) ? getDefaultWidth() : getDefaultHeight());
	}

	protected boolean isHorizontal(ITargetContext context) {
		if (!isImport(context)) {
			// not importing - set isHorizontal to be the same as parent Pool
			if (FeatureSupport.isTargetParticipant(context)) {
				Participant targetParticipant = FeatureSupport.getTargetParticipant(context);
				BPMNShape participantShape = findDIShape(targetParticipant);
				if (participantShape != null) {
					return participantShape.isIsHorizontal();
				}
			} else
			
			if (FeatureSupport.isTargetLane(context)) {
				Lane targetLane = FeatureSupport.getTargetLane(context);
				BPMNShape laneShape = findDIShape(targetLane);
				if (laneShape != null) {
					return laneShape.isIsHorizontal();
				}
			}
		}
		return Bpmn2Preferences.getInstance().isHorizontalDefault();
	}
	
	/**
	 * Return the default height
	 * 
	 * @return
	 */
	public abstract int getDefaultHeight();
	
	/**
	 * Return the default width
	 * @return
	 */
	public abstract int getDefaultWidth();
	
	/**
	 * Return true if the element should create a label
	 * 
	 * @return
	 */
	protected abstract boolean isCreateExternalLabel();
}