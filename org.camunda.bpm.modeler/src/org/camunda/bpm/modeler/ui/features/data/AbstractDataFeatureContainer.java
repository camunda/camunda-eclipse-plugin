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
package org.camunda.bpm.modeler.ui.features.data;

import org.camunda.bpm.modeler.core.features.DefaultBpmn2MoveShapeFeature;
import org.camunda.bpm.modeler.core.features.DirectEditNamedElementFeature;
import org.camunda.bpm.modeler.core.features.UpdateBaseElementNameFeature;
import org.camunda.bpm.modeler.core.features.api.IDecorateFeature;
import org.camunda.bpm.modeler.core.features.container.BaseElementFeatureContainer;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.ui.features.AbstractDefaultDeleteFeature;
import org.camunda.bpm.modeler.ui.features.LayoutBaseElementTextFeature;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Participant;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

public abstract class AbstractDataFeatureContainer extends BaseElementFeatureContainer {

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		return new UpdateBaseElementNameFeature(fp);
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
		return new DirectEditNamedElementFeature(fp);
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return new LayoutBaseElementTextFeature(fp) {

			@Override
			public int getMinimumWidth() {
				return GraphicsUtil.DATA_WIDTH;
			}
		};
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
		return new DefaultBpmn2MoveShapeFeature(fp) {

			@Override
			public boolean canMoveShape(IMoveShapeContext context) {
				
				// quick fix: cannot add those guys on connections
				
				if (context.getTargetConnection() != null) {
					return false;
				}

				ContainerShape targetContainer = context.getTargetContainer();
				
				BaseElement targetBusinessObject = BusinessObjectUtil.getFirstBaseElement(targetContainer);
				
				return targetBusinessObject instanceof Participant ||
					   targetBusinessObject instanceof Lane ||
					   targetBusinessObject instanceof Collaboration ||
					   targetBusinessObject instanceof Process ||
					   targetBusinessObject instanceof FlowElementsContainer;
			}
		};
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
		return new DefaultResizeShapeFeature(fp) {
			@Override
			public boolean canResizeShape(IResizeShapeContext context) {
				return false;
			}
		};
	}

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider fp) {
		return new AbstractDefaultDeleteFeature(fp);
	}
}
