package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.property;

import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.ItemKind;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditor;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.ItemDefinitionDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.editors.SchemaObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Composite;

public class JbpmItemDefinitionDetailComposite extends ItemDefinitionDetailComposite {

	public JbpmItemDefinitionDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	public JbpmItemDefinitionDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	
	@Override
	protected void bindReference(Composite parent, EObject object, EReference reference) {
		if ("structureRef".equals(reference.getName()) &&
				modelEnablement.isEnabled(object.eClass(), reference)) {
			
			if (parent==null)
				parent = getAttributesParent();
			
			final ItemDefinition def = (ItemDefinition)object;
			String displayName = ModelUtil.getLabel(object, reference);
			
			JbpmImportObjectEditor editor = new JbpmImportObjectEditor(this,object,reference);
			editor.createControl(parent,displayName);
		}
		else
			super.bindReference(parent, object, reference);
	}
}
