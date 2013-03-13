package org.camunda.bpm.modeler.ui.editor;

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

    	return diagram;
    }

}
