package org.eclipse.bpmn2.modeler.core.test.util;

import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

/**
 * Utility methods
 * 
 * @author nico.rehwaldt
 */
public class Util {

	public static <T extends BaseElement> T findBusinessObjectById(Shape e, String id, Class<T> cls) {
		BaseElement element = findBusinessObjectById(e, id);
		
		assertThat(element).isInstanceOf(cls);
		
		return cls.cast(element);
	}
	
	public static Shape findShapeByBusinessObjectId(Shape shape, String id) {
		
		if (shape.getLink() != null) {
			if (findBusinessObjectById(shape.getLink().getBusinessObjects() , id) != null) {
				return shape;
			}
		}
		
		if (shape instanceof ContainerShape) {
			ContainerShape containerShape = (ContainerShape) shape;
			EList<Shape> children = containerShape.getChildren();
			for (Shape child : children) {
				Shape found = findShapeByBusinessObjectId(child, id);
				if (found != null) {
					return found;
				}
			}			
		}
		
		return null;
	}
	
	public static Connection findConnectionByBusinessObjectId(Diagram diagram, String id) {
		for (Connection con : diagram.getConnections() ) {
			if (findBusinessObjectById(con.getLink().getBusinessObjects() , id) != null) {
				return con;
			}
		}
		return null;
	}
	
	protected static BaseElement findBusinessObjectById(EList<EObject> businessObjects, String id) {
		for (EObject eObject : businessObjects) {
			if (eObject instanceof BaseElement) {
				BaseElement baseElement = (BaseElement)eObject;
				if (baseElement.getId() != null && baseElement.getId().equals(id)) {
					return baseElement;
				}
			}
		}
		return null;
	}

	public static BaseElement findBusinessObjectById(Shape shape, String id) {
		BaseElement object = null;
		
		Diagram diagram = Graphiti.getPeService().getDiagramForPictogramElement(shape);
		
		for (Connection c: diagram.getConnections()) {
			object = findBusinessObjectById(c.getLink().getBusinessObjects() , id);
			if (object != null) {
				return object;
			}
		}
		
		return recursiveFindBusinessObjectById(shape, id);
	}

	private static BaseElement recursiveFindBusinessObjectById(Shape shape, String id) {
		BaseElement object = null;
		
		if (shape.getLink() != null) {
			object = findBusinessObjectById(shape.getLink().getBusinessObjects() , id);
			if (object != null) {
				return object;
			}
		}
		
		if (shape instanceof ContainerShape) {
			ContainerShape containerShape = (ContainerShape) shape;
			EList<Shape> children = containerShape.getChildren();
			for (Shape child : children) {
				object = recursiveFindBusinessObjectById(child, id);
				if (object != null) {
					return object;
				}
			}
		}
		
		return null;
	}
}
