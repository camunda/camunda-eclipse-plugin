package org.eclipse.bpmn2.modeler.core.test.importer.messageflow;

import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.mm.pictograms.impl.FreeFormConnectionImpl;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ImportMessageFlowTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testMessageFlow() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		EList<Shape> children = diagram.getChildren();
		
		// two elements + label
		Assert.assertEquals(3, children.size());
		
		// Two container shapes (process pools)
		// One element per shape (user task / intermediate catching event
		// message flow between these
		
		Shape shape1 = children.get(0);
		Assert.assertTrue(shape1 instanceof ContainerShape);
		
		Shape shape2 = children.get(1);
		Assert.assertTrue(shape2 instanceof ContainerShape);
		
		Assert.assertTrue(containsChildLinkedTo((ContainerShape) shape1, UserTask.class));
		Assert.assertTrue(containsChildLinkedTo((ContainerShape) shape2, IntermediateCatchEvent.class));
		
		EList<Connection> connections = diagram.getConnections();
		Assert.assertEquals(connections.size(), 1);
		
		Connection c = connections.get(0);
		Assert.assertTrue(c instanceof FreeFormConnectionImpl);
		Assert.assertTrue(isLinkedTo(c, MessageFlow.class));
	}

	@Test
	@DiagramResource
	public void testMessageFlowWithWayPoints() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		EList<Shape> children = diagram.getChildren();
		
		// two elements + label
		Assert.assertEquals(3, children.size());
		
		// Two container shapes (process pools)
		// One element per shape (user task / intermediate catching event
		// message flow between these
		
		Shape shape1 = children.get(0);
		Assert.assertTrue(shape1 instanceof ContainerShape);
		
		Shape shape2 = children.get(1);
		Assert.assertTrue(shape2 instanceof ContainerShape);
		
		Assert.assertTrue(containsChildLinkedTo((ContainerShape) shape1, UserTask.class));
		Assert.assertTrue(containsChildLinkedTo((ContainerShape) shape2, IntermediateCatchEvent.class));
		
		EList<Connection> connections = diagram.getConnections();
		Assert.assertEquals(connections.size(), 1);
		
		Connection c = connections.get(0);
		Assert.assertTrue(c instanceof FreeFormConnectionImpl);
		Assert.assertTrue(isLinkedTo(c, MessageFlow.class));
	}
	
	@Test
	@Ignore
	@DiagramResource
	public void testMessageFlowWithMessage() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		// Two container shapes (process pools)
		// One element per shape (user task / intermediate catching event
		// message flow between these
		
		// ...
		
		Assert.fail("Not supported");
	}

	@Test
	@DiagramResource
	public void testActivityToCollapsedPoolBidirectional() {
		ModelImport importer = createModelImport();
		importer.execute();
	}

	@Test
	@DiagramResource
	public void testActivityToCollapsedPool() {
		ModelImport importer = createModelImport();
		importer.execute();
	}
	
	@Test
	@DiagramResource
	public void testCollapsedPoolToActivity() {
		ModelImport importer = createModelImport();
		importer.execute();
	}
	
	private boolean containsChildLinkedTo(ContainerShape shape, Class<?> linkedElementCls) {
		for (Shape child: shape.getChildren()) {
			
			if (isLinkedTo(child, linkedElementCls)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isLinkedTo(PictogramElement element, Class<?> cls) {
		PictogramLink link = element.getLink();
		if (link == null) {
			return false;
		}
		
		for (EObject o: link.getBusinessObjects()) {
			if (cls.isInstance(o)) {
				return true;
			}
		}
		
		return false;
	}

}
