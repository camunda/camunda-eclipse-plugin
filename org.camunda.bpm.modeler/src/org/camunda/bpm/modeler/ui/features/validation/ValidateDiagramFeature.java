package org.camunda.bpm.modeler.ui.features.validation;

import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.validation.Bpmn2ProjectValidator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.features.custom.ICustomFeature;

/**
 * A {@link ICustomFeature} that validates the BPMN 2.0 diagram
 * using the registered validators.
 * 
 * @author nico.rehwaldt
 * 
 * @see Bpmn2ProjectValidator
 */
public class ValidateDiagramFeature extends AbstractCustomFeature {

	public ValidateDiagramFeature(IFeatureProvider featureProvider) {
		super(featureProvider);
	}
		
	@Override
	public String getName() {
		return "Validate";
	}
	
	@Override
	public boolean canExecute(ICustomContext context) {
		return true;
	}
	
	@Override
	public void execute(ICustomContext context) {
		EObject modelObject = BusinessObjectUtil.getBusinessObjectForPictogramElement(getDiagram());
		Resource resource = modelObject.eResource();
		
		Bpmn2ProjectValidator.validateResource(resource, new NullProgressMonitor());
	}
}
