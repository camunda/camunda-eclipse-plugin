/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.core.test.util.assertions;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.Assertions;
import org.fest.assertions.core.Condition;

/**
 * 
 * @author Nico Rehwaldt
 */
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
	
	public AbstractShapeAssert<ContainerShapeAssert, ContainerShape> hasChild(Shape child) {
		Assertions.assertThat(actual.getChildren().contains(child));
		return myself;
	}

	@Override
	public AbstractShapeAssert<ContainerShapeAssert, ContainerShape> doesNotHaveChild(Shape child) {
		Assertions.assertThat(!actual.getChildren().contains(child));
		return myself;
	}
}
