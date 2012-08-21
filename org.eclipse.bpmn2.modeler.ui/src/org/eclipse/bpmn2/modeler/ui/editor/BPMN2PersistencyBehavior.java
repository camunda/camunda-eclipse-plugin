package org.eclipse.bpmn2.modeler.ui.editor;

import java.util.Arrays;

import org.eclipse.bpmn2.modeler.core.validation.BPMN2ProjectValidator;
import org.eclipse.bpmn2.modeler.core.validation.BPMN2ValidationStatusLoader;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.editor.DefaultPersistencyBehavior;
import org.eclipse.graphiti.ui.editor.DiagramEditor;

public class BPMN2PersistencyBehavior extends DefaultPersistencyBehavior {

	BPMN2Editor editor;
	
	public BPMN2PersistencyBehavior(DiagramEditor diagramEditor) {
		super(diagramEditor);
		editor = (BPMN2Editor)diagramEditor;
	}
    @Override
    public Diagram loadDiagram(URI modelUri) {
    	Diagram diagram = super.loadDiagram(modelUri);

        // read in the markers
        BPMN2ValidationStatusLoader vsl = new BPMN2ValidationStatusLoader(editor);

        try {
            vsl.load(Arrays.asList(editor.getModelFile().findMarkers(
            		BPMN2ProjectValidator.BPMN2_MARKER_ID, true, IResource.DEPTH_ZERO)));
        } catch (CoreException e) {
            Activator.logStatus(e.getStatus());
        }

        return diagram;
    }

}
