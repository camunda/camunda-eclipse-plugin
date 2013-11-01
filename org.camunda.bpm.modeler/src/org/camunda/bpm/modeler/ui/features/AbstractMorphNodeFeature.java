package org.camunda.bpm.modeler.ui.features;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.camunda.bpm.modeler.core.layout.util.ConnectionUtil;
import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.core.utils.transform.Transformer;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IAddFeature;
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
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.graphiti.ui.services.IImageService;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
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
		return ImageProvider.IMG_16_CONFIGURE;
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
		
		Shape newShape = createNewShape(option, oldShape, newObject);
		
		reconnect(newObject, oldShape, newShape);
		
		cleanUp(oldShape);
		changed = true;
	}
	
	public abstract EClass getBusinessObjectClass();
	
	public List<MorphOption> getOptions(EObject bo) {
		EClass newType = getBusinessObjectClass();
		
		List<MorphOption> availableTypes = new ArrayList<MorphOption>();
		
		List<EClassifier> classifiers = Bpmn2Package.eINSTANCE.getEClassifiers();
		
		for (EClassifier classifier : classifiers) {
			
			if (classifier instanceof EClass) {
				EClass type = (EClass) classifier;

				if (type.isAbstract() || bo.eClass().equals(type) || excludeTypes(bo).contains(type)) {
					continue;
				}
				
				List<EClass> superTypes = type.getEAllSuperTypes();
				
				if (superTypes.contains(newType)) {
					String name = ModelUtil.toDisplayName(type.getName());
					MorphOption option = new MorphOption(name, type);
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
	
	protected Shape createNewShape(MorphOption option, Shape oldShape, T newObject) {
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
		addContext.putProperty(AbstractMorphNodeFeature.MORPH_ELEMENT, true);
		
		IAddFeature addFeature = getFeatureProvider().getAddFeature(addContext);
		return (Shape) addFeature.add(addContext);
	}

	protected void cleanUp(PictogramElement pictogramElement) {
		removePictogramElement(pictogramElement);
	}
	
	protected final void reconnect(EObject bo, Shape oldShape, Shape newShape) {
		List<Anchor> oldAnchors = oldShape.getAnchors();
		List<Anchor> newAnchors = newShape.getAnchors();
		
		for (int i = 0; i < oldAnchors.size(); i++) {
			
			Anchor oldAnchor = oldAnchors.get(i);
			Anchor newAnchor = newAnchors.get(i);
			
			List<Connection> oldIncomingConnections = oldAnchor.getIncomingConnections();
			List<Connection> newIncomingConnections = newAnchor.getIncomingConnections();

			swapConnections(bo, oldIncomingConnections, newIncomingConnections, true);
			
			List<Connection> oldOutgoingConnections = oldAnchor.getOutgoingConnections();
			List<Connection> newOutgoingConnections = newAnchor.getOutgoingConnections();
			
			swapConnections(bo, oldOutgoingConnections, newOutgoingConnections, false);
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
	
	protected void handleDataAssociation(EObject bo, Connection dataAssociation, List<Connection> connections, boolean incoming) {
		handleConnection(dataAssociation, connections);
	}
	
	protected final void handleConnection(Connection connection, List<Connection> connections) {
		if (!connections.contains(connection)) {
			connections.add(connection);
		}
	}
	
	protected final void removePictogramElement(PictogramElement pictogramElement) {
		IRemoveContext context = new RemoveContext(pictogramElement);
		IRemoveFeature removeFeature = getFeatureProvider().getRemoveFeature(context);
		removeFeature.remove(context);
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
	
	protected class LabelProvider implements ILabelProvider {
		
		private IImageService imageService;
		
		public LabelProvider() {
			imageService = GraphitiUi.getImageService();
		}

		@Override
		public void addListener(ILabelProviderListener arg0) {}

		@Override
		public void dispose() {}

		@Override
		public boolean isLabelProperty(Object arg0, String arg1) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener arg0) {}

		@Override
		public Image getImage(Object arg0) {
			return null;
		}

		@Override
		public String getText(Object arg0) {
			if (!(arg0 instanceof AbstractMorphNodeFeature.MorphOption)) {
				return "";
			}
			MorphOption option = (MorphOption) arg0;
			return option.getName();
		}
		
		
		protected Image getImageForId(String imageId) {
			return imageService.getImageForId(imageId);
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
