package org.camunda.bpm.modeler.ui.property.tabs;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Base class of tab composite factories.
 * 
 * @author nico.rehwaldt
 */
public abstract class AbstractTabCompositeFactory<T extends BaseElement> {
	
	protected final GFPropertySection section;
	protected final Composite parent;

	public AbstractTabCompositeFactory(GFPropertySection section, Composite parent) {
		this.section = section;
		this.parent = parent;
	}
	
	/**
	 * Create composite for the given business object.
	 * 
	 * @param businessObject
	 * 
	 * @return
	 */
	public abstract Composite createCompositeForBusinessObject(T businessObject);
}
