package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import java.util.List;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Error;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.InclusiveGateway;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.BaseElementIdComboBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.ModelViewBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.change.EAttributeChangeSupport;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.change.EObjectChangeSupport.ModelChangedEvent;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Decision gateway specific stuff
 * 
 * @author nico.rehwaldt
 */
public class DecisionGatewayPropertiesBuilder extends AbstractPropertiesBuilder<Gateway> {

	private static final EStructuralFeature FLOW_NODE_OUTGOING = Bpmn2Package.eINSTANCE.getFlowNode_Outgoing();
	
	private final EStructuralFeature DEFAULT_FLOW_FEATURE;
	
	public DecisionGatewayPropertiesBuilder(Composite parent, GFPropertySection section, Gateway bo) {
		super(parent, section, bo);
		
		if (bo instanceof ExclusiveGateway) {
			DEFAULT_FLOW_FEATURE = Bpmn2Package.eINSTANCE.getExclusiveGateway_Default();
		} else 
		if (bo instanceof InclusiveGateway) {
			DEFAULT_FLOW_FEATURE = Bpmn2Package.eINSTANCE.getInclusiveGateway_Default();
		} else {
			throw new IllegalArgumentException("Unsupported gateway: " + bo);
		}
	}

	@Override
	public void create() {
		final CCombo dropDown = PropertyUtil.createDropDown(section, parent, "Default Flow");
		
		// register change support
		EAttributeChangeSupport.ensureAdded(bo, FLOW_NODE_OUTGOING, dropDown);
		
		dropDown.addListener(Events.MODEL_CHANGED, new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				ModelChangedEvent event = (ModelChangedEvent) e;
				if (FLOW_NODE_OUTGOING.equals(event.getFeature())) {
					updateDropdownLabels(dropDown);
				}
			}
		});
		
		BaseElementIdComboBinding<SequenceFlow> dropDownBinding = new BaseElementIdComboBinding<SequenceFlow>(bo, DEFAULT_FLOW_FEATURE, dropDown) {
			@Override
			protected SequenceFlow getModelById(String id) {
				return getSequenceFlowById(id);
			}
		};
		
		dropDownBinding.establish();
		
		updateDropdownLabels(dropDown);
	}
	
	/**
	 * Returns the sequence flow with the given id
	 * in the context of this factory
	 * 
	 * @param id
	 * @return
	 */
	private SequenceFlow getSequenceFlowById(String id) {
		List<SequenceFlow> flows = getSequenceFlows();
		for (SequenceFlow outgoing: flows) {
			String nodeId = outgoing.getId();
			if (nodeId.equals(id)) {
				return outgoing;
			}
		}
		
		return null;
	}

	private List<SequenceFlow> getSequenceFlows() {
		return (List<SequenceFlow>) bo.eGet(FLOW_NODE_OUTGOING);
	}
	
	private void updateDropdownLabels(CCombo dropDown) {
		
		dropDown.removeAll();
		
		List<SequenceFlow> flows = getSequenceFlows();
		
		EObject defaultFlow = (EObject) bo.eGet(DEFAULT_FLOW_FEATURE);
		
		dropDown.add("");
		
		for (SequenceFlow outgoing: flows) {
			String nodeId = outgoing.getId();
			dropDown.add(nodeId);
			
			if (outgoing.equals(defaultFlow)) {
				dropDown.select(dropDown.indexOf(nodeId));
			}
		}
	}
}
