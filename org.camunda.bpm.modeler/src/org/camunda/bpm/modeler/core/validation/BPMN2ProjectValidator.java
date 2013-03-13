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
package org.camunda.bpm.modeler.core.validation;

import java.io.IOException;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.core.ProxyURIConverterImplExtension;
import org.camunda.bpm.modeler.core.builder.BPMN2Nature;
import org.camunda.bpm.modeler.core.model.Bpmn2ModelerResourceImpl;
import org.camunda.bpm.modeler.core.model.Bpmn2ModelerResourceSetImpl;
import org.camunda.bpm.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.validation.marker.MarkerUtil;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.validation.AbstractValidator;
import org.eclipse.wst.validation.ValidationEvent;
import org.eclipse.wst.validation.ValidationFramework;
import org.eclipse.wst.validation.ValidationResult;
import org.eclipse.wst.validation.ValidationState;
import org.eclipse.wst.validation.Validator;
import org.eclipse.wst.validation.ValidatorMessage;

public class BPMN2ProjectValidator extends AbstractValidator {

    /** ID for BPMN2 specific problem markers. */
    public static final String BPMN2_MARKER_ID = "org.eclipse.bpmn2.modeler.core.problemMarker";
	private Bpmn2Preferences preferences;
	private IFile modelFile;

    @Override
    public ValidationResult validate(ValidationEvent event, ValidationState state, IProgressMonitor monitor) {
    	IResource file = event.getResource();
        if ((event.getKind() & IResourceDelta.REMOVED) != 0 
        		|| file.isDerived(IResource.CHECK_ANCESTORS)
        		|| !(file instanceof IFile)) {
            return new ValidationResult();
        }
    	modelFile = (IFile) file;
    	try {
			modelFile.deleteMarkers(BPMN2_MARKER_ID, false, IProject.DEPTH_INFINITE);
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        ResourceSet rs = new Bpmn2ModelerResourceSetImpl();
		rs.setURIConverter(new ProxyURIConverterImplExtension());

		Resource resource = rs.createResource(
                URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true),
                Bpmn2ModelerResourceImpl.BPMN2_CONTENT_TYPE_ID);
        try {
            resource.load(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ValidationResult result = new ValidationResult();
        if (resource.getContents().isEmpty()) {
            ValidatorMessage message = ValidatorMessage.create("Invalid bpmn2 file", modelFile);
            message.setType(BPMN2_MARKER_ID);
            result.add(message);
        } else {
            IBatchValidator validator = ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
            processStatus(validator.validate(resource.getContents(), monitor), modelFile, result);
        }
        return result;
    }
    
    public static void validate(IResource resource, IProgressMonitor monitor) {
		if (isBPMN2File(resource)) {
			Validator[] validators = ValidationFramework.getDefault().getValidatorsFor(resource);
			for (Validator v : validators) {
				if (BPMN2ProjectValidator.class.getName().equals(v.getValidatorClassname())) {
					v.validate(resource, IResourceDelta.CHANGED, null, monitor);
					break;
				}
			}
		}
    }
    
    public static void validate(IResourceDelta delta, IProgressMonitor monitor) {
		IResource resource = delta.getResource();
		if (isBPMN2File(resource)) {
			Validator[] validators = ValidationFramework.getDefault().getValidatorsFor(resource);
			for (Validator v : validators) {
				if (BPMN2ProjectValidator.class.getName().equals(v.getValidatorClassname())) {
					v.validate(resource, delta.getKind(), null, monitor);
					break;
				}
			}
		}
    }

    public static boolean isBPMN2File(IResource resource) {
    	if (resource instanceof IFile) {
	    	try {
	    		IContentDescription cd = ((IFile)resource).getContentDescription();
	    		if (cd!=null) {
					return Bpmn2ModelerResourceImpl.BPMN2_CONTENT_TYPE_ID.equals(
							cd.getContentType().getId());
	    		}
			} catch (Exception e) {
			}
    	}
    	return false;
    }
    
	public static boolean validateOnSave(Resource resource, IProgressMonitor monitor) {

		boolean needValidation = false;
		String pathString = resource.getURI().toPlatformString(true);
		IPath path = Path.fromOSString(pathString);
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		IProject project = file.getProject();

		if (project!=null) {
			try {
				IProjectNature nature = project.getNature(BPMN2Nature.NATURE_ID);
				if (nature==null) {
					Bpmn2Preferences preferences = Bpmn2Preferences.getInstance(project);
					if (preferences.getCheckProjectNature()) {
						Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
						String title = "Configure BPMN2 Project Nature";
						String message = "The project '"+
								project.getName()+
								"' has not been configured with the BPMN2 Project Nature.\n\n"+
								"Adding the BPMN2 Project Nature will cause all BPMN2 files in this project "+
								"to be validated automatically whenever the project is built.\n\n"+
								"Do you want to add this Nature to the Project now?";
						MessageDialogWithToggle result = MessageDialogWithToggle.open(
								MessageDialog.QUESTION,
								shell,
								title,
								message,
								"Don't ask me again", // toggle message
								false, // toggle state
								null, // pref store
								null, // pref key
								SWT.NONE);
						if (result.getReturnCode() == IDialogConstants.YES_ID) {
							IProjectDescription description = project.getDescription();
							String[] natures = description.getNatureIds();
							String[] newNatures = new String[natures.length + 1];
							System.arraycopy(natures, 0, newNatures, 0, natures.length);
							newNatures[natures.length] = BPMN2Nature.NATURE_ID;
							description.setNatureIds(newNatures);
							project.setDescription(description, null);
							needValidation = true;
						}
						if (result.getToggleState()) {
							// don't ask again
							preferences.setCheckProjectNature(false);
						}
					}
				}
				else
					needValidation = true;

			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		if (needValidation) {
			validate(file, monitor);
			return true;
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
                String uris = relatedUris.toString();
                message.setAttribute(EValidator.RELATED_URIS_ATTRIBUTE, uris);
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
	
    protected Bpmn2Preferences getPreferences() {
		if (preferences==null) {
			assert(modelFile!=null);
			IProject project = modelFile.getProject();
			loadPreferences(project);
		}
		return preferences;
	}
	
    protected void loadPreferences(IProject project) {
		preferences = Bpmn2Preferences.getInstance(project);
		preferences.load();
	}
}
