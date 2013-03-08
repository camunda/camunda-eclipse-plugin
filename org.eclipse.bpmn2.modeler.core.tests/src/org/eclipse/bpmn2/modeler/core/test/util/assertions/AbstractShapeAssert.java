package org.eclipse.bpmn2.modeler.core.test.util.assertions;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Assertions;
import org.fest.assertions.api.Fail;
import org.fest.assertions.api.ObjectAssert;
import org.fest.assertions.core.Condition;

public abstract class AbstractShapeAssert<S extends AbstractShapeAssert<S, A>, A extends Shape> extends AbstractAssert<S, A> {

	public AbstractShapeAssert(A actual, Class<S> assertCls) {
		super(actual, assertCls);
	}
	
	public abstract AbstractShapeAssert<S, A> hasChildren();
	
	public abstract AbstractShapeAssert<S, A> hasChildLinkedTo(Condition<EObject> condition);
	
	public AbstractShapeAssert<S, A> isLinkedTo(Condition<EObject> condition) {
		PictogramLink link = actual.getLink();
		Assertions
			.assertThat(link)
			.isNotNull();
		
		EList<EObject> businessObjects = link.getBusinessObjects();
		
		
		Assertions
			.assertThat(businessObjects)
			.isNotNull()
			.areAtLeast(1, condition);
		
		return myself;
	}
	
	public AbstractShapeAssert<S, A> hasParentModelElement(EObject eObject) {
		
		EObject businessObjectForActual = BusinessObjectUtil.getBusinessObjectForPictogramElement(actual);
		EObject actualContainer = businessObjectForActual.eContainer();
		
		Assertions.assertThat(actualContainer).isSameAs(eObject);
		
		return myself;
	}

	public AbstractShapeAssert<S, A> isContainedIn(Shape containerShape) {
		Assertions.assertThat(actual.eContainer()).isEqualTo(containerShape);
		
		return myself;
	}
	
	public AbstractShapeAssert<S, A> isNotContainedIn(Shape containerShape) {
		Assertions.assertThat(actual.eContainer()).isNotEqualTo(containerShape);
		
		return myself;
	}
	
	public abstract AbstractShapeAssert<S, A> isContainerShape();
	
	public abstract AbstractShapeAssert<S, A> hasNoChildren();
	
	public abstract AbstractShapeAssert<S, A> hasChild(Shape child);
	public abstract AbstractShapeAssert<S, A> doesNotHaveChild(Shape child);
	
	public abstract AbstractShapeAssert<S, A> hasContainerShapeChildCount(int count);

	public PointAssert position() {
		IRectangle absoluteRectangle = LayoutUtil.getAbsoluteBounds(actual);
		return new PointAssert(point(absoluteRectangle.getX(), absoluteRectangle.getY()));
	}
	
	public IRectangleAssert bounds() {
		IRectangle bounds = LayoutUtil.getAbsoluteBounds(actual);
		
		return new IRectangleAssert(bounds);
	}

	/**
	 * Asserts that two shapes are in front of each other.
	 * 
	 * @param other
	 * @return
	 */
	public AbstractShapeAssert<S, A> isInFrontOf(Shape other) {
		
		List<ContainerShape> actualParents = getAllParents(actual);
		List<ContainerShape> otherParents = getAllParents(other);
		
		List<ContainerShape> sharedParents = new ArrayList<ContainerShape>(actualParents);
		
		// keep only same parents
		sharedParents.retainAll(otherParents);
		
		if (sharedParents.isEmpty()) {
			Fail.fail(String.format("Expected <%s> and <%s> to have shared parent", actual, other));
		}
		
		ContainerShape sameParent = sharedParents.get(0);
		Shape actualChild = actualParents.indexOf(sameParent) == 0 ? actual : actualParents.get(actualParents.indexOf(sameParent) - 1);
		Shape otherChild = otherParents.indexOf(sameParent) == 0 ? other : otherParents.get(otherParents.indexOf(sameParent) - 1);
		
		
		int indexOfActualChild = sameParent.getChildren().indexOf(actualChild);
		int indexOfOtherChild = sameParent.getChildren().indexOf(otherChild);
		
		if (indexOfActualChild < indexOfOtherChild) {
			Fail.fail(String.format("Expected <%s> or descendant to be in front of <%s> in shared parents <%s> children", actual, other, sameParent));
		}
		
		return myself;
	}
	
	/**
	 * Get parents of the shape up to the diagram (last element in resulting list).
	 * 
	 * @param shape
	 * @return
	 */
	protected List<ContainerShape> getAllParents(Shape shape) {
		List<ContainerShape> parents = new ArrayList<ContainerShape>();
		while (!(shape instanceof Diagram)) {
			shape = shape.getContainer();
			parents.add((ContainerShape) shape);
		}
		
		return parents;
	}
	
	/**
	 * Assert that a movement took place
	 * 
	 * @param expectedMovement
	 * @param referenceBounds
	 * @return
	 */
	public AbstractShapeAssert<S, A> movedBy(Point expectedMovement, IRectangle referenceBounds) {
		IRectangle bounds = LayoutUtil.getAbsoluteBounds(actual);
		
		Point diff = point(bounds.getX() - referenceBounds.getX(), bounds.getY() - referenceBounds.getY());
		
		Bpmn2ModelAssertions.assertThat(diff).isEqualTo(expectedMovement);
		
		return myself;
	}

	public FreeFormConnectionAssert outgoingConnectionTo(Shape connectionEnd) {
		
		List<Connection> matchingConnections = new ArrayList<Connection>();
		
		for (Anchor a: actual.getAnchors()) {
			for (Connection outgoingConnection: a.getOutgoingConnections()) {
				if (connectionEnd.equals(outgoingConnection.getEnd().getParent())) {
					matchingConnections.add(outgoingConnection);
				}
			}
		}
		
		if (matchingConnections.isEmpty()) {
			Fail.fail(String.format("Expected <%s> to have outgoing edge to <%s>", actual, connectionEnd));
		}

		if (matchingConnections.size() > 1) {
			Fail.fail(String.format("Expected <%s> to have exactly one outgoing edge to <%s> but found (<%s>)", actual, connectionEnd, matchingConnections));
		}
			
		return new FreeFormConnectionAssert((FreeFormConnection) matchingConnections.get(0));
	}

	public PointAssert midPoint() {
		IRectangle absoluteRectangle = LayoutUtil.getAbsoluteBounds(actual);
		ILocation midPoint = LayoutUtil.getRectangleCenter(absoluteRectangle);
		
		return new PointAssert(point(midPoint));
	}
}
