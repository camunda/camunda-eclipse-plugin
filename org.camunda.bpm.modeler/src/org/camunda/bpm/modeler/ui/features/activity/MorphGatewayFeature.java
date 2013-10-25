package org.camunda.bpm.modeler.ui.features.activity;

import org.camunda.bpm.modeler.ui.ImageProvider;
import org.camunda.bpm.modeler.ui.features.AbstractMorphNodeFeature;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;

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
	public String getImageId() {
		return ImageProvider.IMG_16_CONFIGURE;
	}
	
	@Override
	public EClass getBusinessObjectClass() {
		return Bpmn2Package.eINSTANCE.getGateway();
	}

}
