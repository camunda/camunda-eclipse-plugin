package org.eclipse.bpmn2.modeler.ui.features.label;

import org.eclipse.bpmn2.modeler.core.utils.LabelUtil;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * Add feature for labels of shapes
 * 
 * @author nico.rehwaldt
 */
public class AddShapeLabelFeature extends AbstractAddLabelFeature implements IAddFeature {

	public AddShapeLabelFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	protected void postAddHook(IAddContext context, ContainerShape newLabel) {
		super.postAddHook(context, newLabel);
		
		LabelUtil.updateStoredShapeLabelOffset(newLabel, getDiagram());
	}
}
