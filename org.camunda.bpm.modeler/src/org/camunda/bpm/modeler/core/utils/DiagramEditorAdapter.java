package org.camunda.bpm.modeler.core.utils;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.editor.DiagramEditor;

/**
 * Returns an adapter that associates each {@link EObject} with the
 * responding {@link DiagramEditor} it is displayed in.
 * 
 * @author nico.rehwaldt
 */
public class DiagramEditorAdapter implements Adapter {

	private final DiagramEditor editor;

	/**
	 * @param editor
	 */
	public DiagramEditorAdapter(DiagramEditor editor) {
		this.editor = editor;
	}

	public Notifier getTarget() {
		return null;
	}

	public void setTarget(Notifier newTarget) {
	}

	public void notifyChanged(Notification notification) {
	}

	public boolean isAdapterForType(Object type) {
		return type == DiagramEditorAdapter.class || type == DiagramEditor.class;
	}

	public DiagramEditor getDiagramEditor() {
		return this.editor;
	}
}