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
 * @author Innar Made
 ******************************************************************************/
package org.camunda.bpm.modeler.ui.features.event;

import org.camunda.bpm.modeler.core.features.MoveFlowNodeFeature;
import org.camunda.bpm.modeler.core.features.MultiUpdateFeature;
import org.camunda.bpm.modeler.core.features.event.AbstractCreateEventFeature;
import org.camunda.bpm.modeler.core.features.event.AbstractUpdateEventFeature;
import org.camunda.bpm.modeler.core.features.event.AddEventFeature;
import org.camunda.bpm.modeler.ui.Images;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;

public class StartEventFeatureContainer extends AbstractEventFeatureContainer {

	static final String INTERRUPTING = "interrupting";

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof StartEvent;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateStartEventFeature(fp);
	}
	
	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddEventFeature<StartEvent>(fp) {
			
			@Override
			protected void setProperties(IAddContext context, ContainerShape newShape) {
				super.setProperties(context, newShape);
				
				IPeService peService = Graphiti.getPeService();
				
				// set to default so that property change is picked up in update
				// and event is decorated accordingly
				peService.setPropertyValue(newShape, INTERRUPTING, "true");
			}
		};
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
		return new StartEventMoveFeature(fp);
	}
	
	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		return new MultiUpdateFeature(fp)
			.addUpdateFeature(super.getUpdateFeature(fp))
			.addUpdateFeature(new UpdateSubProcessEventFeature(fp))
			.addUpdateFeature(new UpdateStartEventFeature(fp));
	}
	
	@Override
	public ICustomFeature[] getCustomFeatures(IFeatureProvider fp) {
		ICustomFeature[] superFeatures = super.getCustomFeatures(fp);
		ICustomFeature[] thisFeatures = new ICustomFeature[1 + superFeatures.length];
		int i;
		for (i=0; i<superFeatures.length; ++i)
			thisFeatures[i] = superFeatures[i];
		thisFeatures[i++] = new MorphStartEventFeature(fp);
		return thisFeatures;
	}

	public static class CreateStartEventFeature extends AbstractCreateEventFeature<StartEvent> {

		public CreateStartEventFeature(IFeatureProvider fp) {
			super(fp, "Start Event", "Indicates the start of a process or choreography");
		}

		@Override
		public String getStencilImageId() {
			return Images.IMG_16_START_EVENT;
		}

		/* (non-Javadoc)
		 * @see org.camunda.bpm.modeler.features.AbstractCreateFlowElementFeature#getFlowElementClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getStartEvent();
		}
	}

	protected static class UpdateStartEventFeature extends AbstractUpdateEventFeature {

		public UpdateStartEventFeature(IFeatureProvider fp) {
			super(fp);
		}
	}

	private static boolean isEventSubprocess(EObject container) {
		if (container instanceof SubProcess) {
			SubProcess subProcess = (SubProcess) container;
			
			return subProcess.isTriggeredByEvent();
		}
		
		return false;
	}
	
	public static class StartEventMoveFeature extends MoveFlowNodeFeature {
		
		public StartEventMoveFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public boolean canMoveShape(IMoveShapeContext context) {
			if (!super.canMoveShape(context)) {
				return false;
			}
			
			// make sure interrupting events 
			// may not be moved out of target container
			StartEvent event = (StartEvent) getBusinessObjectForPictogramElement(context.getShape());
			if (!event.isIsInterrupting() && isEventSubprocess(event.eContainer())) {
				EObject targetObject = (EObject) getBusinessObjectForPictogramElement(context.getTargetContainer());
				return isEventSubprocess(targetObject);
			}
			
			return true;
		}
	}
	
	public static class UpdateSubProcessEventFeature extends AbstractUpdateFeature {

		public UpdateSubProcessEventFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public boolean canUpdate(IUpdateContext context) {
			Object o = getBusinessObjectForPictogramElement(context.getPictogramElement());
			return o != null && o instanceof StartEvent;
		}

		@Override
		public IReason updateNeeded(IUpdateContext context) {
			IPeService peService = Graphiti.getPeService();
			PictogramElement element = context.getPictogramElement();

			String prop = peService.getPropertyValue(element, INTERRUPTING);
			if (prop == null) {
				return Reason.createFalseReason();
			}

			StartEvent event = (StartEvent) getBusinessObjectForPictogramElement(element);
			boolean interrupting = Boolean.parseBoolean(prop);
			IReason reason = event.isIsInterrupting() == interrupting ? Reason.createFalseReason() : Reason
					.createTrueReason();
			return reason;
		}

		@Override
		public boolean update(IUpdateContext context) {
			IPeService peService = Graphiti.getPeService();
			ContainerShape container = (ContainerShape) context.getPictogramElement();
			StartEvent event = (StartEvent) getBusinessObjectForPictogramElement(container);

			Ellipse ellipse = (Ellipse) peService.getAllContainedShapes(container).iterator().next()
					.getGraphicsAlgorithm();
			LineStyle style = event.isIsInterrupting() ? LineStyle.SOLID : LineStyle.DASH;
			ellipse.setLineStyle(style);

			peService.setPropertyValue(container, INTERRUPTING, Boolean.toString(event.isIsInterrupting()));
			return true;
		}
	}
}