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
 * @author Bob Brodt
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.ui.features;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.features.AbstractAddBPMNShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.AbstractCreateFlowElementFeature;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.runtime.ModelEnablementDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.Tuple;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNFeatureProvider;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.AreaContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.ILayoutService;
import org.eclipse.graphiti.ui.internal.util.ui.PopupMenu;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * @author Bob Brodt
 *
 */
public abstract class AbstractAppendNodeNodeFeature<T extends FlowNode> extends AbstractCustomFeature {
	
	// label provider for the popup menu that displays allowable Activity subclasses
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
			return ModelUtil.toDisplayName(((EClass)element).getName());
		}

		public Image getImage(Object element) {
			return null;
		}

	};

	/**
	 * @param fp
	 */
	public AbstractAppendNodeNodeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		return true;
	}

	@Override
	public boolean isAvailable(IContext context) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.features.custom.ICustomFeature#execute(org.eclipse.graphiti.features.context.ICustomContext)
	 */
	@Override
	public void execute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			PictogramElement pe = pes[0];
			Object bo = getBusinessObjectForPictogramElement(pe);
			if (pe instanceof ContainerShape && bo instanceof FlowNode) {
				try {
					ContainerShape oldShape = (ContainerShape)pe;
					ModelHandler mh = ModelHandler.getInstance(getDiagram());
					
					// Let user select the new type of object to append. The selection will
					// be from a list of subtypes of <code>T</code> as defined by the various
					// AbstractAppendNodeNodeFeature specializations; for example the class
					// AppendActivityFeature will construct a popup list of all Activity subclasses
					// e.g. Task, ScriptTask, SubProcess, etc. 
					EClass newType = selectNewObjectType((EObject)bo);
					if (newType!=null) {
						// if user made a selection, then create the new shape...
						ContainerShape newShape = createNewShape(mh, oldShape, newType);
						// ...and connect this shape to the new one with a SequenceFlow
						createNewConnection(mh, oldShape, newShape);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected EClass selectNewObjectType(EObject oldObject) {
		ModelEnablementDescriptor enablements = TargetRuntime.getCurrentRuntime().getModelEnablements(oldObject);
		EClass newType = getBusinessObjectClass();

		// build a list of possible subclasses for the popup menu
		List<EClass> subtypes = new ArrayList<EClass>();
		for (EClassifier ec : Bpmn2Package.eINSTANCE.getEClassifiers() ) {
			if (ec instanceof EClass) {
				if ( ((EClass) ec).isAbstract()) {
					continue;
				}
				EList<EClass>superTypes = ((EClass)ec).getEAllSuperTypes(); 
				if (superTypes.contains(newType) &&
						enablements.isEnabled((EClass)ec)) {
					if (ec!=Bpmn2Package.eINSTANCE.getBoundaryEvent() &&
							ec!=Bpmn2Package.eINSTANCE.getStartEvent()) {
						subtypes.add((EClass)ec);
					}
				}
			}
		}
		
		// show popup menu
		boolean doit = subtypes.size()>0;
		if (doit) {
			newType = subtypes.get(0);
			if (subtypes.size()>1) {
				PopupMenu popupMenu = new PopupMenu(subtypes, labelProvider);
				doit = popupMenu.show(Display.getCurrent().getActiveShell());
				if (doit) {
					newType = (EClass) popupMenu.getResult();
					return newType;
				}
			}
		}

		return null;
	}

	protected ContainerShape createNewShape(ModelHandler mh, ContainerShape oldShape, EClass newType) {
		ILayoutService layoutService = Graphiti.getLayoutService();
		boolean horz = Bpmn2Preferences.getInstance().isHorizontalDefault();

		ILocation loc = layoutService.getLocationRelativeToDiagram(oldShape);
		int x = loc.getX();
		int y = loc.getY();
		int xOffset = 0;
		int yOffset = 0;
		GraphicsAlgorithm ga = oldShape.getGraphicsAlgorithm();
		int width = ga.getWidth();
		int height = ga.getHeight();
		
		BPMNFeatureProvider fp = (BPMNFeatureProvider) getFeatureProvider();
		AbstractCreateFeature createFeature = (AbstractCreateFeature) fp.getCreateFeatureForBusinessObject(newType.getInstanceClass());
		
		CreateContext createContext = new CreateContext();
		createContext.putProperty(AbstractCreateFlowElementFeature.OPTION_DONT_ADD, "true");
		createContext.setTargetContainer(oldShape.getContainer());
		
		FlowElement newObject = null;
		if (createFeature != null) {
			newObject = (FlowElement) createFeature.create(createContext)[0];
		}else {
			newObject = (FlowElement) mh.create(newType);
		}
		
		ContainerShape containerShape = oldShape.getContainer();
		if (containerShape!=getDiagram()) {
			// we are adding a new shape to a control (e.g a SubProcess)
			// so we need to adjust the location to be relative to the
			// control instead of the diagram
			loc = layoutService.getLocationRelativeToDiagram(containerShape);
			xOffset = loc.getX();
			yOffset = loc.getY();
		}
		BaseElement oldObject = BusinessObjectUtil.getFirstElementOfType(oldShape, BaseElement.class);
		
		if (oldObject instanceof Lane) {
			((Lane)oldObject).getFlowNodeRefs().add((FlowNode)newObject);
		}
		AddContext ac = new AddContext(new AreaContext(), newObject);
		AbstractAddBPMNShapeFeature af = (AbstractAddBPMNShapeFeature)getFeatureProvider().getAddFeature(ac);
		int w = af.getWidth();
		int h = af.getHeight();
		if (horz) {
			x += width + 50 + w/2;
			y += height/2;
			boolean done = false;
			while (!done) {
				done = true;
				List<Shape> shapes = getFlowElementChildren(containerShape);
				for (Shape s : shapes) {
					if (GraphicsUtil.intersects(s, x-w/2, y-h/2, w, h)) {
						y += 100;
						done = false;
						break;
					}
				}
			}
		}
		else {
			x += width/2;
			y += height + 50 + h/2;
			boolean done = false;
			while (!done) {
				done = true;
				List<Shape> shapes = getFlowElementChildren(containerShape);
				for (Shape s : shapes) {
					if (GraphicsUtil.intersects(s, x-w/2, y-h/2, w, h)) {
						x += 100;
						done = false;
						break;
					}
				}
			}
		}
		ac.setX(x - xOffset);
		ac.setY(y - yOffset);
		ac.setTargetContainer( oldShape.getContainer() );
		
		return (ContainerShape) getFeatureProvider().addIfPossible(ac);
	}

	protected List<Shape> getFlowElementChildren(ContainerShape containerShape) {
		List<Shape> children = new ArrayList<Shape>();
		for (Shape s : containerShape.getChildren()) {
			FlowElement bo = BusinessObjectUtil.getFirstElementOfType(s, FlowElement.class);
			if (s instanceof ContainerShape && bo!=null) {
				children.add(s);
			}
		}
		return children;
	}
	
	protected Connection createNewConnection(ModelHandler mh, ContainerShape oldShape, ContainerShape newShape) {
		Tuple<FixPointAnchor, FixPointAnchor> anchors = AnchorUtil.getSourceAndTargetBoundaryAnchors(oldShape, newShape, null);

		CreateConnectionContext ccc = new CreateConnectionContext();
		ccc.setSourcePictogramElement(oldShape);
		ccc.setTargetPictogramElement(newShape);
		ccc.setSourceAnchor(anchors.getFirst());
		ccc.setTargetAnchor(anchors.getSecond());

		FlowNode oldObject = BusinessObjectUtil.getFirstElementOfType(oldShape, FlowNode.class);
		FlowNode newObject = BusinessObjectUtil.getFirstElementOfType(newShape, FlowNode.class);
		
		AddConnectionContext acc = new AddConnectionContext(ccc.getSourceAnchor(), ccc.getTargetAnchor());
		SequenceFlow flow = mh.createSequenceFlow(oldObject, newObject);
		acc.setNewObject(flow);
		Connection connection = (Connection)getFeatureProvider().addIfPossible(acc);
		
		if (connection instanceof FreeFormConnection) {
			// avoid diagonal lines by inserting bendpoints (prefer orthogonal layouts)
			ILayoutService layoutService = Graphiti.getLayoutService();
			ILocation loc0 = layoutService.getLocationRelativeToDiagram(connection.getStart());
			ILocation loc1 = layoutService.getLocationRelativeToDiagram(connection.getEnd());
			if (loc0.getX()!=loc1.getX() && loc0.getY()!=loc1.getY()) {
				boolean horz = Bpmn2Preferences.getInstance().isHorizontalDefault();
				FreeFormConnection ff = (FreeFormConnection)connection;
				Point p;
				if (horz) {
					p = Graphiti.getCreateService().createPoint(loc0.getX(), loc1.getY());
				}
				else {
					p = Graphiti.getCreateService().createPoint(loc1.getX(), loc0.getY());
				}
				ff.getBendpoints().add(p);
				DIUtils.updateDIEdge(connection);
				
				// adjust the anchor point to the new shape if necessary
				DiagramElement shape = mh.findDIElement(newObject);
				AnchorUtil.reConnect(shape, getDiagram());
				
				ILocation newloc0 = layoutService.getLocationRelativeToDiagram(connection.getStart());
				ILocation newloc1 = layoutService.getLocationRelativeToDiagram(connection.getEnd());
				if (!newloc0.equals(loc0) || !newloc1.equals(loc1)) {
					// the connection's End anchor has changed as a result of inserting the bendpoint
					// so need to adjust the bendpoint
					ff.getBendpoints().clear();
					if (horz) {
						p = Graphiti.getCreateService().createPoint(newloc0.getX(), newloc1.getY());
					}
					else {
						p = Graphiti.getCreateService().createPoint(newloc1.getX(), newloc0.getY());
					}
					ff.getBendpoints().add(p);
					DIUtils.updateDIEdge(connection);
				}
			}
		}
		return connection;
	}
	
	/**
	 * @return
	 */
	public abstract EClass getBusinessObjectClass();

}
