/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;

import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerResourceFactoryImpl;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 *
 */
public abstract class AbstractImportBpmnModelTest {

	protected TransactionalEditingDomain editingDomain;
	protected Bpmn2ResourceImpl resource;
	protected ModelHandler modelHandler;
	
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	public TransactionalEditingDomain createEditingDomain(String bpmnResourceName) {
		
		URL resourceUrl = Activator.getBundleContext().getBundle().getResource(bpmnResourceName);
		URI uri = URI.createFileURI("test.bpmn");

		resource = (Bpmn2ResourceImpl) new Bpmn2ModelerResourceFactoryImpl().createResource(uri);
		loadResource(resource, resourceUrl);
		
		// TODO: get rid of evil ModelHandler
		modelHandler = ModelHandlerLocator.createModelHandler(uri, resource);

		editingDomain = TransactionUtil.getEditingDomain(resource);
		if (editingDomain == null) {
			// Not yet existing, create one
			editingDomain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();
			editingDomain.getResourceSet().getResources().add(resource);
		}
		
		return editingDomain;
	}

	protected void loadResource(Resource resource2, URL resourceUrl) {
		InputStream inputStream = null;
		
		try {
			inputStream = resourceUrl.openStream();
			resource.load(inputStream, Collections.EMPTY_MAP);
		}catch (IOException e) {
			throw new RuntimeException("Exception while loading bpmn model", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// ignore
				}				
			}			
		}
		
	}

	@After
	public void tearDownTest() {
		disposeEditingDomain();
	}

	protected void disposeEditingDomain() {
		if (editingDomain != null) {
			editingDomain.dispose();
			editingDomain = null;
			resource = null;
		}
	}

	public TransactionalEditingDomain getEditingDomain() {
		return editingDomain;
	}

	public Bpmn2ResourceImpl getResource() {
		return resource;
	}

	public TemporaryFolder getTempFolder() {
		return tempFolder;
	}
	
	public ModelHandler getModelHandler() {
		return modelHandler;
	}
}
