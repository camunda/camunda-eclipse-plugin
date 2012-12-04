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
package org.eclipse.bpmn2.modeler.ui.wizards;

import org.eclipse.bpmn2.modeler.core.utils.ErrorUtils;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class BPMN2DiagramCreator {

	public static Bpmn2DiagramEditorInput createDiagram(URI uri, Bpmn2DiagramType diagramType, String targetNamespace) throws CoreException {
		return createDiagram(uri, diagramType, targetNamespace, null);
	}

	public static Bpmn2DiagramEditorInput createDiagram(URI modelUri, Bpmn2DiagramType diagramType, String targetNamespace, BPMN2Editor diagramEditor) throws CoreException {

		if (modelUri == null) {
			throw new RuntimeException("Unresolvable model uri; Please clean workspace");
		}
		
		String modelName = modelUri.trimFragment().trimFileExtension().lastSegment();
		final Diagram diagram = Graphiti.getPeCreateService().createDiagram("BPMN2", modelName, true);

		String diagramName = FileService.createTempName(modelName);
		URI diagramUri = URI.createFileURI(diagramName);
		TransactionalEditingDomain domain = FileService.createEmfFileForDiagram(diagramUri, diagram, diagramEditor);

		String providerId = GraphitiUi.getExtensionManager().getDiagramTypeProviderId(diagram.getDiagramTypeId());
		final Bpmn2DiagramEditorInput editorInput = new Bpmn2DiagramEditorInput(modelUri, diagramUri, domain, providerId);
		editorInput.setInitialDiagramType(diagramType);
		editorInput.setTargetNamespace(targetNamespace);

		if (diagramEditor == null) {
			openEditor(editorInput);
		}

		return editorInput;
	}

	private static void openEditor(final DiagramEditorInput editorInput) {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
							.openEditor(editorInput, BPMN2Editor.EDITOR_ID);

				} catch (PartInitException e) {
					String error = "Error while opening diagram editor";
					IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, error, e);
					ErrorUtils.showErrorWithLogging(status);
				}
			}
		});
	}
}
