package org.eclipse.bpmn2.modeler.ui.property.tabs.factories;

import org.eclipse.bpmn2.modeler.ui.property.tabs.PropertyUtil;
import org.eclipse.dd.di.DiPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

public class DiagramPropertiesFactory extends PropertiesFactory {

	public DiagramPropertiesFactory(Composite parent, GFPropertySection section, EObject bo) {
		super(parent, section, bo);
	}

	@Override
	protected void createControls() {
		PropertyUtil.createText(section, parent, "Id", DiPackage.eINSTANCE.getDiagram_Id(), bo);
		PropertyUtil.createText(section, parent, "Name", DiPackage.eINSTANCE.getDiagram_Name(), bo);
	}

}
