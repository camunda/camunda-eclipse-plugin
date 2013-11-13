package org.camunda.bpm.modeler.ui.features;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.camunda.bpm.modeler.core.features.AbstractBpmn2CreateFeature;
import org.camunda.bpm.modeler.core.features.rules.ConnectionOperations;
import org.camunda.bpm.modeler.core.features.rules.ConnectionOperations.CreateConnectionOperation;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.core.utils.transform.Transformer;
import org.camunda.bpm.modeler.ui.Images;
import org.camunda.bpm.modeler.ui.diagram.Bpmn2FeatureProvider;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.DeleteContext;
import org.eclipse.graphiti.features.context.impl.RemoveContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.ui.internal.util.ui.PopupMenu;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public abstract class AbstractMorphNodeFeature<T extends FlowNode> extends AbstractCustomFeature {

	public static String MORPH_ELEMENT = "MORPH_ELEMENT";
	public static String CREATE_MODE = AbstractMorphNodeFeature.class.getName() + "_CREATE_MODE";
	
	protected boolean changed = false;
	protected ILabelProvider labelProvider;
	
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
	public String getImageId() {
		return Images.IMG_16_CONFIGURE;
	}

	@Override
	public void execute(ICustomContext context) {
		PictogramElement[] pictogramElements = context.getPictogramElements();
		Shape oldShape = (Shape) pictogramElements[0];
		
		EObject bo = (EObject) getBusinessObjectForPictogramElement(oldShape);
		
		List<MorphOption> options = getOptions(bo);
		
		if (options.size() < 1) {
			return;
		}
		
		// get selection from context (during tests)
		MorphOption option = (MorphOption) ContextUtil.get(context, CREATE_MODE);
		
		if (option == null) {
			
			option = selectOption(options);
			
			if (option == null) {
				return;
			}
		}
		
		T newObject = morph(bo, option);
		
		Shape newShape = updateShape(option, oldShape, newObject);
		
		reconnect(newObject, oldShape, newShape);
		
		cleanUp(oldShape);
		changed = true;
	}
	
	public abstract EClass getBusinessObjectClass();
	
	public List<MorphOption> getOptions(EObject bo) {
		EClass newType = getBusinessObjectClass();
		
		List<MorphOption> availableTypes = new ArrayList<MorphOption>();
		
		List<EClassifier> classifiers = Bpmn2Package.eINSTANCE.getEClassifiers();
		List<EClass> excludeTypes = excludeTypes(bo);
		
		for (EClassifier classifier : classifiers) {
			
			if (classifier instanceof EClass) {
				EClass type = (EClass) classifier;

				EClass actualType = ModelUtil.getActualEClass(type);
				
				if (actualType.isAbstract() || bo.eClass().equals(actualType) || excludeTypes.contains(type) || excludeTypes.contains(actualType)) {
					continue;
				}
				
				List<EClass> superTypes = actualType.getEAllSuperTypes();
				
				if (superTypes.contains(newType)) {
					String name = ModelUtil.toDisplayName(actualType.getName());
					MorphOption option = new MorphOption(name, actualType);
					availableTypes.add(option);
				}
			}
		}
		
		return availableTypes;
		
	}
	
	protected List<EClass> excludeTypes(EObject bo) {
		return Collections.emptyList();
	}
	
	protected MorphOption selectOption(List<MorphOption> availableCls) {
		PopupMenu popupMenu = new PopupMenu(availableCls, getLabelProvider());
		boolean showPopup = popupMenu.show(Display.getCurrent().getActiveShell());
		if (showPopup) {
			return (MorphOption) popupMenu.getResult();
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected T morph(EObject target, MorphOption option) {
		EClass newType = option.getNewType();
		
		if (!newType.equals(target.eClass())) {
			Transformer transformer = new Transformer(target);
			return (T) transformer.morph(newType);
		}
		
		return (T) target;
	}
	
	/**
	 * Update shape after underlaying business object changed
	 * to new type.
	 * 
	 * @param option
	 * @param oldShape
	 * @param newObject
	 * @return
	 */
	protected abstract Shape updateShape(MorphOption option, Shape oldShape, T newObject);

	/**
	 * Perform cleanup on pictogram element
	 * 
	 * @param pictogramElement
	 */
	protected void cleanUp(PictogramElement pictogramElement) {
		
	}
	
	/**
	 * Reconnect shapes by fixing all incoming and outgoing connetions
	 * 
	 * @param bo
	 * @param oldShape
	 * @param newShape
	 */
	protected final void reconnect(EObject bo, Shape oldShape, Shape newShape) {
		List<Anchor> oldAnchors = oldShape.getAnchors();
		
		for (Anchor anchor : oldAnchors) {	
			fixIncomingConnections(anchor.getIncomingConnections(), newShape);
			fixOutgoingConnections(anchor.getOutgoingConnections(), newShape);
		}
	}

	protected void fixOutgoingConnections(EList<Connection> outgoingConnections, Shape newTarget) {
		fixConnections(outgoingConnections, newTarget, false);
	}

	protected void fixIncomingConnections(EList<Connection> incomingConnections, Shape newTarget) {
		fixConnections(incomingConnections, newTarget, true);
	}

	private void fixConnections(List<Connection> connections, Shape newTarget, boolean incoming) {
		
		// create a copy to avoid concurent modification excpetion
		List<Connection> connectionsCopy = new ArrayList<Connection>(connections);
		
		// iterate over connections
		for (Connection connection : connectionsCopy) {
			
			if (!isValidConnection(connection, newTarget, incoming)) {
				deleteConnection(connection);
			}
		}
	}
	
	protected boolean isValidConnection(Connection connection, Shape newTarget, boolean incoming) {

		CreateConnectionContext context = new CreateConnectionContext();
		context.setSourceAnchor(connection.getStart());
		context.setSourcePictogramElement(connection.getStart().getParent());
		context.setTargetAnchor(connection.getEnd());
		context.setTargetPictogramElement(connection.getEnd().getParent());
		
		ContextUtil.set(context, ConnectionOperations.CONNECTION_TYPE, BusinessObjectUtil.getBusinessObjectForPictogramElement(connection).eClass());
		
		CreateConnectionOperation operation = ConnectionOperations.getConnectionCreateOperation(context);
		
		return operation.canExecute(context);
	}

	protected final void removePictogramElement(PictogramElement pictogramElement) {
		IRemoveContext context = new RemoveContext(pictogramElement);
		IRemoveFeature removeFeature = getFeatureProvider().getRemoveFeature(context);
		removeFeature.remove(context);
	}

	protected void deleteConnection(Connection connection) {
		deletePictogramElement(connection);
	}
	
	protected final void deletePictogramElement(PictogramElement pictogramElement) {
		IDeleteContext context = new DeleteContext(pictogramElement);
		IDeleteFeature deleteFeature = getFeatureProvider().getDeleteFeature(context);
		deleteFeature.delete(context);		
	}
	
	protected ILabelProvider getLabelProvider() {
		if (labelProvider == null) {
			labelProvider = new LabelProvider();
		}
		return labelProvider;
	}
	
	protected class CreateLabelProvider extends LabelProvider {
		
		private Bpmn2FeatureProvider fp;

		public CreateLabelProvider(Bpmn2FeatureProvider fp) {
			this.fp = fp;
		}

		@Override
		public Image getImage(Object element) {
			if (!(element instanceof MorphOption)) {
				return null;
			}
			
			MorphOption option = (MorphOption) element;
			EClass cls = option.getNewType();
			
			// retrieve image via elements create feature
			// this is the default behavior for image retrieval
			IFeature createFeature = fp.getCreateFeatureForBusinessObject(cls);
			if (createFeature != null && createFeature instanceof AbstractBpmn2CreateFeature) {
				String imageId = ((AbstractBpmn2CreateFeature<?>) createFeature).getCreateImageId();
				return imageId != null ? getImageForId(imageId) : null;
			}
			
			return null;
		}		
	}
	
	protected class LabelProvider extends org.eclipse.jface.viewers.LabelProvider {
		
		@Override
		public String getText(Object arg0) {
			if (!(arg0 instanceof AbstractMorphNodeFeature.MorphOption)) {
				return "";
			}
			MorphOption option = (MorphOption) arg0;
			return option.getName();
		}
		
		protected Image getImageForId(String imageId) {
			return Images.getById(imageId);
		}
	}
	
	public static class MorphOption {
		
		private String name;
		private EClass newType;
		
		public MorphOption(String name, EClass newType) {
			this.name = name;
			this.newType = newType;
		}
		
		public String getName() {
			return name;
		}
		
		public EClass getNewType() {
			return newType;
		}
		
	}
}
