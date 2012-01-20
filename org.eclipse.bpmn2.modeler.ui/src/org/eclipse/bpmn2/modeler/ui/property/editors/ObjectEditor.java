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

import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.AdapterUtil;
import org.eclipse.bpmn2.modeler.ui.adapters.Bpmn2ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Base class for EObject feature editors. All subclasses must render the given object's feature,
 * which may be either an attribute, a reference to an EObject, or a list of EObject references.
 * Subclasses must also provide means to populate the display widgets from the feature and save
 * modifications to the feature made in the display widget.
 * 
 * @author Bob Brodt
 */
public abstract class ObjectEditor {
	protected EObject object;
	protected EStructuralFeature feature;
	protected AbstractBpmn2PropertiesComposite parent;
	
	public ObjectEditor(AbstractBpmn2PropertiesComposite parent, EObject object, EStructuralFeature feature) {
		this.parent = parent;
		this.object = object;
		this.feature = feature;
	}
	
	public abstract Control createControl(Composite composite, String label, int style);
	
	public Control createControl(Composite composite, String label) {
		return createControl(composite,label,SWT.NONE);
	}
	
	public Control createControl(String label) {
		return createControl(parent,label,SWT.NONE);
	}
	
	protected FormToolkit getToolkit() {
		return parent.getToolkit();
	}
	
	protected BPMN2Editor getDiagramEditor() {
		return parent.getDiagramEditor();
	}

	protected Diagram getDiagram() {
		return getDiagramEditor().getDiagramTypeProvider().getDiagram();
	}
	
	protected Label createLabel(Composite parent, String name) {
		Label label = getToolkit().createLabel(parent, name);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		return label;
	}

	protected boolean updateObject(final Object result) {
		if (result != object.eGet(feature)) {
			InsertionAdapter insertionAdapter = AdapterUtil.adapt(object, InsertionAdapter.class);
			if (insertionAdapter!=null) {
				// make sure the new object is added to its container first
				// so that it inherits the container's Resource and EditingDomain
				// before we try to change its value.
				insertionAdapter.execute();
			}
			
			// use the Extended Properties adapter if there is one
			Bpmn2ExtendedPropertiesAdapter adapter = AdapterUtil.adapt(object, Bpmn2ExtendedPropertiesAdapter.class);
			if (adapter!=null) {
				adapter.getFeatureDescriptor(feature).setValue(object, result);
				return true;
			}
		
			// fallback is to set the new value here
			TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
			domain.getCommandStack().execute(new RecordingCommand(domain) {
				@Override
				protected void doExecute() {
					object.eSet(feature, result);
				}
			});
		}
		return true;
	}
}
