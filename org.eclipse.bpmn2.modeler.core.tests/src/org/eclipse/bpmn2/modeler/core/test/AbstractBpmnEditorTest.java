package org.eclipse.bpmn2.modeler.core.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.impl.DocumentRootImpl;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerResourceFactoryImpl;
import org.eclipse.bpmn2.modeler.core.test.util.RunAsEmfCommandRule;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 * 
 * @author Daniel Meyer
 *
 */
public abstract class AbstractBpmnEditorTest {

	protected TransactionalEditingDomain editingDomain;
	protected Bpmn2ResourceImpl resource;
	protected ModelHandler modelHandler;
	
	protected Diagram diagram;
	
	protected IDiagramTypeProvider diagramTypeProvider;
	
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Rule
	public RunAsEmfCommandRule commandRule = new RunAsEmfCommandRule();
	
	public TransactionalEditingDomain createEditingDomain(String bpmnResourceName) {
		
		URL resourceUrl = Activator.getBundleContext().getBundle().getResource(bpmnResourceName);
		if (resourceUrl == null) {
			throw new RuntimeException("Resource " + bpmnResourceName + " not found");
		}
		
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

	protected void loadResource(Resource resource, URL resourceUrl) {
		InputStream inputStream = null;
		
		try {
			inputStream = resourceUrl.openStream();
			resource.load(inputStream, Collections.EMPTY_MAP);
		} catch (Resource.IOWrappedException e) {
			System.out.println("Exception while loading resource: " + e.getCause());
		} catch (IOException e) {
			throw new RuntimeException("Resource could not be loaded", e);
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

	public void disposeEditingDomain() {
		if (editingDomain != null) {
			editingDomain.dispose();
			editingDomain = null;
			resource = null;
			
			diagram = null;
			diagramTypeProvider = null;
		}
	}

	public void setDiagramTypeProvider(IDiagramTypeProvider diagramTypeProvider) {
		this.diagramTypeProvider = diagramTypeProvider;
	}
	
	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
	}
	
	protected Diagram getDiagram() {
		return diagram;
	}

	protected IDiagramTypeProvider getDiagramTypeProvider() {
		return diagramTypeProvider;
	}
	
	public TransactionalEditingDomain getEditingDomain() {
		return editingDomain;
	}

	public Bpmn2ResourceImpl getResource() {
		return resource;
	}
	
	public Definitions getDefinitions() {
		return ((DocumentRootImpl) resource.getContents().get(0)).getDefinitions();
	}

	public TemporaryFolder getTempFolder() {
		return tempFolder;
	}
	
	public ModelHandler getModelHandler() {
		return modelHandler;
	}
}
