package org.eclipse.bpmn2.modeler.core.test;

import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.platform.IDiagramEditor;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.services.GraphitiUi;

public abstract class AbstractTestCommand extends RecordingCommand {

	protected TransactionalEditingDomain editingDomain;
	protected String diagramName;

	protected Diagram diagram;
	protected IDiagramTypeProvider diagramTypeProvider;
	protected Bpmn2ResourceImpl resource;

	public AbstractTestCommand(TransactionalEditingDomain editingDomain,
			String diagramName, Bpmn2ResourceImpl resource) {
		super(editingDomain);
		this.editingDomain = editingDomain;
		this.diagramName = diagramName;
		this.resource = resource;
	}

	@Override
	protected void doExecute() {

		diagram = Graphiti.getPeCreateService().createDiagram("BPMN2",
				diagramName, true);
		
		resource.getContents().add(diagram);
		
		diagramTypeProvider = GraphitiUi.getExtensionManager().createDiagramTypeProvider(
				diagram,
				"org.eclipse.bpmn2.modeler.ui.diagram.MainBPMNDiagramType");
		
		IDiagramEditor diagramEditor = diagramTypeProvider.getDiagramEditor();

		test(diagramTypeProvider);
	}

	abstract void test(IDiagramTypeProvider diagramTypeProvider2);

	public Diagram getDiagram() {
		return diagram;
	}
}
