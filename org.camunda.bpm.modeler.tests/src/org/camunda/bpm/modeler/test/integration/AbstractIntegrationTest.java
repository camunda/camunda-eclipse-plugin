package org.camunda.bpm.modeler.test.integration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.test.util.TestHelper;
import org.camunda.bpm.modeler.test.util.TestHelper.EditorResources;
import org.camunda.bpm.modeler.test.util.TestHelper.ModelResources;
import org.camunda.bpm.modeler.test.util.TransactionalExecutionException;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.platform.IDiagramEditor;
import org.eclipse.graphiti.ui.internal.services.GraphitiUiInternal;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 * An abstract test base to perform integration tests against the modeler.
 * 
 * @author nico.rehwaldt
 */
public class AbstractIntegrationTest {

	@Rule
	public TemporaryFolder tempDirectory = new TemporaryFolder();
	
	private EditorAndModel editorAndModel;

	protected EditorAndModel open(String fileUri) {

		if (!fileUri.startsWith("/")) {
			fileUri = getClass().getResource(fileUri).getPath();
		}
		
		editorAndModel = new EditorAndModel(tempDirectory.getRoot());
		editorAndModel.open(fileUri);
		
		return editorAndModel;
	}

	protected void execute(Behavior editorBehavior) {
		editorAndModel.execute(editorBehavior);
	}
	
	protected String save() throws Exception {
		return editorAndModel.save();
	}
	
	protected static class EditorAndModel {

		private File tempDirectory;

		private String fileUri;
		
		private ModelResources model;

		protected EditorResources editor;

		public EditorAndModel(File tempFolder) {
			this.tempDirectory = tempFolder;
		}
		
		public void execute(final Behavior behavior) {
			
			execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						behavior.run();
					} catch (Exception e) {
						throw new TransactionalExecutionException(e);
					}
				}
			});
		}
		
		public void execute(Runnable runnable) {
			TestHelper.transactionalExecute(model, runnable);
		}
		
		public void open(String fileUri) {
			
			this.fileUri = fileUri;
			
			if (model != null) {
				close();
			}

			// create model
			model = TestHelper.createModel(fileUri);
			
			// create editor
			execute(new Runnable() {
				public void run() {
					try {
						editor = TestHelper.createEditor(model, tempDirectory);
					} catch (Exception e) {
						throw new TransactionalExecutionException(e);
					}
				}
			});
			
			// import model into editor
			execute(new Runnable() {
				@Override
				public void run() {
					ModelImport modelImport = new ModelImport(getDiagramEditor(), getBpmnResource(), getDiagramResource());
					modelImport.execute();
				}
			});
		}

		@SuppressWarnings("restriction")
		public String save() throws Exception {
			// NOTE: copied from Bpmn2PersistencyBehavior#createSaveOptions();
			
			// Save only resources that have actually changed.
			final Map<Object, Object> saveOption = new HashMap<Object, Object>();

			saveOption.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);

			// Use CDATA to escape characters like '<' etc.
			saveOption.put(XMLResource.OPTION_ESCAPE_USING_CDATA, Boolean.TRUE);

			EList<Resource> resources = getDiagramEditor().getEditingDomain().getResourceSet().getResources();
			final Map<Resource, Map<?, ?>> saveOptions = new HashMap<Resource, Map<?, ?>>();
			for (Resource resource : resources) {
				saveOptions.put(resource, saveOption);
			}
			
			GraphitiUiInternal.getEmfService().save(model.getEditingDomain(), saveOptions);

			String fileName = "target/" + fileUri;
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			FileInputStream inputStream = new FileInputStream(fileName);
			
			try {
				FileUtil.transferStreams(
					inputStream, 
					outputStream, fileName, new NullProgressMonitor());
			} finally {
				inputStream.close();
			}
			
			return outputStream.toString("UTF-8");
		}

		public void close() {
			if (editor != null) {
				final IDiagramEditor diagramEditor = getDiagramEditor();
				
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						page.closeEditor((IEditorPart) diagramEditor, false);
					}
				});
			}
			
			editor = null;
			model = null;
		}

		public Diagram getDiagram() {
			return editor.getDiagram();
		}

		protected IDiagramEditor getDiagramEditor() {
			return editor.getDiagramEditor();
		}
		
		protected Resource getDiagramResource() {
			return editor.getDiagramResource();
		}
		
		public IDiagramTypeProvider getDiagramTypeProvider() {
			return getDiagramEditor().getDiagramTypeProvider();
		}

		public Bpmn2Resource getBpmnResource() {
			return model.getResource();
		}
	}
	
	public abstract class Behavior {
		
		/**
		 * Execute the given behavior
		 * 
		 * @throws Exception
		 */
		public abstract void run() throws Exception;
		
		protected IDiagramTypeProvider getDiagramTypeProvider() {
			return editorAndModel.getDiagramTypeProvider();
		}
		
		protected Bpmn2Resource getResource() {
			return editorAndModel.getBpmnResource();
		}
		
		protected Diagram getDiagram() {
			return editorAndModel.getDiagram();
		}

		protected IFeatureProvider getFeatureProvider() {
			return getDiagramTypeProvider().getFeatureProvider();
		}
	}
}
