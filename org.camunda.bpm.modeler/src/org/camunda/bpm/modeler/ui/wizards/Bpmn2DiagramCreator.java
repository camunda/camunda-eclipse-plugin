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
 * @author Innar Made
 ******************************************************************************/
package org.camunda.bpm.modeler.ui.wizards;

import org.camunda.bpm.modeler.core.files.FileService;
import org.camunda.bpm.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.diagram.editor.Bpmn2DiagramEditorInput;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.ui.services.GraphitiUi;

public class Bpmn2DiagramCreator {
	
	public static Bpmn2Resource createBpmn2Resource(URI uri) {
		Definitions definitions = createDefinitions(uri);
		return (Bpmn2Resource) definitions.eResource();
	}
	
	public static Definitions createDefinitions(URI uri) {
		// set targetNamespace because it is a mandatory field
		// the process engine can not deploy the process model without targetNamespace 
		Definitions definitions = new Bpmn2ResourceFactoryImpl().createAndInitResource(uri);
		definitions.setTargetNamespace(ModelPackage.eNS_URI);
		return definitions;
	}

	public static Bpmn2DiagramEditorInput createDiagramInput(URI modelUri, Bpmn2DiagramType diagramType, TransactionalEditingDomain transactionalEditingDomain) {

		if (modelUri == null) {
			throw new IllegalArgumentException("The modelUri must not be null. This may indicate an internal error. Please try to clean the workspace.");
		}

		String modelName = getModelName(modelUri);
		String diagramName = FileService.createTempName(modelName);

		URI diagramUri = URI.createFileURI(diagramName);

		String providerId = GraphitiUi.getExtensionManager().getDiagramTypeProviderId("BPMN2");
		
		return new Bpmn2DiagramEditorInput(modelUri, diagramUri, providerId);
	}

	public static String getModelName(URI uri) {
		return URI.decode(uri.trimFragment().trimFileExtension().lastSegment());
	}
}
