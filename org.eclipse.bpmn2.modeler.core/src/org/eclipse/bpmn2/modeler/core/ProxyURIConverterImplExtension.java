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
package org.eclipse.bpmn2.modeler.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;

public final class ProxyURIConverterImplExtension extends ExtensibleURIConverterImpl {
	private static final String DIR_NAME = "cache/";
	private String connectionTimeout;
	private String readTimeout;
	
	/**
	 * We provide local copies for some files from the web. Local copy names are requested url without starting
	 * "http://" and all '/' are replaced with '_'
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl#createInputStream(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public InputStream createInputStream(URI uri) throws IOException {
		InputStream stream = getInputStreamForUri(uri);
		if (stream != null) {
			return stream;
		}

		setDefaultTimeoutProperties();
		InputStream createInputStream = super.createInputStream(uri);
		restoreTimeoutProperties();
		
		return createInputStream;
	}

	/**
	 * We provide local copies for some files from the web. Local copy names are requested url without starting
	 * "http://" and all '/' are replaced with '_'
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl#createInputStream(org.eclipse.emf.common.util.URI,
	 *      java.util.Map)
	 */
	@Override
	public InputStream createInputStream(URI uri, java.util.Map<?, ?> options) throws IOException {
		InputStream stream = getInputStreamForUri(uri);
		if (stream != null) {
			return stream;
		}

		setDefaultTimeoutProperties();
		InputStream createInputStream = super.createInputStream(uri, options);
		restoreTimeoutProperties();
		
		return createInputStream;
	}

	private InputStream getInputStreamForUri(URI uri) throws IOException {
		if (uri.toString().startsWith("http://")) {
			return checkForLocalCopy(uri);
		}
		return null;
	}

	private InputStream checkForLocalCopy(URI uri) throws IOException {
		String fileName = uri.toString().substring(7).replace("/", "_");
		URL entry = Activator.getDefault().getBundle().getEntry(DIR_NAME + fileName);

		if (entry != null) {
			return entry.openStream();
		}
		return null;
	}
	
	private void saveTimeoutProperties() {
		if (connectionTimeout==null) {
			connectionTimeout = System.getProperty("sun.net.client.defaultConnectTimeout");
			if (connectionTimeout==null)
				connectionTimeout = "";
		}
		if (readTimeout==null) {
			readTimeout = System.getProperty("sun.net.client.defaultReadTimeout");
			if (readTimeout==null)
				readTimeout = "";
		}
	}
	
	private void restoreTimeoutProperties() {
		if(connectionTimeout!=null) {
			System.setProperty("sun.net.client.defaultConnectTimeout", connectionTimeout);
			connectionTimeout = null;
		}
		if (readTimeout!=null) {
			System.setProperty("sun.net.client.defaultReadTimeout", readTimeout);
			readTimeout = null;
		}
	}
	
	private void setDefaultTimeoutProperties() {
		saveTimeoutProperties();
		String timeout = Bpmn2Preferences.getInstance().getConnectionTimeout();
		System.setProperty("sun.net.client.defaultConnectTimeout", timeout);
		System.setProperty("sun.net.client.defaultReadTimeout", timeout);
	}
}