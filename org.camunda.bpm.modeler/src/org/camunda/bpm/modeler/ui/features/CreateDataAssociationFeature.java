package org.camunda.bpm.modeler.ui.features;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.features.flow.AbstractCreateFlowFeature;
import org.camunda.bpm.modeler.core.features.rules.ConnectionOperations;
import org.camunda.bpm.modeler.core.features.rules.ConnectionOperations.ConnectionType;
import org.camunda.bpm.modeler.core.features.rules.ConnectionOperations.CreateConnectionOperation;
import org.camunda.bpm.modeler.core.features.rules.ConnectionOperations.StartFormCreateConnectionOperation;
import org.camunda.bpm.modeler.core.utils.AnchorUtil;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class CreateDataAssociationFeature extends AbstractCreateFlowFeature<DataAssociation, BaseElement, BaseElement> {

	public CreateDataAssociationFeature(IFeatureProvider fp) {
		super(fp, "Data Association", "Associate item aware elements like data objects with activities and events");
	}

	@Override
	public boolean canStartConnection(ICreateConnectionContext context) {
		context.putProperty(ConnectionOperations.CONNECTION_TYPE, ConnectionType.DATA_ASSOCIATION);
		StartFormCreateConnectionOperation operation = ConnectionOperations.getStartFromConnectionCreateOperation(context);
		return operation.canExecute(context);
	}
	
	@Override
	public boolean canCreate(ICreateConnectionContext context) {
		context.putProperty(ConnectionOperations.CONNECTION_TYPE, ConnectionType.DATA_ASSOCIATION);
		CreateConnectionOperation operation = ConnectionOperations.getConnectionCreateOperation(context);
		return operation.canExecute(context);
	}

	@Override
	protected String getStencilImageId() {
		return ImageProvider.IMG_16_ASSOCIATION;
	}

	@Override
	protected Class<BaseElement> getSourceClass() {
		return BaseElement.class;
	}

	@Override
	protected Class<BaseElement> getTargetClass() {
		return BaseElement.class;
	}

	@Override
	protected BaseElement getSourceBo(ICreateConnectionContext context) {
		Anchor anchor = context.getSourceAnchor();
		if (anchor != null && anchor.getParent() instanceof Shape) {
			Shape shape = (Shape) anchor.getParent();
			Connection conn = AnchorUtil.getConnectionPointOwner(shape);
			if (conn != null) {
				return BusinessObjectUtil.getFirstElementOfType(conn, getTargetClass());
			}
			return BusinessObjectUtil.getFirstElementOfType(shape, getTargetClass());
		}
		return null;
	}

	@Override
	protected BaseElement getTargetBo(ICreateConnectionContext context) {
		Anchor anchor = context.getTargetAnchor();
		if (anchor != null && anchor.getParent() instanceof Shape) {
			Shape shape = (Shape) anchor.getParent();
			Connection conn = AnchorUtil.getConnectionPointOwner(shape);
			if (conn != null) {
				return BusinessObjectUtil.getFirstElementOfType(conn, getTargetClass());
			}
			return BusinessObjectUtil.getFirstElementOfType(shape, getTargetClass());
		}
		return null;
	}

	@Override
	public DataAssociation createBusinessObject(ICreateConnectionContext context) {
		ModelHandler mh = ModelHandler.getInstance(getDiagram());
		
		BaseElement source = getSourceBo(context);
		BaseElement target = getTargetBo(context);
		
		DataAssociation bo = mh.createDataAssociation(source, target);
		
		putBusinessObject(context, bo);
		
		return bo;
	}

	@Override
	public String getCreateName() {
		return "Data Association";
	}

	@Override
	public EClass getBusinessObjectClass() {
		return Bpmn2Package.eINSTANCE.getDataAssociation();
	}

}
