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
 * @author Bob Brodt
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.ui.editor;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.modeler.ui.wizards.Bpmn2DiagramEditorInput;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.editor.DiagramEditorContextMenuProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.part.MultiPageSelectionProvider;
import org.eclipse.ui.services.IServiceLocator;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class implements a multi-page version of the BPMN2 Modeler (BPMN2Editor class).
 * To revert back to the original, single-page version simply change the editor extension
 * point in plugin.xml (see comments there).
 * 
 * This is still in the experimental phase and currently only supports a single diagram
 * per .bpmn file. An optional second page, which displays the XML source, can be created
 * from the context menu. The source view is not yet synchronized to the design view and
 * can only show the XML as of the last "Save" i.e. the current state of the file on disk,
 * not the in-memory model. Design/Source view synchronization will be implemented in a
 * future version, but direct editing of the XML will not be supported - it will remain
 * "view only".
 * 
 * Future versions will support multiple diagrams per .bpmn file with the ability to add
 * and remove pages containing different diagram types. It should be possible for the user
 * to create a single file that contains a mix of Process, Collaboration and Choreography
 * diagrams. Whether or not these types of files are actually deployable and/or executable
 * is another story ;)
 */
public class BPMN2MultiPageEditor extends MultiPageEditorPart {

	BPMN2Editor designEditor;
	StructuredTextEditor sourceViewer;
	CTabFolder tabFolder;
	int defaultTabHeight;
	int activePage = 0;
	List<BPMNDiagram> pages = new ArrayList<BPMNDiagram>();
	
	/**
	 * 
	 */
	public BPMN2MultiPageEditor() {
		super();
	}

	@Override
	protected IEditorSite createSite(IEditorPart editor) {
		return new MultiPageEditorSite(this, editor) {
			@Override
			protected void handleSelectionChanged(SelectionChangedEvent event) {
				ISelectionProvider parentProvider = getMultiPageEditor().getSite()
						.getSelectionProvider();
				if (parentProvider instanceof MultiPageSelectionProvider) {
					SelectionChangedEvent newEvent = getNewEvent(parentProvider, event);
					MultiPageSelectionProvider prov = (MultiPageSelectionProvider) parentProvider;
					prov.fireSelectionChanged(newEvent);
				}
			}
			
			@Override
			protected void handlePostSelectionChanged(SelectionChangedEvent event) {
				ISelectionProvider parentProvider = getMultiPageEditor().getSite()
						.getSelectionProvider();
				if (parentProvider instanceof MultiPageSelectionProvider) {
					SelectionChangedEvent newEvent = getNewEvent(parentProvider, event);
					MultiPageSelectionProvider prov = (MultiPageSelectionProvider) parentProvider;
					prov.firePostSelectionChanged(newEvent);
				}
			}
			
			protected SelectionChangedEvent getNewEvent(ISelectionProvider parentProvider, SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection ss = (IStructuredSelection)selection;
					Object o = ss.getFirstElement();
					if (o instanceof Node) {
						selection = getNewSelection((Node)o);
					}
				}
				if (selection!=null)
					return new SelectionChangedEvent(parentProvider, selection);
				return event;
			}

			protected StructuredSelection getNewSelection(Node node) {
				int type =  node.getNodeType();
				if (type==1) {
					// node type = element
					PictogramElement pe = null;
					Element elem = (Element)node;
					String value = elem.getAttribute("bpmnElement");
					if (value!=null) {
						pe = findPictogramElement(value);
					}
					
					if (pe==null) {
						value = elem.getAttribute("id");
						if (value!=null)
							pe = findPictogramElement(value);
					}
					
					if (pe!=null) {
						return new StructuredSelection(pe);
					}
					return getNewSelection(node.getParentNode());
				}
				else if (type==2) {
					// node type = attribute
					// search the attribute's owner
					Attr attr = (Attr)node;
					return getNewSelection(attr.getOwnerElement());
				}
				else if (type==3) {
					// node type = text
					return getNewSelection(node.getParentNode());
				}
				return null;
			}
			
			protected PictogramElement findPictogramElement(String id) {
				PictogramElement pictogramElement = null;
				if (id!=null) {
					BaseElement be = designEditor.getModelHandler().findElement(id);
					List<PictogramElement> pes = Graphiti.getLinkService().getPictogramElements(designEditor.getDiagramTypeProvider().getDiagram(), be);
					for (PictogramElement pe : pes) {
						if (pe instanceof ContainerShape) {
							pictogramElement = pe;
						}
						else if (pe instanceof FreeFormConnection) {
							pictogramElement = pe;
						}
					}
				}
				
				return pictogramElement;
			}
		};
	}

	@Override
	public String getTitle() {
		if (designEditor!=null)
			return designEditor.getTitle();
		return super.getTitle();
	}

	@Override
	public String getPartName() {
		if (designEditor!=null)
			return designEditor.getPartName();
		return super.getPartName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {
		tabFolder = (CTabFolder)getContainer();
		tabFolder.addCTabFolder2Listener( new CTabFolder2Listener() {

			@Override
			public void close(CTabFolderEvent event) {
				if (event.item.getData() == sourceViewer)
					removeSourceViewer();
			}

			@Override
			public void minimize(CTabFolderEvent event) {
			}

			@Override
			public void maximize(CTabFolderEvent event) {
			}

			@Override
			public void restore(CTabFolderEvent event) {
			}

			@Override
			public void showList(CTabFolderEvent event) {
			}
			
		});
		createDesignEditor();
	}

	protected void createDesignEditor() {
		if (designEditor==null) {
			designEditor = new DesignEditor(this);
			
			try {
				int pageIndex = tabFolder.getItemCount();
				if (sourceViewer!=null)
					--pageIndex;
				addPage(pageIndex, designEditor, BPMN2MultiPageEditor.this.getEditorInput());
				defaultTabHeight = tabFolder.getTabHeight();
				setPageText(pageIndex,"Design");

				// TODO: it should be possible to create additional instances of the BPMN2Editor
				// that use the same IEditorInput as the original, but work within different
				// BPMNPlane objects within the same model.
				// Likewise, it should be possible to remove a page, which causes the associated
				// BPMNPlane to be removed from the model. The last page may not be removed because
				// this would invalidate the bpmn file.
//				++pageIndex;
//				DesignEditor designEditor2 = new DesignEditor();
//				addPage(pageIndex, designEditor2, BPMN2MultiPageEditor.this.getEditorInput());
//				setPageText(pageIndex,"Design 2");
				
				defaultTabHeight = tabFolder.getTabHeight();

				updateTabs();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void setActivePage(int pageIndex) {
		activePage = pageIndex;
		super.setActivePage(pageIndex);
	}
	
	protected void addDesignPage(BPMNDiagram bpmnDiagram) {
		createDesignEditor();
		try {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					designEditor.setPictogramElementsForSelection(null);
					designEditor.refresh();
				}
			});
			
			int pageIndex = tabFolder.getItemCount();
			if (sourceViewer!=null)
				--pageIndex;
			Bpmn2DiagramEditorInput input = (Bpmn2DiagramEditorInput)designEditor.getEditorInput();
			input.setBpmnDiagram(bpmnDiagram);
			addPage(pageIndex, designEditor, input);
			setPageText(pageIndex,bpmnDiagram.getName());

			this.setActivePage(pageIndex);
			updateTabs();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void createSourceViewer() {
		if (sourceViewer==null) {
			sourceViewer = new SourceViewer();

			try {
				int pageIndex = tabFolder.getItemCount();
				FileEditorInput input = new FileEditorInput(designEditor.getModelFile());
				addPage(pageIndex, sourceViewer, input);
				tabFolder.getItem(pageIndex).setShowClose(true);
				
				setPageText(pageIndex,"Source");
				updateTabs();
			}
			catch (Exception e) {
				e.printStackTrace();
				if (sourceViewer!=null)
					sourceViewer.dispose();
			}
		}
	}

	public void addPage(int pageIndex, IEditorPart editor, IEditorInput input)
			throws PartInitException {
		super.addPage(pageIndex,editor,input);
		if (editor instanceof BPMN2Editor) {
			pages.add(pageIndex,((BPMN2Editor)editor).getBpmnDiagram());
		}
	}
	
	@Override
	public void removePage(int pageIndex) {
		Object page = tabFolder.getItem(pageIndex).getData();
		if (page instanceof EditorPart) {
			// make sure the editor gets disposed - neither CTabFolder nor super does this for us!
			((EditorPart)page).dispose();
		}
		super.removePage(pageIndex);
		updateTabs();
		if (page instanceof BPMN2Editor) {
			pages.remove(pageIndex);
		}
	}

	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);

		IEditorPart editor = getEditor(newPageIndex);
		if (editor instanceof BPMN2Editor) {
			BPMNDiagram bpmnDiagram = pages.get(newPageIndex);
			((BPMN2Editor)editor).setBpmnDiagram(bpmnDiagram);
		}
	}

	public void removeSourceViewer() {
		// there will only be one source page and it will always be the last page in the tab folder
		if (sourceViewer!=null) {
			int pageIndex = tabFolder.getItemCount() - 1;
			if (pageIndex>0)
				removePage(pageIndex);
		}
	}

	private void updateTabs() {
		if (tabFolder.getItemCount()==1) {
			tabFolder.setTabHeight(0);
		}
		else
			tabFolder.setTabHeight(defaultTabHeight);
		tabFolder.layout();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		designEditor.doSave(monitor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		IEditorPart activeEditor = getActiveEditor();
		activeEditor.doSaveAs();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		
		/* 
		 * Depending upon the active page in multipage editor, call the saveAsAllowed. 
		 * It helps to see whether a particular editor allows 'save as' feature 
		 */
		IEditorPart activeEditor = getActiveEditor();
		return activeEditor.isSaveAsAllowed();
	}

	@Override
	public void dispose() {
		designEditor.dispose();
		if (sourceViewer!=null)
			sourceViewer.dispose();
	}

	public class DesignEditor extends BPMN2Editor {
		
		BPMN2MultiPageEditor multipageEditor;
		ResourceSetListener resourceSetListener = null;
		
		public DesignEditor(BPMN2MultiPageEditor mpe) {
			super();
			multipageEditor = mpe;
		}
		
		public void dispose() {
			getEditingDomain().removeResourceSetListener(resourceSetListener);
			resourceSetListener = null;
			super.dispose();
		}
		
		@Override
		protected void setInput(IEditorInput input) {
			super.setInput(input);
			if (resourceSetListener==null) {
				resourceSetListener = new ResourceSetListener() {
					@Override
					public NotificationFilter getFilter() {
						return null;
					}

					@Override
					public Command transactionAboutToCommit(ResourceSetChangeEvent event) throws RollbackException {
						return null;
					}

					@Override
					public void resourceSetChanged(ResourceSetChangeEvent event) {
						for (Notification n : event.getNotifications()) {
							if (n.getNewValue() instanceof BPMNPlane && n.getNotifier() instanceof BPMNDiagram) {
								final BPMNDiagram d = (BPMNDiagram)n.getNotifier();
								Display.getCurrent().asyncExec( new Runnable() {
									@Override
									public void run() {
										multipageEditor.addDesignPage(d);
									}
								});
								break;
							}
						}
					}

					@Override
					public boolean isAggregatePrecommitListener() {
						return false;
					}

					@Override
					public boolean isPrecommitOnly() {
						return false;
					}

					@Override
					public boolean isPostcommitOnly() {
						return true;
					}
				};
				getEditingDomain().addResourceSetListener(resourceSetListener);
			}
		}

		@Override
		protected void createActions() {
			super.createActions();
			ActionRegistry registry = getActionRegistry();
			IAction action = new WorkbenchPartAction(designEditor) {

				@Override
				protected void init() {
					super.init();
					setId("show.or.hide.source.view");
				}

				@Override
				public String getText() {
					return sourceViewer==null ? "Show Source View" : "Hide Source View";
				}

				@Override
				protected boolean calculateEnabled() {
					return true;
				}
				
				public void run() {
					if (sourceViewer==null) {
						createSourceViewer();
						setActivePage(tabFolder.getItemCount()-1);
					}
					else {
						removeSourceViewer();
					}
				}
			};
			registry.registerAction(action);
		}

		@Override
		protected ContextMenuProvider createContextMenuProvider() {
			return new DiagramEditorContextMenuProvider(getGraphicalViewer(), getActionRegistry(), getDiagramTypeProvider()) {
				@Override
				public void buildContextMenu(IMenuManager manager) {
					super.buildContextMenu(manager);
					IAction action = getActionRegistry().getAction("show.or.hide.source.view");
					action.setText( action.getText() );
					manager.add(action);
				}
			};
		}
	}

	public class SourceViewer extends StructuredTextEditor {
		
		ActionRegistry actionRegistry = null;
		
		@Override
		@SuppressWarnings("rawtypes")
		public Object getAdapter(Class required) {
			if (required==ActionRegistry.class)
				return getActionRegistry();
			if (required==BPMN2Editor.class || required==DiagramEditor.class)
				return designEditor;
			return super.getAdapter(required);
		}

		@Override
		public boolean isEditable() {
			return false;
		}

		@Override
		public void dispose() {
			super.dispose();
			BPMN2MultiPageEditor.this.sourceViewer = null;
		}

		protected ActionRegistry getActionRegistry() {
			if (actionRegistry == null)
				actionRegistry = new ActionRegistry();
			return actionRegistry;
		}
	}

	public BPMN2Editor getDesignEditor() {
		return designEditor;
	}
}
