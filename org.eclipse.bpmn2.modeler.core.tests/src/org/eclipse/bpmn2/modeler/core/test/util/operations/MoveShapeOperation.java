package org.eclipse.bpmn2.modeler.core.test.util.operations;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;

import java.util.List;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.LabelUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.fest.assertions.api.Assertions;

/**
 * 
 * @author Daniel Meyer
 * @author Nico Rehwaldt
 *
 */
public class MoveShapeOperation extends ShapeOperation<MoveShapeContext, IMoveShapeFeature> {
	
	private Point moveBy = null;
	
	public MoveShapeOperation(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		super(shape, diagramTypeProvider);
	}

	/**
	 * Returns the container this shape is visually contained in.
	 * 
	 * @param shape
	 * @return
	 */
	private Shape getReferenceContainer(Shape shape) {
		
		if (LabelUtil.isLabel(shape)) {
			EObject businessObject = BusinessObjectUtil.getFirstBaseElement(shape);
			
			List<PictogramElement> pictogramElements = Graphiti.getLinkService().getPictogramElements(getDiagram(), businessObject);
			for (PictogramElement pictogramElement : pictogramElements) {
				if (!LabelUtil.isLabel(pictogramElement)) {
					return ((Shape) pictogramElement).getContainer();
				}
			}
			
			throw new IllegalArgumentException("No reference container for shape: " + shape);
		} else {
			return shape.getContainer();
		}
	}

	@Override
	protected MoveShapeContext createContext() {
		MoveShapeContext context = new MoveShapeContext(shape);
		
		context.setSourceContainer(shape.getContainer());
		context.setTargetContainer(shape.getContainer());
		
		context.setLocation(0, 0);
		
		return context;
	}

	public MoveShapeOperation by(int x, int y) {
		this.moveBy = point(x, y);
		
		return this;
	}
	
	public MoveShapeOperation by(Point point) {
		this.moveBy = point;
		
		return this;
	}
	
	public MoveShapeOperation to(int x, int y) {
		context.setLocation(x, y);	
		return this;
	}
	
	public MoveShapeOperation to(Point point) {
		return to(point.getX(), point.getY());
	}
	
	protected Point getRelativePosition(Shape shape, Shape container) {
		IRectangle referenceBounds = LayoutUtil.getAbsoluteBounds(container);
		IRectangle shapeBounds = LayoutUtil.getAbsoluteBounds(shape);
		
		return point(
			shapeBounds.getX() - referenceBounds.getX(), 
			shapeBounds.getY() - referenceBounds.getY());
	}

	public MoveShapeOperation toContainer(ContainerShape containerShape) {
		context.setTargetContainer(containerShape);
		return this;
	}
	
	@Override
	public Object execute() {
		// apply a fix for the movement of labels
		updateRelativeMovement();
		
		// execute original operation
		return super.execute();
	}
	
	private void updateRelativeMovement() {

		Point relativePosition;
		
		// for labels we need to fake the movement into
		// a particular shape on target container change
		// because the label absolute coordinates need to be translated into target shape local ones.
		// (that is what Graphiti does, too and we will do it here as well).
		if (context.getSourceContainer() != context.getTargetContainer()) {
			Shape referenceContainer = getReferenceContainer(shape);
			
			relativePosition = getRelativePosition(shape, referenceContainer);
		} else {
			relativePosition = point(LayoutUtil.getRelativeBounds(shape));
		}
		
		if (moveBy != null) {
			context.setDeltaX(moveBy.getX());
			context.setDeltaY(moveBy.getY());
			
			// the delta information is not used in the Graphiti default implementations
			// context x / y should contain the new coordinates, Graphiti will calculate the delta itself
			context.setX(relativePosition.getX() + moveBy.getX());
			context.setY(relativePosition.getY() + moveBy.getY());
		}
	}

	@Override
	protected IMoveShapeFeature createFeature(MoveShapeContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		
		IMoveShapeFeature feature = featureProvider.getMoveShapeFeature(context);
		
		Assertions.assertThat(feature).as("Move feature").isNotNull();
		
		return feature;
	}
	
	public static MoveShapeOperation move(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		return new MoveShapeOperation(shape, diagramTypeProvider);
	}
}
