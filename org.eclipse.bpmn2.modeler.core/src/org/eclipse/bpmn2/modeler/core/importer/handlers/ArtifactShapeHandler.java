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

import org.eclipse.bpmn2.Artifact;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 */
public class ArtifactShapeHandler extends AbstractShapeHandler<Artifact> {

	public ArtifactShapeHandler(ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}

}
