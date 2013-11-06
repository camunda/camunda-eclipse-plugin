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

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;

import org.camunda.bpm.modeler.core.preferences.Bpmn2Preferences;
import org.camunda.bpm.modeler.plugin.core.ExtensionRegistry;
import org.camunda.bpm.modeler.plugin.core.Extensions;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
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
	private ExtensionRegistry extensions;

	
	public Activator() {
		this.extensions = new ExtensionRegistry();
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
		}
	}
	
	
	//// static providers
	
	public static Extensions getExtensions() {
		return INSTANCE.extensions;
	}
	
	public static Activator getDefault() {
		return INSTANCE;
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
	
	/**
	 * Initializes the table of images used in this plugin.
	 */
	@Override
	protected ImageRegistry createImageRegistry() {
		ImageRegistry registry = super.createImageRegistry();
		URL baseURL = getBundle().getEntry("/"); //$NON-NLS-1$

		// A little reflection magic ... so that we don't
		// have to add the createImageDescriptor every time
		// we add it to the IConstants ..
		Field fields[] = IConstants.class.getFields();	
		for(int i=0; i < fields.length; i++) {
			Field f = fields[i];
			if (f.getType() != String.class) { 
				continue;
			}
			String name = f.getName();
			if (name.startsWith("ICON_") || name.startsWith("CURSOR_") || name.startsWith("IMAGE_")) {   //$NON-NLS-1$ //$NON-NLS-2$
				try {
					String value = (String) f.get(null);
					createImageDescriptor(registry, value, baseURL);
				} catch (Exception e) {
					logError(e);
				}
			}			
		}
		return registry;
	}

	/**
	 * Creates an image descriptor and places it in the image registry.
	 */
	private void createImageDescriptor(ImageRegistry registry, String id, URL baseURL) {
		URL url = null;
		try {
			url = new URL(baseURL, IConstants.ICON_PATH + id);
		} catch (MalformedURLException e) {
			logError(e);
		}
		ImageDescriptor desc = ImageDescriptor.createFromURL(url);
		registry.put(id, desc);
	}

	public Image getImage(String id) {
		return getImageRegistry().get(id);
	}
	
	public ImageDescriptor getImageDescriptor(String id) {
		return getImageRegistry().getDescriptor(id);
	}

	private static Status createStatus(Exception e) {
		return new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
	}
}
