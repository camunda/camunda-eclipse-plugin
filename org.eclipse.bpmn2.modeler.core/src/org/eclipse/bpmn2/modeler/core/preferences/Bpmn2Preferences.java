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
 * @author Bob Brodt
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.preferences;

import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.core.internal.resources.ProjectPreferences;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

@SuppressWarnings("restriction")
public class Bpmn2Preferences implements IPreferenceChangeListener {
	public final static String PROJECT_PREFERENCES_ID = "org.eclipse.bpmn2.modeler";
	public final static String PREF_TARGET_RUNTIME = "target.runtime";
	public final static String PREF_SHOW_ADVANCED_PROPERTIES = "show.advanced.properties";
	
	private Preferences prefs;
	private boolean loaded;
	private boolean dirty;
	
	// the per-project preferences:
	private TargetRuntime targetRuntime;
	private boolean showAdvancedPropertiesTab;
	
	// the global user preferences:
	// TODO: stuff like colors, fonts, etc.

	public Bpmn2Preferences(IProject project) {
		IEclipsePreferences rootNode = Platform.getPreferencesService()
				.getRootNode();
		prefs = rootNode.node(ProjectScope.SCOPE)
				.node(project.getName())
				.node(PROJECT_PREFERENCES_ID);
		if (prefs instanceof ProjectPreferences)
			((ProjectPreferences)prefs).addPreferenceChangeListener(this);
	}

	public void restoreDefaults() {
		prefs.put(PREF_TARGET_RUNTIME,TargetRuntime.DEFAULT_RUNTIME_ID);
	}
	
	public void dispose() {
		if (prefs instanceof ProjectPreferences)
			((ProjectPreferences)prefs).removePreferenceChangeListener(this);
	}
	
	public synchronized void reload() {
		loaded = false;
		load();
		dirty = false;
	}
	
	public void load() {
		
		if (!loaded) {
			// load all preferences: this will eventually include all per-project
			// as well as global user preferences.
			targetRuntime = TargetRuntime.getRuntime(
					prefs.get(PREF_TARGET_RUNTIME,TargetRuntime.DEFAULT_RUNTIME_ID));
			showAdvancedPropertiesTab = prefs.getBoolean(PREF_SHOW_ADVANCED_PROPERTIES, true);
			loaded = true;
		}
	}
	
	public synchronized void save() throws BackingStoreException {
		
		if (dirty) {
			prefs.put(PREF_TARGET_RUNTIME,targetRuntime.getId());
			prefs.putBoolean(PREF_SHOW_ADVANCED_PROPERTIES, showAdvancedPropertiesTab);
			prefs.flush();
			
			dirty = false;
		}
	}
	
	public TargetRuntime getRuntime() {
		
		load();
		
		return targetRuntime;
	}

	/**
	 * If the project has not been configured for a specific runtime through the "BPMN2"
	 * project properties page (i.e. the target is "None") then allow the runtime extension
	 * plug-ins an opportunity to identify the given process file contents as their own.
	 * 
	 * If none of the plug-ins respond with "yes, this file is targeted for my runtime",
	 * then use the "None" as the extension. This will configure the BPMN2 Modeler with
	 * generic property sheets and other default behavior.
	 * 
	 * @param file
	 * @return
	 */
	public TargetRuntime getRuntime(IFile file) {
		
		load();
		
		if (targetRuntime == TargetRuntime.getDefaultRuntime()) {
			for (TargetRuntime rt : TargetRuntime.getAllRuntimes()) {
				if (rt.getRuntimeExtension().isContentForRuntime(file)) {
					return rt;
				}
			}
		}
		else
			return targetRuntime;
		
		return TargetRuntime.getDefaultRuntime();
	}
	
	public void setRuntime(TargetRuntime rt) {
		
		assert(rt!=null);
		targetRuntime = rt;
		
		dirty = true;
	}
	
	public boolean getShowAdvancedPropertiesTab() {
		load();
		return showAdvancedPropertiesTab;
	}
	
	public void setShowAdvancedPropertiesTab(boolean show) {
		showAdvancedPropertiesTab = show;
		dirty = true;
	}

	@Override
	public void preferenceChange(PreferenceChangeEvent event) {
		reload();
	}
}
