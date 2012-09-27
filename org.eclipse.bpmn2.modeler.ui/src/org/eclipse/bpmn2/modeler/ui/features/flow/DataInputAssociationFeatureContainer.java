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
package org.eclipse.bpmn2.modeler.ui.features.flow;

import java.io.IOException;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractAddFlowFeature;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class DataInputAssociationFeatureContainer extends AssociationFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof DataInputAssociation;
	}
	
	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AbstractAddFlowFeature<DataInputAssociation>(fp) {

			@Override
			protected Polyline createConnectionLine(Connection connection) {
				IPeService peService = Graphiti.getPeService();
				IGaService gaService = Graphiti.getGaService();
				BaseElement be = BusinessObjectUtil.getFirstBaseElement(connection);

				Polyline connectionLine = super.createConnectionLine(connection);
				connectionLine.setLineStyle(LineStyle.DOT);

				ConnectionDecorator endDecorator = peService.createConnectionDecorator(connection, false, 1.0, true);

				int w = 5;
				int l = 10;

				Polyline arrowhead = gaService.createPolyline(endDecorator, new int[] { -l, w, 0, 0, -l, -w });
				StyleUtil.applyStyle(arrowhead, be);
				
				return connectionLine;
			}

			@Override
			protected Class<? extends BaseElement> getBoClass() {
				return DataInputAssociation.class;
			}
		};
	}
	
	 @Override
	  public ICreateConnectionFeature getCreateConnectionFeature(IFeatureProvider fp) {
	    return new CreateAssociationFeature(fp) {
	      @Override
	      public Association createBusinessObject(ICreateConnectionContext context) {
	        Association bo = null;
	        try {
	          ModelHandler mh = ModelHandler.getInstance(getDiagram());
	          BaseElement source = getSourceBo(context);
	          BaseElement target = getTargetBo(context);
	          bo = mh.createAssociation(source, target);
	          putBusinessObject(context, bo);

	        } catch (IOException e) {
	          // TODO Auto-generated catch block
	          e.printStackTrace();
	        }
	        return bo;
	      }
	      
	      @Override
	      public boolean canCreate(ICreateConnectionContext context) {
	        BaseElement sourceBo = getSourceBo(context);
	        BaseElement targetBo = getTargetBo(context);
	        
	        return sourceBo instanceof ItemAwareElement || targetBo instanceof ItemAwareElement;
	      }
	      
	      
	      
	      @Override
	      public String getCreateName() {
	          return "Data Association";
	      }
	      
	      @Override
	      public EClass getBusinessObjectClass() {
	        return Bpmn2Package.eINSTANCE.getDataAssociation();
	      }
	    };
	  }

}