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
package org.eclipse.bpmn2.modeler.ui.diagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.features.activity.ActivitySelectionBehavior;
import org.eclipse.bpmn2.modeler.core.features.event.EventSelectionBehavior;
//import org.eclipse.bpmn2.modeler.core.preferences.ModelEnablementDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.CustomTaskDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.ModelEnablementDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.modeler.ui.FeatureMap;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.CustomTaskFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.choreography.ChoreographySelectionBehavior;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.FeatureCheckerAdapter;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.IFeatureChecker;
import org.eclipse.graphiti.features.IFeatureCheckerHolder;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.impl.ConnectionCreationToolEntry;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.tb.ContextButtonEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.eclipse.graphiti.tb.IContextButtonPadData;

public class BpmnToolBehaviourFeature extends DefaultToolBehaviorProvider implements IFeatureCheckerHolder {

	public BpmnToolBehaviourFeature(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
	}

	@Override
	public IPaletteCompartmentEntry[] getPalette() {

		EList<Resource> resources = getDiagramTypeProvider().getDiagram().eResource().getResourceSet().getResources();
		IProject project = null;
		for (Resource resource : resources) {
			if (resource.getURI().segmentCount() > 1) {
				String projectName = resource.getURI().segment(1);
				project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
				if (project != null) {
					break;
				}
			}
		}

		BPMN2Editor editor = (BPMN2Editor)getDiagramTypeProvider().getDiagramEditor();
		Diagram diagram = getDiagramTypeProvider().getDiagram();
		Object object = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(diagram);
		ModelEnablementDescriptor med = editor.getTargetRuntime().getModelEnablements((EObject)object);
		
		List<IPaletteCompartmentEntry> ret = new ArrayList<IPaletteCompartmentEntry>();

		// add compartments from super class

		IFeatureProvider featureProvider = getFeatureProvider();

		createConnectors(med, ret, featureProvider);

		createTasksCompartments(med, ret, featureProvider);
		createGatewaysCompartments(med, ret, featureProvider);
		createEventsCompartments(med, ret, featureProvider);
		createEventDefinitionsCompartments(med, ret, featureProvider);
		createDataCompartments(med, ret, featureProvider);
		createOtherCompartments(med, ret, featureProvider);

		createCustomTasks(med, ret, featureProvider);

		return ret.toArray(new IPaletteCompartmentEntry[ret.size()]);
	}

	private void createEventsCompartments(ModelEnablementDescriptor med, List<IPaletteCompartmentEntry> ret,
			IFeatureProvider featureProvider) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry("Events", null);
		ret.add(compartmentEntry);

		createEntries(med, FeatureMap.EVENTS, compartmentEntry, featureProvider);
	}

	private void createOtherCompartments(ModelEnablementDescriptor med, List<IPaletteCompartmentEntry> ret,
			IFeatureProvider featureProvider) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry("Other", null);
		compartmentEntry.setInitiallyOpen(false);
		ret.add(compartmentEntry);

		createEntries(med, FeatureMap.OTHER, compartmentEntry, featureProvider);

	}

	private void createDataCompartments(ModelEnablementDescriptor med, List<IPaletteCompartmentEntry> ret,
			IFeatureProvider featureProvider) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry("Data Items", null);
		compartmentEntry.setInitiallyOpen(false);
		ret.add(compartmentEntry);

		createEntries(med, FeatureMap.DATA, compartmentEntry, featureProvider);

	}

	private void createEventDefinitionsCompartments(ModelEnablementDescriptor med, List<IPaletteCompartmentEntry> ret,
			IFeatureProvider featureProvider) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry("Event Definitions", null);
		compartmentEntry.setInitiallyOpen(false);
		ret.add(compartmentEntry);

		createEntries(med, FeatureMap.EVENT_DEFINITIONS, compartmentEntry, featureProvider);

	}

	private void createGatewaysCompartments(ModelEnablementDescriptor med, List<IPaletteCompartmentEntry> ret,
			IFeatureProvider featureProvider) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry("Gateways", null);
		ret.add(compartmentEntry);

		createEntries(med, FeatureMap.GATEWAYS, compartmentEntry, featureProvider);

	}

	private void createTasksCompartments(ModelEnablementDescriptor med, List<IPaletteCompartmentEntry> ret,
			IFeatureProvider featureProvider) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry("Tasks", null);
		ret.add(compartmentEntry);

		createEntries(med, FeatureMap.TASKS, compartmentEntry, featureProvider);

	}

	private void createConnectors(ModelEnablementDescriptor med, List<IPaletteCompartmentEntry> ret,
			IFeatureProvider featureProvider) {
		PaletteCompartmentEntry compartmentEntry;
		compartmentEntry = new PaletteCompartmentEntry("Connectors", null);
		ret.add(compartmentEntry);
		// add all create-connection-features to the new stack-entry
		ICreateConnectionFeature[] createConnectionFeatures = featureProvider.getCreateConnectionFeatures();
		for (ICreateConnectionFeature cf : createConnectionFeatures) {
			if (med.isEnabled(FeatureMap.getElement(cf))) {
				ConnectionCreationToolEntry connectionCreationToolEntry = new ConnectionCreationToolEntry(
						cf.getCreateName(), cf.getCreateDescription(), cf.getCreateImageId(),
						cf.getCreateLargeImageId());
				connectionCreationToolEntry.addCreateConnectionFeature(cf);
				compartmentEntry.addToolEntry(connectionCreationToolEntry);
			}
		}
	}

	private void createEntries(ModelEnablementDescriptor med, List<Class<? extends IFeature>> neededEntries,
			PaletteCompartmentEntry compartmentEntry, IFeatureProvider featureProvider) {
		List<ICreateFeature> tools = Arrays.asList(featureProvider.getCreateFeatures());

		for (ICreateFeature cf : tools) {
			EClass feature = FeatureMap.getElement(cf);
			if (feature==null)
				continue;
			if (med.isEnabled(feature) && neededEntries.contains(cf.getClass())) {
				ObjectCreationToolEntry objectCreationToolEntry = new ObjectCreationToolEntry(cf.getCreateName(),
						cf.getCreateDescription(), cf.getCreateImageId(), cf.getCreateLargeImageId(), cf);
				compartmentEntry.addToolEntry(objectCreationToolEntry);
			}
		}
	}

	private void createCustomTasks(ModelEnablementDescriptor med, List<IPaletteCompartmentEntry> ret, IFeatureProvider featureProvider) {
		PaletteCompartmentEntry compartmentEntry = null;
		BPMN2Editor editor = (BPMN2Editor) getDiagramTypeProvider().getDiagramEditor();
		TargetRuntime rt = editor.getTargetRuntime();
		
		try {
			for (CustomTaskDescriptor tc : rt.getCustomTasks()) {
				if (true) {//med.isEnabled(tc.getId())) {
					CustomTaskFeatureContainer container = (CustomTaskFeatureContainer)tc.getFeatureContainer();
	
					container.setId(featureProvider, tc.getId());
					ICreateFeature cf = container.getCreateFeature(featureProvider);
					ObjectCreationToolEntry objectCreationToolEntry = new ObjectCreationToolEntry(tc.getName(),
							cf.getCreateDescription(), cf.getCreateImageId(), cf.getCreateLargeImageId(), cf);
					
					if (compartmentEntry==null) {
						compartmentEntry = new PaletteCompartmentEntry("Custom Task", null);
						compartmentEntry.setInitiallyOpen(false);
						ret.add(compartmentEntry);
					}
					
					compartmentEntry.addToolEntry(objectCreationToolEntry);
				}
			}
		} catch (Exception ex) {
			Activator.logError(ex);
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
		}
		return super.getSelectionBorder(pe);
	}

	@Override
	public IContextButtonPadData getContextButtonPad(IPictogramElementContext context) {
		IContextButtonPadData data = super.getContextButtonPad(context);
		PictogramElement pe = context.getPictogramElement();

		// 1. set the generic context buttons
		// note, that we do not add 'remove' (just as an example)
//		setGenericContextButtons(data, pe, CONTEXT_BUTTON_DELETE | CONTEXT_BUTTON_UPDATE);

		// 2. set the expand & collapse buttons
		CustomContext cc = new CustomContext(new PictogramElement[] { pe });
		ICustomFeature[] cf = getFeatureProvider().getCustomFeatures(cc);
		for (int i = 0; i < cf.length; i++) {
			ICustomFeature iCustomFeature = cf[i];
			ContextButtonEntry button = new ContextButtonEntry(iCustomFeature, cc);
			button.setText(iCustomFeature.getName()); //$NON-NLS-1$
			button.setIconId(iCustomFeature.getImageId());
			button.setDescription(iCustomFeature.getDescription());
			
			data.getDomainSpecificContextButtons().add(button);
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
		ICreateConnectionFeature[] features = getFeatureProvider().getCreateConnectionFeatures();
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
		for (int i=0; i<names.size(); ++i) {
			if (description==null)
				description = "Click and drag to create a\n";
			description += names.get(i);
			if (i+2 == names.size())
				description += " or ";
			else if (i+1 < names.size())
				description += ", ";
		}
		button.setDescription(description);

		// 3.d. add context button, button only if it contains at least one feature
		if (button.getDragAndDropFeatures().size() > 0) {
			data.getDomainSpecificContextButtons().add(button);
		}

		return data;
	}

}