package org.eclipse.bpmn2.modeler.ui.features.label;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.eclipse.bpmn2.modeler.core.utils.ContextUtil.is;
import static org.eclipse.bpmn2.modeler.core.utils.ContextUtil.isNot;
import static org.eclipse.bpmn2.modeler.core.utils.ContextUtil.set;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.di.BPMNLabel;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.features.AbstractAddBpmnShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.ContextConstants;
import org.eclipse.bpmn2.modeler.core.features.UpdateBaseElementNameFeature;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.core.runtime.Assert;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class AddLabelFeature extends AbstractAddBpmnShapeFeature<BaseElement> {

	private static final String CUSTOM_POSITION = "AddLabelFeature.CUSTOM_POSITION";
	
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

	/**
	 * Creates the label shape
	 */
	@Override
	protected ContainerShape createPictogramElement(IAddContext context, IRectangle bounds) {

		BaseElement baseElement = getBusinessObject(context);
		
		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();

		int x = bounds.getX();
		int y = bounds.getY();
		int width = bounds.getWidth();
		int height = bounds.getHeight();
		
		// target is the diagram
		Diagram diagram = getDiagram();
		
		// labels are always added to the diagram
		ContainerShape newLabelShape = peService.createContainerShape(diagram, true);
		gaService.createInvisibleRectangle(newLabelShape);
		
		Shape textShape = peService.createShape(newLabelShape, false);
		peService.setPropertyValue(textShape, UpdateBaseElementNameFeature.TEXT_ELEMENT, Boolean.toString(true));
		String name = ModelUtil.getDisplayName(baseElement);
		MultiText text = gaService.createDefaultMultiText(diagram, textShape, name);

		StyleUtil.applyStyle(text, baseElement);
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_TOP);
		
		GraphicsUtil.makeLabel(newLabelShape);
		
		// perform actual positioning of label
		if (is(context, CUSTOM_POSITION)) {
		  GraphicsUtil.setLabelPosition(text, newLabelShape, x, y);
		} else {
		  // perform alignment with shape if we got no di coordinates
		  GraphicsUtil.alignWithShape(text, newLabelShape, width, height, point(x, y), null);
		}
		
		BPMNShape bpmnShape = (BPMNShape) ModelHandler.findDIElement(diagram, baseElement);
		Assert.isNotNull(bpmnShape);
		
		link(newLabelShape, new Object[] { baseElement, bpmnShape });
		
		return newLabelShape;
	}
	
	@Override
	protected void postAddHook(IAddContext context, ContainerShape newLabelShape) {
		super.postAddHook(context, newLabelShape);
		
		IPeService peService = Graphiti.getPeService();
		
		// send element to front
		peService.sendToFront(newLabelShape);
	}
	
	@Override
	protected BPMNShape createDi(Shape shape, BaseElement baseElement, IAddContext context) {
		
		// no label di should be created upon add for now
		return null;
	}
	
	@Override
	protected IRectangle getAddBounds(IAddContext context) {

		ContainerShape container = context.getTargetContainer();
		IRectangle containerBounds = LayoutUtil.getAbsoluteBounds(container);
		
		// calculate absolute coordinates from 
		// designated target container
		int x = context.getX() + containerBounds.getX();
		int y = context.getY() + containerBounds.getY();
		
		int width = (Integer) context.getProperty(ContextConstants.WIDTH);
		int height = (Integer) context.getProperty(ContextConstants.HEIGHT);
		
		BaseElement baseElement = getBusinessObject(context);
		BPMNShape bpmnShape = (BPMNShape) ModelHandler.findDIElement(getDiagram(), baseElement);
		
		BPMNLabel bpmnLabel = bpmnShape.getLabel();
		
		if (bpmnLabel != null && bpmnLabel.getBounds() != null) {
		  x = (int) bpmnLabel.getBounds().getX();
		  y = (int) bpmnLabel.getBounds().getY();
		  
		  // we got actual coordinates from DI
		  set(context, CUSTOM_POSITION);
		} else
		if (isNot(context, DIUtils.IMPORT)) {
			
			// Boundary events get a different add context, 
			// so use the context coordinates relative
			if (baseElement instanceof BoundaryEvent) {
				
				x -= width / 2;
				y -= height / 2;
			}
		}
		
		return rectangle(x, y, width, height);
	}

	@Override
	public int getDefaultHeight() {
		return 0;
	}

	@Override
	public int getDefaultWidth() {
		return 0;
	}

	@Override
	protected boolean isCreateExternalLabel() {
		// we are a label ourself
		return false;
	}
}
