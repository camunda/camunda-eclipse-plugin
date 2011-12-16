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
package org.eclipse.bpmn2.modeler.ui.property.connectors;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Expression;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.PropertyUtil;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class SequenceFlowPropertiesComposite extends DefaultPropertiesComposite {

	private Button addRemoveConditionButton;
	private Button setDefaultFlowCheckbox;
	
	public SequenceFlowPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param section
	 */
	public SequenceFlowPropertiesComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	@SuppressWarnings("restriction")
	@Override
	public void createBindings(final EObject be) {
		
		if (be instanceof SequenceFlow) {
			
			final SequenceFlow sequenceFlow = (SequenceFlow) be;
				
			addRemoveConditionButton = new Button(this, SWT.PUSH);
			addRemoveConditionButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
			addRemoveConditionButton.addSelectionListener(new SelectionAdapter() {
				
				public void widgetSelected(SelectionEvent e) {
					TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							if (sequenceFlow.getConditionExpression()!=null)
								sequenceFlow.setConditionExpression(null);
							else {
								Expression exp = Bpmn2Factory.eINSTANCE.createFormalExpression();
								sequenceFlow.setConditionExpression(exp);
								ModelUtil.setID(exp);
								
								setDefault(sequenceFlow,null);
							}
							setEObject(be);
						}
					});
				}
			});
			Expression exp = (Expression) sequenceFlow.getConditionExpression();
			
			setDefaultFlowCheckbox = new Button(this, SWT.CHECK);
			setDefaultFlowCheckbox.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
			setDefaultFlowCheckbox.addSelectionListener(new SelectionAdapter() {
				
				public void widgetSelected(SelectionEvent e) {
					TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							if (getDefault(sequenceFlow) != sequenceFlow)
								setDefault(sequenceFlow,sequenceFlow);
							else
								setDefault(sequenceFlow,null);
						}
					});
				}
			});
			
			if (exp != null) {
				addRemoveConditionButton.setText("Remove Condition");
				setDefaultFlowCheckbox.setVisible(false);
				this.be = exp;
				super.createBindings(exp);
			}
			else {
				addRemoveConditionButton.setText("Add Condition");
				if (sequenceFlow.getSourceRef() instanceof FlowNode) {
					FlowNode flowNode = (FlowNode)sequenceFlow.getSourceRef();
//					Object adapter = flowNode.eClass().
					String objectName = flowNode.getName();
					if (objectName!=null && objectName.isEmpty())
						objectName = null;
					String typeName = ModelUtil.getDisplayName(flowNode,null);
					setDefaultFlowCheckbox.setVisible(true);
					setDefaultFlowCheckbox.setSelection( getDefault(sequenceFlow) == sequenceFlow );
					setDefaultFlowCheckbox.setText("Default Flow for "+ typeName +
							(objectName==null ? "" : (" \"" + objectName + "\"")));
				}
				else {
					setDefaultFlowCheckbox.setVisible(false);
				}
			}
			PropertyUtil.recursivelayout(this);
		}
		
	}
	
	private void setDefault(SequenceFlow sf, EObject target) {

		EObject obj = sf.getSourceRef();
		if (obj!=null) {
			EStructuralFeature feature = obj.eClass().getEStructuralFeature("default");
			if (feature!=null && obj.eGet(feature)!=target) {
				obj.eSet(feature, target);
			}
		}
	}
	
	private EObject getDefault(SequenceFlow sf) {
		EObject obj = sf.getSourceRef();
		if (obj!=null) {
			EStructuralFeature feature = obj.eClass().getEStructuralFeature("default");
			if (feature!=null) {
				return (EObject) obj.eGet(feature);
			}
		}
		return null;
	}
}
