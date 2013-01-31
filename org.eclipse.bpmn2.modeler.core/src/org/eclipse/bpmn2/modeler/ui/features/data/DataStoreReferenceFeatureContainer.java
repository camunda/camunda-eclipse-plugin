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
package org.eclipse.bpmn2.modeler.ui.features.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataStore;
import org.eclipse.bpmn2.DataStoreReference;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.features.AbstractCreateFlowElementFeature;
import org.eclipse.bpmn2.modeler.core.features.data.AddDataFeature;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerFactory;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.ContextUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.features.LayoutBaseElementTextFeature;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.ui.internal.util.ui.PopupMenu;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class DataStoreReferenceFeatureContainer extends AbstractDataFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof DataStoreReference;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateDataStoreReferenceFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddDataStoreReferenceFeature(fp);
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return new LayoutBaseElementTextFeature(fp) {

			@Override
			public int getMinimumWidth() {
				return 50;
			}
		};
	}
	
	/**
	 * Create datastore reference feature
	 * 
	 * @author nico.rehwaldt
	 */
	public static class CreateDataStoreReferenceFeature extends AbstractCreateFlowElementFeature<DataStoreReference> {

		private static ILabelProvider labelProvider = new ILabelProvider() {

			public void removeListener(ILabelProviderListener listener) {
				
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void dispose() {

			}

			public void addListener(ILabelProviderListener listener) {

			}

			public String getText(Object element) {
				if (((DataStore) element).getId() == null)
					return ((DataStore) element).getName();
				return "Reference existing \"" + ((DataStore) element).getName() + "\"";
			}

			public Image getImage(Object element) {
				return null;
			}

		};
		
		public CreateDataStoreReferenceFeature(IFeatureProvider fp) {
			super(
					fp,
					"Data Store",
					"Reference to a Data Store instance."
							+ " Data Stores provide a mechanism for Activities to persist data beyond the lifetime of the Process."
							+ " The same Data Store instance can be visualized through a Data Store Reference in one or more"
							+ " places in the Process.");
		}

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_DATA_STORE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.bpmn2.modeler.core.features.AbstractCreateFlowElementFeature
		 * #getFlowElementClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getDataStoreReference();
		}

		@Override
		public DataStoreReference createBusinessObject(ICreateContext context) {
			// NOTE: this is slightly different from DataObject/DataObjectReference:
			// Both DataObject and DataObjectReference instances are contained in some FlowElementContainer
			// (e.g. a Process) whereas DataStore instances are contained in the root element "Definitions".
			// This means that whenever the user creates a "Data Store" (using DND from the tool palette),
			// it necessarily means that a DataStoreReference is created and added to the FlowElementContainer
			// which is the target of the ICreateContext. In addition, if the new DataStoreReference refers
			// to a new DataStore, one is created and added to Definitions.
			// 
			DataStoreReference bo = null;
			
			ModelHandler mh = ModelHandler.getInstance(getDiagram());
			bo = Bpmn2ModelerFactory.create(DataStoreReference.class);

			ModelUtil.setID(bo);
			
			DataStore dataStore = Bpmn2ModelerFactory.create(DataStore.class);
			dataStore.setName("Create a new Data Store");
			
			List<DataStore> dataStoreList = new ArrayList<DataStore>();
			dataStoreList.add(dataStore);
			TreeIterator<EObject> iter = mh.getDefinitions().eAllContents();
			while (iter.hasNext()) {
				EObject obj = iter.next();
				if (obj instanceof DataStore)
					dataStoreList.add((DataStore) obj);
			}

			DataStore result = dataStore;
			if (dataStoreList.size() > 1) {
				PopupMenu popupMenu = new PopupMenu(dataStoreList, labelProvider);
				boolean b = popupMenu.show(Display.getCurrent().getActiveShell());
				if (b) {
					result = (DataStore) popupMenu.getResult();
				}
			}
			if (result == dataStore) { // the new one
				mh.addRootElement(dataStore);
				ModelUtil.setID(dataStore);
				dataStore.setName(ModelUtil.toDisplayName(dataStore.getId()));
				bo.setName(dataStore.getName());
			} else
				bo.setName(result.getName() + " Ref");

			bo.setDataStoreRef(result);
			putBusinessObject(context, bo);

			return bo;
		}
	}
	
	/**
	 * Add datastore feature
	 * 
	 * @author nico.rehwaldt
	 */
	public static class AddDataStoreReferenceFeature extends AddDataFeature<DataStoreReference> {

		public AddDataStoreReferenceFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public boolean canAdd(IAddContext context) {
			return true;
		}

		@Override
		protected ContainerShape createPictogramElement(IAddContext context, IRectangle bounds) {
			IGaService gaService = Graphiti.getGaService();
			IPeService peService = Graphiti.getPeService();
			
			DataStoreReference store = getBusinessObject(context);

			int width = bounds.getWidth();
			int height = bounds.getHeight();
			int x = bounds.getX();
			int y = bounds.getY();
			
			ContainerShape newShape = peService.createContainerShape(context.getTargetContainer(), true);
			Rectangle invisibleRect = gaService.createInvisibleRectangle(newShape);
			gaService.setLocationAndSize(invisibleRect, x, y, width, height);

			int whalf = width / 2;

			int[] xy = { 0, 10, whalf, 20, width, 10, width, height - 10, whalf, height, 0, height - 10 };
			int[] bend = { 0, 0, whalf, whalf, 0, 0, 0, 0, whalf, whalf, 0, 0 };
			Polygon polygon = gaService.createPolygon(invisibleRect, xy, bend);
			polygon.setFilled(true);

			StyleUtil.applyStyle(polygon, store);

			xy = new int[] { 0, 14, whalf, 24, width, 14 };
			bend = new int[] { 0, 0, whalf, whalf, 0, 0 };
			Polyline line1 = gaService.createPolyline(invisibleRect, xy, bend);
			line1.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));

			xy = new int[] { 0, 18, whalf, 28, width, 18 };
			Polyline line2 = gaService.createPolyline(invisibleRect, xy, bend);
			line2.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));

			xy = new int[] { 0, 11, whalf, 0, width, 11 };
			Polyline lineTop = gaService.createPolyline(invisibleRect, xy, bend);
			lineTop.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			
			return newShape;
		}

		@Override
		public int getDefaultHeight() {
			return GraphicsUtil.DATA_WIDTH;
		}

		@Override
		public int getDefaultWidth() {
			return GraphicsUtil.DATA_HEIGHT;
		}

		@Override
		protected boolean isSupportCollectionMarkers() {
			return false;
		}
		
		@Override
		public String getName(DataStoreReference t) {
			return t.getName();
		}
	}
}