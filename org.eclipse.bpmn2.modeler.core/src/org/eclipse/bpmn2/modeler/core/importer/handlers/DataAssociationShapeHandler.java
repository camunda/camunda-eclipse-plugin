/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.core.importer.handlers;

import java.util.List;

import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.modeler.core.importer.InvalidContentException;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.importer.UnmappedElementException;
import org.eclipse.bpmn2.modeler.core.importer.UnsupportedFeatureException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * 
 * @author Nico Rehwaldt
 */
public abstract class DataAssociationShapeHandler<T extends DataAssociation> extends AbstractEdgeHandler<T> {

	public DataAssociationShapeHandler(ModelImport modelImport) {
		super(modelImport);
	}
	
	protected PictogramElement resolvePictogramElement(EObject elementReference) {
		PictogramElement element = null;
		
		do {
			element = getPictogramElementOrNull(elementReference);
			elementReference = elementReference.eContainer();
		} while (element == null && elementReference.eContainer() != null);
		
		return element;
	}
}
