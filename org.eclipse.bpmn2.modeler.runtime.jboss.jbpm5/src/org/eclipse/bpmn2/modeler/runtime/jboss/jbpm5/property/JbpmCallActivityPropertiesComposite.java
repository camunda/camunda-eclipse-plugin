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

package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.property;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CallableElement;
import org.eclipse.bpmn2.modeler.core.utils.ErrorUtils;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.editors.ObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.TextAndButtonObjectEditor;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class JbpmCallActivityPropertiesComposite extends JbpmActivityPropertiesComposite {

	public JbpmCallActivityPropertiesComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public JbpmCallActivityPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void bindReference(Composite parent, EObject object, EReference reference) {
		if ("calledElementRef".equals(reference.getName())) {
			if (modelEnablement.isEnabled(object.eClass(), reference)) {
				if (parent==null)
					parent = getAttributesParent();
				
				String displayName = PropertyUtil.getLabel(object, reference);
				ObjectEditor editor = new TextAndButtonObjectEditor(this,object,reference) {

					@Override
					protected void buttonClicked() {
						IInputValidator validator = new IInputValidator() {

							@Override
							public String isValid(String newText) {
								if (newText==null || newText.isEmpty())
									return "Please enter the ID of a callable activity";
								return null;
							}
							
						};
						
						String initialValue = PropertyUtil.getText(object,feature);
						InputDialog dialog = new InputDialog(
								getShell(),
								"Called Activity",
								"Enter the ID of a callable activity",
								initialValue,
								validator);
						if (dialog.open()==Window.OK){
							updateObject(dialog.getValue());
						}
					}
					
					
					@Override
					protected boolean updateObject(final Object result) {
						if (result != object.eGet(feature)) {
							TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
							domain.getCommandStack().execute(new RecordingCommand(domain) {
								@Override
								protected void doExecute() {
									CallableElement ce = (CallableElement)Bpmn2Factory.eINSTANCE.create(Bpmn2Package.eINSTANCE.getCallableElement());
									((InternalEObject)ce).eSetProxyURI(URI.createURI((String)result));
									
									object.eSet(feature, ce);
								}
							});
							if (getDiagramEditor().getDiagnostics()!=null) {
								ErrorUtils.showErrorMessage(getDiagramEditor().getDiagnostics().getMessage());
							}
							else {
								ErrorUtils.showErrorMessage(null);
								updateText();
								return true;
							}
						}
						return false;
					}
				};
				editor.createControl(parent,displayName);
			}
		}
		else
			super.bindReference(parent, object, reference);
	}
}
