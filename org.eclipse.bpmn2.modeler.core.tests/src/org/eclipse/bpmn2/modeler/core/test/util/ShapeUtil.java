package org.eclipse.bpmn2.modeler.core.test.util;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class ShapeUtil {

	public static Shape findShapeByBusinessObjectId(Shape shape, String id) {
		
		if(shape.getLink() != null) {
			EList<EObject> businessObjects = shape.getLink().getBusinessObjects();
			for (EObject eObject : businessObjects) {
				if(eObject instanceof BaseElement) {
					BaseElement baseElement = (BaseElement)eObject;
					if(baseElement.getId() != null && baseElement.getId().equals(id)) {
						return shape;
					}
				}
			}
		}
		
		if (shape instanceof ContainerShape) {
			ContainerShape containerShape = (ContainerShape) shape;
			EList<Shape> children = containerShape.getChildren();
			for (Shape child : children) {
				Shape found = findShapeByBusinessObjectId(child, id);
				if(found != null) {
					return found;
				}
			}			
		}
		
		return null;
		
	}
}
