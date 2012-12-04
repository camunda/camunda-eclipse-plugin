package org.eclipse.bpmn2.modeler.ui.property.tabs.factories;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

public abstract class PropertiesFactory {
	
	protected GFPropertySection section;
	protected EObject bo;
	protected Composite parent;

	public PropertiesFactory(Composite parent, GFPropertySection section, EObject bo) {
		this.section = section;
		this.bo = bo;
		this.parent = parent;
	}

	public abstract void create();

}
