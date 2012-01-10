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
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.GatewayDirection;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.utils.PropertyUtil;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.adapters.AdapterUtil;
import org.eclipse.bpmn2.modeler.ui.adapters.Bpmn2ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.dialogs.FeatureEditingDialog;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * @author Bob Brodt
 *
 */
public class ComboObjectEditor extends MultivalueObjectEditor {

	protected ComboViewer comboViewer;
	
	/**
	 * @param parent
	 * @param object
	 * @param feature
	 */
	public ComboObjectEditor(AbstractBpmn2PropertiesComposite parent, EObject object, EStructuralFeature feature) {
		super(parent, object, feature);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.property.editors.ObjectEditor#createControl(org.eclipse.swt.widgets.Composite, java.lang.String, int)
	 */
	@Override
	public Control createControl(Composite composite, String label, int style) {
		createLabel(composite, label);

		boolean canEdit = canEdit();
		boolean canCreateNew = canCreateNew();
		
		comboViewer = createComboViewer(composite,
				AdapterUtil.getLabelProvider(), style);
		Combo combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, (canEdit || canCreateNew) ? 1 : 2, 1));
		combo.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				comboViewer = null;
			}
			
		});

		Composite buttons = null;
		if (canEdit || canCreateNew) {
			buttons =  getToolkit().createComposite(composite);
			buttons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			buttons.setLayout(new FillLayout(SWT.HORIZONTAL));

			if (canCreateNew) {
				Button createButton = getToolkit().createButton(buttons, "Create New...", SWT.PUSH);
				createButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						// create a new target object
						FeatureEditingDialog dialog = new FeatureEditingDialog(getDiagramEditor(), object, feature, null);							
						if ( dialog.open() == Window.OK) {
							EObject value = dialog.getNewObject();
							addEObject(value);
							fillCombo();
						}
					}
				});
			}
			if (canEdit) {
				Button editButton = getToolkit().createButton(buttons, "Edit...", SWT.PUSH);
				editButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						ISelection selection = comboViewer.getSelection();
						if (selection instanceof StructuredSelection) {
							String firstElement = (String) ((StructuredSelection) selection).getFirstElement();
							if ((firstElement != null && firstElement.isEmpty())) {
								// nothing to edit
								firstElement = null;
							}
							if (firstElement != null && comboViewer.getData(firstElement) instanceof EObject) {
								EObject value = (EObject) comboViewer.getData(firstElement);
								FeatureEditingDialog dialog = new FeatureEditingDialog(getDiagramEditor(),
										object, feature, value);
								if ( dialog.open() == Window.OK) {
									value = dialog.getNewObject();
									updateEObject(value);
									fillCombo();
								}
							}
						}
					}
				});
			}
		}
		
		fillCombo();

		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = comboViewer.getSelection();
				if (selection instanceof StructuredSelection) {
					String firstElement = (String) ((StructuredSelection) selection).getFirstElement();
					if(firstElement!=null && comboViewer.getData(firstElement)!=null)
						updateEObject(comboViewer.getData(firstElement));
					else {
						if (firstElement!=null && firstElement.isEmpty())
							firstElement = null;
						updateEObject(firstElement);
					}
				}
			}
		});
		
		return combo;
	}
	
	protected void fillCombo() {
		if (comboViewer!=null) {
			Object oldValue =  object.eGet(feature);
	
			while (comboViewer.getElementAt(0) != null)
				comboViewer.remove(comboViewer.getElementAt(0));
			
			Hashtable<String,Object> choices = getChoiceOfValues(object, feature);
			if (canSetNull()) {
				// selecting this one will set the target's value to null
				comboViewer.add("");
			}
			// add all other possible selections
			for (Entry<String, Object> entry : choices.entrySet()) {
				comboViewer.add(entry.getKey());
				Object newValue = entry.getValue(); 
				if (newValue!=null) {
					comboViewer.setData(entry.getKey(), newValue);
					if (newValue==oldValue)
						comboViewer.setSelection(new StructuredSelection(entry.getKey()));
				}
			}
		}
	}

	protected void updateEObject(final Object result) {
		TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			@Override
			protected void doExecute() {
				object.eSet(feature, result);
			}
		});
	}

	protected void addEObject(final Object result) {
		TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			@Override
			protected void doExecute() {
				getDiagramEditor().getModelHandler().set(object, feature, (EObject)result);
			}
		});
	}

	private ComboViewer createComboViewer(Composite parent, AdapterFactoryLabelProvider labelProvider, int style) {
		ComboViewer comboViewer = new ComboViewer(parent, style);
		comboViewer.setLabelProvider(labelProvider);

		Combo combo = comboViewer.getCombo();
		
		return comboViewer;
	}
}
