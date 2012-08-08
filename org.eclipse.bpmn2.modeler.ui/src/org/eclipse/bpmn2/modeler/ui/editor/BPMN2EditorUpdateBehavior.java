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

package org.eclipse.bpmn2.modeler.ui.editor;

import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerResourceSetImpl;
import org.eclipse.core.commands.operations.DefaultOperationHistory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.emf.workspace.IWorkspaceCommandStack;
import org.eclipse.emf.workspace.WorkspaceEditingDomainFactory;
import org.eclipse.graphiti.ui.editor.DefaultUpdateBehavior;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.internal.editor.GFWorkspaceCommandStackImpl;

/**
 * This overrides the DefaultUpdateBehavior provider class from Graphiti. This is necessary
 * because we want to provide our own ResourceSet implementation instead of being forced to
 * deal with the default ResourceSetImpl. See  createResourceSetAndEditingDomain() for details.
 * 
 * @author Bob Brodt
 *
 */
public class BPMN2EditorUpdateBehavior extends DefaultUpdateBehavior {
	
		private TransactionalEditingDomain editingDomain;

		/**
		 * @param diagramEditor
		 */
		public BPMN2EditorUpdateBehavior(DiagramEditor diagramEditor) {
			super(diagramEditor);
		}
		
		public TransactionalEditingDomain getEditingDomain() {
			if (editingDomain==null)
				createEditingDomain();
			return editingDomain;
		}

		@Override
		public void createEditingDomain() {
			if (editingDomain==null) {
//			TransactionalEditingDomain editingDomain = GraphitiUiInternal.getEmfService().createResourceSetAndEditingDomain();
				editingDomain = createResourceSetAndEditingDomain();
				initializeEditingDomain(editingDomain);
			}
		}
		
		public TransactionalEditingDomain createResourceSetAndEditingDomain() {
			// Argh!! This is the ONLY line of code that actually differs (significantly) from
			// the Graphiti EMF Service. Here we want to substitute our own Bpmn2ModelerResourceSetImpl
			// instead of using a ResourceSetImpl.
			final ResourceSet resourceSet = new Bpmn2ModelerResourceSetImpl();
			final IWorkspaceCommandStack workspaceCommandStack = new GFWorkspaceCommandStackImpl(new DefaultOperationHistory());
		
			final TransactionalEditingDomainImpl editingDomain = new TransactionalEditingDomainImpl(new ComposedAdapterFactory(
					ComposedAdapterFactory.Descriptor.Registry.INSTANCE), workspaceCommandStack, resourceSet);
			WorkspaceEditingDomainFactory.INSTANCE.mapResourceSet(editingDomain);
			return editingDomain;
		}

	}