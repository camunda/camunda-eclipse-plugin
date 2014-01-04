package org.camunda.bpm.modeler.test.diagram;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Fail.fail;

import java.io.ByteArrayInputStream;
import java.io.File;

import org.camunda.bpm.modeler.core.files.FileService;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.junit.Test;


/**
 * Testing file service, one, two.
 * 
 * @author nico.rehwaldt
 */
public class FileServiceTest {
	
	public String getWorkspaceLocation() {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		assertThat(workspace).isNotNull();
		
		IWorkspaceRoot root = workspace.getRoot();
		return root.getRawLocationURI().toString();
	}
	
	@Test
	public void shouldHandleWorkspaceFile_NonExisting() throws Exception {
		
		URI fileUri = URI.createURI(getWorkspaceLocation() + "/SomeProject/some-file.txt");
		URI expectedUri = URI.createURI("platform:/resource/SomeProject/some-file.txt");
		
		URI resolvedUri = FileService.resolveAsWorkspaceResource(fileUri);
		
		// no exists check is performed on workspace local files
		assertThat(resolvedUri).isEqualTo(expectedUri);
	}
	
	@Test
	public void shouldHandleExternalFile_NonExisting() throws Exception {
		URI fileUri = URI.createURI("file:/C:/some-non-existing-file.txt");
		
		try {
			FileService.resolveAsWorkspaceResource(fileUri);

			fail("expected exception (not found)");
		} catch (CoreException e) {
			assertThat(FileService.isNotFound(e)).isTrue();
		}
	}
	
	@Test
	public void shouldHandleWorkspaceFile() throws Exception {
		
		String path = "/FOO/asdf";
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
		IProject project = root.getProject("FOO");
		project.create(null);
		project.open(null);
		
		IFile file = root.getFile(new Path(path));
		file.create(new ByteArrayInputStream("FOO".getBytes("UTF-8")), true, null);
		
		try {
			URI platformUri = URI.createPlatformResourceURI(path, true);
			URI resolvedUri = FileService.resolveAsWorkspaceResource(platformUri);
			
			assertThat(platformUri).isEqualTo(resolvedUri);
		} finally {
			project.delete(true, true, null);
		}
	}
	
	@Test
	public void shouldHandleExternalFile() throws Exception {
		File f = File.createTempFile("empty", null);
		
		try {
			URI externalUri = URI.createURI(f.toURI().toString());
			URI resolvedUri = FileService.resolveAsWorkspaceResource(externalUri);

			URI expectedUri = URI.createURI("platform:/resource/.internal/" + FileService.getLinkName(externalUri));
			
			assertThat(resolvedUri).isEqualTo(expectedUri);
		} finally {
			f.delete();
		}
	}
	
	@Test
	public void shouldTransformLinkNames() {
		assertThat(FileService.getLinkName(URI.createURI("file:/E:/FOO/bar.txt"))).isEqualTo("E.FOO.bar.txt");
	}
}
