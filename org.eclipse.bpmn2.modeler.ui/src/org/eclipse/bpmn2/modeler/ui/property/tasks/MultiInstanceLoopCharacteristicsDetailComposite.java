package org.eclipse.bpmn2.modeler.ui.property.tasks;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

public class MultiInstanceLoopCharacteristicsDetailComposite extends
		DefaultDetailComposite {

	public MultiInstanceLoopCharacteristicsDetailComposite(Composite parent,
			int style) {
		super(parent, style);
	}

	public MultiInstanceLoopCharacteristicsDetailComposite(
			AbstractBpmn2PropertySection section) {
		super(section);
	}

	public void createBindings(EObject be) {
		bindAttribute(be,"testBefore");
		bindReference(be,"loopCondition");
		bindReference(be,"loopMaximum");
	}
}
