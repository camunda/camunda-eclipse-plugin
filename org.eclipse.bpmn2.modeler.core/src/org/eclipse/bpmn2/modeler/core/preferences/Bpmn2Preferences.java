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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.core.internal.resources.ProjectPreferences;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.navigator.ResourceNavigator;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.eclipse.emf.common.util.URI;

@SuppressWarnings("restriction")
public class Bpmn2Preferences implements IPreferenceChangeListener, IPropertyChangeListener, IResourceChangeListener {
	public final static String PROJECT_PREFERENCES_ID = "org.eclipse.bpmn2.modeler";
	public final static String PREF_TARGET_RUNTIME = "target.runtime";
	public final static String PREF_TARGET_RUNTIME_LABEL = "Target &Runtime";
	public final static String PREF_SHOW_ADVANCED_PROPERTIES = "show.advanced.properties";
	public final static String PREF_SHOW_ADVANCED_PROPERTIES_LABEL = "Show the &Advanced Properties Tab for BPMN2 Elements";
	public final static String PREF_EXPAND_PROPERTIES = "expand.properties";
	public final static String PREF_EXPAND_PROPERTIES_LABEL = "E&xpand compound property details instead of showing a selection list";
	public final static String PREF_OVERRIDE_MODEL_ENABLEMENTS = "override.model.enablements";
	public final static String PREF_VERTICAL_ORIENTATION = "vertical.orientation";
	public final static String PREF_VERTICAL_ORIENTATION_LABEL = "Use &Vertical layout for Pools and Lanes";
	public final static String PREF_WSIL_URL = "wsil.url";
	public final static String PREF_SHAPE_STYLE = "shape.style";
	// do we need these? >>
	public final static String PREF_SHAPE_DEFAULT_COLOR = "shape.default.color";
	public final static String PREF_SHAPE_PRIMARY_SELECTED_COLOR = "shape.primary.selected.color";
	public final static String PREF_SHAPE_SECONDARY_SELECTED_COLOR = "shape.secondary.selected.color";
	public final static String PREF_SHAPE_BORDER_COLOR = "shape.border.color";
	public final static String PREF_TEXT_COLOR = "text.color";
	public final static String PREF_TEXT_FONT = "text.font";
	// << do we need these?

	private static Hashtable<IProject,Bpmn2Preferences> instances = null;
	private static IProject activeProject;

	private IProject project;
	private Preferences projectPreferences;
	private IPreferenceStore globalPreferences;
	private boolean loaded;
	private boolean dirty;
	
	private TargetRuntime targetRuntime;
	private boolean showAdvancedPropertiesTab;
	private boolean overrideModelEnablements;
	private boolean expandProperties;
	private boolean verticalOrientation;
	private HashMap<Class, ShapeStyle> shapeStyles = new HashMap<Class, ShapeStyle>();
	
	// TODO: stuff like colors, fonts, etc.

	private Bpmn2Preferences(IProject project) {
		this.project = project;
		IEclipsePreferences rootNode = Platform.getPreferencesService()
				.getRootNode();
		if (project!=null) {
		projectPreferences = rootNode.node(ProjectScope.SCOPE)
				.node(project.getName())
				.node(PROJECT_PREFERENCES_ID);
		if (projectPreferences instanceof ProjectPreferences)
			((ProjectPreferences)projectPreferences).addPreferenceChangeListener(this);
		}		
		globalPreferences = Activator.getDefault().getPreferenceStore();
		globalPreferences.addPropertyChangeListener(this);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	// various preference instance getters
	
	/**
	 * Return the Preferences for the currently active project. This should be used
	 * with caution: the active project is set by the BPMN2Editor, so this should only
	 * be used in a context that is known to have an active editor.
	 * 
	 * @return project preferences
	 */
	public static Bpmn2Preferences getInstance() {
		return getInstance(getActiveProject());
	}
	
	/**
	 * Return the Preferences for the project containing the EMF Resource
	 * 
	 * @param resource
	 * @return project preferences
	 */
	public static Bpmn2Preferences getInstance(Resource resource) {
		return getInstance(resource.getURI());
	}
	
	/**
	 * Return the Preferences for the project containing the EMF Resource specified
	 * by the resource URI. This must be a Platform URI.
	 * 
	 * @param resourceURI
	 * @return project preferences
	 */
	public static Bpmn2Preferences getInstance(URI resourceURI) {
		String filename = resourceURI.toPlatformString(true);
		IProject project = ResourcesPlugin.getWorkspace().getRoot().findMember(filename).getProject();
		return getInstance(project);
	}
	
	/**
	 * Return the Preferences for the given project.
	 * 
	 * @param project
	 * @return project preferences
	 */
	public static Bpmn2Preferences getInstance(IProject project) {
		if (instances==null) {
			instances = new Hashtable<IProject,Bpmn2Preferences>();
		}
		Bpmn2Preferences pref;
		if (project==null)
			pref = new Bpmn2Preferences(null);
		else
			pref = instances.get(project);
		if (pref==null) {
			pref = new Bpmn2Preferences(project);
			instances.put(project, pref);
		}
		return pref;
	}
	
	public IPreferenceStore getGlobalPreferences()
	{
		return globalPreferences;
	}
	
	public Preferences getProjectPreferences()
	{
		return projectPreferences;
	}
	
	public void restoreDefaults() {
		if (projectPreferences!=null) {
		projectPreferences.remove(PREF_TARGET_RUNTIME);
		projectPreferences.remove(PREF_SHOW_ADVANCED_PROPERTIES);
		projectPreferences.remove(PREF_EXPAND_PROPERTIES);
		projectPreferences.remove(PREF_VERTICAL_ORIENTATION);
			for (Class key : shapeStyles.keySet()) {
				projectPreferences.remove(getShapeStyleId(key));
			}
		}		
		globalPreferences.setDefault(PREF_TARGET_RUNTIME,TargetRuntime.getFirstNonDefaultId());
		globalPreferences.setDefault(PREF_SHOW_ADVANCED_PROPERTIES, false);
		globalPreferences.setDefault(PREF_EXPAND_PROPERTIES, false);
		globalPreferences.setDefault(PREF_VERTICAL_ORIENTATION, false);
		for (Class key : shapeStyles.keySet()) {
			globalPreferences.setDefault(getShapeStyleId(key), IPreferenceStore.STRING_DEFAULT_DEFAULT);
		}
		
		globalPreferences.setToDefault(PREF_TARGET_RUNTIME);
		globalPreferences.setToDefault(PREF_SHOW_ADVANCED_PROPERTIES);
		globalPreferences.setToDefault(PREF_EXPAND_PROPERTIES);
		globalPreferences.setToDefault(PREF_VERTICAL_ORIENTATION);
		
		for (Class key : shapeStyles.keySet()) {
			globalPreferences.setToDefault(getShapeStyleId(key));
		}

		loaded = false;
		load();
	}
	
	public boolean hasProjectPreference(String key) {
		try {
			String[] keys;
			keys = projectPreferences.keys();
			for (String k : keys) {
				if (k.equals(key))
					return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	public void dispose() {
		if (projectPreferences instanceof ProjectPreferences)
			((ProjectPreferences)projectPreferences).removePreferenceChangeListener(this);
		globalPreferences.removePropertyChangeListener(this);
		instances.remove(project);
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}
	
	public synchronized void reload() {
		loaded = false;
		load();
		dirty = false;
	}
	
	public void load() {
		
		if (!loaded) {
			// load all preferences
			if (projectPreferences!=null)
			overrideModelEnablements = projectPreferences.getBoolean(PREF_OVERRIDE_MODEL_ENABLEMENTS, false);

			String id = getString(PREF_TARGET_RUNTIME,TargetRuntime.getFirstNonDefaultId());
			if (id==null || id.isEmpty())
				id = TargetRuntime.getFirstNonDefaultId();
			targetRuntime = TargetRuntime.getRuntime(id);
			showAdvancedPropertiesTab = getBoolean(PREF_SHOW_ADVANCED_PROPERTIES, false);
			expandProperties = getBoolean(PREF_EXPAND_PROPERTIES, false);
			verticalOrientation = getBoolean(PREF_VERTICAL_ORIENTATION, false);
			
			loaded = true;
		}
	}
	
	public synchronized void save() throws BackingStoreException {
		if (dirty) {
			// this is the only preference that is a project property,
			// and not saved in the preference store for this plugin.
			if (projectPreferences!=null)
			projectPreferences.putBoolean(PREF_OVERRIDE_MODEL_ENABLEMENTS, overrideModelEnablements);

			setString(PREF_TARGET_RUNTIME,targetRuntime.getId());
			setBoolean(PREF_SHOW_ADVANCED_PROPERTIES, showAdvancedPropertiesTab);
			setBoolean(PREF_EXPAND_PROPERTIES, expandProperties);
			setBoolean(PREF_VERTICAL_ORIENTATION, verticalOrientation);
			
		}
		
		for (Entry<Class, ShapeStyle> entry : shapeStyles.entrySet()) {
			setShapeStyle(entry.getKey(), entry.getValue());
		}
		
		if (projectPreferences!=null)
		projectPreferences.flush();
		dirty = false;
	}
	
	public static String getShapeStyleId(EObject object) {
		try {
			Class clazz = Class.forName(object.eClass().getInstanceClassName());
			return getShapeStyleId(clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getShapeStyleId(Class clazz) {
		return clazz.getSimpleName() + "." + PREF_SHAPE_STYLE;
	}

	public ShapeStyle getShapeStyle(EObject object) {
		Class clazz;
		try {
			clazz = Class.forName(object.eClass().getInstanceClassName());
			return getShapeStyle(clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ShapeStyle getShapeStyle(Class clazz) {
		ShapeStyle ss = shapeStyles.get(clazz);
		if (ss==null) {
			String key = getShapeStyleId(clazz);
			String value;
			if (hasProjectPreference(key)) {
				value = projectPreferences.get(key, "");
			}
			else {
				value = globalPreferences.getString(key);
				if (value.isEmpty()) {
					//get from TargetRuntime
					ss = getRuntime().getShapeStyles().get(clazz);
					if (ss==null) {
						if (!TargetRuntime.DEFAULT_RUNTIME_ID.equals(getRuntime().getId())) {
							// search default runtime
							ss = TargetRuntime.getDefaultRuntime().getShapeStyles().get(clazz);
						}
						if (ss==null) {
							// give up
							ss = new ShapeStyle();
						}
					}
					// don't cache this because we don't want to save it PreferenceStore
					return ss;
				}
			}
			ss = ShapeStyle.decode(value);
			shapeStyles.put(clazz, ss);
		}
		return ss;
	}
	
	public void setShapeStyle(Class clazz, ShapeStyle style) {
		if (style.isDirty()) {
			String key = getShapeStyleId(clazz);
			String value = ShapeStyle.encode(style);
			if (hasProjectPreference(key))
				projectPreferences.put(key, value);
			else
				globalPreferences.setValue(key, value);
			shapeStyles.put(clazz, style);
			style.setDirty(false);
			dirty = true;
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
		overrideGlobalString(PREF_TARGET_RUNTIME, rt.getId());
		targetRuntime = rt;
	}
	
	public boolean getShowAdvancedPropertiesTab() {
		load();
		return showAdvancedPropertiesTab;
	}
	
	public void setShowAdvancedPropertiesTab(boolean show) {
		overrideGlobalBoolean(PREF_SHOW_ADVANCED_PROPERTIES, show);
		showAdvancedPropertiesTab = show;
	}
	
	public boolean getOverrideModelEnablements() {
		load();
		return overrideModelEnablements;
	}
	
	public void setOverrideModelEnablements(boolean override) {
		overrideModelEnablements = override;
		dirty = true;
	}
	
	public boolean getExpandProperties() {
		load();
		return expandProperties;
	}
	
	public void setExpandProperties(boolean expand) {
		overrideGlobalBoolean(PREF_EXPAND_PROPERTIES, expand);
		expandProperties = expand;
	}

	public boolean isVerticalOrientation() {
		load();
		return verticalOrientation;
	}

	public void setVerticalOrientation(boolean vertical) {
		overrideGlobalBoolean(PREF_VERTICAL_ORIENTATION, vertical);
		verticalOrientation = vertical;
	}
	
	@Override
	public void preferenceChange(PreferenceChangeEvent event) {
		reload();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		reload();
	}

	// preference/property getters and setters
	public boolean getBoolean(String key, boolean defaultValue) {
		if (hasProjectPreference(key))
			return projectPreferences.getBoolean(key, defaultValue);
		return globalPreferences.getBoolean(key);
	}
	
	public void setBoolean(String key, boolean value) {
		if (hasProjectPreference(key))
			projectPreferences.putBoolean(key, value);
		else
			globalPreferences.setValue(key, value);
	}

	private void overrideGlobalBoolean(String key, boolean value) {
		if (value!=globalPreferences.getBoolean(key)) {
			projectPreferences.putBoolean(key, value);
			dirty = true;
		}
	}
	
	public String getString(String key, String defaultValue) {
		if (hasProjectPreference(key))
			return projectPreferences.get(key, defaultValue);
		return globalPreferences.getString(key);
	}
	
	public void setString(String key, String value) {
		if (hasProjectPreference(key))
			projectPreferences.put(key, value);
		else
			globalPreferences.setValue(key, value);
	}

	private void overrideGlobalString(String key, String value) {
		if (value!=globalPreferences.getString(key)) {
			projectPreferences.put(key, value);
			dirty = true;
		}
	}

	// TODO: use CNF for indigo & future - keep ResourceNavigator for backward compatibility
	public static IProject getActiveProject() {
		if (activeProject!=null)
			return activeProject;
		
		IWorkbench workbench = PlatformUI.getWorkbench(); 
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		if (page!=null) {
			IViewPart[] parts = page.getViews();
	
			for (int i = 0; i < parts.length; i++) {
				if (parts[i] instanceof ResourceNavigator) {
					ResourceNavigator navigator = (ResourceNavigator) parts[i];
					StructuredSelection sel = (StructuredSelection) navigator.getTreeViewer().getSelection();
					IResource resource = (IResource) sel.getFirstElement();
					activeProject = resource.getProject();
					break;
				}
			}
		}
		return activeProject;
	}

	public static void setActiveProject(IProject project) {
		activeProject = project;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		int type = event.getType();
		if (type==IResourceChangeEvent.PRE_CLOSE) {
			try {
				save();
			} catch (Exception e) {
				e.printStackTrace();
			}
			dispose();
		}
		if (type==IResourceChangeEvent.PRE_DELETE)
			dispose();
	}
}
