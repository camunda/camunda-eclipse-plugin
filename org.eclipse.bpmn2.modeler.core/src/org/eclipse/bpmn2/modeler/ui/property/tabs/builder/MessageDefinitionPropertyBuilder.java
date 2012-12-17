package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Message;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Builder for the error code / name property.
 * 
 * @author nico.rehwaldt
 */
public class MessageDefinitionPropertyBuilder extends DefinitionReferencePropertyBuilder<Message> {

	private static final EStructuralFeature MESSAGE_REF_FEATURE = Bpmn2Package.eINSTANCE.getMessageEventDefinition_MessageRef();

	public MessageDefinitionPropertyBuilder(Composite parent, GFPropertySection section, MessageEventDefinition bo) {
		super(parent, section, bo, "Message", MESSAGE_REF_FEATURE, Message.class);
	}
}
