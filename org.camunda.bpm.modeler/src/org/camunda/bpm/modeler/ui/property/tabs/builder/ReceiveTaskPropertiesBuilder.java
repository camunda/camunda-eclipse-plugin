package org.camunda.bpm.modeler.ui.property.tabs.builder;

import java.util.List;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.ui.change.filter.FeatureChangeFilter;
import org.camunda.bpm.modeler.ui.change.filter.IsManyAttributeAnyChildChangeFilter;
import org.camunda.bpm.modeler.ui.property.tabs.binding.BaseElementIdComboBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.change.EAttributeChangeSupport;
import org.camunda.bpm.modeler.ui.property.tabs.binding.change.EObjectChangeSupport.ModelChangedEvent;
import org.camunda.bpm.modeler.ui.property.tabs.util.Events;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Message;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Builder for the receive task properties.
 * 
 * @author kristin.polenz
 */
public class ReceiveTaskPropertiesBuilder<T extends BaseElement> extends AbstractPropertiesBuilder<BaseElement> {

	private final EStructuralFeature ROOT_ELEMENTS_FEATURE = Bpmn2Package.eINSTANCE.getDefinitions_RootElements();

	private static final EStructuralFeature MESSAGE_REF_FEATURE = Bpmn2Package.eINSTANCE.getReceiveTask_MessageRef();
	private static final EStructuralFeature MESSAGE_NAME_FEATURE = Bpmn2Package.eINSTANCE.getMessage_Name();

	private static final String dropDownLabel = "Message";

	private final Class<Message> definitionCls = Message.class;

	/**
	 * Creates a new factory from the given arguments
	 * 
	 * @param parent
	 * @param section
	 * @param bo
	 */
	public ReceiveTaskPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		super(parent, section, bo);
	}

	/**
	 * Creates the receive task specific controls
	 */
	@Override
	public void create() {

		createDropDown();

		new DefinitionsPropertiesBuilder(parent, section, ModelUtil.getDefinitions(bo)).createMessageMappingsTable();
	}

	private void createDropDown() {

		final CCombo dropDown = PropertyUtil.createDropDown(section, parent, dropDownLabel);

		updateDropdownLabels(dropDown);

		Definitions definitions = ModelUtil.getDefinitions(bo);

		// register change support
		EAttributeChangeSupport changeSupport = new EAttributeChangeSupport(definitions, ROOT_ELEMENTS_FEATURE, dropDown);
		changeSupport.setFilter(new FeatureChangeFilter(definitions, ROOT_ELEMENTS_FEATURE).or(new IsManyAttributeAnyChildChangeFilter(definitions, ROOT_ELEMENTS_FEATURE)));

		changeSupport.register();

		dropDown.addListener(Events.MODEL_CHANGED, new Listener() {

			@Override
			public void handleEvent(Event e) {
				ModelChangedEvent event = (ModelChangedEvent) e;
				if (ROOT_ELEMENTS_FEATURE.equals(event.getFeature())) {
					Display.getCurrent().asyncExec(new Runnable() {
						public void run() {
							updateDropdownLabels(dropDown);
						}
					});
				}
			}
		});

		BaseElementIdComboBinding<T> modelViewBinding = new BaseElementIdComboBinding<T>(bo, MESSAGE_REF_FEATURE, dropDown) {

			@Override
			protected T getModelById(String label) {
				return getModelByLabel(label);
			}

			@Override
			protected String toString(T value) {
				if (value == null) {
					return "";
				} else {
					return getDefinitionLabel(value);
				}
			}
		};

		modelViewBinding.establish();
	}

	/**
	 * Returns the error definition with the given id
	 * in the context of this factory
	 * 
	 * @param id
	 * @return
	 */
	private T getModelByLabel(String id) {
		List<T> definitions = getDefinitions();
		for (T d: definitions) {
			String nodeId = getDefinitionLabel(d);
			if (nodeId.equals(id)) {
				return d;
			}
		}

		return null;
	}

	private List<T> getDefinitions() {
		Definitions definitions = ModelUtil.getDefinitions(bo);
		return (List<T>) ModelUtil.getAllRootElements(definitions, definitionCls);
	}

	private void updateDropdownLabels(CCombo dropDown) {

		List<T> definitions = getDefinitions();

		// We need to avoid that the combo box fires a SWT.Modify event 
		// while updating the drop down labels.

		// Doing so, we remove all drop down element one by one
		for (String s: dropDown.getItems()) {
			dropDown.remove(s);
		}

		// and add the labels again one by one
		dropDown.add("");

		for (T d: definitions) {
			dropDown.add(getDefinitionLabel(d));
		}

		T object = (T) bo.eGet(MESSAGE_REF_FEATURE);
		if (object != null && definitions.contains(object)) {
			dropDown.setText(getDefinitionLabel(object));
		} else {
			dropDown.setText("");
		}
	}

	private String getDefinitionLabel(T d) {
		return d.eGet(MESSAGE_NAME_FEATURE) + " (" + d.getId() + ")";
	}

}
