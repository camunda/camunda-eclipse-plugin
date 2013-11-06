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

package org.camunda.bpm.modeler.ui.features;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.features.AbstractBpmn2AddShapeFeature;
import org.camunda.bpm.modeler.core.features.AbstractCreateFlowElementFeature;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.preferences.Bpmn2Preferences;
import org.camunda.bpm.modeler.core.runtime.ModelEnablementDescriptor;
import org.camunda.bpm.modeler.core.runtime.TargetRuntime;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.runtime.engine.model.BoundaryEvent;
import org.camunda.bpm.modeler.ui.diagram.Bpmn2FeatureProvider;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CompensateEventDefinition;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.AreaContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
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
	
	private boolean changesDone = false;;
	
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
						// ...and connect this shape to the new one
						createNewConnection(mh, oldShape, newShape);
						changesDone = true;
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
		
		Bpmn2FeatureProvider fp = (Bpmn2FeatureProvider) getFeatureProvider();
		AbstractCreateFeature createFeature = (AbstractCreateFeature) fp.getCreateFeatureForBusinessObject(newType);
		
		CreateContext createContext = new CreateContext();
		createContext.putProperty(AbstractCreateFlowElementFeature.SKIP_ADD_GRAPHICS, true);
		createContext.setTargetContainer(oldShape.getContainer());
		
		FlowElement newObject = null;
		if (createFeature != null) {
			newObject = (FlowElement) createFeature.create(createContext)[0];
		}else {
			newObject = (FlowElement) mh.create(newType);
		}
		
		EObject bo = (EObject) getBusinessObjectForPictogramElement(oldShape);
		
		// Hook: If the source element is an boundary compensation event
		// and the new object an Activity, then set the flag "isForCompensation"
		// to true!
		if (isCompensationBoundaryEvent(bo) && newObject instanceof Activity) {
			Activity newActivity = (Activity) newObject;
			
			newActivity.setIsForCompensation(true);
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
		AbstractBpmn2AddShapeFeature af = (AbstractBpmn2AddShapeFeature)getFeatureProvider().getAddFeature(ac);
		int w = af.getDefaultWidth();
		int h = af.getDefaultHeight();
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
		
		Anchor sourceAnchor = LayoutUtil.getCenterAnchor(oldShape);
		Anchor targetAnchor = LayoutUtil.getCenterAnchor(newShape);

		CreateConnectionContext context = new CreateConnectionContext();
		
		context.setSourceAnchor(sourceAnchor);
		context.setSourcePictogramElement(oldShape);
		
		context.setTargetAnchor(targetAnchor);
		context.setTargetPictogramElement(newShape);
		
		// Assuming that the array of create connection features has the following order:
		// (1) SequenceFlowFeatureContainer
		// (2) MessageFlowFeatureContainer
		// (3) AssociationFeatureContainer
		// (4) ConversationLinkFeatureContainer
		// (5) DataAssociationFeatureContainer
		// The first one who can create a connection between the elements wins!
		ICreateConnectionFeature[] createConnectionFeatures = getFeatureProvider().getCreateConnectionFeatures();
		for (ICreateConnectionFeature createFeature : createConnectionFeatures) {
			if (createFeature.canCreate(context)) {
				Connection connection = createFeature.create(context);
				return connection;
			}
		}
		
		return null;
	}
	
	/**
	 * @return
	 */
	public abstract EClass getBusinessObjectClass();

	@Override
	public boolean hasDoneChanges() {
		return changesDone;
	}
	
	protected final boolean isCompensationBoundaryEvent(ICustomContext context) {
		PictogramElement[] pictogramElements = context.getPictogramElements();
		
		if (pictogramElements == null || pictogramElements.length != 1) {
			return false;
		}
		
		PictogramElement pictogramElement = pictogramElements[0];
		
		EObject bo = (EObject) getBusinessObjectForPictogramElement(pictogramElement);
		
		return isCompensationBoundaryEvent(bo);
	}
	
	private boolean isCompensationBoundaryEvent(EObject bo) {
		if (!(bo instanceof BoundaryEvent)) {
			return false;
		}
		
		BoundaryEvent boundaryEvent = (BoundaryEvent) bo;
		
		List<EventDefinition> eventDefinitions = boundaryEvent.getEventDefinitions();
		
		if (!eventDefinitions.isEmpty() && eventDefinitions.size() == 1) {
			return eventDefinitions.get(0) instanceof CompensateEventDefinition;
		}
		
		return false;
	}
}
