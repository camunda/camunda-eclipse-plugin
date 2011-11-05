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
/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.property;

import java.util.ArrayList;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;

public class JbpmPropertiesComposite extends DefaultPropertiesComposite {

	private ArrayList<EStructuralFeature> attributes;
	private Button customEditorButton;
	private GridData buttonGridData;

	public JbpmPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}

	public JbpmPropertiesComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	@Override
	public void createBindings(EObject be) {

		customEditorButton = new Button(this, SWT.None);
		customEditorButton.setText("Open Custom Editor");
		buttonGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		customEditorButton.setLayoutData(buttonGridData);
		toolkit.adapt(customEditorButton, true, true);
		customEditorButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageDialog.openInformation(getShell(), "Custom Editor", "This dialog will contain a custom editor");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		boolean showCustomButton = be.eClass().getInstanceClass()
				.equals(Task.class);
		customEditorButton.setVisible(showCustomButton);
		buttonGridData.exclude = !showCustomButton;
	}

	private void updateDialogContents(MessageBox box) {
		for (EStructuralFeature eStructuralFeature : attributes) {
			if (eStructuralFeature.getName().equals("taskName"))
				box.setMessage("Here should be a custom editor for "
						+ be.eGet(eStructuralFeature) + " !");
		}
	}
}
