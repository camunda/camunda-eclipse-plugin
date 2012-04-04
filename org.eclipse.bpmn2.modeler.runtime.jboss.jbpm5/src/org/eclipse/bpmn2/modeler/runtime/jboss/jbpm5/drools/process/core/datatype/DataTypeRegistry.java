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

package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype;

import java.util.Hashtable;

import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.impl.NewInstanceDataTypeFactory;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.impl.type.BooleanDataType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.impl.type.EnumDataType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.impl.type.FloatDataType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.impl.type.IntegerDataType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.impl.type.StringDataType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.impl.type.UndefinedDataType;

/**
 * @author Bob Brodt
 *
 */
public class DataTypeRegistry {

	public static Hashtable<String,DataTypeFactory> instance = null;

	public static DataTypeFactory getFactory(String type) {
		assert(type!=null && !type.isEmpty());
		
		if (instance==null) {
			instance = new Hashtable<String,DataTypeFactory>();
			instance.put("BooleanDataType", new NewInstanceDataTypeFactory(BooleanDataType.class));
			instance.put("EnumDataType", new NewInstanceDataTypeFactory(EnumDataType.class));
			instance.put("FloatDataType", new NewInstanceDataTypeFactory(FloatDataType.class));
			instance.put("IntegerDataType", new NewInstanceDataTypeFactory(IntegerDataType.class));
			instance.put("StringDataType", new NewInstanceDataTypeFactory(StringDataType.class));
			instance.put("UndefinedDataType", new NewInstanceDataTypeFactory(UndefinedDataType.class));
		}
		DataTypeFactory factory = instance.get(type);
		if (factory==null)
			factory = instance.get("UndefinedDataType");
		return factory;
	}
}
