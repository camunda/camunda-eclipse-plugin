package org.eclipse.bpmn2.modeler.ui.features.label;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.features.ContextConstants;
import org.eclipse.bpmn2.modeler.core.features.UpdateBaseElementNameFeature;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class AddLabelFeature extends AbstractAddShapeFeature {

	public AddLabelFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context) && FeatureSupport.isTargetLaneOnTop(context);
		boolean intoParticipant = FeatureSupport.isTargetParticipant(context);
		boolean intoFlowELementContainer = BusinessObjectUtil.containsElementOfType(context.getTargetContainer(),
		        FlowElementsContainer.class);
		return intoDiagram || intoLane || intoParticipant || intoFlowELementContainer;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();
		
		int width = (Integer) context.getProperty(ContextConstants.WIDTH);
		int height = (Integer) context.getProperty(ContextConstants.HEIGHT);
		
		int x = context.getX();
		int y = context.getY();
		
		BaseElement baseElement = (BaseElement) context.getProperty(ContextConstants.BUSINESS_OBJECT);
		BPMNShape bpmnShape = (BPMNShape) ModelHandler.findDIElement(baseElement);
		
		ContainerShape targetContainer = getTargetContainer(context);
		
		final ContainerShape textContainerShape = peService.createContainerShape(targetContainer, true);
		gaService.createInvisibleRectangle(textContainerShape);
		
		Shape textShape = peService.createShape(textContainerShape, false);
		peService.setPropertyValue(textShape, UpdateBaseElementNameFeature.TEXT_ELEMENT, Boolean.toString(true));
		String name = ModelUtil.getDisplayName(baseElement);
		MultiText text = gaService.createDefaultMultiText(getDiagram(), textShape, name);
		StyleUtil.applyStyle(text, baseElement);
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_TOP);
		
		this.link(textContainerShape, baseElement);
		updatePictogramElement(textContainerShape);
		
		if (bpmnShape != null && bpmnShape.getLabel() != null && bpmnShape.getLabel().getBounds() != null) {
		  x = getRelativeX(targetContainer, (int) bpmnShape.getLabel().getBounds().getX());
		  y = getRelativeY(targetContainer, (int) bpmnShape.getLabel().getBounds().getY());
		}
		
		// Boundary events get a different add context, so use the context coodinates relative
		if ( (baseElement instanceof BoundaryEvent) && !isImport(context) ){
			x = context.getTargetContainer().getGraphicsAlgorithm().getX() + context.getX()-width/2;
			y = context.getTargetContainer().getGraphicsAlgorithm().getY() + context.getY()-height/2;
		}
		
		if (bpmnShape.getLabel() != null && context.getProperty(DIUtils.IMPORT_PROPERTY) != null) {
		  GraphicsUtil.setLabelPosition(text, textContainerShape, x, y);
		} else {
		  GraphicsUtil.alignWithShape(text, textContainerShape, width, height, x, y, 0, 0);
		}
		
		Graphiti.getPeService().setPropertyValue(textContainerShape, GraphicsUtil.LABEL_PROPERTY, "true");
		
		layoutPictogramElement(textContainerShape);
		
		return textContainerShape;
	}
	
	private boolean isImport(IAddContext context) {
		return context.getProperty(DIUtils.IMPORT_PROPERTY) == null ? false : (Boolean) context.getProperty(DIUtils.IMPORT_PROPERTY);
	}
	
	/**
	 * Get the correct target control, boundary events need special handling, because we need to find a parent,
	 * where the label is visible.
	 * 
	 * @param context
	 * @return the target control for the current context
	 */
	ContainerShape getTargetContainer(IAddContext context) {
		boolean isBoundary = context.getProperty(ContextConstants.BUSINESS_OBJECT) instanceof BoundaryEvent;
		
		if ( isBoundary && !isImport(context) ){
			if (context.getTargetContainer()!=null){
				return context.getTargetContainer().getContainer();
			}
		}
		return context.getTargetContainer();
	}

	private int getRelativeX(ContainerShape targetContainer, int x) {
	  x -= targetContainer.getGraphicsAlgorithm().getX();
	  ContainerShape parent = targetContainer.getContainer();
	  if (parent != null && !(parent instanceof Diagram)) {
	    return getRelativeX(parent, x);
	  }
	  return x;
	}

	 private int getRelativeY(ContainerShape targetContainer, int y) {
	    y -= targetContainer.getGraphicsAlgorithm().getY();
	    ContainerShape parent = targetContainer.getContainer();
	    if (parent != null && !(parent instanceof Diagram)) {
	      return getRelativeY(parent, y);
	    }
	    return y;
	 }
	
}
