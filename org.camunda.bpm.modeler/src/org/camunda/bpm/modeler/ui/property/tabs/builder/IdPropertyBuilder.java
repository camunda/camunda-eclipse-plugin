package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.ui.property.tabs.binding.ValidatingStringTextBinding;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Builder for the id property
 * 
 * @author nico.rehwaldt
 */
public class IdPropertyBuilder extends AbstractPropertiesBuilder<BaseElement> {

	protected static final EAttribute BASE_ELEMENT_ID_FEATURE = Bpmn2Package.eINSTANCE.getBaseElement_Id();
	
	protected String label;

	private ValidatingStringTextBinding binding;

	public IdPropertyBuilder(Composite parent, GFPropertySection section, BaseElement bo, String label) {
		super(parent, section, bo);
		
		this.label = label;
	}
	
	public IdPropertyBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		this(parent, section, bo, "Id");
	}

	@Override
	public void create() {
		final Text idText = PropertyUtil.createUnboundText(section, parent, label);
		
		binding = new ValidatingStringTextBinding(bo, BASE_ELEMENT_ID_FEATURE, idText);
		// validate unique id
		binding.addErrorCode(100);
		binding.addErrorCode(101);

		binding.setMandatory(true);
		
		binding.establish();
	}

	
}
