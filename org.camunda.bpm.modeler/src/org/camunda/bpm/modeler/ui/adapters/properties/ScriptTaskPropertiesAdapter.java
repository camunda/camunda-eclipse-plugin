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

package org.camunda.bpm.modeler.ui.adapters.properties;

import org.camunda.bpm.modeler.core.adapters.FeatureDescriptor;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 *
 */
public class ScriptTaskPropertiesAdapter extends TaskPropertiesAdapter<ScriptTask> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public ScriptTaskPropertiesAdapter(AdapterFactory adapterFactory, ScriptTask object) {
		super(adapterFactory, object);


    	EStructuralFeature ref = Bpmn2Package.eINSTANCE.getScriptTask_Script();
    	setFeatureDescriptor(ref,
			new FeatureDescriptor<ScriptTask>(adapterFactory,object,ref) {
    		
	    		@Override
	    		public String getDisplayName(Object context) {
	    			ScriptTask task = adopt(context);
					if (task.getScript()==null)
						return "";
					return task.getScript();
	    		}
    	});
    	
    	ref = Bpmn2Package.eINSTANCE.getScriptTask_ScriptFormat();
    	setFeatureDescriptor(ref,
			new FeatureDescriptor<ScriptTask>(adapterFactory,object,ref) {
    		
	    		@Override
	    		public String getDisplayName(Object context) {
	    			ScriptTask task = adopt(context);
					if (task.getScriptFormat()==null)
						return "text/xml"; // TODO: is there a default mime-type?
					return task.getScriptFormat();
	    		}
    	});
	}

}
