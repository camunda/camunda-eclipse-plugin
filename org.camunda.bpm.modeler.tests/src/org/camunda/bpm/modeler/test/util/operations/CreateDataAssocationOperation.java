package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.features.CreateDataAssociationFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class CreateDataAssocationOperation extends AssociationOperation<ICreateConnectionContext, CreateDataAssociationFeature> {

	public CreateDataAssocationOperation(Shape source, Shape target, IDiagramTypeProvider diagramTypeProvider) {
		super(source, target, diagramTypeProvider);
	}

	@Override
	protected ICreateConnectionContext createContext() {
		CreateConnectionContext ctx = new CreateConnectionContext();
		ctx.setSourceAnchor(source.getAnchors().get(1));
		ctx.setTargetAnchor(target.getAnchors().get(1));
		
		return ctx;
	}

	@Override
	protected CreateDataAssociationFeature createFeature(ICreateConnectionContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return new CreateDataAssociationFeature(featureProvider);	
	}
	
	public static CreateDataAssocationOperation createDataAssocation(Shape source, Shape target, IDiagramTypeProvider diagramTypeProvider) {
		return new CreateDataAssocationOperation(source, target, diagramTypeProvider);
	}
}
