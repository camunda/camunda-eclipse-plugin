package org.camunda.bpm.modeler.ui.diagram.editor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.core.importer.ImportException;
import org.camunda.bpm.modeler.core.importer.ModelImportCommand;
import org.camunda.bpm.modeler.core.validation.Bpmn2ProjectValidator;
import org.camunda.bpm.modeler.core.validation.BpmnValidationStatusLoader;
import org.camunda.bpm.modeler.ui.dialog.importer.ModelProblemsDialog;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.platform.IDiagramContainer;
import org.eclipse.graphiti.ui.editor.DefaultRefreshBehavior;
import org.eclipse.graphiti.ui.internal.editor.GFPaletteRoot;
import org.eclipse.swt.widgets.Display;

@SuppressWarnings("restriction")
public class Bpmn2RefreshBehavior extends DefaultRefreshBehavior {

	public Bpmn2RefreshBehavior(Bpmn2DiagramBehavior diagramBehavior) {
		super(diagramBehavior);
	}
	
	protected Bpmn2DiagramBehavior getDiagramBehavior() {
		return (Bpmn2DiagramBehavior) diagramBehavior;
	}
	
	@Override
	protected void autoUpdate() {
		// use the auto update functionality to 
		// import the BPMN 2.0 model on reset and startup
		
		// check if bpmn input is actually loaded
		// before importing the bpmn diagram
		if (getDiagramContainer().isBpmnLoaded()) {
			importBpmnDiagram();
		}
		
		super.autoUpdate();
	}

	/**
	 * Import diagram from input resources
	 */
	private void importBpmnDiagram() {

		TransactionalEditingDomain editingDomain = getEditingDomain();

		Bpmn2Editor diagramContainer = getDiagramContainer();
		
		final Resource diagramResource = diagramContainer.getDiagramResource();
		final Bpmn2Resource bpmnResource = diagramContainer.getModelResource();
		
		
		// init diagram / behavior
		// (needs to be done once per import)

		CommandStack commandStack = editingDomain.getCommandStack();
		commandStack.execute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				importDiagram(bpmnResource, diagramResource);
				bpmnResource.setModified(false);
			}
		});

		try {
			diagramResource.save(null);
		} catch (IOException e) {
			Activator.logStatus(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Failed to save diagram resource", e));
		}

		commandStack.flush();
		loadMarkers();
	}
	
	private void importDiagram(Bpmn2Resource bpmnResource, Resource diagramResource) {

		IDiagramContainer diagramContainer = diagramBehavior.getDiagramContainer();
		TransactionalEditingDomain editingDomain = diagramBehavior.getEditingDomain();
		
		ModelImportCommand command = new ModelImportCommand(editingDomain, diagramContainer, bpmnResource, diagramResource);
		
		try {
			editingDomain.getCommandStack().execute(command);

			if (!command.wasSuccessful() || !command.getRecordedWarnings().isEmpty()) {
				handleImportErrorAndWarnings(command.getRecordedException(), command.getRecordedWarnings());
			}
		} catch (Exception e) {
			// if we got here, there was an exception in the import on emf model level
			// we dont want to die now because this will produce NPEs in the
			// creation of the editor, so we swallow this exception -> much better
			// user experience
			Activator.logError(e);
		}

		// this needs to happen AFTER the diagram has been imported because we need
		// to be able to determine the diagram type from the file's contents in
		// order
		// to build the right tool palette for the target runtime and model
		// enablements.
		
		refreshPalette();
	}

	private void refreshPalette() {
		GFPaletteRoot paletteRoot = (GFPaletteRoot) getDiagramBehavior().getPaletteBehavior().getPaletteRoot();
		paletteRoot.updatePaletteEntries();		
	}

	protected void handleImportErrorAndWarnings(ImportException exception, List<ImportException> warnings) {
		ModelProblemsDialog dialog = new ModelProblemsDialog(Display.getDefault().getActiveShell());

		dialog.setException(exception);
		dialog.setWarnings(warnings);

		dialog.open();

		if (exception != null) {
			throw exception;
		}
	}

	private void loadMarkers() {
		Bpmn2Editor bpmnEditor = getDiagramContainer();
		IFile modelFile = bpmnEditor.getModelFile();
		
		if (modelFile != null) {
			// read in the markers
			BpmnValidationStatusLoader vsl = new BpmnValidationStatusLoader(bpmnEditor);

			try {
				vsl.load(Arrays.asList(modelFile.findMarkers(Bpmn2ProjectValidator.BPMN2_MARKER_ID, true, IResource.DEPTH_ZERO)));
			} catch (CoreException e) {
				Activator.logStatus(e.getStatus());
			}
		}
	}

	// private accessor helpers
	private Bpmn2Editor getDiagramContainer() {
		return getDiagramBehavior().getDiagramContainer();
	}

	private TransactionalEditingDomain getEditingDomain() {
		return getDiagramBehavior().getEditingDomain();
	}
}
