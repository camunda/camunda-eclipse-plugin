package org.camunda.bpm.modeler.ui.features.gateway;

import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.ui.diagram.Bpmn2FeatureProvider;
import org.camunda.bpm.modeler.ui.features.AbstractMorphNodeFeature;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.UpdateContext;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.jface.viewers.ILabelProvider;

public class MorphGatewayFeature extends AbstractMorphNodeFeature<Gateway> {

	public MorphGatewayFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public String getName() {
		return "Morph Gateway";
	}

	@Override
	public String getDescription() {
		return "Change the Gateway type";
	}

	@Override
	public EClass getBusinessObjectClass() {
		return Bpmn2Package.eINSTANCE.getGateway();
	}
	
	@Override
	protected Shape updateShape(MorphOption option, Shape oldShape, Gateway newObject) {
		UpdateContext updateContext = new UpdateContext(oldShape);
		
		// set isMarkerVisible="true" for exclusive gateways
		// remove isMarkerVisible for all other elements because it has no semantics on them
		BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(oldShape, BPMNShape.class);
		if (newObject instanceof ExclusiveGateway) {
			bpmnShape.setIsMarkerVisible(true);
		} else {
			bpmnShape.eUnset(BpmnDiPackage.eINSTANCE.getBPMNShape_IsMarkerVisible());
		}
		
		IFeature updateFeature = getFeatureProvider().getUpdateFeature(updateContext);
		if (updateFeature != null && updateFeature.canExecute(updateContext)) {
			updateFeature.execute(updateContext);
		}
		
		return oldShape;
	}
	
	@Override
	public ILabelProvider getLabelProvider() {
		if (labelProvider == null) {
			labelProvider = new CreateLabelProvider((Bpmn2FeatureProvider) getFeatureProvider());
		}
		return labelProvider;
	}
}
