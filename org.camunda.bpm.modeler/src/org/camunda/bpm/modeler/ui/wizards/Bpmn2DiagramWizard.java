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
package org.camunda.bpm.modeler.ui.wizards;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.core.utils.ErrorUtils;
import org.camunda.bpm.modeler.ui.diagram.editor.Bpmn2Editor;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class Bpmn2DiagramWizard extends Wizard implements INewWizard {
	private Bpmn2DiagramWizardPage page2;
	private ISelection selection;

	/**
	 * Constructor for BPMN2DiagramWizard.
	 */
	public Bpmn2DiagramWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page2 to the wizard.
	 */

	@Override
	public void addPages() {
		page2 = new Bpmn2DiagramWizardPage(selection);
		addPage(page2);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We will create an operation and run it using
	 * wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		final String fileName = page2.getFileName();
		final IResource container = page2.getDiagramContainer();

		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					IPath path = container.getFullPath().append(fileName);
					URI diagramUri = URI.createPlatformResourceURI(path.toString(), true);
					
					// create the diagram
					final Bpmn2Resource bpmn2Resource = Bpmn2DiagramCreator.createBpmn2Resource(diagramUri);

					ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
						@Override
						public void run(IProgressMonitor monitor) throws CoreException {
							try {
								bpmn2Resource.save(null);
							} catch (IOException e) {
								throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Failed to save file", e));
							}
						}
					}, monitor);
					
					// and locate + open it as a file editor input
					// to prevent opening it twice upon double click in workspace
					IWorkspace workspace = container.getWorkspace();
					IWorkspaceRoot root = workspace.getRoot();
					
					IFile diagramFile = root.getFile(path);
					
					openEditor(new FileEditorInput(diagramFile));
				} catch (CoreException e) {
					Activator.logError(e);
					MessageDialog.openError(getShell(), "Failed to create diagram", e.getMessage());
				} finally {
					monitor.done();
				}
			}
		};
		
		try {
			getContainer().run(true, false, runnable);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		
		return true;
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	public static void openEditor(final IEditorInput editorInput) {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					activePage.openEditor(editorInput, Bpmn2Editor.EDITOR_ID);
					
					/**
					 * open the properties view or bring it to the top, so the use can start modeling immediately
					 */
					IViewPart propSheet = activePage.findView(IPageLayout.ID_PROP_SHEET);
					
					if (propSheet != null) { 
						activePage.bringToTop(propSheet); 
					} else {
						activePage.showView(IPageLayout.ID_PROP_SHEET);
					}

				} catch (PartInitException e) {
					String error = "Error while opening diagram editor";
					IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, error, e);
					ErrorUtils.showErrorWithLogging(status);
				}
			}
		});
	}
}