package org.eclipse.bpmn2.modeler.core.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.bpmn2.AdHocSubProcess;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BusinessRuleTask;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.CompensateEventDefinition;
import org.eclipse.bpmn2.ComplexGateway;
import org.eclipse.bpmn2.ConditionalEventDefinition;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.ErrorEventDefinition;
import org.eclipse.bpmn2.EscalationEventDefinition;
import org.eclipse.bpmn2.EventBasedGateway;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.GatewayDirection;
import org.eclipse.bpmn2.InclusiveGateway;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.bpmn2.Property;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.bpmn2.SendTask;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.SignalEventDefinition;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;

public class BPMN2BatchValidationConstraint extends AbstractModelConstraint {

	List<IStatus> errorList = new ArrayList<IStatus>();
	IValidationContext context;

	public BPMN2BatchValidationConstraint() {
	}

	@Override
	public IStatus validate(IValidationContext ctx) {
		this.context = ctx;
		EObject eObj = ctx.getTarget();
		EMFEventType eType = ctx.getEventType();
		errorList.clear();

		// In the case of batch mode.
		if (eType == EMFEventType.NULL) {
			if (eObj instanceof BPMNDiagram) {
				return validateDiagram(ctx, (BPMNDiagram) eObj);
			}
			if (eObj instanceof Definitions) {
				return validateDefinitions(ctx, (Definitions) eObj);
			}
			if (eObj instanceof BaseElement) {
				return validateBaseElement(ctx, (BaseElement) eObj);
			}
		} else { // In the case of live mode.
			System.err.println("Live: " + getClass().getSimpleName() + ": " + eObj + ": " + ctx.getFeatureNewValue());
		}

		return ctx.createSuccessStatus();
	}

	private IStatus validateDiagram(IValidationContext ctx, BPMNDiagram obj) {
		Definitions defs = (Definitions) obj.eContainer();
		return validateDefinitions(ctx, defs);
	}

	private IStatus validateDefinitions(IValidationContext ctx, Definitions def) {
		List<RootElement> rootElements = def.getRootElements();
		for (RootElement root : rootElements) {
			if (root instanceof Process) {
				Process process = (Process) root;
				if (process.getFlowElements() != null && process.getFlowElements().size() > 0) {
					process.getFlowElements().get(0).getId();
				}

				if (isEmpty(process.getId())) {
					return ctx.createFailureStatus("Process has no id.");
				} else {
					if (!SyntaxCheckerUtils.isNCName(process.getId())) {
						return ctx.createFailureStatus(
								"Invalid process id. See http://www.w3.org/TR/REC-xml-names/#NT-NCName for more info.");
					}
				}

				String pname = null;
				Iterator<FeatureMap.Entry> iter = process.getAnyAttribute().iterator();
				boolean foundPackageName = false;
				while (iter.hasNext()) {
					FeatureMap.Entry entry = iter.next();
					if (entry.getEStructuralFeature().getName().equals("packageName")) {
						foundPackageName = true;
						pname = (String) entry.getValue();
						if (isEmpty(pname)) {
							return ctx.createFailureStatus("Process has no package name.");
						}
					}
				}
				if (!foundPackageName) {
					return ctx.createFailureStatus("Process has no package name.");
				}

				if (isEmpty(process.getName())) {
					return ctx.createFailureStatus("Process has no name.");
				}

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
					return ctx.createFailureStatus("Process has no Start node.");
				}
				if (!foundEndEvent) {
					return ctx.createFailureStatus("Process has no End node.");
				}
			}
		}
		return ctx.createSuccessStatus();
	}

	private IStatus validateBaseElement(IValidationContext ctx, BaseElement fe) {

		if (fe instanceof StartEvent) {
			StartEvent se = (StartEvent) fe;
			if (se.getOutgoing() == null || se.getOutgoing().size() < 1) {
				return ctx.createFailureStatus("Start node has no outgoing connections");
			}
		} else if (fe instanceof EndEvent) {
			EndEvent ee = (EndEvent) fe;
			if (ee.getIncoming() == null || ee.getIncoming().size() < 1) {
				return ctx.createFailureStatus("End node has no incoming connections");
			}
		} else {
			if (fe instanceof FlowNode) {
				FlowNode fn = (FlowNode) fe;
				if ((fn.getOutgoing() == null || fn.getOutgoing().size() < 1)) {
					return ctx.createFailureStatus("Node has no outgoing connections");
				}
				if ((fn.getIncoming() == null || fn.getIncoming().size() < 1)) {
					return ctx.createFailureStatus("Node has no incoming connections");
				}
			}
		}

		if (fe instanceof BusinessRuleTask) {
			BusinessRuleTask bt = (BusinessRuleTask) fe;
			Iterator<FeatureMap.Entry> biter = bt.getAnyAttribute().iterator();
			boolean foundRuleflowGroup = false;
			while (biter.hasNext()) {
				FeatureMap.Entry entry = biter.next();
				if (entry.getEStructuralFeature().getName().equals("ruleFlowGroup")) {
					foundRuleflowGroup = true;
					String ruleflowGroup = (String) entry.getValue();
					if (isEmpty(ruleflowGroup)) {
						return ctx.createFailureStatus("Business Rule Task has no ruleflow-group.");
					}
				}
			}
			if (!foundRuleflowGroup) {
				return ctx.createFailureStatus("Business Rule Task has no ruleflow-group.");
			}
		}

		if (fe instanceof ScriptTask) {
			ScriptTask st = (ScriptTask) fe;
			if (isEmpty(st.getScript())) {
				return ctx.createFailureStatus("Script Task has no script.");
			}
			if (isEmpty(st.getScriptFormat())) {
				return ctx.createFailureStatus("Script Task has no script format.");
			}
		}

		if (fe instanceof SendTask) {
			SendTask st = (SendTask) fe;
			if (st.getOperationRef() == null) {
				return ctx.createFailureStatus("Send Task has no operation.");
			}
			if (st.getMessageRef() == null) {
				return ctx.createFailureStatus("Send Task has no message.");
			}
		}

		if (fe instanceof UserTask) {
			UserTask ut = (UserTask) fe;
			String taskName = null;
			Iterator<FeatureMap.Entry> utiter = ut.getAnyAttribute().iterator();
			boolean foundTaskName = false;
			while (utiter.hasNext()) {
				FeatureMap.Entry entry = utiter.next();
				if (entry.getEStructuralFeature().getName().equals("taskName")) {
					foundTaskName = true;
					taskName = (String) entry.getValue();
					if (isEmpty(taskName)) {
						return ctx.createFailureStatus("User Task has no task name.");
					}
				}
			}
			if (!foundTaskName) {
				return ctx.createFailureStatus("User Task has no task name.");
			}

			// simulation validation
			if (ut.getExtensionValues() != null && fe.getExtensionValues().size() > 0) {
				boolean foundStaffAvailability = false;
				for (ExtensionAttributeValue extattrval : fe.getExtensionValues()) {
					FeatureMap extensionElements = extattrval.getValue();
				}
				if (!foundStaffAvailability) {
					return ctx.createFailureStatus("User Task has no staff availability defined.");
				}
			}
		}

		if (fe instanceof Task) {
			Task ta = (Task) fe;
			// simulation validation
			if (ta.getExtensionValues() != null && fe.getExtensionValues().size() > 0) {
				boolean foundDistributionType = false;
				String distributionTypeValue = "";
				boolean foundDuration = false;
				boolean foundTimeUnits = false;
				boolean foundRange = false;
				boolean foundStandardDeviation = false;
				for (ExtensionAttributeValue extattrval : fe.getExtensionValues()) {
					FeatureMap extensionElements = extattrval.getValue();

				}
				if (!foundDistributionType) {
					return ctx.createFailureStatus("Task has no distribution type defined.");
				}
				if (!foundDuration) {
					return ctx.createFailureStatus("Task has no duration defined.");
				}
				if (!foundTimeUnits) {
					return ctx.createFailureStatus( "Task has no Time Units defined.");
				}
				if (foundDistributionType) {
					if ((distributionTypeValue.equals("random") || distributionTypeValue.equals("uniform"))
							&& !foundRange) {
						return ctx.createFailureStatus("Task has no Range defined.");
					}
					if (distributionTypeValue.equals("normal") && !foundStandardDeviation) {
						return ctx.createFailureStatus("Task has no Standard Deviation defined.");
					}
				}
			}
		}

		if (fe instanceof CatchEvent) {
			CatchEvent event = (CatchEvent) fe;
			List<EventDefinition> eventdefs = event.getEventDefinitions();
			for (EventDefinition ed : eventdefs) {
				if (ed instanceof TimerEventDefinition) {
					TimerEventDefinition ted = (TimerEventDefinition) ed;
					boolean gotTimerDef = (ted.getTimeDate() != null || ted.getTimeDuration() != null || ted
							.getTimeCycle() != null);
					if (!gotTimerDef) {
						return ctx.createFailureStatus("Catch Event has no time value.");
					}
				} else if (ed instanceof SignalEventDefinition) {
					if (((SignalEventDefinition) ed).getSignalRef() == null) {
						return ctx.createFailureStatus("Catch Event has no signalref.");
					}
				} else if (ed instanceof ErrorEventDefinition) {
					if (((ErrorEventDefinition) ed).getErrorRef() == null
							|| ((ErrorEventDefinition) ed).getErrorRef().getErrorCode() == null) {
						return ctx.createFailureStatus("Catch Event has no errorref.");
					}
				} else if (ed instanceof ConditionalEventDefinition) {
					FormalExpression conditionalExp = (FormalExpression) ((ConditionalEventDefinition) ed)
							.getCondition();
					if (conditionalExp.getBody() == null) {
						return ctx.createFailureStatus("Catch Event has no condition expression.");
					}
				} else if (ed instanceof EscalationEventDefinition) {
					if (((EscalationEventDefinition) ed).getEscalationRef() == null) {
						return ctx.createFailureStatus("Catch Event has no escalationref.");
					}
				} else if (ed instanceof MessageEventDefinition) {
					if (((MessageEventDefinition) ed).getMessageRef() == null) {
						return ctx.createFailureStatus("Catch Event has no messageref.");
					}
				} else if (ed instanceof CompensateEventDefinition) {
					if (((CompensateEventDefinition) ed).getActivityRef() == null) {
						return ctx.createFailureStatus("Catch Event has no activityref.");
					}
				}
			}
		}

		if (fe instanceof ThrowEvent) {
			ThrowEvent event = (ThrowEvent) fe;
			List<EventDefinition> eventdefs = event.getEventDefinitions();
			for (EventDefinition ed : eventdefs) {
				if (ed instanceof TimerEventDefinition) {
					TimerEventDefinition ted = (TimerEventDefinition) ed;
					if (ted.getTimeDate() == null) {
						return ctx.createFailureStatus("Throw Event has no timedate.");
					}
					if (ted.getTimeDuration() == null) {
						return ctx.createFailureStatus("Throw Event has no timeduration.");
					}
					if (ted.getTimeCycle() != null) {
						return ctx.createFailureStatus("Throw Event has no timecycle.");
					}
				} else if (ed instanceof SignalEventDefinition) {
					if (((SignalEventDefinition) ed).getSignalRef() == null) {
						return ctx.createFailureStatus("Throw Event has no signalref.");
					}
				} else if (ed instanceof ErrorEventDefinition) {
					if (((ErrorEventDefinition) ed).getErrorRef() == null
							|| ((ErrorEventDefinition) ed).getErrorRef().getErrorCode() == null) {
						return ctx.createFailureStatus("Throw Event has no errorref.");
					}
				} else if (ed instanceof ConditionalEventDefinition) {
					FormalExpression conditionalExp = (FormalExpression) ((ConditionalEventDefinition) ed)
							.getCondition();
					if (conditionalExp.getBody() == null) {
						return ctx.createFailureStatus("Throw Event has no conditional expression.");
					}
				} else if (ed instanceof EscalationEventDefinition) {
					if (((EscalationEventDefinition) ed).getEscalationRef() == null) {
						return ctx.createFailureStatus("Throw Event has no conditional escalationref.");
					}
				} else if (ed instanceof MessageEventDefinition) {
					if (((MessageEventDefinition) ed).getMessageRef() == null) {
						return ctx.createFailureStatus("Throw Event has no conditional messageref.");
					}
				} else if (ed instanceof CompensateEventDefinition) {
					if (((CompensateEventDefinition) ed).getActivityRef() == null) {
						return ctx.createFailureStatus("Throw Event has no conditional activityref.");
					}
				}
			}
		}

		if (fe instanceof SequenceFlow) {
			SequenceFlow sf = (SequenceFlow) fe;
			if (sf.getSourceRef() == null) {
				return ctx.createFailureStatus("An Edge must have a source node.");
			}
			if (sf.getTargetRef() == null) {
				return ctx.createFailureStatus("An Edge must have a target node.");
			}
		}

		if (fe instanceof Gateway) {
			Gateway gw = (Gateway) fe;
			if (gw.getGatewayDirection() == null
					|| gw.getGatewayDirection().getValue() == GatewayDirection.UNSPECIFIED.getValue()) {
				return ctx.createFailureStatus("Gateway does not specify a valid direction.");
			}
			if (gw instanceof ExclusiveGateway) {
				if (gw.getGatewayDirection().getValue() != GatewayDirection.DIVERGING.getValue()
						&& gw.getGatewayDirection().getValue() != GatewayDirection.CONVERGING.getValue()) {
					return ctx.createFailureStatus(
							"Invalid Gateway direction for Exclusing Gateway. It should be 'Converging' or 'Diverging'.");
				}
			}
			if (gw instanceof EventBasedGateway) {
				if (gw.getGatewayDirection().getValue() != GatewayDirection.DIVERGING.getValue()) {
					return ctx.createFailureStatus(
							"Invalid Gateway direction for EventBased Gateway. It should be 'Diverging'.");
				}
			}
			if (gw instanceof ParallelGateway) {
				if (gw.getGatewayDirection().getValue() != GatewayDirection.DIVERGING.getValue()
						&& gw.getGatewayDirection().getValue() != GatewayDirection.CONVERGING.getValue()) {
					return ctx.createFailureStatus(
							"Invalid Gateway direction for Parallel Gateway. It should be 'Converging' or 'Diverging'.");
				}
			}
			if (gw instanceof InclusiveGateway) {
				if (gw.getGatewayDirection().getValue() != GatewayDirection.DIVERGING.getValue()) {
					return ctx.createFailureStatus(
							"Invalid Gateway direction for Inclusive Gateway. It should be 'Diverging'.");
				}
			}
			if (gw instanceof ComplexGateway) {
				if (gw.getGatewayDirection().getValue() != GatewayDirection.DIVERGING.getValue()
						&& gw.getGatewayDirection().getValue() != GatewayDirection.CONVERGING.getValue()) {
					return ctx.createFailureStatus(
							"Invalid Gateway direction for Complex Gateway. It should be 'Converging' or 'Diverging'.");
				}
			}
		}

		if (fe instanceof CallActivity) {
			CallActivity ca = (CallActivity) fe;
			if (ca.getCalledElementRef() == null) {
				return ctx.createFailureStatus(
						"Reusable Subprocess has no called element specified.");
			}
		}

		if (fe instanceof DataObject) {
			DataObject dao = (DataObject) fe;
			if (dao.getName() == null || dao.getName().length() < 1) {
				return ctx.createFailureStatus("Data Object has no name defined.");
			} else {
				if (containsWhiteSpace(dao.getName())) {
					return ctx.createFailureStatus("Data Object name contains white spaces.");
				}
			}
		}
		return ctx.createSuccessStatus();
	}

	private static boolean isEmpty(final CharSequence str) {
		if (str == null || str.length() == 0) {
			return true;
		}
		for (int i = 0, length = str.length(); i < length; i++) {
			if (str.charAt(i) != ' ') {
				return false;
			}
		}
		return true;
	}

	private boolean containsWhiteSpace(String testString) {
		if (testString != null) {
			for (int i = 0; i < testString.length(); i++) {
				if (Character.isWhitespace(testString.charAt(i))) {
					return true;
				}
			}
		}
		return false;
	}
}
