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

import java.util.List;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.ParticipantMultiplicity;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.bpmn2.di.BpmnDiPackage;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.FeatureDescriptor;
import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.ObjectDescriptor;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author Bob Brodt
 *
 */
public class ParticipantPropertiesAdapter extends ExtendedPropertiesAdapter<Participant> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public ParticipantPropertiesAdapter(AdapterFactory adapterFactory, Participant object) {
		super(adapterFactory, object);
		

		EStructuralFeature ref = Bpmn2Package.eINSTANCE.getParticipant_ProcessRef();
    	setProperty(ref, UI_CAN_CREATE_NEW, Boolean.FALSE);

    	setFeatureDescriptor(ref, new RootElementRefFeatureDescriptor<Participant>(adapterFactory,object,ref));

		setObjectDescriptor(new ObjectDescriptor<Participant>(adapterFactory, object) {
			
			@Override
			public Participant createObject(Resource resource, Object context) {
				Participant participant = super.createObject(resource, context);
				participant.eSetDeliver(false);
				
				Definitions definitions = null;
				if (resource!=null)
					definitions = (Definitions) resource.getContents().get(0).eContents().get(0);
				else {
					definitions = ModelUtil.getDefinitions(participant);
					resource = definitions.eResource();
				}

		        // create a Process for this Participant
		        Process process = (Process) ModelUtil.createObject(resource, Bpmn2Package.eINSTANCE.getProcess());
		        participant.setProcessRef(process);
		        
		        // NOTE: this is needed because it fires the InsertionAdapter, which adds the new Process
		        // to Definitions.rootElements, otherwise the Process would be a dangling object
		        process.eSetDeliver(false);
		        process.setName(participant.getName()+" Process");

		        // add the Participant to the first Choreography or Collaboration we find.
		        // TODO: when multipage editor is working, this will be the specific Choreography or
		        // Collaboration that is being rendered on the current page.
		        List<RootElement> rootElements = definitions.getRootElements();
		        for (RootElement element : rootElements) {
		            if (element instanceof Collaboration || element instanceof Choreography) {
						InsertionAdapter.add(
								(Collaboration)element,
								Bpmn2Package.eINSTANCE.getCollaboration_Participants(),
								participant);
		                break;
		            }
		        }
				
		        BPMNDiagram bpmnDiagram = BpmnDiFactory.eINSTANCE.createBPMNDiagram();
				ModelUtil.setID(bpmnDiagram, resource);
		        bpmnDiagram.setName(process.getName());
		        
				BPMNPlane plane = BpmnDiFactory.eINSTANCE.createBPMNPlane();
				ModelUtil.setID(plane, resource);
				InsertionAdapter.add(bpmnDiagram, BpmnDiPackage.eINSTANCE.getBPMNDiagram_Plane(), plane);
				InsertionAdapter.add(plane, BpmnDiPackage.eINSTANCE.getBPMNPlane_BpmnElement(), process);
				InsertionAdapter.add(definitions, Bpmn2Package.eINSTANCE.getDefinitions_Diagrams(), bpmnDiagram);
				InsertionAdapter.add(bpmnDiagram, Bpmn2Package.eINSTANCE.getDefinitions_Diagrams(), definitions);
		        
		        process.eSetDeliver(true);
				participant.eSetDeliver(true);

				return participant;
			}
			
		});
		
		ref = Bpmn2Package.eINSTANCE.getParticipant_ParticipantMultiplicity();
		setProperty(ref, UI_CAN_EDIT_INLINE, Boolean.FALSE);
		setProperty(ref, UI_CAN_CREATE_NEW, Boolean.TRUE);
		setProperty(ref, UI_CAN_EDIT, Boolean.TRUE);
		setProperty(ref, UI_IS_MULTI_CHOICE, Boolean.FALSE);
    	setFeatureDescriptor(ref, new FeatureDescriptor<Participant>(adapterFactory,object,ref) {

			@Override
			public String getLabel(Object context) {
				return "Multiplicity";
			}

			@Override
			public String getDisplayName(Object context) {
				 Participant object = adopt(context);
				 ParticipantMultiplicity pm = object.getParticipantMultiplicity();
				 if (pm!=null) {
					 return pm.getMinimum() + ".." + pm.getMaximum();
				 }
				 return "";
			}

    	});

	}

}
