package org.eclipse.bpmn2.modeler.core.test.feature.add;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.elementOfType;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.AddDataObjectOperation.addDataObject;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.AddDataStoreOperation.addDataStore;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveShapeOperation.move;

import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.DataStore;
import org.eclipse.bpmn2.DataStoreReference;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.Fail;
import org.junit.Test;

public class AddTaskFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddFeatureTestBase.testAddToDiagram.bpmn")
	public void testAddToDiagram() throws Exception {

		// given empty diagram

		Fail.fail("Implement me!");
		
		// when
		// element is added to it
		addDataStore(diagramTypeProvider)
			.toContainer(diagram)
			.execute();

		Shape shape = diagram.getChildren().get(0);
		Shape labelShape = GraphicsUtil.getLabelShape(shape, diagram);
		
		// then
		// diagram should contain the element and a label
		assertThat(diagram)
			.hasContainerShapeChildCount(2);
		
		assertThat(shape)
			.isLinkedTo(elementOfType(DataStoreReference.class));
		
		assertThat(labelShape)
			.isNotNull()
			.isContainedIn(diagram);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddFeatureTestBase.testAddToDiagram.bpmn")
	public void testAddOnToFlow() throws Exception {

		// given empty diagram

		Fail.fail("Implement me!");
		
		// when
		// element is added to it
		addDataStore(diagramTypeProvider)
			.toContainer(diagram)
			.execute();

		Shape shape = diagram.getChildren().get(0);
		Shape labelShape = GraphicsUtil.getLabelShape(shape, diagram);
		
		// then
		// diagram should contain the element and a label
		assertThat(diagram)
			.hasContainerShapeChildCount(2);
		
		assertThat(shape)
			.isLinkedTo(elementOfType(DataStoreReference.class));
		
		assertThat(labelShape)
			.isNotNull()
			.isContainedIn(diagram);
	}
}
