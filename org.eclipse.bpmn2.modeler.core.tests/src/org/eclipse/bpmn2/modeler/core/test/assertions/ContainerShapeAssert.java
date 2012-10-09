package org.eclipse.bpmn2.modeler.core.test.assertions;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.Assertions;
import org.fest.assertions.core.Condition;

public class ContainerShapeAssert extends AbstractShapeAssert<ContainerShapeAssert, ContainerShape> {

	public ContainerShapeAssert(ContainerShape actual) {
		super(actual, ContainerShapeAssert.class);
	}

	@Override
	public ContainerShapeAssert hasChildren() {
		Assertions.assertThat(actual.getChildren()).isNotEmpty();
		
		return myself;
	}

	@Override
	public ContainerShapeAssert hasChildLinkedTo(Condition<EObject> condition) {
		hasChildren();
		
		for (Shape child: actual.getChildren()) {
			PictogramLink link = child.getLink();
			if (link == null) {
				continue;
			}
			
			EList<EObject> businessObjects = link.getBusinessObjects();
			if (businessObjects == null) {
				continue;
			}
			
			Assertions
				.assertThat(businessObjects)
				.areAtLeast(1, condition);
		}
		
		return myself;
	}

	@Override
	public AbstractShapeAssert<ContainerShapeAssert, ContainerShape> isContainerShape() {
		return myself;
	}

	@Override
	public AbstractShapeAssert<ContainerShapeAssert, ContainerShape> hasNoChildren() {
		Assertions.assertThat(actual.getChildren()).isEmpty();
		return myself;
	}
}
