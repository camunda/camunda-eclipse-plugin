package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.features;

import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.modeler.ui.features.event.IntermediateCatchEventFeatureContainer;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;

public class JbpmIntermediateCatchEventFeatureContainer extends IntermediateCatchEventFeatureContainer {

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new JbpmCreateCatchEventFeature(fp);
	}

	public class JbpmCreateCatchEventFeature extends CreateIntermediateCatchEventFeature {

		public JbpmCreateCatchEventFeature(IFeatureProvider fp) {
			super(fp);
		}
		
		@Override
		public IntermediateCatchEvent createBusinessObject(ICreateContext context) {
			IntermediateCatchEvent event = super.createBusinessObject(context);
			event.setName("");
			return event;
		}
	}
}
