package org.eclipse.bpmn2.modeler.core.test;

import java.io.IOException;

import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.services.GraphitiUi;

public abstract class AbstractTestCommand extends RecordingCommand {
	
	protected AbstractImportBpmnModelTest testCase;
	protected String diagramName;
	
	protected Diagram diagram;	
	protected IDiagramTypeProvider diagramTypeProvider;
	protected Resource diagramResource;

	public AbstractTestCommand(AbstractImportBpmnModelTest importBpmnModelTest, String diagramName) {
		super(importBpmnModelTest.getEditingDomain());
		testCase = importBpmnModelTest;
		this.diagramName = diagramName;
	}

	@Override
	protected void doExecute() {

		diagram = Graphiti.getPeCreateService().createDiagram("BPMN2", diagramName, true);

		URI uri = null;

		try {
			uri = URI.createFileURI(testCase.getTempFolder().newFile(diagramName + ".diagram").getAbsolutePath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		ModelHandlerLocator.put(uri, testCase.getModelHandler());

		diagramResource = testCase.getEditingDomain().getResourceSet().createResource(uri);
		diagramResource.getContents().add(diagram);
		
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
