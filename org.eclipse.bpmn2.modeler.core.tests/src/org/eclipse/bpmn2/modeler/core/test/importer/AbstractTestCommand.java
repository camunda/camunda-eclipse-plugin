package org.eclipse.bpmn2.modeler.core.test.importer;

import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.impl.DocumentRootImpl;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.services.GraphitiUi;

public abstract class AbstractTestCommand extends RecordingCommand {
	
	protected AbstractImportBpmn2ModelTest testCase;
	protected String diagramName;
	
	protected Diagram diagram;	
	protected IDiagramTypeProvider diagramTypeProvider;
	protected Resource diagramResource;

	protected Throwable recordedException;
	
	public AbstractTestCommand(AbstractImportBpmn2ModelTest importBpmnModelTest, String diagramName) {
		super(importBpmnModelTest.getEditingDomain());
		
		this.testCase = importBpmnModelTest;
		this.diagramName = diagramName;
	}

	@Override
	protected void doExecute() {

		try {
			diagram = Graphiti.getPeCreateService().createDiagram("BPMN2", diagramName, true);

			String fileName = diagramName.replaceAll("/", ".").replaceAll(".bpmn", ".diagram");
			URI uri = URI.createFileURI(testCase.getTempFolder().newFile(fileName).getAbsolutePath());
			
			System.out.println("Using temp folder " + testCase.getTempFolder());
			
			ModelHandlerLocator.put(uri, testCase.getModelHandler());
	
			diagramResource = testCase.getEditingDomain().getResourceSet().createResource(uri);
			diagramResource.getContents().add(diagram);
			
			diagramTypeProvider = GraphitiUi.getExtensionManager().createDiagramTypeProvider(
					diagram,
					"org.eclipse.bpmn2.modeler.ui.diagram.MainBPMNDiagramType");
			
			Bpmn2ResourceImpl resource = testCase.getResource();
			DocumentRootImpl rootImpl = (DocumentRootImpl) resource.getContents().get(0);
			BPMNDiagram bpmnDiagram = rootImpl.getDefinitions().getDiagrams().get(0);
			
			diagramTypeProvider.getDiagramEditor().getResourceSet();
	
			diagramTypeProvider.getFeatureProvider().link(diagram, bpmnDiagram);
			
			test(diagramTypeProvider, diagram);
			
		} catch (RuntimeException e) {
			this.recordedException = e;
			throw e;
			
		} catch (Throwable e) {
			this.recordedException = e;
			throw new RuntimeException(e);
		}
	}

	public abstract void test(IDiagramTypeProvider diagramTypeProvider, Diagram diagram) throws Throwable;

	public Throwable getRecordedException() {
		return recordedException;
	}
}
