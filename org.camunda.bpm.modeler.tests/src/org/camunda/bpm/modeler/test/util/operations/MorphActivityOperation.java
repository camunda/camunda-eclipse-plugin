package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.features.activity.MorphActivityFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class MorphActivityOperation extends AbstractMorphFlowNodeOperation<MorphActivityFeature, MorphActivityOperation> {

	public MorphActivityOperation(IDiagramTypeProvider diagramTypeProvider, PictogramElement activity) {
		super(diagramTypeProvider, activity, MorphActivityOperation.class);
	}

	@Override
	protected MorphActivityFeature createFeature(CustomContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return new MorphActivityFeature(featureProvider);
	}
	
	public static MorphActivityOperation morphActivity(PictogramElement activity, IDiagramTypeProvider typeProvider) {
		return new MorphActivityOperation(typeProvider, activity);
	}

}
