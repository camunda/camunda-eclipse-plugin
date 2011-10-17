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

import javax.xml.namespace.QName;

import org.eclipse.bpmn2.Import;
import org.eclipse.bpmn2.modeler.core.utils.NamespaceUtil;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.dialogs.SchemaSelectionDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;

/**
 * @author Bob Brodt
 * 
 */
public class SchemaObjectEditor extends TextAndButtonObjectEditor {

	protected Import imp;
	protected Button editButton;

	/**
	 * @param parent
	 * @param object
	 * @param feature
	 */
	public SchemaObjectEditor(AbstractBpmn2PropertiesComposite parent, EObject object, EStructuralFeature feature) {
		super(parent, object, feature);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.bpmn2.modeler.ui.property.editors.ObjectEditor#createControl
	 * (org.eclipse.swt.widgets.Composite, java.lang.String, int)
	 */
	@Override
	public Control createControl(Composite composite, String label, int style) {
		return super.createControl(composite, label, style);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.property.editors.TextAndButtonObjectEditor#buttonClicked()
	 */
	@Override
	protected void buttonClicked() {
		SchemaSelectionDialog dialog = new SchemaSelectionDialog(parent.getShell(), object);

		if (dialog.open() == Window.OK) {
			Object result = dialog.getResult()[0];
			String value = "";
			String selectionType = "";

			// TODO: do we need these?
			if (result instanceof PortType) {
				selectionType = "WSDL Port Type";
			}
			if (result instanceof Operation) {
				selectionType = "WSDL Operation";
			}
			if (result instanceof Input) {
				Input input = (Input)result;
				result = input.getMessage();
				selectionType = "WSDL Input";
			}
			if (result instanceof Output) {
				Output output = (Output)result;
				result = output.getMessage();
				selectionType = "WSDL Output";
			}
			if (result instanceof Fault) {
				Fault fault = (Fault)result;
				result = fault.getMessage();
				selectionType = "WSDL Fault";
			}
			if (result instanceof Part) {
				Part part = (Part)result;
				result = part.getElementDeclaration();
				selectionType = "WSDL Message Part";
			}
			if (result instanceof Message) {
				Message message = (Message)result;
				QName qname = message.getQName();
				String prefix = NamespaceUtil.getPrefixForNamespace(object, qname.getNamespaceURI());
				if (prefix==null)
					prefix = NamespaceUtil.addNamespace(object, qname.getNamespaceURI());
				if (prefix!=null)
					value = prefix + ":";
				value += qname.getLocalPart();
				selectionType = "WSDL Message";
			}
			if (result instanceof XSDAttributeDeclaration) {
				selectionType = "XML Attribute";
			}
			
			if (result instanceof XSDElementDeclaration) {
				XSDElementDeclaration decl = (XSDElementDeclaration)result;
				String ns = decl.getTargetNamespace();
				if (ns==null) {
					XSDTypeDefinition type = decl.getTypeDefinition();
					if (type!=null)
						ns = type.getTargetNamespace();
				}
				String prefix = NamespaceUtil.getPrefixForNamespace(object, ns);
				if (prefix!=null)
					value = prefix + ":";
				value += decl.getName();
			}
			if (result instanceof XSDTypeDefinition) {
				XSDTypeDefinition type = (XSDTypeDefinition)result;
				String prefix = NamespaceUtil.getPrefixForNamespace(object, type.getTargetNamespace());
				if (prefix!=null)
					value = prefix + ":";
				value += type.getName();
			}
			if (result instanceof XSDSchema) {
				XSDSchema schema = (XSDSchema)result;
				String prefix = NamespaceUtil.getPrefixForNamespace(object, schema.getTargetNamespace());
				if (prefix!=null)
					value = prefix + ":";
				value += "schema";
			}
			if (value.isEmpty()) {
				MessageDialog.openWarning(parent.getShell(), "Invalid Selection","The selection, "+
						selectionType+" is not a valid type definition.");
			}
			else
				updateObject(value);
		}
	}
}
