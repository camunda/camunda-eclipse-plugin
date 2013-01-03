package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import java.util.List;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.change.filter.FeatureChangeFilter;
import org.eclipse.bpmn2.modeler.ui.change.filter.NestedFeatureChangeFilter;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.BaseElementIdComboBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.change.EAttributeChangeSupport;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.change.EObjectChangeSupport.ModelChangedEvent;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.HelpText;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Builder for the error code / name property.
 * 
 * @author nico.rehwaldt
 */
public class DefinitionReferencePropertyBuilder<T extends RootElement> extends AbstractPropertiesBuilder<EventDefinition> {

	private final EStructuralFeature ROOT_ELEMENTS_FEATURE = Bpmn2Package.eINSTANCE.getDefinitions_RootElements();

	private final EStructuralFeature refFeature;
	private final EStructuralFeature nameFeature;
	
	private final String label;

	private final Class<T> definitionCls;

	private String note;

	public DefinitionReferencePropertyBuilder(
			Composite parent, GFPropertySection section, EventDefinition bo, 
			String label, EStructuralFeature refFeature, EStructuralFeature nameFeature, Class<T> definitionCls) {
		
		super(parent, section, bo);
		
		this.refFeature = refFeature;
		this.nameFeature = nameFeature;
		
		this.definitionCls = definitionCls;
		
		this.label = label;
		this.note = String.format(HelpText.ELEMENT_DEF_TABLE, new Object[] { definitionCls.getSimpleName() });
	}
	
	@Override
	public void create() {

		final CCombo dropDown = PropertyUtil.createDropDown(section, parent, label);
		
		if (note != null) {
			PropertyUtil.attachNote(dropDown, note);
		}
		
		updateDropdownLabels(dropDown);
		
		Definitions definitions = ModelUtil.getDefinitions(bo);
		
		// register change support
		EAttributeChangeSupport changeSupport = new EAttributeChangeSupport(definitions, ROOT_ELEMENTS_FEATURE, dropDown);
		changeSupport.setFilter(new FeatureChangeFilter(definitions, ROOT_ELEMENTS_FEATURE).or(new NestedFeatureChangeFilter(definitions, ROOT_ELEMENTS_FEATURE)));
		
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
		
		BaseElementIdComboBinding<T> modelViewBinding = new BaseElementIdComboBinding<T>(bo, refFeature, dropDown) {

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
		return ModelUtil.getAllRootElements(definitions, definitionCls);
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
		
		T object = (T) bo.eGet(refFeature);
		if (object != null && definitions.contains(object)) {
			dropDown.setText(getDefinitionLabel(object));
		} else {
			dropDown.setText("");
		}
	}
	
	private String getDefinitionLabel(T d) {
		return d.eGet(nameFeature) + "(" + d.getId() + ")";
	}
}
