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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.EndPoint;
import org.eclipse.bpmn2.Import;
import org.eclipse.bpmn2.Interface;
import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.ItemKind;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.modeler.core.adapters.AdapterUtil;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;

/**
 * @author Bob Brodt
 *
 */
public class ImportUtil {

	public static Import addImport(EObject modelObject, Object importObject) {
		Resource resource = modelObject.eResource();
		return ImportUtil.addImport(resource,importObject);
	}

	public static boolean removeImport(Import imp) {
		Definitions definitions = ModelUtil.getDefinitions(imp);
		boolean canRemove = false;
		boolean canRemoveNamespace = true;
		String location = imp.getLocation();
		String namespace = imp.getNamespace();
		for (Import i : definitions.getImports()) {
			if (i!=imp) {
				String loc1 = i.getLocation();
				String ns1 = i.getNamespace();
				// different import locations, same namespace?
				if (loc1!=null && location!=null && !loc1.equals(location) &&
						ns1!=null && namespace!=null && ns1.equals(namespace)) {
					// this namespace is still in use by another import!
					canRemoveNamespace = false;
					break;
				}
			}
			else
				canRemove = true;
		}
		
		if (canRemoveNamespace)
			NamespaceUtil.removeNamespace(imp.eResource(), namespace);
		
		if (canRemove) {
			String type = imp.getImportType();
			String loc = imp.getLocation();
			
			if ("http://schemas.xmlsoap.org/wsdl/".equals(type)) {
				List<Interface> list = new ArrayList<Interface>();
				for (RootElement re : definitions.getRootElements()) {
					if (re instanceof Interface)
						list.add((Interface)re);
				}
				for (Interface intf : list) {
					Object ref = intf.getImplementationRef();
					if (ref instanceof EObject) {
						URI uri = EcoreUtil.getURI((EObject)ref);
						String uriString = uri.trimFragment().toString();
						if (loc.equals(uriString))
							deleteInterface(definitions, intf);
					}
				}
			}
			else if ("http://www.w3.org/2001/XMLSchema".equals(type)) {
				List<ItemDefinition> list = new ArrayList<ItemDefinition>();
				for (RootElement re : definitions.getRootElements()) {
					if (re instanceof ItemDefinition)
						list.add((ItemDefinition)re);
				}
				for (ItemDefinition itemDef : list) {
					Object ref = itemDef.getStructureRef();
					if (ref instanceof EObject) {
						URI uri = EcoreUtil.getURI((EObject)ref);
						String uriString = uri.trimFragment().toString();
						if (loc.equals(uriString))
							EcoreUtil.delete(itemDef);
					}
				}
			}
			else if ("http://www.java.com/javaTypes".equals(type)) {
				String className = imp.getLocation();
				boolean deleted = false;
				IJavaProject[] projects = JavaProjectClassLoader.findProject(className);
				for (int i=0; i<projects.length; ++i) {
					JavaProjectClassLoader loader = new JavaProjectClassLoader(projects[i]);
					Class clazz = loader.findClass(className);
					if (clazz!=null) {
						deleteItemDefinition(definitions, imp, clazz);
						deleted = true;
					}
				}
				if (!deleted)
					deleteItemDefinition(definitions, imp, className);
			}
			else if ("http://www.omg.org/spec/BPMN/20100524/MODEL".equals(type)) {
				
			}
			definitions.getImports().remove(imp);
		}
		return canRemove;
	}

	public static Import addImport(Resource resource, final Object importObject) {
		Import imp = null;
		if (resource instanceof Bpmn2Resource) {
			final Definitions definitions = (Definitions) resource.getContents().get(0).eContents().get(0);
	
			if (importObject instanceof org.eclipse.wst.wsdl.Definition) {
				// WSDL Definition
				Definition wsdlDefinition = (Definition)importObject;
	
				imp = Bpmn2Factory.eINSTANCE.createImport();
				imp.setImportType("http://schemas.xmlsoap.org/wsdl/");
				imp.setLocation(wsdlDefinition.getLocation());
				imp.setNamespace(wsdlDefinition.getTargetNamespace());
			}
			else if (importObject instanceof XSDSchema){
				// XSD Schema
				XSDSchema schema = (XSDSchema)importObject;
				
				imp = Bpmn2Factory.eINSTANCE.createImport();
				imp.setImportType("http://www.w3.org/2001/XMLSchema");
				imp.setLocation(schema.getSchemaLocation());
				imp.setNamespace(schema.getTargetNamespace());
			}
			else if (importObject instanceof Class) {
				// Java class
				Class clazz = (Class)importObject;
				// TODO: create a location URI for the class file
//				ClassLoader cl = clazz.getClassLoader();
//				String name = clazz.getName().replaceAll("\\.", "/").concat(".class");
//				java.net.URL url = cl.getResource(name);
//				URI uri = URI.createPlatformPluginURI(url.getPath(), true);
				imp = Bpmn2Factory.eINSTANCE.createImport();
				imp.setImportType("http://www.java.com/javaTypes");
				imp.setLocation(clazz.getName());
				imp.setNamespace("http://" + clazz.getPackage().getName());
			}
			else if (importObject instanceof org.eclipse.bpmn2.Definitions) {
				// BPMN 2.0 Diagram file
				Definitions defs = (Definitions)importObject;
				
				imp = Bpmn2Factory.eINSTANCE.createImport();
				imp.setImportType("http://www.omg.org/spec/BPMN/20100524/MODEL");
				imp.setLocation(defs.eResource().getURI().toString());
				imp.setNamespace(defs.getTargetNamespace());
			}

			if (imp!=null) {
				// make sure this is a new one!
				for (Import i : definitions.getImports()) {
					String location = i.getLocation();
					if (location!=null && location.equals(imp.getLocation())) {
						imp = null;
						break;
					}
				}
			}
			
			if (imp!=null) {
				definitions.getImports().add(imp);
				NamespaceUtil.addNamespace(imp.eResource(), imp.getNamespace());

				if (importObject instanceof org.eclipse.wst.wsdl.Definition) {
					// WSDL Definition
					Definition wsdlDefinition = (Definition)importObject;
		
					for (Binding b :(List<Binding>)wsdlDefinition.getEBindings()) {
						createInterface(definitions, imp,  b.getEPortType());
					}
				}
				else if (importObject instanceof XSDSchema){
					XSDSchema schema = (XSDSchema)importObject;

					for (XSDElementDeclaration elem : schema.getElementDeclarations()) {
						createItemDefinition(definitions, imp, elem, ItemKind.INFORMATION);
					}
				}
				else if (importObject instanceof Class) {
					Class clazz = (Class)importObject;

					createItemDefinition(definitions, imp, clazz);
				}
				else if (importObject instanceof org.eclipse.bpmn2.Definitions) {
					// what to do here?
				}
			}
		}
		return imp;
	}

	private static Interface createInterface(Definitions definitions, Import imp, PortType portType) {
		Interface intf = Bpmn2Factory.eINSTANCE.createInterface();
		intf.setName(portType.getQName().getLocalPart());
		intf.setImplementationRef(portType);
		Interface i = findInterface(definitions,intf);
		if (i!=null)
			return i;
		
		definitions.getRootElements().add(intf);
		ModelUtil.setID(intf);
		createOperations(definitions, imp, intf, portType);
		
		return intf;
	}

	private static void deleteInterface(Definitions definitions, Interface intf) {
		deleteOperations(definitions,intf);
		EcoreUtil.delete(intf);
	}
	
	private static Interface findInterface(Definitions definitions, Interface intf) {
		ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(intf, ExtendedPropertiesAdapter.class);
		if (adapter!=null) {
			for (RootElement re : definitions.getRootElements()) {
				if (re instanceof Interface) {
					Interface i = (Interface)re;
					if (compare(i.getName(),intf.getName())) {
						if (compare(i.getImplementationRef(),intf.getImplementationRef()))
							return i;
					}
				}
			}
		}
		return null;
	}

	private static void createOperations(Definitions definitions, Import imp, Interface intf, PortType portType) {
		for (Operation wsdlop : (List<Operation>)portType.getEOperations()) {
			org.eclipse.bpmn2.Operation bpmn2op = Bpmn2Factory.eINSTANCE.createOperation();
			bpmn2op.setImplementationRef(wsdlop);
			bpmn2op.setName(wsdlop.getName());
			
			Input input = wsdlop.getEInput();
			if (input!=null) {
				org.eclipse.bpmn2.Message bpmn2msg = createMessage(definitions, imp, input.getEMessage());
				bpmn2op.setInMessageRef(bpmn2msg);
			}
			
			Output output = wsdlop.getEOutput();
			if (output!=null) {
				org.eclipse.bpmn2.Message bpmn2msg = createMessage(definitions, imp, output.getEMessage());
				bpmn2op.setOutMessageRef(bpmn2msg);
			}
			
			for (Fault fault : (List<Fault>)wsdlop.getEFaults()) {
				bpmn2op.getErrorRefs().add(createError(definitions, imp, fault));
			}
			
			if (findOperation(definitions, bpmn2op)==null) {
				intf.getOperations().add(bpmn2op);
				ModelUtil.setID(bpmn2op);
			}
		}
	}

	private static void deleteOperations(Definitions definitions, Interface intf) {
		List<org.eclipse.bpmn2.Operation> opList = new ArrayList<org.eclipse.bpmn2.Operation>();
		for (org.eclipse.bpmn2.Operation bpmn2op : intf.getOperations()) {
			opList.add(bpmn2op);
		}
		for (org.eclipse.bpmn2.Operation bpmn2op : opList) {
			org.eclipse.bpmn2.Message m;
			m = bpmn2op.getInMessageRef();
			if (m!=null) {
				deleteMessage(definitions,m);
			}
			m = bpmn2op.getOutMessageRef();
			if (m!=null) {
				deleteMessage(definitions,m);
			}
			
			List<org.eclipse.bpmn2.Error> errorList = new ArrayList<org.eclipse.bpmn2.Error>();
			errorList.addAll(bpmn2op.getErrorRefs());
			for (org.eclipse.bpmn2.Error e : errorList) {
				ItemDefinition itemDef = e.getStructureRef();
				if (itemDef!=null)
					EcoreUtil.delete(itemDef);
				EcoreUtil.delete(e);
			}
			EcoreUtil.delete(bpmn2op);
		}
	}

	private static org.eclipse.bpmn2.Operation findOperation(Definitions definitions, org.eclipse.bpmn2.Operation bpmn2op) {
		ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(bpmn2op, ExtendedPropertiesAdapter.class);
		if (adapter!=null) {
			for (RootElement re : definitions.getRootElements()) {
				if (re instanceof org.eclipse.bpmn2.Operation) {
					if (adapter.getObjectDescriptor().equals(re))
						return (org.eclipse.bpmn2.Operation)re;
				}
			}
		}
		return null;
	}

	private static org.eclipse.bpmn2.Message createMessage(Definitions definitions, Import imp, Message wsdlmsg) {
		org.eclipse.bpmn2.Message bpmn2msg = Bpmn2Factory.eINSTANCE.createMessage();
		ItemDefinition itemDef = createItemDefinition(definitions, imp, wsdlmsg);
		bpmn2msg.setItemRef(itemDef);
		bpmn2msg.setName(wsdlmsg.getQName().getLocalPart());
		
		org.eclipse.bpmn2.Message m = findMessage(definitions, bpmn2msg);
		if (m!=null)
			return m;
		
		definitions.getRootElements().add(bpmn2msg);
		ModelUtil.setID(bpmn2msg);
		
		return bpmn2msg;
	}

	private static void deleteMessage(Definitions definitions, org.eclipse.bpmn2.Message msg) {
		ItemDefinition itemDef = msg.getItemRef();
		if (itemDef!=null)
			EcoreUtil.delete(itemDef);
		EcoreUtil.delete(msg);
	}

	private static org.eclipse.bpmn2.Message findMessage(Definitions definitions, org.eclipse.bpmn2.Message msg) {
		ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(msg, ExtendedPropertiesAdapter.class);
		if (adapter!=null) {
			for (RootElement re : definitions.getRootElements()) {
				if (re instanceof org.eclipse.bpmn2.Message) {
					if (adapter.getObjectDescriptor().equals(re))
						return (org.eclipse.bpmn2.Message)re;
				}
			}
		}
		return null;
	}

	private static org.eclipse.bpmn2.Error createError(Definitions definitions, Import imp, Fault fault) {
		org.eclipse.bpmn2.Error error = Bpmn2Factory.eINSTANCE.createError();
		ItemDefinition itemDef = createItemDefinition(definitions, imp, fault, ItemKind.INFORMATION);
		error.setName(fault.getName());
		error.setStructureRef(itemDef);
		org.eclipse.bpmn2.Error e = findError(definitions, error);
		if (e!=null)
			return e;

		definitions.getRootElements().add(error);
		ModelUtil.setID(error);
		
		return error;
	}
	
	private static org.eclipse.bpmn2.Error findError(Definitions definitions, org.eclipse.bpmn2.Error error) {
		ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(error, ExtendedPropertiesAdapter.class);
		if (adapter!=null) {
			for (RootElement re : definitions.getRootElements()) {
				if (re instanceof org.eclipse.bpmn2.Error) {
					if (adapter.getObjectDescriptor().equals(re))
						return (org.eclipse.bpmn2.Error)re;
				}
			}
		}
		return null;
	}
	
	private static ItemDefinition createItemDefinition(Definitions definitions, Import imp, Message wsdlmsg) {
		return createItemDefinition(definitions, imp, wsdlmsg, ItemKind.INFORMATION);
	}

	private static ItemDefinition createItemDefinition(Definitions definitions, Import i, Class clazz) {
		for (Class c : clazz.getDeclaredClasses()) {
			createItemDefinition(definitions, i, c);
		}
		return createItemDefinition(definitions, i, clazz.getName(), ItemKind.PHYSICAL);
	}
	
	private static ItemDefinition createItemDefinition(Definitions definitions, Import imp, String structName, ItemKind kind) {
		EObject structureRef = ModelUtil.createStringWrapper(structName);
		return createItemDefinition(definitions, imp, structureRef, kind);
	}
	
	private static ItemDefinition createItemDefinition(Definitions definitions, Import imp, EObject structureRef, ItemKind kind) {
		ItemDefinition itemDef = Bpmn2Factory.eINSTANCE.createItemDefinition();
		itemDef.setImport(imp);
		itemDef.setItemKind(kind);
		itemDef.setStructureRef(structureRef);
		ItemDefinition i = findItemDefinition(definitions, itemDef);
		if (i!=null)
			return i;

		definitions.getRootElements().add(itemDef);
		ModelUtil.setID(itemDef);
		
		return itemDef;
	}
	
	private static ItemDefinition findItemDefinition(Definitions definitions, Import imp, EObject structureRef, ItemKind kind) {
		ItemDefinition itemDef = Bpmn2Factory.eINSTANCE.createItemDefinition();
		itemDef.setImport(imp);
		itemDef.setItemKind(kind);
		itemDef.setStructureRef(structureRef);
		return findItemDefinition(definitions, itemDef);
	}
	
	private static ItemDefinition findItemDefinition(Definitions definitions, ItemDefinition itemDef) {
		ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(itemDef, ExtendedPropertiesAdapter.class);
		if (adapter!=null) {
			for (RootElement re : definitions.getRootElements()) {
				if (re instanceof ItemDefinition) {
					if (adapter.getObjectDescriptor().equals(re))
						return (ItemDefinition)re;
				}
			}
		}
		return null;
	}

	private static void deleteItemDefinition(Definitions definitions, Import imp, Class clazz) {
		for (Class c : clazz.getDeclaredClasses()) {
			deleteItemDefinition(definitions, imp, c);
		}
		EObject structureRef = ModelUtil.createStringWrapper(clazz.getName());
		ItemDefinition itemDef = findItemDefinition(definitions, imp, structureRef, ItemKind.PHYSICAL);
		if (itemDef!=null) {
			EcoreUtil.delete(itemDef);
		}
	}

	private static void deleteItemDefinition(Definitions definitions, Import imp, String structName) {
		EObject structureRef = ModelUtil.createStringWrapper(structName);
		ItemDefinition itemDef = findItemDefinition(definitions, imp, structureRef, ItemKind.PHYSICAL);
		if (itemDef==null)
			itemDef = findItemDefinition(definitions, imp, structureRef, ItemKind.INFORMATION);
		
		if (itemDef!=null) {
			EcoreUtil.delete(itemDef);
		}
	}
	
	private static boolean compare(Object v1, Object v2) {
		if (v1==null) {
			if (v2!=null)
				return false;
		}
		else if (v2==null) {
			if (v1!=null)
				return false;
		}
		return v1.equals(v2);
	}
}
