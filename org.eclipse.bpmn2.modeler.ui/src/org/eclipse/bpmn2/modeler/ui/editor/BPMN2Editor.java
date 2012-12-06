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
package org.eclipse.bpmn2.modeler.ui.editor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Assignment;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.CallChoreography;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.Category;
import org.eclipse.bpmn2.CompensateEventDefinition;
import org.eclipse.bpmn2.ConditionalEventDefinition;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.DataObjectReference;
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.bpmn2.DataStore;
import org.eclipse.bpmn2.DataStoreReference;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.ErrorEventDefinition;
import org.eclipse.bpmn2.EscalationEventDefinition;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.Expression;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.GlobalBusinessRuleTask;
import org.eclipse.bpmn2.GlobalManualTask;
import org.eclipse.bpmn2.GlobalScriptTask;
import org.eclipse.bpmn2.GlobalTask;
import org.eclipse.bpmn2.GlobalUserTask;
import org.eclipse.bpmn2.HumanPerformer;
import org.eclipse.bpmn2.Import;
import org.eclipse.bpmn2.InputOutputSpecification;
import org.eclipse.bpmn2.Interface;
import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.LinkEventDefinition;
import org.eclipse.bpmn2.ManualTask;
import org.eclipse.bpmn2.Message;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.MultiInstanceLoopCharacteristics;
import org.eclipse.bpmn2.Performer;
import org.eclipse.bpmn2.PotentialOwner;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.Property;
import org.eclipse.bpmn2.ResourceAssignmentExpression;
import org.eclipse.bpmn2.ResourceParameterBinding;
import org.eclipse.bpmn2.ResourceRole;
import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.SignalEventDefinition;
import org.eclipse.bpmn2.StandardLoopCharacteristics;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.Bpmn2TabbedPropertySheetPage;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.ProxyURIConverterImplExtension;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.importer.ImportException;
import org.eclipse.bpmn2.modeler.core.importer.ModelImportCommand;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDialogComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultListComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerResourceImpl;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.DiagramEditorAdapter;
import org.eclipse.bpmn2.modeler.core.utils.ErrorUtils;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.core.validation.BPMN2ProjectValidator;
import org.eclipse.bpmn2.modeler.core.validation.BPMN2ValidationStatusLoader;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.dialog.importer.ModelProblemsDialog;
import org.eclipse.bpmn2.modeler.ui.property.artifact.CategoryDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.artifact.TextAnnotationDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.connectors.MessageFlowDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.connectors.SequenceFlowDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.data.ConditionalEventDefinitionDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.data.DataAssignmentDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.data.DataObjectPropertySection.DataObjectDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.data.DataObjectReferencePropertySection.DataObjectReferenceDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.data.DataStorePropertySection.DataStoreDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.data.DataStoreReferencePropertySection.DataStoreReferenceDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.data.ExpressionDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.data.InterfaceDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.data.ItemAwareElementDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.data.MessageDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.data.MessageListComposite;
import org.eclipse.bpmn2.modeler.ui.property.data.ResourceAssignmentExpressionDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.data.ResourceParameterBindingDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.data.ResourceRoleDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.DefinitionsPropertyComposite.ImportDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.ItemDefinitionDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.ItemDefinitionListComposite;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.ProcessDiagramPropertyComposite;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.PropertyListComposite;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.ResourceRoleListComposite;
import org.eclipse.bpmn2.modeler.ui.property.events.BoundaryEventDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.events.CatchEventDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.events.CommonEventDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.events.CommonEventPropertySection.EventDefinitionDialogComposite;
import org.eclipse.bpmn2.modeler.ui.property.events.EndEventDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.events.StartEventDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.events.ThrowEventDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.events.TimerEventDefinitionDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.gateways.GatewayDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.tasks.ActivityDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.tasks.ActivityInputDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.tasks.ActivityOutputDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.tasks.DataAssociationDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.tasks.IoParametersDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.tasks.ManualTaskDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.tasks.MultiInstanceLoopCharacteristicsDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.tasks.ScriptTaskDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.tasks.StandardLoopCharacteristicsDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.tasks.TaskDetailComposite;
import org.eclipse.bpmn2.modeler.ui.views.outline.BPMN2EditorOutlinePage;
import org.eclipse.bpmn2.modeler.ui.wizards.BPMN2DiagramCreator;
import org.eclipse.bpmn2.modeler.ui.wizards.Bpmn2DiagramEditorInput;
import org.eclipse.bpmn2.modeler.ui.wizards.FileService;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain.Lifecycle;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.platform.IDiagramEditor;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.ui.editor.DefaultPersistencyBehavior;
import org.eclipse.graphiti.ui.editor.DefaultUpdateBehavior;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.graphiti.ui.internal.editor.GFPaletteRoot;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptorProvider;

/**
 * 
 */
@SuppressWarnings("restriction")
public class BPMN2Editor extends DiagramEditor implements IPropertyChangeListener, IGotoMarker {

	// Register the Default List and Detail Composites as fallback for rendering EObject
	static {
		PropertiesCompositeFactory.register(EObject.class, DefaultDetailComposite.class);
		PropertiesCompositeFactory.register(EObject.class, DefaultListComposite.class);
		PropertiesCompositeFactory.register(EObject.class, DefaultDialogComposite.class);
		PropertiesCompositeFactory.register(Message.class, MessageDetailComposite.class);
		PropertiesCompositeFactory.register(Message.class, MessageListComposite.class);
		PropertiesCompositeFactory.register(MessageFlow.class, MessageFlowDetailComposite.class);
		PropertiesCompositeFactory.register(Property.class, ItemAwareElementDetailComposite.class);
		PropertiesCompositeFactory.register(CallActivity.class, ActivityDetailComposite.class);
		PropertiesCompositeFactory.register(GlobalTask.class, ActivityDetailComposite.class);
		PropertiesCompositeFactory.register(GlobalBusinessRuleTask.class, ActivityDetailComposite.class);
		PropertiesCompositeFactory.register(GlobalManualTask.class, ActivityDetailComposite.class);
		PropertiesCompositeFactory.register(GlobalScriptTask.class, ActivityDetailComposite.class);
		PropertiesCompositeFactory.register(GlobalUserTask.class, ActivityDetailComposite.class);
		PropertiesCompositeFactory.register(Import.class, ImportDetailComposite.class);
		PropertiesCompositeFactory.register(Category.class, CategoryDetailComposite.class);
		PropertiesCompositeFactory.register(TextAnnotation.class, TextAnnotationDetailComposite.class);
		PropertiesCompositeFactory.register(SequenceFlow.class, SequenceFlowDetailComposite.class);
		PropertiesCompositeFactory.register(DataObject.class, DataObjectDetailComposite.class);
		PropertiesCompositeFactory.register(DataObjectReference.class, DataObjectDetailComposite.class);
		PropertiesCompositeFactory.register(Assignment.class, DataAssignmentDetailComposite.class);
		PropertiesCompositeFactory.register(Expression.class, ExpressionDetailComposite.class);
		PropertiesCompositeFactory.register(FormalExpression.class, ExpressionDetailComposite.class);
		PropertiesCompositeFactory.register(ResourceAssignmentExpression.class, ResourceAssignmentExpressionDetailComposite.class);
		PropertiesCompositeFactory.register(ResourceParameterBinding.class, ResourceParameterBindingDetailComposite.class);
		PropertiesCompositeFactory.register(PotentialOwner.class, ResourceRoleDetailComposite.class);
		PropertiesCompositeFactory.register(HumanPerformer.class, ResourceRoleDetailComposite.class);
		PropertiesCompositeFactory.register(Performer.class, ResourceRoleDetailComposite.class);
		PropertiesCompositeFactory.register(DataObjectReference.class, DataObjectReferenceDetailComposite.class);
		PropertiesCompositeFactory.register(DataStore.class, DataStoreDetailComposite.class);
		PropertiesCompositeFactory.register(DataStoreReference.class, DataStoreReferenceDetailComposite.class);
		PropertiesCompositeFactory.register(Interface.class, InterfaceDetailComposite.class);
		PropertiesCompositeFactory.register(ItemDefinition.class, ItemDefinitionDetailComposite.class);
		PropertiesCompositeFactory.register(ItemDefinition.class, ItemDefinitionListComposite.class);
		PropertiesCompositeFactory.register(Property.class, PropertyListComposite.class);
		PropertiesCompositeFactory.register(ResourceRole.class, ResourceRoleListComposite.class);
		PropertiesCompositeFactory.register(Event.class, CommonEventDetailComposite.class);
		PropertiesCompositeFactory.register(StartEvent.class, StartEventDetailComposite.class);
		PropertiesCompositeFactory.register(EndEvent.class, EndEventDetailComposite.class);
		PropertiesCompositeFactory.register(CatchEvent.class, CatchEventDetailComposite.class);
		PropertiesCompositeFactory.register(ThrowEvent.class, ThrowEventDetailComposite.class);
		PropertiesCompositeFactory.register(BoundaryEvent.class, BoundaryEventDetailComposite.class);
		PropertiesCompositeFactory.register(TimerEventDefinition.class, TimerEventDefinitionDetailComposite.class);
		PropertiesCompositeFactory.register(ConditionalEventDefinition.class, ConditionalEventDefinitionDetailComposite.class);
		PropertiesCompositeFactory.register(CompensateEventDefinition.class, EventDefinitionDialogComposite.class);
		PropertiesCompositeFactory.register(ConditionalEventDefinition.class, EventDefinitionDialogComposite.class);
		PropertiesCompositeFactory.register(ErrorEventDefinition.class, EventDefinitionDialogComposite.class);
		PropertiesCompositeFactory.register(EscalationEventDefinition.class, EventDefinitionDialogComposite.class);
		PropertiesCompositeFactory.register(LinkEventDefinition.class, EventDefinitionDialogComposite.class);
		PropertiesCompositeFactory.register(MessageEventDefinition.class, EventDefinitionDialogComposite.class);
		PropertiesCompositeFactory.register(SignalEventDefinition.class, EventDefinitionDialogComposite.class);
		PropertiesCompositeFactory.register(TimerEventDefinition.class, EventDefinitionDialogComposite.class);
		PropertiesCompositeFactory.register(Process.class, ProcessDiagramPropertyComposite.class);
		PropertiesCompositeFactory.register(EndEvent.class, EndEventDetailComposite.class);
		PropertiesCompositeFactory.register(StartEvent.class, StartEventDetailComposite.class);
		PropertiesCompositeFactory.register(ThrowEvent.class, ThrowEventDetailComposite.class);
		PropertiesCompositeFactory.register(StandardLoopCharacteristics.class, StandardLoopCharacteristicsDetailComposite.class);
		PropertiesCompositeFactory.register(MultiInstanceLoopCharacteristics.class, MultiInstanceLoopCharacteristicsDetailComposite.class);
		PropertiesCompositeFactory.register(Gateway.class, GatewayDetailComposite.class);
		PropertiesCompositeFactory.register(Activity.class, ActivityInputDetailComposite.class);
		PropertiesCompositeFactory.register(InputOutputSpecification.class, ActivityInputDetailComposite.class);
		PropertiesCompositeFactory.register(Activity.class, ActivityOutputDetailComposite.class);
		PropertiesCompositeFactory.register(CallChoreography.class, ActivityDetailComposite.class);
		PropertiesCompositeFactory.register(InputOutputSpecification.class, IoParametersDetailComposite.class);
		PropertiesCompositeFactory.register(DataInput.class, DataAssociationDetailComposite.class);
		PropertiesCompositeFactory.register(DataOutput.class, DataAssociationDetailComposite.class);
		PropertiesCompositeFactory.register(ManualTask.class, ManualTaskDetailComposite.class);
		PropertiesCompositeFactory.register(ScriptTask.class, ScriptTaskDetailComposite.class);
		PropertiesCompositeFactory.register(SubProcess.class, ActivityDetailComposite.class);
		PropertiesCompositeFactory.register(Task.class, TaskDetailComposite.class);
	}

	public static final String EDITOR_ID = "org.eclipse.bpmn2.modeler.ui.bpmn2editor";
	public static final String CONTRIBUTOR_ID = "org.eclipse.bpmn2.modeler.ui.PropertyContributor";

	private ModelHandler modelHandler;
	private URI modelUri;
	private URI diagramUri;
	private boolean editable = true;

	protected BPMNDiagram bpmnDiagram;
	protected Bpmn2ResourceImpl bpmnResource;
	
	private IWorkbenchListener workbenchListener;
	private IPartListener2 selectionListener;
    private IResourceChangeListener markerChangeListener;
	private static BPMN2Editor activeEditor;
	private static ITabDescriptorProvider tabDescriptorProvider;

	private BPMN2EditingDomainListener editingDomainListener;
	
	private Bpmn2Preferences preferences;
	private TargetRuntime targetRuntime;

	protected DiagramEditorAdapter editorAdapter;
	
	public BPMN2Editor() {
		super();
	}
	
	public static BPMN2Editor getActiveEditor() {
		return activeEditor;
	}
	
	private void setActiveEditor(BPMN2Editor editor) {
		activeEditor = editor;
		if (activeEditor!=null) {
			Bpmn2Preferences.setActiveProject(activeEditor.getProject());
			TargetRuntime.setCurrentRuntime( activeEditor.getTargetRuntime() );
		}
	}

	protected DiagramEditorAdapter getEditorAdapter() {
		return editorAdapter;
	}
	
	@Override
	protected DiagramEditorInput convertToDiagramEditorInput(IEditorInput input)
			throws PartInitException {
		if (input instanceof Bpmn2DiagramEditorInput) {
			return (Bpmn2DiagramEditorInput) input;
		} else {
			return createNewDiagramEditorInput(input, Bpmn2DiagramType.COLLABORATION, null);
		}
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, convertToDiagramEditorInput(input));

		getTargetRuntime(input);
		setActiveEditor(this);
		
		addSelectionListener();
		addMarkerChangeListener();
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isEditable() {
	    return editable;
	}

	@Override
	public boolean isDirty() {
		if (!editable)
			return false;
		return super.isDirty();
	}
	
	@Override
	protected DefaultUpdateBehavior createUpdateBehavior() {
		return new BPMN2EditorUpdateBehavior(this);
	}
	
    @Override
    protected DefaultPersistencyBehavior createPersistencyBehavior() {
    	return new BPMN2PersistencyBehavior(this);
    }
    
	public Bpmn2Preferences getPreferences() {
		if (preferences==null) {
			loadPreferences(getProject());
		}
		return preferences;
	}
	
	private void loadPreferences(IProject project) {
		preferences = Bpmn2Preferences.getInstance(project);
		preferences.load();
		preferences.getGlobalPreferences().addPropertyChangeListener(this);
	}

	/**
	 * ID for tabbed property sheets.
	 * 
	 * @return the contributor id
	 */
	@Override
	public String getContributorId() {
		return CONTRIBUTOR_ID;
	}

	public TargetRuntime getTargetRuntime(ITabDescriptorProvider tdp) {
		tabDescriptorProvider = tdp;
		return getTargetRuntime();
	}
	
	public TargetRuntime getTargetRuntime() {
		if (targetRuntime==null) {
			targetRuntime = getTargetRuntime(getEditorInput());
		}
		return targetRuntime;
	}
	
	protected TargetRuntime getTargetRuntime(IEditorInput input) {
		if (targetRuntime==null) {
			 // If the project has not been configured for a specific runtime through the "BPMN2"
			 // project properties page (i.e. the target is "None") then allow the runtime extension
			 // plug-ins an opportunity to identify the given process file contents as their own.
			 // If none of the plug-ins respond with "yes, this file is targeted for my runtime",
			 // then use the "None" as the extension. This will configure the BPMN2 Modeler with
			 // generic property sheets and other default behavior.
			targetRuntime = getPreferences().getRuntime();
			if (targetRuntime == TargetRuntime.getDefaultRuntime()) {
				for (TargetRuntime rt : TargetRuntime.getAllRuntimes()) {
					if (rt.getRuntimeExtension().isContentForRuntime(input)) {
						targetRuntime = rt;
						break;
					}
				}
			}
		}
		return targetRuntime;
	}
	
	/**
	 * Beware, creates a new input and changes this editor!
	 */
	private Bpmn2DiagramEditorInput createNewDiagramEditorInput(IEditorInput input, Bpmn2DiagramType diagramType, String targetNamespace) {
		try {
			modelUri = FileService.getInputUri(input);
			input = BPMN2DiagramCreator.createDiagramInput(modelUri, diagramType, targetNamespace, this);
			diagramUri = ((Bpmn2DiagramEditorInput)input).getUri();

			return (Bpmn2DiagramEditorInput)input;
		}
		catch (Exception e) {
			Activator.logError(e);
			throw new RuntimeException(e);
		}
	}

	private void saveModelFile() {
		modelHandler.save();
		((BasicCommandStack) getEditingDomain().getCommandStack()).saveIsDone();
		updateDirtyState();
	}

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		
		// Hook a transaction exception handler so we can get diagnostics about EMF validation errors.
		getEditingDomainListener();
		
		BasicCommandStack basicCommandStack = (BasicCommandStack) getEditingDomain().getCommandStack();

		if (input instanceof Bpmn2DiagramEditorInput) {
			Bpmn2DiagramEditorInput bpmn2DiagramEditorInput = (Bpmn2DiagramEditorInput) input; 
			ResourceSet resourceSet = getEditingDomain().getResourceSet();
			getTargetRuntime().setResourceSet(resourceSet);
			
			/**
			 * we assume that the input will have the model uri
			 */
			bpmnResource = (Bpmn2ResourceImpl) resourceSet.createResource(bpmn2DiagramEditorInput.getModelUri(),
					Bpmn2ModelerResourceImpl.BPMN2_CONTENT_TYPE_ID);

			resourceSet.setURIConverter(new ProxyURIConverterImplExtension());
			resourceSet.eAdapters().add(editorAdapter = new DiagramEditorAdapter(this));

			modelHandler = ModelHandlerLocator.createModelHandler(bpmn2DiagramEditorInput.getModelUri(), bpmnResource);
			ModelHandlerLocator.put(bpmn2DiagramEditorInput.getDiagramUri(), modelHandler);

			getTargetRuntime(input);
			setActiveEditor(this);

			// allow the runtime extension to construct custom tasks and whatever else it needs
			// custom tasks should be added to the current target runtime's custom tasks list
			// where they will be picked up by the toolpalette refresh.
			getTargetRuntime().getRuntimeExtension().initialize(this);

			try {
				if (getModelFile()==null || getModelFile().exists()) {
					bpmnResource.load(null);
				} else {
					saveModelFile();
				}
			} catch (IOException e) {
				Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
				ErrorUtils.showErrorWithLogging(status);
			}
			basicCommandStack.execute(new RecordingCommand(getEditingDomain()) {

				@Override
				protected void doExecute() {
					importDiagram(bpmnResource);
				}
			});
		}
		basicCommandStack.saveIsDone();
		basicCommandStack.flush();
		loadMarkers();
	}
	
	private void importDiagram(Bpmn2Resource resourceToImport) {
		
		// make sure this guy is active, otherwise it's not selectable
		Diagram diagram = getDiagramTypeProvider().getDiagram();
		IFeatureProvider featureProvider = getDiagramTypeProvider().getFeatureProvider();
		diagram.setActive(true);
		
		Bpmn2DiagramEditorInput input = (Bpmn2DiagramEditorInput) getEditorInput();
		Bpmn2DiagramType diagramType = input.getInitialDiagramType();
		String targetNamespace = input.getTargetNamespace();
// FIXME delete
//		if (diagramType != Bpmn2DiagramType.NONE) {
//			bpmnDiagram = modelHandler.createDiagramType(diagramType, targetNamespace);
//			featureProvider.link(diagram, bpmnDiagram);
//			saveModelFile();
//		}
		
		IDiagramEditor diagramEditor = getDiagramTypeProvider().getDiagramEditor();
		TransactionalEditingDomain editingDomain = diagramEditor.getEditingDomain();
		ModelImportCommand command = new ModelImportCommand(editingDomain, diagramEditor, resourceToImport);
		
		editingDomain.getCommandStack().execute(command);
		
		if (!command.wasSuccessful() || !command.getRecordedWarnings().isEmpty()) {
			handleImportErrorAndWarnings(command.getRecordedException(), command.getRecordedWarnings());
		}
		
		// this needs to happen AFTER the diagram has been imported because we need
		// to be able to determine the diagram type from the file's contents in order
		// to build the right tool palette for the target runtime and model enablements.
		GFPaletteRoot pr = (GFPaletteRoot)getPaletteRoot();
		pr.updatePaletteEntries();
	}

	protected void handleImportErrorAndWarnings(ImportException exception, List<ImportException> warnings) {
		ModelProblemsDialog dialog = new ModelProblemsDialog(getSite().getShell());
		
		dialog.setException(exception);
		dialog.setWarnings(warnings);
		
		dialog.open();
		
		if (exception != null) {
			throw exception;
		}
	}

	@Override
	protected PictogramElement[] getPictogramElementsForSelection() {
		// filter out invisible elements when setting selection
		PictogramElement[] pictogramElements = super.getPictogramElementsForSelection();
		if (pictogramElements==null)
			return null;
		ArrayList<PictogramElement> visibleList = new ArrayList<PictogramElement>();
		for (PictogramElement pe : pictogramElements) {
			if (pe.isVisible())
				visibleList.add(pe);
		}
		return visibleList.toArray(new PictogramElement[visibleList.size()]);
	}
	
    @Override
    public void gotoMarker(IMarker marker) {
        final EObject target = getTargetObject(marker);
        if (target == null) {
            return;
        }
        final PictogramElement pe = getDiagramTypeProvider().getFeatureProvider().getPictogramElementForBusinessObject(
                target);
        if (pe == null) {
            return;
        }
        selectPictogramElements(new PictogramElement[] {pe });
    }

    private void loadMarkers() {
    	if (getModelFile()!=null) {
	        // read in the markers
	        BPMN2ValidationStatusLoader vsl = new BPMN2ValidationStatusLoader(this);
	
	        try {
	            vsl.load(Arrays.asList(getModelFile().findMarkers(
	            		BPMN2ProjectValidator.BPMN2_MARKER_ID, true, IResource.DEPTH_ZERO)));
	        } catch (CoreException e) {
	            Activator.logStatus(e.getStatus());
	        }
    	}
    }
    
    private EObject getTargetObject(IMarker marker) {
        final String uriString = marker.getAttribute(EValidator.URI_ATTRIBUTE, null);
        final URI uri = uriString == null ? null : URI.createURI(uriString);
        if (uri == null) {
            return null;
        }
        return getEditingDomain().getResourceSet().getEObject(uri, false);
    }

	private void removeWorkbenchListener()
	{
		if (workbenchListener!=null) {
			PlatformUI.getWorkbench().removeWorkbenchListener(workbenchListener);
			workbenchListener = null;
		}
	}
	
	private void addSelectionListener() {
		if (selectionListener == null) {
			IWorkbenchPage page = getSite().getPage();
			selectionListener = new IPartListener2() {
				public void partActivated(IWorkbenchPartReference partRef) {
				}

				@Override
				public void partBroughtToTop(IWorkbenchPartReference partRef) {
					IWorkbenchPart part = partRef.getPart(false);
				}

				@Override
				public void partClosed(IWorkbenchPartReference partRef) {
				}

				@Override
				public void partDeactivated(IWorkbenchPartReference partRef) {
				}

				@Override
				public void partOpened(IWorkbenchPartReference partRef) {
				}

				@Override
				public void partHidden(IWorkbenchPartReference partRef) {
				}

				@Override
				public void partVisible(IWorkbenchPartReference partRef) {
				}

				@Override
				public void partInputChanged(IWorkbenchPartReference partRef) {
				}
			};
			page.addPartListener(selectionListener);
		}
	}

	private void removeSelectionListener()
	{
		if (selectionListener!=null) {
			getSite().getPage().removePartListener(selectionListener);
			selectionListener = null;
		}
	}

	private void addMarkerChangeListener() {
		if (getModelFile()!=null) {
			if (markerChangeListener==null) {
				markerChangeListener = new BPMN2MarkerChangeListener(this);
		        getModelFile().getWorkspace().addResourceChangeListener(markerChangeListener, IResourceChangeEvent.POST_BUILD);
			}
		}
	}
	
	private void removeMarkerChangeListener() {
		if (markerChangeListener!=null) {
			getModelFile().getWorkspace().removeResourceChangeListener(markerChangeListener);
			markerChangeListener = null;
		}
	}
	
	public void refreshTitle() {
		String name = getEditorInput().getName();
		setPartName(URI.decode(name));
	}

	public BPMN2EditingDomainListener getEditingDomainListener() {
		if (editingDomainListener==null) {
			TransactionalEditingDomainImpl editingDomain = (TransactionalEditingDomainImpl)getEditingDomain();
			if (editingDomain==null) {
				return null;
			}
			editingDomainListener = new BPMN2EditingDomainListener(this);

			Lifecycle domainLifeCycle = (Lifecycle) editingDomain.getAdapter(Lifecycle.class);
			domainLifeCycle.addTransactionalEditingDomainListener(editingDomainListener);
		}
		return editingDomainListener;
	}
	
	public BasicDiagnostic getDiagnostics() {
		return getEditingDomainListener().getDiagnostics();
	}
	
	@Override
	@SuppressWarnings("rawtypes") 
	public Object getAdapter(Class required) {
		if (required==ITabDescriptorProvider.class) {
			if (tabDescriptorProvider==null) {
				IWorkbenchPage page = getEditorSite().getPage();
				String viewID = "org.eclipse.ui.views.PropertySheet";
				try {
					page.showView(viewID, null, IWorkbenchPage.VIEW_CREATE);
					page.showView(viewID, null,  IWorkbenchPage.VIEW_ACTIVATE);
				}
				catch (Exception e) {}
			}
			return tabDescriptorProvider;
		}
		if (required==TargetRuntime.class)
			return getTargetRuntime();
		if (required==Bpmn2Preferences.class)
			return getPreferences();
		if (required == IPropertySheetPage.class) {
			return new Bpmn2TabbedPropertySheetPage(this);
		}
		if (required == SelectionSynchronizer.class) {
			return getSelectionSynchronizer();
		}
		if (required == IContentOutlinePage.class) {
			if (getDiagramTypeProvider() != null) {
				BPMN2EditorOutlinePage outlinePage = new BPMN2EditorOutlinePage(this);
				return outlinePage;
			}
		}
		
		return super.getAdapter(required);
	}

	@Override
	public void dispose() {
		// clear ID mapping tables if no more instances of editor are active
		int instances = 0;
		IEditorSite editorSite = getEditorSite();
		
		Bpmn2DiagramEditorInput diagramEditorInput = (Bpmn2DiagramEditorInput) getEditorInput();
		
		// need to check != null to deal with errors
		if (editorSite != null) {
			IWorkbenchPage[] pages = editorSite.getWorkbenchWindow().getPages();
			for (IWorkbenchPage p : pages) {
				IEditorReference[] refs = p.getEditorReferences();
				instances += refs.length;
			}
		}
		
		File diagramFile = new File(diagramEditorInput.getDiagramUri().toFileString());
		if (diagramFile.exists()) {
			try {
				diagramFile.delete();
			} catch (Exception e) {
				Activator.logStatus(new Status(IStatus.WARNING, Activator.PLUGIN_ID, e.getMessage(), e));
			}
		}
		
		try {
			if (modelHandler != null) {
				ModelUtil.clearIDs(modelHandler.getResource(), instances==0);
			}
			getPreferences().getGlobalPreferences().removePropertyChangeListener(this);
			
			getResourceSet().eAdapters().remove(getEditorAdapter());
			removeSelectionListener();
			if (instances==0)
				setActiveEditor(null);
		} catch (Exception e) {
			Activator.logError(e);
		}
		
		super.dispose();
		try {
			ModelHandlerLocator.remove(diagramEditorInput.getModelUri());
			FileService.deleteTempFile(diagramEditorInput.getModelUri());
			removeWorkbenchListener();
			removeMarkerChangeListener();
			getPreferences().dispose();
		} catch (Exception e) {
			Activator.logError(e);
		}
	}

	public IPath getModelPath() {
		if (getModelFile()!=null)
			return getModelFile().getFullPath();
		return null;
	}
	
	public IProject getProject() {
		if (getModelFile()!=null)
			return getModelFile().getProject();
		return null;
	}
	
	public IFile getModelFile() {
		if (modelUri!=null) {
			String uriString = modelUri.trimFragment().toPlatformString(true);
			if (uriString!=null) {
				IPath fullPath = new Path(uriString);
				return ResourcesPlugin.getWorkspace().getRoot().getFile(fullPath);
			}
		}
		return null;
	}
	
	public ModelHandler getModelHandler() {
		return modelHandler;
	}
	
	public void createPartControl(Composite parent) {
		if (getGraphicalViewer()==null) {
			super.createPartControl(parent);
		}
	}
	
	public BPMNDiagram getBpmnDiagram() {
		if (bpmnDiagram==null)
			bpmnDiagram = getModelHandler().getDefinitions().getDiagrams().get(0);

//		if (bpmnDiagram!=null) {
//			GraphicalViewer viewer = getGraphicalViewer();
//			mapDiagramToViewer.put(bpmnDiagram, viewer);
//		}
		return bpmnDiagram;
	}
	
	public void setBpmnDiagram(final BPMNDiagram bpmnDiagram) {
		// create a new Graphiti Diagram if needed
		Diagram diagram = DIUtils.getOrCreateDiagram(this, bpmnDiagram);
		
		// Tell the DTP about the new Diagram
		getDiagramTypeProvider().resourceReloaded(diagram);
		getRefreshBehavior().initRefresh();
		setPictogramElementsForSelection(null);
		// set Diagram as contents for the graphical viewer and refresh
		getGraphicalViewer().setContents(diagram);
		
		refreshContent();
		
		// remember this for later
		this.bpmnDiagram = bpmnDiagram;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);

		Resource resource = getResourceSet().getResource( ((Bpmn2DiagramEditorInput) getEditorInput()).getModelUri(), false);
		BPMN2ProjectValidator.validateOnSave(resource, monitor);
	}

	@Override
	public boolean isSaveAsAllowed() {
		// FIXME allow save as later, its buggy right now
		return false;
	}
	
	@Override
	public void doSaveAs() {
		IFile oldFile = getModelFile();
		SaveAsDialog saveAsDialog = new SaveAsDialog(getSite().getShell());
		saveAsDialog.setOriginalFile(oldFile);
		saveAsDialog.create();
		if (saveAsDialog.open() == SaveAsDialog.CANCEL) {
			return;
		}
		IPath newFilePath = saveAsDialog.getResult();
		if (newFilePath == null){
			return;
		}
		
        IFile newFile = ResourcesPlugin.getWorkspace().getRoot().getFile(newFilePath);
        IWorkbenchPage page = getSite().getPage();
        
        try {
        	// Save the current(old) file
        	doSave(null);
        	// if new file exists, close its editor (if open) and delete the existing file
            if (newFile.exists()) {
    			IEditorPart editorPart = ResourceUtil.findEditor(page, newFile);
    			if (editorPart!=null)
	    			page.closeEditor(editorPart, false);
        		newFile.delete(true, null);
            }
            // make a copy
			oldFile.copy(newFilePath, true, null);
		} catch (CoreException e) {
			showErrorDialogWithLogging(e);
			return;
		}

        // open new editor
    	try {
			page.openEditor(new FileEditorInput(newFile), BPMN2Editor.EDITOR_ID);
		} catch (PartInitException e1) {
			showErrorDialogWithLogging(e1);
			return;
		}
    	
    	// and close the old editor
		IEditorPart editorPart = ResourceUtil.findEditor(page, oldFile);
		if (editorPart!=null)
			page.closeEditor(editorPart, false);
		
    	try {
			newFile.refreshLocal(IResource.DEPTH_ZERO,null);
		} catch (CoreException e) {
			showErrorDialogWithLogging(e);
			return;
		}
	}

	public void closeEditor() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				boolean closed = getSite().getPage().closeEditor(BPMN2Editor.this, false);
				if (!closed){
					// If close editor fails, try again with explicit editorpart 
					// of the old file
					IFile oldFile = ResourcesPlugin.getWorkspace().getRoot().getFile(getModelPath());
					IEditorPart editorPart = ResourceUtil.findEditor(getSite().getPage(), oldFile);
					closed = getSite().getPage().closeEditor(editorPart, false);
				}
			}
		});
	}

	// Show error dialog and log the error
	private void showErrorDialogWithLogging(Exception e) {
		Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
		ErrorUtils.showErrorWithLogging(status);
	}

	////////////////////////////////////////////////////////////////////////////////
	// WorkspaceSynchronizer handlers called from delegate
	////////////////////////////////////////////////////////////////////////////////
	
	public boolean handleResourceChanged(final Resource resource) {
		// FIXME
		//		try {
//			getEditingDomain().runExclusive(new Runnable() {
//				
//				@Override
//				public void run() {
//					importDiagram((Bpmn2Resource) resource);
//				}
//			});
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		TransactionalCommandStack stack = (TransactionalCommandStack) getEditingDomain().getCommandStack();
//		
//		Map<String, Object> options = new HashMap<String, Object>();
//		options.put(Transaction.OPTION_IS_UNDO_REDO_TRANSACTION, true);
//		
//		try {
//			stack.execute(new RecordingCommand(getEditingDomain()) {
//
//				@Override
//				protected void doExecute() {
//					importDiagram(bpmnResource);
//				}
//			}, options);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (RollbackException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		return true;
	}

	public boolean handleResourceDeleted(Resource resource) {
		closeEditor();
		return true;
	}

	public boolean handleResourceMoved(Resource resource, URI newURI) {
		URI oldURI = resource.getURI();
		resource.setURI(newURI);
		
		if (modelUri.equals(oldURI)) {
			ModelHandlerLocator.remove(modelUri);
			modelUri = newURI;
			if (preferences!=null) {
				preferences.getGlobalPreferences().removePropertyChangeListener(this);
				preferences.dispose();
				preferences = null;
			}
			targetRuntime = null;
			modelHandler = ModelHandlerLocator.createModelHandler(modelUri, (Bpmn2ResourceImpl)resource);
			ModelHandlerLocator.put(diagramUri, modelHandler);
		}
		else if (diagramUri.equals(oldURI)) {
			ModelHandlerLocator.remove(diagramUri);
			diagramUri = newURI;
			ModelHandlerLocator.put(diagramUri, modelHandler);
		}

		return true;
	}
	
	////////////////////////////////////////////////////////////////////////////////
	// Other handlers
	////////////////////////////////////////////////////////////////////////////////

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// Graphiti understands multipage editors
		super.selectionChanged(part,selection); // Graphiti's DiagramEditorInternal
		// but apparently GEF doesn't
		//updateActions(getSelectionActions()); // usually done in GEF's GraphicalEditor
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		
		System.out.println(event);
		
		if (event.getProperty().endsWith(Bpmn2Preferences.PREF_SHAPE_STYLE)) {
			getEditingDomain().getCommandStack().execute(new RecordingCommand(getEditingDomain()) {
				@Override
				protected void doExecute() {
					IPeService peService = Graphiti.getPeService();
					TreeIterator<EObject> iter = getDiagramTypeProvider().getDiagram().eAllContents();
					while (iter.hasNext()) {
						EObject o = iter.next();
						if (o instanceof PictogramElement) {
							PictogramElement pe = (PictogramElement)o;
							BaseElement be = BusinessObjectUtil.getFirstElementOfType(pe, BaseElement.class);
							if (be!=null) {
								TreeIterator<EObject> childIter = pe.eAllContents();
								while (childIter.hasNext()) {
									o = childIter.next();
									if (o instanceof GraphicsAlgorithm) {
										GraphicsAlgorithm ga = (GraphicsAlgorithm)o;
										if (peService.getPropertyValue(ga, Bpmn2Preferences.PREF_SHAPE_STYLE)!=null) {
											StyleUtil.applyStyle(ga, be);
										}
									}
								}
							}
						}
					}
				}
			});
		}
	}
}
