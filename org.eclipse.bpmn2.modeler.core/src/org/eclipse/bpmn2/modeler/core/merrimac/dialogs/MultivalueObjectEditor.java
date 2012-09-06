/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.core.merrimac.dialogs;

import java.util.Hashtable;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Base class for Object Editors whose feature is a multi-valued item; either a one-of-many
 * item such as a combo box or selection list, or a containment list.
 * 
 * @author Bob Brodt
 */
public abstract class MultivalueObjectEditor extends ObjectEditor {

	/**
	 * @param parent
	 * @param object
	 * @param feature
	 */
	public MultivalueObjectEditor(AbstractDetailComposite parent, EObject object, EStructuralFeature feature) {
		super(parent, object, feature);
	}
	
	/**
	 * Create the list of name/value pairs from the feature domain. The name string is
	 * intended to businessObject used for display in the editor widget, and the value is the corresponding
	 * feature value. If the values are null, then the name string is assumed to also businessObject
	 * the feature value.
	 * 
	 * The default implementation simply uses the EMF edit provider adapter to construct a valid
	 * list of values for the feature. If the list is empty or if an edit provider is not available,
	 * the default behavior is to construct a list of objects contained in the Resource which also
	 * contains the object being edited.  
	 *  
	 * @param object
	 * @param feature
	 * @return
	 */
	protected Hashtable<String,Object> getChoiceOfValues(EObject object, EStructuralFeature feature) {
		return ModelUtil.getChoiceOfValues(object, feature);
	}
}
