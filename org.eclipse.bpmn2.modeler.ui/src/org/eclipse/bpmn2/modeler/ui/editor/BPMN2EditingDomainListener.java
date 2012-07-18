/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.editor;

import org.eclipse.bpmn2.modeler.core.utils.ErrorUtils;
import org.eclipse.bpmn2.modeler.core.validation.ValidationDiagnostic;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.ExceptionHandler;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.emf.transaction.TransactionalEditingDomainEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomainListenerImpl;
import org.eclipse.emf.validation.model.ConstraintStatus;

public class BPMN2EditingDomainListener extends TransactionalEditingDomainListenerImpl implements ExceptionHandler {
	
	protected BPMN2Editor bpmn2Editor;
	protected BasicDiagnostic diagnostics;

	public BPMN2EditingDomainListener(BPMN2Editor bpmn2Editor) {
		super();
		this.bpmn2Editor = bpmn2Editor;
		TransactionalCommandStack stack = (TransactionalCommandStack) bpmn2Editor.getEditingDomain().getCommandStack();
		stack.setExceptionHandler(this);
	}

	@Override
	public void transactionStarting(TransactionalEditingDomainEvent event) {
		diagnostics = null;
	}
	
	/**
	 * this will be called in case of rollback
	 */
	@Override
	public void transactionClosed(TransactionalEditingDomainEvent event) {
		super.transactionClosed(event);
		IStatus status = event.getTransaction().getStatus();
		if (status != null && event.getTransaction().getStatus() instanceof ConstraintStatus) {
			final ConstraintStatus constraintStatus = (ConstraintStatus) status;
			
			final Resource resource = constraintStatus.getTarget().eResource();
			if (resource!=null) {
				final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new 
						Path(resource.getURI().toPlatformString(true)));
				if (constraintStatus.getSeverity() == IStatus.CANCEL) {
					ErrorUtils.showErrorMessage(constraintStatus.getMessage());
				}else if (constraintStatus.getSeverity() == IStatus.WARNING) {
					resource.getWarnings().add(new ValidationDiagnostic(status, resource));
				}
			}
		}
		else if(status!= null) {
			// TODO not working yet, if not canceled, show the warnings in the problem view
			// maybe this should be done via a ValidationAdapter, see http://help.eclipse.org/galileo/index.jsp?topic=/org.eclipse.emf.validation.doc/tutorials/validationAdapterTutorial.html
			//			Resource bpmnResource = bpmn2Editor.getResourceSet().getResources().get(1);
//			if (bpmnResource.isLoaded()) {
//				IBatchValidator validator = ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
//				validator.setIncludeLiveConstraints(true);
//				Resource validationResource = bpmn2Editor.getResourceSet().getResources().get(1);
//				DocumentRoot content = (DocumentRoot) validationResource.getContents().get(0);
//				IStatus validationResult = validator.validate(content.getDefinitions());
//				int sev = validationResult.getSeverity();
//				IStatus[] results = new IStatus[] {validationResult};
//				if (validationResult.isMultiStatus()) {
//					results = validationResult.getChildren();
//				}
//				
//				for (IStatus s : results) {
//					if (sev == IStatus.CANCEL) {
//						ErrorUtils.showErrorMessage(s.getMessage());
//					}
//					
//					if (sev == IStatus.WARNING) {
//						validationResource.getWarnings().add(new ValidationDiagnostic(s, validationResource));
//					}
//				}
//			}
		}
	}
	
	@Override
	public void handleException(Exception e) {
		String source = null;
		int code = 0;
		String message = e.getMessage();
		Object[] data = null;
		StackTraceElement trace[] = e.getStackTrace();
		if (trace!=null && trace.length>0) {
			source = trace[0].getMethodName();
		}
		if (diagnostics==null) {
			diagnostics = new BasicDiagnostic(source,code,message,data);
		}
		else
			diagnostics.add(new BasicDiagnostic(source,code,message,data));
	}

	public BasicDiagnostic getDiagnostics() {
		return diagnostics;
	}
	
	public IMarker createMarker(IResource resource, int severity, String msg) {
		try {
			IMarker m = resource.createMarker(IMarker.PROBLEM);
			m.setAttribute(IMarker.MESSAGE, msg);
			m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
			m.setAttribute(IMarker.SEVERITY, severity);
			return m;
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}
	
}
