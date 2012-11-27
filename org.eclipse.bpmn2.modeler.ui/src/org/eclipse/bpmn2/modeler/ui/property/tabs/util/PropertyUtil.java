package org.eclipse.bpmn2.modeler.ui.property.tabs.util;

import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

public class PropertyUtil {
	
	public static Text createText(GFPropertySection section, Composite parent, String label, final EStructuralFeature feature, final EObject bo) {
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
		
		return text;
	}
	
	public static Text createMultiText(GFPropertySection section, Composite parent, String label, final EStructuralFeature feature, final EObject bo) {
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
		
		return text;
	}
	
	public static Text createSimpleText(GFPropertySection section, Composite parent, String value) {
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		final Text text = factory.createText(parent, value); //$NON-NLS-1$
		setStandardLayout(text);
		return text;
	}
	
	public static void createToolTipFor(Control element, String message) {
		
        final ToolTip tip = new ToolTip(element.getShell(), SWT.NONE);
        tip.setMessage(message);

        element.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {
                tip.setVisible(false);
            }

            @Override
            public void focusGained(FocusEvent e) {
                Text actionWidget = (Text) e.widget;
                
                Rectangle bounds = actionWidget.getBounds();
                
                Point loc = actionWidget.toDisplay(actionWidget.getLocation());
                
                tip.setLocation(loc.x - bounds.x, loc.y - bounds.y + bounds.height + actionWidget.getBorderWidth());
                tip.setVisible(true);
            }
        });
        
        element.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				tip.dispose();
			}
		});
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
		
		Object newValue = valueConvert(value, feature);
		
		return ModelUtil.setValue(domain, bo, feature, newValue);
	}

	private static Object valueConvert(Object value, EStructuralFeature feature) {
		
		EClassifier featureType = feature.getEType();
		if (featureType.isInstance(value)) {
			return value;
		}
		
		if (value instanceof String) {
			String str = (String) value;
			
			if (str.isEmpty()) {
				return null;
			}
			
			Class<?> instanceClass = featureType.getInstanceClass();
			if (instanceClass.equals(int.class) || instanceClass.equals(Integer.class)) {
				return Integer.parseInt(str, 10);
			} else 
			if (instanceClass.equals(long.class) || instanceClass.equals(Long.class)) {
				return Long.parseLong(str, 10);
			} else 
			if (instanceClass.equals(double.class) || instanceClass.equals(Double.class)) {
				return Double.parseDouble(str);
			} else 
			if (instanceClass.equals(float.class) || instanceClass.equals(Float.class)) {
				return Float.parseFloat(str);
			}
		}
		
		return value;
	}

	public static Composite createStandardComposite(GFPropertySection section, Composite parent) {
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		Composite composite = factory.createFlatFormComposite(parent);
		
		composite.setLayoutData(new GridData(SWT.FILL, GridData.CENTER, true, false));
		return composite;
	}

	public static Text createTextWithDatePicker(GFPropertySection section, Composite parent, String label, final EStructuralFeature feature, final EObject bo) {
		return createText(section, parent, label, feature, bo);
	}
}
