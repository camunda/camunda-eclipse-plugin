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

package org.eclipse.bpmn2.modeler.ui.property.dialogs;

import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.property.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

// TODO: this needs to be a FormDialog
public class ObjectEditingDialog extends Dialog {

	protected BPMN2Editor editor;
	protected EObject object;
	protected boolean cancel = false;
	
	public ObjectEditingDialog(BPMN2Editor editor, EObject object) {
		super(editor.getEditorSite().getShell());
		
		setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.MAX | SWT.RESIZE
				| getDefaultOrientation());
		
		this.editor = editor;
		this.object = object;
	}

	@Override
	public int open() {
		String title = null;
		if (object!=null)
			title = "Edit " + PropertyUtil.getLabel(object);
		create();
		if (cancel)
			return Window.CANCEL;
		
		getShell().setSize(500,500);
		if (title==null)
			title = "Create New " + PropertyUtil.getLabel(object);
		getShell().setText(title);
		return super.open();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);
		
		EClass eclass = object.eClass();
		AbstractDetailComposite comp = PropertiesCompositeFactory.createDetailComposite(
				eclass.getInstanceClass(), dialogArea, SWT.NONE);
		comp.setBusinessObject(object);
		
		return dialogArea;
	}
	
	@Override
	protected void cancelPressed() {
		super.cancelPressed();
		object = null;
	}

	public EObject getNewObject() {
		return object;
	}
}