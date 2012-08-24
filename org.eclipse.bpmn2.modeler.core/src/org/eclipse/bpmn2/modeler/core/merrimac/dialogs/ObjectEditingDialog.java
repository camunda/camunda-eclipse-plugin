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

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class ObjectEditingDialog extends FormDialog {

	protected IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
	protected DiagramEditor editor;
	protected EClass eclass;
	protected EObject object;
	protected boolean cancel = false;
	protected boolean abortOnCancel = true;

	protected Composite dialogContent;

	public ObjectEditingDialog(DiagramEditor editor, EObject object) {
		this(editor,object,object.eClass());
	}
	
	public ObjectEditingDialog(DiagramEditor editor, EObject object, EClass eclass) {
		super(editor.getEditorSite().getShell());
		
		setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.MAX | SWT.RESIZE
				| getDefaultOrientation());
		
		this.editor = editor;
		this.object = object;
		this.eclass = eclass;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		return super.createDialogArea(parent);
	}

	@Override
	protected void createFormContent(IManagedForm mform) {
		super.createFormContent(mform); 
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
		
		dialogContent = createDialogContent(body);
		
		data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		dialogContent.setLayoutData(data);

		getShell().setSize(600,400);
		
		form.setContent(body);
	}
	
	protected Composite createDialogContent(Composite parent) {
		Composite content = PropertiesCompositeFactory.createDialogComposite(
				eclass, parent, SWT.NONE);
		return content;
	}

	protected void addControlListener() {

		Point p = getShell().getSize();
		int width = preferenceStore.getInt("dialog."+eclass.getName()+".width");
		if (width==0)
			width = p.x;
		int height = preferenceStore.getInt("dialog."+eclass.getName()+".height");
		if (height==0)
			height = p.y;
		getShell().setSize(width,height);
		
		p = getShell().getLocation();
		int x = preferenceStore.getInt("dialog."+eclass.getName()+".x");
		if (x==0)
			x = p.x;
		int y = preferenceStore.getInt("dialog."+eclass.getName()+".y");
		if (y==0)
			y = p.y;
		getShell().setLocation(x,y);

		getShell().addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e)
			{
				Point p = getShell().getLocation();
				preferenceStore.setValue("dialog."+eclass.getName()+".x", p.x);
				preferenceStore.setValue("dialog."+eclass.getName()+".y", p.y);
			}
			
			public void controlResized(ControlEvent e)
			{
				Point p = getShell().getSize();
				preferenceStore.setValue("dialog."+eclass.getName()+".width", p.x);
				preferenceStore.setValue("dialog."+eclass.getName()+".height", p.y);
			}
	
		});
	}
	
	protected void aboutToOpen() {
		dialogContent.setData(object);
	}

	@Override
	public int open() {
		String title = null;
		if (object!=null)
			title = "Edit " + ModelUtil.getLabel(object);
		create();
		if (cancel)
			return Window.CANCEL;
		if (title==null)
			title = "Create New " + ModelUtil.getLabel(object);
		getShell().setText(title);

		addControlListener();
		aboutToOpen();

		final int result[] = new int[1];
		final String cancelMsg = getShell().getText()+" Dialog canceled by user";
		final TransactionalEditingDomainImpl domain = (TransactionalEditingDomainImpl)editor.getEditingDomain();
		if (domain!=null && domain.getActiveTransaction()==null) {
			domain.getCommandStack().execute(new RecordingCommand(domain) {
				@Override
				protected void doExecute() {
					result[0] = open(domain);
					if (result[0]!=Window.OK) {
						if (isAbortOnCancel()) {
							throw new OperationCanceledException(cancelMsg);
						}
					}
				}
			});
		}
		else {
			result[0] = open(domain);
			if (result[0]!=Window.OK) {
				if (isAbortOnCancel()) {
					if (domain!=null && domain.getActiveTransaction()!=null)
						domain.getActiveTransaction().abort(new Status(IStatus.INFO, Activator.PLUGIN_ID, cancelMsg));
				}
			}
		}
		
		return result[0];
	}
	
	protected int open(TransactionalEditingDomain domain) {
		return super.open();
	}
	
	/**
	 * Return state of the "abortOnCancel transaction on cancel" flag
	 * 
	 * @return true if the current transaction will be aborted if the dialog is canceled.
	 */
	public boolean isAbortOnCancel() {
		return abortOnCancel;
	}

	/**
	 * Abort the currently active transaction if dialog is canceled either by clicking the "Cancel"
	 * button, pressing the ESCAPE key or closing the dialog with the Window Close button.
	 * 
	 * @param abortOnCancel - if true, abortOnCancel the current transaction if dialog is canceled,
	 * otherwise allow transaction to commit.
	 */
	public void setAbortOnCancel(boolean abort) {
		this.abortOnCancel = abort;
	}

	@Override
	protected void cancelPressed() {
		dialogContent.dispose();
		super.cancelPressed();
	}
	
	@Override
	protected void okPressed() {
		dialogContent.dispose();
		super.okPressed();
	}
}