package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Error;
import org.eclipse.bpmn2.ErrorEventDefinition;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Builder for the error code / name property.
 * 
 * @author nico.rehwaldt
 */
public class ErrorDefinitionPropertyBuilder extends DefinitionReferencePropertyBuilder<Error> {

	private static final EStructuralFeature ERROR_REF_FEATURE = Bpmn2Package.eINSTANCE.getErrorEventDefinition_ErrorRef();
	private static final EStructuralFeature ERROR_NAME_FEATURE = Bpmn2Package.eINSTANCE.getError_Name();

	public ErrorDefinitionPropertyBuilder(Composite parent, GFPropertySection section, ErrorEventDefinition bo) {
		super(parent, section, bo, "Error", ERROR_REF_FEATURE, ERROR_NAME_FEATURE, Error.class);
	}
	
	@Override
	public void create() {
		// create error ref
		super.create();
		
		// create error definitions table
		createDefinitionsTable();
	}

	private void createDefinitionsTable() {
		new DefinitionsPropertiesBuilder(parent, section, ModelUtil.getDefinitions(bo)).createErrorMappingsTable();
	}
}
