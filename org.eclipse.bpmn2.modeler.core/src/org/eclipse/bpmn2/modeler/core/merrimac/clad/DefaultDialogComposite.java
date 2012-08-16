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
	
	public DefaultDialogComposite(Composite parent, EClass eclass, int style) {
		super(parent, eclass, style);
		
		setLayout(new FormLayout());
		
		ITabDescriptor[] tabDescriptors = getTabDescriptors();
		int detailsCount = getDetailsCount();
		
		if (detailsCount>0) {
			folder = new TabFolder(this, SWT.NONE);
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
						
						Composite body = form.getBody();
						TableWrapLayout tableWrapLayout = new TableWrapLayout();
						tableWrapLayout.numColumns = 1;
						tableWrapLayout.verticalSpacing = 1;
						tableWrapLayout.horizontalSpacing = 1;
						TableWrapData twd = new TableWrapData(TableWrapData.FILL_GRAB);
						body.setLayout(tableWrapLayout);
						body.setLayoutData(twd);
						
						section = (AbstractBpmn2PropertySection)sd.getSectionClass();
						AbstractDetailComposite detail = getDetail(section, body);
						detail.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
	
						form.setContent(body);
						
						tab.setText(td.getLabel());
						tab.setControl(form);
						details.add(detail);
					}
				}
			}
			control = folder;
			control.setBackground(parent.getBackground());
		}
		else if (section!=null) {
			control = section.createSectionRoot(parent,SWT.NONE);
		}
		else {
			control = PropertiesCompositeFactory.createDetailComposite(eclass.getInstanceClass(), parent, SWT.NONE);
		}
		
		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		control.setLayoutData(data);
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
	public void setData(Object object) {
		businessObject = (EObject)object;
		if (details!=null && details.size()>0) {
			for (AbstractDetailComposite detail : details) {
				detail.setBusinessObject(businessObject);
			}
		}
		else if (control instanceof AbstractDetailComposite) {
			((AbstractDetailComposite)control).setBusinessObject(businessObject);
		}
		
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
	
	@Override
	public void dispose() {
		if (details!=null) {
			for (AbstractDetailComposite detail : details) {
				detail.dispose();
			}
		}
		control.dispose();
		super.dispose();
	}
}
