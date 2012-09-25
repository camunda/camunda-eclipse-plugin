package org.eclipse.bpmn2.modeler.core.test;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.services.GraphitiUi;

public abstract class AbstractTestCommand extends RecordingCommand {

	protected TransactionalEditingDomain editingDomain;
	protected String diagramName;

	protected Resource resource;
	protected Diagram diagram;

	public Diagram getDiagram() {
		return diagram;
	}

	protected IDiagramTypeProvider dtp;

	public AbstractTestCommand(TransactionalEditingDomain editingDomain,
			String diagramName, Resource resource) {
		super(editingDomain);
		this.editingDomain = editingDomain;
		this.diagramName = diagramName;
		this.resource = resource;
	}

	@Override
	protected void doExecute() {

		diagram = Graphiti.getPeCreateService().createDiagram("BPMN2",
				diagramName, true);

		dtp = GraphitiUi.getExtensionManager().createDiagramTypeProvider(
				diagram,
				"org.eclipse.bpmn2.modeler.ui.diagram.MainBPMNDiagramType");

		test();
	}

	abstract void test();
}
