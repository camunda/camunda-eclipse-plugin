package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Base class for all factories
 * 
 * @author nico.rehwaldt
 */
public abstract class AbstractPropertiesBuilder<T extends BaseElement> {
	
	protected GFPropertySection section;
	protected T bo;
	protected Composite parent;

	public AbstractPropertiesBuilder(Composite parent, GFPropertySection section, T bo) {
		this.section = section;
		this.bo = bo;
		this.parent = parent;
	}

	/**
	 * Perform the actual create operation
	 */
	public abstract void create();

	/**
	 * 
	 * @param control
	 */
	public void relayout() {
		if (!parent.isDisposed()) {
			relayoutAll(parent);
		}
	}
	
	/**
	 * 
	 * @param parent
	 * @return
	 */
	private Composite relayoutAll(Composite parent) {
		parent.layout(true, true);
		parent.redraw();
		
		if (parent instanceof ScrolledComposite) {
			return parent;
		} else {
			return relayoutAll(parent.getParent());
		}
	}
}
