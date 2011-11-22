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


package org.eclipse.bpmn2.modeler.ui.property.tasks;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.InputOutputSpecification;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.swt.widgets.Composite;

public class IoSpecificationPropertiesComposite extends AbstractBpmn2PropertiesComposite {

	public IoSpecificationPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param section
	 */
	public IoSpecificationPropertiesComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite
	 * #createBindings(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public void createBindings(final EObject be) {
		final EStructuralFeature ioSpecificationFeature = be.eClass().getEStructuralFeature("ioSpecification");
		if (ioSpecificationFeature != null) {
			InputOutputSpecification ioSpecification = (InputOutputSpecification)be.eGet(ioSpecificationFeature);
			if (ioSpecification==null) {
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						InputOutputSpecification newValue = Bpmn2Factory.eINSTANCE.createInputOutputSpecification();
						be.eSet(ioSpecificationFeature, newValue);
						ModelUtil.setID((EObject)newValue);
					}
				});
				ioSpecification = (InputOutputSpecification)be.eGet(ioSpecificationFeature);
			}
			
			EStructuralFeature dataInputsFeature = getFeature(ioSpecification, "dataInputs");
			bindList(ioSpecification, dataInputsFeature);

			EStructuralFeature dataOutputsFeature = getFeature(ioSpecification, "dataOutputs");
			bindList(ioSpecification, dataOutputsFeature);

			bindList(be, "dataInputAssociations");
			bindList(be, "dataOutputAssociations");
		}
	}
}