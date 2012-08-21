/*************************************************************************************
 * Copyright (c) 2012 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.eclipse.bpmn2.modeler.core.validation;

import java.io.IOException;

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.validation.marker.MarkerUtil;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.wst.validation.AbstractValidator;
import org.eclipse.wst.validation.ValidationEvent;
import org.eclipse.wst.validation.ValidationResult;
import org.eclipse.wst.validation.ValidationState;
import org.eclipse.wst.validation.ValidatorMessage;

/**
 * BPMN2ProjectValidator
 * 
 * <p/>
 * WST V2 validator supporting SwitchYard projects.
 */
public class BPMN2ProjectValidator extends AbstractValidator {

    /** ID for SwitchYard specific problem markers. */
    public static final String BPMN2_MARKER_ID = "org.eclipse.bpmn2.modeler.core.problemMarker";

    @Override
    public ValidationResult validate(ValidationEvent event, ValidationState state, IProgressMonitor monitor) {
        if ((event.getKind() & IResourceDelta.REMOVED) != 0 || event.getResource().isDerived(IResource.CHECK_ANCESTORS)) {
            return new ValidationResult();
        }

        ResourceSet rs = new ResourceSetImpl();
        Resource resource = rs.createResource(
                URI.createPlatformResourceURI(event.getResource().getFullPath().toString(), false),
                "org.switchyard.tools.ui.editor.content-type.xml");
        try {
            resource.load(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ValidationResult result = new ValidationResult();
        if (resource.getContents().isEmpty()) {
            ValidatorMessage message = ValidatorMessage.create("Invalid switchyard.xml file", event.getResource());
            message.setType(BPMN2_MARKER_ID);
            result.add(message);
        } else {
            IBatchValidator validator = ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
            processStatus(validator.validate(resource.getContents(), monitor), event.getResource(), result);
        }
        return result;
    }

    private void processStatus(IStatus status, IResource resource, ValidationResult result) {
        if (status.isMultiStatus()) {
            for (IStatus child : status.getChildren()) {
                processStatus(child, resource, result);
            }
        } else if (!status.isOK()) {
            result.add(createValidationMessage(status, resource));
        }
    }

    private ValidatorMessage createValidationMessage(IStatus status, IResource resource) {
        ValidatorMessage message = ValidatorMessage.create(status.getMessage(), resource);
        switch (status.getSeverity()) {
        case IStatus.INFO:
            message.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
            break;
        case IStatus.WARNING:
            message.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
            break;
        case IStatus.ERROR:
        case IStatus.CANCEL:
            message.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
            break;
        }

        if (status instanceof IConstraintStatus) {
            IConstraintStatus ics = (IConstraintStatus) status;
            message.setAttribute(EValidator.URI_ATTRIBUTE, EcoreUtil.getURI(ics.getTarget()).toString());
            message.setAttribute(MarkerUtil.RULE_ATTRIBUTE, ics.getConstraint().getDescriptor().getId());
            if (ics.getResultLocus().size() > 0) {
                StringBuffer relatedUris = new StringBuffer();
                for (EObject eobject : ics.getResultLocus()) {
                    relatedUris.append(EcoreUtil.getURI(eobject).toString()).append(" ");
                }
                relatedUris.deleteCharAt(relatedUris.length() - 1);
                message.setAttribute(EValidator.RELATED_URIS_ATTRIBUTE, relatedUris.toString());
            }
        }

        message.setType(BPMN2_MARKER_ID);

        return message;
    }

    @Override
    public void clean(IProject project, ValidationState state, IProgressMonitor monitor) {
        super.clean(project, state, monitor);
        try {
            project.deleteMarkers(BPMN2_MARKER_ID, false, IProject.DEPTH_INFINITE);
        } catch (CoreException e) {
            Activator.getDefault().getLog().log(e.getStatus());
        }
    }

}
