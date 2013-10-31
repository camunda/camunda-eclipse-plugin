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
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

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
	public List<MorphOption> getOptions(EObject bo) {
		List<MorphOption> options = super.getOptions(bo);
		
		if (bo.eClass().equals(ModelPackage.eINSTANCE.getCallActivity())) {
			
			Iterator<MorphOption> iterator = options.iterator();
			while(iterator.hasNext()) {
				MorphOption currentOption = iterator.next();
				if (currentOption.getNewType().equals(Bpmn2Package.eINSTANCE.getCallActivity())) {
					iterator.remove();
					break;
				}
			}
		}
		
		return options;
	}
	
	@Override
	protected ILabelProvider getLabelProvider() {
		if (labelProvider == null) {
			labelProvider = new ActivityLabelProvider();
		}
		return labelProvider;
	}
	
	protected class ActivityLabelProvider extends LabelProvider {
		
		@Override
		public Image getImage(Object element) {
			if (!(element instanceof MorphOption)) {
				return null;
			}
			MorphOption option = (MorphOption) element;
			EClass cls = option.getNewType();
			
			if (cls.equals(Bpmn2Package.eINSTANCE.getTask())) {
				return getImageForId(ImageProvider.IMG_16_TASK);
			}
			
			if (cls.equals(Bpmn2Package.eINSTANCE.getManualTask())) {
				return getImageForId(ImageProvider.IMG_16_MANUAL_TASK);
			}
			
			if (cls.equals(Bpmn2Package.eINSTANCE.getUserTask())) {
				return getImageForId(ImageProvider.IMG_16_USER_TASK);
			}
			
			if (cls.equals(Bpmn2Package.eINSTANCE.getScriptTask())) {
				return getImageForId(ImageProvider.IMG_16_SCRIPT_TASK);
			}
			
			if (cls.equals(Bpmn2Package.eINSTANCE.getBusinessRuleTask())) {
				return getImageForId(ImageProvider.IMG_16_BUSINESS_RULE_TASK);
			}
			
			if (cls.equals(Bpmn2Package.eINSTANCE.getServiceTask())) {
				return getImageForId(ImageProvider.IMG_16_SERVICE_TASK);
			}
			
			if (cls.equals(Bpmn2Package.eINSTANCE.getSendTask())) {
				return getImageForId(ImageProvider.IMG_16_SEND_TASK);
			}
			
			if (cls.equals(Bpmn2Package.eINSTANCE.getReceiveTask())) {
				return getImageForId(ImageProvider.IMG_16_RECEIVE_TASK);
			}
			
			if (cls.equals(Bpmn2Package.eINSTANCE.getCallActivity())) {
				return getImageForId(ImageProvider.IMG_16_CALL_ACTIVITY);
			}
			
			return null;
		}		
	}
}
