package org.eclipse.bpmn2.modeler.ui.property.diagrams;

import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite.AbstractItemProvider;
import org.eclipse.emf.ecore.EObject;

public class ProcessDiagramPropertyComposite extends DefaultPropertiesComposite {

	private AbstractItemProvider itemProvider;

	public ProcessDiagramPropertyComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	@Override
	public AbstractItemProvider getItemProvider(EObject object) {
		if (itemProvider == null) {
			itemProvider = new AbstractItemProvider(object) {
				String[] attributes = new String[] { "processType", "isExecutable", "isClosed" };
				String[] lists = new String[] { "properties", "laneSets", "correlationSubscriptions" };
				String[] refs = new String[] { "definitionalCollaborationRef" };
				
				@Override
				public String[] getAttributes() {
					return attributes; 
				}

				@Override
				public boolean alwaysShowAdvancedProperties() {
					return true;
				}

				@Override
				public String[] getLists() {
					return lists;
				}

				@Override
				public String[] getReferences() {
					return refs;
				}
			};
		}
		return itemProvider;
	}
}
