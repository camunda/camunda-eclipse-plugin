package org.camunda.bpm.modeler.test.util.assertions.test;

import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
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
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		assertThat(connection).hasNoDiagonalEdges();
	}
	
	@Test
	@DiagramResource
	public void testAssertNoDiagonalEdgesFail() {
		
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		
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
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		
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
		
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		
		try {
			assertThat(connection).hasBendpointCount(2);
			fail("Expected assertion error");
		} catch (AssertionError e) {
			// expected
		}
	}
}
