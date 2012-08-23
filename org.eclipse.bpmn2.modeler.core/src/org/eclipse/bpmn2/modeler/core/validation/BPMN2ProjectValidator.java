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
import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.validation.marker.IMarkerConfigurator;
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

public class BPMN2ProjectValidator extends AbstractValidator {

    /** ID for BPMN2 specific problem markers. */
    public static final String BPMN2_MARKER_ID = "org.eclipse.bpmn2.modeler.core.problemMarker";

    @Override
    public ValidationResult validate(ValidationEvent event, ValidationState state, IProgressMonitor monitor) {
        if ((event.getKind() & IResourceDelta.REMOVED) != 0 || event.getResource().isDerived(IResource.CHECK_ANCESTORS)) {
            return new ValidationResult();
        }

        ResourceSet rs = new ResourceSetImpl();
        Resource resource = rs.createResource(
                URI.createPlatformResourceURI(event.getResource().getFullPath().toString(), false),
                "org.eclipse.bpmn2.content-type.xml");
        try {
            resource.load(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ValidationResult result = new ValidationResult();
        if (resource.getContents().isEmpty()) {
            ValidatorMessage message = ValidatorMessage.create("Invalid bpmn2 file", event.getResource());
            message.setType(BPMN2_MARKER_ID);
            result.add(message);
        } else {
            IBatchValidator validator = ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
            processStatus(validator.validate(resource.getContents(), monitor), event.getResource(), result);
        }
        return result;
    }
    
	public static boolean validateOnSave(Resource resource, IProgressMonitor monitor) {

		boolean needValidation = false;
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceDescription description = workspace.getDescription();
		if (!description.isAutoBuilding()) {
			needValidation = true;
		}
		if (!needValidation) {
			resource.getURI().toFileString();
			String pathString = resource.getURI().toPlatformString(true);
			IPath path = Path.fromOSString(pathString);
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			IProject project = file.getProject();
			if (project!=null) {
				try {
					IBuildConfiguration config = project.getActiveBuildConfig();
					if (config==null || config.getName()==null || config.getName().isEmpty())
						needValidation = true;
					IBuildConfiguration[] configs = project.getBuildConfigs();
					for (IBuildConfiguration c : configs) {
						System.out.println(c.getName());
					}
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		if (needValidation) {
	        IBatchValidator validator = ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
	        IStatus status = validator.validate(resource.getContents(), monitor);
	    	try {
				MarkerUtil.createMarkers(status, BPMN2_MARKER_ID, new MarkerConfigurator());
				return true;
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
    	return false;
    }
    
    public static void processStatus(IStatus status, IResource resource, ValidationResult result) {
        if (status.isMultiStatus()) {
            for (IStatus child : status.getChildren()) {
                processStatus(child, resource, result);
            }
        } else if (!status.isOK()) {
            result.add(createValidationMessage(status, resource));
        }
    }

    public static ValidatorMessage createValidationMessage(IStatus status, IResource resource) {
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

    public static class MarkerConfigurator implements IMarkerConfigurator {

		@Override
		public void appendMarkerConfiguration(IMarker marker, IConstraintStatus status) throws CoreException {
            marker.setAttribute(EValidator.URI_ATTRIBUTE, EcoreUtil.getURI(status.getTarget()).toString());
            marker.setAttribute(MarkerUtil.RULE_ATTRIBUTE, status.getConstraint().getDescriptor().getId());
            if (status.getResultLocus().size() > 0) {
                StringBuffer relatedUris = new StringBuffer();
                for (EObject eobject : status.getResultLocus()) {
                    relatedUris.append(EcoreUtil.getURI(eobject).toString()).append(" ");
                }
                relatedUris.deleteCharAt(relatedUris.length() - 1);
                marker.setAttribute(EValidator.RELATED_URIS_ATTRIBUTE, relatedUris.toString());
            }
		}
    	
    }
}
