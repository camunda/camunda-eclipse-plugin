package org.eclipse.bpmn2.modeler.ui.property.events;

import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class EventDefinitionListLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
		// empty
	}

	@Override
	public void dispose() {
		// empty
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return true;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// empty
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element != null && element instanceof EventDefinition) {
			return ((EventDefinition)element).getId();
		}
		return null;
	}

}
