package org.camunda.bpm.modeler.ui.features;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.camunda.bpm.modeler.core.features.AbstractAddBpmnShapeFeature;
import org.camunda.bpm.modeler.core.layout.util.ConnectionUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.core.utils.transform.Transformer;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.AreaContext;
import org.eclipse.graphiti.features.context.impl.DeleteContext;
import org.eclipse.graphiti.features.context.impl.RemoveContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.ui.internal.util.ui.PopupMenu;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public abstract class AbstractMorphNodeFeature<T extends FlowNode> extends AbstractCustomFeature {

	private boolean changed = false;
	
	// label provider for the popup menu that displays allowable Activity subclasses
	protected static ILabelProvider labelProvider = new ILabelProvider() {

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
	
	public AbstractMorphNodeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		PictogramElement[] pictogramElements = context.getPictogramElements();
		
		if (pictogramElements == null || pictogramElements.length != 1) {
			return false;
		}
		
		PictogramElement pictogramElement = pictogramElements[0];
		
		if (!(pictogramElement instanceof Shape)) {
			return false;
		}
		
		EObject bo = (EObject) getBusinessObjectForPictogramElement(pictogramElement);
		
		return bo instanceof FlowNode && !(excludeTypes(bo).contains(bo.eClass()));
	}
	
	@Override
	public boolean hasDoneChanges() {
		return changed;
	}

	@Override
	public void execute(ICustomContext context) {
		PictogramElement[] pictogramElements = context.getPictogramElements();
		Shape oldShape = (Shape) pictogramElements[0];
		
		EObject bo = (EObject) getBusinessObjectForPictogramElement(oldShape);
		
		List<EClass> availableTypes = getAvailableTypes(bo);
		
		if (availableTypes.size() < 1) {
			return;
		}
		
		EClass selectedType = selectType(availableTypes);
			
		if (selectedType == null) {
			return;
		}
		
		T newObject = morph(bo, selectedType);
		
		Shape newShape = createNewShape(oldShape, newObject);
		
		reconnect(newObject, oldShape, newShape);
		
		removePictogramElement(oldShape);
		changed = true;
	}
	
	public abstract EClass getBusinessObjectClass();
	
	protected List<EClass> getAvailableTypes(EObject bo) {
		EClass newType = getBusinessObjectClass();
		
		List<EClass> availableTypes = new ArrayList<EClass>();
		
		List<EClassifier> classifiers = Bpmn2Package.eINSTANCE.getEClassifiers();
		
		for (EClassifier classifier : classifiers) {
			
			if (classifier instanceof EClass) {
				EClass type = (EClass) classifier;

				if (type.isAbstract() || bo.eClass().equals(type) || excludeTypes(bo).contains(type)) {
					continue;
				}
				
				List<EClass> superTypes = type.getEAllSuperTypes();
				
				if (superTypes.contains(newType)) {
					availableTypes.add(type);
				}
			}
		}
		
		return availableTypes;
		
	}
	
	protected List<EClass> excludeTypes(EObject bo) {
		return Collections.emptyList();
	}
	
	protected EClass selectType(List<EClass> availableCls) {
		PopupMenu popupMenu = new PopupMenu(availableCls, labelProvider);
		boolean showPopup = popupMenu.show(Display.getCurrent().getActiveShell());
		if (showPopup) {
			return (EClass) popupMenu.getResult();
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected T  morph(EObject bo, EClass to) {
		Transformer transformer = new Transformer(bo);
		return (T) transformer.morph(to);
	}
	
	protected Shape createNewShape(Shape oldShape, T newObject) {
		GraphicsAlgorithm ga = oldShape.getGraphicsAlgorithm();
		
		int height = ga.getHeight();
		int width = ga.getWidth();
		int x = ga.getX();
		int y = ga.getY();
		
		AreaContext areaContext = new AreaContext();
		areaContext.setLocation(x, y);
		areaContext.setSize(width, height);
		
		AddContext addContext = new AddContext(areaContext, newObject);
		addContext.setTargetContainer(oldShape.getContainer());
		addContext.putProperty(AbstractAddBpmnShapeFeature.MORPH_ELEMENT_TYPE, true);
		
		return (Shape) getFeatureProvider().addIfPossible(addContext);		
	}
	
	protected void reconnect(EObject bo, Shape oldShape, Shape newShape) {
		List<Anchor> oldAnchors = oldShape.getAnchors();
		List<Anchor> newAnchors = newShape.getAnchors();
		
		for (int i = 0; i < oldAnchors.size(); i++) {
			
			Anchor oldAnchor = oldAnchors.get(i);
			Anchor newAnchor = newAnchors.get(i);

			swapConnections(bo, oldAnchor.getIncomingConnections(), newAnchor.getIncomingConnections(), true);
			swapConnections(bo, oldAnchor.getOutgoingConnections(), newAnchor.getOutgoingConnections(), false);
		}		
	}
	
	private void swapConnections(EObject bo, List<Connection>  from, List<Connection> to, boolean incoming) {
		// create a copy to avoid concurent modification excpetion
		from = new ArrayList<Connection>(from);
		
		// iterate over connections
		for (Connection connection : from) {
			
			if (ConnectionUtil.isMessageFlow(connection)) {
				handleMessageFlow(bo, connection, to, incoming);
			} else
			if (ConnectionUtil.isSequenceFlow(connection)) {
				handleSequenceFlow(bo, connection, to, incoming);
			} else
			if (ConnectionUtil.isAssociation(connection)) {
				handleAssociation(bo, connection, to, incoming);
			} else
			if (ConnectionUtil.isDataAssociation(connection)) {
				handleDataAssociation(bo, connection, to, incoming);
			}
		}			
	}
	
	protected void handleMessageFlow(EObject bo, Connection messageFlow, List<Connection> connections, boolean incoming) {
		handleConnection(messageFlow, connections);
	}

	protected void handleSequenceFlow(EObject bo, Connection sequenceFlow, List<Connection> connections, boolean incoming) {
		handleConnection(sequenceFlow, connections);
	}
	
	protected void handleAssociation(EObject bo, Connection association, List<Connection> connections, boolean incoming) {
		handleConnection(association, connections);
	}
	
	protected void handleDataAssociation(EObject bo, Connection dataAssocation, List<Connection> connections, boolean incoming) {
		handleConnection(dataAssocation, connections);
	}
	
	protected final void handleConnection(Connection dataAssocation, List<Connection> connections) {
		connections.add(dataAssocation);
	}
	
	protected void removePictogramElement(PictogramElement pictogramElement) {
		IRemoveContext context = new RemoveContext(pictogramElement);
		IRemoveFeature removeFeature = getFeatureProvider().getRemoveFeature(context);
		removeFeature.remove(context);
	}
	
	protected void deletePictogramElement(PictogramElement pictogramElement) {
		IDeleteContext context = new DeleteContext(pictogramElement);
		IDeleteFeature deleteFeature = getFeatureProvider().getDeleteFeature(context);
		deleteFeature.delete(context);		
	}
}
