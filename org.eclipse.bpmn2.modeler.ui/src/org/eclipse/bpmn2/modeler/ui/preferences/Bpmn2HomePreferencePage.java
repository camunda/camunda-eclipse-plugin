package org.eclipse.bpmn2.modeler.ui.preferences;

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.ui.Messages;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

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
				Bpmn2Preferences.PREF_SHOW_ADVANCED_PROPERTIES_LABEL,
				getFieldEditorParent());
		addField(showAdvancedPropsTab);

		BooleanFieldEditor expandProperties = new BooleanFieldEditor(
				Bpmn2Preferences.PREF_EXPAND_PROPERTIES,
				Bpmn2Preferences.PREF_EXPAND_PROPERTIES_LABEL,
				getFieldEditorParent());
		addField(expandProperties);
		
		String[][] entries = new String[TargetRuntime.getAllRuntimes().length][2];
		int i = 0;
		for (TargetRuntime r : TargetRuntime.getAllRuntimes()) {
			entries[i][0] = r.getName();
			entries[i][1] = r.getId();
			++i;
		}

		ComboFieldEditor targetRuntimes = new ComboFieldEditor(
				Bpmn2Preferences.PREF_TARGET_RUNTIME,
				Bpmn2Preferences.PREF_TARGET_RUNTIME_LABEL,
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