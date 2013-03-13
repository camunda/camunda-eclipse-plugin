package org.camunda.bpm.modeler.ui.editor;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

public class SourceViewer extends StructuredTextEditor {
	
	/**
	 * 
	 */
	private final BPMN2MultiPageEditor2 multiPageEditor;

	/**
	 * @param multiPageEditor
	 */
	SourceViewer(BPMN2MultiPageEditor2 multiPageEditor) {
		this.multiPageEditor = multiPageEditor;
	}

	ActionRegistry actionRegistry = null;
	
	@Override
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class required) {
		if (required==ActionRegistry.class)
			return getActionRegistry();
		if (required==BPMN2Editor.class || required==DiagramEditor.class)
			return multiPageEditor.getDesignEditor();
		return super.getAdapter(required);
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	protected ActionRegistry getActionRegistry() {
		if (actionRegistry == null)
			actionRegistry = new ActionRegistry();
		return actionRegistry;
	}
}