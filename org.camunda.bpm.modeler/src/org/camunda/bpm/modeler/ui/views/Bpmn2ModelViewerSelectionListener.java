/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *
 * @author Innar Made
 ******************************************************************************/
package org.camunda.bpm.modeler.ui.views;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.ui.diagram.editor.BpmnEditor;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

class Bpmn2ModelViewerSelectionListener implements ISelectionListener {
	private final ViewContentProvider contentProvider;
	private BpmnEditor editor;
	private final TreeViewer viewer;

	public Bpmn2ModelViewerSelectionListener(TreeViewer viewer) {
		this.viewer = viewer;
		this.contentProvider = (ViewContentProvider) viewer.getContentProvider();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		Object bpmn2Editor = part.getAdapter(BpmnEditor.class);
		if (bpmn2Editor instanceof BpmnEditor) {
			editor = (BpmnEditor)bpmn2Editor;
			
			ModelHandler modelHandler = ModelHandler.getInstance(getDiagram());
			contentProvider.updateModel(modelHandler);
			viewer.refresh(true);
		}
		
		Object[] selected = contentProvider.getSelected(selection);
		if (selected != null) {
			viewer.setSelection(new StructuredSelection(selected), true);
		}
	}
	
	private Diagram getDiagram() {
		return editor.getDiagramTypeProvider().getDiagram();
	}
}
