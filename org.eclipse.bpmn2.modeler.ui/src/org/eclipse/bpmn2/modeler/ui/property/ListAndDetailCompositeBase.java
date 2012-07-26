package org.eclipse.bpmn2.modeler.ui.property;

import java.io.IOException;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerFactory;
import org.eclipse.bpmn2.modeler.core.runtime.ModelEnablementDescriptor;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class ListAndDetailCompositeBase extends Composite implements ResourceSetListener {

	public final static Bpmn2Package PACKAGE = Bpmn2Package.eINSTANCE;
	public final static Bpmn2ModelerFactory FACTORY = Bpmn2ModelerFactory.getInstance();
	protected AbstractBpmn2PropertySection propertySection;
	protected FormToolkit toolkit;
	protected ModelEnablementDescriptor modelEnablement;
	protected IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
	protected EObject businessObject;
	protected int style;
	protected BPMN2Editor bpmn2Editor;
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
		addDomainListener();
	}
	
	@Override
	public void dispose() {
		removeDomainListener();
		PropertyUtil.disposeChildWidgets(this);
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
		return propertySection;
	}
	
	public TabbedPropertySheetPage getTabbedPropertySheetPage() {
		return propertySection.getTabbedPropertySheetPage();
	}

	public FormToolkit getToolkit() {
		return toolkit;
	}

	public void setDiagramEditor(BPMN2Editor bpmn2Editor) {
		this.bpmn2Editor = bpmn2Editor;
	}
	
	public BPMN2Editor getDiagramEditor() {
		if (bpmn2Editor!=null)
			return bpmn2Editor;
		if (propertySection!=null)
			return (BPMN2Editor)propertySection.getDiagramEditor();
		Composite parent = getParent();
		while (parent!=null && !(parent instanceof ListAndDetailCompositeBase))
			parent = parent.getParent();
		if (parent instanceof ListAndDetailCompositeBase)
			return ((ListAndDetailCompositeBase)parent).getDiagramEditor();
		return BPMN2Editor.getActiveEditor();
	}

	public void setBusinessObject(EObject object) {
		modelEnablement = getDiagramEditor().getTargetRuntime().getModelEnablements(object);
		try {
			modelHandler = ModelHandlerLocator.getModelHandler(
					getDiagramEditor().getDiagramTypeProvider().getDiagram().eResource());
		} catch (IOException e1) {
			Activator.showErrorWithLogging(e1);
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

	@Override
	public void resourceSetChanged(ResourceSetChangeEvent event) {
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
