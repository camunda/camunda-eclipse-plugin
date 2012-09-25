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

import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.junit.After;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 *
 */
public abstract class AbstractImportBpmnModelTest {

	protected ResourceSetImpl resourceSet;
	protected TransactionalEditingDomain editingDomain;
	protected Resource resource;

	public void createEditingDomain(String bpmnResourceName) {
		
		URL resourceUrl = Activator.getBundleContext().getBundle().getResource(bpmnResourceName);
		URI uri = URI.createFileURI("test.bpmn");

		resource = new Bpmn2ModelerResourceFactoryImpl().createResource(uri);
		loadResource(resource, resourceUrl);

		editingDomain = TransactionUtil.getEditingDomain(resource);
		if (editingDomain == null) {
			// Not yet existing, create one
			editingDomain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();
			editingDomain.getResourceSet().getResources().add(resource);
		}
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
		}
	}
}
