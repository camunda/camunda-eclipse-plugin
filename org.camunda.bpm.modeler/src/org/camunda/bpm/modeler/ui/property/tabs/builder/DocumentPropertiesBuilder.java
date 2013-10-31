package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Builder for document properties from the definitions
 * 
 * @author kristin.polenz
 */
public class DocumentPropertiesBuilder extends AbstractPropertiesBuilder<Definitions> {

	private static final EStructuralFeature TARGET_NAMESPACE_FEATURE = Bpmn2Package.eINSTANCE.getDefinitions_TargetNamespace();
	
	public DocumentPropertiesBuilder(Composite parent, GFPropertySection section, Definitions bo) {
		super(parent, section, bo);
	}
	
	@Override
	public void create() {
		PropertyUtil.createText(section, parent, "Target Namespace", TARGET_NAMESPACE_FEATURE, bo);
	}
}
