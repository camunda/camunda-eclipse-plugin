package org.camunda.bpm.modeler.core.utils;

import org.camunda.bpm.modeler.ui.diagram.Bpmn2DiagramTypeProvider;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.graphiti.ui.services.IImageService;
import org.eclipse.swt.graphics.Image;

/**
 * Utility for image retrival
 * 
 * @author nico.rehwaldt
 */
public class Images {

	/**
	 * Returns an image by id
	 * 
	 * @param imageId
	 * 
	 * @return
	 */
	public static Image getById(String imageId) {
		return getById(Bpmn2DiagramTypeProvider.ID, imageId);
	}
	
	/**
	 * Return an image by id in the scope of the given editor
	 * 
	 * @param editorId
	 * @param imageId
	 * 
	 * @return
	 */
	public static Image getById(String editorId, String imageId) {
		IImageService imageService = GraphitiUi.getImageService();
		return imageService.getImageForId(editorId, imageId);
		
	}
}
