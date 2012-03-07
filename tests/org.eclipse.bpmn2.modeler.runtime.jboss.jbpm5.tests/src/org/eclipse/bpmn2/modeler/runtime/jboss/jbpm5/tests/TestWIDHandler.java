/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

import junit.framework.Assert;

import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.wid.WIDException;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.wid.WIDHandler;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.wid.WorkItemDefinition;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.Test;
import org.osgi.framework.Bundle;

/**
 * Basic tests for the WIDHandler
 * @author bfitzpat
 *
 */
public class TestWIDHandler {

	private String getFile( String filepath ) {
		try {
			if (filepath == null) {
				Bundle bundle = Activator.getDefault().getBundle();
				IPath path = new Path("widfiles/logemail.wid");
				URL setupUrl = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
				File setupFile = new File(FileLocator.toFileURL(setupUrl).toURI());
				filepath = setupFile.getAbsolutePath();
			} else {
				Bundle bundle = Activator.getDefault().getBundle();
				IPath path = new Path(filepath);
				URL setupUrl = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
				File setupFile = new File(FileLocator.toFileURL(setupUrl).toURI());
				filepath = setupFile.getAbsolutePath();
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuilder text = new StringBuilder();
	    String NL = System.getProperty("line.separator");
	    Scanner scanner = null;
	    try {
	    	scanner = new Scanner(new FileInputStream(filepath), "UTF-8");
	    	while (scanner.hasNextLine()){
	    		text.append(scanner.nextLine() + NL);
	    	}
	    	return text.toString();
	    } catch (FileNotFoundException e) {
	    	e.printStackTrace();
	    } finally {
	    	if (scanner != null)
	    		scanner.close();
	    }	
	    return null;
	}
	
	@Test
	public void testBasic() {
		System.out.println("testBasic: logemail.wid");
		String content = getFile(null);
		HashMap<String, WorkItemDefinition> widMap = new HashMap<String, WorkItemDefinition>();
		try {
			WIDHandler.evaluateWorkDefinitions(widMap, content);
		} catch (WIDException e) {
			Assert.fail("Failed with exception " + e.getMessage());
		}
		Assert.assertTrue(!widMap.isEmpty());
		java.util.Iterator<WorkItemDefinition> widIterator = widMap.values().iterator();
		while(widIterator.hasNext())
			System.out.println(widIterator.next().toString());
	}
	
	@Test
	public void testComplex() {
		System.out.println("testComplex: widfiles/Email.wid");
		String content = getFile("widfiles/Email.wid");
		HashMap<String, WorkItemDefinition> widMap = new HashMap<String, WorkItemDefinition>();
		try {
			WIDHandler.evaluateWorkDefinitions(widMap, content);
		} catch (WIDException e) {
			Assert.fail("Failed with exception " + e.getMessage());
		}
		Assert.assertTrue(!widMap.isEmpty());
		java.util.Iterator<WorkItemDefinition> widIterator = widMap.values().iterator();
		while(widIterator.hasNext()) {
			WorkItemDefinition wid = widIterator.next();
			Assert.assertTrue(wid.getEclipseCustomEditor() != null &&
					wid.getEclipseCustomEditor().trim().length() > 0);
			System.out.println(wid.toString());
		};
	}

	@Test
	public void testResults() {
		System.out.println("testResults: widfiles/java.wid");
		String content = getFile("widfiles/java.wid");
		HashMap<String, WorkItemDefinition> widMap = new HashMap<String, WorkItemDefinition>();
		try {
			WIDHandler.evaluateWorkDefinitions(widMap, content);
		} catch (WIDException e) {
			Assert.fail("Failed with exception " + e.getMessage());
		}
		Assert.assertTrue(!widMap.isEmpty());
		java.util.Iterator<WorkItemDefinition> widIterator = widMap.values().iterator();
		while(widIterator.hasNext()) {
			WorkItemDefinition wid = widIterator.next();
			Assert.assertTrue(!wid.getResults().isEmpty());
			System.out.println(wid.toString());
		}
	}

	@Test
	public void testFail() {
		System.out.println("testFail: no wid");
		HashMap<String, WorkItemDefinition> widMap = new HashMap<String, WorkItemDefinition>();
		try {
			WIDHandler.evaluateWorkDefinitions(widMap, null);
		} catch (WIDException e) {
			Assert.assertTrue(e != null);
		}
	}
}
