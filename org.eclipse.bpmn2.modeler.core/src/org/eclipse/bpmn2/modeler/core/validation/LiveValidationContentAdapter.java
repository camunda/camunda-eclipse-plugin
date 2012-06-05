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
package org.eclipse.bpmn2.modeler.core.validation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.validation.model.ConstraintStatus;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.ILiveValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class LiveValidationContentAdapter extends EContentAdapter {
	private ILiveValidator validator = null;

	public LiveValidationContentAdapter() {
	}

	public void notifyChanged(final Notification notification) {
		super.notifyChanged(notification);
		
		if (validator == null) {
			validator = (ILiveValidator)ModelValidationService.getInstance().newValidator(EvaluationMode.LIVE);
		}
		
		IStatus validationStatus = validator.validate(notification);
		
		if (validationStatus instanceof ConstraintStatus) {
			ConstraintStatus status = (ConstraintStatus) validationStatus;
			Resource resource = status.getTarget().eResource();
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new 
					Path(resource.getURI().toPlatformString(true)));
			if (!status.isOK()) {
				switch (status.getSeverity()) {
				case IStatus.ERROR:
					reportError(status);
					break;
				case IStatus.WARNING:
					createMarker(file, IMarker.SEVERITY_WARNING, status.getMessage());
					break;
				}
			}
		}
	}
	
	
	void createMarker(IResource resource, int severity, String msg) {
		try {
			IMarker m = resource.createMarker(IMarker.PROBLEM);
			m.setAttribute(IMarker.MESSAGE, msg);
			m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
			m.setAttribute(IMarker.SEVERITY, severity);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void reportError(IStatus status) {
		MessageDialog.openError(new Shell(Display.getCurrent()),"Error", status.getMessage());
	}
	
}
