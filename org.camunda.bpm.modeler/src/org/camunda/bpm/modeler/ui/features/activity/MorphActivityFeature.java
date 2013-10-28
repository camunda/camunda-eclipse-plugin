package org.camunda.bpm.modeler.ui.features.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.camunda.bpm.modeler.ui.features.AbstractMorphNodeFeature;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.Connection;

public class MorphActivityFeature extends AbstractMorphNodeFeature<Activity> {

	public MorphActivityFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public String getName() {
		return "Morph Activity";
	}

	@Override
	public String getDescription() {
		return "Change the Activity type";
	}

	@Override
	public String getImageId() {
		return ImageProvider.IMG_16_CONFIGURE;
	}

	@Override
	public EClass getBusinessObjectClass() {
		return Bpmn2Package.eINSTANCE.getActivity();
	}
	
	@Override
	public List<EClass> excludeTypes(EObject bo) {
		List<EClass> exclusions = new ArrayList<EClass>();
		
		exclusions.add(Bpmn2Package.eINSTANCE.getSubProcess());
		exclusions.add(Bpmn2Package.eINSTANCE.getAdHocSubProcess());
		exclusions.add(Bpmn2Package.eINSTANCE.getTransaction());
		
		return exclusions;
	}
	
	@Override
	protected void handleMessageFlow(EObject bo, Connection messageFlow, List<Connection> connections, boolean incoming) {
		if (bo.eClass().equals(ModelPackage.eINSTANCE.getCallActivity())) {
			deletePictogramElement(messageFlow);
			return;
		}
		
		super.handleMessageFlow(bo, messageFlow, connections, incoming);
	}
	
	@Override
	protected List<EClass> getAvailableTypes(EObject bo) {
		List<EClass> availableTypes = super.getAvailableTypes(bo);
		
		if (bo.eClass().equals(ModelPackage.eINSTANCE.getCallActivity())) {
			
			Iterator<EClass> iterator = availableTypes.iterator();
			while(iterator.hasNext()) {
				EClass currentCls = iterator.next();
				if (currentCls.equals(Bpmn2Package.eINSTANCE.getCallActivity())) {
					iterator.remove();
					break;
				}
			}
		}
		
		return availableTypes;
	}
}
