package org.eclipse.bpmn2.modeler.ui.property.diagrams;

import org.eclipse.bpmn2.modeler.ui.property.MainPropertiesComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

public class ProcessDiagramPropertyComposite extends MainPropertiesComposite  {

	public ProcessDiagramPropertyComposite(Composite parent, int style) {
		super(parent, style);
	}

	
	@Override
	protected boolean canBindList(EObject object, EStructuralFeature feature) {
		return "properties".equals(feature.getName());
	}
}
