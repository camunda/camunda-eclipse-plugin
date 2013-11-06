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
package org.camunda.bpm.modeler.ui.diagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.camunda.bpm.modeler.core.features.activity.ActivitySelectionBehavior;
import org.camunda.bpm.modeler.core.features.api.IBpmn2AddFeature;
import org.camunda.bpm.modeler.core.features.api.IBpmn2CreateFeature;
import org.camunda.bpm.modeler.core.features.event.EventSelectionBehavior;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.core.validation.ValidationStatusAdapter;
import org.camunda.bpm.modeler.ui.FeatureMap;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.camunda.bpm.modeler.ui.diagram.editor.Bpmn2Editor;
import org.camunda.bpm.modeler.ui.features.choreography.ChoreographySelectionBehavior;
import org.camunda.bpm.modeler.ui.features.choreography.ChoreographyUtil;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.MultiInstanceLoopCharacteristics;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.IExecutionInfo;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.FeatureCheckerAdapter;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDirectEditingInfo;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.IFeatureAndContext;
import org.eclipse.graphiti.features.IFeatureChecker;
import org.eclipse.graphiti.features.IFeatureCheckerHolder;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IDoubleClickContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.features.context.impl.UpdateContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.impl.ConnectionCreationToolEntry;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.eclipse.graphiti.platform.IPlatformImageConstants;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.tb.ContextButtonEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.eclipse.graphiti.tb.IContextButtonPadData;
import org.eclipse.graphiti.tb.IDecorator;
import org.eclipse.graphiti.tb.IImageDecorator;
import org.eclipse.graphiti.tb.ImageDecorator;

public class Bpmn2ToolBehaviour extends DefaultToolBehaviorProvider implements IFeatureCheckerHolder {

	public Bpmn2ToolBehaviour(Bpmn2DiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
	}

	@Override
	protected Bpmn2FeatureProvider getFeatureProvider() {
		return (Bpmn2FeatureProvider) super.getFeatureProvider();
	}
	
	@Override
	public IPaletteCompartmentEntry[] getPalette() {

		Bpmn2Editor editor = (Bpmn2Editor) getDiagramTypeProvider().getDiagramEditor();
		Diagram diagram = getDiagramTypeProvider().getDiagram();
		Object object = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(diagram);

		List<IPaletteCompartmentEntry> palette = new ArrayList<IPaletteCompartmentEntry>();

		if (object != null) {
			// add compartments from super class
			createConnectors(palette);
			createTasksCompartments(palette);
			createGatewaysCompartments(palette);
			createEventsCompartments(palette);
			createEventDefinitionsCompartments(palette);
			createDataCompartments(palette);
			createOtherCompartments(palette);
		}

		return palette.toArray(new IPaletteCompartmentEntry[palette.size()]);
	}

	private void createEventsCompartments(List<IPaletteCompartmentEntry> palette) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(
				"Events", null);

		createEntriesForBusinessObjectClasses(FeatureMap.EVENTS, compartmentEntry);

		if (compartmentEntry.getToolEntries().size() > 0) {
			palette.add(compartmentEntry);
		}
	}

	private void createOtherCompartments(List<IPaletteCompartmentEntry> palette) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(
				"Other", null);
		compartmentEntry.setInitiallyOpen(false);

		createEntriesForBusinessObjectClasses(FeatureMap.OTHER, compartmentEntry);

		if (compartmentEntry.getToolEntries().size() > 0) {
			palette.add(compartmentEntry);
		}
	}

	private void createDataCompartments(List<IPaletteCompartmentEntry> palette) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(
				"Data Items", null);
		compartmentEntry.setInitiallyOpen(false);

		createEntriesForBusinessObjectClasses(FeatureMap.DATA, compartmentEntry);

		if (compartmentEntry.getToolEntries().size() > 0) {
			palette.add(compartmentEntry);
		}
	}

	private void createEventDefinitionsCompartments(
			List<IPaletteCompartmentEntry> palette) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(
				"Event Definitions", null);
		compartmentEntry.setInitiallyOpen(false);

		createEntriesForBusinessObjectClasses(FeatureMap.EVENT_DEFINITIONS, compartmentEntry);

		if (compartmentEntry.getToolEntries().size() > 0) {
			palette.add(compartmentEntry);
		}
	}

	private void createGatewaysCompartments(
			List<IPaletteCompartmentEntry> palette) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(
				"Gateways", null);

		createEntriesForBusinessObjectClasses(FeatureMap.GATEWAYS, compartmentEntry);

		if (compartmentEntry.getToolEntries().size() > 0) {
			palette.add(compartmentEntry);
		}
	}

	private void createTasksCompartments(List<IPaletteCompartmentEntry> palette) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry("Tasks", null);

		createEntriesForBusinessObjectClasses(FeatureMap.TASKS, compartmentEntry);

		createCustomEntries(getFeatureProvider().getCustomCreateFeatures(), compartmentEntry);
		
		if (compartmentEntry.getToolEntries().size() > 0) {
			palette.add(compartmentEntry);
		}
	}

	private void createConnectors(List<IPaletteCompartmentEntry> palette) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(
				"Connectors", null);

		createEntriesForBusinessObjectClasses(FeatureMap.CONNECTORS, compartmentEntry);

		if (compartmentEntry.getToolEntries().size() > 0) {
			palette.add(compartmentEntry);
		}
	}

	private void createCustomEntries(List<IFeature> features, PaletteCompartmentEntry compartmentEntry) {
		for (IFeature feature : features) {
			createEntry(feature, compartmentEntry);
		}
	}
	
	private void createEntriesForBusinessObjectClasses(List<EClass> classes, PaletteCompartmentEntry compartmentEntry) {
		
		for (EClass eClass: classes) {
			IFeature feature = getFeatureProvider().getCreateFeatureForBusinessObject(eClass);
			createEntry(feature, compartmentEntry);
		}
	}

	private void createEntry(IFeature feature, PaletteCompartmentEntry compartmentEntry) {
		if (feature instanceof ICreateFeature) {
			ICreateFeature cf = (ICreateFeature) feature;
			
			ObjectCreationToolEntry objectCreationToolEntry = new ObjectCreationToolEntry(
					cf.getCreateName(), cf.getCreateDescription(),
					cf.getCreateImageId(), cf.getCreateLargeImageId(), cf);
			
			compartmentEntry.addToolEntry(objectCreationToolEntry);
			
		} else if (feature instanceof ICreateConnectionFeature) {
			ICreateConnectionFeature cf = (ICreateConnectionFeature) feature;
			
			ConnectionCreationToolEntry connectionCreationToolEntry = new ConnectionCreationToolEntry(
					cf.getCreateName(), cf.getCreateDescription(),
					cf.getCreateImageId(), cf.getCreateLargeImageId());
			
			connectionCreationToolEntry.addCreateConnectionFeature(cf);
			compartmentEntry.addToolEntry(connectionCreationToolEntry);
		}
	}

	@Override
	public IFeatureChecker getFeatureChecker() {
		return new FeatureCheckerAdapter(false) {
			@Override
			public boolean allowAdd(IContext context) {
				return super.allowAdd(context);
			}

			@Override
			public boolean allowCreate() {
				return super.allowCreate();
			}
		};
	}
	
	@Override
	public boolean equalsBusinessObjects(Object o1, Object o2) {
		/** multiInstanceLoopCharacteristics is an extension to an activity
		  * e.g.
		  * <bpmn2:serviceTask id="ServiceTask_1" camunda:class="" name="Service Task">
		  * 	<bpmn2:multiInstanceLoopCharacteristics />
		  * </bpmn2:serviceTask>
		  * therefore it is necessary to compare the loopCharacteristics attribute value
		  * from the service task with the other object
		  *
		  */
		if (o1 instanceof MultiInstanceLoopCharacteristics && o2 instanceof Activity) {
			if (o1.equals(((Activity) o2).getLoopCharacteristics())) {
				return true;
			}
		}

		return super.equalsBusinessObjects(o1, o2);
	}

	@Override
	public GraphicsAlgorithm[] getClickArea(PictogramElement pe) {
		if (ActivitySelectionBehavior.canApplyTo(pe)) {
			return ActivitySelectionBehavior.getClickArea(pe);
		} else if (EventSelectionBehavior.canApplyTo(pe)) {
			return EventSelectionBehavior.getClickArea(pe);
		} else if (ChoreographySelectionBehavior.canApplyTo(pe)) {
			return ChoreographySelectionBehavior.getClickArea(pe);
		}
		return super.getClickArea(pe);
	}

	@Override
	public GraphicsAlgorithm getSelectionBorder(PictogramElement pe) {
		if (ActivitySelectionBehavior.canApplyTo(pe)) {
			return ActivitySelectionBehavior.getSelectionBorder(pe);
		} else if (EventSelectionBehavior.canApplyTo(pe)) {
			return EventSelectionBehavior.getSelectionBorder(pe);
		} else if (ChoreographySelectionBehavior.canApplyTo(pe)) {
			return ChoreographySelectionBehavior.getSelectionBorder(pe);
		} else if (pe instanceof ContainerShape) {
			if (((ContainerShape) pe).getChildren().size() > 0) {
				GraphicsAlgorithm ga = ((ContainerShape) pe).getChildren()
						.get(0).getGraphicsAlgorithm();
				if (!(ga instanceof Text) && !(ga instanceof Polyline))
					return ga;
				ga = ((ContainerShape) pe).getGraphicsAlgorithm();
				if (ga.getGraphicsAlgorithmChildren().size() > 0)
					return ga.getGraphicsAlgorithmChildren().get(0);
				return ga;
			}
		} else if (pe instanceof Shape) {
			return ((Shape) pe).getGraphicsAlgorithm();
		}
		return super.getSelectionBorder(pe);
	}

	@Override
	public IContextButtonPadData getContextButtonPad(IPictogramElementContext context) {
		
		IContextButtonPadData data = super.getContextButtonPad(context);
		PictogramElement pe = context.getPictogramElement();
		IFeatureProvider fp = getFeatureProvider();

		if (LabelUtil.isLabel(pe)) {
			// labels don't have a buttonpad
			setGenericContextButtons(data, pe, 0);
			return data;
		}

		if (pe.getGraphicsAlgorithm() != null
				&& pe.getGraphicsAlgorithm().getWidth() < 40) {
			ILocation origin = getAbsoluteLocation(pe.getGraphicsAlgorithm());
			data.getPadLocation().setRectangle(origin.getX(), origin.getY(),
					40, 40);
		}

		// 1. set the generic context buttons
		// Participant bands can only be removed from the choreograpy task
		int genericButtons = CONTEXT_BUTTON_DELETE;
		if (ChoreographyUtil.isChoreographyParticipantBand(pe)) {
			genericButtons |= CONTEXT_BUTTON_REMOVE;
		}
		setGenericContextButtons(data, pe, genericButtons);

		// 2. set the expand & collapse buttons
		CustomContext cc = new CustomContext(new PictogramElement[] { pe });
		ICustomFeature[] cf = fp.getCustomFeatures(cc);
		for (int i = 0; i < cf.length; i++) {
			ICustomFeature iCustomFeature = cf[i];
			if (iCustomFeature != null && iCustomFeature.isAvailable(cc) && iCustomFeature.canExecute(cc)) {
				ContextButtonEntry button = new ContextButtonEntry(iCustomFeature, cc);
				button.setText(iCustomFeature.getName()); //$NON-NLS-1$
				button.setIconId(iCustomFeature.getImageId());
				button.setDescription(iCustomFeature.getDescription());

				data.getDomainSpecificContextButtons().add(button);
			}
		}

		// 3. add one domain specific context-button, which offers all
		// available connection-features as drag&drop features...

		// 3.a. create new CreateConnectionContext
		CreateConnectionContext ccc = new CreateConnectionContext();
		ccc.setSourcePictogramElement(pe);
		Anchor anchor = null;
		if (pe instanceof Anchor) {
			anchor = (Anchor) pe;
		} else if (pe instanceof AnchorContainer) {
			// assume, that our shapes always have chopbox anchors
			anchor = Graphiti.getPeService().getChopboxAnchor((AnchorContainer) pe);
		}
		ccc.setSourceAnchor(anchor);

		// 3.b. create context button and add "Create Connections" feature
		ICreateConnectionFeature[] features = fp.getCreateConnectionFeatures();
		ContextButtonEntry button = new ContextButtonEntry(null, context);
		button.setText("Create Connection"); //$NON-NLS-1$
		String description = null;
		ArrayList<String> names = new ArrayList<String>();
		button.setIconId(ImageProvider.IMG_16_SEQUENCE_FLOW);
		for (ICreateConnectionFeature feature : features) {
			if (feature.isAvailable(ccc) && feature.canStartConnection(ccc)) {
				button.addDragAndDropFeature(feature);
				names.add(feature.getCreateName());
			}
		}

		// 3.c. build a reasonable description for the context button action
		for (int i = 0; i < names.size(); ++i) {
			if (description == null)
				description = "Click and drag to create a\n";
			description += names.get(i);
			if (i + 2 == names.size())
				description += " or ";
			else if (i + 1 < names.size())
				description += ", ";
		}
		button.setDescription(description);

		// 3.d. add context button, button only if it contains at least one
		// feature
		if (button.getDragAndDropFeatures().size() > 0) {
			data.getDomainSpecificContextButtons().add(button);
		}

		return data;
	}

	@Override
	public void postExecute(IExecutionInfo executionInfo) {
		Bpmn2Editor editor = (Bpmn2Editor) getDiagramTypeProvider().getDiagramEditor();
		for (IFeatureAndContext fc : executionInfo.getExecutionList()) {
			IContext context = fc.getContext();
			IFeature feature = fc.getFeature();
			if (context instanceof AddContext) {
				if (feature instanceof IBpmn2AddFeature) {
					((IBpmn2AddFeature) feature).postExecute(executionInfo);
				}
			} else if (context instanceof CreateContext) {
				if (feature instanceof IBpmn2CreateFeature) {
					((IBpmn2CreateFeature) feature).postExecute(executionInfo);
				}
			} else if (context instanceof UpdateContext) {
				editor.setPictogramElementForSelection(((UpdateContext) context)
						.getPictogramElement());
				editor.refresh();
			}
		}
	}

	@Override
	public IDecorator[] getDecorators(PictogramElement pe) {
		List<IDecorator> decorators = new ArrayList<IDecorator>();

		if (LabelUtil.isLabel(pe)) {
			return new IDecorator[0];
		}

		IFeatureProvider featureProvider = getFeatureProvider();
		Object bo = featureProvider.getBusinessObjectForPictogramElement(pe);
		if (bo != null) {
			ValidationStatusAdapter statusAdapter = (ValidationStatusAdapter) EcoreUtil
					.getRegisteredAdapter((EObject) bo,
							ValidationStatusAdapter.class);
			if (statusAdapter != null) {
				final IImageDecorator decorator;
				final IStatus status = statusAdapter.getValidationStatus();
				switch (status.getSeverity()) {
				case IStatus.INFO:
					decorator = new ImageDecorator(
							IPlatformImageConstants.IMG_ECLIPSE_INFORMATION_TSK);
					break;
				case IStatus.WARNING:
					decorator = new ImageDecorator(
							IPlatformImageConstants.IMG_ECLIPSE_WARNING_TSK);
					break;
				case IStatus.ERROR:
					decorator = new ImageDecorator(
							IPlatformImageConstants.IMG_ECLIPSE_ERROR_TSK);
					break;
				default:
					decorator = null;
					break;
				}
				if (decorator != null) {
					GraphicsAlgorithm ga = getSelectionBorder(pe);
					if (ga == null) {
						ga = pe.getGraphicsAlgorithm();
					}
					decorator.setX(-5);
					decorator.setY(-5);
					decorator.setMessage(status.getMessage());
					decorators.add(decorator);
				}
			}
		}

		return decorators.toArray(new IDecorator[0]);
	}

	@Override
	public ICustomFeature getDoubleClickFeature(IDoubleClickContext context) {
		return new AbstractCustomFeature(getFeatureProvider()) {

			@Override
			public boolean canExecute(ICustomContext context) {
				return true;
			}

			@Override
			public void execute(ICustomContext customContext) {
				if (Arrays.asList(customContext.getPictogramElements()).isEmpty()) {
					return;
				}
				
				PictogramElement contextPe = customContext.getPictogramElements()[0];
				
				ContainerShape labelShape = LabelUtil.getLabelShape(contextPe, getDiagram());
				
				PictogramElement pe = (labelShape == null) ? contextPe : labelShape;
				
				IDirectEditingInfo directEditingInfo = getFeatureProvider()
						.getDirectEditingInfo();
				
				directEditingInfo.setMainPictogramElement(pe);
				directEditingInfo.setPictogramElement(pe);

				directEditingInfo
						.setGraphicsAlgorithm(labelShape != null ? labelShape.getGraphicsAlgorithm() : customContext.getInnerGraphicsAlgorithm());
				
				directEditingInfo.setActive(true);
				getDiagramEditor().refresh();
			}
		};
	}
}