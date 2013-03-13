package org.camunda.bpm.modeler.core.utils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Utilities around selections
 * 
 * @author nico.rehwaldt
 */
public class SelectionUtil {

	/**
	 * Returns the object contained in the selection
	 * 
	 * @param selection
	 * @return
	 */
	public static Object getSelectedModel(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			
			Object primarySelection = structuredSelection.getFirstElement();
			if (primarySelection instanceof AbstractEditPart) {
				return ((AbstractEditPart) primarySelection).getModel();
			}
			
			return null;
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the business object referenced in the selection
	 * 
	 * @param selection
	 * @return
	 */
	public static EObject getSelectedBusinessObject(ISelection selection) {
		Object model = getSelectedModel(selection);
		return getSelectedBusinessObject(model);
	}
	
	/**
	 * Given a diagram element (which may be null), return the associated business object. 
	 * 
	 * @param model
	 * @return
	 */
	public static EObject getSelectedBusinessObject(Object model) {
		
		if (model != null && model instanceof PictogramElement) {
			EObject businessObject = BusinessObjectUtil.getBusinessObjectForPictogramElement((PictogramElement) model);
			return businessObject;
		}
		
		return null;
	}
}
