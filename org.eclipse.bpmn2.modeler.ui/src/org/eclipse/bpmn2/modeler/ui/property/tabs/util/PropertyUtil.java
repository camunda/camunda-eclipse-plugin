package org.eclipse.bpmn2.modeler.ui.property.tabs.util;

import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.BooleanButtonBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.IntegerTextBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.StringTextBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.swt.Radio.RadioGroup;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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

/**
 * Using form data here for layouting.
 * 
 * To get to know how it works, refer to 
 * {@link http://www.eclipse.org/articles/article.php?file=Article-Understanding-Layouts/index.html}.
 * 
 * @author nico.rehwaldt
 */
public class PropertyUtil {
	
	public static final FormData STANDARD_LAYOUT;
	
	static {
		STANDARD_LAYOUT = new FormData();
		STANDARD_LAYOUT.left = new FormAttachment(0, 120);
		STANDARD_LAYOUT.right = new FormAttachment(100, 0);
		STANDARD_LAYOUT.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
	}
	
	public static Text createText(GFPropertySection section, Composite parent, String label, final EStructuralFeature feature, final EObject bo) {
		Text text = createUnboundText(section, parent, label);
		
		addBinding(text, bo, feature);
		
		return text;
	}

	public static Text createUnboundText(GFPropertySection section, Composite parent, String label) {
		Composite composite = createStandardComposite(section, parent);
		Text text = createSimpleText(section, composite, "");
		
		createLabel(section, composite, label, text);		
		return text;
	}
	
	public static Text createMultiText(GFPropertySection section, Composite parent, String label, final EStructuralFeature feature, final EObject bo) {
		Composite composite = createStandardComposite(section, parent);
		
		Text text = createSimpleMultiText(section, composite, "");
		
		new StringTextBinding(bo, feature, text).establish();
		
		createLabel(section, composite, label, text);
		
		return text;
	}

	protected static Button createSimpleCheckbox(GFPropertySection section, Composite parent) {
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		final Button checkbox = factory.createButton(parent, "", SWT.CHECK);
		setStandardLayout(checkbox);
		
		return checkbox;
	}
	
	public static Button createCheckbox(GFPropertySection section, Composite parent, String label, final EStructuralFeature feature, final EObject bo) {
		Button checkbox = createUnboundCheckbox(section, parent, label);
		
		new BooleanButtonBinding(bo, feature, checkbox).establish();
		
		return checkbox;
	}
	
	public static Button createUnboundCheckbox(GFPropertySection section, Composite parent, String label) {
		Composite composite = createStandardComposite(section, parent);
		
		Button checkbox = createSimpleCheckbox(section, composite);
		createLabel(section, composite, label, checkbox);
		
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
	
	public static ToolTip createToolTipFor(Control element, String message) {
		
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
        
        return tip;
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

	public static Text createRadioText(GFPropertySection section, Composite parent, String label, 
			final EStructuralFeature feature, RadioGroup<EStructuralFeature> radioGroup, final EObject bo) {

		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		
		Composite composite = createStandardComposite(section, parent);
		
		Composite radioComposite = createStandardComposite(section, composite);
		
		setStandardLayout(radioComposite);
		
		final Text text = factory.createText(radioComposite, "");
		final Button radioButton = factory.createButton(radioComposite, "", SWT.RADIO);
		
		FormData textFormData = new FormData();
		textFormData.left = new FormAttachment(0, 15);
		textFormData.right = new FormAttachment(100, 0);
		
		FormData radioButtonData = new FormData();
		radioButtonData.right = new FormAttachment(text, 0);
		radioButtonData.top = new FormAttachment (text, 0, SWT.CENTER);
		
		text.setLayoutData(textFormData);
		radioButton.setLayoutData(radioButtonData);
		
		// register with radio group
		radioGroup.add(radioButton, feature);
		
		radioButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				boolean selected = radioButton.getSelection();
				text.setEnabled(selected);
			}
		});
		
		createLabel(section, composite, label, radioComposite);
		
		addBinding(text, bo, feature);
		
		return text;
	}
	
	public static Text createRadioText1(GFPropertySection section, Composite parent, String label, 
			final EStructuralFeature feature, RadioGroup<EStructuralFeature> radioGroup, final EObject bo) {

		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		
		final Text text = createText(section, parent, label, feature, bo);
		Composite radioComposite = text.getParent();
		
		final CLabel clabel = (CLabel) radioComposite.getChildren()[1];
		
		final Button radioButton = factory.createButton(radioComposite, "", SWT.RADIO);

		FormData textFormData = (FormData) text.getLayoutData();
		
		FormData radioButtonData = new FormData();
		radioButtonData.right = new FormAttachment(text, 0);
		radioButtonData.top = new FormAttachment (text, 0, SWT.CENTER);
		radioButtonData.left = textFormData.left;
		
		// make space for the radio button
		textFormData.left = new FormAttachment(0, 120 + 15);
		
		FormData labelFormData = (FormData) clabel.getLayoutData();
		labelFormData.right = new FormAttachment(radioButton, -ITabbedPropertyConstants.HSPACE);
		labelFormData.top = new FormAttachment(radioButton, 0, SWT.CENTER);
		
		radioButton.setLayoutData(radioButtonData);
		
		// register with radio group
		radioGroup.add(radioButton, feature);
		
		radioButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				boolean selected = radioButton.getSelection();
				text.setEnabled(selected);
			}
		});
		
		return text;
	}
	
	protected static Text createSimpleText(GFPropertySection section, Composite parent, String value) {
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		final Text text = factory.createText(parent, value); //$NON-NLS-1$
		setStandardLayout(text);
		return text;
	}
	
	protected static Text createSimpleMultiText(GFPropertySection section, Composite parent, String value) {
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
	
	private static void setStandardLayout(Control control) {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 120);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		control.setLayoutData(data);
	}
	
	
	// binding ///////////////////////////////////////////
	
	private static void addBinding(Text text, EObject bo, EStructuralFeature feature) {

		EClassifier featureType = feature.getEType();

		Class<?> instanceClass = featureType.getInstanceClass();
		if (instanceClass.equals(int.class) || instanceClass.equals(Integer.class)) {
			new IntegerTextBinding(bo, feature, text).establish();
		} else {
			new StringTextBinding(bo, feature, text).establish();
		}
	}
	
}
