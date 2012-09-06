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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.IConstants;
import org.eclipse.bpmn2.modeler.core.adapters.AdapterRegistry;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.ui.celleditor.FeatureEditorDialog;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * EObject Reference List Editor.
 * This class implements an EObject reference list editor. The feature must be an EList of EObject references.
 * The list is rendered in a single-line text field with an "Edit" button to the right. Clicking the edit button
 * displays an EMF FeatureEditorDialog, which allows adding, removing and reordering of available object references.
 * 
 * @author Bob Brodt
 */
public class FeatureListObjectEditor extends MultivalueObjectEditor {

	Text text;
	List<EObject> references;
	Button editButton;
	
	/**
	 * @param parent
	 * @param object
	 * @param feature
	 */
	public FeatureListObjectEditor(AbstractDetailComposite parent, EObject object, EStructuralFeature feature) {
		super(parent, object, feature);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.property.editors.ObjectEditor#createControl(org.eclipse.swt.widgets.Composite, java.lang.String, int)
	 */
	@Override
	public Control createControl(Composite composite, String label, int style) {
		createLabel(composite, label);

		text = getToolkit().createText(composite, "");
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		references = (List<EObject>) object.eGet(feature);
		updateTextField();

		boolean canEdit = ModelUtil.canEdit(object,feature);
		boolean canCreateNew = ModelUtil.canCreateNew(object,feature);

		if (canEdit || canCreateNew) {
			Composite buttons =  getToolkit().createComposite(composite);
			buttons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			buttons.setLayout(new FillLayout(SWT.HORIZONTAL));

			if (canCreateNew) {
				// TODO: this isn't working yet.
//				Button createButton = getToolkit().createButton(buttons, "Add New...", SWT.PUSH);
//				createButton.addSelectionListener(new SelectionAdapter() {
//					public void widgetSelected(SelectionEvent e) {
//						// create a new target object
//						FeatureEditingDialog dialog = new FeatureEditingDialog(getDiagramEditor(), object, feature, null);							
//						if ( dialog.open() == Window.OK) {
//							updateObject(references, dialog.getNewObject());
//							updateTextField(references, text);
//						}
//					}
//				});
			}
			if (canEdit) {
				editButton = getToolkit().createButton(buttons, null, SWT.PUSH);
				editButton.setImage( Activator.getDefault().getImage(IConstants.ICON_ADD_20));

				editButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						Hashtable<String,Object> choices = getChoiceOfValues(object,feature);
						List values = new ArrayList();
						values.addAll(choices.values());

						FeatureEditorDialog featureEditorDialog = new FeatureEditorDialog(parent.getShell(),
								AdapterRegistry.getLabelProvider(), object, feature, "Select elements", values) {

							@Override
							protected Control createDialogArea(Composite parent) {
								if (parent.getLayoutData() instanceof GridData) {
									GridData data = (GridData)parent.getLayoutData();
									data.widthHint = Display.getCurrent().getBounds().width / 8;
								}
								Composite contents = (Composite)super.createDialogArea(parent);
								return contents;
							}

						};

						if (featureEditorDialog.open() == Window.OK) {
							updateEObject((EList<EObject>) featureEditorDialog.getResult());
							updateTextField();
						}
					}
				});
			}
		}

		return text;
	}

	private void updateEObject(final EList<EObject> result) {
		TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			@Override
			protected void doExecute() {

				if (result == null) {
					references.clear();
					return;
				}
				references.retainAll(result);
				for (EObject di : result) {
					if (!references.contains(di)) {
						references.add(di);
					}
				}
			}
		});
	}

	private void updateTextField() {
		String listText = "";
		if (references != null) {
			for (int i = 0; i < references.size() - 1; i++) {
				listText += AdapterRegistry.getLabelProvider().getText(references.get(i)) + ", ";
			}
			if (references.size() > 0) {
				listText += AdapterRegistry.getLabelProvider().getText(references.get(references.size() - 1));
			}
		}

		text.setText(listText);
	}
	
	@Override
	public void notifyChanged(Notification notification) {
		if (this.object == notification.getNotifier() &&
				this.feature == notification.getFeature()) {
			super.notifyChanged(notification);
			updateTextField();
		}
	}
}
