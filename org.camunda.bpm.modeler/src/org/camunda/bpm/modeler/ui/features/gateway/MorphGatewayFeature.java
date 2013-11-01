package org.camunda.bpm.modeler.ui.features.gateway;

import org.camunda.bpm.modeler.ui.ImageProvider;
import org.camunda.bpm.modeler.ui.features.AbstractMorphNodeFeature;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

public class MorphGatewayFeature extends AbstractMorphNodeFeature<Gateway> {

	public MorphGatewayFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public String getName() {
		return "Morph Gateway";
	}

	@Override
	public String getDescription() {
		return "Change the Gateway type";
	}

	@Override
	public EClass getBusinessObjectClass() {
		return Bpmn2Package.eINSTANCE.getGateway();
	}
	
	@Override
	public ILabelProvider getLabelProvider() {
		if (labelProvider == null) {
			labelProvider = new GatewayLabelProvider();
		}
		return labelProvider;
	}
	
	protected class GatewayLabelProvider extends LabelProvider {
		
		@Override
		public Image getImage(Object element) {
			if (!(element instanceof MorphOption)) {
				return null;
			}
			
			MorphOption option = (MorphOption) element;
			EClass cls = option.getNewType();
			
			if (cls.equals(Bpmn2Package.eINSTANCE.getInclusiveGateway())) {
				return getImageForId(ImageProvider.IMG_16_INCLUSIVE_GATEWAY);
			}
			
			if (cls.equals(Bpmn2Package.eINSTANCE.getExclusiveGateway())) {
				return getImageForId(ImageProvider.IMG_16_EXCLUSIVE_GATEWAY);
			}
			
			if (cls.equals(Bpmn2Package.eINSTANCE.getParallelGateway())) {
				return getImageForId(ImageProvider.IMG_16_PARALLEL_GATEWAY);
			}
			
			if (cls.equals(Bpmn2Package.eINSTANCE.getEventBasedGateway())) {
				return getImageForId(ImageProvider.IMG_16_EVENT_BASED_GATEWAY);
			}
			
			if (cls.equals(Bpmn2Package.eINSTANCE.getComplexGateway())) {
				return getImageForId(ImageProvider.IMG_16_COMPLEX_GATEWAY);
			}
			
			return null;
		}
	}
}
