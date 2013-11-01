package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.diagram.BpmnFeatureProvider;
import org.camunda.bpm.modeler.ui.features.activity.subprocess.SubProcessFeatureContainer.CreateSubProcessFeature;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.impl.CreateContext;

/**
 * 
 * @author nico.rehwaldt
 *
 */
public class AddSubProcessOperation extends AbstractAddShapeOperation<CreateSubProcessFeature, AddSubProcessOperation> {

	public AddSubProcessOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddSubProcessOperation.class);
	}
	
	@Override
	protected CreateSubProcessFeature createFeature(CreateContext context) {
		
		BpmnFeatureProvider featureProvider = (BpmnFeatureProvider) diagramTypeProvider.getFeatureProvider();
		IFeature createFeature = featureProvider.getCreateFeatureForBusinessObject(Bpmn2Package.eINSTANCE.getSubProcess());
		
		return (CreateSubProcessFeature) createFeature;
	}

	public static AddSubProcessOperation addSubProcess(IDiagramTypeProvider diagramTypeProvider) {
		return new AddSubProcessOperation(diagramTypeProvider);
	}
}
