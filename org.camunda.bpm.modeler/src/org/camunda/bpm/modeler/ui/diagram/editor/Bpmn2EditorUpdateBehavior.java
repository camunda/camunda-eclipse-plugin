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
 * @author Bob Brodt
 ******************************************************************************/

package org.camunda.bpm.modeler.ui.diagram.editor;

import org.camunda.bpm.modeler.core.model.Bpmn2ModelerResourceSetImpl;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.core.commands.operations.DefaultOperationHistory;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.emf.workspace.IWorkspaceCommandStack;
import org.eclipse.emf.workspace.WorkspaceEditingDomainFactory;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer.Delegate;
import org.eclipse.graphiti.ui.editor.DefaultUpdateBehavior;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.internal.editor.GFWorkspaceCommandStackImpl;

/**
 * This overrides the DefaultUpdateBehavior provider class from Graphiti. This
 * is necessary because we want to provide our own ResourceSet implementation
 * instead of being forced to deal with the default ResourceSetImpl. See
 * {@link createResourceSetAndEditingDomain()} for details.
 * 
 * @author Bob Brodt
 * @author nico.rehwaldt
 */
@SuppressWarnings("restriction")
public class Bpmn2EditorUpdateBehavior extends DefaultUpdateBehavior {

	// need to save the delegate to expose it
	protected Delegate workspaceSynchronizerDelegate;
	
	public Bpmn2EditorUpdateBehavior(DiagramEditor diagramEditor) {
		super(diagramEditor);
	}
	
	@Override
	public void handleActivate() {
		
		// update the change flag in case external file
		// changes occured
		if (!isResourceChanged() && externalFileChange()) {
			setResourceChanged(true);
		}
		
		super.handleActivate();
	}

	/**
	 * Return true if an external file changes happened
	 * to the model file, either from external programs
	 * or a source editor
	 * 
	 * @return
	 */
	private boolean externalFileChange() {
		Bpmn2Editor editor = getEditor();
		
		Bpmn2Resource modelResource = editor.getModelResource();
		IFile modelFile = editor.getModelFile();
		
		long fileTimeStamp = modelFile.getLocalTimeStamp();
		long modelTimeStamp = modelResource.getTimeStamp();
		
		return fileTimeStamp > modelTimeStamp;
	}

	protected Bpmn2Editor getEditor() {
		return (Bpmn2Editor) diagramEditor;
	}

	@Override
	public void createEditingDomain() {
		TransactionalEditingDomain editingDomain = createResourceSetAndEditingDomain();
		initializeEditingDomain(editingDomain);
	}
	
	/**
	 * Return the workspace synchronizer
	 * 
	 * @return
	 */
	public Delegate getWorkspaceSynchronizerDelegate() {
		return workspaceSynchronizerDelegate;
	}
	
	public TransactionalEditingDomain createResourceSetAndEditingDomain() {
		// Argh!! This is the ONLY line of code that actually differs
		// (significantly) from
		// the Graphiti EMF Service. Here we want to substitute our own
		// Bpmn2ModelerResourceSetImpl instead of using a ResourceSetImpl.
		final ResourceSet resourceSet = new Bpmn2ModelerResourceSetImpl();
		final IWorkspaceCommandStack workspaceCommandStack = new GFWorkspaceCommandStackImpl(new DefaultOperationHistory());

		final TransactionalEditingDomainImpl editingDomain = new TransactionalEditingDomainImpl(
				new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE),
				workspaceCommandStack, resourceSet);
		
		WorkspaceEditingDomainFactory.INSTANCE.mapResourceSet(editingDomain);
		return editingDomain;
	}
}
