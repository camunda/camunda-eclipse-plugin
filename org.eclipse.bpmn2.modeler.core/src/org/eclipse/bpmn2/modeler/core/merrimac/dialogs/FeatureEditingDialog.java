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

import org.eclipse.bpmn2.modeler.core.merrimac.clad.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.emf.transaction.RecordingCommand;

public class FeatureEditingDialog extends ObjectEditingDialog {

	protected EStructuralFeature feature;
	protected EObject newObject;
	protected boolean createNew = false;

	public FeatureEditingDialog(DiagramEditor editor, EObject object, EStructuralFeature feature, EObject value) {
		super(editor, object, (EClass)feature.getEType());
		this.feature = feature;
		this.newObject = value;
	}
	
	protected Composite createDialogContent(Composite parent) {
		if (newObject==null) {
			// create the new object
			createNew = true;
			ModelSubclassSelectionDialog dialog = new ModelSubclassSelectionDialog(editor, object, feature);
			if (dialog.open()==Window.OK){
				eclass = (EClass)dialog.getResult()[0];
				newObject = createNewObject(object, feature, eclass);
			}
			else
				cancel = true;
		}
		Composite content = PropertiesCompositeFactory.createDetailComposite(
				eclass.getInstanceClass(), parent, SWT.NONE);
		
		return content;
	}
	
	protected EObject createNewObject(final EObject object, final EStructuralFeature feature, final EClass eclass) {
		final EObject[] result = new EObject[1];
		final TransactionalEditingDomain domain = (TransactionalEditingDomainImpl)editor.getEditingDomain();
		if (domain!=null) {
			domain.getCommandStack().execute(new RecordingCommand(domain) {
				@Override
				protected void doExecute() {
					result[0] = ModelUtil.createFeature(object, feature, eclass);
				}
			});
		}
		else {
			result[0] = ModelUtil.createFeature(object, feature, eclass);
		}
		return result[0];
	}
	
	public void aboutToOpen() {
		String title;
		if (createNew)
			title = "Create New " + ModelUtil.getLabel(newObject);
		else
			title = "Edit " + ModelUtil.getLabel(newObject);
		getShell().setText(title);
		
		dialogContent.setData(newObject);
	}
	
	@Override
	protected void cancelPressed() {
		super.cancelPressed();
		newObject = null;
	}
	
	public EObject getNewObject() {
		return newObject;
	}
}