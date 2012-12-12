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

package org.eclipse.bpmn2.modeler.ui.property.dialogs;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Bob Brodt
 *
 */
public class NamespacesEditingDialog extends InputDialog {
	
	Text namespaceText;
	String namespace;
	Text namespaceErrorText;
	Map<String,String> map;
	
	public NamespacesEditingDialog(Shell shell, String title, final Map<String,String> map, final String prefix, String namespace) {
		super(
			shell,
			title,
			"Prefix",
			prefix,
			new IInputValidator() {

				@Override
				public String isValid(String newText) {
					if (newText==null || newText.isEmpty())
						return "Prefix can not be empty";
					if (newText.equals(prefix))
						return null;
					if (map.containsKey(newText))
						return "The prefix '"+newText+"' is already defined.";
					return null;
				}
			}
		);
		this.namespace = namespace;
		this.map = map;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite)super.createDialogArea(parent);
		composite.getChildren();
		if (namespace!=null) {
            Label label = new Label(composite, SWT.WRAP);
            label.setText("Namespace");
	            GridData data = new GridData(GridData.GRAB_HORIZONTAL
	                    | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL
	                    | GridData.VERTICAL_ALIGN_CENTER);
	            data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
	            label.setLayoutData(data);
	            label.setFont(parent.getFont());

		        namespaceText = new Text(composite, getInputTextStyle());
		        namespaceText.setText(namespace);
		        namespaceText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
		                | GridData.HORIZONTAL_ALIGN_FILL));
		        namespaceText.addModifyListener(new ModifyListener() {
		            public void modifyText(ModifyEvent e) {
		                validateInput();
		            }
		        });
		        
		        namespaceErrorText = new Text(composite, SWT.READ_ONLY | SWT.WRAP);
		        namespaceErrorText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
		                | GridData.HORIZONTAL_ALIGN_FILL));
		        namespaceErrorText.setBackground(namespaceErrorText.getDisplay()
		                .getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		        
			}				
			return composite;
		}
	
		protected void validateInput() {
			super.validateInput();
			if (namespaceText!=null) {
				String msg = null;
				String ns = namespaceText.getText();
				if (ns==null || ns.isEmpty())
					msg = "Namespace can not be empty";
				try {
					URI uri = URI.createURI(ns);
				}
				catch (Exception e) {
					msg = "Namespace is not a valid URI";
				}
				namespaceErrorText.setText(msg == null ? "" : msg);
	    		Control button = getButton(IDialogConstants.OK_ID);
	    		if (button != null) {
	    			if (button.isEnabled())
	    				button.setEnabled(msg == null);
	    		}
			}
		}
		
		@Override
	    protected void buttonPressed(int buttonId) {
	        if (buttonId == IDialogConstants.OK_ID) {
	            namespace = namespaceText.getText();
	        } else {
	            namespace = null;
	        }
	        super.buttonPressed(buttonId);
	    }

	    public String getPrefix() {
	    	return getValue();
	    }
	    
		public String getNamespace() {
			return namespace;
		}
	}
