package org.eclipse.bpmn2.modeler.core.test.util;

import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class TestUtil {
	
	public static String toDetailsString(Shape shape) {
		StringBuilder builder = new StringBuilder();
		
		appendToString(builder, shape, "");
		
		return builder.toString();
	}
	
	private static void appendToString(StringBuilder builder, Shape shape, String indent) {
		builder
			.append(indent)
			.append(shape)
			.append("\n");
		
		indent += "  ";
		
		PictogramLink link = shape.getLink();
		if (link != null) {
			builder
				.append(indent)
				.append("linked: ")
				.append(shape.getLink().getBusinessObjects())
				.append("\n");
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
}
