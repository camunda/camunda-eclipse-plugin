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

import org.eclipse.bpmn2.BaseElement;

import org.eclipse.bpmn2.modeler.core.importer.ImportException;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 */
public abstract class AbstractDiagramElementHandler<T extends BaseElement> {
	
	protected ModelImport modelImport;
	protected IFeatureProvider featureProvider;

	public AbstractDiagramElementHandler(ModelImport modelImport) {
		this.modelImport = modelImport;
		
		featureProvider = modelImport.getFeatureProvider();
	}
	
	public abstract PictogramElement handleDiagramElement(T bpmnElement, DiagramElement diagramElement, ContainerShape container);

	/**
	 * Return a pictogram element for the given flow node.
	 * 
	 * @param node
	 * @throws ImportException if corresponding pictogram element is not yet present
	 * @return
	 */
	protected PictogramElement getPictogramElement(EObject node) {
		if (node instanceof BaseElement) {
			return modelImport.getPictogramElement((BaseElement) node);
		} else {
			// Must be a unresolvable proxy
			throw new ImportException("Failed to resolve node " + node);
		}
	}
	
	/**
	 * Return a pictogram element for the given flow node.
	 * 
	 * @param node
	 * @throws ImportException if corresponding pictogram element is not yet present
	 * @return
	 */
	protected PictogramElement getPictogramElementOrNull(EObject node) {
		return modelImport.getPictogramElementOrNull((BaseElement) node);
	}
	
	/**
	 * 
	 * @param bpmnElement
	 * 
	 * @throws ImportException if corresponding diagram element is not yet present
	 * @return
	 */
	protected DiagramElement getDiagramElement(BaseElement bpmnElement) {
		return modelImport.getDiagramElement(bpmnElement);
	}
	
	protected void createLink(T bpmnElement, DiagramElement shape, PictogramElement newContainer) {
		featureProvider.link(newContainer, new Object[] { bpmnElement, shape });
	}
}
