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

package org.eclipse.bpmn2.modeler.core.utils;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.launching.JavaRuntime;

public class JavaProjectClassLoader extends ClassLoader {
	private IJavaProject javaProject;
	private static final String PROTOCOL_PREFIX = "file:///";

	public JavaProjectClassLoader(IJavaProject project) {
		super();
		if (project == null || !project.exists())
			throw new IllegalArgumentException("Invalid javaProject");
		this.javaProject = project;
	}

	public Class findClass(String className) {
		try {
			String[] classPaths = JavaRuntime.computeDefaultRuntimeClassPath(javaProject);
			URL[] urls = new URL[classPaths.length];
			for (int i = 0; i < classPaths.length; i++)
				urls[i] = new URL(PROTOCOL_PREFIX + computeForURLClassLoader(classPaths[i]));
			ClassLoader loader = new URLClassLoader(urls);
			Class classObject = loader.loadClass(className);
			return classObject;

		} catch (Exception e) {
		}
		return null;
	}
	
	public List<Class> findClasses(String classNamePattern) {
		if (classNamePattern.endsWith(".java")) {
			classNamePattern = classNamePattern.substring(0,classNamePattern.lastIndexOf("."));
		}
		if (!classNamePattern.endsWith("*"))
			classNamePattern += "*";
		SearchPattern pattern = SearchPattern.createPattern(classNamePattern,
	            IJavaSearchConstants.TYPE, IJavaSearchConstants.TYPE,
	            SearchPattern.R_PATTERN_MATCH);
		final List<Class> results = new ArrayList<Class>();
		SearchEngine searchEngine = new SearchEngine();
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope((IJavaElement[]) new IJavaProject[] {javaProject});
		SearchRequestor requestor = new SearchRequestor() {
			public void acceptSearchMatch(SearchMatch match) {
				IJavaElement e = (IJavaElement) match.getElement();
				String elementName = e.getElementName();
				while (e!=null) {
					if (e instanceof IPackageFragment) {
						IPackageFragment pf = (IPackageFragment)e;
						String className = pf.getElementName() + "." + elementName;
						Class c = findClass(className);
						if (c!=null)
							results.add(c);
					}
					e = e.getParent();
				}
			}
		};
		try {
			searchEngine.search(
					pattern,
					new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() },
					scope,
					requestor,
					null);
		} catch (CoreException e) {
		}
		return results;
	}
	
	public static IJavaProject[] findProject(final String className) {
		SearchPattern pattern = SearchPattern.createPattern(className,
	            IJavaSearchConstants.TYPE, IJavaSearchConstants.TYPE,
	            SearchPattern.R_EXACT_MATCH);
		final List<IJavaProject> results = new ArrayList<IJavaProject>();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject p : projects) {
			try {
				if (p.isOpen() && p.hasNature(JavaCore.NATURE_ID)) {
					final IJavaProject javaProject = JavaCore.create(p);
					SearchEngine searchEngine = new SearchEngine();
					IJavaSearchScope scope = SearchEngine.createJavaSearchScope((IJavaElement[]) new IJavaProject[] {javaProject});
					SearchRequestor requestor = new SearchRequestor() {
						public void acceptSearchMatch(SearchMatch match) {
							IJavaElement e = (IJavaElement) match.getElement();
							String elementName = e.getElementName();
							while (e!=null) {
								if (e instanceof IPackageFragment) {
									IPackageFragment pf = (IPackageFragment)e;
									String n = pf.getElementName() + "." + elementName;
									if (className.equals(n))
										results.add(javaProject);
								}
								e = e.getParent();
							}
						}
					};
					searchEngine.search(
							pattern,
							new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() },
							scope,
							requestor,
							null);
				}
			} catch (Exception e) {
			}
		}
		return (IJavaProject[]) results.toArray(new IJavaProject[results.size()]);
	}

	private static String computeForURLClassLoader(String classpath) {
		if (!classpath.endsWith("/")) {
			File file = new File(classpath);
			if (file.exists() && file.isDirectory())
				classpath = classpath.concat("/");
		}
		return classpath;
	}
}