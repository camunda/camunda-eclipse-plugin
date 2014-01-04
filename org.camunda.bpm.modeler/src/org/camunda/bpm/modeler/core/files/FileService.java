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
package org.camunda.bpm.modeler.core.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.ui.diagram.editor.Bpmn2DiagramEditorInput;
import org.eclipse.core.internal.resources.ProjectDescription;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.FileEditorInput;

/**
 * A utility class that provides file related services to the modeler.
 * 
 * @author nico.rehwaldt
 */
@SuppressWarnings("restriction")
public class FileService {

	private static String INTERNAL_PROJECT_NAME = ".internal";
	
	private static final IProgressMonitor NULL_PROGRESS_MONITOR = new NullProgressMonitor();
	
	/**
	 * Create a diagram resource with a given {@link Diagram}
	 * in the context of a {@link TransactionalEditingDomain}.
	 * 
	 * @param uri
	 * @param diagram
	 * @param editingDomain
	 * 
	 * @return
	 */
	public static Resource createDiagramResource(final URI uri, final Diagram diagram, final TransactionalEditingDomain editingDomain) {

		final ResourceSet resourceSet = editingDomain.getResourceSet();
		
		final Resource resource  = resourceSet.createResource(uri);
		
		CommandStack commandStack = editingDomain.getCommandStack();
		commandStack.execute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				// resource.setTrackingModification(true);
				resource.getContents().add(diagram);
			}
		});
		
		try {
			resource.save(null);
		} catch (IOException e) {
			throw new IllegalStateException("Failed to create diagram resource", e);
		}
		
		return resource;
	}

	/**
	 * Resolve an {@link IEditorInput} as a workspace local {@link URI}. 
	 * 
	 * This may involve linking the file to the workspace. 
	 * 
	 * @param input
	 * @return
	 * @throws CoreException
	 */
	public static URI resolveInWorkspace(IEditorInput input) throws CoreException {
		URI uri = getInputUri(input);
		return resolveAsWorkspaceResource(uri);
	}

	public static URI resolveAsWorkspaceResource(URI uri) throws CoreException {
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		
		URI workspaceUri = null;
		
		if (uri.isFile()) {
			workspaceUri = linkAsPlatformResource(uri, workspace);
		} else {
			workspaceUri = resolveInWorkspace(uri, workspace);
		}
		
		return workspaceUri;
	}
	
	private static URI resolveInWorkspace(URI uri, IWorkspace workspace) throws ResourceException {
		return uri;
	}

	private static URI linkAsPlatformResource(URI fileUri, IWorkspace workspace) throws CoreException {
		
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		
		String filePath = fileUri.toString();
		String workspacePath = workspaceRoot.getLocationURI().toString();
		
		// check for workspace relative file
		// (can be loaded into workspace directly)
		if (filePath.startsWith(workspacePath)) {
			String relativePath = filePath.substring(workspacePath.length());
			
			return resolveInWorkspace(URI.createPlatformResourceURI(relativePath, true), workspace);
		}
		
		IProject internalProject = getOrCreateInternalProject(workspaceRoot);
		
		String linkName = getLinkName(fileUri);
		
		IFile file = internalProject.getFile(linkName);
		
		if (!file.exists()) {
			// create link
			file.createLink(new Path(fileUri.toFileString()), IResource.HIDDEN, NULL_PROGRESS_MONITOR);
		}

		return URI.createPlatformResourceURI(file.getFullPath().toPortableString(), true);
	}

	public static String getLinkName(URI fileUri) {
		
		List<String> parts = new ArrayList<String>();
		
		parts.add(fileUri.device());
		
		for (String segment: fileUri.segments()) {
			parts.add(segment);
		}
		
		StringBuilder builder = new StringBuilder();
		
		for (String part: parts) {
			if (builder.length() > 0) {
				builder.append(".");
			}
			
			builder.append(part.replaceAll("[\\:]+", ""));
		}
		
		return builder.toString();
	}

	/**
	 * Returns true if the given exception represents a
	 * not found exception.
	 * 
	 * @param e
	 * 
	 * @return
	 */
	public static boolean isNotFound(CoreException e) {
		return IResourceStatus.NOT_FOUND_LOCAL == e.getStatus().getCode();
	}

	private static IProject getOrCreateInternalProject(IWorkspaceRoot root) throws CoreException {
		final IProject project = root.getProject(INTERNAL_PROJECT_NAME);

		if (!project.exists()) {
			project.create(createDescription(project), IResource.HIDDEN, NULL_PROGRESS_MONITOR);
		}

		if (!project.isOpen()) {
			project.open(NULL_PROGRESS_MONITOR);
		}
		
		return project;
	}

	private static IProjectDescription createDescription(IProject project) {
		ProjectDescription description = new ProjectDescription();
		
		description.setName(project.getName());
		description.setComment("BPMN 2.0 modeler internal project that links non-workspace resources");
		
		return description;
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
