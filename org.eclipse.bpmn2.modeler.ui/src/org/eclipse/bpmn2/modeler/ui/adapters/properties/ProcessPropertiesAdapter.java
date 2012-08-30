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

package org.eclipse.bpmn2.modeler.ui.adapters.properties;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.adapters.AdapterUtil;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.FeatureDescriptor;
import org.eclipse.bpmn2.modeler.core.adapters.ObjectDescriptor;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * @author Bob Brodt
 *
 */
public class ProcessPropertiesAdapter extends RootElementPropertiesAdapter<Process> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public ProcessPropertiesAdapter(AdapterFactory adapterFactory, Process object) {
		super(adapterFactory, object);
    	final EStructuralFeature ref = Bpmn2Package.eINSTANCE.getCallableElement_Name();
    	setFeatureDescriptor(ref,
			new FeatureDescriptor<Process>(adapterFactory,object,ref) {
	    		@Override
	    		public void setValue(Object context, final Object value) {
	    			// changing the process name also changes its BPMNDiagram name
	    			// which is used as the tab label in the multipage editor
	    			final Process process = adopt(context);
	    			BPMNDiagram bpmnDiagram = null;
	    			Definitions defs = ModelUtil.getDefinitions(process);
	    			if (defs!=null) {
	    				for (BPMNDiagram d : defs.getDiagrams()) {
	    					if (d.getPlane().getBpmnElement() == process) {
	    						bpmnDiagram = d;
	    						break;
	    					}
	    				}
	    			}

	    			TransactionalEditingDomain editingDomain = getEditingDomain(process);
	    			if (editingDomain == null) {
		    			process.setName((String)value);
	    				if (bpmnDiagram!=null)
	    					bpmnDiagram.setName((String)value);
	    			} else {
	    				final BPMNDiagram d = bpmnDiagram;
	    				editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
	    					@Override
	    					protected void doExecute() {
	    		    			process.setName((String)value);
	    	    				if (d!=null)
	    	    					d.setName((String)value);
	    					}
	    				});
	    			}
	    		}
    		}
    	);
    	
		setObjectDescriptor(new ObjectDescriptor<Process>(adapterFactory, object) {
			@Override
			public Process createObject(Resource resource, Object context) {
				ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(
						Bpmn2Package.eINSTANCE.getRootElement(),
						ExtendedPropertiesAdapter.class);
				Process process = (Process) adapter.getObjectDescriptor().createObject(resource, object);
				return process;
			}
		});
	}

}
