package org.camunda.bpm.modeler.test.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.camunda.bpm.modeler.runtime.engine.model.util.ModelResourceFactoryImpl;
import org.camunda.bpm.modeler.test.Activator;
import org.camunda.bpm.modeler.test.core.utils.ExceptionCapturingCommand;
import org.camunda.bpm.modeler.ui.diagram.Bpmn2DiagramTypeProvider;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.impl.DocumentRootImpl;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.platform.IDiagramContainer;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.junit.Ignore;

@Ignore
public class TestHelper {

	public static ModelResources createModel(String bpmnDiagramUrl) {
			
		URL resourceUrl = Activator.getBundleContext().getBundle().getResource(bpmnDiagramUrl);
		if (resourceUrl == null) {
			throw new AssertionFailedError("Resource " + bpmnDiagramUrl + " not found");
		}

		URI testResourceUri = URI.createFileURI("target/" + bpmnDiagramUrl);
		
		Bpmn2Resource resource = createBpmn2Resource(testResourceUri);
		populateResource(resource, resourceUrl);
		
		TransactionalEditingDomain editingDomain = createEditingDomain(resource);
		
		return new ModelResources(bpmnDiagramUrl, resource, editingDomain);
	}
	
	/**
	 * Create the editor resources from a given modelResources
	 * 
	 * @param diagramName
	 * @param tempDir
	 * 
	 * @return
	 * @throws IOException
	 */
	public static EditorResources createEditor(ModelResources modelResources, File tempDirectory) throws IOException {
		
		String bpmnDiagramUrl = modelResources.getUrl();
		
		Diagram diagram = Graphiti.getPeCreateService().createDiagram("BPMN2", bpmnDiagramUrl, true);

		String fileName = bpmnDiagramUrl.replaceAll("/", ".").replaceAll(".bpmn", "");
		URI uri = URI.createFileURI(File.createTempFile(fileName, "diagram", tempDirectory).getAbsolutePath());
		
		Resource diagramResource = modelResources.getResourceSet().createResource(uri);
		diagramResource.getContents().add(diagram);
		
		IDiagramTypeProvider typeProvider = 
				GraphitiUi.getExtensionManager().createDiagramTypeProvider(diagram, Bpmn2DiagramTypeProvider.ID);

		Bpmn2Resource modelResource = modelResources.getResource();
		
		List<EObject> contents = modelResource.getContents();
		if (!contents.isEmpty()) {
			DocumentRootImpl rootImpl = (DocumentRootImpl) modelResource.getContents().get(0);
			Definitions definitions = rootImpl.getDefinitions();
			if (definitions != null) {
				List<BPMNDiagram> diagrams = definitions.getDiagrams();
				
				if (!diagrams.isEmpty()) {
					BPMNDiagram bpmnDiagram = diagrams.get(0);
					
					// linking is required otherwise the diagram may not be found
					// in diagram import
					typeProvider.getFeatureProvider().link(diagram, bpmnDiagram);
				}
			}
		}
		
		IDiagramContainer diagramContainer = typeProvider.getDiagramBehavior().getDiagramContainer();
		
		return new EditorResources(diagram, diagramContainer, diagramResource);
	}
	
	private static TransactionalEditingDomain createEditingDomain(Bpmn2Resource resource) {
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(resource);
		if (editingDomain == null) {
			// Not yet existing, create one
			editingDomain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();
			editingDomain.getResourceSet().getResources().add(resource);
		}
		
		return editingDomain;
	}

	/**
	 * Returns a {@link Bpmn2Resource} associated with the given uri.
	 * 
	 * @param uri
	 * @return
	 */
	private static Bpmn2Resource createBpmn2Resource(URI uri) {
		return (Bpmn2Resource) new ModelResourceFactoryImpl().createResource(uri);
	}

	/**
	 * Load resource with the from the given url
	 * 
	 * @param resource
	 * @param resourceUrl
	 */
	private static void populateResource(Resource resource, URL resourceUrl) {
		InputStream inputStream = null;
		
		try {
			inputStream = resourceUrl.openStream();
			resource.load(inputStream, Collections.EMPTY_MAP);
		} catch (Resource.IOWrappedException e) {
			System.err.println("Exception while loading resource: " + e.getMessage());
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
	
	/**
	 * Saves a {@link Resource} to a string
	 * 
	 * @param resource
	 * @return
	 * @throws IOException
	 */
	public static String saveToString(Resource resource) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		resource.save(outputStream, null);
		return outputStream.toString("UTF-8");
	}
	
	/**
	 * Executes a model operation encapsulated in the given {@link Runnable} in the transactional 
	 * scope of the given {@link TransactionalEditingDomain}. 
	 * 
	 * Throws a {@link TransactionalExecutionException} if the execution threw an exception.
	 * 
	 * @param editingDomain
	 * @param runnable
	 * 
	 * @throws TransactionalExecutionException if the transactional execution resulted in an error
	 */
	public static void transactionalExecute(TransactionalEditingDomain editingDomain, Runnable runnable) throws TransactionalExecutionException {
		
		ExceptionCapturingCommand command = new ExceptionCapturingCommand(editingDomain, runnable);
		editingDomain.getCommandStack().execute(command);

		if (command.failedWithException()) {
			
			RuntimeException capturedException = command.getCapturedException();
			if (capturedException instanceof TransactionalExecutionException) {
				throw capturedException;
			} else {
				throw new TransactionalExecutionException(capturedException);
			}
		}
	}

	/**
	 * Executes a model operation encapsulated in the given {@link Runnable} in the transactional 
	 * scope of the given {@link ModelResources}. 
	 * 
	 * Throws a {@link TransactionalExecutionException} if the execution threw an exception.
	 * 
	 * @param resources
	 * @param runnable
	 * 
	 * @throws TransactionalExecutionException if the transactional execution resulted in an error
	 * 
	 * @see Util#transactionalExecute(TransactionalEditingDomain, Runnable)
	 */
	public static void transactionalExecute(ModelResources resources, Runnable runnable) throws TransactionalExecutionException {
		TransactionalEditingDomain editingDomain = resources.getEditingDomain();
		transactionalExecute(editingDomain, runnable);
	}
	
	public static final class ModelResources {

		private final String url;
		private final Bpmn2Resource resource;
		
		private final TransactionalEditingDomain editingDomain;
		
		public ModelResources(String url, Bpmn2Resource resource, TransactionalEditingDomain editingDomain) {
			this.url = url;
			this.resource = resource;
			
			this.editingDomain = editingDomain;
		}

		public ResourceSet getResourceSet() {
			return getEditingDomain().getResourceSet();
		}

		public String getUrl() {
			return url;
		}
		
		public Bpmn2Resource getResource() {
			return resource;
		}
		
		public TransactionalEditingDomain getEditingDomain() {
			return editingDomain;
		}
	}
	
	public static final class EditorResources {

		private final Diagram diagram;
		private final IDiagramContainer diagramContainer;
		private final Resource resource;
		
		public EditorResources(Diagram diagram, IDiagramContainer diagramContainer, Resource resource) {
			this.diagram = diagram;
			this.diagramContainer = diagramContainer;
			this.resource = resource;
		}
		
		public Diagram getDiagram() {
			return diagram;
		}
		
		public Resource getDiagramResource() {
			return resource;
		}
		
		public IDiagramContainer getDiagramContainer() {
			return diagramContainer;
		}
	}
}
