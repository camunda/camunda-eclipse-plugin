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
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.property.tasks;

import org.eclipse.bpmn2.InputOutputSpecification;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.PropertiesCompositeFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class IoSpecificationPropertySection extends AbstractBpmn2PropertySection {
	static {
		PropertiesCompositeFactory.register(InputOutputSpecification.class, IoSpecificationPropertiesComposite.class);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection#createSectionRoot()
	 */
	@Override
	protected AbstractBpmn2PropertiesComposite createSectionRoot() {
		return new IoSpecificationPropertiesComposite(this);
	}

	@Override
	protected EObject getBusinessObjectForPictogramElement(PictogramElement pe) {
		EObject be = super.getBusinessObjectForPictogramElement(pe);
		if (be!=null) {
			EStructuralFeature feature = be.eClass().getEStructuralFeature("ioSpecification");
			if (feature != null)
				return be;
		}
		return null;
	}
}
