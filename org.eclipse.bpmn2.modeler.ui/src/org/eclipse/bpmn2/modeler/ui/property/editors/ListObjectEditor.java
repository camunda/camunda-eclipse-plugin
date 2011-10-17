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

package org.eclipse.bpmn2.modeler.ui.property.editors;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.adapters.AdapterUtil;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * @author Bob Brodt
 *
 */
public class ListObjectEditor extends MultivalueObjectEditor {

	/**
	 * @param parent
	 * @param object
	 * @param feature
	 */
	public ListObjectEditor(AbstractBpmn2PropertiesComposite parent, EObject object, EStructuralFeature feature) {
		super(parent, object, feature);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.property.editors.ObjectEditor#createControl(org.eclipse.swt.widgets.Composite, java.lang.String, int)
	 */
	@Override
	public Control createControl(Composite composite, String label, int style) {
		createLabel(composite, label);

		final Text text = getToolkit().createText(composite, "");
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button editButton = getToolkit().createButton(composite, "Edit...", SWT.PUSH);
		editButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Object eGet = object.eGet(feature);
		final List<EObject> refs = (List<EObject>) eGet;
		updateTextField(refs, text);

		SelectionAdapter editListener = new SelectionAdapter() {

			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				List values = getChoiceOfValues(object,feature);

				FeatureEditorDialog featureEditorDialog = new FeatureEditorDialog(parent.getShell(),
						AdapterUtil.getLabelProvider(), object, feature, "Select elements", values) {

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

					updateEObject(refs, (EList<EObject>) featureEditorDialog.getResult());
					updateTextField(refs, text);
				}
			}

			public void updateEObject(final List<EObject> refs, final EList<EObject> result) {
				TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {

						if (result == null) {
							refs.clear();
							return;
						}
						refs.retainAll(result);
						for (EObject di : result) {
							if (!refs.contains(di)) {
								refs.add(di);
							}
						}
					}
				});
			}
		};
		editButton.addSelectionListener(editListener);
		
		return text;
	}

	private void updateTextField(final List<EObject> refs, Text text) {
		String listText = "";
		if (refs != null) {
			for (int i = 0; i < refs.size() - 1; i++) {
				listText += AdapterUtil.getLabelProvider().getText(refs.get(i)) + ", ";
			}
			if (refs.size() > 0) {
				listText += AdapterUtil.getLabelProvider().getText(refs.get(refs.size() - 1));
			}
		}

		text.setText(listText);
	}
}
