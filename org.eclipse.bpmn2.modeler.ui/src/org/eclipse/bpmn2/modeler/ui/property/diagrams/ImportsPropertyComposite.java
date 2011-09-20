package org.eclipse.bpmn2.modeler.ui.property.diagrams;

import org.eclipse.bpmn2.modeler.ui.property.MainPropertiesComposite;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

public class ImportsPropertyComposite extends MainPropertiesComposite  {

	public ImportsPropertyComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected boolean canBindList(EStructuralFeature feature, EObject object) {
		if (super.canBindList(feature, object))
			return "imports".equals(feature.getName());
		return false;
	}

	@Override
	protected boolean canBindListColumn(EAttribute attribute, EObject object) {
		// TODO Auto-generated method stub
		return super.canBindListColumn(attribute, object);
	}
}
