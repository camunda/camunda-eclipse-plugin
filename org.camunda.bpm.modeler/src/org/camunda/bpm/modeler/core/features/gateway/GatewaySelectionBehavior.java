package org.camunda.bpm.modeler.core.features.gateway;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.bpmn2.Gateway;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class GatewaySelectionBehavior {

	public static boolean canApplyTo(PictogramElement element) {
		if (element.getLink() == null || !(element instanceof ContainerShape)) {
			return false;
		}

		EList<EObject> objects = element.getLink().getBusinessObjects();

		for (EObject eObject : objects) {
			if (eObject instanceof Gateway) {
				return true;
			}
		}

		return false;
	}

	public static GraphicsAlgorithm[] getClickArea(PictogramElement element) {
		Iterator<Shape> iterator = Graphiti.getPeService().getAllContainedShapes((ContainerShape) element).iterator();
		GraphicsAlgorithm[] algorithms = new GraphicsAlgorithm[1];
		algorithms[0] = iterator.next().getGraphicsAlgorithm();
		return algorithms;
	}

	public static GraphicsAlgorithm getSelectionBorder(PictogramElement element) {
		Collection<Shape> children = Graphiti.getPeService().getAllContainedShapes((ContainerShape) element);
		PictogramElement first = children.iterator().next();
		return first.getGraphicsAlgorithm();
	}
}
