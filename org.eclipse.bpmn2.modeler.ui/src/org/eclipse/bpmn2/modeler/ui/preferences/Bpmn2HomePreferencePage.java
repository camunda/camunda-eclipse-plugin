package org.eclipse.bpmn2.modeler.ui.preferences;

import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.IConstants;
import org.eclipse.bpmn2.modeler.ui.Messages;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class Bpmn2HomePreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {
	
	public Bpmn2HomePreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(Messages.Bpmn2PreferencePage_HomePage_Description);
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	@Override
	public void createFieldEditors() {
		BooleanFieldEditor showAdvancedPropsTab = new BooleanFieldEditor(
				Bpmn2Preferences.PREF_SHOW_ADVANCED_PROPERTIES,
				"Show the &Advanced Properties Tab for BPMN2 Elements",
				getFieldEditorParent());
		addField(showAdvancedPropsTab);
		
		String[][] entries = new String[TargetRuntime.getAllRuntimes().length][2];
		int i = 0;
		for (TargetRuntime r : TargetRuntime.getAllRuntimes()) {
			entries[i][0] = r.getName();
			entries[i][1] = r.getId();
			++i;
		}

		ComboFieldEditor targetRuntimes = new ComboFieldEditor(
				Bpmn2Preferences.PREF_TARGET_RUNTIME,
				"Target &Runtime",
				entries,
				getFieldEditorParent());
		addField(targetRuntimes);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}