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

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateFeature;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.dd.di.DiFactory;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;

public class CreateParticipantFeature extends AbstractBpmn2CreateFeature {
	
	private static int index = 1;
	
	public CreateParticipantFeature(IFeatureProvider fp) {
	    super(fp, "Pool", "Container for partitioning a set of activities");
    }

	@Override
    public boolean canCreate(ICreateContext context) {
		return context.getTargetContainer() instanceof Diagram;
    }

	@Override
    public Object[] create(ICreateContext context) {
		Participant participant = null;
		
        BPMNDiagram bpmnDiagram = BusinessObjectUtil.getFirstElementOfType(context.getTargetContainer(), BPMNDiagram.class);
        Definitions definitions = ModelUtil.getDefinitions(bpmnDiagram);
        participant = (Participant) PropertyUtil.createObject(definitions.eResource(), Bpmn2Package.eINSTANCE.getParticipant());
        participant.setName("Pool nr " + index++);
        // add the Pool to the first Choreography or Collaboration we find.
        // TODO: when multipage editor is working, this will be the specific Choreography or
        // Collaboration that is being rendered on the current page.
        List<RootElement> rootElements = definitions.getRootElements();
        for (RootElement element : rootElements) {
            if (element instanceof Collaboration || element instanceof Choreography) {
                ((Collaboration)element).getParticipants().add(participant);
                break;
            }
        }

        // create a Process for this Participant
        Process process = (Process) PropertyUtil.createObject(bpmnDiagram.eResource(), Bpmn2Package.eINSTANCE.getProcess());
        // NOTE: this is needed because it fires the InsertionAdapter, which adds the new Process
        // to Definitions.rootElements, otherwise the Process would be a dangling object
        process.setName(participant.getName()+" Process");
        participant.setProcessRef(process);
        
        addGraphicalRepresentation(context, participant);
        
        bpmnDiagram = BpmnDiFactory.eINSTANCE.createBPMNDiagram();
        bpmnDiagram.setName(process.getName());
        definitions.getDiagrams().add(bpmnDiagram);
		ModelUtil.setID(bpmnDiagram);
        
		BPMNPlane plane = BpmnDiFactory.eINSTANCE.createBPMNPlane();
		bpmnDiagram.setPlane(plane);
		ModelUtil.setID(plane);

        definitions.getDiagrams().add(bpmnDiagram);
        
		return new Object[] { participant };
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
	public Class getBusinessObjectClass() {
		return Participant.class;
	}
}
