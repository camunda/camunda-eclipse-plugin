package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

public class TextAnnotationPropertiesBuilder extends AbstractPropertiesBuilder<TextAnnotation> {

	private static final EStructuralFeature TEXT_FEATURE = Bpmn2Package.eINSTANCE.getTextAnnotation_Text();
	
	public TextAnnotationPropertiesBuilder(Composite parent, GFPropertySection section, TextAnnotation bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		PropertyUtil.createMultiText(section, parent, "Text", TEXT_FEATURE, bo);
	}
}
