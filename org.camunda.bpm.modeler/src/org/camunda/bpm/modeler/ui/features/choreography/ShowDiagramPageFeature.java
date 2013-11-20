package org.camunda.bpm.modeler.ui.features.choreography;

import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.ui.Images;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

//NOT USED YET
public class ShowDiagramPageFeature extends AbstractCustomFeature {

	public ShowDiagramPageFeature(IFeatureProvider fp) {
		super(fp);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "Show Diagram";
	}

	@Override
	public String getDescription() {
		return "Switch to the Diagram Page that contains the details of this activity";
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			PictogramElement pe = pes[0];
			Object bo = getBusinessObjectForPictogramElement(pe);
			BaseElement baseElement = null;
			if (bo instanceof Participant) {
				Participant participant = (Participant)bo;
				baseElement = participant.getProcessRef();
			}
			else if (bo instanceof BaseElement) {
				baseElement = (BaseElement)bo;
			}
			
			return DIUtils.findBPMNDiagram(getDiagramBehavior().getDiagramContainer(), baseElement, false) != null;
		}
		return false;
	}

	@Override
	public boolean isAvailable(IContext context) {
		return true;
	}

	@Override
	public void execute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			PictogramElement pe = pes[0];
			Object bo = getBusinessObjectForPictogramElement(pe);
			BaseElement baseElement = null;
			if (bo instanceof Participant) {
				Participant participant = (Participant)bo;
				baseElement = participant.getProcessRef();
			}
			else if (bo instanceof BaseElement) {
				baseElement = (BaseElement)bo;
			}
			DIUtils.findBPMNDiagram(getDiagramBehavior().getDiagramContainer(), baseElement, false);
		}
	}

	@Override
	public String getImageId() {
		return Images.IMG_16_EXPAND;
	}

	@Override
	public boolean hasDoneChanges() {
		return false;
	}

}
