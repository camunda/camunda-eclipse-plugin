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
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDialogComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

// TODO: this needs to be a FormDialog
public class FeatureEditingDialog extends Dialog {

	protected IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
	protected DiagramEditor editor;
	protected EObject object;
	protected EStructuralFeature feature;
	protected EObject newObject;
	protected boolean cancel = false;
	
	public FeatureEditingDialog(DiagramEditor editor, EObject object, EStructuralFeature feature, EObject value) {
		super(editor.getEditorSite().getShell());
		
		setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.MAX | SWT.RESIZE
				| getDefaultOrientation());
		
		this.editor = editor;
		this.object = object;
		this.feature = feature;
		this.newObject = value;
	}

	@Override
	public int open() {
		String title = null;
		if (newObject!=null)
			title = "Edit " + ModelUtil.getLabel(newObject);
		create();
		if (cancel)
			return Window.CANCEL;
		
		getShell().setSize(500,500);
		if (title==null)
			title = "Create New " + ModelUtil.getLabel(newObject);
		getShell().setText(title);
		
		aboutToOpen();
		return super.open();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);
		
		EClass eclass = (EClass)feature.getEType();
		if (newObject==null) {
			// create the new object
			ModelSubclassSelectionDialog dialog = new ModelSubclassSelectionDialog(editor, object, feature);
			if (dialog.open()==Window.OK){
				eclass = (EClass)dialog.getResult()[0];
				newObject = ModelUtil.createFeature(object, feature, eclass);
			}
			else
				cancel = true;
		}
		AbstractDetailComposite comp = PropertiesCompositeFactory.createDetailComposite(
				eclass.getInstanceClass(), dialogArea, SWT.NONE);
		comp.setBusinessObject(newObject);
		
		return dialogArea;
	}
	
	public void aboutToOpen() {
		if (newObject!=null) {
			final EClass eclass = newObject.eClass();
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