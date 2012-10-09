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

import org.eclipse.bpmn2.DataOutput;
import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;

/**
 * 
 * @author Nico Rehwaldt
 */
public class DataOutputShapeHandler extends AbstractShapeHandler<DataOutput> {

	public DataOutputShapeHandler(Bpmn2ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}
}
