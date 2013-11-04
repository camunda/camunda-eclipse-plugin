package org.camunda.bpm.modeler.ui.features.event;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.features.event.AbstractUpdateEventFeature;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.transform.Transformer;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.camunda.bpm.modeler.ui.features.AbstractMorphNodeFeature;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.context.impl.UpdateContext;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

public abstract class AbstractMorphEventFeature extends AbstractMorphNodeFeature<Event> {

	public AbstractMorphEventFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public EClass getBusinessObjectClass() {
		return Bpmn2Package.eINSTANCE.getEvent();
	}
	
	@Override
	public String getName() {
		return "Morph Event";
	}

	@Override
	public String getDescription() {
		return "Change the Event type";
	}
	
	@Override
	public ILabelProvider getLabelProvider() {
		if (labelProvider == null) {
			labelProvider = new EventLabelProvider();
		}
		return labelProvider;
	}
	
	@Override
	public Event morph(EObject target, MorphOption option) {
		// in that case only MorphEventOption will be handled
		if (!(option instanceof MorphEventOption)) {
			return (Event) target;
		}
		
		MorphEventOption eventDefinitionOption = (MorphEventOption) option;
		Event event = (Event) target;
		
		// At first morph the event definition of the event
		// to the new event definition type. 
		EventDefinition newEventDefinition = morphEventDefinition(event, eventDefinitionOption);
		
		// Then morph the event to the new event type
		Event newEvent = morphEvent(event, eventDefinitionOption);
		
		List<EventDefinition> eventDefinitions = getEventDefinitions(newEvent);

		// It can happen, that after morphing an event to a new type the list of event
		// definitions is empty, so that the new event definition will added to the list.
		if (newEventDefinition != null && !eventDefinitions.contains(newEventDefinition)) {
			eventDefinitions.add(newEventDefinition);
		}
		
		return newEvent;
	}
	
	private Event morphEvent(Event event, MorphEventOption option) {
		EClass newType = option.getNewType();
		
		if (newType.equals(Bpmn2Package.eINSTANCE.getBoundaryEvent())) {
			if (Bpmn2Package.eINSTANCE.getBoundaryEvent().isSuperTypeOf(event.eClass())) {
				return event;
			}
		}
		
		return super.morph(event, option);
	}
	
	private EventDefinition morphEventDefinition(Event event, MorphEventOption option) {
		EClass newEventDefinitionType = option.getNewEventDefinitionType();
		
		List<EventDefinition> eventDefinitions = getEventDefinitions(event);
		
		// if it is not a specific event definition selected, then unset 
		// the event definitions from the event.
		if (newEventDefinitionType == null) {
			EStructuralFeature feature = event.eClass().getEStructuralFeature("eventDefinitions");
			event.eUnset(feature);
			return null;
		}
		
		// if the event does not contain any event definition, then a new 
		// event definition will be created.
		if (eventDefinitions.isEmpty()) {
			return createNewEventDefinition(newEventDefinitionType);
		}
		
		// if the event contains one event definition, then this event
		// definition will be morphed to the new event definition type.
		if (eventDefinitions.size() == 1) {
			EventDefinition target = eventDefinitions.get(0);
			
			Transformer transformer = new Transformer(target);
			return (EventDefinition) transformer.morph(newEventDefinitionType);
		}
		
		// if there are more then one event definition, ...
		if (eventDefinitions.size() > 1) {
			
			// ... then iterate over them and...
			Iterator<EventDefinition> iterator = eventDefinitions.iterator();
			while (iterator.hasNext()) {
				EventDefinition eventDefinition = iterator.next();
				// ...remove the event definitions, which type does not match
				// the new event definition type.
				if (!eventDefinition.eClass().equals(newEventDefinitionType)) {
					iterator.remove();
				}
			}
			
			// if the list of event definitions is empty after the iteration,
			// then create a new event definition.
			if (eventDefinitions.isEmpty()) {
				return createNewEventDefinition(newEventDefinitionType);
			}
			
			// if the list of event definitions is not empty after the iteration,
			// then get the first one and return it. The list contains only event
			// definitions that match the new event definition type. In case there
			// are more than one, then the first one wins.
			return eventDefinitions.get(0);
		}
		return null;
	}
	
	private EventDefinition createNewEventDefinition(EClass fromEventDefinitionType) {
		Resource resource = fromEventDefinitionType.eResource();
		EventDefinition newEventDefinition = (EventDefinition) ModelHandler.create(resource, fromEventDefinitionType);
		
		return newEventDefinition;
	}
	
	@Override
	protected Shape createNewShape(MorphOption option, Shape oldShape, Event newObject) {
		// In case of events we do not have to create a new shape for the event,
		// we only have to trigger the update feature so that the shape of the
		// event including his children will be updated.
		IUpdateContext updateContext = new UpdateContext(oldShape);
		
		IUpdateFeature updateFeature = getFeatureProvider().getUpdateFeature(updateContext);
		updateFeature.update(updateContext);

		return oldShape;
	}
	
	@Override
	protected void cleanUp(PictogramElement pictogramElement) {
		// We do not have to do any cleanups, the event shape
		// will be updated.
		return;
	}
	
	@Override
	protected void handleMessageFlow(EObject bo, Connection messageFlow, List<Connection> connections, boolean incoming) {
		if (incoming && !Bpmn2Package.eINSTANCE.getCatchEvent().isInstance(bo)) {
			deletePictogramElement(messageFlow);
			return;
		}
		
		if (!incoming && !Bpmn2Package.eINSTANCE.getThrowEvent().isInstance(bo)) {
			deletePictogramElement(messageFlow);
			return;			
		}
		
		Event event = (Event) bo;
		List<EventDefinition> eventDefinitions = getEventDefinitions(event);

		MessageEventDefinition message = getEventDefinition(MessageEventDefinition.class, eventDefinitions);
		
		if (message == null) {
			deletePictogramElement(messageFlow);
			return;
		}
		
		super.handleMessageFlow(bo, messageFlow, connections, incoming);
	}

	@Override
	protected void handleDataAssociation(EObject bo, Connection dataAssociation, List<Connection> connections, boolean incoming) {
		if (incoming && !Bpmn2Package.eINSTANCE.getThrowEvent().isInstance(bo)) {
			deleteDataAssociation(dataAssociation);
			return;
		}

		if (!incoming && !Bpmn2Package.eINSTANCE.getCatchEvent().isInstance(bo)) {
			deleteDataAssociation(dataAssociation);
			return;
		}
		
		super.handleDataAssociation(bo, dataAssociation, connections, incoming);
	}
	
	private void deleteDataAssociation(Connection connection) {
		DataAssociation dataAssociation = BusinessObjectUtil.getFirstElementOfType(connection, DataAssociation.class);
		
		EObject container = dataAssociation.eContainer();
		EObject parentContainer = container.eContainer();
		
		if (parentContainer != null) {
			deletePictogramElement(connection);
			return;
		}
		
		// In case that an event has been morphed to another type 
		// (e.g. from catch event to throw event) the eContainer of
		// the event is null (event.eContainer() == null), because
		// this element has been removed from the diagram. Due this
		// it is not possible to delete the connection (i.e. the data
		// association), because an "UnsupportedOperationException" will
		// be thrown. That's why at first the bpmnEdge will be deleted and
		// then the connection will be removed.
		BPMNEdge bpmnEdge = BusinessObjectUtil.getFirstElementOfType(connection, BPMNEdge.class);
		EcoreUtil.delete(bpmnEdge, true);
		
		removePictogramElement(connection);
	}
	
	protected final <T extends EventDefinition> T getEventDefinition(Class<T> cls, List<EventDefinition> eventDefinitions) {
		for (EventDefinition eventDefinition : eventDefinitions) {
			if (cls.isInstance(eventDefinition)) {
				return cls.cast(eventDefinition);
			}
		}
		
		return null;
	}
	
	protected final List<EventDefinition> getEventDefinitions(Event event) {
		if (Bpmn2Package.eINSTANCE.getCatchEvent().isInstance(event)) {
			CatchEvent catchEvent = (CatchEvent) event;
			return catchEvent.getEventDefinitions();
		}
		
		if (Bpmn2Package.eINSTANCE.getThrowEvent().isInstance(event)) {
			ThrowEvent throwEvent = (ThrowEvent) event;
			return throwEvent.getEventDefinitions();
		}
		
		return Collections.emptyList();
	}
	
	public static class MorphEventOption extends MorphOption {

		private EClass newEventDefinitionType;
		
		public MorphEventOption(String name, EClass type) {
			super(name, type);
		}
		
		public MorphEventOption(String name, EClass type, EClass newEventDefinitionType) {
			super(name, type);
			
			this.newEventDefinitionType = newEventDefinitionType;
		}
		
		public EClass getNewEventDefinitionType() {
			return newEventDefinitionType;
		}
		
	}
	
	protected class EventLabelProvider extends LabelProvider {
		
		private boolean isCatchEvent(EClass cls) {
			return Bpmn2Package.eINSTANCE.getCatchEvent().isSuperTypeOf(cls);
		}
		
		@Override
		public Image getImage(Object element) {
			if (!(element instanceof MorphEventOption)) {
				return null;
			}
			
			MorphEventOption option = (MorphEventOption) element;
			
			EClass cls = option.getNewType();
			EClass eventDefinitionCls = (EClass) option.getNewEventDefinitionType();
			
			// EventDefinitions
			if (eventDefinitionCls != null) {
				if (eventDefinitionCls.equals(Bpmn2Package.eINSTANCE.getConditionalEventDefinition())) {
					return getImageForId(ImageProvider.IMG_16_CONDITION);
				}
				
				if (eventDefinitionCls.equals(Bpmn2Package.eINSTANCE.getTimerEventDefinition())) {
					return getImageForId(ImageProvider.IMG_16_TIMER);
				}
				
				if (eventDefinitionCls.equals(Bpmn2Package.eINSTANCE.getTerminateEventDefinition())) {
					return getImageForId(ImageProvider.IMG_16_TERMINATE);
				}				
				
				if (eventDefinitionCls.equals(Bpmn2Package.eINSTANCE.getSignalEventDefinition())) {
					if (isCatchEvent(cls)) {
						return getImageForId(ImageProvider.IMG_16_SIGNAL);
					} else {
						return getImageForId(ImageProvider.IMG_16_SIGNAL_THROW);
					}
				}
				
				if (eventDefinitionCls.equals(Bpmn2Package.eINSTANCE.getMessageEventDefinition())) {
					if (isCatchEvent(cls)) {
						return getImageForId(ImageProvider.IMG_16_MESSAGE);
					} else {
						return getImageForId(ImageProvider.IMG_16_MESSAGE_THROW);
					}
				}
				
				if (eventDefinitionCls.equals(Bpmn2Package.eINSTANCE.getEscalationEventDefinition())) {
					if (isCatchEvent(cls)) {
						return getImageForId(ImageProvider.IMG_16_ESCAlATION);
					} else {
						return getImageForId(ImageProvider.IMG_16_ESCAlATION_THROW);
					}
				}
				
				if (eventDefinitionCls.equals(Bpmn2Package.eINSTANCE.getCompensateEventDefinition())) {
					if (isCatchEvent(cls)) {
						return getImageForId(ImageProvider.IMG_16_COMPENSATE);
					} else {
						return getImageForId(ImageProvider.IMG_16_COMPENSATE_THROW);
					}
				}
				
				if (eventDefinitionCls.equals(Bpmn2Package.eINSTANCE.getLinkEventDefinition())) {
					if (isCatchEvent(cls)) {
						return getImageForId(ImageProvider.IMG_16_LINK);
					} else {
						return getImageForId(ImageProvider.IMG_16_LINK_THROW);
					}
				}
				
				if (eventDefinitionCls.equals(Bpmn2Package.eINSTANCE.getErrorEventDefinition())) {
					if (isCatchEvent(cls)) {
						return getImageForId(ImageProvider.IMG_16_ERROR);
					} else {
						return getImageForId(ImageProvider.IMG_16_ERROR_THROW);
					}
				}
				
				if (eventDefinitionCls.equals(Bpmn2Package.eINSTANCE.getCancelEventDefinition())) {
					if (isCatchEvent(cls)) {
						return getImageForId(ImageProvider.IMG_16_CANCEL);
					} else {
						return getImageForId(ImageProvider.IMG_16_CANCEL_THROW);
					}
				}
			}
			
			// Events
			if (cls.equals(Bpmn2Package.eINSTANCE.getStartEvent())) {
				return getImageForId(ImageProvider.IMG_16_START_EVENT);
			}
			
			if (cls.equals(Bpmn2Package.eINSTANCE.getBoundaryEvent())) {
				return getImageForId(ImageProvider.IMG_16_BOUNDARY_EVENT);
			}			
			
			if (cls.equals(Bpmn2Package.eINSTANCE.getIntermediateCatchEvent())) {
				return getImageForId(ImageProvider.IMG_16_INTERMEDIATE_CATCH_EVENT);
			}

			if (cls.equals(Bpmn2Package.eINSTANCE.getIntermediateThrowEvent())) {
				return getImageForId(ImageProvider.IMG_16_INTERMEDIATE_THROW_EVENT);
			}

			if (cls.equals(Bpmn2Package.eINSTANCE.getEndEvent())) {
				return getImageForId(ImageProvider.IMG_16_END_EVENT);
			}
			
			return null;
		}		
	}

}
