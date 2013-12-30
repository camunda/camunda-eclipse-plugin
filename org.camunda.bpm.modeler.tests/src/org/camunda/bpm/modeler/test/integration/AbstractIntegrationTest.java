package org.camunda.bpm.modeler.test.integration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.test.util.TestHelper;
import org.camunda.bpm.modeler.test.util.TestHelper.EditorResources;
import org.camunda.bpm.modeler.test.util.TestHelper.ModelResources;
import org.camunda.bpm.modeler.test.util.TransactionalExecutionException;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.platform.IDiagramContainer;
import org.eclipse.graphiti.ui.internal.services.GraphitiUiInternal;
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
					ModelImport modelImport = new ModelImport(getDiagramContainer(), getBpmnResource(), getDiagramResource());
					modelImport.execute();
				}
			});
		}

		@SuppressWarnings("restriction")
		public String save() throws Exception {
			GraphitiUiInternal.getEmfService().save(model.getEditingDomain());

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
				editor.getDiagramContainer().close();
			}
			
			editor = null;
			model = null;
		}

		public Diagram getDiagram() {
			return editor.getDiagram();
		}

		protected IDiagramContainer getDiagramContainer() {
			return editor.getDiagramContainer();
		}
		
		protected Resource getDiagramResource() {
			return editor.getDiagramResource();
		}
		
		public IDiagramTypeProvider getDiagramTypeProvider() {
			return getDiagramContainer().getDiagramTypeProvider();
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
