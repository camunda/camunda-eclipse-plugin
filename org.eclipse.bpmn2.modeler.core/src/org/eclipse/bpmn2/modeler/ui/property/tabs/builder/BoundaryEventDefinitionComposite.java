package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import java.util.List;

import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.bpmn2.modeler.ui.property.tabs.EventTabCompositeFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

public class BoundaryEventDefinitionComposite extends AbstractPropertiesBuilder<BoundaryEvent> {

	public BoundaryEventDefinitionComposite(Composite parent, GFPropertySection section, BoundaryEvent bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		List<EventDefinition> eventDefinitions = bo.getEventDefinitions();
		
		if (getEventDefinition(MessageEventDefinition.class, eventDefinitions) != null ||
			getEventDefinition(TimerEventDefinition.class, eventDefinitions) != null) {
				
			PropertyUtil.createCheckbox(section, parent, "Cancel Activity",
					Bpmn2Package.eINSTANCE.getBoundaryEvent_CancelActivity(), bo);
		}
	}
	
	/**
	 * FIXME: Condense with {@link EventTabCompositeFactory#getEventDefinition()}
	 * 
	 * @param cls
	 * @param eventDefinitions
	 * @return
	 */
	private <T extends EventDefinition> T getEventDefinition(Class<T> cls, List<EventDefinition> eventDefinitions) {
		for (EventDefinition eventDefinition : eventDefinitions) {
			if (cls.isInstance(eventDefinition)) {
				return cls.cast(eventDefinition);
			}
		}
		
		return null;
	}
}
