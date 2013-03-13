package org.camunda.bpm.modeler.ui.adapters.properties;

import org.camunda.bpm.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.camunda.bpm.modeler.core.adapters.ObjectDescriptor;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;

public class EventDefinitionPropertiesAdapter<T extends EventDefinition> extends ExtendedPropertiesAdapter<T> {

	public EventDefinitionPropertiesAdapter(AdapterFactory adapterFactory, T object) {
		super(adapterFactory, object);
		
		setObjectDescriptor(new ObjectDescriptor<T>(adapterFactory, object) {
			@Override
			public T createObject(Resource resource, Object context) {
				T rootElement = super.createObject(resource, context);
				return rootElement;
			}
		});
	}
}
