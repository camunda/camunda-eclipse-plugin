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
import org.eclipse.bpmn2.ManualTask;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.bpmn2.modeler.ui.property.tabs.factories.ActivityPropertiesFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.factories.CallActivityPropertiesFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.factories.DecisionGatewayPropertiesFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.factories.DiagramPropertiesFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.factories.ProcessPropertiesFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.factories.RetryEnabledPropertiesFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.factories.ScriptTaskPropertiesFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.factories.SequenceFlowPropertiesFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.factories.ServiceTaskPropertiesFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.factories.StartEventPropertiesFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.factories.UserTaskPropertiesFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.dd.di.Diagram;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class BpmnPropertyCompositeFactory {
	
	private GFPropertySection section;
	private Composite parentComposite;

	public BpmnPropertyCompositeFactory(GFPropertySection section, Composite parent) {
		this.section = section;
		this.parentComposite = parent;
	}
	
	public Composite createCompositeForBpmnElement(EObject bo) {
		if (bo instanceof Diagram) {
			createDiagramComposite((Diagram) bo);
			return parentComposite;
		}
		
		createIdField((BaseElement) bo);
		
		if (bo instanceof FlowElement) {
			createFlowElementComposite((FlowElement) bo);
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
		if (bo instanceof org.eclipse.bpmn2.Process) {
			createProcessComposite((org.eclipse.bpmn2.Process) bo);
		}		
		if (bo instanceof Gateway) {
			createGatewayComposite((Gateway) bo);
		}
		
		
		return parentComposite;
	}

	private void createTimerCatchEventComposite(CatchEvent bo) {
		new RetryEnabledPropertiesFactory(parentComposite, section, bo).create();
	}

	private void createGatewayComposite(Gateway bo) {
		if (bo instanceof ExclusiveGateway) {
			new DecisionGatewayPropertiesFactory(parentComposite, section, bo).create();
		}
		
		if (bo instanceof InclusiveGateway) {
			new DecisionGatewayPropertiesFactory(parentComposite, section, bo).create();
		}
	}

	private Composite createDiagramComposite(Diagram bo) {
		new DiagramPropertiesFactory(parentComposite, section, bo).create();
		
		return parentComposite;
	}

	private void createSequenceFlowComposite(SequenceFlow bo) {
		new SequenceFlowPropertiesFactory(parentComposite, section, bo).create();
	}
	
	private void createProcessComposite(org.eclipse.bpmn2.Process bo) {
		new ProcessPropertiesFactory(parentComposite, section, bo).create();
	}
	
	private Composite createActivityComposite(Activity bo) {
		if (!(bo instanceof ManualTask) && !(bo instanceof AdHocSubProcess)) {
			new ActivityPropertiesFactory(parentComposite, section, bo).create();
		}
		if (bo instanceof CallActivity) {
			new CallActivityPropertiesFactory(parentComposite, section, bo).create();
		}
		return parentComposite;
	}

	private boolean isTimerEvent(CatchEvent e) {
		List<EventDefinition> eventDefinitions = e.getEventDefinitions();
		
		for (EventDefinition eventDefinition : eventDefinitions) {
			if (eventDefinition.eClass().equals(Bpmn2Package.eINSTANCE.getTimerEventDefinition())) {
				return true;
			}
		}
		
		return false;
	}
	
	private Composite createTaskComposite(Task bo) {
		if (bo instanceof UserTask) {
			new UserTaskPropertiesFactory(parentComposite, section, bo).create();
		} else if (bo instanceof ScriptTask) {
			new ScriptTaskPropertiesFactory(parentComposite, section, bo).create();
		} else if (bo instanceof ServiceTask) {
			new ServiceTaskPropertiesFactory(parentComposite, section, bo).create();
		}
		return parentComposite;
	}
	
	private Composite createEventComposite(Event bo) {
		if (bo instanceof StartEvent) {
			new StartEventPropertiesFactory(parentComposite, section, bo).create();
		}
		return parentComposite;
	}
	
	// default fields (ID / Name) ///////////////////////////////////
	
	private Text createFlowElementComposite(final FlowElement bo) {
		return PropertyUtil.createText(section, parentComposite, "Name", Bpmn2Package.eINSTANCE.getFlowElement_Name(), bo);
	}
	
	private Text createIdField(final BaseElement bo) {
		Text idText = PropertyUtil.createText(section, parentComposite, "Id", Bpmn2Package.eINSTANCE.getBaseElement_Id(), bo);
		
		if (bo instanceof Process) {
			// we keep id enabled for process
		} else {
			// FIXME: Id change is not properly propagated
			idText.setEnabled(false);
		}
		
		return idText;
	}

}
