package org.eclipse.bpmn2.modeler.core.test.util.assertions.test;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.ShapeUtil;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class FreeFormConnectionAssertTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource
	public void testAssertNoDiagonalEdgesPass() {
		FreeFormConnection connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		assertThat(connection).hasNoDiagonalEdges();
	}
	
	@Test
	@DiagramResource
	public void testAssertNoDiagonalEdgesFail() {
		
		FreeFormConnection connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		
		try {
			assertThat(connection).hasNoDiagonalEdges();
			fail("Expected assertion error");
		} catch (AssertionError e) {
			// expected
		}
	}
	
	@Test
	@DiagramResource
	public void testAssertBendpointCountPass() {
		FreeFormConnection connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		
		assertThat(connection).hasBendpointCount(2);

		try {
			assertThat(connection).hasBendpointCount(3);
			fail("Expected assertion error");
		} catch (AssertionError e) {
			// expected
		}

		try {
			assertThat(connection).hasBendpointCount(1);
			fail("Expected assertion error");
		} catch (AssertionError e) {
			// expected
		}
	}
	
	@Test
	@DiagramResource
	public void testAssertBendpointCountFail() {
		
		FreeFormConnection connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		
		try {
			assertThat(connection).hasBendpointCount(2);
			fail("Expected assertion error");
		} catch (AssertionError e) {
			// expected
		}
	}
}
