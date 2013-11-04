package org.camunda.bpm.modeler.ui.diagram.editor;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.editor.DiagramEditorContextMenuProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPart;

public class DesignEditor extends Bpmn2Editor {
	
	protected ResourceSetListener resourceSetListener = null;
	private BPMNDiagram bpmnDiagramDeleted = null;
	// the container that holds the tabFolder
	protected Composite container;
	protected CTabFolder tabFolder;
	private int defaultTabHeight;

	public DesignEditor() {
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
		super.setPartName(URI.decode(partName));
    }

	private boolean inSelectionChanged = false;
	
	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		// is the selected EObject in our resource?
		if (!inSelectionChanged) {
			try {
				inSelectionChanged = true;
				EObject object = BusinessObjectUtil.getBusinessObjectForSelection(selection);
				if (object!=null && object.eResource() == bpmnResource) {
					BPMNDiagram newBpmnDiagram = null;
					if (object instanceof BaseElement) {
						// select the right diagram page
						newBpmnDiagram = DIUtils.findBPMNDiagram(this, (BaseElement)object, true);
					}
					else if (object instanceof BPMNDiagram) {
						newBpmnDiagram = (BPMNDiagram)object;
					}
					if (newBpmnDiagram!=null && getBpmnDiagram() != newBpmnDiagram) {
						BPMNDiagram rootBpmnDiagram = newBpmnDiagram;
						object = newBpmnDiagram.getPlane().getBpmnElement();
						while (!(object instanceof RootElement)) {
							// this is a BPMNDiagram that contains a SubProcess, not a RootElement
							// so find the BPMNDiagram that contains the RootElement which owns
							// this SubProcess
							rootBpmnDiagram = DIUtils.findBPMNDiagram(this, (BaseElement)object, true);
							object = rootBpmnDiagram.getPlane().getBpmnElement();
						}
						if (rootBpmnDiagram != newBpmnDiagram) {
							final BPMNDiagram d = newBpmnDiagram;
							Display.getDefault().asyncExec(new Runnable() {
								public void run() {
									showDesignPage(d);
									Object sel = BusinessObjectUtil.getPictogramElementForSelection(selection);
									if (sel instanceof PictogramElement)
										DesignEditor.super.selectPictogramElements(new PictogramElement[] {(PictogramElement)sel});
								}
							});
						}
					}
				}
				DesignEditor.super.selectionChanged(part,selection);
				
			} catch(Exception e) {
			} finally {
				inSelectionChanged = false;
			}
		}
	}

	public void pageChange(final BPMNDiagram bpmnDiagram) {
		setBpmnDiagram(bpmnDiagram);
		reloadTabs();
		tabFolder.setSelection(0);
		tabFolder.getItem(0).getControl().setVisible(true);
		tabFolder.getItem(0).setData(bpmnDiagram);
	}
	
	public void selectBpmnDiagram(BPMNDiagram bpmnDiagram) {
		Diagram diagram = DIUtils.findDiagram(DesignEditor.this, bpmnDiagram);
		if (diagram != null)
			selectPictogramElements(new PictogramElement[] {(PictogramElement)diagram});
	}

	public void showDesignPage(final BPMNDiagram bpmnDiagram) {
		CTabItem current = tabFolder.getSelection();
		if (current!=null && current.getData() == bpmnDiagram) {
			current.getControl().setVisible(true);
			return;
		}
		showDesignPageInternal(bpmnDiagram);
	}
	
	private void showDesignPageInternal(BPMNDiagram bpmnDiagram) {
		for (CTabItem item : tabFolder.getItems()) {
			if (item.getData() == bpmnDiagram) {
				setBpmnDiagram(bpmnDiagram);
				tabFolder.setSelection(item);
				item.getControl().setVisible(true);
			}
		}
	}

	protected void addDesignPage(final BPMNDiagram bpmnDiagram) {
		setBpmnDiagram( (BPMNDiagram)tabFolder.getItem(0).getData() );
		reloadTabs();
		showDesignPage(bpmnDiagram);
	}
	
	protected void removeDesignPage(final BPMNDiagram bpmnDiagram) {
		CTabItem currentItem = tabFolder.getSelection();
		BPMNDiagram currentBpmnDiagram = (BPMNDiagram)currentItem.getData();
		setBpmnDiagram( (BPMNDiagram)tabFolder.getItem(0).getData() );
		reloadTabs();
		showDesignPage(currentBpmnDiagram);
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
				BPMNDiagram bd = DIUtils.findBPMNDiagram(this, fe, false);
				if (bd!=null)
					bpmnDiagrams.add(bd);
				TreeIterator<EObject> iter = fe.eAllContents();
				while (iter.hasNext()) {
					EObject o = iter.next();
					if (o instanceof BaseElement) {
						bd = DIUtils.findBPMNDiagram(this, (BaseElement)o, false);
						if (bd!=null) {
							bpmnDiagrams.add(bd);
						}
					}
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
					showDesignPageInternal(bpmnDiagram);
					selectBpmnDiagram(bpmnDiagram);
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
			boolean debug = Activator.getDefault().isDebugging();
			
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
