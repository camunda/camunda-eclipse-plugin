package org.camunda.bpm.modeler.ui.views.outline;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.parts.TreeViewer;

public class DiagramTreeViewer extends TreeViewer implements EditPartViewer {

	private boolean dispatching;

	@Override
	protected void fireSelectionChanged() {
		if (!dispatching) {
			super.fireSelectionChanged();
		}
	}
	
	public void setDispatching(boolean dispatching) {
		this.dispatching = dispatching;
	}
}
