package org.eclipse.bpmn2.modeler.core.test.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.impl.DocumentRootImpl;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerResourceFactoryImpl;
import org.eclipse.bpmn2.modeler.core.test.Activator;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.junit.rules.TemporaryFolder;

public class TestHelper {

	public static ModelResources createModel(String bpmnDiagramUrl) {
			
		URL resourceUrl = Activator.getBundleContext().getBundle().getResource(bpmnDiagramUrl);
		if (resourceUrl == null) {
			throw new RuntimeException("Resource " + bpmnDiagramUrl + " not found");
		}

		URI testResourceUri = URI.createFileURI("target/test.bpmn");
		
		Bpmn2Resource resource = createBpmn2Resource(testResourceUri);
		populateResource(resource, resourceUrl);
		
		// TODO: get rid of evil ModelHandler
		createModelHandler(resource, testResourceUri);
		
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
		
		// TODO: Get rid of model handler all together
		// register with model handler
		ModelHandler modelHandler = ModelHandlerLocator.getModelHandler(modelResources.getResource());
		ModelHandlerLocator.put(uri, modelHandler);
		
		Resource resource = modelResources.getResourceSet().createResource(uri);
		resource.getContents().add(diagram);
		
		IDiagramTypeProvider typeProvider = GraphitiUi.getExtensionManager().createDiagramTypeProvider(
				diagram,
				"org.eclipse.bpmn2.modeler.ui.diagram.MainBPMNDiagramType");

		// required to eagerly initialize this one
		typeProvider.getDiagramEditor().getResourceSet();
		
		Bpmn2Resource modelResource = modelResources.getResource();
		
		List<EObject> contents = modelResource.getContents();
		if (!contents.isEmpty()) {
			DocumentRootImpl rootImpl = (DocumentRootImpl) modelResource.getContents().get(0);
			Definitions definitions = rootImpl.getDefinitions();
			if (definitions != null) {
				List<BPMNDiagram> diagrams = definitions.getDiagrams();
				
				if (!diagrams.isEmpty()) {
					BPMNDiagram bpmnDiagram = diagrams.get(0);
					typeProvider.getFeatureProvider().link(diagram, bpmnDiagram);
				}
			}
		}
		
		return new EditorResources(diagram, typeProvider, resource);
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

	private static ModelHandler createModelHandler(Bpmn2Resource resource, URI testResourceUri) {
		return ModelHandlerLocator.createModelHandler(testResourceUri, (Bpmn2ResourceImpl) resource);
	}

	/**
	 * Returns a {@link Bpmn2Resource} associated with the given uri.
	 * 
	 * @param uri
	 * @return
	 */
	private static Bpmn2Resource createBpmn2Resource(URI uri) {
		return (Bpmn2Resource) new Bpmn2ModelerResourceFactoryImpl().createResource(uri);
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
		private final IDiagramTypeProvider typeProvider;
		private final Resource resource;
		
		public EditorResources(Diagram diagram, IDiagramTypeProvider typeProvider, Resource resource) {
			this.diagram = diagram;
			this.typeProvider = typeProvider;
			this.resource = resource;
		}
		
		public Diagram getDiagram() {
			return diagram;
		}
		
		public Resource getResource() {
			return resource;
		}
		
		public IDiagramTypeProvider getTypeProvider() {
			return typeProvider;
		}
	}
}
