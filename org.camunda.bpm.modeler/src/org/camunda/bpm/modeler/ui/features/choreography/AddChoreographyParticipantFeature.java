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

package org.camunda.bpm.modeler.ui.features.choreography;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.ui.Images;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.ChoreographyTask;
import org.eclipse.bpmn2.Participant;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.internal.util.ui.PopupMenu;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * @author Bob Brodt
 *
 */
public class AddChoreographyParticipantFeature extends AbstractCustomFeature {
	
	private boolean changesDone = false;
	
	private static ILabelProvider labelProvider = new ILabelProvider() {

		public void removeListener(ILabelProviderListener listener) {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void dispose() {

		}

		public void addListener(ILabelProviderListener listener) {

		}

		public String getText(Object element) {
			return ((Participant)element).getName();
		}

		public Image getImage(Object element) {
			return null;
		}

	};

	/**
	 * @param fp
	 */
	public AddChoreographyParticipantFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public String getName() {
	    return "Add Participant";
	}
	
	@Override
	public String getDescription() {
	    return "Add a new Participant to this Choreography Task";
	}

	@Override
	public String getImageId() {
		return Images.IMG_16_ADD_PARTICIPANT;
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
			Object bo = getBusinessObjectForPictogramElement(pe);
			if (bo instanceof ChoreographyTask) {
				return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.features.custom.ICustomFeature#execute(org.eclipse.graphiti.features.context.ICustomContext)
	 */
	@Override
	public void execute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			PictogramElement pe = pes[0];
			Object bo = getBusinessObjectForPictogramElement(pe);
			if (pe instanceof ContainerShape && bo instanceof ChoreographyTask) {
				ContainerShape containerShape = (ContainerShape)pe;
				ChoreographyTask task = (ChoreographyTask)bo;
				
				Participant participant = null;
				List<Participant> participantList = new ArrayList<Participant>();
				participant = Bpmn2Factory.eINSTANCE.createParticipant();
				participant.setName("New Participant");
				ModelUtil.setID(participant, task.eResource());
				
				participantList.add(participant);
				TreeIterator<EObject> iter = ModelUtil.getDefinitions(task).eAllContents();
				while (iter.hasNext()) {
					EObject obj = iter.next();
					if (obj instanceof Participant && !task.getParticipantRefs().contains(obj))
						participantList.add((Participant)obj);
				}
				
				Participant result = participant;

				changesDone = true;
				if (participantList.size()>1) {
					PopupMenu popupMenu = new PopupMenu(participantList, labelProvider);
					changesDone = popupMenu.show(Display.getCurrent().getActiveShell());
					if (changesDone) {
						result = (Participant) popupMenu.getResult();
					}
				}
				if (changesDone) {
					if (result==participant) { // the new one
						participant.setName( ModelUtil.toDisplayName(participant.getId()) );
						Choreography choreography = (Choreography)task.eContainer();
						choreography.getParticipants().add(result);
						/*
						 Finish this later after we figure out how to deal with multiple BPMNDiagrams and BPMNPlanes
						 
						Process process = (Process) PropertyUtil.createObject(task.eResource(), Bpmn2Package.eINSTANCE.getProcess());
						// NOTE: this is needed because it fires the InsertionAdapter, which adds the new Process
						// to Definitions.rootElements, otherwise the Process would be a dangling object
						process.setName(participant.getName()+" Process");
						participant.setProcessRef(process);
						*/
					}

					if (task.getInitiatingParticipantRef() == null) {
						task.setInitiatingParticipantRef(result);
					}

					task.getParticipantRefs().add(result);
				}
			}
		}
	}

	@Override
	public boolean hasDoneChanges() {
		return changesDone;
	}
}
