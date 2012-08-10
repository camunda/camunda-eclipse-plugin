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

package org.eclipse.bpmn2.modeler.ui.property.data;

import org.eclipse.bpmn2.Interface;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite.AbstractPropertiesProvider;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.editors.SchemaObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class InterfaceDetailComposite extends DefaultDetailComposite {

	/**
	 * @param parent
	 * @param style
	 */
	public InterfaceDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param section
	 */
	public InterfaceDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	@Override
	public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
		if (propertiesProvider==null) {
			propertiesProvider = new AbstractPropertiesProvider(object) {
				String[] properties = new String[] {
						"name",
						"implementationRef",
						"operations"
				};
				
				@Override
				public String[] getProperties() {
					return properties; 
				}
			};
		}
		return propertiesProvider;
	}

	@Override
	protected void bindReference(Composite parent, EObject object, EReference reference) {
		if ("implementationRef".equals(reference.getName()) &&
				modelEnablement.isEnabled(object.eClass(), reference)) {
			
			if (parent==null)
				parent = getAttributesParent();
			
			final Interface iface = (Interface)object;
			String displayName = ModelUtil.getLabel(object, reference);
			
			SchemaObjectEditor editor = new SchemaObjectEditor(this,object,reference);
			editor.createControl(parent,displayName);
		}
		else
			super.bindReference(parent, object, reference);
	}
}
