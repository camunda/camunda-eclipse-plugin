package org.camunda.bpm.modeler.core.utils;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.vector;
import static org.camunda.bpm.modeler.core.layout.util.VectorUtil.substract;
import static org.camunda.bpm.modeler.core.utils.FeatureUtil.getFeature;
import static org.camunda.bpm.modeler.core.utils.PictogramElementPropertyUtil.get;
import static org.camunda.bpm.modeler.core.utils.PictogramElementPropertyUtil.set;

import java.util.List;

import org.camunda.bpm.modeler.core.features.PropertyNames;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.ui.features.context.RepositionContext;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.AbstractText;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

/**
 * 
 * @author nico.rehwaldt
 */
public class LabelUtil {
	
	/**
	 * Reposition label if any after changes in the given pictogram element
	 * 
	 * @param element
	 * @param featureProvider
	 */
	public static void repositionLabel(PictogramElement element, IFeatureProvider featureProvider) {

		ContainerShape labelShape = getLabelShape(element, featureProvider.getDiagramTypeProvider().getDiagram());
		
		if (labelShape != null) {
			repositionLabel(element, labelShape, featureProvider);
		}
	}

	/**
	 * Reposition label if any after changes in the given pictogram element
	 * 
	 * @param element
	 * @param labelShape
	 * @param featureProvider
	 */
	public static void repositionLabel(PictogramElement element, ContainerShape labelShape, IFeatureProvider featureProvider) {
		RepositionContext repositionContext = new RepositionContext();
		
		repositionContext.setShape(labelShape);
		repositionContext.setReferencedElement(element);
		
		IFeature repositionFeature = getFeature(featureProvider, repositionContext);
		if (repositionFeature.canExecute(repositionContext)) {
			repositionFeature.execute(repositionContext);
		}
	}
	
	/**
	 * Reposition connection label if any after changes on the connection
	 * 
	 * @param connection
	 * @param featureProvider
	 */
	public static void repositionConnectionLabel(FreeFormConnection connection, IFeatureProvider featureProvider) {
		repositionLabel(connection, featureProvider);
	}

	/**
	 * Returns the label of a given pictogram element (connected via the elements business object)
	 * in the scope of the given diagram.
	 * 
	 * @param pictogramElement
	 * @param diagram
	 * 
	 * @return the label shape or null if non was found.
	 */
	public static ContainerShape getLabelShape(PictogramElement pictogramElement, Diagram diagram) {
		return getLabelShape(BusinessObjectUtil.getFirstBaseElement(pictogramElement), diagram);
	}
	
	/**
	 * Returns the text of a label shape
	 * 
	 * @param labelShape
	 * @return
	 */
	public static AbstractText getLabelShapeText(Shape labelShape) {
		
		if (!(labelShape instanceof ContainerShape)) {
			throw new IllegalArgumentException("Expected argument to be a container shape");
		}
		
		ContainerShape containerLabelShape = (ContainerShape) labelShape;
		
		return (AbstractText) containerLabelShape.getChildren().get(0).getGraphicsAlgorithm();
	}
	
	/**
	 * Returns the corresponding non label pictogram element for a given label shape (connected via the elements business object)
	 * in the scope of the given diagram.
	 * 
	 * @param pictogramElement
	 * @param diagram
	 * 
	 * @return the non label pictogram element or null if non was found.
	 */
	public static PictogramElement getNonLabelPictogramElement(Shape labelShape, Diagram diagram) {
		return getNonLabelPictogramElement(BusinessObjectUtil.getFirstBaseElement(labelShape), diagram);
	}
	
	/**
	 * Returns non label pictogram element for a given business object 
	 * in the scope of the given diagram.
	 * 
	 * @param bpmnElement
	 * @param diagram
	 * 
	 * @return the non label pictogram element or null if no label was found
	 */
	public static PictogramElement getNonLabelPictogramElement(BaseElement bpmnElement, Diagram diagram) {
		if (bpmnElement == null || diagram == null) {
			throw new IllegalArgumentException("Arguments may not be null");
		}
		
		List<PictogramElement> linkedPictogramElements = Graphiti.getLinkService().getPictogramElements(diagram, bpmnElement);
		
		for (PictogramElement element : linkedPictogramElements) {
			if (!isLabel(element)) {
				return (PictogramElement) element;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the label of the given business object 
	 * in the scope of the given diagram.
	 * 
	 * @param bpmnElement
	 * @param diagram
	 * 
	 * @return the label or null if no label was found
	 */
	public static ContainerShape getLabelShape(BaseElement bpmnElement, Diagram diagram) {
		if (bpmnElement == null || diagram == null) {
			throw new IllegalArgumentException("Arguments may not be null");
		}
		
		List<PictogramElement> linkedPictogramElements = Graphiti.getLinkService().getPictogramElements(diagram, bpmnElement);
		
		for (PictogramElement element : linkedPictogramElements) {
			if (isLabel(element)) {
				return (ContainerShape) element;
			}
		}
		
		return null;
	}

	/**
	 * Returns true if the given pictogram element represents a label.
	 * 
	 * @param element
	 * @return
	 */
	public static boolean isLabel(PictogramElement element) {
		return Graphiti.getPeService().getPropertyValue(element, GraphicsUtil.LABEL_PROPERTY) != null;
	}

	/**
	 * Sets properties on the given pictogram element which denotes it as a label
	 *  
	 * @param textContainerShape
	 */
	public static void makeLabel(PictogramElement element) {
		Graphiti.getPeService().setPropertyValue(element, GraphicsUtil.LABEL_PROPERTY, "true");
	}

	/**
	 * Returns the label offset for a given shape
	 * 
	 * @param labelShape
	 * @return
	 */
	public static Point getStoredLabelOffset(Shape labelShape) {
		Integer x = get(labelShape, PropertyNames.OFFSET_X, Integer.class);
		Integer y = get(labelShape, PropertyNames.OFFSET_Y, Integer.class);
		
		if (x == null || y == null) {
			throw new IllegalArgumentException(String.format("No LABEL_OFFSET set for shape <%s>", labelShape));
		}
		
		return point(x, y);
	}

	/**
	 * Sets the label offset for a given shape
	 * 
	 * @param labelShape
	 * @param offset
	 */
	public static void storeLabelOffset(Shape labelShape, Point offset) {
		set(labelShape, PropertyNames.OFFSET_X, offset.getX());
		set(labelShape, PropertyNames.OFFSET_Y, offset.getY());
	}

	public static void updateStoredShapeLabelOffset(Shape labelShape, Diagram diagram) {

		PictogramElement pictogramElement = LabelUtil.getNonLabelPictogramElement(labelShape, diagram);
		
		if (pictogramElement == null) {
			return;
		}
		
		if (pictogramElement instanceof Shape) {
			Point diff = getLabelOffset(labelShape, (Shape) pictogramElement);			
			storeLabelOffset(labelShape, diff);
		} else {
			throw new IllegalArgumentException(String.format("Labeled element referenced by label <%s> is not a shape", labelShape));
		}
	}

	/**
	 * Gets the label offset between a label shape and its labeled element
	 * 
	 * @param labelShape
	 * @param labeledShape
	 * @return
	 */
	protected static Point getLabelOffset(Shape labelShape, Shape labeledShape) {
		
		Point labelPosition = getLabelPosition(labelShape);
		Point shapeReferencePoint = point(LayoutUtil.getAbsoluteShapeCenter(labeledShape));
		
		Point offset = point(substract(vector(labelPosition), vector(shapeReferencePoint)));
		
		return offset;
	}

	/**
	 * Returns the label position of the given label shape.
	 * 
	 * The position returned is the top middle point of the shape in absolute coordinates.
	 * 
	 * @param labelShape
	 * @return
	 */
	public static Point getLabelPosition(Shape labelShape) {
		IRectangle labelBounds = LayoutUtil.getAbsoluteBounds(labelShape);
		
		return point(labelBounds.getX() + labelBounds.getWidth() / 2, labelBounds.getY());
	}

	/**
	 * Return the required label adjustment to align the label according to its offset.
	 * 
	 * @param labelShape
	 * @param labeledShape
	 * @return
	 */
	public static Point getAdjustedLabelPosition(Shape labelShape, Shape labeledShape) {
		Point labelOffset = getLabelOffset(labelShape, labeledShape);
		Point storedLabelOffset = getStoredLabelOffset(labelShape);
		
		return point(substract(vector(storedLabelOffset), vector(labelOffset)));
	}
}
