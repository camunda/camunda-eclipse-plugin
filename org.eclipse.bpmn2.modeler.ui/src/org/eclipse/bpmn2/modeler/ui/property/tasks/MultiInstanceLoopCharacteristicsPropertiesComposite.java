package org.eclipse.bpmn2.modeler.ui.property.tasks;

import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.DefaultDetailComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

public class MultiInstanceLoopCharacteristicsPropertiesComposite extends
		DefaultDetailComposite {

	public MultiInstanceLoopCharacteristicsPropertiesComposite(Composite parent,
			int style) {
		super(parent, style);
	}

	public MultiInstanceLoopCharacteristicsPropertiesComposite(
			AbstractBpmn2PropertySection section) {
		super(section);
	}

	public void createBindings(EObject be) {
		bindAttribute(be,"testBefore");
		bindReference(be,"loopCondition");
		bindReference(be,"loopMaximum");
	}
}
