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
package org.eclipse.bpmn2.modeler.ui.property.data;

import org.eclipse.bpmn2.Assignment;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Expression;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class DataAssignmentPropertiesComposite extends DefaultPropertiesComposite {

	private DefaultPropertiesComposite fromDetails;
	private DefaultPropertiesComposite toDetails;

	public DataAssignmentPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param section
	 */
	public DataAssignmentPropertiesComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}
	
	@Override
	protected void cleanBindings() {
		super.cleanBindings();
		fromDetails = null;
		toDetails = null;
	}
	
	@SuppressWarnings("restriction")
	@Override
	public void createBindings(final EObject be) {
		
		if (be instanceof Assignment) {
			
			final Assignment assignment = (Assignment) be;
			
			// an Assignment is not really valid without both a From and To
			Expression toExp = assignment.getTo();
			if (toExp==null) {
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						Expression exp = Bpmn2Factory.eINSTANCE.createFormalExpression();
						assignment.setTo(exp);
						ModelUtil.setID(exp);
					}
				});
				toExp = assignment.getTo();
			}
			
			Expression fromExp = assignment.getFrom();
			if (fromExp==null) {
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						Expression exp = Bpmn2Factory.eINSTANCE.createFormalExpression();
						assignment.setFrom(exp);
						ModelUtil.setID(exp);
					}
				});
				fromExp = assignment.getFrom();
			}
			
			if (toDetails==null) {
				toDetails = new DefaultPropertiesComposite(this,SWT.BORDER);
				toDetails.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,3,1));
			}
			toDetails.setEObject(getDiagramEditor(), toExp);
			toDetails.setTitle("To Expression");
	
			if (fromDetails==null) {
				fromDetails = new DefaultPropertiesComposite(this,SWT.BORDER);
				fromDetails.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,3,1));
			}
			fromDetails.setEObject(getDiagramEditor(), fromExp);
			fromDetails.setTitle("From Expression");
	
			PropertyUtil.layoutAllParents(this);
		}
	}
}
