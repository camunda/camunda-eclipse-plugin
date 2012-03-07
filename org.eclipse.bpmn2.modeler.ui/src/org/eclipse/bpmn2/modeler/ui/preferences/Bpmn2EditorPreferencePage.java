/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.preferences;


import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.IConstants;
import org.eclipse.bpmn2.modeler.ui.Messages;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


@SuppressWarnings("nls")
public class Bpmn2EditorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public Bpmn2EditorPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(Messages.Bpmn2PreferencePage_EditorPage_Description);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	protected void createFieldEditors() {
		ColorFieldEditor canvasColor = new ColorFieldEditor(
				IConstants.PREF_CANVAS_COLOR,
				"&Canvas Color:",
		 		getFieldEditorParent());
		addField(canvasColor);
		
		ColorFieldEditor gridColor = new ColorFieldEditor(
				IConstants.PREF_GRID_COLOR,
				"&Grid Color:",
		 		getFieldEditorParent());
		addField(gridColor);
		
		BooleanFieldEditor showGrid = new BooleanFieldEditor(
				IConstants.PREF_SHOW_GRID, 
				"&Show Grid Lines", 
		 		getFieldEditorParent());
		addField(showGrid);
	}

}
