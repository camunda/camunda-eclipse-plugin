package org.camunda.bpm.modeler.core.features.rules;

import static org.camunda.bpm.modeler.core.utils.BusinessObjectUtil.getBusinessObjectForPictogramElement;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * Container class for operations inside the model
 * 
 * @author nico.rehwaldt
 */
public class ModelOperations extends RuleOperations {

	private static List<MoveAlgorithm> MOVE;
	private static List<CreateAlgorithm> CREATE;

	static {
		MOVE = new ArrayList<MoveAlgorithm>();

		MOVE.add(new FromLaneAlgorithm());
		MOVE.add(new ToLaneAlgorithm());
		MOVE.add(new FromParticipantAlgorithm());
		MOVE.add(new ToParticipantAlgorithm());
		MOVE.add(new ToDiagramAlgorithm());
		MOVE.add(new FromFlowElementsContainerAlgorithm());
		MOVE.add(new ToFlowElementsContainerAlgorithm());
		
		CREATE = new ArrayList<CreateAlgorithm>();
		
		CREATE.add(new OnLaneAlgorithm());
		CREATE.add(new OnParticipantAlgorithm());
		CREATE.add(new OnDiagramAlgorithm());
		CREATE.add(new OnFlowElementsContainer());
	}
	
	public static ModelMoveOperation getFlowNodeMoveOperation(IMoveShapeContext context) {
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

		return new ModelMoveOperation(from, to);
	}

	public static ModelCreateOperation getFlowNodeCreateOperation(ICreateContext context) {
		CreateAlgorithm algorithm = null;
		
		for (CreateAlgorithm a : CREATE) {
			if (a.appliesTo(context)) {
				algorithm = a;
				break;
			}
		}

		return new ModelCreateOperation(algorithm);
	}

	public static class ModelMoveOperation extends FromToModelOperation<IMoveShapeContext> {

		public ModelMoveOperation(Algorithm<IMoveShapeContext> from, Algorithm<IMoveShapeContext> to) {
			super(from, to);
		}
	}
	
	public static class ModelCreateOperation extends ToModelOperation<ICreateContext> {

		public ModelCreateOperation(Algorithm<ICreateContext> to) {
			super(to);
		}
	}
	
	//// algorithm implementations ////////////////////////////////////////////

	abstract static class MoveAlgorithm implements Algorithm<IMoveShapeContext> {
		public abstract Side getType();
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
		
		protected BaseElement getBusinessObject(PictogramElement e) {
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
		public final Side getType() {
			return Side.FROM;
		}
	}
	
	abstract static class MoveToAlgorithm extends DefaultMoveAlgorithm {

		@Override
		public final Side getType() {
			return Side.TO;
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
			return FeatureSupport.isTargetParticipant(context);
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
	
	static class ToDiagramAlgorithm extends MoveToAlgorithm {

		@Override
		public boolean appliesTo(IMoveShapeContext context) {
			return FeatureSupport.isTargetDiagram(context);
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
	
	abstract static class CreateAlgorithm implements Algorithm<ICreateContext>  {
	}
	
	abstract static class DefaultCreateAlgorithm extends CreateAlgorithm {
		
		@Override
		public void execute(ICreateContext context) {
			// do nothing
		}
		
		private BaseElement getBusinessObject(PictogramElement e) {
			return BusinessObjectUtil.getFirstElementOfType(e, BaseElement.class);
		}
		
		protected BaseElement getTarget(ICreateContext context) {
			return getBusinessObject(context.getTargetContainer());
		}
	}
	
	static class OnLaneAlgorithm extends DefaultCreateAlgorithm {

		@Override
		public boolean appliesTo(ICreateContext context) {
			return FeatureSupport.isTargetLane(context);
		}
		
		@Override
		public boolean canExecute(ICreateContext context) {
			return canAddToLane((Lane) getTarget(context));
		}
	}

	static class OnParticipantAlgorithm extends DefaultCreateAlgorithm {

		@Override
		public boolean appliesTo(ICreateContext context) {
			return FeatureSupport.isTargetParticipant(context);
		}
		
		@Override
		public boolean canExecute(ICreateContext context) {
			return canAddToFlowElementsContainer((Participant) getTarget(context));
		}
	}
	
	static class OnDiagramAlgorithm extends DefaultCreateAlgorithm {

		@Override
		public boolean appliesTo(ICreateContext context) {
			return FeatureSupport.isTargetDiagram(context);
		}
		
		@Override
		public boolean canExecute(ICreateContext context) {
			BaseElement target = getTarget(context);
			return canAddToFlowElementsContainer(target);
		}
	}
	
	static class OnFlowElementsContainer extends DefaultCreateAlgorithm {
		
		@Override
		public boolean appliesTo(ICreateContext context) {
			Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
			return bo != null && bo instanceof FlowElementsContainer;
		}
		
		@Override
		public boolean canExecute(ICreateContext context) {
			return true;
		}
	}
	
	static boolean canAddToLane(Lane lane) {
		return lane.getChildLaneSet() == null || lane.getChildLaneSet().getLanes().isEmpty();
	}
	

	static boolean canAddToDiagram(Diagram target) {
		
		Definitions definitions = BusinessObjectUtil.getFirstElementOfType(target, Definitions.class);
		
		for (RootElement e: definitions.getRootElements()) {
			if (e instanceof Collaboration) {
				return false;
			}
		}
		
		return true;
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
