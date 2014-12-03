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
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.camunda.bpm.modeler.runtime.engine.model.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.core.model.Bpmn2ModelerResourceImpl;
import org.camunda.bpm.modeler.core.preferences.Bpmn2Preferences;
import org.camunda.bpm.modeler.runtime.engine.model.EntryType;
import org.camunda.bpm.modeler.runtime.engine.model.MapType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.ParameterType;
import org.camunda.bpm.modeler.runtime.engine.model.ScriptType;
import org.camunda.bpm.modeler.runtime.engine.model.fox.FailedJobRetryTimeCycleType;
import org.camunda.bpm.modeler.runtime.engine.model.fox.FoxPackage;
import org.camunda.bpm.modeler.runtime.engine.util.DiagramExport;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.Documentation;
import org.eclipse.bpmn2.Error;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.dc.Point;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLSave;
import org.eclipse.emf.ecore.xmi.impl.XMLLoadImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLString;
import org.eclipse.emf.ecore.xml.type.ProcessingInstruction;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.w3c.dom.Node;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <!-- begin-user-doc --> The <b>Resource </b> associated with the package.
 * <!-- end-user-doc -->
 * 
 * @see org.camunda.bpm.modeler.runtime.jboss.jbpm5.model.util.ModelResourceFactoryImpl
 * @generated NOT
 */
public class ModelResourceImpl extends Bpmn2ModelerResourceImpl {

	/**
	 * Creates an instance of the resource. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param uri
	 *          the URI of the new resource.
	 * @generated NOT
	 */
	public ModelResourceImpl(URI uri) {
		super(uri);
	}

	/**
	 * Override this method to hook in our own XmlHandler
	 */
	@Override
	protected XMLLoad createXMLLoad() {

		return new XMLLoadImpl(createXMLHelper()) {
			@Override
			protected DefaultHandler makeDefaultHandler() {
				return new ModelXmlHandler(resource, helper, options);
			}

		};
	}

	@Override
	protected XMLSave createXMLSave() {
		try {
			if (isGenerateDiagramImage()) {
				new DiagramExport(null).execute(null);
			}
		} catch (Exception e) {
			Activator.logStatus(new Status(IStatus.WARNING, Activator.PLUGIN_ID,
					"Failed to generate diagram", e));
		}

		return new Bpmn2ModelerXMLSave(createXMLHelper()) {
			@Override
			protected void saveTypeAttribute(EClass eClass) {
				if (!eClass.getEPackage().getNsPrefix().equals("activiti")
						&& !eClass.getEPackage().getNsPrefix().equals("camunda") &&

						// prevent that 'xsi:type="xsd:anyType"' will be added to
						// an element which type is "AnyTypeImpl"
						!eClass.getEPackage().getNsPrefix().equals("ecore.xml.type")) {

					super.saveTypeAttribute(eClass);
				}
			}

			@Override
			protected boolean saveElementFeatureMap(EObject o, EStructuralFeature f) {
				
				// NOTE: the implementation of this method is copied from
				// XmlSaveImpl#saveElementFeatureMap()!
				// We need to copy it because there is no way to hook into the case
				// "entryFeature == XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT"
				// (see if-clause below). 
				
				@SuppressWarnings("unchecked")
				List<? extends FeatureMap.Entry> values = (List<? extends FeatureMap.Entry>) helper.getValue(o, f);
				int size = values.size();
				for (int i = 0; i < size; i++) {
					FeatureMap.Entry entry = values.get(i);
					EStructuralFeature entryFeature = entry.getEStructuralFeature();
					Object value = entry.getValue();

					if (entryFeature == XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__PROCESSING_INSTRUCTION) {
						ProcessingInstruction pi = (ProcessingInstruction) value;
						String target = pi.getTarget();
						String data = pi.getData();
						if (escape != null && data != null) {
							data = escape.convertLines(data);
						}
						if (!toDOM) {
							doc.addProcessingInstruction(target, data);
						} else {
							// TODO processing instructions are not sent to recordValues
							currentNode.appendChild(document.createProcessingInstruction(target, data));
						}
					} else if (entryFeature instanceof EReference) {
						if (value == null) {
							saveNil(o, entryFeature);
						} else {
							EReference referenceEntryFeature = (EReference) entryFeature;
							if (referenceEntryFeature.isContainment()) {
								saveElement((InternalEObject) value, entryFeature);
							} else if (referenceEntryFeature.isResolveProxies()) {
								saveFeatureMapElementReference((EObject) value, referenceEntryFeature);
							} else {
								saveElementIDRef(o, (EObject) value, entryFeature);
							}
						}
					} else {
						if (entryFeature == XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT) {
							String svalue = value.toString();
							if (escape != null) {
								
								// DO NOT REMOVE THIS!
								
								// the following lines prevents that a line-break will not be escaped
								// in case of a ParameterType, EntryType:
								//
								// <camunda:inputParameter name="xyz">
								//   <camunda:script ... />
								// </camunda:inputParameter>
								//
								// will not be saved as following:
								//
								// <camunda:inputParameter name="xyz"><![CDATA[
								//   ]]><camunda:script ... /><![CDATA[
								// ]]></camunda:inputParameter>
								
								if (o instanceof ParameterType
										&& f.getName().equals(ModelPackage.eINSTANCE.getParameterType_Mixed().getName())) {
									
									if (!o.eIsSet(ModelPackage.eINSTANCE.getParameterType_Script())
											&& !o.eIsSet(ModelPackage.eINSTANCE.getParameterType_Map())
											&& !o.eIsSet(ModelPackage.eINSTANCE.getParameterType_List())) {
										
										svalue = escape.convertText(svalue);
									}
									
								} else if (o instanceof EntryType
										&& f.getName().equals(ModelPackage.eINSTANCE.getEntryType_Mixed().getName())) {
									
									if (!o.eIsSet(ModelPackage.eINSTANCE.getEntryType_Script())
											&& !o.eIsSet(ModelPackage.eINSTANCE.getEntryType_Map())
											&& !o.eIsSet(ModelPackage.eINSTANCE.getEntryType_List())) {
											
										svalue = escape.convertText(svalue);
									}								
								}
								else {
									svalue = escape.convertText(svalue);
								}
								
							}
							if (!toDOM) {
								doc.addText(svalue);
							} else {
								Node text = document.createTextNode(svalue);
								currentNode.appendChild(text);
								handler.recordValues(text, o, f, entry);
							}
						} else if (entryFeature == XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__CDATA) {
							String stringValue = value.toString();
							if (escape != null) {
								stringValue = escape.convertLines(stringValue);
							}
							if (!toDOM) {
								doc.addCDATA(stringValue);
							} else {
								Node cdata = document.createCDATASection(stringValue);
								currentNode.appendChild(cdata);
								handler.recordValues(cdata, o, f, entry);
							}
						} else if (entryFeature == XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__COMMENT) {
							String stringValue = value.toString();
							if (escape != null) {
								stringValue = escape.convertLines(stringValue);
							}
							if (!toDOM) {
								doc.addComment(stringValue);
							} else {
								// TODO comments are not sent to recordValues
								currentNode.appendChild(document.createComment(stringValue));
							}
						} else {
							saveElement(o, value, entryFeature);
						}
					}
				}
				return size > 0;
			}
			
			@Override
			protected boolean shouldSaveFeature(EObject o, EStructuralFeature f) {
				if (f == ModelPackage.eINSTANCE.getExecutionListenerType_Event()
						|| f == ModelPackage.eINSTANCE.getTaskListenerType_Event()) {

					return true;
				}
				if (o instanceof Point) {
					return true;
				}

				if (o instanceof Bounds) {
					return true;
				}

				if (o instanceof FormalExpression
						&& f.getName().equals(
								Bpmn2Package.eINSTANCE.getFormalExpression_Body().getName())) {
					return false;
				}
				if (o instanceof Documentation
						&& f.getName().equals(
								Bpmn2Package.eINSTANCE.getDocumentation_Text().getName())) {
					return false;
				}
				if (o instanceof FailedJobRetryTimeCycleType
						&& f.getName().equals(
								FoxPackage.eINSTANCE.getFailedJobRetryTimeCycleType_Text()
										.getName())) {
					return false;
				}
				
				return super.shouldSaveFeature(o, f);
			}

			@Override
			protected void init(XMLResource resource, Map<?, ?> options) {
				super.init(resource, options);
		        doc = new XMLString(Integer.MAX_VALUE, publicId, systemId, null) {
		        	@Override
		        	public void addAttribute(String name, String value) {
		        		if (XSI_SCHEMA_LOCATION.equals(name)) {
		        			value = "http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd";
		        		}
		        		super.addAttribute(name, value);
		        	}
		        };
			}
		};
	}

	private boolean isGenerateDiagramImage() {
		return Bpmn2Preferences.getInstance().getBoolean(
				Bpmn2Preferences.PREF_TOGGLE_DIAGRAM_GENERATION, true);
	}

	/**
	 * We need extend the standard SAXXMLHandler to hook into the handling of
	 * attribute references which may be either simple ID Strings or QNames. We'll
	 * search through all of the objects' IDs first to find the one we're looking
	 * for. If not, we'll try a QName search.
	 */
	protected static class ModelXmlHandler extends Bpmn2ModelerXmlHandler {
		Logger log = Logger.getLogger(getClass().getSimpleName());

		public ModelXmlHandler(XMLResource xmiResource, XMLHelper helper,
				Map<?, ?> options) {
			super(xmiResource, helper, options);
		}

		@Override
		protected void handleForwardReferences(boolean isEndDocument) {
			try {
				if (isEndDocument) {

					for (Iterator<InternalEObject> i = sameDocumentProxies.iterator(); i
							.hasNext();) {
						InternalEObject proxy = i.next();
						if (!documentRoot.eIsProxy()) {
							Definitions def = (Definitions) documentRoot.eContents().get(0);

							EObject resolved = EcoreUtil.resolve(proxy, def.eResource());

							if (!resolved.eIsProxy()) {
								continue;
							}
						}

						if (proxy instanceof Error) {

							final Error newError = Bpmn2Factory.eINSTANCE.createError();
							String id = proxy.eProxyURI().fragment();
							newError.setId(id);
							newError.setName(id);
							newError.setErrorCode(id);

							TransactionalEditingDomain domain = TransactionUtil
									.getEditingDomain(xmlResource);

							final DocumentRoot root = (DocumentRoot) xmlResource
									.getContents().get(0);

							domain.getCommandStack().execute(new RecordingCommand(domain) {
								@Override
								protected void doExecute() {
									root.getDefinitions().getRootElements().add(0, newError);
								}
							});

						}
						i.remove();
						EcoreUtil.delete(proxy, true);
					}
				}
			} catch (Exception e) {
				log.fine(e.getMessage());
			}

			super.handleForwardReferences(isEndDocument);
		}

		@Override
		protected void handleProxy(InternalEObject proxy, String uriLiteral) {
			super.handleProxy(proxy, uriLiteral);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void processElement(String name, String prefix, String localName) {
			super.processElement(name, prefix, localName);
		}

		@Override
		protected EStructuralFeature getFeature(EObject object, String prefix,
				String name, boolean isElement) {
			return super.getFeature(object, prefix, name, isElement);
		}

		@Override
		protected String getXSIType() {
			if (isNamespaceAware)
				return attribs.getValue(ExtendedMetaData.XSI_URI, XMLResource.TYPE);
			String element = elements.peek();
			// If an parameter specifies multiple xsi:type data types, the last one
			// wins.
			// NOTE: we must check for "type" in any namespace with the URI
			// "http://www.w3.org/2001/XMLSchema-instance"
			String value = null;
			int length = attribs.getLength();
			for (int i = 0; i < length; ++i) {
				attribs.getQName(i);
				String localpart = attribs.getLocalName(i);
				String prefix = null;
				int ci = localpart.lastIndexOf(':');
				if (ci > 0) {
					prefix = localpart.substring(0, ci);
					localpart = localpart.substring(ci + 1);
				}
				if (TYPE.equals(localpart)) {
					String uri = helper.getNamespaceURI(prefix);
					if (XSI_URI.equals(uri)) {
						value = attribs.getValue(i);
					}
				}
			}

			if (element.contains("timeDuration") || element.contains("timeDate")
					|| element.contains("timeCycle")) {
				String prefix = "";
				if (element.indexOf(":") != -1) {
					prefix = element.split(":")[0] + ":";
				}
				return prefix + "tFormalExpression";
			}

			return value;
		}
	}
} // ModelResourceImpl
