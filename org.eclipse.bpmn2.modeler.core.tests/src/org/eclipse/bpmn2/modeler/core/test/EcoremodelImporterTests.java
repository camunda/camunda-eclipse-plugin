package org.eclipse.bpmn2.modeler.core.test;

import java.net.URL;
import java.util.Collections;

import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerResourceFactoryImpl;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerResourceImpl;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * 
 * @author Daniel Meyer
 * 
 */
public class EcoremodelImporterTests {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	protected ResourceSetImpl resourceSet;
	protected TransactionalEditingDomain editingDomain;
	protected Resource resource;

	@Before
	public void initializeTest() throws Exception {
		URL resourceUrl = Activator.getBundleContext()
			.getBundle()
			.getResource("org/eclipse/bpmn2/modeler/core/test/bpmn/TerminateEndEventTest.testProcessTerminate.bpmn");
		
		URI uri = URI.createFileURI("test.bpmn");
		
		resource = new Bpmn2ModelerResourceFactoryImpl().createResource(uri);
		resource.load(resourceUrl.openStream(), Collections.EMPTY_MAP);
		
		
		editingDomain = TransactionUtil.getEditingDomain(resource);
		if (editingDomain == null) {
			// Not yet existing, create one
			editingDomain = TransactionalEditingDomain.Factory.INSTANCE
					.createEditingDomain();
			editingDomain.getResourceSet().getResources().add(resource);
		}
	}
	
	@Test
	public void testCmd() {
		editingDomain.getCommandStack().execute(new AbstractTestCommand(temporaryFolder, editingDomain, "test.bpmn", resource) {
			void test() {

			}
		});
	}

	@After
	public void tearDownTest() {
		editingDomain.dispose();
	}

}
