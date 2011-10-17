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

package org.eclipse.bpmn2.modeler.ui.property.editors;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Bob Brodt
 *
 */
public abstract class MultivalueObjectEditor extends ObjectEditor {

	/**
	 * @param parent
	 * @param object
	 * @param feature
	 */
	public MultivalueObjectEditor(AbstractBpmn2PropertiesComposite parent, EObject object, EStructuralFeature feature) {
		super(parent, object, feature);
	}
	
	protected List getChoiceOfValues(EObject object, EStructuralFeature feature) {
		List values = null;
		IItemPropertyDescriptor propertyDescriptor = AbstractBpmn2PropertiesComposite.getPropertyDescriptor(object, feature);
		
		if (propertyDescriptor!=null) {
			try {
				values = (List) propertyDescriptor.getChoiceOfValues(object);
			}
			catch(Exception e) {
				// ignore NPE from eResolveProxy() if unable to resolve
			}
		}
		if (values==null) {
			try {
				ModelHandler modelHandler = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
				values = (List<Object>) modelHandler.getAll(feature.getEType().getInstanceClass());
			} catch (IOException e1) {
				Activator.showErrorWithLogging(e1);
			}
		}
		return values;
	}
}
