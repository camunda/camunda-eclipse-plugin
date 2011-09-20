package org.eclipse.bpmn2.modeler.ui.property.events;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class EventDefinitionListContentProvider implements IStructuredContentProvider {

	public EventDefinitionListContentProvider() {
		// empty
	}
	
	@Override
	public void dispose() {
		// empty
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// empty
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof EObject && !(inputElement instanceof DiagramElement)) {
			return getFilteredElements(inputElement).toArray();
		}
		return null;
	}

	private List<EObject> getFilteredElements(Object element) {
		List<EObject> ret = new ArrayList<EObject>();

		for (EObject eObject : ((EObject) element).eContents()) {
			if (eObject instanceof EventDefinition) {
				ret.add(eObject);
			}
		}
		return ret;
	}
}
