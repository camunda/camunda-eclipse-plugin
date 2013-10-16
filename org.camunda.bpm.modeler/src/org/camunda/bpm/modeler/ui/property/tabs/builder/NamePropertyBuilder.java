package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.ui.property.tabs.binding.StringTextBinding;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CallableElement;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LinkEventDefinition;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

/**
 * Builder for the name property
 * 
 * @author nico.rehwaldt
 */
public class NamePropertyBuilder extends AbstractPropertiesBuilder<BaseElement> {

	private static final int BOX_HEIGHT = 16;

	private EStructuralFeature NAME_FEATURE;
	
	private String label;
	private String helpText;

	public NamePropertyBuilder(Composite parent, GFPropertySection section, BaseElement bo, String label, String helpText) {
		this(parent, section, bo, label);

		this.helpText = helpText;
	}

	public NamePropertyBuilder(Composite parent, GFPropertySection section, BaseElement bo, String label) {
		super(parent, section, bo);
		
		this.label = label;

		if (bo instanceof FlowElement) {
			NAME_FEATURE = Bpmn2Package.eINSTANCE.getFlowElement_Name();
		} else 
		if (bo instanceof Collaboration) {
			NAME_FEATURE = Bpmn2Package.eINSTANCE.getCollaboration_Name();
		} else
		if (bo instanceof CallableElement) {
			NAME_FEATURE = Bpmn2Package.eINSTANCE.getCallableElement_Name();
		} else
		if (bo instanceof Participant) {
			NAME_FEATURE = Bpmn2Package.eINSTANCE.getParticipant_Name();
		} else 
		if (bo instanceof Lane) {
			NAME_FEATURE = Bpmn2Package.eINSTANCE.getLane_Name();
		} else
		if (bo instanceof MessageFlow) {
			NAME_FEATURE = Bpmn2Package.eINSTANCE.getMessageFlow_Name();
		} else
		if (bo instanceof LinkEventDefinition) {
			NAME_FEATURE = Bpmn2Package.eINSTANCE.getLinkEventDefinition_Name();
		} else {
			// Unsupported base element: Do nothing
		}
	}
	
	public NamePropertyBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		this(parent, section, bo, "Name");
	}
	
	@Override
	public void create() {
		if (NAME_FEATURE != null) {
			final Text multiText = createAutoResizingMultiText(section, parent, label, NAME_FEATURE, bo);
			if (helpText != null) {
				PropertyUtil.attachNoteWithLink(section, multiText, helpText);
			}
		}
	}

	private Text createAutoResizingMultiText(GFPropertySection section, final Composite parent, String label,
			EStructuralFeature feature, BaseElement bo) {
		
		Composite composite = PropertyUtil.createStandardComposite(section, parent);
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		
		final Text multiText = factory.createText(composite, "", SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL); //$NON-NLS-1$

		final FormData data = PropertyUtil.getStandardLayout();

		data.height = BOX_HEIGHT;
		multiText.setLayoutData(data);

		PropertyUtil.createLabel(section, composite, label, multiText);
		
		ISWTObservableValue multiTextObservable = SWTObservables.observeText(multiText, SWT.Modify);
		multiTextObservable.addValueChangeListener(new IValueChangeListener() {
			
			@Override
			public void handleValueChange(ValueChangeEvent event) {
				
				String text = (String) event.diff.getNewValue();
				int crCount = crCount(text);
				
				int newHeight = BOX_HEIGHT * (crCount + 1);
				if (newHeight != data.height) {
					data.height = newHeight;
					
					relayout();
				}
			}
		});

		StringTextBinding stringTextBinding = new StringTextBinding(bo, feature, multiText);
		stringTextBinding.establish();
		
		return multiText;
	}
	
	public int crCount(String s) {
		int i = -1;
		int c = 0;
		
		do {
			i = s.indexOf(SWT.CR, i + 1);
			if (i != -1) {
				c++;
			}
			
		} while (i != -1);
		
		return c;
	}
}
