package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5;

import org.eclipse.graphiti.ui.platform.AbstractImageProvider;

public class JBPM5ImageProvider extends AbstractImageProvider {

	@Override
	protected void addAvailableImages() {
		// does nothing - we do it at load time
	}

	/* This method publishes needed protected method 'addImageFilePath' */
	public void addImageFilePathLazy(String imageId, String imageFilePath){
	    /** Check if its not already registered */
	    if(getImageFilePath( imageId ) == null){
	        addImageFilePath( imageId, imageFilePath );
	    }
	}	
}
