package org.eclipse.bpmn2.modeler.ui.property.events;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.ImplicitThrowEvent;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.Transaction;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultListComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ModelSubclassSelectionDialog;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;

public class EventDefinitionsListComposite extends DefaultListComposite {
	
	protected Event event;
	
	public EventDefinitionsListComposite(Composite parent, Event event) {
		super(parent, ADD_BUTTON|REMOVE_BUTTON|SHOW_DETAILS);
		this.event = event;
	}

	@Override
	public EClass getListItemClassToAdd(EClass listItemClass) {
		EClass eclass = null;
		ModelSubclassSelectionDialog dialog = new ModelSubclassSelectionDialog(getDiagramEditor(), businessObject, feature) {
			@Override
			protected void filterList(List<EClass> items) {
				BaseElement eventOwner = null;
				if (event instanceof BoundaryEvent) {
					eventOwner = ((BoundaryEvent)event).getAttachedToRef();
				}
				else {
					EObject parent = event.eContainer();
					while (parent!=null) {
						if (parent instanceof FlowElementsContainer ) {
							eventOwner = (BaseElement)parent;
							break;
						}
						parent = parent.eContainer();
					}
				}
				List<EClass> filteredItems = new ArrayList<EClass>();
				List<EClass> allowedItems = new ArrayList<EClass>();
				if (event instanceof BoundaryEvent) {
					if (eventOwner instanceof Transaction) {
						if (((BoundaryEvent)event).isCancelActivity())
							allowedItems.add(PACKAGE.getCancelEventDefinition());
					}
					if (((BoundaryEvent)event).isCancelActivity())
						allowedItems.add(PACKAGE.getCompensateEventDefinition());
					allowedItems.add(PACKAGE.getConditionalEventDefinition());
					if (((BoundaryEvent)event).isCancelActivity())
						allowedItems.add(PACKAGE.getErrorEventDefinition());
					allowedItems.add(PACKAGE.getEscalationEventDefinition());
					allowedItems.add(PACKAGE.getMessageEventDefinition());
					allowedItems.add(PACKAGE.getSignalEventDefinition());
					allowedItems.add(PACKAGE.getTimerEventDefinition());
				}
				else if (event instanceof IntermediateCatchEvent) {
					allowedItems.add(PACKAGE.getConditionalEventDefinition());
					allowedItems.add(PACKAGE.getLinkEventDefinition());
					allowedItems.add(PACKAGE.getMessageEventDefinition());
					allowedItems.add(PACKAGE.getSignalEventDefinition());
					allowedItems.add(PACKAGE.getTimerEventDefinition());
				}
				else if (event instanceof StartEvent) {
					if (eventOwner instanceof SubProcess) {
						if (((StartEvent)event).isIsInterrupting()) {
							allowedItems.add(PACKAGE.getCompensateEventDefinition());
							allowedItems.add(PACKAGE.getErrorEventDefinition());
						}
						allowedItems.add(PACKAGE.getEscalationEventDefinition());
					}
					allowedItems.add(PACKAGE.getConditionalEventDefinition());
					allowedItems.add(PACKAGE.getMessageEventDefinition());
					allowedItems.add(PACKAGE.getSignalEventDefinition());
					allowedItems.add(PACKAGE.getTimerEventDefinition());
				}
				else if (event instanceof EndEvent) {
					if (eventOwner instanceof Transaction)
						allowedItems.add(PACKAGE.getCancelEventDefinition());
					allowedItems.add(PACKAGE.getCompensateEventDefinition());
					allowedItems.add(PACKAGE.getErrorEventDefinition());
					allowedItems.add(PACKAGE.getEscalationEventDefinition());
					allowedItems.add(PACKAGE.getMessageEventDefinition());
					allowedItems.add(PACKAGE.getSignalEventDefinition());
					allowedItems.add(PACKAGE.getTerminateEventDefinition());
				}
				else if (event instanceof ImplicitThrowEvent) {
					allowedItems.add(PACKAGE.getCompensateEventDefinition());
					allowedItems.add(PACKAGE.getEscalationEventDefinition());
					allowedItems.add(PACKAGE.getLinkEventDefinition());
					allowedItems.add(PACKAGE.getMessageEventDefinition());
					allowedItems.add(PACKAGE.getSignalEventDefinition());
				}
				else if (event instanceof IntermediateThrowEvent) {
					allowedItems.add(PACKAGE.getCompensateEventDefinition());
					allowedItems.add(PACKAGE.getEscalationEventDefinition());
					allowedItems.add(PACKAGE.getLinkEventDefinition());
					allowedItems.add(PACKAGE.getMessageEventDefinition());
					allowedItems.add(PACKAGE.getSignalEventDefinition());
				}
				for (EClass eclass : items) {
					if (allowedItems.contains(eclass))
						filteredItems.add(eclass);
				}
				items.clear();
				items.addAll(filteredItems);
			}
		};
		
		if (dialog.open()==Window.OK){
			eclass = (EClass)dialog.getResult()[0];
		}
		return eclass;
	}
}