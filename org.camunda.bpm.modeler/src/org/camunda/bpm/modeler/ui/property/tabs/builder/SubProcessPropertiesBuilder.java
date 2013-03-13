package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author nico.rehwaldt
 */
public class SubProcessPropertiesBuilder extends AbstractPropertiesBuilder<SubProcess> {

	/**
	 * Creates a new factory from the given arguments
	 * 
	 * @param parent
	 * @param section
	 * @param bo
	 */
	public SubProcessPropertiesBuilder(Composite parent, GFPropertySection section, SubProcess bo) {
		super(parent, section, bo);
	}

	/**
	 * Creates the service task specific controls
	 */
	@Override
	public void create() {
		PropertyUtil.createCheckbox(section, parent, "Triggered by Event", Bpmn2Package.eINSTANCE.getSubProcess_TriggeredByEvent(), bo);	
	}
}
