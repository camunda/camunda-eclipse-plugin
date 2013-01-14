package org.eclipse.bpmn2.modeler.core.test.util.assertions;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Assertions;
import org.fest.assertions.core.Condition;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;

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

	public PointAssert position() {
		IRectangle absoluteRectangle = LayoutUtil.getAbsoluteBounds(actual);
		return new PointAssert(point(absoluteRectangle.getX(), absoluteRectangle.getY()));
	}
}
