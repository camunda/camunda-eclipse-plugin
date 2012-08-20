package org.eclipse.bpmn2.modeler.ui.features.choreography;

import java.io.IOException;

import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.IConstants;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditingDialog;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2MultiPageEditor;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.editor.DiagramEditor;

public class ShowDiagramPageFeature extends AbstractCustomFeature {

	public ShowDiagramPageFeature(IFeatureProvider fp) {
		super(fp);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "Show Properties";
	}

	@Override
	public String getDescription() {
		return "Display a Property configuration popup dialog for the selected item";
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			PictogramElement pe = pes[0];
			Object bo = getBusinessObjectForPictogramElement(pe);
			if (bo instanceof Participant) {
				Participant participant = (Participant)bo;
				Process process = participant.getProcessRef();
				if (process!=null) {
					try {
						ModelHandler mh = ModelHandlerLocator.getModelHandler(participant.eResource());
						DiagramElement de = mh.findDIElement(process);
						return de!=null;
					}
					catch (Exception e){
					}
				}
				return true;
			}
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
			if (bo instanceof Participant) {
				Participant participant = (Participant)bo;
				Process process = participant.getProcessRef();
				if (process!=null) {
					// go to the diagram page for this process if one exists
					ModelHandler mh;
					try {
						mh = ModelHandlerLocator.getModelHandler(participant.eResource());
						DiagramElement de = mh.findDIElement(process);
						if (de.eContainer() instanceof BPMNDiagram) {
							BPMNDiagram bpmnDiagram = (BPMNDiagram)de.eContainer();
							BPMN2MultiPageEditor mpe = ((BPMN2Editor)getDiagramEditor()).getMultipageEditor();
							mpe.showDesignPage(bpmnDiagram);
							return;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public String getImageId() {
		return null; //IConstants.ICON_PROPERTIES_16;
	}

	@Override
	public boolean hasDoneChanges() {
		return false;
	}

}
