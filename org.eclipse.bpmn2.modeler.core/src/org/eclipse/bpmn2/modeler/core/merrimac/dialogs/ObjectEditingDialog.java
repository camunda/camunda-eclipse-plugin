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

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDialogComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

// TODO: this needs to be a FormDialog
public class ObjectEditingDialog extends FormDialog {

	protected DiagramEditor editor;
	protected EObject object;
	protected boolean cancel = false;
	protected AbstractDialogComposite dialogContent;
	
	public ObjectEditingDialog(DiagramEditor editor, EObject object) {
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
			title = "Edit " + ModelUtil.getLabel(object);
		create();
		if (cancel)
			return Window.CANCEL;
		
//		getShell().setSize(500,500);
		if (title==null)
			title = "Create New " + ModelUtil.getLabel(object);
		getShell().setText(title);
		
		dialogContent.aboutToOpen();
		return super.open();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		return super.createDialogArea(parent);
	}

	@Override
	protected void createFormContent(IManagedForm mform) {
		super.createFormContent(mform); 

		EClass eclass = object.eClass();
		final ScrolledForm form = mform.getForm();
		form.setExpandHorizontal(true);
		form.setExpandVertical(true);
		form.setText(null);

		Composite body = form.getBody();
		body.setBackground(form.getBackground());

		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		body.setLayoutData(data);
		body.setLayout(new FormLayout());
		
		dialogContent = PropertiesCompositeFactory.createDialogComposite(
				eclass.getInstanceClass(), body, SWT.TOP);
		
		data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		dialogContent.getControl().setLayoutData(data);

		form.setContent(body);

		dialogContent.setBusinessObject(object);
	}

//	@Override
//	protected Control createDialogArea(Composite parent) {
//		Composite dialogArea = (Composite) super.createDialogArea(parent);
//		
//		EClass eclass = object.eClass();
//		AbstractDialogComposite dialogContent = PropertiesCompositeFactory.createDialogComposite(
//				eclass.getInstanceClass(), dialogArea, SWT.TOP);
//		dialogContent.setBusinessObject(object);
//		
//		return dialogArea;
//	}
	
	@Override
	protected void cancelPressed() {
		super.cancelPressed();
		object = null;
	}

	public EObject getNewObject() {
		return object;
	}
}