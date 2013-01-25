package org.eclipse.bpmn2.modeler.core.features.rules;

import static org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil.getBusinessObjectForPictogramElement;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * Container class for operations inside the model
 * 
 * @author nico.rehwaldt
 */
public class ModelOperations {

	private static List<MoveAlgorithm> MOVE;
	private static List<AddAlgorithm> CREATE;

	static {
		MOVE = new ArrayList<MoveAlgorithm>();

		MOVE.add(new FromLaneAlgorithm());
		MOVE.add(new ToLaneAlgorithm());
		MOVE.add(new FromParticipantAlgorithm());
		MOVE.add(new ToParticipantAlgorithm());
		MOVE.add(new FromFlowElementsContainerAlgorithm());
		MOVE.add(new ToFlowElementsContainerAlgorithm());
		
		CREATE = new ArrayList<AddAlgorithm>();
		
		CREATE.add(new OnLaneAlgorithm());
		CREATE.add(new OnParticipantAlgorithm());
		CREATE.add(new OnFlowElementsContainer());
	}
	
	private enum MoveType {
		FROM, 
		TO
	};
	
	/**
	 * TODO: Refactor or streamline with other stuff.
	 * 
	 * @author nico.rehwaldt
	 */
	public interface Algorithm<T extends IContext> {
		
		boolean appliesTo(T context);

		public boolean canExecute(T context);
		
		public void execute(T context);
	}

	public static ModelOperation<IMoveShapeContext> getFlowNodeMoveAlgorithms(IMoveShapeContext context) {
		MoveAlgorithm from = null;
		MoveAlgorithm to = null;
		
		for (MoveAlgorithm algorithm : MOVE) {
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

		return new ModelOperation<IMoveShapeContext>(from, to);
	}
	
	public static class ModelOperation<T extends IContext> {
		
		public Algorithm<T> fromAlgorithm;
		public Algorithm<T> toAlgorithm;

		public ModelOperation(Algorithm<T> fromAlgorithm, Algorithm<T> toAlgorithm) {
			this.fromAlgorithm = fromAlgorithm;
			this.toAlgorithm = toAlgorithm;
		}

		public boolean canExecute(T context) {
			return fromAlgorithm.canExecute(context) && toAlgorithm.canExecute(context);
		}

		public void execute(T context) {
			fromAlgorithm.execute(context);
			toAlgorithm.execute(context);
		}

		public boolean isEmpty() {
			return fromAlgorithm == null || toAlgorithm == null;
		}
	}
	
	//// algorithm implementations ////////////////////////////////////////////
	
	abstract static class MoveAlgorithm implements Algorithm<IMoveShapeContext> {
		public abstract MoveType getType();
	}
	
	abstract static class DefaultMoveAlgorithm extends MoveAlgorithm {

		
		@Override
		public boolean canExecute(IMoveShapeContext context) {
			return true;
		}
		
		@Override
		public void execute(IMoveShapeContext context) {
			
			BaseElement element = getElement(context);
			
			ModelHandler handler = ModelHandler.getInstance(element);
			handler.moveFlowNode((FlowNode) element, getSource(context), getTarget(context));
		}
		
		private BaseElement getBusinessObject(PictogramElement e) {
			return BusinessObjectUtil.getFirstElementOfType(e, BaseElement.class);
		}
		
		protected BaseElement getSource(IMoveShapeContext context) {
			return getBusinessObject(context.getSourceContainer());
		}
		
		protected BaseElement getTarget(IMoveShapeContext context) {
			return getBusinessObject(context.getTargetContainer());
		}
		
		protected BaseElement getElement(IMoveShapeContext context) {
			return getBusinessObject(context.getPictogramElement());
		}
	}

	abstract static class MoveFromAlgorithm extends DefaultMoveAlgorithm {

		@Override
		public final MoveType getType() {
			return MoveType.FROM;
		}
	}
	
	abstract static class MoveToAlgorithm extends DefaultMoveAlgorithm {

		@Override
		public final MoveType getType() {
			return MoveType.TO;
		}
	}

	static class FromLaneAlgorithm extends MoveFromAlgorithm {

		@Override
		public boolean appliesTo(IMoveShapeContext context) {
			return FeatureSupport.isSourceLane(context);
		}

		@Override
		public void execute(IMoveShapeContext context) {
			Lane lane = (Lane) getSource(context);
			FlowNode element = (FlowNode) getElement(context);

			lane.getFlowNodeRefs().remove(element);
			element.getLanes().remove(lane);
		}
	}

	static class ToLaneAlgorithm extends MoveToAlgorithm {

		@Override
		public boolean appliesTo(IMoveShapeContext context) {
			return FeatureSupport.isTargetLane(context);
		}

		@Override
		public boolean canExecute(IMoveShapeContext context) {
			Lane lane = (Lane) getTarget(context);
			return canAddToLane(lane);
		}

		@Override
		public void execute(IMoveShapeContext context) {
			
			Lane lane = (Lane) getTarget(context);
			FlowNode element = (FlowNode) getElement(context);
			
			lane.getFlowNodeRefs().add(element);
			element.getLanes().add(lane);
			super.execute(context);
		}
	}

	static class FromParticipantAlgorithm extends MoveFromAlgorithm {

		@Override
		public boolean canExecute(IMoveShapeContext context) {
			return true;
		}
		
		@Override
		public void execute(IMoveShapeContext context) {
			
		}
		
		@Override
		public boolean appliesTo(IMoveShapeContext context) {
			return context.getSourceContainer() != context.getTargetContainer() && 
				   FeatureSupport.isSourceParticipant(context);
		}
	}

	static class ToParticipantAlgorithm extends MoveToAlgorithm {

		@Override
		public boolean appliesTo(IMoveShapeContext context) {
			return FeatureSupport.isTargetParticipant(context) || 
				   FeatureSupport.isTargetDiagram(context);
		}

		@Override
		public boolean canExecute(IMoveShapeContext context) {
			BaseElement source = getSource(context);
			BaseElement target = getTarget(context);
			
			if (source == target) {
				return true;
			}
			
			return canAddToFlowElementsContainer(target);
		}
		
	}

	static class FromFlowElementsContainerAlgorithm extends MoveFromAlgorithm {

		@Override
		public boolean appliesTo(IMoveShapeContext context) {
			Object bo = getBusinessObjectForPictogramElement(context.getSourceContainer());
			return bo != null && bo instanceof FlowElementsContainer;
		}

		@Override
		public void execute(IMoveShapeContext context) {
			
		}
	}

	static class ToFlowElementsContainerAlgorithm extends MoveToAlgorithm {

		@Override
		public boolean appliesTo(IMoveShapeContext context) {
			Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
			return bo != null && bo instanceof FlowElementsContainer;
		}
	}
	
	//// create algorithms /////////////////////////////////////////
	
	abstract static class AddAlgorithm implements Algorithm<IAddContext>  {
		
	}
	
	abstract static class DefaultAddAlgorithm extends AddAlgorithm {
		
		@Override
		public boolean canExecute(IAddContext context) {
			return false;
		}
		
		@Override
		public void execute(IAddContext context) {
			
		}
		
		private BaseElement getBusinessObject(PictogramElement e) {
			return BusinessObjectUtil.getFirstElementOfType(e, BaseElement.class);
		}
		
		protected BaseElement getTarget(IAddContext context) {
			return getBusinessObject(context.getTargetContainer());
		}
		
		protected BaseElement getElement(IAddContext context) {
			return (BaseElement) context.getNewObject();
		}
	}
	
	static class OnLaneAlgorithm extends DefaultAddAlgorithm {

		@Override
		public boolean appliesTo(IAddContext context) {
			return FeatureSupport.isTargetLane(context);
		}
		
		@Override
		public boolean canExecute(IAddContext context) {
			return canAddToLane((Lane) getTarget(context));
		}
	}

	static class OnParticipantAlgorithm extends DefaultAddAlgorithm {

		@Override
		public boolean appliesTo(IAddContext context) {
			return FeatureSupport.isTargetParticipant(context) || 
				   FeatureSupport.isTargetDiagram(context);
		}
		
		@Override
		public boolean canExecute(IAddContext context) {
			return canAddToFlowElementsContainer((Lane) getTarget(context));
		}
	}
	
	static class OnFlowElementsContainer extends DefaultAddAlgorithm {
		@Override
		public boolean appliesTo(IAddContext context) {
			Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
			return bo != null && bo instanceof FlowElementsContainer;
		}
	}
	
	static boolean canAddToLane(Lane lane) {
		return lane.getChildLaneSet() == null || lane.getChildLaneSet().getLanes().isEmpty();
	}
	
	static boolean canAddToFlowElementsContainer(BaseElement target) {
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
