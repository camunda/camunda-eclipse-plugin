package org.camunda.bpm.modeler.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

import org.camunda.bpm.modeler.core.preferences.Bpmn2Preferences;
import org.camunda.bpm.modeler.test.util.TestHelper;
import org.camunda.bpm.modeler.test.util.TestHelper.EditorResources;
import org.camunda.bpm.modeler.test.util.TestHelper.ModelResources;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.platform.IDiagramContainer;

public abstract class AbstractTestCommand extends RecordingCommand {
	
	protected ModelResources modelResources;
	protected EditorResources editorResources;
	
	private Throwable recordedException;

	/**
	 * Test case executed
	 */
	protected AbstractEditorTest testCase;
	
	/**
	 * Name of the executed test
	 */
	protected String testName;
	
	private File tempDir;
	
	private static final String TEST_DIR = "target/test";
	
	public AbstractTestCommand(AbstractEditorTest testCase, String testName, ModelResources modelResources, File tempDir) {
		super(modelResources.getEditingDomain());
		
		this.testCase = testCase;
		this.testName = testName;

		this.tempDir = tempDir;
		this.modelResources = modelResources;
	}

	@Override
	protected final void doExecute() {
		
		try {
			
			// initialize bpmn 2 preferences
			Bpmn2Preferences.getInstance(modelResources.getResource()).load();
			
			// create editor
			editorResources = TestHelper.createEditor(modelResources, tempDir);

			// apply resources to test case
			testCase.setModelResources(modelResources);
			testCase.setEditorResources(editorResources);
			
			Bpmn2Resource bpmn2Resource = modelResources.getResource();

			File testFileDir = new File(TEST_DIR);
			testFileDir.mkdir();
			
			saveTestResource(bpmn2Resource, "before", testFileDir);
			
			execute(editorResources.getDiagramContainer(), editorResources.getDiagram());
			
			testCase.setTestResources(TestHelper.createModel(saveTestResource(bpmn2Resource, "after", testFileDir)));
		} catch (RuntimeException e) {
			this.recordedException = e;
			throw e;
			
		} catch (Throwable e) {
			this.recordedException = e;
			throw new RuntimeException(e);
		}
	}
	
	private String saveTestResource(Bpmn2Resource resource, String suffix, File directory) {
		String fileName = testCase.getClass().getName() + "." + testName + "." + suffix + ".bpmn";
		
		FileOutputStream out = null;
		File file = new File(directory, fileName);
		
		try {
			out = new FileOutputStream(file);
			resource.save(out, Collections.emptyMap());
			out.close();
		} catch (Exception e) {
			// We cant always write models, we have tests with broken models, wich can not be saved
			// not checking for exception type, because they are somewhat arbitary (e.g. BasicIndexOutofBoundsException)
			
			if (out != null) {
				try {
					out.close();
				} catch (IOException ioe) {
					// cannot handle
				}
			}
		}
		return file.getPath();
	}

	/**
	 * Execute 
	 * @param diagramTypeProvider
	 * @param diagram
	 * @throws Throwable
	 */
	public abstract void execute(IDiagramContainer diagramContainer, Diagram diagram) throws Throwable;

	/**
	 * Returns the exception recorded during command execution
	 * @return
	 */
	public Throwable getRecordedException() {
		return recordedException;
	}
}
