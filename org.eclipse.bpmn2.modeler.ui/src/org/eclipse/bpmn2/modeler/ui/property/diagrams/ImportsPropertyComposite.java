package org.eclipse.bpmn2.modeler.ui.property.diagrams;

import org.eclipse.bpmn2.modeler.ui.property.MainPropertiesComposite;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

public class ImportsPropertyComposite extends MainPropertiesComposite  {

	public ImportsPropertyComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected boolean canBindList(EObject object, EStructuralFeature feature) {
		return "imports".equals(feature.getName());
	}

	@Override
	protected boolean canBindListColumn(EObject object, EAttribute attribute) {
		return true;
	}

	@Override
	protected int getListStyleFlags(EObject object, EStructuralFeature feature) {
		return EDITABLE_LIST | ORDERED_LIST;
	}

	@Override
	protected boolean canBindAttribute(EObject object, EAttribute attribute) {
		return false;
	}
	
	@Override
	protected boolean canBindReference(EObject object, EReference reference) {
		return false;
	}
}
