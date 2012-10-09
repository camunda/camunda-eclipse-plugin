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

import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ImportException;
import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;
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
	
	protected Bpmn2ModelImport bpmn2ModelImport;
	protected IFeatureProvider featureProvider;

	public AbstractDiagramElementHandler(Bpmn2ModelImport bpmn2ModelImport) {
		this.bpmn2ModelImport = bpmn2ModelImport;
		
		featureProvider = bpmn2ModelImport.getFeatureProvider();
	}
	
	public abstract PictogramElement handleDiagramElement(T bpmnElement, DiagramElement diagramElement, ContainerShape container);

	/**
	 * Return a pictogram element for the given flow node.
	 * 
	 * @param node
	 * @throws Bpmn2ImportException if corresponding pictogram element is not yet present
	 * @return
	 */
	protected PictogramElement getPictogramElement(EObject node) {
		if (node instanceof BaseElement) {
			return bpmn2ModelImport.getPictogramElement((BaseElement) node);
		} else {
			// Must be a unresolvable proxy
			throw new Bpmn2ImportException("Failed to resolve node " + node);
		}
	}

	/**
	 * 
	 * @param bpmnElement
	 * 
	 * @throws Bpmn2ImportException if corresponding diagram element is not yet present
	 * @return
	 */
	protected DiagramElement getDiagramElement(BaseElement bpmnElement) {
		return bpmn2ModelImport.getDiagramElement(bpmnElement);
	}
	
	protected void createLink(T bpmnElement, DiagramElement shape, PictogramElement newContainer) {
		featureProvider.link(newContainer, new Object[] { bpmnElement, shape });
	}
}
