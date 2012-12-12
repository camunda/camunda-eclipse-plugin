package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.bpmn2.modeler.ui.features.CreateDataAssociationFeature;

public class CreateDataAssocationOperation extends AssociationOperation<ICreateConnectionContext, CreateDataAssociationFeature> {

	public CreateDataAssocationOperation(Shape source, Shape target, IDiagramTypeProvider diagramTypeProvider) {
		super(source, target, diagramTypeProvider);
	}

	@Override
	protected void createContext() {
		CreateConnectionContext tmpContext = new CreateConnectionContext();
		tmpContext.setSourceAnchor(source.getAnchors().get(1));
		tmpContext.setTargetAnchor(target.getAnchors().get(1));
		
		context = tmpContext;
	}

	@Override
	protected void createFeature() {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		feature = new CreateDataAssociationFeature(featureProvider);	
	}
	
	public static CreateDataAssocationOperation createDataAssocation(Shape source, Shape target, IDiagramTypeProvider diagramTypeProvider) {
		return new CreateDataAssocationOperation(source, target, diagramTypeProvider);
	}
	
}
