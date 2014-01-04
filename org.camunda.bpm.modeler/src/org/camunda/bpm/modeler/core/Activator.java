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
 * @author Ivar Meikas
 ******************************************************************************/
package org.camunda.bpm.modeler.core;

import org.camunda.bpm.modeler.core.preferences.Bpmn2Preferences;
import org.camunda.bpm.modeler.core.runtime.TargetRuntime;
import org.camunda.bpm.modeler.plugin.core.Extensions;
import org.camunda.bpm.modeler.plugin.core.impl.ExtensionImpl;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.graphiti.ui.internal.GraphitiUIPlugin;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The {@link Plugin} activator that controls the
 * modeler plugin life cycle.
 * 
 * @author nico.rehwaldt
 */
public class Activator extends AbstractUIPlugin {

	// plugin id
	public static final String PLUGIN_ID = "org.camunda.bpm.modeler"; 

	// shared instance
	private static Activator INSTANCE;

	// plugin extensions
	private ExtensionImpl extensions;

	
	public Activator() {
		this.extensions = new ExtensionImpl();
	}

	
	//// live cycle support
	
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		INSTANCE = this;
		
		initPreferences();
		loadExtensions();
	}

	public void stop(BundleContext context) throws Exception {
		INSTANCE = null;
		super.stop(context);
	}


	//// internal loading
	
	private void loadExtensions() {
		this.extensions.load();
	}

	private void initPreferences() {
		getPreferenceStore();
		
		if (PlatformUI.isWorkbenchRunning()) {
			Bpmn2Preferences.getInstance().load();
		} else {
			// load current target runtime during tests
			TargetRuntime.getDefaultRuntime();
		}
	}
	
	
	//// static providers
	
	public static Extensions getExtensions() {
		return INSTANCE.extensions;
	}
	
	public static Activator getDefault() {
		return INSTANCE;
	}
	
	@Override
	public ImageRegistry getImageRegistry() {
		return GraphitiUIPlugin.getDefault().getImageRegistry();
	}

	//// logging helpers
	
	public static void logStatus(IStatus status) {
		Platform.getLog(INSTANCE.getBundle()).log(status);
	}
	
	public static void logError(Exception e) {
		logStatus(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
	}

	public static void showErrorWithLogging(Exception e){
		Status s = createStatus(e);
		logStatus(s);
		ErrorDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "An error occured", null, s);
	}

	private static Status createStatus(Exception e) {
		return new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
	}
}
