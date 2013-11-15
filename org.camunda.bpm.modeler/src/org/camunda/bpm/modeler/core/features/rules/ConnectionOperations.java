package org.camunda.bpm.modeler.core.features.rules;

import static org.camunda.bpm.modeler.core.utils.BusinessObjectUtil.getBusinessObjectForPictogramElement;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.features.rules.RuleOperations.Algorithm;
import org.camunda.bpm.modeler.core.features.rules.RuleOperations.FromModelOperation;
import org.camunda.bpm.modeler.core.features.rules.RuleOperations.FromToModelOperation;
import org.camunda.bpm.modeler.core.features.rules.RuleOperations.Side;
import org.camunda.bpm.modeler.core.utils.AnchorUtil;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.CompensateEventDefinition;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.LinkEventDefinition;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.IReconnectionContext;
import org.eclipse.graphiti.features.context.impl.ReconnectionContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class ConnectionOperations {
	
	public static final String CONNECTION_TYPE = "CONNECTION_TYPE";
	
	private static ArrayList<ConnectAlgorithm<ICreateConnectionContext>> CREATE_CONNECTION;
	private static ArrayList<ConnectAlgorithm<IReconnectionContext>> RECONNECT_CONNECTION;
	
	static {
		CREATE_CONNECTION = new ArrayList<ConnectAlgorithm<ICreateConnectionContext>>();
		CREATE_CONNECTION.add(new CreateConnectionFromFlowNodeAlgorithm());
		CREATE_CONNECTION.add(new CreateConnectionToFlowNodeAlgorithm());
		CREATE_CONNECTION.add(new CreateConnectionFromInteractionNodeAlgorithm());
		CREATE_CONNECTION.add(new CreateConnectionToInteractionNodeAlgorithm());
		CREATE_CONNECTION.add(new CreateConnectionFromBaseElementAlgorithm());
		CREATE_CONNECTION.add(new CreateConnectionToBaseElementAlgorithm());
		CREATE_CONNECTION.add(new CreateConnectionFromItemAwareElementAlgorithm());
		CREATE_CONNECTION.add(new CreateConnectionToActivityOrThrowEventAlgorithm());
		CREATE_CONNECTION.add(new CreateConnectionFromActivityOrCatchEventAlgorithm());
		CREATE_CONNECTION.add(new CreateConnectionToItemAwareElementAlgorithm());
		
		RECONNECT_CONNECTION = new ArrayList<ConnectAlgorithm<IReconnectionContext>>();
		RECONNECT_CONNECTION.add(new ReconnectConnectionFromFlowNodeAlgorithm());
		RECONNECT_CONNECTION.add(new ReconnectConnectionToFlowNodeAlgorithm());
		RECONNECT_CONNECTION.add(new ReconnectConnectionFromInteractionNodeAlgorithm());
		RECONNECT_CONNECTION.add(new ReconnectConnectionToInteractionNodeAlgorithm());
		RECONNECT_CONNECTION.add(new ReconnectConnectionFromBaseElementAlgorithm());
		RECONNECT_CONNECTION.add(new ReconnectConnectionToBaseElementAlgorithm());
		RECONNECT_CONNECTION.add(new ReconnectConnectionFromItemAwareElementAlgorithm());
		RECONNECT_CONNECTION.add(new ReconnectConnectionToActivityOrThrowEventAlgorithm());
		RECONNECT_CONNECTION.add(new ReconnectConnectionFromActivityOrCatchEventAlgorithm());
		RECONNECT_CONNECTION.add(new ReconnectConnectionToItemAwareElementAlgorithm());
	}
	
	private static final EClass SEQUENCE_FLOW = Bpmn2Package.eINSTANCE.getSequenceFlow();
	private static final EClass MESSAGE_FLOW = Bpmn2Package.eINSTANCE.getMessageFlow();
	private static final EClass DATA_ASSOCIATION = Bpmn2Package.eINSTANCE.getDataAssociation();
	private static final EClass ASSOCIATION = Bpmn2Package.eINSTANCE.getAssociation();
	
	public static StartFormCreateConnectionOperation getStartFromConnectionCreateOperation(ICreateConnectionContext context) {
		ConnectAlgorithm<ICreateConnectionContext> from = null;
		
		for (ConnectAlgorithm<ICreateConnectionContext> algorithm : CREATE_CONNECTION) {
			if (algorithm.appliesTo(context)) {
				if (algorithm.getType() == Side.FROM) {
					from = algorithm;
				}
			}
		}

		return new StartFormCreateConnectionOperation(from);
	}
	
	public static CreateConnectionOperation getConnectionCreateOperation(ICreateConnectionContext context) {
		ConnectAlgorithm<ICreateConnectionContext> from = null;
		ConnectAlgorithm<ICreateConnectionContext> to = null;
		
		for (ConnectAlgorithm<ICreateConnectionContext> algorithm : CREATE_CONNECTION) {
			if (algorithm.appliesTo(context)) {
				switch (algorithm.getType()) {
				case FROM:
					from = algorithm;
					break;
				case TO:
					to = algorithm;
					break;
				}
			}
		}

		return new CreateConnectionOperation(from, to);
	}
	
	public static ReconnectConnectionOperation getConnectionReconnectOperation(IReconnectionContext context) {
		ConnectAlgorithm<IReconnectionContext> from = null;
		ConnectAlgorithm<IReconnectionContext> to = null;
		
		for (ConnectAlgorithm<IReconnectionContext> algorithm : RECONNECT_CONNECTION) {
			if (algorithm.appliesTo(context)) {
				switch (algorithm.getType()) {
				case FROM:
					from = algorithm;
					break;
				case TO:
					to = algorithm;
					break;
				}
			}
		}

		return new ReconnectConnectionOperation(from, to);
	}	
	
	public static class ReconnectConnectionOperation extends FromToModelOperation<IReconnectionContext> {

		public ReconnectConnectionOperation(ConnectAlgorithm<IReconnectionContext> from, ConnectAlgorithm<IReconnectionContext> to) {
			super(from, to);
		}
	}
	
	public static class CreateConnectionOperation extends FromToModelOperation<ICreateConnectionContext> {
		
		public CreateConnectionOperation(ConnectAlgorithm<ICreateConnectionContext> from, ConnectAlgorithm<ICreateConnectionContext> to) {
			super(from, to);
		}		
	
	}
	
	public static class StartFormCreateConnectionOperation extends FromModelOperation<ICreateConnectionContext> {
		
		public StartFormCreateConnectionOperation(ConnectAlgorithm<ICreateConnectionContext> from) {
			super(from);
		}		
	
	}
	
	public abstract static class ConnectAlgorithm<T extends IContext> implements Algorithm<T> {

		public abstract Side getType();
		
		public abstract EClass getSupportedConnectionType();
		
		protected abstract Object getSourceBusinessObject(T context);
		
		protected abstract Object getTargetBusinessObject(T context);
		
	}
	
	public abstract static class DefaultConnectAlgorithm<T extends IContext> extends ConnectAlgorithm<T> {
		
		@Override
		public void execute(T context) {
			// should not be called (yet), as execution is implemented in 
			// add connection features
			
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean appliesTo(T context) {
			Object connectionType = context.getProperty(CONNECTION_TYPE);
			
			return getSupportedConnectionType().isSuperTypeOf((EClass) connectionType);
		}
		
	}
	
	public abstract static class ConnectFromAlgorithm<T extends IContext> extends DefaultConnectAlgorithm<T> {
		
		@Override
		public Side getType() {
			return Side.FROM;
		}
		
	}
	
	// algorithm for sequence flows
	
	abstract static class ConnectFromFlowNodeAlgorithm<T extends IContext> extends ConnectFromAlgorithm<T> {
		
		@Override
		public boolean appliesTo(T context) {
			Object bo = getSourceBusinessObject(context);
			return bo != null && bo instanceof FlowNode && super.appliesTo(context);
		}
		
		@Override
		public boolean canExecute(T context) {
			Object source = getSourceBusinessObject(context);
			
			if (source instanceof IntermediateThrowEvent) {
				IntermediateThrowEvent throwEvent = (IntermediateThrowEvent) source;
				
				List<EventDefinition> eventDefinitions = throwEvent.getEventDefinitions();
				
				if (!eventDefinitions.isEmpty() && eventDefinitions.size() == 1) {
					EventDefinition eventDefinition = eventDefinitions.get(0);
					return !(eventDefinition instanceof LinkEventDefinition);
				}
			} else if (source instanceof BoundaryEvent) {
				BoundaryEvent boundaryEvent = (BoundaryEvent) source;
				
				List<EventDefinition> eventDefinitions = boundaryEvent.getEventDefinitions();
				
				if (!eventDefinitions.isEmpty() && eventDefinitions.size() == 1) {
					EventDefinition eventDefinition = eventDefinitions.get(0);
					return !(eventDefinition instanceof CompensateEventDefinition);
				}				
			}
			
			return !(source instanceof EndEvent);
		}
		
		@Override
		public EClass getSupportedConnectionType() {
			return SEQUENCE_FLOW;
		}
		
	}
	
	static class CreateConnectionFromFlowNodeAlgorithm extends ConnectFromFlowNodeAlgorithm<ICreateConnectionContext> {

		@Override
		protected Object getSourceBusinessObject(ICreateConnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}
		
		@Override
		protected Object getTargetBusinessObject(ICreateConnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
		
	}
	
	static class ReconnectConnectionFromFlowNodeAlgorithm extends ConnectFromFlowNodeAlgorithm<IReconnectionContext> {

		@Override
		protected Object getSourceBusinessObject(IReconnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}
		
		@Override
		protected Object getTargetBusinessObject(IReconnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
		
	}
	
	public abstract static class ConnectToAlgorithm<T extends IContext> extends DefaultConnectAlgorithm<T> {
		@Override
		public Side getType() {
			return Side.TO;
		}
		
	}
	
	static abstract class ConnectToFlowNodeAlgorithm<T extends IContext> extends ConnectToAlgorithm<T> {
		
		@Override
		public boolean appliesTo(T context) {
			Object bo = getTargetBusinessObject(context);
			return bo != null && bo instanceof FlowNode && super.appliesTo(context);
		}
		
		@Override
		public boolean canExecute(T context) {
			FlowNode source = (FlowNode) getSourceBusinessObject(context);
			FlowNode target = (FlowNode) getTargetBusinessObject(context);
			
			if (!isInSameContainer(source, target)) {
				return false;
			}
			
			if (target instanceof IntermediateCatchEvent) {
				IntermediateCatchEvent catchEvent = (IntermediateCatchEvent) target;
				List<EventDefinition> eventDefinitions = catchEvent.getEventDefinitions();
				
				if (!eventDefinitions.isEmpty() && eventDefinitions.size() == 1) {
					EventDefinition eventDefinition = eventDefinitions.get(0);
					return !(eventDefinition instanceof LinkEventDefinition);
				}
			}
			
			return !(target instanceof StartEvent) && !(target instanceof BoundaryEvent);
		}
		
		@Override
		public EClass getSupportedConnectionType() {
			return SEQUENCE_FLOW;
		}
		
		private boolean isInSameContainer(FlowNode source, FlowNode target) {
			return source.eContainer().equals(target.eContainer());
		}
	}
	
	static class CreateConnectionToFlowNodeAlgorithm extends ConnectToFlowNodeAlgorithm<ICreateConnectionContext> {

		@Override
		protected Object getSourceBusinessObject(ICreateConnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}
		
		@Override
		protected Object getTargetBusinessObject(ICreateConnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
		
	}
	
	static class ReconnectConnectionToFlowNodeAlgorithm extends ConnectToFlowNodeAlgorithm<IReconnectionContext> {

		@Override
		protected Object getSourceBusinessObject(IReconnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}
		
		@Override
		protected Object getTargetBusinessObject(IReconnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
	}
	
	// algorithm for message flows

	abstract static class ConnectFromInteractionNodeAlgorithm<T extends IContext> extends ConnectFromAlgorithm<T> {
		
		@Override
		public boolean appliesTo(T context) {
			Object bo = getSourceBusinessObject(context);
			return bo != null && bo instanceof InteractionNode && super.appliesTo(context);
		}
		
		@Override
		public boolean canExecute(T context) {
			Object source = getSourceBusinessObject(context);
			
			if (getParticipant((InteractionNode) source) == null) {
				return false;
			}
			
			if (source instanceof ThrowEvent) {
				ThrowEvent throwEvent = (ThrowEvent) source;
				List<EventDefinition> eventDefinitions = throwEvent.getEventDefinitions();
				
				if (!containsMessageEventDefinition(eventDefinitions)) {
					return false;
				}
				return true;
			}
			
			return source instanceof Task || source instanceof Participant;
		}
		
		@Override
		public EClass getSupportedConnectionType() {
			return MESSAGE_FLOW;
		}
		
	}
	
	static class CreateConnectionFromInteractionNodeAlgorithm extends ConnectFromInteractionNodeAlgorithm<ICreateConnectionContext> {

		@Override
		protected Object getSourceBusinessObject(ICreateConnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}

		@Override
		protected Object getTargetBusinessObject(ICreateConnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
		
	}
	
	static class ReconnectConnectionFromInteractionNodeAlgorithm extends ConnectFromInteractionNodeAlgorithm<IReconnectionContext> {

		@Override
		protected Object getSourceBusinessObject(IReconnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}
		
		@Override
		protected Object getTargetBusinessObject(IReconnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
	}
	
	static abstract class ConnectToInteractionNodeAlgorithm<T extends IContext> extends ConnectToAlgorithm<T> {
		
		@Override
		public boolean appliesTo(T context) {
			Object bo = getTargetBusinessObject(context);
			return bo != null && bo instanceof InteractionNode && super.appliesTo(context);
		}
		
		@Override
		public boolean canExecute(T context) {
			InteractionNode source = (InteractionNode) getSourceBusinessObject(context);
			InteractionNode target = (InteractionNode) getTargetBusinessObject(context);
			
			if (!isDifferentParticipant(source, target)) {
				return false;
			}
			
			if (target instanceof CatchEvent) {
				CatchEvent catchEvent = (CatchEvent) target;
				List<EventDefinition> eventDefinitions = catchEvent.getEventDefinitions();
				
				if (!containsMessageEventDefinition(eventDefinitions)) {
					return false;
				}
				return true;
			}
			
			return target instanceof Task || target instanceof Participant;
		}
		
		@Override
		public EClass getSupportedConnectionType() {
			return MESSAGE_FLOW;
		}
		
	}
	
	static class CreateConnectionToInteractionNodeAlgorithm extends ConnectToInteractionNodeAlgorithm<ICreateConnectionContext> {

		@Override
		protected Object getSourceBusinessObject(ICreateConnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}

		@Override
		protected Object getTargetBusinessObject(ICreateConnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
		
	}
	
	static class ReconnectConnectionToInteractionNodeAlgorithm extends ConnectToInteractionNodeAlgorithm<IReconnectionContext> {

		@Override
		protected Object getSourceBusinessObject(IReconnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}
		
		@Override
		protected Object getTargetBusinessObject(IReconnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
	}
	
	// algorithm for associations

	abstract static class ConnectFromBaseElementAlgorithm<T extends IContext> extends ConnectFromAlgorithm<T> {
		
		@Override
		public boolean appliesTo(T context) {
			Object bo = getSourceBusinessObject(context);
			return bo != null && bo instanceof BaseElement && super.appliesTo(context);
		}
		
		@Override
		public boolean canExecute(T context) {
			return true;
		}
		
		@Override
		public EClass getSupportedConnectionType() {
			return ASSOCIATION;
		}
		
	}
	
	static class CreateConnectionFromBaseElementAlgorithm extends ConnectFromBaseElementAlgorithm<ICreateConnectionContext> {

		@Override
		protected Object getSourceBusinessObject(ICreateConnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}

		@Override
		protected Object getTargetBusinessObject(ICreateConnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
		
	}
	
	static class ReconnectConnectionFromBaseElementAlgorithm extends ConnectFromBaseElementAlgorithm<IReconnectionContext> {
		
		@Override
		protected Object getSourceBusinessObject(IReconnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}
		
		@Override
		protected Object getTargetBusinessObject(IReconnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
	}
	
	static abstract class ConnectToBaseElementAlgorithm<T extends IContext> extends ConnectToAlgorithm<T> {
		
		@Override
		public boolean appliesTo(T context) {
			Object bo = getTargetBusinessObject(context);
			return bo != null && bo instanceof BaseElement && super.appliesTo(context);
		}
		
		@Override
		public boolean canExecute(T context) {
			BaseElement sourceBusinessObject = (BaseElement) getSourceBusinessObject(context);
			BaseElement targetBusinessObject = (BaseElement) getTargetBusinessObject(context);
			
			if (sourceBusinessObject.equals(targetBusinessObject)) {
				return false;
			}
			
			return targetBusinessObject != null;
		}
		
		@Override
		public EClass getSupportedConnectionType() {
			return ASSOCIATION;
		}
		
	}
	
	static class CreateConnectionToBaseElementAlgorithm extends ConnectToBaseElementAlgorithm<ICreateConnectionContext> {
		
		@Override
		protected Object getSourceBusinessObject(ICreateConnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}

		@Override
		protected Object getTargetBusinessObject(ICreateConnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
		
	}
	
	static class ReconnectConnectionToBaseElementAlgorithm extends ConnectToBaseElementAlgorithm<IReconnectionContext> {
		
		@Override
		protected Object getSourceBusinessObject(IReconnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}
		
		@Override
		protected Object getTargetBusinessObject(IReconnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
	}	
	
	// algorithm for data associations

	abstract static class ConnectFromItemAwareElementAlgorithm<T extends IContext> extends ConnectFromAlgorithm<T> {
		
		@Override
		public boolean appliesTo(T context) {
			Object bo = getSourceBusinessObject(context);
			return bo != null && bo instanceof ItemAwareElement && super.appliesTo(context);
		}
		
		@Override
		public boolean canExecute(T context) {
			return true;
		}
		
		@Override
		public EClass getSupportedConnectionType() {
			return DATA_ASSOCIATION;
		}
		
	}
	
	static class CreateConnectionFromItemAwareElementAlgorithm extends ConnectFromItemAwareElementAlgorithm<ICreateConnectionContext> {

		@Override
		protected Object getSourceBusinessObject(ICreateConnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}

		@Override
		protected Object getTargetBusinessObject(ICreateConnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
		
	}
	
	static class ReconnectConnectionFromItemAwareElementAlgorithm extends ConnectFromItemAwareElementAlgorithm<IReconnectionContext> {
		
		@Override
		protected Object getSourceBusinessObject(IReconnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}
		
		@Override
		protected Object getTargetBusinessObject(IReconnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
	}
	
	abstract static class ConnectFromActivityOrCatchEventAlgorithm<T extends IContext> extends ConnectFromAlgorithm<T> {
		
		@Override
		public boolean appliesTo(T context) {
			Object bo = getSourceBusinessObject(context);
			return bo != null && (bo instanceof Activity || bo instanceof CatchEvent) && super.appliesTo(context);
		}
		
		@Override
		public boolean canExecute(T context) {
			return true;
		}
		
		@Override
		public EClass getSupportedConnectionType() {
			return DATA_ASSOCIATION;
		}
		
	}
	
	static class CreateConnectionFromActivityOrCatchEventAlgorithm extends ConnectFromActivityOrCatchEventAlgorithm<ICreateConnectionContext> {

		@Override
		protected Object getSourceBusinessObject(ICreateConnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}

		@Override
		protected Object getTargetBusinessObject(ICreateConnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
		
	}
	
	static class ReconnectConnectionFromActivityOrCatchEventAlgorithm extends ConnectFromActivityOrCatchEventAlgorithm<IReconnectionContext> {
		
		@Override
		protected Object getSourceBusinessObject(IReconnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}
		
		@Override
		protected Object getTargetBusinessObject(IReconnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
	}
	
	static abstract class ConnectToItemAwareElementAlgorithm<T extends IContext> extends ConnectToAlgorithm<T> {
		
		@Override
		public boolean appliesTo(T context) {
			Object bo = getTargetBusinessObject(context);
			return bo != null && bo instanceof ItemAwareElement && super.appliesTo(context);
		}
		
		@Override
		public boolean canExecute(T context) {
			BaseElement source = (BaseElement) getSourceBusinessObject(context);
			return source instanceof Activity || source instanceof CatchEvent;
		}
		
		@Override
		public EClass getSupportedConnectionType() {
			return DATA_ASSOCIATION;
		}
		
	}
	
	static class CreateConnectionToItemAwareElementAlgorithm extends ConnectToItemAwareElementAlgorithm<ICreateConnectionContext> {
		
		@Override
		protected Object getSourceBusinessObject(ICreateConnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}

		@Override
		protected Object getTargetBusinessObject(ICreateConnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
		
	}
	
	static class ReconnectConnectionToItemAwareElementAlgorithm extends ConnectToItemAwareElementAlgorithm<IReconnectionContext> {
		
		@Override
		protected Object getSourceBusinessObject(IReconnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}
		
		@Override
		protected Object getTargetBusinessObject(IReconnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
	}	
	
	static abstract class ConnectToActivityOrThrowEventAlgorithm<T extends IContext> extends ConnectToAlgorithm<T> {
		
		@Override
		public boolean appliesTo(T context) {
			Object bo = getTargetBusinessObject(context);
			return bo != null && (bo instanceof Activity || bo instanceof ThrowEvent) && super.appliesTo(context);
		}
		
		@Override
		public boolean canExecute(T context) {
			BaseElement source = (BaseElement) getSourceBusinessObject(context);
			return source instanceof ItemAwareElement;
		}
		
		@Override
		public EClass getSupportedConnectionType() {
			return DATA_ASSOCIATION;
		}
		
	}
	
	static class CreateConnectionToActivityOrThrowEventAlgorithm extends ConnectToActivityOrThrowEventAlgorithm<ICreateConnectionContext> {
		
		@Override
		protected Object getSourceBusinessObject(ICreateConnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}

		@Override
		protected Object getTargetBusinessObject(ICreateConnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
		
	}
	
	static class ReconnectConnectionToActivityOrThrowEventAlgorithm extends ConnectToActivityOrThrowEventAlgorithm<IReconnectionContext> {
		
		@Override
		protected Object getSourceBusinessObject(IReconnectionContext context) {
			return getSourceBusinessObjectFromContext(context);
		}
		
		@Override
		protected Object getTargetBusinessObject(IReconnectionContext context) {
			return getTargetBusinessObjectFromContext(context);
		}
	}
	
	// Utilities
	
	protected static boolean isDifferentParticipant(InteractionNode source, InteractionNode target) {
		Participant sourceParticipant = getParticipant(source);
		Participant targetParticipant = getParticipant(target);
		
		if (sourceParticipant == null || targetParticipant == null) {
			return false;
		}
		
		return !sourceParticipant.getId().equals(targetParticipant.getId());
		
	}
	
	protected static Participant getParticipant(InteractionNode node) {
		if (node == null) {
			return null;
		}
		
		if (node instanceof Participant) {
			return (Participant) node;
		}
		
		Process process = getProcess(node);
		
		if (process == null) {
			return null;
		}
		
		Definitions definitions = (Definitions) process.eContainer();
		
		if (definitions == null) {
			return null;
		}
		
		Collaboration collaboration = null;
		for (RootElement rootElement : definitions.getRootElements()) {
			if (rootElement instanceof Collaboration) {
				collaboration = (Collaboration) rootElement;
				break;
			}
		}
		
		if (collaboration == null) {
			return null;
		}
		
		for (Participant particpant : collaboration.getParticipants()) {
			if (process.equals(particpant.getProcessRef())) {
				return particpant;
			}
		}
		
		return null;
	}
	
	private static Process getProcess(EObject element) {
		if (element instanceof Process) {
			return (Process) element;
		}
		
		EObject container = element.eContainer();
		if (container == null) {
			return null;
		}
		
		return getProcess(container);
	}
	
	protected static boolean containsMessageEventDefinition(List<EventDefinition> eventDefinitions) {
		for (EventDefinition eventDefinition : eventDefinitions) {
			if (eventDefinition instanceof MessageEventDefinition) {
				return true;
			}
		}
		return false;
	}
	
	protected static Object getSourceBusinessObjectFromContext(ICreateConnectionContext context) {
		if (context.getSourcePictogramElement() != null) {
			return getBusinessObjectForPictogramElement(context.getSourcePictogramElement());
		}
		
		Anchor anchor = context.getSourceAnchor();
		if (anchor != null && anchor.getParent() instanceof Shape) {
			Shape shape = (Shape) anchor.getParent();
			Connection conn = AnchorUtil.getConnectionPointOwner(shape);
			if (conn != null) {
				return BusinessObjectUtil.getFirstElementOfType(conn, BaseElement.class);
			}
			return BusinessObjectUtil.getFirstElementOfType(shape, BaseElement.class);
		}
		return null;
	}

	protected static Object getSourceBusinessObjectFromContext(IReconnectionContext context) {
		if (context.getTargetPictogramElement() instanceof FreeFormConnection) {
			return getBusinessObjectForPictogramElement(context.getTargetPictogramElement());
		}
		
		boolean reconnectSource = ReconnectionContext.RECONNECT_SOURCE.equals(context.getReconnectType());
		Anchor sourceAnchor = reconnectSource ? context.getNewAnchor() : context.getConnection().getStart();
		
		if (sourceAnchor == null) {
			return null;
		}
		
		AnchorContainer sourceContainer = sourceAnchor.getParent();
		EObject linkedObject = getBusinessObjectForPictogramElement(sourceContainer);
		
		if (linkedObject instanceof FreeFormConnection) {
			return getBusinessObjectForPictogramElement((FreeFormConnection) linkedObject);
		}
		
		return linkedObject;
	}
	
	protected static Object getTargetBusinessObjectFromContext(ICreateConnectionContext context) {
		if (context.getTargetPictogramElement() != null) {
			return getBusinessObjectForPictogramElement(context.getTargetPictogramElement());
		}
		
		Anchor anchor = context.getTargetAnchor();
		if (anchor != null && anchor.getParent() instanceof Shape) {
			Shape shape = (Shape) anchor.getParent();
			Connection conn = AnchorUtil.getConnectionPointOwner(shape);
			if (conn != null) {
				return BusinessObjectUtil.getFirstElementOfType(conn, BaseElement.class);
			}
			return BusinessObjectUtil.getFirstElementOfType(shape, BaseElement.class);
		}
		return null;
	}

	protected static Object getTargetBusinessObjectFromContext(IReconnectionContext context) {
		if (context.getTargetPictogramElement() instanceof FreeFormConnection) {
			return getBusinessObjectForPictogramElement(context.getTargetPictogramElement());
		}
		
		boolean reconnectSource = ReconnectionContext.RECONNECT_SOURCE.equals(context.getReconnectType());
		Anchor targetAnchor = reconnectSource ? context.getConnection().getEnd() : context.getNewAnchor();
		
		if (targetAnchor == null) {
			return null;
		}
		
		AnchorContainer targetContainer = targetAnchor.getParent();
		EObject linkedObject = getBusinessObjectForPictogramElement(targetContainer);
		
		if (linkedObject instanceof FreeFormConnection) {
			return getBusinessObjectForPictogramElement((FreeFormConnection) linkedObject);
		}
		
		return linkedObject;
	}
	
}
