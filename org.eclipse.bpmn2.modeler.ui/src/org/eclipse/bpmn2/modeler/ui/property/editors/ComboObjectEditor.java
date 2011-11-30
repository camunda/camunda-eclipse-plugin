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

import org.eclipse.bpmn2.GatewayDirection;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.adapters.AdapterUtil;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

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

		comboViewer = createComboViewer(composite,
				AdapterUtil.getLabelProvider(), style);
		Combo combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Object oldValue =  object.eGet(feature);

		Hashtable<String,Object> choices = getChoiceOfValues(object, feature);
		comboViewer.add("");
		for (Entry<String, Object> entry : choices.entrySet()) {
			comboViewer.add(entry.getKey());
			Object newValue = entry.getValue(); 
			if (newValue!=null) {
				comboViewer.setData(entry.getKey(), newValue);
				if (newValue==oldValue)
					comboViewer.setSelection(new StructuredSelection(entry.getKey()));
			}
		}

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

	protected void updateEObject(final Object result) {
		TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			@Override
			protected void doExecute() {
				object.eSet(feature, result);
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
