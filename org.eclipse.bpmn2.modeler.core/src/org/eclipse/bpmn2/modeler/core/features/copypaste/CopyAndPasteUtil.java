package org.eclipse.bpmn2.modeler.core.features.copypaste;

import java.util.Arrays;
import java.util.List;

import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * Utilities for copy and paste
 * 
 * @author nico.rehwaldt
 */
public class CopyAndPasteUtil {

	public static ContainerShape getSharedParent(List<PictogramElement> pictogramElements) {
		ContainerShape parent = null;
		
		for (PictogramElement pictogramElement : pictogramElements) {
			if (pictogramElement instanceof Shape) {
				Shape s = (Shape) pictogramElement;
				if (parent == null) {
					parent = s.getContainer();
				} else {
					if (parent != s.getContainer()) {
						return null;
					}
				}
			}
		}
		
		return parent;
	}
	
	public static ContainerShape getSharedParent(PictogramElement[] pictogramElements) {
		return getSharedParent(Arrays.asList(pictogramElements));
	}
}
