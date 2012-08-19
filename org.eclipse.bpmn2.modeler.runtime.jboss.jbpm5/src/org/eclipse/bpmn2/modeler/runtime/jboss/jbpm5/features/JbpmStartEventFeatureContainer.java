package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.features;

import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.modeler.ui.features.event.StartEventFeatureContainer;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;

public class JbpmStartEventFeatureContainer extends StartEventFeatureContainer {

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new JbpmCreateStartEventFeature(fp);
	}

	public class JbpmCreateStartEventFeature extends CreateStartEventFeature {

		public JbpmCreateStartEventFeature(IFeatureProvider fp) {
			super(fp);
		}
		
		@Override
		public StartEvent createBusinessObject(ICreateContext context) {
			StartEvent event = super.createBusinessObject(context);
			event.setName("");
			return event;
		}
	}
}
