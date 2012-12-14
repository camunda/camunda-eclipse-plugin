package org.eclipse.bpmn2.modeler.ui.property.tabs;

import java.util.List;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Event tab composite factory
 * 
 * @author nico.rehwaldt
 */
public class EventTabCompositeFactory extends AbstractTabCompositeFactory<Event> {
	
	public EventTabCompositeFactory(GFPropertySection section, Composite parent) {
		super(section, parent);
	}
	
	@Override
	public Composite createCompositeForBusinessObject(Event event) {
		
		List<EventDefinition> eventDefinitions = ModelUtil.getEventDefinitions(event);
		
		for (EventDefinition eventDefinition: eventDefinitions) {
			
			if (eventDefinition instanceof TimerEventDefinition) {
				createTimerEventComposite((TimerEventDefinition) eventDefinition);
			}
		}
		
		return parent;
	}

	private void createTimerEventComposite(TimerEventDefinition eventDefinition) {
		
	}
}
