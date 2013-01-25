package org.eclipse.bpmn2.modeler.core.features.rules;

import static org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil.getBusinessObjectForPictogramElement;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.copypaste.IPasteIntoContext;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.graphiti.features.context.IMoveShapeContext;

public class Algorithms {

	private static List<Algorithm> NODE_MOVE;
	private static List<Algorithm> NODE_CREATE;

	static {
		NODE_MOVE = new ArrayList<Algorithm>();

		NODE_MOVE.add(new FromLaneAlgorithm());
		NODE_MOVE.add(new ToLaneAlgorithm());
		NODE_MOVE.add(new FromParticipantAlgorithm());
		NODE_MOVE.add(new ToParticipantAlgorithm());
		NODE_MOVE.add(new FromFlowElementsContainerAlgorithm());
		NODE_MOVE.add(new ToFlowElementsContainerAlgorithm());
		
		NODE_CREATE = new ArrayList<Algorithm>();
	}
	
	private enum Type {
		FROM, 
		TO
	};
	
	/**
	 * TODO: Refactor or streamline with other stuff.
	 * 
	 * @author nico.rehwaldt
	 */
	public interface Algorithm {

		public Type getType();
		
		public boolean canApplyTo(IMoveShapeContext context);

		public boolean isMoveAllowed(Object source, Object target);

		public void move(FlowNode node, Object source, Object target);
	}

	public static AlgorithmContainer getFlowNodeMoveAlgorithms(IMoveShapeContext context) {
		Algorithm fromAlgorithm = null;
		Algorithm toAlgorithm = null;

		for (Algorithm a : NODE_MOVE) {
			if (a.canApplyTo(context)) {
				switch (a.getType()) {
				case FROM:
					fromAlgorithm = a;
					break;
				case TO:
					toAlgorithm = a;
					break;
				}
			}
		}

		return new AlgorithmContainer(fromAlgorithm, toAlgorithm);
	}
	
	public static class AlgorithmContainer {
		
		public Algorithm fromAlgorithm;
		public Algorithm toAlgorithm;

		public AlgorithmContainer(Algorithm fromAlgorithm, Algorithm toAlgorithm) {
			this.fromAlgorithm = fromAlgorithm;
			this.toAlgorithm = toAlgorithm;
		}

		public boolean isMoveAllowed(Object source, Object target) {
			return fromAlgorithm.isMoveAllowed(source, target) && toAlgorithm.isMoveAllowed(source, target);
		}

		public void move(FlowNode node, Object source, Object target) {
			fromAlgorithm.move(node, source, target);
			toAlgorithm.move(node, source, target);
		}

		public boolean isEmpty() {
			return fromAlgorithm == null || toAlgorithm == null;
		}
	}
	
	//// algorithm implementations ////////////////////////////////////////////
	
	abstract static class DefaultAlgorithm implements Algorithm {

		@Override
		public boolean isMoveAllowed(Object source, Object target) {
			return true;
		}

		@Override
		public void move(FlowNode node, Object source, Object target) {
			ModelHandler handler = ModelHandler.getInstance(node);
			handler.moveFlowNode(node, source, target);
		}
	}

	abstract static class FromAlgorithm extends DefaultAlgorithm {

		@Override
		public final Type getType() {
			return Type.FROM;
		}
	}
	
	abstract static class ToAlgorithm extends DefaultAlgorithm {

		@Override
		public final Type getType() {
			return Type.TO;
		}
	}

	static class FromLaneAlgorithm extends FromAlgorithm {

		@Override
		public boolean canApplyTo(IMoveShapeContext context) {
			return FeatureSupport.isSourceLane(context);
		}

		@Override
		public void move(FlowNode node, Object source, Object target) {
			Lane lane = (Lane) source;
			lane.getFlowNodeRefs().remove(node);
			node.getLanes().remove(lane);
		}
	}

	static class ToLaneAlgorithm extends ToAlgorithm {

		@Override
		public boolean canApplyTo(IMoveShapeContext context) {
			return FeatureSupport.isTargetLane(context);
		}

		@Override
		public boolean isMoveAllowed(Object source, Object target) {
			Lane lane = (Lane) target;
			return lane.getChildLaneSet() == null || lane.getChildLaneSet().getLanes().isEmpty();
		}

		@Override
		public void move(FlowNode node, Object source, Object target) {
			Lane lane = (Lane) target;
			lane.getFlowNodeRefs().add(node);
			node.getLanes().add(lane);
			super.move(node, source, target);
		}
	}

	static class FromParticipantAlgorithm extends FromAlgorithm {

		@Override
		public boolean isMoveAllowed(Object source, Object target) {
			return false;
		}
		
		@Override
		public boolean canApplyTo(IMoveShapeContext context) {
			return context.getSourceContainer() != context.getTargetContainer() && 
				   FeatureSupport.isSourceParticipant(context);
		}
	}

	static class ToParticipantAlgorithm extends ToAlgorithm {

		@Override
		public boolean canApplyTo(IMoveShapeContext context) {
			return FeatureSupport.isTargetParticipant(context) || 
				   FeatureSupport.isTargetDiagram(context);
		}

		@Override
		public boolean isMoveAllowed(Object source, Object target) {
			if (source == target) {
				return true;
			}
			
			if (target instanceof Participant) {
				Participant p = (Participant) target;
				
				if (p.getProcessRef() == null) {
					return true;
				}
				
				for (LaneSet laneSet: p.getProcessRef().getLaneSets()) {
					if (!laneSet.getLanes().isEmpty()) { 
						return false;
					}
				}
				
				return true;
			} else
			
			if (target instanceof FlowElementsContainer) {
				FlowElementsContainer flowElementsContainer = (FlowElementsContainer) target;
				if (flowElementsContainer.getLaneSets().isEmpty()) {
					return true;
				}
			}
			return false;
		}
		
	}

	static class FromFlowElementsContainerAlgorithm extends FromAlgorithm {

		@Override
		public boolean canApplyTo(IMoveShapeContext context) {
			Object bo = getBusinessObjectForPictogramElement(context.getSourceContainer());
			return bo != null && bo instanceof FlowElementsContainer;
		}

		@Override
		public void move(FlowNode node, Object source, Object target) {
		}
	}

	static class ToFlowElementsContainerAlgorithm extends ToAlgorithm {

		@Override
		public boolean canApplyTo(IMoveShapeContext context) {
			Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
			return bo != null && bo instanceof FlowElementsContainer;
		}
	}
}
