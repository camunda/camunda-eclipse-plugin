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

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.utils.ErrorUtils;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Bob Brodt
 *
 */
public class BooleanObjectEditor extends ObjectEditor {

	protected Button button;
	
	/**
	 * @param businessObject
	 * @param feature
	 */
	public BooleanObjectEditor(AbstractDetailComposite parent, EObject obj, EStructuralFeature feat) {
		super(parent, obj, feat);
	}
	
	public Control createControl(Composite composite, String label, int style) {

		// create a separate label to the LEFT of the checkbox, otherwise the grid layout will
		// businessObject off by one column for all other widgets that are created after this one.
		createLabel(composite, label);
		
		button = getToolkit().createButton(composite, "", SWT.CHECK);
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		button.setSelection((Boolean) object.eGet(feature));
		IObservableValue buttonObserver = SWTObservables.observeSelection(button);
		buttonObserver.addValueChangeListener(new IValueChangeListener() {
			
			@SuppressWarnings("restriction")
			@Override
			public void handleValueChange(ValueChangeEvent event) {

				if (!object.eGet(feature).equals(button.getSelection())) {
					TransactionalEditingDomain editingDomain = getDiagramEditor().getEditingDomain();
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
						@Override
						protected void doExecute() {
							object.eSet(feature, button.getSelection());
						}
					});
					
//					if (getDiagramEditor().getDiagnostics()!=null) {
//						// revert the change and display error errorList message.
//						button.setSelection((Boolean) object.eGet(feature));
//						ErrorUtils.showErrorMessage(getDiagramEditor().getDiagnostics().getMessage());
//					}
//					else
//						ErrorUtils.showErrorMessage(null);
					button.setSelection((Boolean) object.eGet(feature));
				}
			}
		});
		
		button.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				ErrorUtils.showErrorMessage(null);
			}
		});
		
		return button;
	}

	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		if (this.object == notification.getNotifier() &&
				this.feature == notification.getFeature()) {
			button.setSelection((Boolean) object.eGet(feature));
		}
	}
}
