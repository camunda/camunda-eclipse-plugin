package org.eclipse.bpmn2.modeler.core.importer;

import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.platform.IDiagramEditor;

public class Bpmn2ModelImportCommand extends RecordingCommand {

	protected IDiagramEditor diagramEditor;
	protected Bpmn2Resource resource;

	public Bpmn2ModelImportCommand(TransactionalEditingDomain domain, IDiagramEditor diagramEditor, Bpmn2Resource resource) {
		super(domain);
		this.diagramEditor = diagramEditor;
		this.resource = resource;
	}

	@Override
	protected void doExecute() {
		
		Bpmn2ModelImport bpmn2ModelImport = new Bpmn2ModelImport(diagramEditor.getDiagramTypeProvider(), resource);
		bpmn2ModelImport.execute();
		
	}

	
}
