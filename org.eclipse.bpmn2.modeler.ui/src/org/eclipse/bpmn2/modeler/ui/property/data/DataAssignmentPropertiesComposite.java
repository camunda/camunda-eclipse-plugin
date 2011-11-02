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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class DataAssignmentPropertiesComposite extends DefaultPropertiesComposite {

	private Button addRemoveToExpressionButton;
	private Button addRemoveFromExpressionButton;

	public DataAssignmentPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param section
	 */
	public DataAssignmentPropertiesComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	@SuppressWarnings("restriction")
	@Override
	public void createBindings(final EObject be) {
		
		if (be instanceof Assignment) {
			
			final Assignment assignment = (Assignment) be;
				
			addRemoveToExpressionButton = new Button(this, SWT.PUSH);
			addRemoveToExpressionButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
			addRemoveToExpressionButton.addSelectionListener(new SelectionAdapter() {
				
				public void widgetSelected(SelectionEvent e) {
					TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							if (assignment.getTo()!=null)
								assignment.setTo(null);
							else {
								Expression exp = Bpmn2Factory.eINSTANCE.createFormalExpression();
								assignment.setTo(exp);
								ModelUtil.setID(exp);
							}
							setEObject(be);
						}
					});
				}
			});
			addRemoveFromExpressionButton = new Button(this, SWT.PUSH);
			addRemoveFromExpressionButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
			addRemoveFromExpressionButton.addSelectionListener(new SelectionAdapter() {
				
				public void widgetSelected(SelectionEvent e) {
					TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							if (assignment.getFrom()!=null)
								assignment.setFrom(null);
							else {
								Expression exp = Bpmn2Factory.eINSTANCE.createFormalExpression();
								assignment.setFrom(exp);
								ModelUtil.setID(exp);
							}
							setEObject(be);
						}
					});
				}
			});
			Expression toExp = (Expression) assignment.getTo();
			if (toExp != null) {
				addRemoveToExpressionButton.setText("Remove To Expression");
				this.be = toExp;
				super.createBindings(toExp);
			}
			else {
				addRemoveToExpressionButton.setText("Add To Expression");
			}
			Expression fromExp = (Expression) assignment.getFrom();
			if (fromExp != null) {
				addRemoveFromExpressionButton.setText("Remove From Expression");
				this.be = fromExp;
				super.createBindings(fromExp);
			}
			else {
				addRemoveFromExpressionButton.setText("Add From Expression");
			}
//			this.propertySection.recursivelayout(this);
		}
		
	}

}
