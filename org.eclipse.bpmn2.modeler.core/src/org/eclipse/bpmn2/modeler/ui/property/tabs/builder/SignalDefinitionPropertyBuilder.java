package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Signal;
import org.eclipse.bpmn2.SignalEventDefinition;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Builder for the error code / name property.
 * 
 * @author nico.rehwaldt
 */
public class SignalDefinitionPropertyBuilder extends DefinitionReferencePropertyBuilder<Signal> {

	private static final EStructuralFeature SIGNAL_REF_FEATURE = Bpmn2Package.eINSTANCE.getSignalEventDefinition_SignalRef();

	public SignalDefinitionPropertyBuilder(Composite parent, GFPropertySection section, SignalEventDefinition bo) {
		super(parent, section, bo, "Signal", SIGNAL_REF_FEATURE, Signal.class);
	}
}
