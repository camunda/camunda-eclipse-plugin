package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Builder for the id property
 * 
 * @author nico.rehwaldt
 */
public class IdPropertyBuilder extends AbstractPropertiesBuilder<BaseElement> {

	private String label;

	public IdPropertyBuilder(Composite parent, GFPropertySection section, BaseElement bo, String label) {
		super(parent, section, bo);
		
		this.label = label;
	}
	
	public IdPropertyBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		this(parent, section, bo, "Id");
	}

	@Override
	public void create() {
		Text idText = PropertyUtil.createText(section, parent, label, Bpmn2Package.eINSTANCE.getBaseElement_Id(), bo);
		
		if (bo instanceof Process) {
			// we keep id enabled for process
		} else {
			// FIXME: Id change is not properly propagated
			idText.setEnabled(false);
		}
	}
}
