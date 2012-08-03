package org.eclipse.bpmn2.modeler.ui.property;

import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.ui.property.dialogs.ModelSubclassSelectionDialog;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class DefaultListComposite extends AbstractListComposite {
	protected EClass listItemClass;

	public DefaultListComposite(AbstractBpmn2PropertySection section, int style) {
		super(section, style);
	}

	public DefaultListComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public DefaultListComposite(Composite parent, int style) {
		super(parent, style);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.property.AbstractListComposite#addListItem(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	protected EObject addListItem(EObject object, EStructuralFeature feature) {
		EList<EObject> list = (EList<EObject>)object.eGet(feature);
		EClass listItemClass = getListItemClass(object,feature);
		EObject newItem = null;
		if (!(list instanceof EObjectContainmentEList)) {
			// this is not a containment list so we can't add it
			// because we don't know where the new object belongs
			
			MessageDialog.openError(getShell(), "Internal Error",
					"Can not create a new " +
					listItemClass.getName() +
					" because the list is not a container. " +
					"The default addListItem() method must be implemented."
			);
			return null;
		}
		else {
			if (listItemClass==null) {
				listItemClass = getListItemClassToAdd(listItemClass);
				if (listItemClass==null)
					return null; // user cancelled
			}
			newItem = PropertyUtil.createFeature(object,feature,listItemClass);
			list.add(newItem);
		}
		return newItem;
	}
	
	/**
	 * Find all subtypes of the given listItemClass EClass and display a selection
	 * list if there are more than 1 subtypes.
	 * 
	 * @param listItemClass
	 * @return
	 */
	public EClass getListItemClassToAdd(EClass listItemClass) {
		EClass eclass = null;
		ModelSubclassSelectionDialog dialog = new ModelSubclassSelectionDialog(getDiagramEditor(), businessObject, feature);
		if (dialog.open()==Window.OK){
			eclass = (EClass)dialog.getResult()[0];
		}
		return eclass;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.property.AbstractListComposite#createDetailComposite(org.eclipse.swt.widgets.Composite, java.lang.Class)
	 */
	public AbstractDetailComposite createDetailComposite(Composite parent, Class eClass) {
		AbstractDetailComposite composite = PropertiesCompositeFactory.createDetailComposite(eClass, parent, SWT.NONE);
		return composite;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.property.AbstractListComposite#editListItem(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	protected EObject editListItem(EObject object, EStructuralFeature feature) {
		MessageDialog.openError(getShell(), "Internal Error",
				"A List Item Editor has not been defined for "+
				PropertyUtil.getDisplayName(object, feature)
				);
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.property.AbstractListComposite#removeListItem(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature, int)
	 */
	protected Object removeListItem(EObject object, EStructuralFeature feature, int index) {
		EList<EObject> list = (EList<EObject>)object.eGet(feature);
		int[] map = buildIndexMap(object,feature);
		EObject selected = null;
		if (index<map.length-1)
			selected = list.get(map[index+1]);
		list.remove(map[index]);
		return selected;
	}
	
	protected Object deleteListItem(EObject object, EStructuralFeature feature, int index) {
		EList<EObject> list = (EList<EObject>)object.eGet(feature);
		int[] map = buildIndexMap(object,feature);
		EObject removed = list.get(map[index]);
		EObject selected = null;
		if (index<map.length-1)
			selected = list.get(map[index+1]);
		// this ensures that all references to this Interface are removed
		EcoreUtil.delete(removed);
		return selected;
	}
	
	protected Object moveListItemUp(EObject object, EStructuralFeature feature, int index) {
		EList<EObject> list = (EList<EObject>)object.eGet(feature);
		int[] map = buildIndexMap(object,feature);
		if (index>0) {
			list.move(map[index-1], map[index]);
			return list.get(map[index-1]);
		}
		return null;
	}

	protected Object moveListItemDown(EObject object, EStructuralFeature feature, int index) {
		EList<EObject> list = (EList<EObject>)object.eGet(feature);
		int[] map = buildIndexMap(object,feature);
		if (index<map.length-1) {
			list.move(map[index+1], map[index]);
			return list.get(map[index+1]);
		}
		return null;
	}

	public void setListItemClass(EClass clazz) {
		this.listItemClass = clazz;
	}
	
	public EClass getListItemClass(EObject object, EStructuralFeature feature) {
		return listItemClass;
	}
}
