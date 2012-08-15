package org.eclipse.bpmn2.modeler.core.merrimac.clad;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ListCompositeContentProvider implements IStructuredContentProvider {
	/**
	 * 
	 */
	protected final AbstractListComposite listComposite;
	protected EObject object;
	protected EStructuralFeature feature;
	protected EList<EObject> list;
	
	public ListCompositeContentProvider(AbstractListComposite listComposite, EObject object, EStructuralFeature feature, EList<EObject> list) {
		this.listComposite = listComposite;
		this.object = object;
		this.feature = feature;
		this.list = list;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		EClass listItemClass = listComposite.getListItemClass(object,feature);
		if (listItemClass==null) {
			// display all items in the list that are subclasses of listItemClass
			return list.toArray();
		}
		else {
			// we're only interested in display specific EClass instances
			List<EObject> elements = new ArrayList<EObject>();
			for (EObject o : list) {
				EClass ec = o.eClass();
				if (ec == listItemClass)
					elements.add(o);
			}
			return elements.toArray(new EObject[elements.size()]);
		}
	}
}