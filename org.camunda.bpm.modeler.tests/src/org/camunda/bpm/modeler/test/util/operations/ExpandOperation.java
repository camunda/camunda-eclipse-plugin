package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.features.activity.subprocess.ExpandFlowNodeFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class ExpandOperation extends Operation<CustomContext, ExpandFlowNodeFeature> {

	public ExpandOperation(IDiagramTypeProvider diagramTypeProvider, PictogramElement pictogramElement) {
		super(diagramTypeProvider);

		this.context = createContext();

		PictogramElement[] elements = { pictogramElement };
		this.context.setPictogramElements(elements);
	}
	
	@Override
	protected ExpandFlowNodeFeature createFeature(CustomContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return new ExpandFlowNodeFeature(featureProvider);
	}

	@Override
	protected CustomContext createContext() {
		CustomContext customContext = new CustomContext();
		return customContext;
	}

	public static ExpandOperation expand(PictogramElement element, IDiagramTypeProvider typeProvider) {
		return new ExpandOperation(typeProvider, element);
	}
}
