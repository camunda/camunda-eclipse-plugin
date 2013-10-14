package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

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
	 * Relayout the properties after changes in the
	 * controls. 
	 */
	public void relayout() {

		// General challenge: We want the scroll panel to 
		// to update after the change in child elements 
		// The trick to achieve this is to send the resize event
		// to that particular composite.
		if (!parent.isDisposed()) {
			
			parent.getParent().layout();
			parent.getParent().redraw();
			
			Composite scrollableComposite = getScrollableComposite(parent);

			Event e = new Event();
			e.type = SWT.Resize;
			
			scrollableComposite.notifyListeners(SWT.Resize, e);
			
			scrollableComposite.redraw();
		}
	}
	
	
	/**
	 * Returns the layout container directly contained in a scrollable.
	 * 
	 * @param parent
	 * @return
	 */
	private Composite getScrollableComposite(Composite parent) {
		if (parent instanceof ScrolledComposite) {
			return parent;
		} else {
			return getScrollableComposite(parent.getParent());
		}
	}
}
