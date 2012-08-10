package org.eclipse.bpmn2.modeler.ui.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.runtime.Bpmn2SectionDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.IBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptorProvider;

// TODO: create a tabbed composite that contains all of the sections normally displayed in the tabbed property sheet
public class DefaultDialogComposite extends AbstractDialogComposite {

	protected static final String DESCRIPTION_SECTION_ID = "org.eclipse.bpmn2.modeler.description.tab.section";
	protected static final String ADVANCED_SECTION_ID = "org.eclipse.bpmn2.modeler.advanced.tab.section";
	protected IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
	protected EObject businessObject;
	protected List<AbstractDetailComposite> details = new ArrayList<AbstractDetailComposite>();
	protected ScrolledForm form;
	protected TabFolder folder;
	protected Composite control;
	protected ITabDescriptor[] tabDescriptors;
	protected AbstractBpmn2PropertySection section;
	
	public DefaultDialogComposite(Composite parent, int style) {
		super(parent, style);
		
		setLayout(new FillLayout());
		
		ITabDescriptor[] tabDescriptors = getTabDescriptors();
		int detailsCount = getDetailsCount();
		
		if (detailsCount>1) {
			folder = new TabFolder(parent, SWT.NONE);
			folder.setLayout(new FormLayout());
			folder.setBackground(parent.getBackground());
			
			int index = 0;
			for (ITabDescriptor td : tabDescriptors) {
				for (Object o : td.getSectionDescriptors()) {
					if (o instanceof Bpmn2SectionDescriptor) {
						Bpmn2SectionDescriptor sd = (Bpmn2SectionDescriptor)o;
						if (DESCRIPTION_SECTION_ID.equals(sd.getId()) || ADVANCED_SECTION_ID.equals(sd.getId()))
							continue;
			
						TabItem tab = new TabItem(folder, SWT.NONE);
						form = new ScrolledForm(folder, SWT.V_SCROLL);
						form.setBackground(parent.getBackground());
						FormData data = new FormData();
						data.top = new FormAttachment(0, 0);
						data.bottom = new FormAttachment(100, 0);
						data.left = new FormAttachment(0, 0);
						data.right = new FormAttachment(100, 0);
	
						form.setLayoutData(data);
						form.setExpandVertical(true);
						form.setExpandHorizontal(true);
						form.setBackground(parent.getBackground());
						
						Composite formBody = form.getBody();
						TableWrapLayout tableWrapLayout = new TableWrapLayout();
						tableWrapLayout.numColumns = 1;
						tableWrapLayout.verticalSpacing = 1;
						tableWrapLayout.horizontalSpacing = 1;
						TableWrapData twd = new TableWrapData(TableWrapData.FILL_GRAB);
						formBody.setLayout(tableWrapLayout);
						formBody.setLayoutData(twd);
						
						section = (AbstractBpmn2PropertySection)sd.getSectionClass();
						AbstractDetailComposite detail = getDetails(index++, formBody);
						detail.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
	
						tab.setText(td.getLabel());
						tab.setControl(form);
						details.add(detail);
					}
				}
			}
			control = folder;
		}
		else if (section!=null) {
			control = section.createSectionRoot(parent,SWT.NONE);
		}
		else {
			control = PropertiesCompositeFactory.createDialogComposite(getBusinessObjectClass().getInstanceClass(), parent, SWT.NONE);
		}
		
		control.setLayout(new FormLayout());
		control.setBackground(parent.getBackground());
	}

	public EClass getBusinessObjectClass() {
		return EcorePackage.eINSTANCE.getEObject();
	}
	
	protected ITabDescriptor[] getTabDescriptors() {
		if (tabDescriptors==null) {
			IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().getActivePart();
			ISelection sel = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
			ITabDescriptorProvider tabDescriptorProvider = (ITabDescriptorProvider)part.getAdapter(ITabDescriptorProvider.class);
			tabDescriptors = tabDescriptorProvider.getTabDescriptors(part,sel);
		}
		return tabDescriptors;
	}
	
	protected int getDetailsCount() {
		int detailsCount = 0;
		ITabDescriptor[] tabDescriptors = getTabDescriptors();
		for (ITabDescriptor td : tabDescriptors) {
			for (Object o : td.getSectionDescriptors()) {
				if (o instanceof Bpmn2SectionDescriptor) {
					Bpmn2SectionDescriptor sd = (Bpmn2SectionDescriptor)o;
					if (!DESCRIPTION_SECTION_ID.equals(sd.getId()) && !ADVANCED_SECTION_ID.equals(sd.getId())) {
						section = (AbstractBpmn2PropertySection)sd.getSectionClass();
						++detailsCount;
					}
				}
			}
		}
		return detailsCount;
	}
	
	protected AbstractDetailComposite getDetails(int index, Composite parent) {
		return section.createSectionRoot(parent,SWT.NONE);
	}

	@Override
	public void setBusinessObject(EObject object) {
		businessObject = object;
		for (AbstractDetailComposite detail : details) {
			detail.setBusinessObject(object);
		}
	}
	
	public void aboutToOpen() {
		if (businessObject!=null) {
			final EClass eclass = businessObject.eClass();
			Point p = getShell().getSize();
			int width = preferenceStore.getInt("dialog."+eclass.getName()+".width");
			if (width==0)
				width = p.x;
			int height = preferenceStore.getInt("dialog."+eclass.getName()+".height");
			if (height==0)
				height = p.y;
			getShell().setSize(width,height);
			
			p = getShell().getLocation();
			int x = preferenceStore.getInt("dialog."+eclass.getName()+".x");
			if (x==0)
				x = p.x;
			int y = preferenceStore.getInt("dialog."+eclass.getName()+".y");
			if (y==0)
				y = p.y;
			getShell().setLocation(x,y);
	
			getShell().addControlListener(new ControlListener() {
				public void controlMoved(ControlEvent e)
				{
					Point p = getShell().getLocation();
					preferenceStore.setValue("dialog."+eclass.getName()+".x", p.x);
					preferenceStore.setValue("dialog."+eclass.getName()+".y", p.y);
				}
				
				public void controlResized(ControlEvent e)
				{
					Point p = getShell().getSize();
					preferenceStore.setValue("dialog."+eclass.getName()+".width", p.x);
					preferenceStore.setValue("dialog."+eclass.getName()+".height", p.y);
				}
		
			});
			form.reflow(true);
		}
	}
	
	public Composite getControl() {
		return control;
	}
}
