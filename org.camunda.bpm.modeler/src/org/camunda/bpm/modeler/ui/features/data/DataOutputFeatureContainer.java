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

import org.camunda.bpm.modeler.core.features.data.AbstractCreateDataInputOutputFeature;
import org.camunda.bpm.modeler.core.features.data.AddDataFeature;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.camunda.bpm.modeler.ui.Images;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Polygon;

public class DataOutputFeatureContainer extends AbstractDataFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof DataOutput;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateDataOutputFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddDataFeature<DataOutput>(fp) {
			@Override
			protected boolean isSupportCollectionMarkers() {
				return false;
			}

			@Override
			protected void decorate(Polygon p) {
				Polygon arrow = GraphicsUtil.createDataArrow(p);
				arrow.setFilled(true);
				arrow.setBackground(manageColor(StyleUtil.CLASS_FOREGROUND));
				arrow.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			}

			@Override
			public String getName(DataOutput t) {
				return t.getName();
			}
		};
	}

	public static class CreateDataOutputFeature extends AbstractCreateDataInputOutputFeature<DataOutput> {

		public CreateDataOutputFeature(IFeatureProvider fp) {
			super(fp, "Data Output", "Declaration that a particular kind of data can be produced as output");
		}

		@Override
		public String getStencilImageId() {
			return Images.IMG_16_DATA_OUTPUT;
		}

		/* (non-Javadoc)
		 * @see org.camunda.bpm.modeler.features.AbstractBpmn2CreateFeature#getBusinessObjectClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getDataOutput();
		}
	}
}