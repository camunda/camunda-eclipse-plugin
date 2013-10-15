/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.camunda.bpm.modeler.core.importer;

import java.util.Collections;
import java.util.List;

import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.platform.IDiagramContainer;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 */
public class ModelImportCommand extends RecordingCommand {

	protected IDiagramContainer diagramContainer;
	protected Bpmn2Resource resource;
	
	private ImportException recordedException;
	private ModelImport modelImport;

	public ModelImportCommand(TransactionalEditingDomain domain, IDiagramContainer diagramContainer, Bpmn2Resource resource) {
		super(domain);
		this.diagramContainer = diagramContainer;
		this.resource = resource;
	}

	@Override
	protected void doExecute() {
		try {
			modelImport = new ModelImport(diagramContainer.getDiagramTypeProvider(), resource);
			modelImport.execute();
		} catch (ImportException e) {
			recordedException = e;
		} catch (Exception e) {
			recordedException = new ImportException("Unhandled exception during import", e);
			modelImport.log(recordedException);
		}
	}

	public boolean wasSuccessful() {
		return recordedException == null;
	}
	
	/**
	 * Return fatal import error if any
	 * 
	 * @return
	 */
	public ImportException getRecordedException() {
		return recordedException;
	}
	

	/**
	 * Return import warnings if any
	 * 
	 * @return
	 */
	public List<ImportException> getRecordedWarnings() {
		if (modelImport != null) {
			return modelImport.getImportWarnings();
		} else {
			return Collections.emptyList();
		}
	}
}
