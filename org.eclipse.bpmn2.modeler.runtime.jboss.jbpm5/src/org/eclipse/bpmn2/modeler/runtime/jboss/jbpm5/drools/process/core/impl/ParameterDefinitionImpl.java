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

package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.impl;

import java.io.Serializable;

import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.ParameterDefinition;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.DataType;

public class ParameterDefinitionImpl implements ParameterDefinition, Serializable {
   
    private static final long serialVersionUID = 510l;
   
    private String name;
    private DataType type;
    
    public ParameterDefinitionImpl() {
    }
    
    public ParameterDefinitionImpl(String name, DataType type) {
        setName(name);
        setType(type);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot businessObject null");
        }
        this.name = name;
    }
    
    public DataType getType() {
        return type;
    }
    
    public void setType(DataType type) {
        if (type == null) {
            throw new IllegalArgumentException("Data type cannot businessObject null");
        }
        this.type = type;
    }
    
    public String toString() {
        return name;
    }
}
