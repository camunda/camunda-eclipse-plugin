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
package org.camunda.bpm.modeler.ui.features.participant;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.features.DefaultBpmn2DeleteShapeFeature;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.ui.features.choreography.ChoreographyUtil;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class DeleteParticipantFeature extends DefaultBpmn2DeleteShapeFeature {

	public DeleteParticipantFeature(IFeatureProvider fp) {
		super(fp);
	}

	/**
	 * FIXME this method is too big, slim fast please
	 */
	@Override
	public void delete(IDeleteContext context) {
		Participant participant = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), Participant.class);
		Definitions definitions = ModelUtil.getDefinitions(participant);
		
		Collaboration collaboration = (Collaboration) participant.eContainer();
		Process process = participant.getProcessRef();
		
		super.deletePeEnvironment(context.getPictogramElement());
		super.delete(context);
		
		List<BPMNDiagram> processDiagrams = new ArrayList<BPMNDiagram>(); 
		List<BPMNDiagram> collaborationDiagrams = new ArrayList<BPMNDiagram>(); 
		
		for (BPMNDiagram bpmnDiagram : definitions.getDiagrams()) {
			BPMNPlane plane = bpmnDiagram.getPlane();
			
			if (plane.getBpmnElement() == process) {
				processDiagrams.add(bpmnDiagram);
			}
			
			if (plane.getBpmnElement() == collaboration) {
				collaborationDiagrams.add(bpmnDiagram);
			}
		}

		deleteBusinessObject(process);
		
		if (!processDiagrams.isEmpty()) {
			for (BPMNDiagram processDiagram : processDiagrams) {
				deleteBusinessObject(processDiagram);
			}
		}else if (collaboration.getParticipants().size() == 0) { // particpant is already deleted by super.delete
			definitions.getRootElements().remove(collaboration);
			
			List<Collaboration> allCollaborations = ModelUtil
					.getAllRootElements(definitions, Collaboration.class);

			if (allCollaborations.isEmpty()) {
				// we need to add a new process to be able to add new elements
				// to it (see ModelHandler.getFlowElementsContainer also)
				
				Process newProcess = Bpmn2Factory.eINSTANCE.createProcess();
				ModelUtil.setID(newProcess);
				definitions.getRootElements().add(newProcess);
				
				if (collaborationDiagrams.size() == 1) {
					for (BPMNDiagram collaborationDiagram : collaborationDiagrams) {
						deleteBusinessObject(collaborationDiagram);
					}
					BPMNDiagram newDiagram = BpmnDiFactory.eINSTANCE.createBPMNDiagram();
					ModelUtil.setID(newDiagram);
					definitions.getDiagrams().add(newDiagram);
					
					BPMNPlane newPlane = BpmnDiFactory.eINSTANCE.createBPMNPlane();
					ModelUtil.setID(newPlane);
					newPlane.setBpmnElement(newProcess);
					newDiagram.setPlane(newPlane);
					
					link(getDiagram(), new Object[] { newProcess, newDiagram, definitions }); // we need to relink, the old diagram is gone
				} else {
					throw new IllegalStateException(
							"Unable to handle multiple collaborations diagrams.s");
				}

			}
		}
		
	}

	@Override
	public boolean canDelete(IDeleteContext context) {
		// Participant bands in a ChoreographyTask only be "deleted" (from the model)
		// if there are no other references to the participant; but they can be "removed"
		// (from the ChoreographyTask's participantRef list) at any time.
		// @see RemoveChoreographyParticipantFeature
		PictogramElement pe = context.getPictogramElement();
		if (ChoreographyUtil.isChoreographyParticipantBand(pe)) {
			int referenceCount = 0;
			Participant participant = BusinessObjectUtil.getFirstElementOfType(pe, Participant.class);
			Definitions definitions = ModelUtil.getDefinitions(participant);
			TreeIterator<EObject> iter = definitions.eAllContents();
			while (iter.hasNext()) {
				EObject o = iter.next();
				for (EReference reference : o.eClass().getEAllReferences()) {
					if (!reference.isContainment() && !(o instanceof DiagramElement)) {
						if (reference.isMany()) {
							List list = (List)o.eGet(reference);
							for (Object referencedObject : list) {
								if (referencedObject==participant)
									++referenceCount;
							}
						}
						else {
							Object referencedObject = o.eGet(reference);
							if (referencedObject == participant)
								++referenceCount;
						}
					}
				}
			}
			return referenceCount <= 1;
		}
		return true;
	}

}