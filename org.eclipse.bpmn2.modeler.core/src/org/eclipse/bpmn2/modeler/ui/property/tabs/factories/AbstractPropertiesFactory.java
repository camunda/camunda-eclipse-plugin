package org.eclipse.bpmn2.modeler.ui.property.tabs.factories;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Base class for all factories
 * 
 * @author nico.rehwaldt
 */
public abstract class AbstractPropertiesFactory {
	
	protected GFPropertySection section;
	protected EObject bo;
	protected Composite parent;

	public AbstractPropertiesFactory(Composite parent, GFPropertySection section, EObject bo) {
		this.section = section;
		this.bo = bo;
		this.parent = parent;
	}

	/**
	 * Perform the actual create operation
	 */
	public abstract void create();

}
