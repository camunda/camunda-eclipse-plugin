package org.eclipse.bpmn2.modeler.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.graphiti.ui.editor.DiagramEditorContextMenuProvider;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPart;

public class DesignEditor extends BPMN2Editor {
	
	protected ResourceSetListener resourceSetListener = null;
	private BPMNDiagram bpmnDiagramDeleted = null;
	protected boolean debug;
	// the container that holds the tabFolder
	protected Composite container;
	protected CTabFolder tabFolder;
	private int defaultTabHeight;

	public DesignEditor(BPMN2MultiPageEditor bpmn2MultiPageEditor, BPMN2MultiPageEditor mpe) {
		super(mpe);
	}

	public void deleteBpmnDiagram(BPMNDiagram bpmnDiagram) {
		this.bpmnDiagramDeleted = bpmnDiagram;
	}

	public void dispose() {
		if (bpmnDiagramDeleted == null) {
			getEditingDomain().removeResourceSetListener(resourceSetListener);
			resourceSetListener = null;
			super.dispose();
		} else {
			bpmnDiagramDeleted = null;
		}
	}

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		if (resourceSetListener == null) {
			resourceSetListener = new AddRemoveDiagramListener();
			getEditingDomain().addResourceSetListener(resourceSetListener);
		}
	}
	
	@Override
    protected void setPartName(String partName) {
		IEditorInput input = getEditorInput();
		if (input instanceof DiagramEditorInput) {
			URI uri = ((DiagramEditorInput)input).getUri();
			partName = URI.decode(uri.trimFileExtension().lastSegment());
	
		}
		super.setPartName(partName);
    }
	
	public void pageChange(BPMNDiagram bpmnDiagram) {
		super.setBpmnDiagram(bpmnDiagram);
		reloadTabs();
	}

	private void reloadTabs() {
		BPMNDiagram bpmnDiagram = getBpmnDiagram();
		List<BPMNDiagram> bpmnDiagrams = new ArrayList<BPMNDiagram>();
		
		BaseElement bpmnElement = bpmnDiagram.getPlane().getBpmnElement();
		List<FlowElement> flowElements = null;
		if (bpmnElement instanceof Process) {
			flowElements = ((Process)bpmnElement).getFlowElements();
		}
		else if (bpmnElement instanceof Collaboration) {
			((Collaboration)bpmnElement).getParticipants();
		}
		else if (bpmnElement instanceof Choreography) {
			flowElements = ((Choreography)bpmnElement).getFlowElements();
		}
		if (flowElements != null) {
			for (FlowElement fe : flowElements) {
				BPMNDiagram bd = DIUtils.findBPMNDiagram(this, fe);
				if (bd!=null) {
					bpmnDiagrams.add(bd);
				}
			}
		}
		
		tabFolder.setLayoutDeferred(true);
		for (int i=tabFolder.getItemCount()-1; i>0; --i) {
			tabFolder.getItem(i).setControl(null);
			tabFolder.getItem(i).dispose();
		}

		if (bpmnDiagrams.size()>0) {
			for (BPMNDiagram bd : bpmnDiagrams) {
				CTabItem item = new CTabItem(tabFolder, SWT.NONE);
				item.setControl(container);
				BaseElement be = bd.getPlane().getBpmnElement();
				item.setText(ModelUtil.getDisplayName(be));
				item.setData(bd);
			}
			
		}
		tabFolder.setSelection(0);
		tabFolder.getItem(0).getControl().setVisible(true);
		tabFolder.getItem(0).setData(bpmnDiagram);
		tabFolder.setLayoutDeferred(false);
		
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				updateTabs();
			}
		});
	}
	
	public void createPartControl(Composite parent) {
		if (getGraphicalViewer()==null) {
			tabFolder = new CTabFolder(parent, SWT.BOTTOM);
			tabFolder.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					int pageIndex = tabFolder.indexOf((CTabItem) e.item);
					CTabItem item = tabFolder.getItem(pageIndex);
					BPMNDiagram bpmnDiagram = (BPMNDiagram) item.getData();
					showDesignPage(bpmnDiagram);
				}
			});
			tabFolder.addTraverseListener(new TraverseListener() { 
				// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=199499 : Switching tabs by Ctrl+PageUp/PageDown must not be caught on the inner tab set
				public void keyTraversed(TraverseEvent e) {
					switch (e.detail) {
						case SWT.TRAVERSE_PAGE_NEXT:
						case SWT.TRAVERSE_PAGE_PREVIOUS:
							int detail = e.detail;
							e.doit = true;
							e.detail = SWT.TRAVERSE_NONE;
							Control control = tabFolder.getParent();
							control.traverse(detail, new Event());
					}
				}
			});
			defaultTabHeight = tabFolder.getTabHeight();

			container = new Composite(tabFolder, SWT.NONE);
			container.setLayout(new FillLayout());
			CTabItem item = new CTabItem(tabFolder, SWT.NONE, 0);
			item.setText("Diagram");
			item.setControl(container);
			item.setData(getBpmnDiagram());

			super.createPartControl(container);
			
			
			// create additional editor tabs for BPMNDiagrams in the parent MultiPageEditor
			final List<BPMNDiagram> bpmnDiagrams = getModelHandler().getAll(BPMNDiagram.class);
			for (int i=1; i<bpmnDiagrams.size(); ++i) {
				BPMNDiagram bpmnDiagram = bpmnDiagrams.get(i);
				if (bpmnDiagram.getPlane().getBpmnElement() instanceof RootElement)
					multipageEditor.addDesignPage(bpmnDiagram);
			}
		}
	}
	
	public void showDesignPage(final BPMNDiagram bpmnDiagram) {
		for (CTabItem item : tabFolder.getItems()) {
			if (item.getData() == bpmnDiagram) {
				setBpmnDiagram(bpmnDiagram);
				tabFolder.setSelection(item);
				item.getControl().setVisible(true);
			}
		}
	}
	
	public void updateTabs() {
//		if (!tabFolder.isLayoutDeferred()) {
			if (tabFolder.getItemCount()==1) {
				tabFolder.setTabHeight(0);
			}
			else
				tabFolder.setTabHeight(defaultTabHeight);
//		}
		tabFolder.layout();
	}
	
	@Override
	protected void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();
		IAction action = new WorkbenchPartAction(multipageEditor.getDesignEditor()) {

			@Override
			protected void init() {
				super.init();
				setId("show.or.hide.source.view");
			}

			@Override
			public String getText() {
				return multipageEditor.getSourceViewer() == null ? "Show Source View" : "Hide Source View";
			}

			@Override
			protected boolean calculateEnabled() {
				return true;
			}

			public void run() {
				if (multipageEditor.getSourceViewer() == null) {
					multipageEditor.createSourceViewer();
				} else {
					multipageEditor.removeSourceViewer();
				}
			}
		};
		registry.registerAction(action);

		action = new WorkbenchPartAction(multipageEditor.getDesignEditor()) {

			@Override
			protected void init() {
				super.init();
				setId("delete.page");
			}

			@Override
			public String getText() {
				int pageIndex = multipageEditor.getActivePage();
				return "Delete Diagram \"" + multipageEditor.getTabItem(pageIndex).getText() + "\"";
			}

			@Override
			public boolean isEnabled() {
				return calculateEnabled();
			}

			@Override
			protected boolean calculateEnabled() {
				BPMNDiagram bpmnDiagram = getBpmnDiagram();
				BPMNPlane plane = bpmnDiagram.getPlane();
				BaseElement process = plane.getBpmnElement();
				List<Participant> participants = getModelHandler().getAll(Participant.class);
				for (Participant p : participants) {
					if (p.getProcessRef() == process)
						return false;
				}
				return true;
			}

			public void run() {
				int pageIndex = multipageEditor.getActivePage();
				boolean result = MessageDialog.openQuestion(getSite().getShell(),
						"Delete Page",
						"Are you sure you want to delete the page \""
						+ multipageEditor.getTabItem(pageIndex).getText()
						+ "\"?");
				if (result) {
					final BPMNDiagram bpmnDiagram = getBpmnDiagram();
					TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(bpmnDiagram);
					// removeDesignPage(bpmnDiagram);

					if (domain != null) {
						domain.getCommandStack().execute(new RecordingCommand(domain) {
							@Override
							protected void doExecute() {
								BPMNPlane plane = bpmnDiagram.getPlane();
								BaseElement process = plane.getBpmnElement();
								DIUtils.deleteDiagram(DesignEditor.this, bpmnDiagram);
								EcoreUtil.delete(process);
							}
						});
					}
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
				action.setText(action.getText());
				manager.add(action);

				int pageIndex = multipageEditor.getActivePage();
				int lastPage = multipageEditor.getDesignPageCount();
				if (pageIndex > 0 && pageIndex < lastPage) {
					action = getActionRegistry().getAction("delete.page");
					action.setText(action.getText());
					action.setEnabled(action.isEnabled());
					manager.add(action);
				}
			}
		};
	}

	public class AddRemoveDiagramListener implements ResourceSetListener {
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
				int et = n.getEventType();
				Object notifier = n.getNotifier();
				Object newValue = n.getNewValue();
				Object oldValue = n.getOldValue();
				Object feature = n.getFeature();

				if (debug) {
					if (et == Notification.ADD || et == Notification.REMOVE || et == Notification.SET) {
						System.out.print("event: " + et + "\t");
						if (notifier instanceof EObject) {
							System.out.print("notifier: $" + ((EObject) notifier).eClass().getName());
						} else
							System.out.print("notifier: " + notifier);
					}
				}

				if (et == Notification.ADD) {
					if (debug) {
						if (newValue instanceof EObject) {
							System.out.println("\t\tvalue:    " + ((EObject) newValue).eClass().getName());
						} else
							System.out.println("\t\tvalue:    " + newValue);
					}

					if (notifier instanceof Definitions
							&& newValue instanceof BPMNDiagram
							&& feature == Bpmn2Package.eINSTANCE.getDefinitions_Diagrams()) {
						final BPMNDiagram bpmnDiagram = (BPMNDiagram) newValue;
						BaseElement bpmnElement = bpmnDiagram.getPlane().getBpmnElement();
						if (bpmnElement instanceof RootElement)
							multipageEditor.addDesignPage(bpmnDiagram);
						else
							reloadTabs();
						break;
					}
				} else if (et == Notification.REMOVE) {
					if (debug) {
						if (oldValue instanceof EObject) {
							System.out.println("\t\tvalue:    " + ((EObject) oldValue).eClass().getName());
						} else
							System.out.println("\t\tvalue:    " + oldValue);
					}

					if (notifier instanceof Definitions
							&& oldValue instanceof BPMNDiagram
							&& feature == Bpmn2Package.eINSTANCE.getDefinitions_Diagrams()) {
						final BPMNDiagram bpmnDiagram = (BPMNDiagram) oldValue;
						BaseElement bpmnElement = bpmnDiagram.getPlane().getBpmnElement();
						if (bpmnElement instanceof RootElement)
							multipageEditor.removeDesignPage(bpmnDiagram);
						else
							reloadTabs();
						break;
					}
				} else if (et == Notification.SET) {
					// check if we need to change the tab names
					if (n.getFeature() instanceof EStructuralFeature &&
							((EStructuralFeature)n.getFeature()).getName().equals("name")) {
						for (int i=1; i<tabFolder.getItemCount(); ++i) {
							CTabItem item = tabFolder.getItem(i);
							BPMNDiagram bpmnDiagram = (BPMNDiagram)item.getData();
							if (bpmnDiagram!=null) {
								if (bpmnDiagram==notifier || bpmnDiagram.getPlane().getBpmnElement() == notifier) {
									item.setText(n.getNewStringValue());
								}
							}
						}
						for (int i=0; i<multipageEditor.getPageCount(); ++i) {
							BPMNDiagram bpmnDiagram = multipageEditor.getBpmnDiagram(i);
							if (bpmnDiagram == notifier) {
								CTabItem item = multipageEditor.getTabItem(i);
								item.setText(n.getNewStringValue());
							}
						}
					}
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
	}
}
