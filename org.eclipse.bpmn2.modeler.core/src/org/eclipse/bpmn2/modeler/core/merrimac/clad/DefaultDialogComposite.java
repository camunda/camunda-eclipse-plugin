package org.eclipse.bpmn2.modeler.core.merrimac.clad;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.runtime.Bpmn2SectionDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.Bpmn2TabDescriptor;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptorProvider;

// TODO: create a tabbed composite that contains all of the sections normally displayed in the tabbed property sheet
public class DefaultDialogComposite extends AbstractDialogComposite {

	protected IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
	protected EObject businessObject;
	protected List<AbstractDetailComposite> details = new ArrayList<AbstractDetailComposite>();
	protected TabFolder folder;
	protected Composite control;
	protected ITabDescriptor[] tabDescriptors;
	protected AbstractBpmn2PropertySection section;
	protected EClass eclass;
	private ControlListener dialogListener;
	
	public DefaultDialogComposite(Composite parent, EClass eclass, int style) {
		super(parent, style);
		this.eclass = eclass;
		
		setLayout(new FillLayout());
		
		ITabDescriptor[] tabDescriptors = getTabDescriptors();
		int detailsCount = getDetailsCount();
		
		if (detailsCount>0) {
			folder = new TabFolder(parent, SWT.NONE);
			folder.setLayout(new FormLayout());
			folder.setBackground(parent.getBackground());
			
			int index = 0;
			for (ITabDescriptor td : tabDescriptors) {
				if (td instanceof Bpmn2TabDescriptor && !((Bpmn2TabDescriptor)td).isPopup()) {
					// exclude this tab if not intended for popup dialog
					continue;
				}
				for (Object o : td.getSectionDescriptors()) {
					if (o instanceof Bpmn2SectionDescriptor) {
						Bpmn2SectionDescriptor sd = (Bpmn2SectionDescriptor)o;
			
						TabItem tab = new TabItem(folder, SWT.NONE);
						ScrolledForm form = new ScrolledForm(folder, SWT.V_SCROLL);
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
						AbstractDetailComposite detail = getDetail(section, formBody);
						detail.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
	
						tab.setText(td.getLabel());
						tab.setControl(form);
						details.add(detail);
					}
				}
			}
			folder.setLayout(new FormLayout());
			control = folder;
			control.setBackground(parent.getBackground());
		}
		else if (section!=null) {
			control = section.createSectionRoot(parent,SWT.NONE);
		}
		else {
			control = PropertiesCompositeFactory.createDetailComposite(getBusinessObjectClass().getInstanceClass(), parent, SWT.NONE);
		}
		
	}

	public EClass getBusinessObjectClass() {
		return eclass;
	}
	
	protected ITabDescriptor[] getTabDescriptors() {
		if (tabDescriptors==null) {
			IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().getActivePart();
			ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
			ITabDescriptorProvider tabDescriptorProvider = (ITabDescriptorProvider)part.getAdapter(ITabDescriptorProvider.class);
			tabDescriptors = tabDescriptorProvider.getTabDescriptors(part,selection);
		}
		return tabDescriptors;
	}
	
	protected int getDetailsCount() {
		int detailsCount = 0;
		ITabDescriptor[] tabDescriptors = getTabDescriptors();
		for (ITabDescriptor td : tabDescriptors) {
			if (td instanceof Bpmn2TabDescriptor && !((Bpmn2TabDescriptor)td).isPopup()) {
				// exclude this tab if not intended for popup dialog
				continue;
			}
			
			for (Object o : td.getSectionDescriptors()) {
				if (o instanceof Bpmn2SectionDescriptor) {
					Bpmn2SectionDescriptor sd = (Bpmn2SectionDescriptor)o;
					section = (AbstractBpmn2PropertySection)sd.getSectionClass();
					++detailsCount;
				}
			}
		}
		return detailsCount;
	}
	
	protected AbstractDetailComposite getDetail(AbstractBpmn2PropertySection section, Composite parent) {
		return section.createSectionRoot(parent,SWT.NONE);
	}

	@Override
	public void setBusinessObject(EObject object) {
		businessObject = object;
		if (details!=null && details.size()>0) {
			for (AbstractDetailComposite detail : details) {
				detail.setBusinessObject(object);
			}
		}
		else if (control instanceof AbstractDetailComposite) {
			((AbstractDetailComposite)control).setBusinessObject(object);
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
			addDialogListener();
			
			if (folder!=null) {
				int i = preferenceStore.getInt("dialog."+eclass.getName()+".tab");
				if (i>=0 && i<folder.getItemCount())
					folder.setSelection(i);
				folder.addSelectionListener( new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						int i = folder.getSelectionIndex();
						preferenceStore.setValue("dialog."+eclass.getName()+".tab", i);
					}
				});
				
				if (details!=null) {
					List<TabItem> removedTabs = new ArrayList<TabItem>();
					List<AbstractDetailComposite> removedDetails = new ArrayList<AbstractDetailComposite>();
					for (i=0; i<details.size(); ++i) {
						AbstractDetailComposite detail = details.get(i);
						if (detail.getChildren().length==0) {
							removedTabs.add(folder.getItem(i));
							removedDetails.add(detail);
						}
					}
					for (TabItem tab : removedTabs) {
						tab.dispose();
					}
					details.removeAll(removedDetails);
				}
			}
		}
	}
	
	public Composite getControl() {
		return control;
	}
	
	@Override
	public void dispose() {
		removeDialogListener();
		if (details!=null) {
			for (AbstractDetailComposite detail : details) {
				detail.dispose();
			}
		}
		control.dispose();
		super.dispose();
	}

	private void addDialogListener() {
		if (dialogListener==null) {
			dialogListener = new ControlListener() {
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
		
			};
			getShell().addControlListener(dialogListener);
		}
	}
	
	private void removeDialogListener() {
		if (dialogListener!=null) {
			try {
				getShell().removeControlListener(dialogListener);
			}
			catch(Exception e) {}
			dialogListener = null;
		}
	}
}
