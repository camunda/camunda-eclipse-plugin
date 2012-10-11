/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.core.importer;

import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.platform.IDiagramEditor;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 */
public class Bpmn2ModelImportCommand extends RecordingCommand {

	protected IDiagramEditor diagramEditor;
	protected Bpmn2Resource resource;
	
	private ImportException recordedException;

	public Bpmn2ModelImportCommand(TransactionalEditingDomain domain, IDiagramEditor diagramEditor, Bpmn2Resource resource) {
		super(domain);
		this.diagramEditor = diagramEditor;
		this.resource = resource;
	}

	@Override
	protected void doExecute() {
		try {
			Bpmn2ModelImport bpmn2ModelImport = new Bpmn2ModelImport(diagramEditor.getDiagramTypeProvider(), resource);
			bpmn2ModelImport.execute();
		} catch (ImportException e) {
			recordedException = e;
		}
	}
	
	public boolean wasSuccessful() {
		return recordedException == null;
	}
	
	public ImportException getRecordedException() {
		return recordedException;
	}
}
