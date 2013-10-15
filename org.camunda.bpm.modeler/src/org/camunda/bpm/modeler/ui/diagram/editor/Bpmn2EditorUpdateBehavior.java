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
import org.eclipse.core.commands.operations.DefaultOperationHistory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.emf.workspace.IWorkspaceCommandStack;
import org.eclipse.emf.workspace.WorkspaceEditingDomainFactory;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.graphiti.ui.editor.DefaultUpdateBehavior;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.graphiti.ui.internal.editor.GFWorkspaceCommandStackImpl;

/**
 * This overrides the DefaultUpdateBehavior provider class from Graphiti. This
 * is necessary because we want to provide our own ResourceSet implementation
 * instead of being forced to deal with the default ResourceSetImpl. See
 * createResourceSetAndEditingDomain() for details.
 * 
 * @author Bob Brodt
 * 
 */
public class Bpmn2EditorUpdateBehavior extends DefaultUpdateBehavior {

	private TransactionalEditingDomain editingDomain;
	private WorkspaceSynchronizer workspaceSynchronizer;

	/**
	 * @param diagramBehavior
	 */
	public Bpmn2EditorUpdateBehavior(DiagramBehavior diagramBehavior) {
		super(diagramBehavior);
	}
	
	public TransactionalEditingDomain getEditingDomain() {
		if (editingDomain == null)
			createEditingDomain();
		return editingDomain;
	}

	@Override
	public void createEditingDomain() {
		if (editingDomain == null) {
			editingDomain = createResourceSetAndEditingDomain();
			initializeEditingDomain(editingDomain);
		}
	}

	public TransactionalEditingDomain createResourceSetAndEditingDomain() {
		// Argh!! This is the ONLY line of code that actually differs
		// (significantly) from
		// the Graphiti EMF Service. Here we want to substitute our own
		// Bpmn2ModelerResourceSetImpl instead of using a ResourceSetImpl.
		final ResourceSet resourceSet = new Bpmn2ModelerResourceSetImpl();
		final IWorkspaceCommandStack workspaceCommandStack = new GFWorkspaceCommandStackImpl(
				new DefaultOperationHistory());

		final TransactionalEditingDomainImpl editingDomain = new TransactionalEditingDomainImpl(
				new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE),
				workspaceCommandStack, resourceSet);
		WorkspaceEditingDomainFactory.INSTANCE.mapResourceSet(editingDomain);
		return editingDomain;
	}
	
	protected void initializeEditingDomain(TransactionalEditingDomain domain) {
		// we want first crack at these notifications!
		workspaceSynchronizer = new WorkspaceSynchronizer(getEditingDomain(),
				new BPMN2EditorWorkspaceSynchronizerDelegate(diagramBehavior));
		super.initializeEditingDomain(domain);
	}
	
	public void dispose() {
		super.dispose();
		workspaceSynchronizer.dispose();
	}
	
	public class BPMN2EditorWorkspaceSynchronizerDelegate implements WorkspaceSynchronizer.Delegate {

		private Bpmn2Editor bpmnEditor;

		/**
		 * The DiagramEditorBehavior reacts on a setResourceChanged(true) if he gets
		 * activated.
		 */
		public BPMN2EditorWorkspaceSynchronizerDelegate(DiagramBehavior diagramBehavior) {
			this.bpmnEditor = (Bpmn2Editor)diagramBehavior.getDiagramContainer();
		}

		public void dispose() { 
			bpmnEditor = null;
		}

		public boolean handleResourceChanged(Resource resource) {
			return bpmnEditor.handleResourceChanged(resource);
		}

		public boolean handleResourceDeleted(Resource resource) {
			return bpmnEditor.handleResourceDeleted(resource);
		}

		public boolean handleResourceMoved(Resource resource, URI newURI) {
			return bpmnEditor.handleResourceMoved(resource, newURI);
		}

	}}