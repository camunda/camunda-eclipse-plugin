package org.eclipse.bpmn2.modeler.ui.features;

import java.io.IOException;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractCreateFlowFeature;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
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
	public boolean canCreate(ICreateConnectionContext context) {
		BaseElement sourceBo = getSourceBo(context);
		BaseElement targetBo = getTargetBo(context);
		if (sourceBo instanceof ItemAwareElement) {
			if (targetBo instanceof Activity || targetBo instanceof ThrowEvent) {
				return true;
			}
		} else if (targetBo instanceof ItemAwareElement) {
			if (sourceBo instanceof Activity || sourceBo instanceof CatchEvent) {
				return true;
			}
		}
		return false;
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
