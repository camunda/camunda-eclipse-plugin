package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.features.activity.subprocess.CollapseFlowNodeFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class CollapseOperation extends Operation<CustomContext, CollapseFlowNodeFeature> {

	public CollapseOperation(IDiagramTypeProvider diagramTypeProvider, PictogramElement pictogramElement) {
		super(diagramTypeProvider);

		this.context = createContext();

		PictogramElement[] elements = { pictogramElement };
		this.context.setPictogramElements(elements);
	}

	@Override
	protected CollapseFlowNodeFeature createFeature(CustomContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return new CollapseFlowNodeFeature(featureProvider);
	}

	@Override
	protected CustomContext createContext() {
		CustomContext customContext = new CustomContext();
		return customContext;
	}

	public static CollapseOperation collapse(PictogramElement element, IDiagramTypeProvider typeProvider) {
		return new CollapseOperation(typeProvider, element);
	}
}
