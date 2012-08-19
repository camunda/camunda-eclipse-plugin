package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.features;

import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.modeler.ui.features.event.EndEventFeatureContainer;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;

public class JbpmEndEventFeatureContainer extends EndEventFeatureContainer {

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new XJbpmCreateEndEventFeature(fp);
	}

	public class XJbpmCreateEndEventFeature extends CreateEndEventFeature {

		public XJbpmCreateEndEventFeature(IFeatureProvider fp) {
			super(fp);
		}
		
		@Override
		public EndEvent createBusinessObject(ICreateContext context) {
			EndEvent event = super.createBusinessObject(context);
			event.setName("");
			return event;
		}
	}
}
