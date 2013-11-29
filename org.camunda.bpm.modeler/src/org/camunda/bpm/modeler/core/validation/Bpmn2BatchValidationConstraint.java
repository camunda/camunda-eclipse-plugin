package org.camunda.bpm.modeler.core.validation;

import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.CompensateEventDefinition;
import org.eclipse.bpmn2.ConditionalEventDefinition;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.Error;
import org.eclipse.bpmn2.ErrorEventDefinition;
import org.eclipse.bpmn2.Escalation;
import org.eclipse.bpmn2.EscalationEventDefinition;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.Message;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.bpmn2.SendTask;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.Signal;
import org.eclipse.bpmn2.SignalEventDefinition;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;

public class Bpmn2BatchValidationConstraint extends AbstractModelConstraint {
	
	public enum ValidationMode {
		ERRORS("org.camunda.bpm.modeler.validation.error"),
		WARNINGS("org.camunda.bpm.modeler.validation.warning"),
		OTHER(null);
		
		private final String id;

		private ValidationMode(String id) {
			this.id = id;
		}

		public String getId() {
			return id;
		}
		
		/**
		 * Return validation mode by id
		 * @param id
		 * @return
		 */
		public static ValidationMode getById(String id) {
			
			for (ValidationMode mode : values()) {
				if (id.equals(mode.id)) {
					return mode;
				}
			}
			
			return OTHER;
		}
	}
	
	private ValidationMode validationMode;
	
	public Bpmn2BatchValidationConstraint() {
	}

	@Override
	public IStatus validate(IValidationContext ctx) {
		EObject eObj = ctx.getTarget();
		EMFEventType eType = ctx.getEventType();
		String id = ctx.getCurrentConstraintId();
		
		validationMode = ValidationMode.getById(id);
		
		// only validate warnings (shows no error markers)
		if (validationMode != ValidationMode.WARNINGS) {
			return ctx.createSuccessStatus();
		}
		
		// In the case of batch mode.
		if (eType == EMFEventType.NULL) {
			if (eObj instanceof Definitions) {
				return validateDefinitions(ctx, (Definitions) eObj);
			}
			if (eObj instanceof BaseElement) {
				return validateBaseElement(ctx, (BaseElement) eObj);
			}
		} else { // In the case of live mode.
		}

		return ctx.createSuccessStatus();
	}

	private IStatus validateDefinitions(IValidationContext ctx, Definitions def) {
		List<RootElement> rootElements = def.getRootElements();
		for (RootElement root : rootElements) {
			if (root instanceof Process) {
				Process process = (Process) root;

				// report warnings only
				boolean foundStartEvent = false;
				boolean foundEndEvent = false;
				List<FlowElement> flowElements = process.getFlowElements();
				for (FlowElement fe : flowElements) {
					if (fe instanceof StartEvent) {
						foundStartEvent = true;
					}
					if (fe instanceof EndEvent) {
						foundEndEvent = true;
					}
				}
				if (!foundStartEvent) {
					ctx.addResult(root);
					return ctx.createFailureStatus("Process has no Start Event");
				}
				if (!foundEndEvent) {
					ctx.addResult(root);
					return ctx.createFailureStatus("Process has no End Event");
				}
				
				// report errors only
				if (isEmpty(process.getName())) {
					ctx.addResult(root);
					return ctx.createFailureStatus("Process has no name");
				}
			} else if (root instanceof Error) {
				if (((Error)root).getStructureRef()==null) {
					ctx.addResult(root);
					return ctx.createFailureStatus("Error has no type definition");
				}
			} else if (root instanceof Escalation) {
				if (((Escalation)root).getStructureRef()==null) {
					ctx.addResult(root);
					return ctx.createFailureStatus("Escalation has no type definition");
				}
			}
			else if (root instanceof Message) {
				if (((Message)root).getItemRef()==null) {
					ctx.addResult(root);
					return ctx.createFailureStatus("Message has no type definition");
				}
			} else if (root instanceof Signal) {
				if (((Signal)root).getStructureRef()==null) {
					ctx.addResult(root);
					return ctx.createFailureStatus("Signal has no type definition");
				}
			} else if (root instanceof ItemDefinition) {
				if (((ItemDefinition)root).getStructureRef()==null) {
					ctx.addResult(root);
					return ctx.createFailureStatus("Item Definition has no structure");
				}
			}
		}
		
		return ctx.createSuccessStatus();
	}

	private IStatus validateBaseElement(IValidationContext ctx, BaseElement fe) {

		if (fe instanceof Task) {
			String name = ((Task) fe).getName();
			if (name == null || name.isEmpty()) {
				return ctx.createFailureStatus("Task has no name");
			}
		}
		
		if (fe instanceof StartEvent) {
			StartEvent se = (StartEvent) fe;
			
			if (se.getOutgoing() == null || se.getOutgoing().size() < 1) {
				return ctx.createFailureStatus("Start Event has no outgoing connections");
			}
		} else if (fe instanceof EndEvent) {
			EndEvent ee = (EndEvent) fe;
			
			if (ee.getIncoming() == null || ee.getIncoming().size() < 1) {
				return ctx.createFailureStatus("End Event has no incoming connections");
			}
		}	else if (fe instanceof ScriptTask) {
			ScriptTask st = (ScriptTask) fe;
			
			if (isEmpty(st.getScript())) {
				return ctx.createFailureStatus("Script Task has no script");
			}
			if (isEmpty(st.getScriptFormat())) {
				return ctx.createFailureStatus("Script Task has no script format");
			}
		}	else if (fe instanceof SendTask) {
			SendTask st = (SendTask) fe;

//			if (st.getOperationRef() == null) {
//				return ctx.createFailureStatus("Send Task has no operation");
//			}
//			if (st.getMessageRef() == null) {
//				return ctx.createFailureStatus("Send Task has no message");
//			}
		}	else if (fe instanceof CatchEvent) {
			CatchEvent event = (CatchEvent) fe;

			List<EventDefinition> eventdefs = event.getEventDefinitions();
			if (eventdefs.isEmpty()) {
				return ctx.createFailureStatus("Catch Event has no event definitions");
			}
			
			for (EventDefinition ed : eventdefs) {
				if (ed instanceof TimerEventDefinition) {
					TimerEventDefinition ted = (TimerEventDefinition) ed;
					if (	ted.getTimeDate() == null
							&& ted.getTimeDuration() == null
							&& ted.getTimeCycle() == null
					) {
						return ctx.createFailureStatus("Timer Event has no timer definition");
					}
				} else if (ed instanceof SignalEventDefinition) {
					if (((SignalEventDefinition) ed).getSignalRef() == null) {
						return ctx.createFailureStatus("Signal Event has no signal definition");
					}
				} else if (ed instanceof ErrorEventDefinition) {
					if (((ErrorEventDefinition) ed).getErrorRef() == null
							|| ((ErrorEventDefinition) ed).getErrorRef().getErrorCode() == null) {
						return ctx.createFailureStatus("Error Event has no error definition");
					}
				} else if (ed instanceof ConditionalEventDefinition) {
					FormalExpression conditionalExp = (FormalExpression) ((ConditionalEventDefinition) ed)
							.getCondition();
					if (conditionalExp.getBody() == null) {
						return ctx.createFailureStatus("Conditional Event has no condition expression");
					}
				} else if (ed instanceof EscalationEventDefinition) {
					if (((EscalationEventDefinition) ed).getEscalationRef() == null) {
						return ctx.createFailureStatus("Escalation Event has no escalation definition");
					}
				} else if (ed instanceof MessageEventDefinition) {
					if (((MessageEventDefinition) ed).getMessageRef() == null) {
						return ctx.createFailureStatus("Message Event has no message definition");
					}
				} else if (ed instanceof CompensateEventDefinition) {
					if (((CompensateEventDefinition) ed).getActivityRef() == null) {
						return ctx.createFailureStatus("Compensate Event has no activity definition");
					}
				}
			}
		} else if (fe instanceof ThrowEvent) {
			ThrowEvent event = (ThrowEvent) fe;

			List<EventDefinition> eventdefs = event.getEventDefinitions();
//			if (eventdefs.isEmpty()) {
//				return ctx.createFailureStatus("Throw Event has no event definitions");
//			}

			for (EventDefinition ed : eventdefs) {
				if (ed instanceof TimerEventDefinition) {
					TimerEventDefinition ted = (TimerEventDefinition) ed;
					if (ted.getTimeDate() == null && ted.getTimeDuration() == null && ted.getTimeCycle() == null) {
						return ctx.createFailureStatus("Timer Event has no timer definition");
					}
				} else if (ed instanceof SignalEventDefinition) {
					if (((SignalEventDefinition) ed).getSignalRef() == null) {
						return ctx.createFailureStatus("Signal Event has no signal definition");
					}
				} else if (ed instanceof ErrorEventDefinition) {
					if (((ErrorEventDefinition) ed).getErrorRef() == null || ((ErrorEventDefinition) ed).getErrorRef().getErrorCode() == null) {
						return ctx.createFailureStatus("Error Event has no error definition");
					}
				} else if (ed instanceof ConditionalEventDefinition) {
					FormalExpression conditionalExp = (FormalExpression) ((ConditionalEventDefinition) ed).getCondition();
					if (conditionalExp.getBody() == null) {
						return ctx.createFailureStatus("Conditional Event has no condition expression");
					}
				} else if (ed instanceof EscalationEventDefinition) {
					if (((EscalationEventDefinition) ed).getEscalationRef() == null) {
						return ctx.createFailureStatus("Escalation Event has no conditional escalation definition");
					}
				} else if (ed instanceof MessageEventDefinition) {
					if (((MessageEventDefinition) ed).getMessageRef() == null) {
						return ctx.createFailureStatus("Message Event has no conditional message definition");
					}
				} else if (ed instanceof CompensateEventDefinition) {
					if (((CompensateEventDefinition) ed).getActivityRef() == null) {
						return ctx.createFailureStatus("Compensate Event has no conditional activity definition");
					}
				}
			}
		} else if (fe instanceof SequenceFlow) {
			SequenceFlow sf = (SequenceFlow) fe;

//			if (sf.getSourceRef() == null) {
//				return ctx.createFailureStatus("Sequence Flow is not connected to a source");
//			}
//			if (sf.getTargetRef() == null) {
//				return ctx.createFailureStatus("Sequence Flow is not connected to a target");
//			}
		} else if (fe instanceof Gateway) {
			Gateway gw = (Gateway) fe;
			
		}	else if (fe instanceof CallActivity) {
			CallActivity ca = (CallActivity) fe;

			if (ca.getCalledElementRef() == null) {
				return ctx.createFailureStatus("Call Activity does not specify called element");
			}
		}	else if (fe instanceof DataObject) {
			DataObject dao = (DataObject) fe;

//			if (dao.getName() == null || dao.getName().length() < 1) {
//				return ctx.createFailureStatus("Data Object has no name");
//			}
		}
		
		
		if (fe instanceof FlowNode) {
			return validateFlowNode(ctx, (FlowNode) fe);
		}

		return ctx.createSuccessStatus();
	}

	private IStatus validateFlowNode(IValidationContext ctx, FlowNode flowNode) {

//		if (flowNode instanceof CatchEvent && !(flowNode instanceof StartEvent)) {
//			if (flowNode.getOutgoing().isEmpty()) {
//				return ctx.createFailureStatus("CatchEvent has no outgoing connections");
//			}
//		}
//		
//		if (flowNode instanceof ThrowEvent && !(flowNode instanceof EndEvent)) {
//			if (flowNode.getIncoming().isEmpty()) {
//				return ctx.createFailureStatus("Node has no incoming connections");
//			}
//		}

		return ctx.createSuccessStatus();
	}

	private static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}
}
