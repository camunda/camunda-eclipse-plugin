package org.eclipse.bpmn2.modeler.ui.property.diagrams;

import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite;
import org.eclipse.emf.ecore.EObject;

public class ProcessDiagramPropertyComposite extends DefaultPropertiesComposite  {

	public ProcessDiagramPropertyComposite(AbstractBpmn2PropertySection section) {
		super(section);
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
