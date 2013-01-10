/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *
 * @author Ivar Meikas
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.features.activity;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.modeler.core.features.MoveFlowNodeFeature;
import org.eclipse.bpmn2.modeler.core.features.event.AbstractBoundaryEventOperation;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeLayoutService;

public class MoveActivityFeature extends MoveFlowNodeFeature {

	public static final String ACTIVITY_MOVE_PROPERTY = "activity.move";
	public static final String SELECTION_MOVE_PROPERTY = "selection.move";

	public MoveActivityFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void preMoveShape(IMoveShapeContext context) {
		MoveShapeContext msc = (MoveShapeContext)context;
		ContainerShape oldContainer = context.getSourceContainer();
		ContainerShape newContainer = context.getTargetContainer();
		IPeLayoutService peLayoutService = Graphiti.getPeLayoutService();
		
		ILocation oldLoc = peLayoutService.getLocationRelativeToDiagram(oldContainer);
		ILocation newLoc = peLayoutService.getLocationRelativeToDiagram(newContainer);

		super.preMoveShape(context);
	}

	@Override
	protected void postMoveShape(final IMoveShapeContext context) {
		super.postMoveShape(context);
		PictogramElement containerShape = context.getPictogramElement();
		Activity activity = BusinessObjectUtil.getFirstElementOfType(containerShape, Activity.class);
		Graphiti.getPeService().sendToFront(context.getShape());
		
		new AbstractBoundaryEventOperation() {
			@Override
			protected void doWorkInternal(ContainerShape container) {
				GraphicsAlgorithm ga = container.getGraphicsAlgorithm();

				MoveShapeContext newContext = new MoveShapeContext(container);
				newContext.setDeltaX(context.getDeltaX());
				newContext.setDeltaY(context.getDeltaY());
				newContext.setSourceContainer(context.getSourceContainer());
				newContext.setTargetContainer(context.getTargetContainer());
				newContext.setTargetConnection(context.getTargetConnection());
				newContext.setLocation(ga.getX(), ga.getY());
				newContext.putProperty(ACTIVITY_MOVE_PROPERTY, true);

				IMoveShapeFeature moveFeature = getFeatureProvider().getMoveShapeFeature(newContext);
				if (moveFeature.canMoveShape(newContext)) {
					moveFeature.moveShape(newContext);
				}
			}
		}.doWork(activity, getDiagram());
		
		if (containerShape.eContainer() instanceof ContainerShape) {
			PictogramElement pe = (PictogramElement) containerShape.eContainer();
			if (BusinessObjectUtil.containsElementOfType(pe, SubProcess.class)) {
				layoutPictogramElement(pe);
			}
		}
	}
}