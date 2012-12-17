package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import java.util.List;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.StringTextBinding;
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
public class ProcessIdPropertyBuilder extends IdPropertyBuilder {

	public ProcessIdPropertyBuilder(Composite parent, GFPropertySection section, Process bo, String label) {
		super(parent, section, bo, label);
	}
	
	public ProcessIdPropertyBuilder(Composite parent, GFPropertySection section, Process bo) {
		super(parent, section, bo, "Id");
	}

	@Override
	public void create() {
		Text idText = PropertyUtil.createUnboundText(section, parent, label);
		
		new StringTextBinding(bo, BASE_ELEMENT_ID_FEATURE, idText) {
			@Override
			public void setModelValue(String value) {
				if (isProcessIdValid(value)) {
					super.setModelValue(value);
				} else {
					setViewValue(getModelValue());
				}
			}
		}.establish();
	}
	
	private boolean isProcessIdValid(String processId) {
		
		Process process = (Process) bo;
		
		EObject eContainer = process.eContainer();
		if (eContainer instanceof Definitions) {

			List<Process> processes = ModelUtil.getAllRootElements((Definitions) eContainer, Process.class);
			
			boolean idOccupied = false;
			
			for (Process p: processes) {
				if (processId.equals(p.getId()) && !process.equals(p)) {
					idOccupied = true;
				}
			}
			
			return !idOccupied;
		}
		
		return true;
	}
}
