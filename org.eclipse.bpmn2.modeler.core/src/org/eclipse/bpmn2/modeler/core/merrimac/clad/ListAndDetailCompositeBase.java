package org.eclipse.bpmn2.modeler.core.merrimac.clad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.Bpmn2TabbedPropertySheetPage;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.merrimac.IConstants;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditor;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerFactory;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.runtime.ModelEnablementDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class ListAndDetailCompositeBase extends Composite implements ResourceSetListener {

	public final static Bpmn2Package PACKAGE = Bpmn2Package.eINSTANCE;
	public final static Bpmn2ModelerFactory FACTORY = Bpmn2ModelerFactory.getInstance();
	protected AbstractBpmn2PropertySection propertySection;
	protected FormToolkit toolkit;
	private ModelEnablementDescriptor modelEnablement;
	protected IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
	protected EObject businessObject;
	protected int style;
	protected DiagramEditor diagramEditor;
	protected TransactionalEditingDomainImpl editingDomain;
	protected ModelHandler modelHandler;

	public ListAndDetailCompositeBase(AbstractBpmn2PropertySection section) {
		this(section, SWT.NONE);
	}
	
	public ListAndDetailCompositeBase(AbstractBpmn2PropertySection section, int style) {
		super(section.getParent(), style);
		propertySection = section;
		toolkit = propertySection.getWidgetFactory();
		initialize();
	}
	
	public ListAndDetailCompositeBase(Composite parent, int style) {
		super(parent, style);
		toolkit = new FormToolkit(Display.getCurrent());
		this.style = style;
		initialize();
	}

	protected void initialize() {
		setLayout(new GridLayout(3, false));
		if (getParent().getLayout() instanceof GridLayout) {
			GridLayout layout = (GridLayout) getParent().getLayout();
			setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, layout.numColumns, 1));
		}
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
	}
	
	@Override
	public void dispose() {
		removeDomainListener();
		ModelUtil.disposeChildWidgets(this);
		super.dispose();
	}
	
	protected void addDomainListener() {
		if (editingDomain==null) {
			editingDomain = (TransactionalEditingDomainImpl)getDiagramEditor().getEditingDomain();
			editingDomain.addResourceSetListener(this);
		}
	}

	protected void removeDomainListener() {
		if (editingDomain!=null) {
			editingDomain.removeResourceSetListener(this);
		}
	}

	public void setPropertySection(AbstractBpmn2PropertySection section) {
		propertySection = section;
	}
	
	public AbstractBpmn2PropertySection getPropertySection() {
		if (propertySection!=null)
			return propertySection;
		Composite parent = getParent();
		while (parent!=null && !(parent instanceof ListAndDetailCompositeBase))
			parent = parent.getParent();
		if (parent instanceof ListAndDetailCompositeBase)
			return ((ListAndDetailCompositeBase)parent).getPropertySection();
		return null;
	}

	public void redrawPage() {
		if (getPropertySection()!=null) {
			getParent().layout();
			getPropertySection().layout();
		}
		else {
			ModelUtil.recursivelayout(getParent());
		}
	}

	public TabbedPropertySheetPage getTabbedPropertySheetPage() {
		return getPropertySection().getTabbedPropertySheetPage();
	}

	public FormToolkit getToolkit() {
		return toolkit;
	}

	public void setDiagramEditor(DiagramEditor bpmn2Editor) {
		this.diagramEditor = bpmn2Editor;
	}
	
	public DiagramEditor getDiagramEditor() {
		if (diagramEditor!=null)
			return diagramEditor;
		if (getPropertySection()!=null)
			return (DiagramEditor)getPropertySection().getDiagramEditor();
		Composite parent = getParent();
		while (parent!=null && !(parent instanceof ListAndDetailCompositeBase))
			parent = parent.getParent();
		if (parent instanceof ListAndDetailCompositeBase)
			return diagramEditor = ((ListAndDetailCompositeBase)parent).getDiagramEditor();
		
		IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		if (part instanceof DiagramEditor)
			diagramEditor = (DiagramEditor)part;
		else if (part instanceof IEditorPart) {
			diagramEditor = (DiagramEditor) ((IEditorPart)part).getAdapter(DiagramEditor.class);
		}
		else if (part instanceof PropertySheet) {
			TabbedPropertySheetPage page = (TabbedPropertySheetPage) ((PropertySheet)part).getCurrentPage();
			if (page instanceof Bpmn2TabbedPropertySheetPage) {
				diagramEditor = ((Bpmn2TabbedPropertySheetPage)page).getDiagramEditor();
			}
		}
		return diagramEditor;
	}
	
	public ModelEnablementDescriptor getModelEnablement(EObject object) {
		if (object==null)
			object = businessObject;
		modelEnablement =  getTargetRuntime().getModelEnablements(object);
		return modelEnablement;
	}

	protected boolean isModelObjectEnabled(String className, String featureName) {
		return modelEnablement.isEnabled(className, featureName);
	}

	protected boolean isModelObjectEnabled(EClass eclass, EStructuralFeature feature) {
		String className = eclass==null ? null : eclass.getName();
		String featureName = feature==null ? null : feature.getName();
		return isModelObjectEnabled(className, featureName);
	}

	protected boolean isModelObjectEnabled(EClass eclass) {
		if (eclass!=null)
			return isModelObjectEnabled(eclass,null);
		return true;
	}

	public TargetRuntime getTargetRuntime() {
		return (TargetRuntime) getDiagramEditor().getAdapter(TargetRuntime.class);
	}
	
	public Bpmn2Preferences getPreferences() {
		return (Bpmn2Preferences) getDiagramEditor().getAdapter(Bpmn2Preferences.class);
	}
	
	@Override
	public void setData(Object object) {
		if (object instanceof EObject)
			setBusinessObject((EObject)object);
	}
	
	public void setBusinessObject(EObject object) {
		getDiagramEditor();
		if (diagramEditor==null)
			diagramEditor = ModelUtil.getEditor(object);
		addDomainListener();
		getModelEnablement(object);
		try {
			modelHandler = ModelHandlerLocator.getModelHandler(
					getDiagramEditor().getDiagramTypeProvider().getDiagram().eResource());
		} catch (IOException e1) {
			Activator.logError(e1);
		}
		businessObject = object;
	}

	public final EObject getBusinessObject() {
		return businessObject;
	}

	@Override
	public NotificationFilter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Command transactionAboutToCommit(ResourceSetChangeEvent event)
			throws RollbackException {
		// TODO Auto-generated method stub
		return null;
	}

	protected void getAllChildWidgets(Composite parent, List<Control>kids) {
		if (parent!=null && !parent.isDisposed()) {
			Control[] cs = parent.getChildren();
			for (Control c : cs) {
				if (c instanceof Composite) {
					getAllChildWidgets((Composite)c,kids);
				}
				if (!c.isDisposed() &&
					c.getData(IConstants.NOTIFY_CHANGE_LISTENER_KEY) instanceof INotifyChangedListener) {
						kids.add(c);
				}
			}
		}
	}
	
	// TODO: Figure out a broader method of detecting model changes.
	// This listener is only called AFTER a transaction has committed,
	// it will not receive notification of model changes inside a txn.
	// So, while this works in the Property Sheet pages, things like
	// the ObjectEditingDialog (which makes changes in the current txn)
	// will not cause other widgets in the dialog to businessObject notified.
	@Override
	public void resourceSetChanged(ResourceSetChangeEvent event) {
		final List<Notification> notifications = new ArrayList<Notification>();
		for (Notification n : event.getNotifications()) {
			int et = n.getEventType();
			if (et==Notification.SET
					|| et==Notification.UNSET
					|| et==Notification.ADD
					|| et==Notification.ADD_MANY
					|| et==Notification.CREATE
					|| et==Notification.REMOVE
					|| et==Notification.REMOVE_MANY) {

				notifications.add(n);
			}
		}
		// run this in the UI thread
		Display.getDefault().asyncExec( new Runnable() {
			public void run() {
				List<Control>kids = new ArrayList<Control>();
				Composite parent = ListAndDetailCompositeBase.this;
				try {
					AbstractBpmn2PropertySection section = ListAndDetailCompositeBase.this.getPropertySection();
					if (section!=null && section.getTabbedPropertySheetPage()!=null) {
						parent = (Composite)section.getTabbedPropertySheetPage().getControl();
					}
				}
				catch (Exception e) {
				}
				getAllChildWidgets(parent, kids);
				for (Notification n : notifications) {
					for (Control c : kids) {
						INotifyChangedListener listener = (INotifyChangedListener)c.getData(
								IConstants.NOTIFY_CHANGE_LISTENER_KEY);
						if (listener!=null) {
							listener.notifyChanged(n);
						}
					}
				}
			}
		});
	}

	@Override
	public boolean isAggregatePrecommitListener() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPrecommitOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPostcommitOnly() {
		// TODO Auto-generated method stub
		return false;
	}
}
