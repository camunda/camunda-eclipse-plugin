/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.camunda.bpm.modeler.core.importer.handlers;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.DataObjectReference;

/**
 * 
 * @author Nico Rehwaldt
 */
public class DataObjectReferenceShapeHandler extends AbstractShapeHandler<DataObjectReference> {

	public DataObjectReferenceShapeHandler(ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}
}
