package org.camunda.bpm.modeler.ui.diagram.editor;

import org.camunda.bpm.modeler.core.Activator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

/**
 * A multi page editor for BPMN 2.0 files showing the diagram file
 * and the (editable) xml source.
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
		setTitleToolTip(bpmnEditor.getTitleToolTip());
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
		return getActiveEditor().isSaveAsAllowed();
	}
}
