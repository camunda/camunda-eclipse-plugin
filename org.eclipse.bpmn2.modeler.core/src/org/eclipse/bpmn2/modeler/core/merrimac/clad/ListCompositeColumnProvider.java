package org.eclipse.bpmn2.modeler.core.merrimac.clad;

import org.eclipse.bpmn2.modeler.core.merrimac.providers.ColumnTableProvider;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class ListCompositeColumnProvider extends ColumnTableProvider {
	protected final AbstractListComposite listComposite;
	protected boolean canModify = true;

	public ListCompositeColumnProvider(AbstractListComposite list) {
		this(list,false);
	}
	
	public ListCompositeColumnProvider(AbstractListComposite list, boolean canModify) {
		super();
		this.canModify = canModify;
		this.listComposite = list;
	}
	
	/**
	 * Implement this to select which columns are editable
	 * @param object - the list object
	 * @param feature - the feature of the item contained in the list
	 * @param item - the selected item in the list
	 * @return true to allow editing
	 */
	public boolean canModify(EObject object, EStructuralFeature feature, EObject item) {
		return canModify;
	}
	
	public TableColumn add(EObject object, EStructuralFeature feature) {
		return add(object, (EClass)feature.eContainer(), feature);
	}
	
	public TableColumn add(EObject object, EClass eclass, EStructuralFeature feature) {
		TableColumn tc = null;
		listComposite.getModelEnablement(object);
		if (listComposite.isModelObjectEnabled(eclass,feature)) {
			tc = new TableColumn(object, feature);
			tc.setOwner(listComposite);
			super.add(tc);
		}
		return tc;
	}
	
	public TableColumn add(TableColumn tc) {
		EStructuralFeature feature = tc.feature;
		EObject object = tc.object;
		listComposite.getModelEnablement(object);
		if (object!=null) {
			if (listComposite.isModelObjectEnabled(object.eClass(),feature)) {
				tc.setOwner(listComposite);
				super.add(tc);
				return tc;
			}
		}
		if (feature!=null) {
			EClass eclass = (EClass)feature.eContainer();
			if (listComposite.isModelObjectEnabled(eclass,feature)) {
				tc.setOwner(listComposite);
				super.add(tc);
				return tc;
			}
		}
		return tc;
	}
}