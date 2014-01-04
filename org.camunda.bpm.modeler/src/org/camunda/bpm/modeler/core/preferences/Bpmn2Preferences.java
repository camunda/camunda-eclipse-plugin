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
package org.camunda.bpm.modeler.core.preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.AdHocSubProcess;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.CallChoreography;
import org.eclipse.bpmn2.CancelEventDefinition;
import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Message;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SubChoreography;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.TerminateEventDefinition;
import org.eclipse.bpmn2.Transaction;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.core.internal.resources.ProjectPreferences;
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
import org.eclipse.emf.common.util.URI;
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
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.ui.views.navigator.ResourceNavigator;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

@SuppressWarnings("restriction")
public class Bpmn2Preferences implements IPreferenceChangeListener, IPropertyChangeListener, IResourceChangeListener {
	public final static String PROJECT_PREFERENCES_ID = "org.camunda.bpm.modeler";
	public final static String PREF_TARGET_RUNTIME = "target.runtime";
	public final static String PREF_TARGET_RUNTIME_LABEL = "Target &Runtime";
	public final static String PREF_SHOW_ADVANCED_PROPERTIES = "show.advanced.properties";
	public final static String PREF_SHOW_ADVANCED_PROPERTIES_LABEL = "Show the &Advanced Properties Tab for BPMN2 Elements";
	public final static String PREF_SHOW_DESCRIPTIONS = "show.descriptions";
	public final static String PREF_SHOW_DESCRIPTIONS_LABEL = "Show &descriptions in Properties Tab for BPMN2 Elements";
	public final static String PREF_OVERRIDE_MODEL_ENABLEMENTS = "override.model.enablements";
	public final static String PREF_IS_HORIZONTAL = "is.horizontal";
	public final static String PREF_IS_HORIZONTAL_LABEL = "&Horizontal layout of Pools, Lanes and diagram elements [isHorizontal]";

	public static final String PREF_TOGGLE_DIAGRAM_GENERATION = "TOGGLE_DIAGRAM_GENERATION";
	public static final String PREF_TOGGLE_DIAGRAM_GENERATION_LABEL = "Generate PNG Diagram Image";

	public final static String PREF_IS_EXPANDED = "is.expanded";
	public final static String PREF_IS_EXPANDED_LABEL = "Expand activity containers (SubProcess, CallActivity, etc.) [isExpanded]";
	public final static String PREF_IS_MESSAGE_VISIBLE = "is.message.visible";
	public final static String PREF_IS_MESSAGE_VISIBLE_LABEL = "Show Participant Band Messages [isMessageVisible]";
	public final static String PREF_IS_MARKER_VISIBLE = "is.marker.visible";
	public final static String PREF_IS_MARKER_VISIBLE_LABEL = "Decorate Exclusive Gateway with \"X\" marker [isMarkerVisible]";
	
	public final static String PREF_WSIL_URL = "wsil.url";
	public final static String PREF_SHAPE_STYLE = "shape.style";

	public final static String PREF_CONNECTION_TIMEOUT = "connection.timeout";
	public final static String PREF_CONNECTION_TIMEOUT_LABEL = "Connection Timeout for resolving remote objects (milliseconds)";

	public final static String PREF_POPUP_CONFIG_DIALOG = "popup.config.dialog";
	public final static String PREF_POPUP_CONFIG_DIALOG_LABEL = "Display element configuration popup dialog after DND of:";
	
	public final static String PREF_POPUP_CONFIG_DIALOG_FOR_activitiES = "popup.config.dialog.for.activities";
	public final static String PREF_POPUP_CONFIG_DIALOG_FOR_activitiES_LABEL = "activities";
	public final static String PREF_POPUP_CONFIG_DIALOG_FOR_GATEWAYS = "popup.config.dialog.for.gateways";
	public final static String PREF_POPUP_CONFIG_DIALOG_FOR_GATEWAYS_LABEL = "Gateways";
	public final static String PREF_POPUP_CONFIG_DIALOG_FOR_EVENTS = "popup.config.dialog.for.events";
	public final static String PREF_POPUP_CONFIG_DIALOG_FOR_EVENTS_LABEL = "Events";
	public final static String PREF_POPUP_CONFIG_DIALOG_FOR_EVENT_DEFS = "popup.config.dialog.for.event.defs";
	public final static String PREF_POPUP_CONFIG_DIALOG_FOR_EVENT_DEFS_LABEL = "Event Definitions";
	public final static String PREF_POPUP_CONFIG_DIALOG_FOR_DATA_DEFS = "popup.config.dialog.for.data.defs";
	public final static String PREF_POPUP_CONFIG_DIALOG_FOR_DATA_DEFS_LABEL = "Data Items";
	public final static String PREF_POPUP_CONFIG_DIALOG_FOR_CONTAINERS = "popup.config.dialog.for.containers";
	public final static String PREF_POPUP_CONFIG_DIALOG_FOR_CONTAINERS_LABEL = "Acitivity containers (Pools, SubProcess, Transaction, etc.)";

	public final static String PREF_SHOW_ID_ATTRIBUTE = "show.id.attribute";
	public final static String PREF_SHOW_ID_ATTRIBUTE_LABEL = "Show ID attribute for BPMN2 Elements";
	public final static String PREF_CHECK_PROJECT_NATURE = "check.project.nature";
	public final static String PREF_CHECK_PROJECT_NATURE_LABEL = "Check if project is configured for BPMN2 Project Nature";
	
	private static Hashtable<IProject,Bpmn2Preferences> instances = null;
	private static IProject activeProject;

	private IProject project;
	private Preferences projectPreferences;
	private IPreferenceStore globalPreferences;
	private boolean loaded;
	private boolean dirty;
	
	public enum BPMNDIAttributeDefault {
		USE_DI_VALUE,
		DEFAULT_TRUE,
		ALWAYS_TRUE,
		ALWAYS_FALSE
	};
	
	private TargetRuntime targetRuntime;
	private boolean showAdvancedPropertiesTab;
	private boolean overrideModelEnablements;
	private boolean showDescriptions;
	private boolean showIdAttribute;
	private boolean checkProjectNature;
	private BPMNDIAttributeDefault isHorizontal;
	private BPMNDIAttributeDefault isExpanded;
	private BPMNDIAttributeDefault isMessageVisible;
	private BPMNDIAttributeDefault isMarkerVisible;
	private String connectionTimeout;
	private int popupConfigDialog;
	private boolean popupConfigDialogFor[] = new boolean[6];

	private HashMap<Class, ShapeStyle> shapeStyles = new HashMap<Class, ShapeStyle>();
	
	private Bpmn2Preferences(IProject project) {
		this.project = project;
	
		IEclipsePreferences rootNode = Platform.getPreferencesService().getRootNode();
		if (project!=null) {
			projectPreferences = rootNode
				.node(ProjectScope.SCOPE)
				.node(project.getName())
				.node(PROJECT_PREFERENCES_ID);
		
			if (projectPreferences instanceof ProjectPreferences) {
				((ProjectPreferences)projectPreferences).addPreferenceChangeListener(this);
			}
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
	 * @param bpmnResource
	 * @return project preferences
	 */
	public static Bpmn2Preferences getInstance(EObject object) {
		return getInstance(object.eResource());
	}
	
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
		String filename = resourceURI.trimFragment().toPlatformString(true);
		if (filename == null) {
			return getInstance();
		}
		IResource resourceFile = ResourcesPlugin.getWorkspace().getRoot().findMember(filename);
		if (resourceFile == null) {
			return null;
		}

		IProject project = resourceFile.getProject();
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
	
	public void loadDefaults() {
		String rid = TargetRuntime.getFirstNonDefaultId();
		globalPreferences.setDefault(PREF_TARGET_RUNTIME, rid);
		globalPreferences.setDefault(PREF_SHOW_ADVANCED_PROPERTIES, false);
		globalPreferences.setDefault(PREF_SHOW_DESCRIPTIONS, true);
		globalPreferences.setDefault(PREF_IS_HORIZONTAL, BPMNDIAttributeDefault.DEFAULT_TRUE.name());
		globalPreferences.setDefault(PREF_IS_EXPANDED, BPMNDIAttributeDefault.ALWAYS_TRUE.name());
		globalPreferences.setDefault(PREF_IS_MESSAGE_VISIBLE, BPMNDIAttributeDefault.ALWAYS_TRUE.name());
		globalPreferences.setDefault(PREF_IS_MARKER_VISIBLE, BPMNDIAttributeDefault.DEFAULT_TRUE.name());

		globalPreferences.setDefault(PREF_POPUP_CONFIG_DIALOG, false); // tri-state checkbox
		globalPreferences.setDefault(PREF_POPUP_CONFIG_DIALOG_FOR_activitiES, false);
		globalPreferences.setDefault(PREF_POPUP_CONFIG_DIALOG_FOR_GATEWAYS, false);
		globalPreferences.setDefault(PREF_POPUP_CONFIG_DIALOG_FOR_EVENTS, false);
		globalPreferences.setDefault(PREF_POPUP_CONFIG_DIALOG_FOR_EVENT_DEFS, false);
		globalPreferences.setDefault(PREF_POPUP_CONFIG_DIALOG_FOR_DATA_DEFS, false);
		globalPreferences.setDefault(PREF_POPUP_CONFIG_DIALOG_FOR_CONTAINERS, false);

		for (Class key : shapeStyles.keySet()) {
			globalPreferences.setDefault(getShapeStyleId(key), IPreferenceStore.STRING_DEFAULT_DEFAULT);
		}
		globalPreferences.setDefault(PREF_CONNECTION_TIMEOUT, "1000");
	}
	
	public void restoreDefaults(boolean resetProjectPreferences) {
		loadDefaults();
		if (resetProjectPreferences && projectPreferences != null) {
			projectPreferences.remove(PREF_TARGET_RUNTIME);
			projectPreferences.remove(PREF_SHOW_ADVANCED_PROPERTIES);
			projectPreferences.remove(PREF_SHOW_DESCRIPTIONS);
			projectPreferences.remove(PREF_SHOW_ID_ATTRIBUTE);
			projectPreferences.remove(PREF_CHECK_PROJECT_NATURE);
			projectPreferences.remove(PREF_IS_HORIZONTAL);
			projectPreferences.remove(PREF_IS_EXPANDED);
			projectPreferences.remove(PREF_IS_MESSAGE_VISIBLE);
			projectPreferences.remove(PREF_IS_MARKER_VISIBLE);

			projectPreferences.remove(PREF_POPUP_CONFIG_DIALOG);
			projectPreferences.remove(PREF_POPUP_CONFIG_DIALOG_FOR_activitiES);
			projectPreferences.remove(PREF_POPUP_CONFIG_DIALOG_FOR_GATEWAYS);
			projectPreferences.remove(PREF_POPUP_CONFIG_DIALOG_FOR_EVENTS);
			projectPreferences.remove(PREF_POPUP_CONFIG_DIALOG_FOR_EVENT_DEFS);
			projectPreferences.remove(PREF_POPUP_CONFIG_DIALOG_FOR_DATA_DEFS);
			projectPreferences.remove(PREF_POPUP_CONFIG_DIALOG_FOR_CONTAINERS);
			for (Class key : shapeStyles.keySet()) {
				projectPreferences.remove(getShapeStyleId(key));
			}
			try {
				projectPreferences.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
		}

		globalPreferences.setToDefault(PREF_TARGET_RUNTIME);
		globalPreferences.setToDefault(PREF_SHOW_ADVANCED_PROPERTIES);
		globalPreferences.setToDefault(PREF_SHOW_DESCRIPTIONS);
		globalPreferences.setToDefault(PREF_SHOW_ID_ATTRIBUTE);
		globalPreferences.setToDefault(PREF_CHECK_PROJECT_NATURE);
		globalPreferences.setToDefault(PREF_IS_HORIZONTAL);
		globalPreferences.setToDefault(PREF_IS_EXPANDED);
		globalPreferences.setToDefault(PREF_IS_MESSAGE_VISIBLE);
		globalPreferences.setToDefault(PREF_IS_MARKER_VISIBLE);

		globalPreferences.setToDefault(PREF_POPUP_CONFIG_DIALOG);
		globalPreferences.setToDefault(PREF_POPUP_CONFIG_DIALOG_FOR_activitiES);
		globalPreferences.setToDefault(PREF_POPUP_CONFIG_DIALOG_FOR_GATEWAYS);
		globalPreferences.setToDefault(PREF_POPUP_CONFIG_DIALOG_FOR_EVENTS);
		globalPreferences.setToDefault(PREF_POPUP_CONFIG_DIALOG_FOR_EVENT_DEFS);
		globalPreferences.setToDefault(PREF_POPUP_CONFIG_DIALOG_FOR_DATA_DEFS);
		globalPreferences.setToDefault(PREF_POPUP_CONFIG_DIALOG_FOR_CONTAINERS);

		List<Class> keys = new ArrayList<Class>();
		keys.addAll(shapeStyles.keySet());
		shapeStyles.clear();
		for (Class key : keys) {
			globalPreferences.setToDefault(getShapeStyleId(key));
			ShapeStyle ss = getShapeStyle(key);
			ss.setDirty(true);
		}
		
		try {
			((ScopedPreferenceStore)globalPreferences).save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasProjectPreference(String key) {
		if (projectPreferences!=null) {
			try {
				String[] keys;
				keys = projectPreferences.keys();
				for (String k : keys) {
					if (k.equals(key))
						return true;
				}
			} catch (Exception e) {
			}
		}
		return false;
	}
	
	public void dispose() {
		if (projectPreferences instanceof ProjectPreferences)
			((ProjectPreferences)projectPreferences).removePreferenceChangeListener(this);
		globalPreferences.removePropertyChangeListener(this);
		if (project!=null)
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
			loadDefaults();
			
			if (projectPreferences!=null)
				overrideModelEnablements = projectPreferences.getBoolean(PREF_OVERRIDE_MODEL_ENABLEMENTS, false);

			String id = getString(PREF_TARGET_RUNTIME,TargetRuntime.getFirstNonDefaultId());
			if (id==null || id.isEmpty())
				id = TargetRuntime.getFirstNonDefaultId();
			targetRuntime = TargetRuntime.getRuntime(id);
			showAdvancedPropertiesTab = getBoolean(PREF_SHOW_ADVANCED_PROPERTIES, false);
			showDescriptions = getBoolean(PREF_SHOW_DESCRIPTIONS, false);
			showIdAttribute = getBoolean(PREF_SHOW_ID_ATTRIBUTE, false);
			checkProjectNature = getBoolean(PREF_CHECK_PROJECT_NATURE, true);
			isHorizontal = getBPMNDIAttributeDefault(PREF_IS_HORIZONTAL, BPMNDIAttributeDefault.USE_DI_VALUE);
			isExpanded = getBPMNDIAttributeDefault(PREF_IS_EXPANDED, BPMNDIAttributeDefault.DEFAULT_TRUE);
			isMessageVisible = getBPMNDIAttributeDefault(PREF_IS_MESSAGE_VISIBLE, BPMNDIAttributeDefault.USE_DI_VALUE);
			isMarkerVisible = getBPMNDIAttributeDefault(PREF_IS_MARKER_VISIBLE, BPMNDIAttributeDefault.DEFAULT_TRUE);
			connectionTimeout = this.getString(PREF_CONNECTION_TIMEOUT, "60000");
			
			popupConfigDialog = getInt(PREF_POPUP_CONFIG_DIALOG, 0); // tri-state checkbox
			popupConfigDialogFor[0] = getBoolean(PREF_POPUP_CONFIG_DIALOG_FOR_activitiES, false);
			popupConfigDialogFor[1] = getBoolean(PREF_POPUP_CONFIG_DIALOG_FOR_GATEWAYS, false);
			popupConfigDialogFor[2] = getBoolean(PREF_POPUP_CONFIG_DIALOG_FOR_EVENTS, false);
			popupConfigDialogFor[3] = getBoolean(PREF_POPUP_CONFIG_DIALOG_FOR_EVENT_DEFS, false);
			popupConfigDialogFor[4] = getBoolean(PREF_POPUP_CONFIG_DIALOG_FOR_DATA_DEFS, false);
			popupConfigDialogFor[5] = getBoolean(PREF_POPUP_CONFIG_DIALOG_FOR_CONTAINERS, false);


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
			setBoolean(PREF_SHOW_DESCRIPTIONS, showDescriptions);
			setBoolean(PREF_SHOW_ID_ATTRIBUTE, showIdAttribute);
			setBoolean(PREF_CHECK_PROJECT_NATURE, checkProjectNature);
			setBPMNDIAttributeDefault(PREF_IS_HORIZONTAL, isHorizontal);

			setBPMNDIAttributeDefault(PREF_IS_EXPANDED, isExpanded);
			setBPMNDIAttributeDefault(PREF_IS_MESSAGE_VISIBLE, isMessageVisible);
			setBPMNDIAttributeDefault(PREF_IS_MARKER_VISIBLE, isMarkerVisible);
			
			setString(PREF_CONNECTION_TIMEOUT, connectionTimeout);

			setInt(PREF_POPUP_CONFIG_DIALOG, popupConfigDialog);
			setBoolean(PREF_POPUP_CONFIG_DIALOG_FOR_activitiES, popupConfigDialogFor[0]);
			setBoolean(PREF_POPUP_CONFIG_DIALOG_FOR_GATEWAYS, popupConfigDialogFor[1]);
			setBoolean(PREF_POPUP_CONFIG_DIALOG_FOR_EVENTS, popupConfigDialogFor[2]);
			setBoolean(PREF_POPUP_CONFIG_DIALOG_FOR_EVENT_DEFS, popupConfigDialogFor[3]);
			setBoolean(PREF_POPUP_CONFIG_DIALOG_FOR_DATA_DEFS, popupConfigDialogFor[4]);
			setBoolean(PREF_POPUP_CONFIG_DIALOG_FOR_CONTAINERS, popupConfigDialogFor[5]);
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
			return getShapeStyleId(object.getClass());
		}
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
			return getShapeStyle(object.getClass());
		}
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
					ss = new ShapeStyle();
//					ss = getRuntime().getShapeStyle(clazz);
//					if (ss==null) {
//						if (!TargetRuntime.DEFAULT_RUNTIME_ID.equals(getRuntime().getId())) {
//							// search default runtime
//							ss = TargetRuntime.getDefaultRuntime().getShapeStyle(clazz);
//						}
//						if (ss==null) {
//							// give up
//							ss = new ShapeStyle();
//						}
//					}
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
	
	public boolean getShowDescriptions() {
		load();
		return showDescriptions;
	}
	
	public void setShowDescriptions(boolean show) {
		overrideGlobalBoolean(PREF_SHOW_DESCRIPTIONS, show);
		showDescriptions = show;
	}
	
	public boolean getShowIdAttribute() {
		load();
		return showIdAttribute;
	}
	
	public void setShowIdAttribute(boolean show) {
		overrideGlobalBoolean(PREF_SHOW_ID_ATTRIBUTE, show);
		showIdAttribute = show;
	}
	
	public boolean getCheckProjectNature() {
		load();
		return checkProjectNature;
	}
	
	public void setCheckProjectNature(boolean show) {
		overrideGlobalBoolean(PREF_CHECK_PROJECT_NATURE, show);
		checkProjectNature = show;
	}

	public boolean getOverrideModelEnablements() {
		load();
		return overrideModelEnablements;
	}
	
	public void setOverrideModelEnablements(boolean override) {
		overrideModelEnablements = override;
		dirty = true;
	}
	
	public boolean getShowPopupConfigDialog(Object context) {
		load();
		if (popupConfigDialog!=0) {
			if (context instanceof Task || context instanceof ChoreographyActivity) {
				return popupConfigDialogFor[0];
			}
			if (context instanceof Gateway) {
				return popupConfigDialogFor[1];
			}
			if (context instanceof Event) {
				return popupConfigDialogFor[2];
			}
			if (context instanceof EventDefinition) {
				if (context instanceof CancelEventDefinition || context instanceof TerminateEventDefinition)
					return false; // these have no additional attributes
				return popupConfigDialogFor[3];
			}
			if (context instanceof ItemAwareElement || context instanceof Message) {
				return popupConfigDialogFor[4];
			}
			if (context instanceof InteractionNode || context instanceof FlowElementsContainer) {
				return popupConfigDialogFor[5];
			}
		}
		return false;
	}
	
	public boolean hasPopupConfigDialog(Object context) {
		if (context instanceof Activity) {
			return true;
		}
		if (context instanceof Gateway) {
			return true;
		}
		if (context instanceof Event) {
			return true;
		}
		if (context instanceof EventDefinition) {
			if (context instanceof CancelEventDefinition || context instanceof TerminateEventDefinition)
				return false; // these have no additional attributes
			return true;
		}
		if (context instanceof ItemAwareElement || context instanceof Message) {
			return true;
		}
		if (context instanceof InteractionNode
				|| context instanceof FlowElementsContainer
				|| context instanceof CallChoreography) {
			return true;
		}
		return false;
	}
	
	public void setShowPopupConfigDialog(Object context, boolean value) {
		overrideGlobalInt(PREF_POPUP_CONFIG_DIALOG,  value ? 1 : 0);
		popupConfigDialog = value ? 1 : 0;
	}

	public boolean isHorizontalDefault() {
		load();
		return isHorizontal==BPMNDIAttributeDefault.ALWAYS_TRUE ||
				isHorizontal==BPMNDIAttributeDefault.DEFAULT_TRUE;
	}

	public BPMNDIAttributeDefault getIsHorizontal() {
		return isHorizontal;
	}
	
	public void setIsHorizontal(BPMNDIAttributeDefault value) {
		overrideGlobalBPMNDIAttributeDefault(PREF_IS_HORIZONTAL, value);
		this.isHorizontal = value;
	}

	public boolean isExpandedDefault() {
		load();
		return isExpanded == BPMNDIAttributeDefault.DEFAULT_TRUE ||
				isExpanded == BPMNDIAttributeDefault.ALWAYS_TRUE;
	}

	public BPMNDIAttributeDefault getIsExpanded() {
		load();
		return isExpanded;
	}

	public void setIsExpanded(BPMNDIAttributeDefault value) {
		overrideGlobalBPMNDIAttributeDefault(PREF_IS_EXPANDED, value);
		this.isExpanded = value;
	}

	public BPMNDIAttributeDefault getIsMessageVisible() {
		load();
		return isMessageVisible;
	}

	public void setIsMessageVisible(BPMNDIAttributeDefault value) {
		overrideGlobalBPMNDIAttributeDefault(PREF_IS_MESSAGE_VISIBLE, value);
		this.isMessageVisible = value;
	}

	public BPMNDIAttributeDefault getIsMarkerVisible() {
		load();
		return isMarkerVisible;
	}

	public void setIsMarkerVisible(BPMNDIAttributeDefault value) {
		overrideGlobalBPMNDIAttributeDefault(PREF_IS_MARKER_VISIBLE, value);
		this.isMarkerVisible = value;
	}

	public String getConnectionTimeout() {
		load();
		return connectionTimeout;
	}
	
	public void setConnectionTimeout(String value) {
		try {
			Integer.parseInt(value);
			overrideGlobalString(PREF_CONNECTION_TIMEOUT, value);
			this.connectionTimeout = value;
		}
		catch (Exception e) {
		}
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
		if (globalPreferences.contains(key))
			return globalPreferences.getBoolean(key);
		return defaultValue;
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
	
	public int getInt(String key, int defaultValue) {
		if (hasProjectPreference(key))
			return projectPreferences.getInt(key, defaultValue);
		if (globalPreferences.contains(key))
			return globalPreferences.getInt(key);
		return defaultValue;
	}
	
	public void setInt(String key, int value) {
		if (hasProjectPreference(key))
			projectPreferences.putInt(key, value);
		else
			globalPreferences.setValue(key, value);
	}

	private void overrideGlobalInt(String key, int value) {
		if (value!=globalPreferences.getInt(key)) {
			projectPreferences.putInt(key, value);
			dirty = true;
		}
	}
	
	public String getString(String key, String defaultValue) {
		if (hasProjectPreference(key))
			return projectPreferences.get(key, defaultValue);
		if (globalPreferences.contains(key))
			return globalPreferences.getString(key);
		return defaultValue;
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

	public BPMNDIAttributeDefault getBPMNDIAttributeDefault(String key, BPMNDIAttributeDefault defaultValue) {
		BPMNDIAttributeDefault value = null;
		if (hasProjectPreference(key))
			value = BPMNDIAttributeDefault.valueOf(projectPreferences.get(key, defaultValue.name()));
		else if (globalPreferences.contains(key))
			value = BPMNDIAttributeDefault.valueOf(globalPreferences.getString(key));
		else
			value = defaultValue;
		return value;
	}
	
	public void setBPMNDIAttributeDefault(String key, BPMNDIAttributeDefault value) {
		if (hasProjectPreference(key))
			projectPreferences.put(key, value.name());
		else
			globalPreferences.setValue(key, value.name());
	}

	private void overrideGlobalBPMNDIAttributeDefault(String key, BPMNDIAttributeDefault value) {
		if (value!=BPMNDIAttributeDefault.valueOf(globalPreferences.getString(key))) {
			projectPreferences.put(key, value.name());
			dirty = true;
		}
	}

	public static String[] getBPMNDIAttributeDefaultChoices() {
		BPMNDIAttributeDefault[] values = BPMNDIAttributeDefault.values();
		String[] choices = new String[values.length];
		int i = 0;
		for (BPMNDIAttributeDefault v : values) {
			String text = "None";
			switch (v) {
			case USE_DI_VALUE:
				text = "False if not set";
				break;
			case DEFAULT_TRUE:
				text = "True if not set";
				break;
			case ALWAYS_TRUE:
				text = "Always true";
				break;
			case ALWAYS_FALSE:
				text = "Always false";
				break;
			}
			choices[i++] = text;
		}
		return choices;
	}
	
	public static String[][] getBPMNDIAttributeDefaultChoicesAndValues() {
		String[] choices = getBPMNDIAttributeDefaultChoices();
		BPMNDIAttributeDefault[] values = BPMNDIAttributeDefault.values();
		String[][] choicesAndValues = new String[choices.length][2];
		int i = 0;
		for (BPMNDIAttributeDefault v : values) {
			choicesAndValues[i][0] = choices[i];
			choicesAndValues[i][1] = v.name();
			++i;
		}
		return choicesAndValues;
	}
	
	/**
	 * Applies preference defaults to a BPMNShape object. The <code>attribs</code> map should contain
	 * only those attributes that are set on the BPMNShape object (as read from the bpmn XML file).
	 * This is used to determine the appropriate default values for certain optional attributes, e.g.
	 * isHorizontal, isExpanded, etc.
	 * 
	 * @param bpmnShape - the BPMNShape object whose attributes are to be set
	 * @param attribs - map of BPMN DI attributes currently set on the BPMNShape object. May be null.
	 * @see getIsHorizontal(), getIsExpanded(), getIsMessageVisible() and getIsMarkerVisible()
	 */
	public void applyBPMNDIDefaults(BPMNShape bpmnShape, Map<String,String>attribs) {
		boolean isHorizontalSet = false;
		boolean isExpandedSet = false;
		boolean isMessageVisibleSet = false;
		boolean isMarkerVisibleSet = false;
		boolean choreographyActivityShapeSet = false;
		
		if (attribs != null) {
			for (Entry<String, String> entry : attribs.entrySet()) {
				String name = entry.getKey();
				if ("isHorizontal".equals(name)) {
					isHorizontalSet = true;
				}
				if ("isExpanded".equals(name)) {
					isExpandedSet = true;
				}
				if ("isMessageVisible".equals(name)) {
					isMessageVisibleSet = true;
				}
				if ("isMarkerVisible".equals(name)) {
					isMarkerVisibleSet = true;
				}
				if ("choreographyActivityShape".equals(name)) {
					choreographyActivityShapeSet = true;
				}
			}
		}
		
		BaseElement be = bpmnShape.getBpmnElement();
		
		// isHorizontal only applies to Pools and Lanes, not Participant bands
		if (!isHorizontalSet) {
			if ((be instanceof Participant && !choreographyActivityShapeSet) || be instanceof Lane) {
				boolean horz = isHorizontalDefault();
				bpmnShape.setIsHorizontal(horz);
			}
		}
		else {
			if ((be instanceof Participant && !choreographyActivityShapeSet) || be instanceof Lane) {
				BPMNDIAttributeDefault df = getIsHorizontal();
				// Assume horizontal to be the default
				if (df == null) {
					bpmnShape.setIsHorizontal(true);
				} else {
					switch(df) {
					case ALWAYS_TRUE:
						bpmnShape.setIsHorizontal(true);
						break;
					case ALWAYS_FALSE:
						bpmnShape.setIsHorizontal(false);
						break;
					}
				}
			}
		}
		
		// isExpanded only applies to activity containers (SubProcess, AdHocSubProcess, etc.)
		if (!isExpandedSet) {
			if (be instanceof CallActivity) {
				bpmnShape.setIsExpanded(false);

			} else if (be instanceof SubProcess
					|| be instanceof AdHocSubProcess
					|| be instanceof Transaction
					|| be instanceof SubChoreography
					|| be instanceof CallChoreography) {
				boolean value = false;
				BPMNDIAttributeDefault df = getIsExpanded();
				switch (df) {
				case ALWAYS_TRUE:
				case DEFAULT_TRUE:
					value = true;
					break;
				case ALWAYS_FALSE:
				case USE_DI_VALUE:
					// get value from the bpmn shape
					// delete hard coded 'value = false'
					value = bpmnShape.isIsExpanded();
				}
				bpmnShape.setIsExpanded(value);
			}
		} else {
			// for the moment, call activities are always not expanded
			if (be instanceof CallActivity) {
				bpmnShape.setIsExpanded(false);

			} else if (be instanceof SubProcess
					|| be instanceof AdHocSubProcess
					|| be instanceof Transaction
					|| be instanceof SubChoreography
					|| be instanceof CallChoreography) {
				BPMNDIAttributeDefault df = getIsExpanded();
				switch (df) {
				case ALWAYS_TRUE:
					bpmnShape.setIsExpanded(true);
					break;
				case ALWAYS_FALSE:
					bpmnShape.setIsExpanded(false);
					break;
				}
			}
		}
		
		// isMessageVisible only applies to Participant Bands
		if (!isMessageVisibleSet) {
			if (be instanceof Participant && choreographyActivityShapeSet) {
				boolean value = false;
				BPMNDIAttributeDefault df = getIsMessageVisible();
				switch(df) {
				case ALWAYS_TRUE:
				case DEFAULT_TRUE:
					value = true;
					break;
				case ALWAYS_FALSE:
				case USE_DI_VALUE:
					value = false;
				}
				bpmnShape.setIsMessageVisible(value);
			}
		}
		else {
			if (be instanceof Participant && choreographyActivityShapeSet) {
				BPMNDIAttributeDefault df = getIsMessageVisible();
				switch(df) {
				case ALWAYS_TRUE:
					bpmnShape.setIsMessageVisible(true);
					break;
				case ALWAYS_FALSE:
					bpmnShape.setIsMessageVisible(false);
					break;
				}
			}
		}
		
		// isMarkerVisible only applies to ExclusiveGateway
		if (!isMarkerVisibleSet) {
			if (be instanceof ExclusiveGateway) {
				BPMNDIAttributeDefault df = getIsMarkerVisible();
				switch(df) {
				case ALWAYS_TRUE:
				case DEFAULT_TRUE:
					bpmnShape.setIsMarkerVisible(true);
					break;
				case ALWAYS_FALSE:
				case USE_DI_VALUE:
					bpmnShape.setIsMarkerVisible(false);
					break;
				}
			}
		}
		else {
			if (be instanceof ExclusiveGateway) {
				BPMNDIAttributeDefault df = getIsMarkerVisible();
				switch(df) {
				case ALWAYS_TRUE:
					bpmnShape.setIsMarkerVisible(true);
					break;
				case ALWAYS_FALSE:
					bpmnShape.setIsMarkerVisible(false);
					break;
				}
			}
		}
	}

	// TODO: use CNF for indigo & future - keep ResourceNavigator for backward compatibility
	public static IProject getActiveProject() {
		if (activeProject!=null)
			return activeProject;
		
		if(!PlatformUI.isWorkbenchRunning()) {
			return null;
		}
		
		IWorkbench workbench = PlatformUI.getWorkbench(); 
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window!=null) {
			IWorkbenchPage page = window.getActivePage();
			if (page!=null) {
				IViewPart[] parts = page.getViews();
		
				for (int i = 0; i < parts.length; i++) {
					if (parts[i] instanceof ResourceNavigator) {
						ResourceNavigator navigator = (ResourceNavigator) parts[i];
						StructuredSelection sel = (StructuredSelection) navigator.getTreeViewer().getSelection();
						IResource resource = (IResource) sel.getFirstElement();
						if (resource!=null) {
							activeProject = resource.getProject();
							break;
						}
					}
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
