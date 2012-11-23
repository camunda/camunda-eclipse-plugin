package org.eclipse.bpmn2.modeler.ui.property.tabs;

import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

public class PropertyUtil {
	
	public static Composite createText(GFPropertySection section, Composite parent, String label, final EStructuralFeature feature, final EObject bo) {
		Composite composite = createStandardComposite(section, parent);
		
		String value = "";
		Object tmp = bo.eGet(feature);
		if (tmp != null && !(tmp instanceof String)) {
			value = String.valueOf(tmp);
		} else {
			value = (String) tmp;
		}
		
		final Text text = createSimpleText(section, composite, value);
		
		text.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent arg0) {
				PropertyUtil.setValue(bo, feature, text.getText());
			}
		});
		
		createLabel(section, composite, label, text);
		
		return composite;
	}
	
	public static Composite createMultiText(GFPropertySection section, Composite parent, String label, final EStructuralFeature feature, final EObject bo) {
		Composite composite = createStandardComposite(section, parent);
		
		String value = "";
		Object tmp = bo.eGet(feature);
		if (tmp != null && !(tmp instanceof String)) {
			value = String.valueOf(tmp);
		} else {
			value = (String) tmp;
		}
		
		final Text text = createSimpleMultiText(section, composite, value);
		
		text.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent arg0) {
				PropertyUtil.setValue(bo, feature, text.getText());
			}
		});
		
		createLabel(section, composite, label, text);
		
		return composite;
	}
	
	public static Text createSimpleText(GFPropertySection section, Composite parent, String value) {
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		final Text text = factory.createText(parent, value); //$NON-NLS-1$
		setStandardLayout(text);
		
		return text;
	}
	
	public static Text createSimpleMultiText(GFPropertySection section, Composite parent, String value) {
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		final Text text = factory.createText(parent, value, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL); //$NON-NLS-1$

		FormData data = new FormData();
		data.left = new FormAttachment(0, 120);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		data.height = 106;
		text.setLayoutData(data);
		
		return text;
	}
	
	public static Composite createCheckbox(GFPropertySection section, Composite parent, String label, final EStructuralFeature feature, final EObject bo) {
		Composite composite = createStandardComposite(section, parent);
		
		final Button checkbox = createSimpleCheckbox(section, composite, (Boolean) bo.eGet(feature));
		
		checkbox.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				PropertyUtil.setValue(bo, feature, checkbox.getSelection());
			}
		});
		
		createLabel(section, composite, label, checkbox);
		
		return composite;
	}
	
	public static Button createSimpleCheckbox(GFPropertySection section, Composite parent, Boolean value) {
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		final Button checkbox = factory.createButton(parent, "", SWT.CHECK);
		setStandardLayout(checkbox);
		checkbox.setSelection(value);
		
		return checkbox;
	}
	
	public static CLabel createLabel(GFPropertySection section, Composite parent, String label, Control control) {
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		CLabel cLabel = factory.createCLabel(parent, label + ":"); //$NON-NLS-1$
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(control, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(control, 0, SWT.TOP);
		cLabel.setLayoutData(data);
		
		return cLabel;
	}
	
	private static void setStandardLayout(Control control) {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 120);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		control.setLayoutData(data);
	}
	
	public static boolean setValue(EObject bo, EStructuralFeature feature, Object value) {
		TransactionalEditingDomain domain = BPMN2Editor.getActiveEditor().getEditingDomain();
		return ModelUtil.setValue(domain, bo, feature, value);
	}

	public static Composite createStandardComposite(GFPropertySection section, Composite parent) {
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		Composite composite = factory.createFlatFormComposite(parent);
		
		composite.setLayoutData(new GridData(SWT.FILL, GridData.CENTER, true, false));
		
		return composite;
		
	}
	
}
