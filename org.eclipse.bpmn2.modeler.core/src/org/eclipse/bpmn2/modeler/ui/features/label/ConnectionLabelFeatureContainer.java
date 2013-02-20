package org.eclipse.bpmn2.modeler.ui.features.label;

import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.modeler.core.features.DirectEditNamedConnectionFeature;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.custom.ICustomFeature;

public class ConnectionLabelFeatureContainer extends LabelFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		// these all have Label features
		return o instanceof SequenceFlow ||
			   o instanceof MessageFlow;
	}
	
	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
		return new DirectEditNamedConnectionFeature(fp); 
	}
	
	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddConnectionLabelFeature(fp);
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
		return new MoveConnectionLabelFeature(fp);
	}
	
	@Override
	public ICustomFeature[] getCustomFeatures(IFeatureProvider fp) {
		return new ICustomFeature[] {
			new RepositionConnectionLabelFeature(fp)
		};
	}
}
