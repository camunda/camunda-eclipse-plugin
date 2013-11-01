package org.camunda.bpm.modeler.ui.views.outline;
/*******************************************************************************
 * <copyright>
 *
 * Copyright (c) 2005, 2010 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    SAP AG - initial API, implementation and documentation
 *
 * </copyright>
 *
 *******************************************************************************/

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.core.IConstants;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.internal.fixed.FixedScrollableThumbnail;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.PageBook;

/**
 * An outline page for the graphical modeling editor. It displays the contents
 * of the editor either as a hierarchical Outline or as a graphical Thumbnail.
 * There are buttons to switch between those displays. Subclasses should
 * overwrite this outline page (and dependent classes), to change the
 * default-behaviour.
 */
// The generic outline uses internal functionality of Graphiti. For concrete
// tool implementations this should not be necessary
@SuppressWarnings("restriction")
public class BpmnEditorOutlinePage extends ContentOutlinePage implements IPropertyListener {

	// The IDs to identify the outline and the thunbnail
	public static final int ID_BUSINESS_MODEL_OUTLINE = 0;
	public static final int ID_INTERCHANGE_MODEL_OUTLINE = 1;
	public static final int ID_THUMBNAIL = 2;

	// Common instances of different Editors/Views, to synchronize their
	// behaviour
	private GraphicalViewer graphicalViewer;

	private ActionRegistry actionRegistry;

	private EditDomain editDomain;

	private KeyHandler keyHandler;


	private SelectionSynchronizer selectionSynchronizer;

	private DiagramEditor diagramEditor;
	
	// The thumbnail to display
	private FixedScrollableThumbnail thumbnail;

	// Actions (buttons) to switch between outline and overview
	private IAction showBusinessModelOutlineAction;
	private IAction showInterchangeModelOutlineAction;
	
	private IAction showOverviewAction;

	// The pagebook, which displays either the outline or the overview
	private PageBook pageBook;

	// The outline-controls and the thumbnail-control of the pagebook
	private Tree businessModelOutline;
	private Tree interchangeModelOutline;

	// and their corresponding editpart factories
	private EditPartFactory businessModelEditPartFactory;
	private EditPartFactory interchangeModelEditPartFactory;

	private Canvas overview;

	/**
	 * Creates a new BPMN2EditorOutlinePage. It is important, that this
	 * outline page uses the same handlers (ActionRegistry, KeyHandler,
	 * ZoomManagerAdapter, ...) as the main editor, so that the behaviour is
	 * synchronized between them.
	 * 
	 * @param diagramEditor
	 *            the attached diagram editor
	 * @since 0.9
	 */
	public BpmnEditorOutlinePage(DiagramEditor diagramEditor) {
		super(new TreeViewer());
		graphicalViewer = diagramEditor.getGraphicalViewer();
		actionRegistry = (ActionRegistry) diagramEditor.getAdapter(ActionRegistry.class);
		editDomain = diagramEditor.getEditDomain();
		keyHandler = (KeyHandler) diagramEditor.getAdapter(KeyHandler.class);
		selectionSynchronizer = (SelectionSynchronizer) diagramEditor.getAdapter(SelectionSynchronizer.class);
		this.diagramEditor = diagramEditor;
	}

	// ========================= standard behavior ===========================

	/**
	 * Is used to register several global action handlers (UNDO, REDO, COPY,
	 * PASTE, ...) on initialization of this outline page. This activates for
	 * example the undo-action in the central Eclipse-Menu.
	 * 
	 * @param pageSite
	 *            the page site
	 * 
	 * @see org.eclipse.ui.part.Page#init(IPageSite)
	 */
	@Override
	public void init(IPageSite pageSite) {
		super.init(pageSite);
		IActionBars actionBars = pageSite.getActionBars();
		registerGlobalActionHandler(actionBars, ActionFactory.UNDO.getId());
		registerGlobalActionHandler(actionBars, ActionFactory.REDO.getId());
		registerGlobalActionHandler(actionBars, ActionFactory.COPY.getId());
		registerGlobalActionHandler(actionBars, ActionFactory.PASTE.getId());
		registerGlobalActionHandler(actionBars, ActionFactory.PRINT.getId());
		registerGlobalActionHandler(actionBars, ActionFactory.SAVE_AS.getId());
		actionBars.updateActionBars();
	}
	
	/**
	 * Creates the Control of this outline page. By default this is a PageBook,
	 * which can toggle between a hierarchical Outline and a graphical
	 * Thumbnail.
	 * 
	 * @param parent
	 *            the parent
	 * 
	 * @see org.eclipse.gef.ui.parts.ContentOutlinePage#createControl(Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		pageBook = new PageBook(parent, SWT.NONE);
		businessModelOutline = (Tree)getViewer().createControl(pageBook);
		interchangeModelOutline = (Tree)getViewer().createControl(pageBook);
		overview = new Canvas(pageBook, SWT.NONE);
		createOutlineViewer();

		// register listeners
		selectionSynchronizer.addViewer(getViewer());
		diagramEditor.addPropertyListener(this);
	}

	/**
	 * Deregisters all 'listeners' of the main-editor.
	 */
	@Override
	public void dispose() {
		// deregister listeners
		selectionSynchronizer.removeViewer(getViewer());
		diagramEditor.removePropertyListener(this);

		if (thumbnail != null)
			thumbnail.deactivate();

		super.dispose();
	}

	/**
	 * Returns the Control of this outline page, which was created in
	 * createControl().
	 * 
	 * @return the control
	 * 
	 * @see org.eclipse.gef.ui.parts.ContentOutlinePage#getControl()
	 */
	@Override
	public Control getControl() {
		return pageBook;
	}

	/**
	 * Refreshes the outline on any change of the diagram editor. Most
	 * importantly, there is a property change event editor-dirty.
	 */
	public void propertyChanged(Object source, int propId) {
		refresh();
	}

	/**
	 * Toggles the page to display between hierarchical Outline and graphical
	 * Thumbnail.
	 * 
	 * @param id
	 *            The ID of the page to display. It must be either ID_BUSINESS_MODEL_OUTLINE or
	 *            ID_THUMBNAIL.
	 */
	protected void showPage(int id) {
		if (id == ID_BUSINESS_MODEL_OUTLINE) {
			if (businessModelEditPartFactory==null)
				businessModelEditPartFactory = new Bpmn2DiagramTreeEditPartFactory(ID_BUSINESS_MODEL_OUTLINE);
			getViewer().setEditPartFactory(businessModelEditPartFactory);
			getViewer().setControl(businessModelOutline);
			Diagram diagram = diagramEditor.getDiagramTypeProvider().getDiagram();
			getViewer().setContents(diagram);
			
			showBusinessModelOutlineAction.setChecked(true);
			showInterchangeModelOutlineAction.setChecked(false);
			showOverviewAction.setChecked(false);
			pageBook.showPage(businessModelOutline);
		} else if (id == ID_INTERCHANGE_MODEL_OUTLINE) {
			if (interchangeModelEditPartFactory==null)
				interchangeModelEditPartFactory = new Bpmn2DiagramTreeEditPartFactory(ID_INTERCHANGE_MODEL_OUTLINE);
			getViewer().setEditPartFactory(interchangeModelEditPartFactory);
			getViewer().setControl(interchangeModelOutline);
			Diagram diagram = diagramEditor.getDiagramTypeProvider().getDiagram();
			getViewer().setContents(diagram);
			
			showBusinessModelOutlineAction.setChecked(false);
			showInterchangeModelOutlineAction.setChecked(true);
			showOverviewAction.setChecked(false);
			pageBook.showPage(interchangeModelOutline);
		} else if (id == ID_THUMBNAIL) {
			if (thumbnail == null)
				createThumbnailViewer();
			showBusinessModelOutlineAction.setChecked(false);
			showInterchangeModelOutlineAction.setChecked(false);
			showOverviewAction.setChecked(true);
			pageBook.showPage(overview);
		}
	}

	/**
	 * Creates the hierarchical Outline viewer.
	 */
	protected void createOutlineViewer() {
		// set the standard handlers
		getViewer().setEditDomain(editDomain);
		getViewer().setKeyHandler(keyHandler);

		// add a context-menu
		ContextMenuProvider contextMenuProvider = createContextMenuProvider();
		if (contextMenuProvider != null)
			getViewer().setContextMenu(contextMenuProvider);

		// add buttons outline/overview to toolbar
		IToolBarManager tbm = getSite().getActionBars().getToolBarManager();
		showBusinessModelOutlineAction = new Action() {

			@Override
			public void run() {
				showPage(ID_BUSINESS_MODEL_OUTLINE);
			}
		};
		
		showBusinessModelOutlineAction.setImageDescriptor(Activator.getDefault().getImageDescriptor(IConstants.ICON_BUSINESS_MODEL));
		showBusinessModelOutlineAction.setToolTipText("Business Model");
		tbm.add(showBusinessModelOutlineAction);

		showInterchangeModelOutlineAction = new Action() {

			@Override
			public void run() {
				Diagram diagram = diagramEditor.getDiagramTypeProvider().getDiagram();
				getViewer().setContents(diagram);
				getViewer().setControl(interchangeModelOutline);
				showPage(ID_INTERCHANGE_MODEL_OUTLINE);
			}
		};
		showInterchangeModelOutlineAction.setImageDescriptor(Activator.getDefault().getImageDescriptor(IConstants.ICON_INTERCHANGE_MODEL));
		showInterchangeModelOutlineAction.setToolTipText("Diagram Interchange Model");
		tbm.add(showInterchangeModelOutlineAction);
		
		
		showOverviewAction = new Action() {

			@Override
			public void run() {
				showPage(ID_THUMBNAIL);
			}
		};
		showOverviewAction.setImageDescriptor(Activator.getDefault().getImageDescriptor(IConstants.ICON_THUMBNAIL));
		showOverviewAction.setToolTipText("Thumbnail");
		
		tbm.add(showOverviewAction);

		// by default show the outline-page
		showPage(ID_BUSINESS_MODEL_OUTLINE);
	}

	/**
	 * Returns a new ContextMenuProvider. Can be null, if no context-menu shall
	 * be displayed.
	 * 
	 * @return A new ContextMenuProvider.
	 */
	protected ContextMenuProvider createContextMenuProvider() {
		return null;
	}

	/**
	 * Creates the graphical Thumbnail viewer.
	 */
	protected void createThumbnailViewer() {
		LightweightSystem lws = new LightweightSystem(overview);
		ScalableFreeformRootEditPart rootEditPart = (ScalableFreeformRootEditPart) graphicalViewer.getRootEditPart();
		thumbnail = new FixedScrollableThumbnail((Viewport) rootEditPart.getFigure());
		thumbnail.setBorder(new MarginBorder(3));
		thumbnail.setSource(rootEditPart.getLayer(LayerConstants.PRINTABLE_LAYERS));
		lws.setContents(thumbnail);
	}

	// ========================= private helper methods =======================

	private void registerGlobalActionHandler(IActionBars actionBars, String id) {
		IAction action = actionRegistry.getAction(id);
		if (action != null)
			actionBars.setGlobalActionHandler(id, action);
	}

	/**
	 * Refresh.
	 */
	void refresh() {
		final EditPartViewer viewer = getViewer();
		final EditPart contents = viewer.getContents();
		if (contents != null) {
			contents.refresh();
		}
	}
}