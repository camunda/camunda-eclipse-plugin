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
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.features.participant;

import java.util.List;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateFeature;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;

public class CreateParticipantFeature extends AbstractBpmn2CreateFeature<Participant> {
	
	public CreateParticipantFeature(IFeatureProvider fp) {
	    super(fp, "Pool", "Container for partitioning a set of activities");
    }

	@Override
    public boolean canCreate(ICreateContext context) {
		return context.getTargetContainer() instanceof Diagram;
    }

	@Override
    public Object[] create(ICreateContext context) {
		BPMNDiagram bpmnDiagram = BusinessObjectUtil.getFirstElementOfType(context.getTargetContainer(), BPMNDiagram.class);
		
		if (bpmnDiagram == null) {
			throw new IllegalStateException("Participant should be in the context of a Graphiti Diagram linked to a BPMNDiagram");
		}
		
		Definitions definitions = (Definitions) bpmnDiagram.eContainer();
		
		List<Collaboration> collaborations = ModelUtil.getAllRootElements( definitions, Collaboration.class);
		List<org.eclipse.bpmn2.Process> processes = ModelUtil.getAllRootElements( definitions, org.eclipse.bpmn2.Process.class);
		Participant newParticipant;
		
		if (collaborations.isEmpty()) {
			org.eclipse.bpmn2.Process correspondingProcess;
			
			if (processes.size() == 1) {
				correspondingProcess = processes.get(0);
			}
			else {
				correspondingProcess = Bpmn2Factory.eINSTANCE.createProcess();
				ModelUtil.setID(correspondingProcess);
				definitions.getRootElements().add(correspondingProcess);
			}
			
			Collaboration newCollaboration = Bpmn2Factory.eINSTANCE.createCollaboration();
			ModelUtil.setID(newCollaboration);
			bpmnDiagram.getPlane().setBpmnElement(newCollaboration);
			
			newParticipant = Bpmn2Factory.eINSTANCE.createParticipant();
			ModelUtil.setID(newParticipant);

			newParticipant.setProcessRef(correspondingProcess);
			
			newCollaboration.getParticipants().add(newParticipant);
			definitions.getRootElements().add(0, newCollaboration);
		}else {
			newParticipant = createBusinessObject(context);
		}

		newParticipant.setName("Pool ");
		addGraphicalRepresentation(context, newParticipant);
		return new Object[] { newParticipant };
    }
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_PARTICIPANT;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); // FIXME
	}

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateFeature#getBusinessObjectClass()
	 */
	@Override
	public EClass getBusinessObjectClass() {
		return Bpmn2Package.eINSTANCE.getParticipant();
	}
}
