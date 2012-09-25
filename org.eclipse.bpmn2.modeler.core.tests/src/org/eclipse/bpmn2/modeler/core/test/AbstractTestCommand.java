package org.eclipse.bpmn2.modeler.core.test;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.services.GraphitiUi;

public abstract class AbstractTestCommand extends RecordingCommand {

	protected TransactionalEditingDomain editingDomain;
	protected String diagramName;

	protected Diagram diagram;
	private IDiagramTypeProvider diagramTypeProvider;

	public AbstractTestCommand(TransactionalEditingDomain editingDomain,
			String diagramName) {
		super(editingDomain);
		this.editingDomain = editingDomain;
		this.diagramName = diagramName;
	}

	@Override
	protected void doExecute() {

		diagram = Graphiti.getPeCreateService().createDiagram("BPMN2",
				diagramName, true);

		diagramTypeProvider = GraphitiUi.getExtensionManager().createDiagramTypeProvider(
				diagram,
				"org.eclipse.bpmn2.modeler.ui.diagram.MainBPMNDiagramType");

		test(diagramTypeProvider);
	}

	abstract void test(IDiagramTypeProvider diagramTypeProvider2);

	public Diagram getDiagram() {
		return diagram;
	}
}
