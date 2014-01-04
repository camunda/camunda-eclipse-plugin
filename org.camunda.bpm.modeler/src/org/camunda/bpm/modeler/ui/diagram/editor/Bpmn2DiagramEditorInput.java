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
package org.camunda.bpm.modeler.ui.diagram.editor;

import org.camunda.bpm.modeler.core.files.FileService;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;

public final class Bpmn2DiagramEditorInput extends DiagramEditorInput {

	private URI modelUri;
	
	public Bpmn2DiagramEditorInput(URI modelUri, URI diagramUri, String providerId) {
		super(diagramUri, providerId);
		
		this.modelUri = modelUri;
	}

	public URI getModelUri() {
		return modelUri;
	}
	
	/**
	 * We are using the original URI field to store the graphiti diagram uri
	 * @return
	 */
	public URI getDiagramUri() {
		return getUri();
	}
	
	@Override
	public String getToolTipText() {
		return modelUri.isPlatformResource() ? modelUri.toPlatformString(true) : modelUri.toFileString();
	}
	
	@Override
	public String getName() {
		return URI.decode(modelUri.trimFileExtension().lastSegment());
	}
	
	public void updateDiagramUri(URI diagramUri) {
		super.updateUri(diagramUri);
	}
	
	@Override
	public void updateUri(URI uri) {
		updateModelUri(uri);
	}
	
	public void updateModelUri(URI modelUri) {
		this.modelUri = modelUri;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		}

		if (obj instanceof IEditorInput) {
			URI otherModelUri = FileService.getInputUri((IEditorInput) obj);
			return equalsModelUri(otherModelUri);
		}
	
		return false;
	}

	private boolean equalsModelUri(URI otherUri) {
		if (otherUri == null) {
			return false;
		}
		
		String modelRef = toAbsoluteRef(modelUri);
		String otherRef = toAbsoluteRef(otherUri);
		
		// handle the error case that any of the uris could
		// not be transformed into absolute refs
		if (modelRef == null || otherRef == null) {
			return false;
		}
		
		return modelRef.equals(otherRef);
	}

	private static String toAbsoluteRef(URI uri) {
		
		if (uri.isFile()) {
			return uri.toFileString();
		}
		
		if (uri.isPlatformResource()) {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceRoot root = workspace.getRoot();
			
			IResource member = root.findMember(uri.toPlatformString(true));
			if (member != null) {
				return member.getLocation().toOSString();
			}
		}
		
		return null;
	}
	
	@Override
	public void saveState(IMemento memento) {
		super.saveState(memento);
		
		/**
		 * We are storing the modelUri in the uri field of the DiagramEditorInput
		 */
		memento.putString(KEY_URI, modelUri.toString());
	}
}