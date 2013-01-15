package org.eclipse.bpmn2.modeler.core.test;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.impl.DocumentRootImpl;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResourceRule;
import org.eclipse.bpmn2.modeler.core.test.util.TestHelper.EditorResources;
import org.eclipse.bpmn2.modeler.core.test.util.TestHelper.ModelResources;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 * 
 * @author Nico Rehwaldt
 */
public abstract class AbstractEditorTest {

	private ModelResources modelResources;
	private ModelResources testResources;
	private EditorResources editorResources;
	
	protected Diagram diagram;
	protected IDiagramTypeProvider diagramTypeProvider;

	protected Bpmn2Resource bpmnResource;
	
	protected TransactionalEditingDomain editingDomain;
	protected boolean importAfterTest = false;
	
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Rule
	public DiagramResourceRule commandRule = new DiagramResourceRule();

	
	@After
	public void importTestResult() {
		if (importAfterTest && testResources != null){
			setModelResources(testResources);
			new ModelImport(getDiagramTypeProvider(), getResource()).execute();
		}
	}

	/**
	 * Set the model resources for the test case
	 * @param modelResources
	 */
	public void setModelResources(ModelResources modelResources) {
		this.modelResources = modelResources;
		
		if (modelResources != null) {
			this.editingDomain = modelResources.getEditingDomain();
			this.bpmnResource = modelResources.getResource();
		} else {
			this.editingDomain = null;
			this.bpmnResource = null;
		}
	}
	
	/**
	 * Set the editor resources for the test case
	 * 
	 * @param editorResources
	 */
	public void setEditorResources(EditorResources editorResources) {
		this.editorResources = editorResources;
		
		if (editorResources != null) {
			this.diagram = editorResources.getDiagram();
			this.diagramTypeProvider = editorResources.getTypeProvider();
		} else {
			this.diagram = null;
			this.diagramTypeProvider = null;
		}
	}
	
	protected Diagram getDiagram() {
		return diagram;
	}

	protected IDiagramTypeProvider getDiagramTypeProvider() {
		return diagramTypeProvider;
	}
	
	public TransactionalEditingDomain getEditingDomain() {
		return editingDomain;
	}

	public Bpmn2Resource getResource() {
		return bpmnResource;
	}
	
	public Definitions getDefinitions() {
		return ((DocumentRootImpl) getResource().getContents().get(0)).getDefinitions();
	}

	public TemporaryFolder getTempFolder() {
		return tempFolder;
	}
	
	protected boolean isEditorLoaded() {
		return modelResources != null && editorResources != null;
	}

	public void setTestResources(ModelResources testResources) {
		this.testResources = testResources;
	}
}
