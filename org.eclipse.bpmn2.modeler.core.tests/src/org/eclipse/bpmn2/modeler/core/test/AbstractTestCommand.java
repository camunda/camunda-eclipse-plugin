package org.eclipse.bpmn2.modeler.core.test;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.junit.rules.TemporaryFolder;

public abstract class AbstractTestCommand extends RecordingCommand {

	protected IProject project;
	protected TransactionalEditingDomain editingDomain;
	protected String diagramName;
	protected Resource createdResource;

	protected TemporaryFolder folder = null;
	protected Resource resource;
	protected Diagram diagram;

	public Diagram getDiagram() {
		return diagram;
	}

	protected IDiagramTypeProvider dtp;
	private XMLResourceImpl modelResource;

	public AbstractTestCommand(TemporaryFolder folder,
			TransactionalEditingDomain editingDomain, String diagramName,
			Resource resource) {
		super(editingDomain);
		this.folder = folder;
		this.editingDomain = editingDomain;
		this.diagramName = diagramName;
		this.resource = resource;
	}

	@Override
	protected void doExecute() {
		// Get all EClasses

		diagram = Graphiti.getPeCreateService().createDiagram("BPMN2",
				diagramName, true);

		URI uri = null;

		try {
			uri = URI.createFileURI(folder.newFile(diagramName)
					.getAbsolutePath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		

		dtp = GraphitiUi.getExtensionManager().createDiagramTypeProvider(
				diagram, "org.eclipse.bpmn2.modeler.ui.diagram.MainBPMNDiagramType");

		this.test();
	}

	abstract void test();
}
