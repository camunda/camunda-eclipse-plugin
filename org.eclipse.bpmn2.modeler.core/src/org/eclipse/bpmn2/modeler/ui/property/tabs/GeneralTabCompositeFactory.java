package org.eclipse.bpmn2.modeler.ui.property.tabs;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.AdHocSubProcess;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.BusinessRuleTask;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.InclusiveGateway;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.ManualTask;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.ActivityPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.CallActivityPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.DecisionGatewayPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.IdPropertyBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.LanePropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.NamePropertyBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.ParticipantPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.ProcessIdPropertyBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.ProcessPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.RetryEnabledPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.ScriptTaskPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.SequenceFlowPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.ServiceTaskPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.ServiceTypeControlsPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.StartEventPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.SubProcessPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.UserTaskPropertiesBuilder;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * General tab composite factory
 * 
 * @author nico.rehwaldt
 */
public class GeneralTabCompositeFactory extends AbstractTabCompositeFactory<BaseElement> {
	
	public GeneralTabCompositeFactory(GFPropertySection section, Composite parent) {
		super(section, parent);
	}
	
	@Override
	public Composite createCompositeForBusinessObject(BaseElement businessObject) {
		createIdField((BaseElement) businessObject);
		createNameField((BaseElement) businessObject);
		
		if (businessObject instanceof Participant) {
			Participant participant = (Participant) businessObject;
			createParticipantComposite(participant);
		}
		
		if (businessObject instanceof Task) {
			createTaskComposite((Task) businessObject);
		}
		
		if (businessObject instanceof Activity) {
			createActivityComposite((Activity) businessObject);
		}
		
		if (businessObject instanceof Event) {
			createEventComposite((Event) businessObject);
		}

		if (businessObject instanceof SequenceFlow) {
			createSequenceFlowComposite((SequenceFlow) businessObject);
		}

		if (businessObject instanceof Lane) {
			createLaneComposite((Lane) businessObject);
		}
		
		if (businessObject instanceof Process) {
			createProcessComposite((Process) businessObject);
		}		
		
		if (businessObject instanceof Gateway) {
			createGatewayComposite((Gateway) businessObject);
		}
		
		return parent;
	}

	private void createParticipantComposite(Participant participant) {
		new ParticipantPropertiesBuilder(parent, section, participant).create();
	}

	private void createLaneComposite(Lane lane) {
		new LanePropertiesBuilder(parent, section, lane).create();
	}

	private void createTimerCatchEventComposite(CatchEvent bo) {
		new RetryEnabledPropertiesBuilder(parent, section, bo).create();
	}

	private void createGatewayComposite(Gateway gateway) {
		if (gateway instanceof ExclusiveGateway) {
			new DecisionGatewayPropertiesBuilder(parent, section, gateway).create();
		}
		
		if (gateway instanceof InclusiveGateway) {
			new DecisionGatewayPropertiesBuilder(parent, section, gateway).create();
		}
	}

	private void createSequenceFlowComposite(SequenceFlow sequenceFlow) {
		new SequenceFlowPropertiesBuilder(parent, section, sequenceFlow).create();
	}
	
	private void createProcessComposite(Process process) {
		new ProcessPropertiesBuilder(parent, section, process).create();
	}
	
	private void createActivityComposite(Activity activity) {
		if (!(activity instanceof ManualTask) && !(activity instanceof AdHocSubProcess)) {
			new ActivityPropertiesBuilder(parent, section, activity).create();
		}
		
		if (activity instanceof CallActivity) {
			new CallActivityPropertiesBuilder(parent, section, (CallActivity) activity).create();
		}
		
		if (activity instanceof SubProcess) {
			new SubProcessPropertiesBuilder(parent, section, (SubProcess) activity).create();
		}
	}

	private boolean isTimerEvent(CatchEvent e) {
		EventDefinition timerEventDefinition = ModelUtil.getEventDefinition(e, TimerEventDefinition.class);
		return timerEventDefinition != null;
	}
	
	private void createTaskComposite(Task task) {
		
		if (task instanceof ServiceTask || task instanceof BusinessRuleTask) {
			new ServiceTypeControlsPropertiesBuilder(parent, section, task).create();
		}
		
		if (task instanceof UserTask) {
			new UserTaskPropertiesBuilder(parent, section, (UserTask) task).create();
		} else
		
		if (task instanceof ScriptTask) {
			new ScriptTaskPropertiesBuilder(parent, section, (ScriptTask) task).create();
		} else
		
		if (task instanceof ServiceTask || task instanceof BusinessRuleTask) {
			new ServiceTaskPropertiesBuilder(parent, section, task).create();
		}
	}
	
	private void createEventComposite(Event event) {
		if (event instanceof StartEvent) {
			new StartEventPropertiesBuilder(parent, section, (StartEvent) event).create();
		}

		if (event instanceof IntermediateCatchEvent || event instanceof BoundaryEvent) {
			CatchEvent catchEvent = (CatchEvent) event;
			if (isTimerEvent(catchEvent)) {
				createTimerCatchEventComposite(catchEvent);
			}
		}
	}
	
	// default fields (ID / Name) ///////////////////////////////////
	
	private void createNameField(BaseElement baseElement) {
		new NamePropertyBuilder(parent, section, baseElement).create();
	}
	
	private void createIdField(BaseElement baseElement) {
		if (baseElement instanceof Process) {
			new ProcessIdPropertyBuilder(parent, section, (Process) baseElement).create();
		} else {
			new IdPropertyBuilder(parent, section, baseElement).create();
		}
	}
}
