package org.camunda.bpm.modeler.ui.diagram.editor;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.core.files.FileService;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

/**
 * A multi page editor for BPMN 2.0 files showing the diagram file
 * and the (editable) xml source.
 * 
 * This editor is able to handle all kinds of {@link IEditorInput} that
 * have a resource uri and may be converted to {@link FileEditorInput}.
 * 
 * It shows the BPMN editors part name and tooltip and displays the dirty
 * state of the currently active editor. To save, it delegates that to the
 * currently active editor, too.
 * 
 * @author nico.rehwaldt
 */
public class Bpmn2MultiPageEditor extends MultiPageEditorPart {

	private Bpmn2Editor bpmnEditor;
	private StructuredTextEditor xmlEditor;
	
	@Override
	protected IEditorSite createSite(IEditorPart editor) {
		IEditorSite site = null;
		if (editor == xmlEditor) {
			site = new MultiPageEditorSite(this, editor) {
				public String getId() {
					// sets this id so nested editor is considered xml source page
					return "org.eclipse.core.runtime.xml.source";
				}
			};
		}	else {
			site = super.createSite(editor);
		}
		return site;
	}

	@Override
	protected void setInput(IEditorInput input) {
		// convert input to a file input
		// to unify handling
		if (!(input instanceof FileEditorInput)) {
			input = convertToFileInput(input);
		}

		super.setInput(input);
	}

	@Override
	protected void createPages() {

		IConfigurationElement configurationElement = getConfigurationElement();
		
		bpmnEditor = new Bpmn2Editor();
		bpmnEditor.setInitializationData(configurationElement, null, null);
		
		xmlEditor = new StructuredTextEditor();
		xmlEditor.setInitializationData(configurationElement, null, null);

		IEditorInput input = getEditorInput();
		
		try {
			addPage(bpmnEditor, input);
		} catch (PartInitException e) {
			Activator.logError(e);
		}
		
		setPageText(0, "Design");
		
		try {
			addPage(xmlEditor, input);
		} catch (PartInitException e) {
			Activator.logError(e);
		}
		
		setPageText(1, "Source");
		
		setPartName(bpmnEditor.getTitle());
	}
	
	@Override
	protected void pageChange(int newPageIndex) {
		
		int index = Math.abs(newPageIndex - 1);
		
		String editorName = getPageText(index);
		IEditorPart editor = getEditor(index);
		
		if (editor != null && editor.isSaveOnCloseNeeded()) {
			
			boolean save = MessageDialog.openQuestion(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"Unsaved changes in " + editorName + " view", 
					"You have unsaved changes in the " + editorName + " view that may lead to editing conflicts. Would you like to save these changes before you continue?");
			
			if (save) {
				editor.doSave(new NullProgressMonitor());
			}
		}

		super.pageChange(newPageIndex);
	}
	
	@Override
	public String getTitleToolTip() {
		if (bpmnEditor != null) {
			return bpmnEditor.getTitleToolTip();
		} else {
			return super.getTitleToolTip();
		}
	}
	
	@Override
	public String getPartName() {
		if (bpmnEditor != null) {
			return bpmnEditor.getPartName();
		} else {
			return super.getPartName();
		}
	}
	
	/**
	 * Converts an editor input to a file input
	 * 
	 * @param input
	 * @return
	 */
	protected IEditorInput convertToFileInput(IEditorInput input) {
		try {
			URI workspaceUri = FileService.resolveAsWorkspaceResource(FileService.getInputUri(input));
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(workspaceUri.toPlatformString(true)));
			return new FileEditorInput(file);
		} catch (CoreException e) {
			throw new IllegalStateException("Failed to resolve input", e);
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		getActiveEditor().doSave(monitor);
	}

	@Override
	public void doSaveAs() {
		getActiveEditor().doSaveAs();
	}

	@Override
	public boolean isSaveAsAllowed() {
		IEditorPart activeEditor = getActiveEditor();
		return activeEditor != null && activeEditor.isSaveAsAllowed();
	}
}
