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

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.LoopCharacteristics;
import org.eclipse.bpmn2.MultiInstanceLoopCharacteristics;
import org.eclipse.bpmn2.StandardLoopCharacteristics;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.PropertyUtil;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.PropertiesCompositeFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class CallActivityPropertiesComposite extends ActivityPropertiesComposite {

	static {
		PropertiesCompositeFactory.register(StandardLoopCharacteristics.class, StandardLoopCharacteristicsPropertiesComposite.class);
		PropertiesCompositeFactory.register(MultiInstanceLoopCharacteristics.class, MultiInstanceLoopCharacteristicsPropertiesComposite.class);
	}

	private Button addStandardLoopButton;
	private Button addMultiLoopButton;

	public CallActivityPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param section
	 */
	public CallActivityPropertiesComposite(AbstractBpmn2PropertySection section) {
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
	public void createBindings(EObject be) {

		if (be instanceof CallActivity) {
			
			bindReference(be,"calledElementRef");

			final CallActivity callActivity = (CallActivity) be;
				
			addStandardLoopButton = new Button(this, SWT.PUSH);
			addStandardLoopButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
			addStandardLoopButton.addSelectionListener(new SelectionAdapter() {
				
				public void widgetSelected(SelectionEvent e) {
					@SuppressWarnings("restriction")
					TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							if (callActivity.getLoopCharacteristics() !=null)
								callActivity.setLoopCharacteristics(null);
							else {
								StandardLoopCharacteristics loopChar = Bpmn2Factory.eINSTANCE.createStandardLoopCharacteristics();
								callActivity.setLoopCharacteristics(loopChar);
								ModelUtil.setID(loopChar);
							}
							setEObject(callActivity);
						}
					});
				}
			});

			addMultiLoopButton = new Button(this, SWT.PUSH);
			addMultiLoopButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
			addMultiLoopButton.addSelectionListener(new SelectionAdapter() {
				
				public void widgetSelected(SelectionEvent e) {
					@SuppressWarnings("restriction")
					TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							if (callActivity.getLoopCharacteristics() !=null)
								callActivity.setLoopCharacteristics(null);
							else {
								MultiInstanceLoopCharacteristics loopChar = Bpmn2Factory.eINSTANCE.createMultiInstanceLoopCharacteristics();
								callActivity.setLoopCharacteristics(loopChar);
								ModelUtil.setID(loopChar);
							}
							setEObject(callActivity);
						}
					});
				}
			});

			LoopCharacteristics loopChar = (LoopCharacteristics) callActivity.getLoopCharacteristics();
			
			if (loopChar != null) {
				addStandardLoopButton.setText("Remove Loop Characteristics");
				addMultiLoopButton.setVisible(false);
//				this.be = loopChar;
				super.createBindingsSuper(loopChar);
			}
			else {
				addStandardLoopButton.setText("Add Standard Loop Characteristics");
				addMultiLoopButton.setText("Add Multi-Instance Loop Characteristics");
				addMultiLoopButton.setVisible(true);
			}
			PropertyUtil.recursivelayout(this);
		}
	}
}