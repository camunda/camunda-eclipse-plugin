/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *
 * @author Innar Made
 ******************************************************************************/
package org.camunda.bpm.modeler.ui.diagram.editor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.core.Bpmn2TabbedPropertySheetPage;
import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.ProxyURIConverterImplExtension;
import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.importer.ImportException;
import org.camunda.bpm.modeler.core.importer.ModelImportCommand;
import org.camunda.bpm.modeler.core.model.Bpmn2ModelerResourceImpl;
import org.camunda.bpm.modeler.core.preferences.Bpmn2Preferences;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.DiagramEditorAdapter;
import org.camunda.bpm.modeler.core.utils.ErrorUtils;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.camunda.bpm.modeler.core.utils.ScrollUtil;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.camunda.bpm.modeler.core.validation.Bpmn2ProjectValidator;
import org.camunda.bpm.modeler.core.validation.BpmnValidationStatusLoader;
import org.camunda.bpm.modeler.runtime.engine.model.util.ModelResourceFactoryImpl;
import org.camunda.bpm.modeler.ui.dialog.importer.ModelProblemsDialog;
import org.camunda.bpm.modeler.ui.views.outline.BaseElementTreeEditPart;
import org.camunda.bpm.modeler.ui.views.outline.Bpmn2EditorOutlinePage;
import org.camunda.bpm.modeler.ui.views.outline.FlowElementTreeEditPart;
import org.camunda.bpm.modeler.ui.wizards.Bpmn2DiagramCreator;
import org.camunda.bpm.modeler.ui.wizards.FileService;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain.Lifecycle;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.platform.IDiagramContainer;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.graphiti.ui.internal.editor.GFPaletteRoot;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptorProvider;

/**
 * The BPMN 2 diagram editor
 * 
 * @author nico.rehwaldt
 */
public class Bpmn2Editor extends DiagramEditor implements IPropertyChangeListener, IGotoMarker {

	public static final String EDITOR_ID = "org.camunda.bpm.modeler.ui.bpmn2editor";
	public static final String CONTRIBUTOR_ID = "org.camunda.bpm.modeler.ui.PropertyContributor";

	private static Bpmn2Editor activeEditor;
	private static ITabDescriptorProvider tabDescriptorProvider;
	
	private ModelHandler modelHandler;
	private boolean editable = true;

	protected BPMNDiagram bpmnDiagram;
	
	protected Bpmn2ResourceImpl bpmnResource;
	private Resource diagramResource;

	private IPartListener2 selectionListener;
	private IResourceChangeListener markerChangeListener;

	private Bpmn2EditingDomainListener editingDomainListener;
	private Bpmn2Preferences preferences;

	protected DiagramEditorAdapter editorAdapter;

	public Bpmn2Editor() {
	}

	public static Bpmn2Editor getActiveEditor() {
		return activeEditor;
	}

	private void setActiveEditor(Bpmn2Editor editor) {
		activeEditor = editor;
		if (activeEditor != null) {
			Bpmn2Preferences.setActiveProject(activeEditor.getProject());
		}
	}

	public Resource getDiagramResource() {
		return getDiagramTypeProvider().getDiagram().eResource();
	}

	protected DiagramEditorAdapter getEditorAdapter() {
		return editorAdapter;
	}

	@Override
	protected DiagramEditorInput convertToDiagramEditorInput(IEditorInput input) throws PartInitException {

		if (input instanceof Bpmn2DiagramEditorInput) {
			return (Bpmn2DiagramEditorInput) input;
		} else {
			return createNewDiagramEditorInput(input, Bpmn2DiagramType.COLLABORATION);
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		setActiveEditor(this);

		addSelectionListener();
		addMarkerChangeListener();
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isEditable() {
		return editable;
	}

	@Override
	public boolean isDirty() {
		if (!editable)
			return false;
		return super.isDirty();
	}

	@Override
	protected DiagramBehavior createDiagramBehavior() {
		return new Bpmn2DiagramBehavior(this);
	}

	public Bpmn2Preferences getPreferences() {
		if (preferences == null) {
			loadPreferences(getProject());
		}
		return preferences;
	}

	private void loadPreferences(IProject project) {
		preferences = Bpmn2Preferences.getInstance(project);
		preferences.load();
		preferences.getGlobalPreferences().addPropertyChangeListener(this);
	}

	/**
	 * ID for tabbed property sheets.
	 * 
	 * @return the contributor id
	 */
	@Override
	public String getContributorId() {
		return CONTRIBUTOR_ID;
	}

	/**
	 * Beware, creates a new input and changes this editor
	 */
	private Bpmn2DiagramEditorInput createNewDiagramEditorInput(IEditorInput input, Bpmn2DiagramType diagramType) {
		URI modelUri = FileService.getInputUri(input);
		
		return Bpmn2DiagramCreator.createDiagramInput(modelUri, diagramType, getEditingDomain());
	}

	@Override
	protected void setInput(IEditorInput input) {

		TransactionalEditingDomain editingDomain = getEditingDomain();
		CommandStack commandStack = editingDomain.getCommandStack();
		
		ResourceSet resourceSet = editingDomain.getResourceSet();


		// get (and init) editing domain listener
		// this allows us to hook into the transaction exception handler to get 
		// diagnostics about EMF validation errors
		getEditingDomainListener();

		
		// configure resource set
		
		resourceSet.setURIConverter(new ProxyURIConverterImplExtension());
		resourceSet.eAdapters().add(editorAdapter = new DiagramEditorAdapter(this));

		resourceSet.getResourceFactoryRegistry().getContentTypeToFactoryMap()
			.put(Bpmn2ModelerResourceImpl.BPMN2_CONTENT_TYPE_ID, new ModelResourceFactoryImpl());
		
		
		// resolve diagram input
		
		Bpmn2DiagramEditorInput diagramEditorInput = null;
		
		if (input instanceof Bpmn2DiagramEditorInput) {
			diagramEditorInput = (Bpmn2DiagramEditorInput) input;
		} else {
			diagramEditorInput = createNewDiagramEditorInput(input, Bpmn2DiagramType.COLLABORATION);
		}
		
		URI modelUri = diagramEditorInput.getModelUri();
		URI diagramUri = diagramEditorInput.getDiagramUri();


		// create graphiti diagram file
		
		Diagram diagram = Graphiti.getPeCreateService().createDiagram("BPMN2", Bpmn2DiagramCreator.getModelName(modelUri), true);
		diagram.setGridUnit(0);

		diagramResource = FileService.createDiagramResource(diagramUri, diagram, editingDomain);
		
		
		// open and load BPMN 2.0 file
		
		bpmnResource = (Bpmn2ResourceImpl) resourceSet.createResource(modelUri, Bpmn2ModelerResourceImpl.BPMN2_CONTENT_TYPE_ID);

		boolean exists = true;
		
		try {
			bpmnResource.load(null);
		} catch (FileNotFoundException e) {
			exists = false;
		} catch (IOException e) {
			String message = e.getMessage();
			
			if (message.matches("Resource '[^']+' does not exist.")) {
				exists = false;
			} else {
				// ok, handled in import error dialog
			}
		}
		
		// set input
		super.setInput(diagramEditorInput);

		setActiveEditor(this);

		if (!exists) {
			// this is the usual case when files are moved around in the file
			// system and Eclipse cannot find the files any more when being opened again
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					closeEditor();
				}
			});
			
			return;
		}
		
		commandStack.execute(new RecordingCommand(getEditingDomain()) {

			@Override
			protected void doExecute() {
				importDiagram(bpmnResource);
			}
		});

		commandStack.flush();

		loadMarkers();
	}

	private void importDiagram(Bpmn2Resource resourceToImport) {

		// make sure this guy is active, otherwise it's not selectable
		Diagram diagram = getDiagramTypeProvider().getDiagram();
		diagram.setActive(true);
		
		IDiagramContainer diagramContainer = getDiagramBehavior().getDiagramContainer();
		TransactionalEditingDomain editingDomain = getDiagramBehavior().getEditingDomain();
		
		ModelImportCommand command = new ModelImportCommand(editingDomain, diagramContainer, resourceToImport);
		
		try {
			editingDomain.getCommandStack().execute(command);

			if (!command.wasSuccessful() || !command.getRecordedWarnings().isEmpty()) {
				handleImportErrorAndWarnings(command.getRecordedException(), command.getRecordedWarnings());
			}
		} catch (Exception e) {
			// if we got here, there was an exception in the import on emf model level
			// we dont want to die now because this will produce NPEs in the
			// creation of the editor, so we swallow this exception -> much better
			// user experience
			Activator.logError(e);
		}

		// this needs to happen AFTER the diagram has been imported because we need
		// to be able to determine the diagram type from the file's contents in
		// order
		// to build the right tool palette for the target runtime and model
		// enablements.
		GFPaletteRoot pr = (GFPaletteRoot) getPaletteRoot();
		pr.updatePaletteEntries();
	}

	protected void handleImportErrorAndWarnings(ImportException exception, List<ImportException> warnings) {
		ModelProblemsDialog dialog = new ModelProblemsDialog(getSite().getShell());

		dialog.setException(exception);
		dialog.setWarnings(warnings);

		dialog.open();

		if (exception != null) {
			throw exception;
		}
	}

    @Override
    public void gotoMarker(IMarker marker) {
        final EObject target = getTargetObject(marker);
        if (target == null) {
            return;
        }
        final PictogramElement pe = getDiagramTypeProvider().getFeatureProvider().getPictogramElementForBusinessObject(
                target);
        if (pe == null) {
            return;
        }
        selectPictogramElements(new PictogramElement[] {pe });
    }

	private void loadMarkers() {
		if (getModelFile() != null) {
			// read in the markers
			BpmnValidationStatusLoader vsl = new BpmnValidationStatusLoader(this);

			try {
				vsl.load(Arrays.asList(getModelFile().findMarkers(Bpmn2ProjectValidator.BPMN2_MARKER_ID, true,
						IResource.DEPTH_ZERO)));
			} catch (CoreException e) {
				Activator.logStatus(e.getStatus());
			}
		}
	}

	private EObject getTargetObject(IMarker marker) {
		final String uriString = marker.getAttribute(EValidator.URI_ATTRIBUTE, null);
		final URI uri = uriString == null ? null : URI.createURI(uriString);
		if (uri == null) {
			return null;
		}
		return getEditingDomain().getResourceSet().getEObject(uri, false);
	}
	
	private void addSelectionListener() {
		if (selectionListener == null) {

		}

		// no selection listener registered
	}

	private void removeSelectionListener() {
		if (selectionListener != null) {
			getSite().getPage().removePartListener(selectionListener);
			selectionListener = null;
		}
	}

	private void addMarkerChangeListener() {
		if (getModelFile() != null) {
			if (markerChangeListener == null) {
				markerChangeListener = new Bpmn2MarkerChangeListener(this);
				getModelFile().getWorkspace().addResourceChangeListener(markerChangeListener, IResourceChangeEvent.POST_BUILD);
			}
		}
	}

	private void removeMarkerChangeListener() {
		if (markerChangeListener != null) {
			getModelFile().getWorkspace().removeResourceChangeListener(markerChangeListener);
			markerChangeListener = null;
		}
	}

	public void refreshTitle() {
		String name = getEditorInput().getName();
		setPartName(URI.decode(name));
	}

	public Bpmn2EditingDomainListener getEditingDomainListener() {
		if (editingDomainListener == null) {
			TransactionalEditingDomainImpl editingDomain = (TransactionalEditingDomainImpl) getEditingDomain();
			if (editingDomain == null) {
				return null;
			}
			editingDomainListener = new Bpmn2EditingDomainListener(this);

			Lifecycle domainLifeCycle = (Lifecycle) editingDomain.getAdapter(Lifecycle.class);
			domainLifeCycle.addTransactionalEditingDomainListener(editingDomainListener);
		}
		
		return editingDomainListener;
	}

	public BasicDiagnostic getDiagnostics() {
		return getEditingDomainListener().getDiagnostics();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class required) {
		if (required == ITabDescriptorProvider.class) {
			if (tabDescriptorProvider == null) {
				IWorkbenchPage page = getEditorSite().getPage();
				String viewID = "org.eclipse.ui.views.PropertySheet";
				try {
					page.showView(viewID, null, IWorkbenchPage.VIEW_CREATE);
					page.showView(viewID, null, IWorkbenchPage.VIEW_ACTIVATE);
				} catch (Exception e) {
				}
			}
			return tabDescriptorProvider;
		}
		if (required == Bpmn2Preferences.class)
			return getPreferences();
		if (required == IPropertySheetPage.class) {
			return new Bpmn2TabbedPropertySheetPage(this);
		}
		if (required == SelectionSynchronizer.class) {
			return getSelectionSynchronizer();
		}
		if (required == IContentOutlinePage.class) {
			if (getDiagramTypeProvider() != null) {
				Bpmn2EditorOutlinePage outlinePage = new Bpmn2EditorOutlinePage(this);
				return outlinePage;
			}
		}

		return super.getAdapter(required);
	}

	@Override
	public void dispose() {
		// clear ID mapping tables if no more instances of editor are active
		int instances = 0;
		IEditorSite editorSite = getEditorSite();

		Bpmn2DiagramEditorInput diagramEditorInput = (Bpmn2DiagramEditorInput) getEditorInput();

		// need to check != null to deal with errors
		if (editorSite != null) {
			IWorkbenchPage[] pages = editorSite.getWorkbenchWindow().getPages();
			for (IWorkbenchPage p : pages) {
				IEditorReference[] refs = p.getEditorReferences();
				instances += refs.length;
			}
		}

		try {
			cleanupDiagramFile(diagramEditorInput);
		} catch (Exception e) {
			Activator.logStatus(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Could not clean up diagram file: "
					+ e.getMessage(), e));
		}

		try {
			if (modelHandler != null) {
				ModelUtil.clearIDs(modelHandler.getResource(), instances == 0);
			}
			getPreferences().getGlobalPreferences().removePropertyChangeListener(this);
			
			getDiagramBehavior().getEditingDomain().getResourceSet().eAdapters().remove(getEditorAdapter());
			
			removeSelectionListener();
			if (instances == 0) {
				setActiveEditor(null);
			}
		} catch (Exception e) {
			Activator.logError(e);
		}

		super.dispose();

		try {
			removeMarkerChangeListener();
			getPreferences().dispose();
		} catch (Exception e) {
			Activator.logError(e);
		}
	}

	private void cleanupDiagramFile(Bpmn2DiagramEditorInput editorInput) {

		// not null check --> dealing with previous open diagram failures
		if (editorInput == null) {
			return;
		}

		URI diagramUri = editorInput.getDiagramUri();
		if (diagramUri == null) {
			return;
		}

		File diagramFile = new File(diagramUri.toFileString());
		if (diagramFile.exists()) {
			diagramFile.delete();
		}
	}

	public IPath getModelPath() {
		IResource modelFile = getModelFile();
		
		if (modelFile != null) {
			return modelFile.getFullPath();
		}

		return null;
	}

	public IProject getProject() {
		IResource modelFile = getModelFile();
		
		if (modelFile != null) {
			return modelFile.getProject();
		} else {
			return null;
		}
	}

	public IFile getModelFile() {
		String platformString = bpmnResource.getURI().toPlatformString(true);
		
		if (platformString == null) {
			return null;
		}
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		if (workspace != null) {
			return (IFile) workspace.getRoot().findMember(platformString);
		}
		
		return null;
	}

	public ModelHandler getModelHandler() {
		return modelHandler;
	}

	public void createPartControl(Composite parent) {
		if (getGraphicalViewer() == null) {
			super.createPartControl(parent);
		}
	}

	public BPMNDiagram getBpmnDiagram() {
		if (bpmnDiagram == null)
			bpmnDiagram = getModelHandler().getDefinitions().getDiagrams().get(0);

		// if (bpmnDiagram!=null) {
		// GraphicalViewer viewer = getGraphicalViewer();
		// mapDiagramToViewer.put(bpmnDiagram, viewer);
		// }
		return bpmnDiagram;
	}

	public void setBpmnDiagram(final BPMNDiagram bpmnDiagram) {
		// create a new Graphiti Diagram if needed
		Diagram diagram = DIUtils.getOrCreateDiagram(this, bpmnDiagram);

		// Tell the DTP about the new Diagram
		getDiagramTypeProvider().resourceReloaded(diagram);
		getDiagramBehavior().getRefreshBehavior().initRefresh();
		setPictogramElementsForSelection(null);
		// set Diagram as contents for the graphical viewer and refresh
		getGraphicalViewer().setContents(diagram);
		
		getDiagramBehavior().refreshContent();
		// remember this for later
		this.bpmnDiagram = bpmnDiagram;
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);

//		Resource resource = getDiagramBehavior().getEditingDomain().getResourceSet().getResource( ((Bpmn2DiagramEditorInput) getEditorInput()).getModelUri(), false);
//		BPMN2ProjectValidator.validateOnSave(resource, monitor);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return getModelFile() != null;
	}

	@Override
	public void doSaveAs() {
		IFile oldFile = getModelFile();
		
		SaveAsDialog saveAsDialog = new SaveAsDialog(getSite().getShell());
		saveAsDialog.setOriginalFile(oldFile);
		saveAsDialog.create();
		
		if (saveAsDialog.open() == SaveAsDialog.CANCEL) {
			return;
		}
		
		IPath newFilePath = saveAsDialog.getResult();
		if (newFilePath == null) {
			return;
		}
		
		URI newURI = URI.createPlatformResourceURI(newFilePath.toString(), true);
		handleResourceMoved(bpmnResource, newURI);
		
		doSave(null);
	}

	public void closeEditor() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				boolean closed = getSite().getPage().closeEditor(Bpmn2Editor.this, false);
				if (!closed) {
					// If close editor fails, try again with explicit editorpart
					// of the old file
					IFile oldFile = ResourcesPlugin.getWorkspace().getRoot().getFile(getModelPath());
					IEditorPart editorPart = ResourceUtil.findEditor(getSite().getPage(), oldFile);
					closed = getSite().getPage().closeEditor(editorPart, false);
				}
			}
		});
	}

	// Show error dialog and log the error
	private void showErrorDialogWithLogging(Exception e) {
		Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
		ErrorUtils.showErrorWithLogging(status);
	}

	// //////////////////////////////////////////////////////////////////////////////
	// WorkspaceSynchronizer handlers called from delegate
	// //////////////////////////////////////////////////////////////////////////////

	public boolean handleResourceChanged(final Resource resource) {
		// FIXME
		// try {
		// getEditingDomain().runExclusive(new Runnable() {
		//
		// @Override
		// public void run() {
		// importDiagram((Bpmn2Resource) resource);
		// }
		// });
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// TransactionalCommandStack stack = (TransactionalCommandStack)
		// getEditingDomain().getCommandStack();
		//
		// Map<String, Object> options = new HashMap<String, Object>();
		// options.put(Transaction.OPTION_IS_UNDO_REDO_TRANSACTION, true);
		//
		// try {
		// stack.execute(new RecordingCommand(getEditingDomain()) {
		//
		// @Override
		// protected void doExecute() {
		// importDiagram(bpmnResource);
		// }
		// }, options);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (RollbackException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		return true;
	}

	public boolean handleResourceDeleted(Resource resource) {
		closeEditor();
		return true;
	}

	public boolean handleResourceMoved(Resource resource, final URI newURI) {
		URI oldURI = resource.getURI();
		
		resource.setURI(newURI);

		if (resource.equals(bpmnResource)) {
			
			if (preferences != null) {
				preferences.getGlobalPreferences().removePropertyChangeListener(this);
				preferences.dispose();
				preferences = null;
			}
			
			getEditorInput().updateModelUri(newURI);
		} else {
			getEditorInput().updateUri(newURI);
		}
		
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				refreshTitle();
				refreshTitleToolTip();
			}
		});
		
		return true;
	}

	@Override
	public Bpmn2DiagramEditorInput getEditorInput() {
		return (Bpmn2DiagramEditorInput) super.getEditorInput();
	}
	
	// //////////////////////////////////////////////////////////////////////////////
	// Other handlers
	// //////////////////////////////////////////////////////////////////////////////

	// FIXME sometime the last element will be selected randomly, could be related
	// to this function
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		// make sure we are not able to select the scroll filter

		final ScrollFilteredSelection scrollFiltered = filterScrollElement(selection);
		if (scrollFiltered != null) {
			fixScrollFilteredSelection(selection, scrollFiltered);
			return;
		}

		super.selectionChanged(part, selection);

		updateActions(getSelectionActions()); // usually done in GEF

		EditPart editPart = BusinessObjectUtil.getEditPartForSelection(selection);
		Object pictogramElement = BusinessObjectUtil.getPictogramElementForSelection(selection);

		if (pictogramElement instanceof PictogramElement
				&& (editPart instanceof FlowElementTreeEditPart || editPart instanceof BaseElementTreeEditPart)) {
			selectPictogramElements(new PictogramElement[] { (PictogramElement) pictogramElement });
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse
	 * .jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {

		if (event.getProperty().endsWith(Bpmn2Preferences.PREF_SHAPE_STYLE)) {
			getEditingDomain().getCommandStack().execute(new RecordingCommand(getEditingDomain()) {
				@Override
				protected void doExecute() {
					IPeService peService = Graphiti.getPeService();
					TreeIterator<EObject> iter = getDiagramTypeProvider().getDiagram().eAllContents();
					while (iter.hasNext()) {
						EObject o = iter.next();
						if (o instanceof PictogramElement) {
							PictogramElement pe = (PictogramElement) o;
							BaseElement be = BusinessObjectUtil.getFirstElementOfType(pe, BaseElement.class);
							if (be != null) {
								TreeIterator<EObject> childIter = pe.eAllContents();
								while (childIter.hasNext()) {
									o = childIter.next();
									if (o instanceof GraphicsAlgorithm) {
										GraphicsAlgorithm ga = (GraphicsAlgorithm) o;
										if (peService.getPropertyValue(ga, Bpmn2Preferences.PREF_SHAPE_STYLE) != null) {
											StyleUtil.applyStyle(ga, be);
										}
									}
								}
							}
						}
					}
				}
			});
		}
	}

	// // Scroll fix related stuff ////////////////////////////////////////////

	private void fixScrollFilteredSelection(ISelection selection, ScrollFilteredSelection scrollFiltered) {

		final GraphicalViewer graphicalViewer = getGraphicalViewer();
		IStructuredSelection newSelection = scrollFiltered.getSelection();
		EditPart scrollEditPart = scrollFiltered.getScrollEditPart();

		boolean async = false;

		if (selection instanceof IStructuredSelection) {
			// if first selected element equals scroll edit part update
			// fire the updated selection asynchronously to allow the
			// property panel to pick the change up
			async = ((IStructuredSelection) selection).getFirstElement().equals(scrollEditPart);
		}

		graphicalViewer.deselect(scrollEditPart);

		if (newSelection.isEmpty()) {
			Object diagramEditPart = graphicalViewer.getEditPartRegistry().get(getDiagramTypeProvider().getDiagram());

			List<?> selectedEditParts = graphicalViewer.getSelectedEditParts();
			if (selectedEditParts.contains(diagramEditPart)) {
				return;
			}

			newSelection = new StructuredSelection(diagramEditPart);
		}

		final ISelection refreshSelection = newSelection;

		if (async) {
			// set the new selection
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					graphicalViewer.setSelection(refreshSelection);
				}
			});
		} else {
			graphicalViewer.setSelection(refreshSelection);
		}
	}

	/**
	 * Returns the list of filtered elements if the scroll shape was contained in
	 * the selection. Returns null otherwise (no filtering required).
	 * 
	 * @param selection
	 * @return
	 */
	private ScrollFilteredSelection filterScrollElement(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;

			boolean foundScrollShape = false;

			ScrollFilteredSelection filteredSelection = new ScrollFilteredSelection();

			for (Object o : structuredSelection.toArray()) {
				if (o instanceof EditPart) {
					EditPart editPart = (EditPart) o;
					PictogramElement element = BusinessObjectUtil.getPictogramElementForEditPart(editPart);
					if (element != null) {
						if (ScrollUtil.isScrollShape(element)) {
							foundScrollShape = true;

							filteredSelection.setScrollElement(editPart, element);

							// do not add scroll shape
							continue;
						}

						filteredSelection.add(editPart, element);
					}
				}
			}

			if (foundScrollShape) {
				return filteredSelection;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Helper for filtering the scroll shape from an editor selection.
	 * 
	 * @author nico.rehwaldt
	 */
	private static class ScrollFilteredSelection {

		private ArrayList<EditPart> editParts;
		private ArrayList<PictogramElement> pictogramElements;

		private EditPart scrollEditPart;

		public ScrollFilteredSelection() {

			this.editParts = new ArrayList<EditPart>();
			this.pictogramElements = new ArrayList<PictogramElement>();
		}

		public void add(EditPart editPart, PictogramElement pictogramElement) {
			this.editParts.add(editPart);
			this.pictogramElements.add(pictogramElement);
		}

		public void setScrollElement(EditPart editPart, PictogramElement pictogramElement) {
			this.scrollEditPart = editPart;
		}

		public EditPart getScrollEditPart() {
			return scrollEditPart;
		}

		public IStructuredSelection getSelection() {
			return new StructuredSelection(editParts.toArray());
		}
	}
}
