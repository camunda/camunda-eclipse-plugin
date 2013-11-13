package org.camunda.bpm.modeler.core;

import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class Bpmn2TabbedPropertySheetPage extends TabbedPropertySheetPage {

	DiagramEditor diagramEditor;
	
	public Bpmn2TabbedPropertySheetPage(
			ITabbedPropertySheetPageContributor tabbedPropertySheetPageContributor) {
		super(tabbedPropertySheetPageContributor);
		diagramEditor = (DiagramEditor)tabbedPropertySheetPageContributor;
	}

	public DiagramEditor getDiagramEditor() {
		return diagramEditor;
	}
	
	@Override
	public void refresh() {
		super.refresh();
	}
	
	@Override
	public void resizeScrolledComposite() {
		// TODO FIX SCROLL SPEED
		super.resizeScrolledComposite();
	}
	
	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		
		Display.getCurrent().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				Bpmn2TabbedPropertySheetPage.super.selectionChanged(part, selection);
			}
		});
	}
}
