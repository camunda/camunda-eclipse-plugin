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
package org.eclipse.bpmn2.modeler.ui.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.preferences.ToolEnablement;
import org.eclipse.bpmn2.modeler.core.preferences.ToolEnablementPreferences;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PropertyPage;
import org.osgi.service.prefs.BackingStoreException;

public class ToolEnablementPropertyPage extends PropertyPage {

	private DataBindingContext m_bindingContext;

	private ToolEnablementPreferences toolEnablementPreferences;
	private Bpmn2Preferences bpmn2Preferences;
	private final List<ToolEnablement> tools = new ArrayList<ToolEnablement>();
	private Object[] toolsEnabled;
	private CheckboxTreeViewer checkboxTreeViewer;
	private Tree checkboxTree;

	private WritableList writableList;

	/**
	 * Create the property page.
	 */
	public ToolEnablementPropertyPage() {
		setTitle("Tool Enablement");
	}

	/**
	 * Create contents of the property page.
	 * 
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		initData();

		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false, 1, 1));
		
		final Button override = new Button(container,SWT.CHECK);
		override.setText("Override default tool enablements with these settings:");
		override.setSelection(bpmn2Preferences.getOverrideModelEnablements());
		override.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 3, 1));

		checkboxTreeViewer = new CheckboxTreeViewer(container, SWT.BORDER);
		checkboxTree = checkboxTreeViewer.getTree();
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		data.heightHint = 200;
		checkboxTree.setLayoutData(data);

		new Label(container, SWT.NONE);

		Button btnImportProfile = new Button(container, SWT.NONE);
		btnImportProfile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.NULL);
				String path = dialog.open();
				if (path != null) {
					try {
						tools.clear();
						toolEnablementPreferences.importPreferences(path);
						reloadPreferences();
						checkboxTreeViewer.refresh();
						restoreDefaults();
					} catch (Exception e1) {
						Activator.showErrorWithLogging(e1);
					}
				}
			}
		});
		btnImportProfile.setText("Import Profile ...");

		Button btnExportProfile = new Button(container, SWT.NONE);
		btnExportProfile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.SAVE);
				String path = dialog.open();
				if (path != null) {
					try {
						toolEnablementPreferences.export(path);
					} catch (Exception e1) {
						Activator.showErrorWithLogging(e1);
					}
				}
			}
		});
		btnExportProfile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnExportProfile.setText("Export Profile ...");

		checkboxTreeViewer.setComparer(new IElementComparer() {

			@Override
			public boolean equals(Object a, Object b) {
				return a == b;
			}

			@Override
			public int hashCode(Object element) {
				return System.identityHashCode(element);
			}
		});
		checkboxTreeViewer.setUseHashlookup(true);
		m_bindingContext = initDataBindings();
		
		checkboxTree.setEnabled(override.getSelection());
		override.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				checkboxTree.setEnabled(override.getSelection());
				bpmn2Preferences.setOverrideModelEnablements(override.getSelection());
			}
		});

		restoreDefaults();

		return container;
	}

	private void restoreDefaults() {
		checkboxTreeViewer.setCheckedElements(toolsEnabled);
	}

	@Override
	protected void performDefaults() {
		super.performDefaults();
		restoreDefaults();
	}

	private void initData() {
		toolEnablementPreferences = ToolEnablementPreferences.getPreferences((IProject) getElement().getAdapter(IProject.class));
		bpmn2Preferences = new Bpmn2Preferences((IProject) getElement().getAdapter(IProject.class));

		reloadPreferences();
	}

	private void reloadPreferences() {
		tools.clear();
		tools.addAll(toolEnablementPreferences.getAllElements());
		ArrayList<ToolEnablement> tEnabled = new ArrayList<ToolEnablement>();
		for (ToolEnablement tool : tools) {
			if (tool.getEnabled()) {
				tEnabled.add(tool);
			}
			ArrayList<ToolEnablement> children = tool.getChildren();
			for (ToolEnablement t : children) {
				if (t.getEnabled()) {
					tEnabled.add(t);
				}
			}
		}
		toolsEnabled = tEnabled.toArray();
	}

	@Override
	public boolean performOk() {
		setErrorMessage(null);
		try {
			updateToolEnablement(tools, Arrays.asList(checkboxTreeViewer.getCheckedElements()));
			bpmn2Preferences.save();
		} catch (BackingStoreException e) {
			Activator.showErrorWithLogging(e);
		}
		return true;
	}

	private void updateToolEnablement(List<ToolEnablement> saveables, List<Object> enabled)
			throws BackingStoreException {
		for (ToolEnablement t : saveables) {
			toolEnablementPreferences.setEnabled(t, enabled.contains(t));
			for (ToolEnablement c : t.getChildren()) {
				toolEnablementPreferences.setEnabled(c, enabled.contains(c));
			}
		}
		toolEnablementPreferences.flush();
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		checkboxTreeViewer.setContentProvider(new ITreeContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean hasChildren(Object element) {
				if (element instanceof ToolEnablement) {
					return !((ToolEnablement) element).getChildren().isEmpty();
				}
				return false;
			}

			@Override
			public Object getParent(Object element) {
				if (element instanceof ToolEnablement) {
					return ((ToolEnablement) element).getParent();
				}
				return null;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof WritableList) {
					return ((WritableList) inputElement).toArray();
				}
				return null;
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof ToolEnablement) {
					return ((ToolEnablement) parentElement).getChildren().toArray();
				}
				return null;
			}
		});

		checkboxTreeViewer.setLabelProvider(new ILabelProvider() {
			@Override
			public void removeListener(ILabelProviderListener listener) {
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void dispose() {

			}

			@Override
			public void addListener(ILabelProviderListener listener) {
			}

			@Override
			public Image getImage(Object element) {
				return null;
			}

			@Override
			public String getText(Object element) {
				if (element instanceof ToolEnablement) {
					return ((ToolEnablement) element).getName();
				}
				return null;
			}
		});
		writableList = new WritableList(tools, ToolEnablement.class);
		checkboxTreeViewer.setInput(writableList);
		//
		return bindingContext;
	}

}
