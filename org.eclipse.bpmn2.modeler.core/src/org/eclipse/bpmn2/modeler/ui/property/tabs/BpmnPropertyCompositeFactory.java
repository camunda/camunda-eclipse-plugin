package org.eclipse.bpmn2.modeler.ui.property.tabs;

import java.util.List;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.AdHocSubProcess;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.FlowElement;
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
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.ProcessPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.RetryEnabledPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.ScriptTaskPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.SequenceFlowPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.ServiceTaskPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.StartEventPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.UserTaskPropertiesBuilder;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

public class BpmnPropertyCompositeFactory {
	
	private GFPropertySection section;
	private Composite parentComposite;

	public BpmnPropertyCompositeFactory(GFPropertySection section, Composite parent) {
		this.section = section;
		this.parentComposite = parent;
	}
	
	public Composite createCompositeForBpmnElement(EObject bo) {
		if (bo instanceof BaseElement) {
			createIdField((BaseElement) bo);
			
			createNameField((BaseElement) bo);
		}
		
		if (bo instanceof Participant) {
			Participant participant = (Participant) bo;
			createParticipantComposite(participant);
		}
		
		if (bo instanceof IntermediateCatchEvent || bo instanceof BoundaryEvent) {
			CatchEvent event = (CatchEvent) bo;
			if (isTimerEvent(event)) {
				createTimerCatchEventComposite(event);
			}
		}
		
		if (bo instanceof Activity) {
			createActivityComposite((Activity) bo);
		}
		
		if (bo instanceof Task) {
			createTaskComposite((Task) bo);
		}
		
		if (bo instanceof Event) {
			createEventComposite((Event) bo);
		}

		if (bo instanceof SequenceFlow) {
			createSequenceFlowComposite((SequenceFlow) bo);
		}

		if (bo instanceof Lane) {
			createLaneComposite((Lane) bo);
		}
		
		if (bo instanceof Process) {
			createProcessComposite((Process) bo);
		}		
		
		if (bo instanceof Gateway) {
			createGatewayComposite((Gateway) bo);
		}
		
		return parentComposite;
	}

	private void createParticipantComposite(Participant participant) {
		new ParticipantPropertiesBuilder(parentComposite, section, participant).create();
	}

	private void createLaneComposite(Lane lane) {
		new LanePropertiesBuilder(parentComposite, section, lane).create();
	}

	private void createTimerCatchEventComposite(CatchEvent bo) {
		new RetryEnabledPropertiesBuilder(parentComposite, section, bo).create();
	}

	private void createGatewayComposite(Gateway bo) {
		if (bo instanceof ExclusiveGateway) {
			new DecisionGatewayPropertiesBuilder(parentComposite, section, bo).create();
		}
		
		if (bo instanceof InclusiveGateway) {
			new DecisionGatewayPropertiesBuilder(parentComposite, section, bo).create();
		}
	}

	private void createSequenceFlowComposite(SequenceFlow bo) {
		new SequenceFlowPropertiesBuilder(parentComposite, section, bo).create();
	}
	
	private void createProcessComposite(Process bo) {
		new ProcessPropertiesBuilder(parentComposite, section, bo).create();
	}
	
	private Composite createActivityComposite(Activity bo) {
		if (!(bo instanceof ManualTask) && !(bo instanceof AdHocSubProcess)) {
			new ActivityPropertiesBuilder(parentComposite, section, bo).create();
		}
		if (bo instanceof CallActivity) {
			new CallActivityPropertiesBuilder(parentComposite, section, (CallActivity) bo).create();
		}
		return parentComposite;
	}

	private boolean isTimerEvent(CatchEvent e) {
		EventDefinition timerEventDefinition = ModelUtil.getEventDefinition(e, TimerEventDefinition.class);
		return timerEventDefinition != null;
	}
	
	private Composite createTaskComposite(Task bo) {
		if (bo instanceof UserTask) {
			new UserTaskPropertiesBuilder(parentComposite, section, (UserTask) bo).create();
		} else if (bo instanceof ScriptTask) {
			new ScriptTaskPropertiesBuilder(parentComposite, section, (ScriptTask) bo).create();
		} else if (bo instanceof ServiceTask) {
			new ServiceTaskPropertiesBuilder(parentComposite, section, (ServiceTask) bo).create();
		}
		return parentComposite;
	}
	
	private Composite createEventComposite(Event bo) {
		if (bo instanceof StartEvent) {
			new StartEventPropertiesBuilder(parentComposite, section, (StartEvent) bo).create();
		}
		
		return parentComposite;
	}
	
	// default fields (ID / Name) ///////////////////////////////////
	
	private void createNameField(BaseElement bo) {
		new NamePropertyBuilder(parentComposite, section, bo).create();
	}
	
	private void createIdField(BaseElement bo) {
		new IdPropertyBuilder(parentComposite, section, bo).create();
	}
}
