package org.eclipse.bpmn2.modeler.ui.editor;

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;

public class BPMN2MultiPageEditor extends MultiPageEditorPart {
	
	BPMN2Editor bpmn2Editor;
	private CTabFolder tabFolder;
	private IEditorInput theEditorInput;
	
	public BPMN2Editor getBpmn2Editor() {
		if (bpmn2Editor == null) {
			bpmn2Editor = new BPMN2Editor();
		}
		return bpmn2Editor;
	}
	
	@Override
	public Object getAdapter(Class required) {
//		return getBpmn2Editor().getAdapter(required);
		return super.getAdapter(required);
	}
	
	public BPMN2MultiPageEditor() {
	}
	
	@Override
	protected void setInput(IEditorInput input) {
		// TODO Auto-generated method stub
		super.setInput(input);
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		this.theEditorInput = input;
	}
	
	@Override
	protected Composite createPageContainer(Composite parent) {
		parent.setLayout(new GridLayout());

		tabFolder = new CTabFolder(parent, SWT.BOTTOM);
		tabFolder.setBorderVisible(true);
		tabFolder.setBackgroundMode(SWT.INHERIT_DEFAULT);
		tabFolder.setSimple(false);  // rounded tabs
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		//Creates the first tab
		CTabItem headerTabItem = new CTabItem(tabFolder, SWT.CLOSE);
		headerTabItem.setText("Header");
		//Adds a composite to the tab
		Composite composite = new Composite(tabFolder, SWT.NONE);
		headerTabItem.setControl(composite);

		//Creates the second tab
		CTabItem detailTabItem = new CTabItem(tabFolder, SWT.CLOSE);
		detailTabItem.setText("Detail");
		
		return tabFolder;
	}
	
	@Override
	protected void createPages() {
		try {
			addPage(getBpmn2Editor(), theEditorInput);
		} catch (PartInitException e) {
			Activator.logError(e);
		}
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		
	}

	@Override
	public void doSaveAs() {
		
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

}
