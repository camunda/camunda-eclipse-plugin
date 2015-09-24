package org.camunda.bpm.modeler.test.integration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.test.util.TestHelper;
import org.camunda.bpm.modeler.test.util.TestHelper.EditorResources;
import org.camunda.bpm.modeler.test.util.TestHelper.ModelResources;
import org.camunda.bpm.modeler.test.util.TransactionalExecutionException;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.platform.IDiagramContainer;
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
			// NOTE: copied from Bpmn2PersistencyBehavior#createSaveOptions();
			
			// Save only resources that have actually changed.
			final Map<Object, Object> saveOption = new HashMap<Object, Object>();

			saveOption.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);

			// Use CDATA to escape characters like '<' etc.
			saveOption.put(XMLResource.OPTION_ESCAPE_USING_CDATA, Boolean.TRUE);

			EList<Resource> resources = getDiagramContainer().getDiagramBehavior().getEditingDomain().getResourceSet().getResources();
			final Map<Resource, Map<?, ?>> saveOptions = new HashMap<Resource, Map<?, ?>>();
			for (Resource resource : resources) {
				saveOptions.put(resource, saveOption);
			}
			
			//GraphitiUiInternal.getEmfService().save(model.getEditingDomain(), saveOptions, null);
			save(model.getEditingDomain(), saveOptions, new NullProgressMonitor());
			
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

		// --------------------------------------------------------------------
		// replacement for EmfService.save(...)
		private Set<Resource> save(final TransactionalEditingDomain editingDomain, final Map<Resource, Map<?, ?>> options,
				IProgressMonitor monitor) {

			final Map<URI, Throwable> failedSaves = new HashMap<URI, Throwable>();
			final Set<Resource> savedResources = new HashSet<Resource>();
			final IWorkspaceRunnable wsRunnable = new IWorkspaceRunnable() {
				public void run(final IProgressMonitor monitor) throws CoreException {

					final Runnable runnable = new Runnable() {
						public void run() {
							Transaction parentTx;
							if (editingDomain != null
									&& (parentTx = ((TransactionalEditingDomainImpl) editingDomain).getActiveTransaction()) != null) {
								do {
									if (!parentTx.isReadOnly()) {
										throw new IllegalStateException(
												"saveInWorkspaceRunnable() called from within a command (likely to produce deadlock)"); //$NON-NLS-1$
									}
								} while ((parentTx = ((TransactionalEditingDomainImpl) editingDomain).getActiveTransaction().getParent()) != null);
							}

							final EList<Resource> resources = editingDomain.getResourceSet().getResources();
							// Copy list to an array to prevent ConcurrentModificationExceptions
							// during the saving of the dirty resources
							Resource[] resourcesArray = new Resource[resources.size()];
							resourcesArray = resources.toArray(resourcesArray);
							for (int i = 0; i < resourcesArray.length; i++) {
								// In case resource modification tracking is switched on, 
								// we can check if a resource has been modified, so that we only need to save
								// really changed resources; otherwise we need to save all resources in the set
								final Resource resource = resourcesArray[i];
								/*
								 * Bug 371513 - Added check for isLoaded(): a
								 * resource that has not yet been loaded (possibly
								 * after a reload triggered by a change in another
								 * editor) has no content yet; saving such a
								 * resource will simply erase _all_ content from the
								 * resource on the disk (including the diagram). -->
								 * a not yet loaded resource must not be saved
								 */
								if ((!resource.isTrackingModification() || resource.isModified()) && resource.isLoaded()) {
									try {
										resource.save(options.get(resource));
										savedResources.add(resource);
									} catch (final Throwable t) {
										failedSaves.put(resource.getURI(), t);
									}
								}
							}
						}
					};

					try {
						editingDomain.runExclusive(runnable);
					} catch (final InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			};
			try {
				ResourcesPlugin.getWorkspace().run(wsRunnable, null);
				if (!failedSaves.isEmpty()) {
					throw new WrappedException(createMessage(failedSaves), new RuntimeException());
				}
			} catch (final CoreException e) {
				final Throwable cause = e.getStatus().getException();
				if (cause instanceof RuntimeException) {
					throw (RuntimeException) cause;
				}
				throw new RuntimeException(e);
			}

			return savedResources;
		}

		private String createMessage(Map<URI, Throwable> failedSaves) {
			final StringBuilder buf = new StringBuilder("The following resources could not be saved:"); //$NON-NLS-1$
			for (final Entry<URI, Throwable> entry : failedSaves.entrySet()) {
				buf.append("\nURI: ").append(entry.getKey().toString()).append(", cause: \n").append(getExceptionAsString(entry.getValue())); //$NON-NLS-1$ //$NON-NLS-2$
			}
			return buf.toString();
		}
		
		private String getExceptionAsString(Throwable t) {
			final StringWriter stringWriter = new StringWriter();
			final PrintWriter printWriter = new PrintWriter(stringWriter);
			t.printStackTrace(printWriter);
			final String result = stringWriter.toString();
			try {
				stringWriter.close();
			} catch (final IOException e) {
				// $JL-EXC$ ignore
			}
			printWriter.close();
			return result;
		}

		// end replacement of EmfService.save(...)
		// --------------------------------------------------------------------

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
