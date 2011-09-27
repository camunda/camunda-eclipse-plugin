package org.eclipse.bpmn2.modeler.ui.property.diagrams;

import java.util.List;

import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

public class ProcessDiagramPropertyComposite extends DefaultPropertiesComposite  {

	public ProcessDiagramPropertyComposite(Composite parent, int style) {
		super(parent, style);
	}

	protected void setBusinessObject(EObject object) {
		AbstractItemProvider itemProvider = new AbstractItemProvider(be) {
			@Override
			public String[] getAttributes() {
				return new String[] {
					"processType",
					"isExecutable",
					"isClosed"
				};
			}

			@Override
			public String[] getLists() {
				return new String[] {
					"properties",
					"laneSets",
					"correlationSubscriptions"
				};
			}

			@Override
			public String[] getReferences() {
				return new String[] {
					"definitionalCollaborationRef"
				};
			}
		};
		
		setItemProvider(itemProvider);
	}
}
