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

import java.util.List;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.EndPoint;
import org.eclipse.bpmn2.Import;
import org.eclipse.bpmn2.Interface;
import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.ItemKind;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
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
		Definitions defs = ModelUtil.getDefinitions(imp);
		boolean canRemove = false;
		boolean canRemoveNamespace = true;
		String location = imp.getLocation();
		String namespace = imp.getNamespace();
		for (Import i : defs.getImports()) {
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
			Resource res = null;
			try {
				ResourceSet rs = imp.eResource().getResourceSet();
				URI uri = URI.createURI(imp.getLocation());
				res = rs.getResource(uri, true);
			}
			catch (Exception e){
			}
			
			String type = imp.getImportType();
			if ("http://schemas.xmlsoap.org/wsdl/".equals(type)) {
				
			}
			else if ("http://www.w3.org/2001/XMLSchema".equals(type)) {
				
			}
			else if ("http://www.java.com/javaTypes".equals(type)) {
				
			}
			defs.getImports().remove(imp);
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
		
				if (imp!=null) {
					TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(resource);
					if (domain != null) {
						final Import i = imp;
						domain.getCommandStack().execute(new RecordingCommand(domain) {
							@Override
							protected void doExecute() {
								definitions.getImports().add(i);
								NamespaceUtil.addNamespace(i.eResource(), i.getNamespace());
	
								if (importObject instanceof org.eclipse.wst.wsdl.Definition) {
									// WSDL Definition
									Definition wsdlDefinition = (Definition)importObject;
						
									for (Binding b :(List<Binding>)wsdlDefinition.getEBindings()) {
										createInterface(definitions, i,  b.getEPortType());
									}
								}
								else if (importObject instanceof XSDSchema){
									XSDSchema schema = (XSDSchema)importObject;
	
									for (XSDElementDeclaration elem : schema.getElementDeclarations()) {
										createItemDefinition(definitions, i, elem, ItemKind.INFORMATION);
									}
								}
								else if (importObject instanceof Class) {
									Class clazz = (Class)importObject;
	
									createItemDefinition(definitions, i, clazz.getName(), ItemKind.PHYSICAL);
								}
								else if (importObject instanceof org.eclipse.bpmn2.Definitions) {
									// what to do here?
								}
							}
						});
					}
				}
			}
		}
		return imp;
	}

	private static EndPoint createEndPoint(Definitions definitions, Binding binding) {
		EndPoint endPoint = Bpmn2Factory.eINSTANCE.createEndPoint();
		definitions.getRootElements().add(endPoint);
		ModelUtil.setID(endPoint);
		
		return endPoint;
	}

	private static Interface createInterface(Definitions definitions, Import imp, PortType portType) {
		Interface intf = Bpmn2Factory.eINSTANCE.createInterface();
		intf.setName(portType.getQName().getLocalPart());
		intf.setImplementationRef(portType);
		definitions.getRootElements().add(intf);
		ModelUtil.setID(intf);
		
		createOperations(definitions, imp, intf, portType);
		
		return intf;
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
			
			intf.getOperations().add(bpmn2op);
			ModelUtil.setID(bpmn2op);
		}
	}
	
	private static org.eclipse.bpmn2.Message createMessage(Definitions definitions, Import imp, Message wsdlmsg) {
		org.eclipse.bpmn2.Message bpmn2msg = Bpmn2Factory.eINSTANCE.createMessage();
		ItemDefinition itemDef = createItemDefinition(definitions, imp, wsdlmsg);
		bpmn2msg.setItemRef(itemDef);
		bpmn2msg.setName(wsdlmsg.getQName().getLocalPart());
		definitions.getRootElements().add(bpmn2msg);
		ModelUtil.setID(bpmn2msg);
		
		return bpmn2msg;
	}
	
	private static org.eclipse.bpmn2.Error createError(Definitions definitions, Import imp, Fault fault) {
		org.eclipse.bpmn2.Error error = Bpmn2Factory.eINSTANCE.createError();
		ItemDefinition itemDef = createItemDefinition(definitions, imp, fault, ItemKind.INFORMATION);
		error.setName(fault.getName());
		error.setStructureRef(itemDef);
		definitions.getRootElements().add(error);
		ModelUtil.setID(error);
		error.setErrorCode(error.getId());
		
		return error;
	}
	
	private static ItemDefinition createItemDefinition(Definitions definitions, Import imp, Message wsdlmsg) {
		return createItemDefinition(definitions, imp, wsdlmsg, ItemKind.INFORMATION);
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
		definitions.getRootElements().add(itemDef);
		ModelUtil.setID(itemDef);
		
		return itemDef;
	}
}
