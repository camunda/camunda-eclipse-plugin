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
import java.util.List;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.core.Bpmn2TabbedPropertySheetPage;
import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.ProxyURIConverterImplExtension;
import org.camunda.bpm.modeler.core.files.FileService;
import org.camunda.bpm.modeler.core.model.Bpmn2ModelerResourceImpl;
import org.camunda.bpm.modeler.core.preferences.Bpmn2Preferences;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.DiagramEditorAdapter;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.camunda.bpm.modeler.core.utils.ScrollUtil;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.camunda.bpm.modeler.runtime.engine.model.util.ModelResourceFactoryImpl;
import org.camunda.bpm.modeler.ui.views.outline.BaseElementTreeEditPart;
import org.camunda.bpm.modeler.ui.views.outline.Bpmn2EditorOutlinePage;
import org.camunda.bpm.modeler.ui.views.outline.FlowElementTreeEditPart;
import org.camunda.bpm.modeler.ui.wizards.Bpmn2DiagramCreator;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
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
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.ide.ResourceUtil;
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

	private Bpmn2Resource bpmnResource;

	private IResourceChangeListener markerChangeListener;
	private Bpmn2EditingDomainListener editingDomainListener;

	private Bpmn2Preferences preferences;

	protected DiagramEditorAdapter editorAdapter;
	
	private boolean bpmnLoaded = false;

	public Bpmn2Editor() {
		editorAdapter = new DiagramEditorAdapter(this);
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

		addListeners();
	}

	protected void addListeners() {
		addMarkerChangeListener();
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

		try {
			modelUri = FileService.resolveAsWorkspaceResource(modelUri);
		} catch (CoreException e) {
			throw new RuntimeException("Failed to create diagram", e);
		}

		return Bpmn2DiagramCreator.createDiagramInput(modelUri, diagramType, getEditingDomain());
	}

	@Override
	protected void setInput(IEditorInput input) {

		TransactionalEditingDomain editingDomain = getEditingDomain();
		ResourceSet resourceSet = editingDomain.getResourceSet();

		// get (and init) editing domain listener
		// this allows us to hook into the transaction exception handler to get
		// diagnostics about EMF validation errors
		getEditingDomainListener();

		// configure resource set
		if (!resourceSet.eAdapters().contains(editorAdapter)) {
			resourceSet.eAdapters().add(editorAdapter);

			resourceSet.setURIConverter(new ProxyURIConverterImplExtension());

			resourceSet.getResourceFactoryRegistry().getContentTypeToFactoryMap()
					.put(Bpmn2ModelerResourceImpl.BPMN2_CONTENT_TYPE_ID, new ModelResourceFactoryImpl());
		}

		// resolve diagram input

		Bpmn2DiagramEditorInput diagramEditorInput = null;

		if (input instanceof Bpmn2DiagramEditorInput) {
			diagramEditorInput = (Bpmn2DiagramEditorInput) input;
		} else {
			diagramEditorInput = createNewDiagramEditorInput(input, Bpmn2DiagramType.COLLABORATION);
		}

		URI modelUri = diagramEditorInput.getModelUri();
		URI diagramUri = diagramEditorInput.getDiagramUri();

		// open and load BPMN 2.0 file

		bpmnResource = (Bpmn2ResourceImpl) resourceSet.createResource(modelUri, Bpmn2ModelerResourceImpl.BPMN2_CONTENT_TYPE_ID);
		bpmnResource.setTrackingModification(true);

		// handle the fact that we may deal with
		// virtual, i.e. linked resource files here and get
		// the model name from the actual file location
		IFile file = WorkspaceSynchronizer.getFile(bpmnResource);
		String modelName = Bpmn2DiagramCreator.getModelName(URI.createURI(file.getRawLocationURI().toString()));

		// create graphiti diagram file

		Diagram diagram = Graphiti.getPeCreateService().createDiagram("BPMN2", modelName, true);
		diagram.setGridUnit(0);
		diagram.setActive(true);

		FileService.createDiagramResource(diagramUri, diagram, editingDomain);

		bpmnLoaded = true;
		
		try {
			bpmnResource.load(null);
		} catch (FileNotFoundException e) {
			bpmnLoaded = false;
		} catch (IOException e) {
			String message = e.getMessage();

			if (message.matches("Resource '[^']+' does not exist.")) {
				bpmnLoaded = false;
			}
		}

		if (!bpmnLoaded) {
			asyncClose();
		}

		// set input
		super.setInput(diagramEditorInput);

		setActiveEditor(this);
	}

	@Override
	public String getTitleToolTip() {
		IFile modelFile = getModelFile();
		if (modelFile != null) {
			IPath location = null;
			
			if (modelFile.isLinked()) {
				location = modelFile.getRawLocation();
			} else {
				location = modelFile.getFullPath();
			}
			
			return location.toPortableString();
		} else {
			return super.getTitleToolTip();
		}
	}
	
	private void asyncClose() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				close();
			}
		});
	}
	
	public Bpmn2Resource getModelResource() {
		return bpmnResource;
	}

	public boolean isBpmnLoaded() {
		return bpmnLoaded;
	}
	
	@Override
	public void gotoMarker(IMarker marker) {
		final EObject target = getTargetObject(marker);
		if (target == null) {
			return;
		}
		final PictogramElement pe = getDiagramTypeProvider().getFeatureProvider().getPictogramElementForBusinessObject(target);
		if (pe == null) {
			return;
		}

		selectPictogramElements(new PictogramElement[] { pe });
	}

	private EObject getTargetObject(IMarker marker) {
		final String uriString = marker.getAttribute(EValidator.URI_ATTRIBUTE, null);
		final URI uri = uriString == null ? null : URI.createURI(uriString);
		if (uri == null) {
			return null;
		}
		return getEditingDomain().getResourceSet().getEObject(uri, false);
	}

	private void addMarkerChangeListener() {
		markerChangeListener = new Bpmn2MarkerChangeListener(this);
		addResourceListener(markerChangeListener, IResourceChangeEvent.POST_CHANGE);
	}

	protected void addResourceListener(IResourceChangeListener listener, int eventMask) {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener, eventMask);
	}

	private void removeMarkerChangeListener() {
		if (markerChangeListener != null) {
			getModelFile().getWorkspace().removeResourceChangeListener(markerChangeListener);
			markerChangeListener = null;
		}
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

		removeListeners();

		// clear ID mapping tables if no more instances of editor are active
		int instances = 0;
		IEditorSite editorSite = getEditorSite();

		Bpmn2DiagramEditorInput diagramEditorInput = getEditorInput();

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
			Activator.logStatus(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Could not clean up diagram file: " + e.getMessage(), e));
		}

		try {
			if (modelHandler != null) {
				ModelUtil.clearIDs(modelHandler.getResource(), instances == 0);
			}
			getPreferences().getGlobalPreferences().removePropertyChangeListener(this);

			getDiagramBehavior().getEditingDomain().getResourceSet().eAdapters().remove(getEditorAdapter());

			if (instances == 0) {
				setActiveEditor(null);
			}
		} catch (Exception e) {
			Activator.logError(e);
		}

		super.dispose();

		try {
			getPreferences().dispose();
		} catch (Exception e) {
			Activator.logError(e);
		}
	}

	private void removeListeners() {
		removeMarkerChangeListener();
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
		return WorkspaceSynchronizer.getUnderlyingFile(bpmnResource);
	}

	public ModelHandler getModelHandler() {
		return modelHandler;
	}

	public void createPartControl(Composite parent) {
		if (getGraphicalViewer() == null) {
			super.createPartControl(parent);
		}
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

		Bpmn2EditorUpdateBehavior updateBehavior = (Bpmn2EditorUpdateBehavior) getDiagramBehavior().getUpdateBehavior();
		updateBehavior.getWorkspaceSynchronizerDelegate().handleResourceMoved(bpmnResource, newURI);

		doSave(null);
	}

	public void close() {
		bpmnLoaded = false;
		
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage workbenchPage = getSite().getPage();
				
				boolean closed = workbenchPage.closeEditor(Bpmn2Editor.this, false);
				if (!closed) {
					// If close editor fails, try again with explicit editorpart
					// of the old file
					IFile oldFile = getModelFile();
					IEditorPart editorPart = ResourceUtil.findEditor(workbenchPage, oldFile);
					
					closed = workbenchPage.closeEditor(editorPart, false);
				}
			}
		});
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
