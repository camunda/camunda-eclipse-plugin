package org.camunda.bpm.modeler.ui.features.label;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.features.AbstractAddBpmnShapeFeature;
import org.camunda.bpm.modeler.core.features.UpdateBaseElementNameFeature;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNLabel;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.core.runtime.Assert;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.di.DiagramElement;
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

public abstract class AbstractAddLabelFeature extends AbstractAddBpmnShapeFeature<BaseElement> {
	
	public AbstractAddLabelFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		// can always add labels
		return true;
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
		
		LabelUtil.makeLabel(newLabelShape);
		
		// perform actual positioning of label
		GraphicsUtil.setLabelPosition(text, newLabelShape, x, y);
		
		DiagramElement diagramElement = ModelHandler.findDIElement(diagram, baseElement);
		Assert.isNotNull(diagramElement);
		
		link(newLabelShape, new Object[] { baseElement, diagramElement });
		
		return newLabelShape;
	}
	
	@Override
	protected BPMNShape createDi(Shape shape, BaseElement baseElement, IAddContext context) {
		
		// no label di should be created upon add for now
		return null;
	}
	
	@Override
	protected IRectangle getAddBounds(IAddContext context) {

		BaseElement baseElement = getBusinessObject(context);
		
		IRectangle bounds;
		
		// Return bounds from DI upon import
		if (isImport(context)) {
			bounds = getBoundsFromDi(baseElement);
			
			if (bounds != null) {
				return bounds;
			}
		}
		
		return getLabelAddBounds(context);
	}

	private IRectangle getLabelAddBounds(IAddContext context) {
		ContainerShape container = context.getTargetContainer();
		IRectangle containerBounds = LayoutUtil.getAbsoluteBounds(container);
		
		// calculate absolute coordinates from 
		// designated target container
		int x = context.getX() + containerBounds.getX();
		int y = context.getY() + containerBounds.getY();
		
		int width = (Integer) context.getWidth();
		int height = (Integer) context.getHeight();
		
		return rectangle(x, y, width, height);
	}

	/**
	 * Return the label di bounds for a given {@link BaseElement}, if any.
	 * 
	 * @param baseElement
	 * @return
	 */
	protected IRectangle getBoundsFromDi(BaseElement baseElement) {
		DiagramElement diagramElement = ModelHandler.findDIElement(getDiagram(), baseElement);
		
		BPMNLabel bpmnLabel = getDiLabel(diagramElement);
		
		if (bpmnLabel != null) {

			Bounds bounds = bpmnLabel.getBounds();
			if (bounds != null) {
				// we use the top middle anchor for a text shape
				return rectangle(
					(int) bounds.getX() + (int) (bounds.getWidth() / 2), 
					(int) bounds.getY(), 
					(int) bounds.getWidth(), 
					(int) bounds.getHeight());
			}
		}
		
		return null;
	}
	
	private BPMNLabel getDiLabel(DiagramElement diagramElement) {
		if (diagramElement == null) {
			return null;
		}
		
		if (diagramElement instanceof BPMNEdge) {
			return ((BPMNEdge) diagramElement).getLabel();
		} else
		if (diagramElement instanceof BPMNShape) {
			return ((BPMNShape) diagramElement).getLabel();
		} else {
			return null;
		}
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
