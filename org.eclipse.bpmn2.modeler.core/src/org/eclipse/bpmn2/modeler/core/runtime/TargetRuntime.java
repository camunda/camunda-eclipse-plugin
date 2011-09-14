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
package org.eclipse.bpmn2.modeler.core.runtime;

import java.util.ArrayList;

import org.eclipse.bpmn2.modeler.core.AbstractPropertyChangeListenerProvider;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.IBpmn2RuntimeExtension;
import org.eclipse.bpmn2.modeler.core.features.activity.task.ICustomTaskFeature;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerResourceImpl;
import org.eclipse.bpmn2.modeler.core.runtime.CustomTaskDescriptor.Property;
import org.eclipse.bpmn2.modeler.core.runtime.CustomTaskDescriptor.Value;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;


public class TargetRuntime extends AbstractPropertyChangeListenerProvider {

	// extension point ID for Target Runtimes
	public static final String RUNTIME_ID = "org.eclipse.bpmn2.modeler.runtime";
	public static final String DEFAULT_RUNTIME_ID = "org.eclipse.bpmn2.modeler.runtime.none";
	
	// our cached registry of target runtimes contributed by other plugins
	protected static TargetRuntime targetRuntimes[];
	
	protected String name;
	protected String[] versions;
	protected String id;
	protected String description;
	private IBpmn2RuntimeExtension runtimeExtension;
	protected ModelDescriptor modelDescriptor;
	protected ArrayList<Bpmn2TabDescriptor> tabDescriptors;
	protected ArrayList<Bpmn2SectionDescriptor> sectionDescriptors;
	protected ArrayList<CustomTaskDescriptor> customTasks;
	
	public TargetRuntime(String id, String name, String versions, String description) {
		this.id = id;
		this.name = name;
		if (versions!=null)
			this.versions = versions.split("[, ]");
		this.description = description;
	}
	
	public static TargetRuntime getRuntime(String id) {
		getAllRuntimes();
		for (TargetRuntime rt : getAllRuntimes()) {
			if (rt.id.equals(id))
				return rt;
		}
		return null;
	}
	
	public static TargetRuntime getDefaultRuntime() {
		return getRuntime(DEFAULT_RUNTIME_ID);
	}
	
	public void setResourceSet(ResourceSet resourceSet) {
		resourceSet.getResourceFactoryRegistry().getContentTypeToFactoryMap().put(
				Bpmn2ModelerResourceImpl.BPMN2_CONTENT_TYPE_ID, modelDescriptor.getResourceFactory());
	}
	
	public static TargetRuntime[] getAllRuntimes() {
		if (targetRuntimes==null) {
			// load runtimes contributions from other plugins
			ArrayList<TargetRuntime> rtList = new ArrayList<TargetRuntime>();
			
			IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
					RUNTIME_ID);

			try {
				for (IConfigurationElement e : config) {
					if (e.getName().equals("runtime")) {
						String id = e.getAttribute("id");
						String name = e.getAttribute("name");
						String versions = e.getAttribute("versions");
						String description = e.getAttribute("description");
						TargetRuntime rt = new TargetRuntime(id,name,versions,description);
						
						rt.setRuntimeExtension((IBpmn2RuntimeExtension) e.createExecutableExtension("class"));
					
						rtList.add(rt);
					}
				}
				
				targetRuntimes = rtList.toArray(new TargetRuntime[rtList.size()]);
				
				for (IConfigurationElement e : config) {
					if (!e.getName().equals("runtime")) {
						TargetRuntime rt = getRuntime(e);
						
						if (e.getName().equals("model")) {
							ModelDescriptor md = new ModelDescriptor();
							if (e.getAttribute("uri")!=null) {
								String uri = e.getAttribute("uri");
								md.setEPackage(EPackage.Registry.INSTANCE.getEPackage(uri));
								md.setEFactory(md.getEPackage().getEFactoryInstance());
							}
							if (e.getAttribute("resourceFactory")!=null)
								md.setResourceFactory((ResourceFactoryImpl) e.createExecutableExtension("resourceFactory"));
							rt.setModelDescriptor(md);
						}
						else if (e.getName().equals("propertyTab")) {
							String id = e.getAttribute("id");
							String category = e.getAttribute("category");
							String label = e.getAttribute("label");
							
							Bpmn2TabDescriptor td = new Bpmn2TabDescriptor(id,category,label);
							td.afterTab = e.getAttribute("afterTab");
							td.replaceTab = e.getAttribute("replaceTab");
							String indented = e.getAttribute("indented");
							td.indented = indented!=null && indented.trim().equalsIgnoreCase("true");
							
							rt.getTabs().add(td);
						}
						else if (e.getName().equals("customTask")) {
							String id = e.getAttribute("id");
							String name = e.getAttribute("name");
							CustomTaskDescriptor ct = new CustomTaskDescriptor(id,name);
							ct.type = e.getAttribute("type");
							ct.description = e.getAttribute("description");
							ct.createFeature = (ICustomTaskFeature) e.createExecutableExtension("createFeature");
							ct.createFeature.setCustomTaskDescriptor(ct);
							ct.createFeature.setId(id);
							getCustomTaskProperties(ct,e);
							rt.addCustomTask(ct);
						}
					}
				}
				
				for (IConfigurationElement e : config) {
					if (!e.getName().equals("runtime")) {
						TargetRuntime rt = getRuntime(e);

						if (e.getName().equals("propertySection")) {
							String id = e.getAttribute("id");
							String tab = e.getAttribute("tab");
							String label = e.getAttribute("label");
	
							Bpmn2SectionDescriptor sd = new Bpmn2SectionDescriptor(id,tab,label);
							sd.sectionClass = (AbstractPropertySection) e.createExecutableExtension("class");
							sd.name = e.getAttribute("name");
							sd.afterSection = e.getAttribute("afterSection");
							sd.filter = e.getAttribute("filter");
							sd.enablesFor = e.getAttribute("enablesFor");
							String type = e.getAttribute("type");
							if (type!=null && !type.isEmpty())
								sd.appliesToClass = Class.forName(type);

							rt.getSections().add(sd);
						}
					}
				}
				
				// associate property sections with their respective tabs
				for (TargetRuntime rt : targetRuntimes) {
					for (Bpmn2TabDescriptor td : rt.getTabs()) {
						for (Bpmn2SectionDescriptor sd : rt.getSections()) {
							if (sd.tab.equals(td.id)) {
								if (td.unfilteredSectionDescriptors==null)
									td.unfilteredSectionDescriptors = new ArrayList<Bpmn2SectionDescriptor>();
								td.unfilteredSectionDescriptors.add(sd);
							}
						}
					}
				}
				
			} catch (Exception ex) {
				Activator.logError(ex);
			}
		}
		return targetRuntimes;
	}
	
	private static Object getCustomTaskProperties(CustomTaskDescriptor ct, IConfigurationElement e) {
		
		String elem = e.getName();
		if ("value".equals(elem)) {
			String id = e.getAttribute("id");
			Value val = new Value(id);
			for (IConfigurationElement cc : e.getChildren()) {
				Object propValue = getCustomTaskProperties(ct, cc);
				val.getValues().add(propValue);
			}
			return val;
		}
		else {
			if (e.getChildren().length==0) {
				if ("property".equals(elem)) {
					String name = e.getAttribute("name");
					String value = e.getAttribute("value");
					String description = e.getAttribute("description");
					Property prop = new Property(name,description);
					if (value!=null)
						prop.getValues().add(value);
					return prop;
				}
			}
			else {
				for (IConfigurationElement c : e.getChildren()) {
					elem = c.getName();
					String name = c.getAttribute("name");
					String value = c.getAttribute("value");
					String description = c.getAttribute("description");
					Property prop = new Property(name,description); 
					if (value!=null)
						prop.getValues().add(value);
					for (IConfigurationElement cc : c.getChildren()) {
						Object propValue = getCustomTaskProperties(ct, cc);
						prop.getValues().add(propValue);
					}
					ct.getProperties().add(prop);
				}
			}
		}
		return null;
	}
	
	private static TargetRuntime getRuntime(IConfigurationElement e) throws InvalidRegistryObjectException, Exception {
		String runtimeId = e.getAttribute("runtimeId");
		TargetRuntime rt = getRuntime(runtimeId);
		if (rt==null) {
			throw new Exception("Plug-in " + Activator.PLUGIN_ID +
					" was unable to find the target runtime with id " +
					runtimeId +
					" referenced in the " + e.getName() + " section "
					);
		}
		return rt;
	}
	
	public ModelDescriptor getModelDescriptor() {
		return modelDescriptor;
	}
	
	public void setModelDescriptor(ModelDescriptor md) {
		md.setRuntime(this);
		this.modelDescriptor = md;
	}
	
	public String getName() {
		return name;
	}
	
	public String getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ArrayList<CustomTaskDescriptor> getCustomTasks()
	{
		if (customTasks==null) {
			customTasks = new ArrayList<CustomTaskDescriptor>();
		}
		return customTasks;
	}
	
	public void addCustomTask(CustomTaskDescriptor ct) {
		ct.setRuntime(this);
		getCustomTasks().add(ct);
	}
	
	private static void addAfterTab(ArrayList<Bpmn2TabDescriptor> list, Bpmn2TabDescriptor tab) {
		
		getAllRuntimes();
		String afterTab = tab.getAfterTab();
		if (afterTab!=null && !afterTab.isEmpty() && !afterTab.equals("top")) {
			for (TargetRuntime rt : targetRuntimes) {
				for (Bpmn2TabDescriptor td : rt.getTabs()) {
					if (tab!=td) {
						if (td.getId().equals(afterTab) || afterTab.equals(td.getReplaceTab())) {
							addAfterTab(list,td);
							if (!list.contains(td))
								list.add(td);
						}
					}
				}
			}
		}
	}

	private ArrayList<Bpmn2TabDescriptor> getTabs() {
		if (tabDescriptors==null)
			tabDescriptors = new ArrayList<Bpmn2TabDescriptor>();
		return tabDescriptors;
	}

	public Bpmn2TabDescriptor getTabDescriptor(String id) {
		for (Bpmn2TabDescriptor tab : getTabs()) {
			if (tab.getId().equals(id))
				return tab;
		}
		return null;
	}
	
	/**
	 * @return
	 */
	public ArrayList<Bpmn2TabDescriptor> getTabDescriptors() {
		ArrayList<Bpmn2TabDescriptor> list = new ArrayList<Bpmn2TabDescriptor>();
		for (Bpmn2TabDescriptor tab : getTabs()) {
			addAfterTab(list, tab);
			if (!list.contains(tab))
				list.add(tab);
		}
		if (list.isEmpty() && this!=getRuntime(DEFAULT_RUNTIME_ID)) {
			return getRuntime(DEFAULT_RUNTIME_ID).getTabDescriptors();
		}
		
		return list;
	}
	
	private static void addAfterSection(ArrayList<Bpmn2SectionDescriptor> list, Bpmn2SectionDescriptor section) {
		
		getAllRuntimes();
		String afterSection = section.getAfterSection();
		if (afterSection!=null) {
			for (TargetRuntime rt : targetRuntimes) {
				for (Bpmn2SectionDescriptor td : rt.getSections()) {
					if (td.getId().equals(afterSection)) {
						addAfterSection(list,td);
						list.add(td);
						return;
					}
				}
			}
		}
	}
	
	private ArrayList<Bpmn2SectionDescriptor> getSections() {
		if (sectionDescriptors==null)
			sectionDescriptors = new ArrayList<Bpmn2SectionDescriptor>();
		return sectionDescriptors;
	}
	
	/**
	 * @return
	 */
	public ArrayList<Bpmn2SectionDescriptor> getSectionDescriptors() {
		ArrayList<Bpmn2SectionDescriptor> list = new ArrayList<Bpmn2SectionDescriptor>();
		for (Bpmn2SectionDescriptor section : getSections()) {
			addAfterSection(list, section);
			if (!list.contains(section))
				list.add(section);
		}
		if (list.isEmpty() && this!=getRuntime(DEFAULT_RUNTIME_ID)) {
			return getRuntime(DEFAULT_RUNTIME_ID).getSectionDescriptors();
		}
		
		return list;
	}

	public IBpmn2RuntimeExtension getRuntimeExtension() {
		return runtimeExtension;
	}

	public void setRuntimeExtension(IBpmn2RuntimeExtension runtimeExtension) {
		this.runtimeExtension = runtimeExtension;
	}
}
