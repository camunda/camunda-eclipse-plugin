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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.camunda.bpm.modeler.core.model.Bpmn2ModelerResourceSetImpl;
import org.camunda.bpm.modeler.ui.diagram.editor.Bpmn2Editor;
import org.camunda.bpm.modeler.ui.diagram.editor.Bpmn2DiagramEditorInput;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.FileEditorInput;

public class FileService {

	public static TransactionalEditingDomain createEmfFileForDiagram(URI diagramResourceUri, final Diagram diagram, Bpmn2Editor diagramEditor) {

		ResourceSet resourceSet = null;
		TransactionalEditingDomain editingDomain = null;
		if (diagramEditor==null) {
			// Create a resource set and EditingDomain
			resourceSet = new Bpmn2ModelerResourceSetImpl();
			editingDomain = TransactionUtil.getEditingDomain(resourceSet);
			if (editingDomain == null) {
				// Not yet existing, create one
				editingDomain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain(resourceSet);
			}
		}
		else {
			editingDomain = diagramEditor.getEditingDomain();
			resourceSet = diagramEditor.getResourceSet();
		}
		
		// Create a resource for this file.
		final Resource resource = resourceSet.createResource(diagramResourceUri);
		CommandStack commandStack = editingDomain.getCommandStack();
		commandStack.execute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				resource.setTrackingModification(true);
				resource.getContents().add(diagram);

			}
		});

		save(editingDomain, Collections.<Resource, Map<?, ?>> emptyMap());
		return editingDomain;
	}

	private static void save(TransactionalEditingDomain editingDomain, Map<Resource, Map<?, ?>> options) {
		saveInWorkspaceRunnable(editingDomain, options);
	}

	private static void saveInWorkspaceRunnable(final TransactionalEditingDomain editingDomain,
			final Map<Resource, Map<?, ?>> options) {

		final Map<URI, Throwable> failedSaves = new HashMap<URI, Throwable>();
		final IWorkspaceRunnable wsRunnable = new IWorkspaceRunnable() {
			@Override
			public void run(final IProgressMonitor monitor) throws CoreException {

				final Runnable runnable = new Runnable() {

					@Override
					public void run() {
						Transaction parentTx;
						if (editingDomain != null
								&& (parentTx = ((TransactionalEditingDomainImpl) editingDomain).getActiveTransaction()) != null) {
							do {
								if (!parentTx.isReadOnly()) {
									throw new IllegalStateException(
											"FileService.save() called from within a command (likely produces a deadlock)"); //$NON-NLS-1$
								}
							} while ((parentTx = ((TransactionalEditingDomainImpl) editingDomain)
									.getActiveTransaction().getParent()) != null);
						}

						final EList<Resource> resources = editingDomain.getResourceSet().getResources();
						// Copy list to an array to prevent
						// ConcurrentModificationExceptions
						// during the saving of the dirty resources
						Resource[] resourcesArray = new Resource[resources.size()];
						resourcesArray = resources.toArray(resourcesArray);
						final Set<Resource> savedResources = new HashSet<Resource>();
						for (int i = 0; i < resourcesArray.length; i++) {
							// In case resource modification tracking is
							// switched on, we can check if a resource
							// has been modified, so that we only need to same
							// really changed resources; otherwise
							// we need to save all resources in the set
							final Resource resource = resourcesArray[i];
							if (resource.isModified()) {
								try {
									resource.save(options.get(resource));
									savedResources.add(resource);
								} catch (final Throwable t) {
									failedSaves.put(resource.getURI(), t);
								}
							}
						}
					}
				};

				try {
					editingDomain.runExclusive(runnable);
				} catch (final InterruptedException e) {
					throw new RuntimeException(e);
				}
				editingDomain.getCommandStack().flush();
			}
		};
		try {
			ResourcesPlugin.getWorkspace().run(wsRunnable, null);
			if (!failedSaves.isEmpty()) {
				throw new WrappedException(createMessage(failedSaves), new RuntimeException());
			}
		} catch (final CoreException e) {
			final Throwable cause = e.getStatus().getException();
			if (cause instanceof RuntimeException) {
				throw (RuntimeException) cause;
			}
			throw new RuntimeException(e);
		}
	}

	private static String createMessage(Map<URI, Throwable> failedSaves) {
		final StringBuilder buf = new StringBuilder("The following resources could not be saved:");
		for (final Entry<URI, Throwable> entry : failedSaves.entrySet()) {
			buf.append("\nURI: ").append(entry.getKey().toString()).append(", cause: \n")
					.append(getExceptionAsString(entry.getValue()));
		}
		return buf.toString();
	}

	private static String getExceptionAsString(Throwable t) {
		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		t.printStackTrace(printWriter);
		final String result = stringWriter.toString();
		try {
			stringWriter.close();
		} catch (final IOException e) {
			// $JL-EXC$ ignore
		}
		printWriter.close();
		return result;
	}

	public static InputStream getInputContents(IEditorInput input) {
		try {
			if (input instanceof Bpmn2DiagramEditorInput) {
				URI uri = getInputUri(input);
				String fileName = null;
				if (uri.isFile())
					fileName = uri.toFileString();
				else if (uri.isPlatformResource())
					fileName = uri.toPlatformString(false);
				
				File file = new File(fileName);
				if (file.exists()) {
					InputStream is = new FileInputStream(file);
					return is;
				}
			} else if (input instanceof FileEditorInput) {
				return ((FileEditorInput) input).getFile().getContents();
			} else if (input instanceof IStorageEditorInput) {
				return ((IStorageEditorInput) input).getStorage().getContents();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static URI getInputUri(IEditorInput input) {
		if (input instanceof Bpmn2DiagramEditorInput) {
			URI uri = ((Bpmn2DiagramEditorInput) input).getModelUri();
			return uri.trimFragment();
		} else if (input instanceof FileEditorInput) {
			IPath path = ((FileEditorInput) input).getFile().getFullPath();
			return URI.createPlatformResourceURI(path.toString(), true);
		} else if (input instanceof IStorageEditorInput) {
			IStorageEditorInput sei = (IStorageEditorInput) input;

			try {
				IPath path = sei.getStorage().getFullPath();
				if (path!=null) {
					return URI.createPlatformResourceURI(path.toString(), true);
				}
				
				// the input is not a local file. Create a temp file and copy its contents
				String name = sei.getStorage().getName();
				InputStream istream;
				
				istream = sei.getStorage().getContents();
	
				File file = createTempFile(name, istream);
				return URI.createFileURI(file.getPath());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (input instanceof FileStoreEditorInput) {
			return URI.createURI(((FileStoreEditorInput) input).getURI().toString());
		} else if (input instanceof DiagramEditorInput) {
			return ((DiagramEditorInput) input).getUri();
		}
		return null;
	}
	
	public static String createTempName(String name) {
		String tempDir = System.getProperty("java.io.tmpdir");
		String tempName = tempDir + File.separatorChar + name + "." + EcoreUtil.generateUUID();
		return tempName;
	}
	
	public static File createTempFile(String name, InputStream istream) {
		File tempFile = null;
		try {
			tempFile = File.createTempFile(name, ".bpmn");
			if (istream!=null) {
				OutputStream ostream = new FileOutputStream(tempFile);
	
				int read = 0;
				byte[] bytes = new byte[1024];
	
				while ((read = istream.read(bytes)) != -1) {
					ostream.write(bytes, 0, read);
				}
	
				istream.close();

				ostream.flush();
				ostream.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempFile;
	}
}
