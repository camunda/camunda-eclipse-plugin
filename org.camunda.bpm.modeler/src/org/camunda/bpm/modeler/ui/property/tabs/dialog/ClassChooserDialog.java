package org.camunda.bpm.modeler.ui.property.tabs.dialog;

import java.util.ArrayList;
import java.util.Arrays;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.ui.diagram.editor.BpmnEditor;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * Helper for a simple class chooser
 * which allows a user to select a java delegate.
 * 
 * @author nico.rehwaldt
 */
public class ClassChooserDialog {

	private Shell shell;

	public ClassChooserDialog(Shell shell) {
		this.shell = shell;
	}

	
	public String chooseClass() {
		SelectionDialog dialog;
		
		try {
			dialog = JavaUI.createTypeDialog(shell, 
				new ProgressMonitorDialog(shell),
				SearchEngine.createWorkspaceScope(), 
				IJavaElementSearchConstants.CONSIDER_CLASSES, false);
		} catch (Exception e) {
			Activator.logError(e);
			
			return null;
		}

		if (dialog.open() != SelectionDialog.OK) {
			return null;
		}
		
		Object[] result = dialog.getResult();
		String clazz = ((IType) result[0]).getFullyQualifiedName();
		IJavaProject containerProject = ((IType) result[0]).getJavaProject();

		DiagramEditor diagramEditor = BpmnEditor.getActiveEditor();

		IProject currentProject = getProjectFromDiagram(diagramEditor.getDiagramTypeProvider().getDiagram());
		
		try {
			doProjectReferenceChange(currentProject, containerProject, clazz);
		} catch (Exception e) {
			return null;
		}
		
		return clazz;
	}
	
	/**
	 * SIMPLY COPIED; 
	 * 
	 * @param diagram
	 * @return
	 */
	private IProject getProjectFromDiagram(Diagram diagram) {
		IProject currentProject = null;
		Resource resource = diagram.eResource();

		URI uri = resource.getURI();
		URI uriTrimmed = uri.trimFragment();

		if (uriTrimmed.isPlatformResource()) {
			String platformString = uriTrimmed.toPlatformString(true);
			IResource fileResource = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(platformString);

			if (fileResource != null) {
				currentProject = fileResource.getProject();
			}
		} else {
			IResource fileResource = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(uriTrimmed.toString());

			if (fileResource != null) {
				currentProject = fileResource.getProject();
			}
		}

		return currentProject;
	}
	
	/**
	 * SIMPLY COPIED; 
	 * 
	 * @param currentProject
	 * @param containerProject
	 * @param className
	 * @throws CoreException
	 */
	private void doProjectReferenceChange(IProject currentProject, IJavaProject containerProject, String className) throws CoreException {
		if (currentProject == null || currentProject.equals(containerProject.getProject())) {
			return;
		}

		IProjectDescription desc = currentProject.getDescription();
		IProject[] refs = desc.getReferencedProjects();
		IProject[] newRefs = new IProject[refs.length + 1];
		System.arraycopy(refs, 0, newRefs, 0, refs.length);
		newRefs[refs.length] = containerProject.getProject();
		desc.setReferencedProjects(newRefs);
		currentProject.setDescription(desc, new NullProgressMonitor());

		IPath dependsOnPath = containerProject.getProject().getFullPath();

		IJavaProject javaProject = (IJavaProject) currentProject
				.getNature(JavaCore.NATURE_ID);
		IClasspathEntry prjEntry = JavaCore
				.newProjectEntry(dependsOnPath, true);

		boolean dependsOnPresent = false;
		for (IClasspathEntry cpEntry : javaProject.getRawClasspath()) {
			if (cpEntry.equals(prjEntry)) {
				dependsOnPresent = true;
			}
		}

		if (!dependsOnPresent) {
			IClasspathEntry[] entryList = new IClasspathEntry[1];
			entryList[0] = prjEntry;
			ArrayList<IClasspathEntry> newEntries = new ArrayList<IClasspathEntry>();
			newEntries.addAll(Arrays.asList(javaProject.getRawClasspath()));
			newEntries.addAll(Arrays.asList(entryList));
			javaProject.setRawClasspath(newEntries.toArray(new IClasspathEntry[0]), null);
		}
	}
}
