package org.eclipse.bpmn2.modeler.core.test.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;

public abstract class TestUtil {
	
	public static String toDetailsString(Shape shape) {
		StringBuilder builder = new StringBuilder();
		
		appendToString(builder, shape, "");
		
		return builder.toString();
	}
	
	private static void appendToString(StringBuilder builder, Shape shape, String indent) {
		
		appendPictogramElementToString(builder, shape, indent);
		
		indent += "  ";
		
		PictogramLink link = shape.getLink();
		if (link != null) {
			builder
				.append(indent)
				.append("linked: ");
			
			appendBusinessObjects(builder, shape.getLink().getBusinessObjects());
			
			builder.append("\n");
		}
		
		List<Connection> connections = new ArrayList<Connection>();
		
		for (Anchor anchor: shape.getAnchors()) {
			connections.addAll(anchor.getIncomingConnections());
			connections.addAll(anchor.getOutgoingConnections());
		}
		
		if (!connections.isEmpty()) {
			builder.append(indent).append("connections: [").append("\n");
			
			for (Connection connection: connections) {
				appendToString(builder, connection, indent + "  ");
			}
			
			builder.append(indent).append("]").append("\n");
		}
		
		if (shape instanceof ContainerShape) {
			ContainerShape containerShape = (ContainerShape) shape;

			builder.append(indent).append("children: [").append("\n");
			
			for (Shape child: containerShape.getChildren()) {
				appendToString(builder, child, indent + "  ");
			}
			builder.append(indent).append("]").append("\n");
		}
	}

	private static void appendToString(StringBuilder builder, Connection connection, String indent) {
		
		appendPictogramElementToString(builder, connection, indent);
		
		indent += "  ";
		
		PictogramLink link = connection.getLink();
		if (link != null) {
			builder
				.append(indent)
				.append("linked: ");
			
			appendBusinessObjects(builder, connection.getLink().getBusinessObjects());
			
			builder
				.append("\n");
		}
	}

	private static void appendBusinessObjects(StringBuilder builder, List<EObject> businessObjects) {
		builder.append("[");
		
		boolean first = true;
		
		for (EObject o: businessObjects) {
			if (!first) {
				builder.append(", ");
			} else {
				first = false;
			}
			
			if (o instanceof BaseElement) {
				appendBpmnElementToString(builder, (BaseElement) o);
				
			} else if (o instanceof DiagramElement){
				appendBpmnDiElementToString(builder, (DiagramElement) o);
				
			} else {
				builder.append(o.getClass().getSimpleName());
			}
		}
		
		builder.append("]");
	}
	
	private static void appendPictogramElementToString(StringBuilder builder, PictogramElement element, String indent) {
		builder
			.append(indent);

		GraphicsAlgorithm graphics = element.getGraphicsAlgorithm();
		
		builder
			.append(element.getClass().getSimpleName())
			.append("(bounds: [")
			.append(graphics.getX()).append("@").append(graphics.getY())
			.append(", ")
			.append(graphics.getWidth()).append("@").append(graphics.getHeight())
			.append("])")
			.append("\n");
	
	}
	
	private static void appendBpmnElementToString(StringBuilder builder, BaseElement element) {
		builder
			.append(element.getClass().getSimpleName())
			.append("(id: ")
			.append(element.getId()).append(")");
	}
	
	private static void appendBpmnDiElementToString(StringBuilder builder, DiagramElement element) {
		builder
			.append(element.getClass().getSimpleName())
			.append("(id: ")
			.append(element.getId()).append(")");
	}
}
