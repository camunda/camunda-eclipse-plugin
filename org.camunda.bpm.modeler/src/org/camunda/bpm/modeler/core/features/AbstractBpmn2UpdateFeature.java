package org.camunda.bpm.modeler.core.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.mm.PropertyContainer;
import org.eclipse.graphiti.services.Graphiti;

/**
 * A {@link IUpdateFeature} that provides basic utility functions to all
 * Bpmn2 update features.
 * 
 * @author nico.rehwaldt
 */
public abstract class AbstractBpmn2UpdateFeature extends AbstractUpdateFeature {

	public AbstractBpmn2UpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	protected void setProperty(PropertyContainer container, String name, Object value) {
		Graphiti.getPeService().setPropertyValue(container, name, value.toString());
	}

	protected String getProperty(PropertyContainer container, String name) {
		return Graphiti.getPeService().getPropertyValue(container, name);
	}
	
	protected Boolean getBooleanProperty(PropertyContainer container, String name) {
		String value = getProperty(container, name);
		
		return value == null ? null : new Boolean(value);
	}
}
