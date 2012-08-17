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

import org.eclipse.bpmn2.modeler.core.merrimac.IConstants;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.utils.ErrorUtils;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
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
public abstract class ObjectEditor implements INotifyChangedListener {
	protected EObject object;
	protected EStructuralFeature feature;
	protected AbstractDetailComposite parent;
	private Label label;
	protected int style;
	
	public ObjectEditor(AbstractDetailComposite parent, EObject object, EStructuralFeature feature, int style) {
		this.parent = parent;
		this.object = object;
		this.feature = feature;
		this.style = style;
	}
	
	public ObjectEditor(AbstractDetailComposite parent, EObject object, EStructuralFeature feature) {
		this(parent, object, feature, SWT.NONE);
	}
	
	protected abstract Control createControl(Composite composite, String label, int style);
	
	public Control createControl(Composite composite, String label) {
		Control c = createControl(composite,label,style);
		c.setData(IConstants.NOTIFY_CHANGE_LISTENER_KEY, this);
		return c; 
	}
	
	public Control createControl(String label) {
		Control c = createControl(parent,label,style);
		c.setData(IConstants.NOTIFY_CHANGE_LISTENER_KEY, this);
		return c; 
	}
	
	public EStructuralFeature getFeature() {
		return feature;
	}

	public EObject getObject() {
		return object;
	}

	public void setObject(EObject object) {
		this.object = object;
	}
	
	public void setObject(EObject object, EStructuralFeature feature) {
		this.object = object;
		this.feature = feature;
	}
	
	protected FormToolkit getToolkit() {
		return parent.getToolkit();
	}
	
	protected DiagramEditor getDiagramEditor() {
		return parent.getDiagramEditor();
	}

	protected Diagram getDiagram() {
		return getDiagramEditor().getDiagramTypeProvider().getDiagram();
	}
	
	protected Label createLabel(Composite parent, String name) {
		label = getToolkit().createLabel(parent, name);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		return label;
	}
	
	public Label getLabel() {
		return label;
	}

	protected boolean updateObject(final Object result) {
		TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
		boolean success = ModelUtil.setValue(domain, object, feature, result);
		if (!success) {
			ErrorUtils.showErrorMessage(
					"Can't set '"+ModelUtil.getDisplayName(object)+
					"' "+ModelUtil.getLabel(object,feature)+
					" with value '"+ModelUtil.getDisplayName(result)+"'");
			return false;
		}
		return true;
	}
}
