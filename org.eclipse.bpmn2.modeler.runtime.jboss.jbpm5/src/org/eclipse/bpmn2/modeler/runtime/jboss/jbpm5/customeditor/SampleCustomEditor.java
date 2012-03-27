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

package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.customeditor;

/**
 * @author Bob Brodt
 *
 */
/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.ParameterDefinition;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.Work;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.WorkDefinition;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.WorkEditor;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.DataType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.impl.ParameterDefinitionImpl;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.impl.WorkImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Custom Work editor that can handle work definitions that only have
 * String parameters.
 */
public class SampleCustomEditor extends EditBeanDialog<Work> implements WorkEditor {

    private WorkDefinition workDefinition;
    private Map<String, Text> texts = new HashMap<String, Text>();
    
    public SampleCustomEditor(Shell parentShell) {
        super(parentShell, "Custom Work Editor");
        setBlockOnOpen(true);
    }
    
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        composite.setLayout(gridLayout);
        
        Work work = (Work) getValue();
        
        Label nameLabel = new Label(composite, SWT.NONE);
        nameLabel.setText("Name: ");
        Text nameText = new Text(composite, SWT.NONE);
        nameText.setEditable(false);
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        nameText.setLayoutData(gridData);
        String name = work.getName();
        nameText.setText(name == null ? "" : name);
        
        Set<ParameterDefinition> parameters = workDefinition.getParameters();
        for (ParameterDefinition param: parameters) {
            Label label = new Label(composite, SWT.NONE);
            label.setText(param.getName() + ": ");
            Text text = new Text(composite, SWT.NONE);
            gridData = new GridData();
            gridData.grabExcessHorizontalSpace = true;
            gridData.horizontalAlignment = GridData.FILL;
            text.setLayoutData(gridData);
            texts.put(param.getName(), text);
            Object value = work.getParameter(param.getName());
            text.setText(value == null ? "" : value.toString());
        }
        
        return composite;
    }
    
    protected Work updateValue(Work value) {
        Work work = new WorkImpl();
        work.setName(((Work) value).getName());
        for (Map.Entry<String, Text> entry: texts.entrySet()) {
            String text = entry.getValue().getText();
            ParameterDefinition pd = value.getParameterDefinition(entry.getKey());
            DataType type = pd.getType();
            if (!type.verifyDataType( type.readValue(text)) ) {
            	MessageDialog.openError(getShell(), "Data Type Error",
            			"The value for parameter "+entry.getKey()+
            			" does not conform to data type "+pd.getType().getStringType());
            	return value;
            }
            work.setParameter(entry.getKey(), "".equals(text) ? null : text);
        }
        work.setParameterDefinitions(((Work) value).getParameterDefinitions());
        return work;
    }
        
    public Work getWork() {
        return (Work) getValue();
    }

    public void setWork(Work work) {
        setValue(work);
    }

    public void setWorkDefinition(WorkDefinition workDefinition) {
        this.workDefinition = workDefinition;
    }

    public boolean show() {
        int result = open();
        return result == OK;
    }
}
