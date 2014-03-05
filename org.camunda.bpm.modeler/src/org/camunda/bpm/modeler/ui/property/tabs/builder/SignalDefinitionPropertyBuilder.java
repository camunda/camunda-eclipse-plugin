package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.property.tabs.binding.BooleanButtonBinding;
import org.camunda.bpm.modeler.ui.property.tabs.util.HelpText;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.camunda.bpm.modeler.ui.util.Browser;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Signal;
import org.eclipse.bpmn2.SignalEventDefinition;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

/**
 * Builder for the signal property.
 * 
 * @author nico.rehwaldt
 */
public class SignalDefinitionPropertyBuilder extends DefinitionReferencePropertyBuilder<Signal> {

	private static final EStructuralFeature SIGNAL_REF_FEATURE = Bpmn2Package.eINSTANCE.getSignalEventDefinition_SignalRef();
	private static final EStructuralFeature SIGNAL_NAME_FEATURE = Bpmn2Package.eINSTANCE.getSignal_Name();

	private static final EStructuralFeature ASYNC_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_Async();

	public SignalDefinitionPropertyBuilder(Composite parent, GFPropertySection section, SignalEventDefinition bo) {
		super(parent, section, bo, "Signal", SIGNAL_REF_FEATURE, SIGNAL_NAME_FEATURE, null, Signal.class);
	}

	@Override
	public void create() {
		// create signal ref
		super.create();

		// create message definitions table
		createDefinitionsTable();

		if (bo.eContainer() instanceof ThrowEvent) {
			Button asyncCheckbox = createAsyncCheckbox();
			new BooleanButtonBinding(bo, ASYNC_FEATURE, asyncCheckbox).establish();
		}
	}

	private void createDefinitionsTable() {
		new DefinitionsPropertiesBuilder(parent, section, ModelUtil.getDefinitions(bo)).createSignalMappingsTable();
	}

	/**
	 *
	 * @return
	 */
	private Button createAsyncCheckbox() {

		Composite composite = PropertyUtil.createStandardComposite(section, parent);

		Composite asyncComposite = PropertyUtil.createStandardComposite(section, composite);
		asyncComposite.setLayoutData(PropertyUtil.getStandardLayout());

		// add label
		PropertyUtil.createLabel(section, composite, "Asynchronous", asyncComposite);

		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		final Button checkbox = factory.createButton(asyncComposite, "", SWT.CHECK);

		FormData checkboxFormData = new FormData();
		checkboxFormData.top = new FormAttachment(0);
		checkboxFormData.left = new FormAttachment(0);

		checkbox.setLayoutData(checkboxFormData);

		Link link = new Link(asyncComposite, SWT.NO_BACKGROUND | SWT.NO_FOCUS);
		link.setText(HelpText.SIGNAL_THROW_EVENT_ASYNC_FLAG);

		factory.adapt(link, false, false);

		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Browser.open(e.text);
			}
		});

		FormData helpTextFormData = new FormData();
		helpTextFormData.left = new FormAttachment(checkbox, 0);
		helpTextFormData.right = new FormAttachment(100, 0);

		link.setLayoutData(helpTextFormData);

		return checkbox;
	}
}
