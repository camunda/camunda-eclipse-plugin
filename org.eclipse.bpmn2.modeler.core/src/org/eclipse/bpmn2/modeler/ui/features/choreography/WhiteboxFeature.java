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

package org.eclipse.bpmn2.modeler.ui.features.choreography;

import java.util.List;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.CallableElement;
import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * @author Bob Brodt
 *
 */
public class WhiteboxFeature extends AbstractCustomFeature {

	/**
	 * @param fp
	 */
	public WhiteboxFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public String getName() {
	    return "Whitebox";
	}
	
	@Override
	public String getDescription() {
	    return "Create a new Diagram for this Participant or CallActivity";
	}

	@Override
	public String getImageId() {
		return ImageProvider.IMG_16_WHITEBOX;
	}

	@Override
	public boolean isAvailable(IContext context) {
		return true;
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			PictogramElement pe = pes[0];
			Process process = null;
			Object bo = getBusinessObjectForPictogramElement(pe);
			if (bo instanceof Participant) {
				process = ((Participant)bo).getProcessRef();
			}
			else if (bo instanceof CallActivity) {
				CallableElement ce = ((CallActivity)bo).getCalledElementRef();
				if (ce instanceof Process)
					process = (Process)ce;
			}
			
			if (process!=null) {
				try {
					ModelHandler mh = ModelHandlerLocator.getModelHandler(process.eResource());
					DiagramElement de = mh.findDIElement(process);
					return de==null;
				}
				catch (Exception e){
				}
			}
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.features.custom.ICustomFeature#execute(org.eclipse.graphiti.features.context.ICustomContext)
	 */
	@Override
	public void execute(ICustomContext context) {
		PictogramElement pe = context.getPictogramElements()[0];
		Object bo = getBusinessObjectForPictogramElement(pe);
		if (bo instanceof Participant) {
			Participant participant = (Participant)bo;
			Definitions definitions = ModelUtil.getDefinitions(participant);
			Resource resource = definitions.eResource();
			Process process = participant.getProcessRef();

			if (process==null) {
		        // create a Process for this Participant
		        process = (Process) ModelUtil.createObject(resource, Bpmn2Package.eINSTANCE.getProcess());
		        participant.setProcessRef(process);
		        
		        // NOTE: this is needed because it fires the InsertionAdapter, which adds the new Process
		        // to Definitions.rootElements, otherwise the Process would be a dangling object
		        process.setName("Process for "+participant.getName());
			}
			
	        // add the Participant to the first Choreography or Collaboration we find.
	        // TODO: when (and if) multipage editor allows additional Choreography or
	        // Collaboration diagrams to be created, this will be the specific diagram
	        // that is being rendered on the current page.
	        List<RootElement> rootElements = definitions.getRootElements();
	        for (RootElement element : rootElements) {
	            if (element instanceof Collaboration || element instanceof Choreography) {
	            	((Collaboration)element).getParticipants().add(participant);
	                break;
	            }
	        }
			DIUtils.createBPMNDiagram(definitions, process);
		}
	}
}
