package org.camunda.bpm.modeler.core.validation;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.modeler.core.Activator;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.ui.util.EditUIMarkerHelper;
import org.eclipse.emf.validation.marker.MarkerUtil;
import org.eclipse.emf.validation.model.ConstraintStatus;
import org.eclipse.emf.validation.model.IModelConstraint;
import org.eclipse.emf.validation.service.ConstraintFactory;
import org.eclipse.emf.validation.service.ConstraintRegistry;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.graphiti.ui.editor.DiagramEditor;

public class BPMN2ValidationStatusLoader {
	DiagramEditor editor;
	
	public BPMN2ValidationStatusLoader(DiagramEditor editor) {
		this.editor = editor;
	}

    public Set<EObject> load(List<IMarker> markers) {
        if (markers == null) {
            return Collections.emptySet();
        }
        Set<EObject> touched = new LinkedHashSet<EObject>();
        for (IMarker marker : markers) {
            final EObject markedObject = getTargetObject(marker);
            if (markedObject == null) {
                continue;
            }
            ValidationStatusAdapter statusAdapter = (ValidationStatusAdapter) EcoreUtil.getRegisteredAdapter(
                    markedObject, ValidationStatusAdapter.class);
    		
            // add the adapter factory for tracking validation errors
            if (statusAdapter==null) {
	            ResourceSet resourceSet = editor.getEditingDomain().getResourceSet();
	            resourceSet.getAdapterFactories().add(new ValidationStatusAdapterFactory());
	            statusAdapter = (ValidationStatusAdapter) EcoreUtil.getRegisteredAdapter(
	                    markedObject, ValidationStatusAdapter.class);
            }

            // convert the problem marker to an IStatus suitable for the validation status adapter
            IStatus status = convertMarker(marker, markedObject);

            // also add an adapter to each affected EObject in the result locus
            // TODO: do we need this? it causes duplicate messages to be created for each marker
            // better to have the constraint handler create additional error messages as needed.
//            if (status instanceof ConstraintStatus) {
//            	ConstraintStatus cs = (ConstraintStatus) status;
//            	for (EObject result : cs.getResultLocus()) {
//            		// CAUTION: the result locus WILL contain references to object
//            		// features (EStructuralFeatures) that identify the feature in
//            		// error for the Property Sheets. We don't want to add a validation
//            		// status adapter to these EObjects.
//            		EPackage pkg = result.eClass().getEPackage();
//            		if (pkg != EcorePackage.eINSTANCE) {
//            			ValidationStatusAdapter sa = (ValidationStatusAdapter) EcoreUtil.getRegisteredAdapter(
//                            result, ValidationStatusAdapter.class);
//            			sa.addValidationStatus(status);
//            			touched.add(result);
//            		}
//            	}
//            }
            
            statusAdapter.addValidationStatus(status);
            touched.add(markedObject);
        }
        return touched;
    }

    private EObject getTargetObject(IMarker marker) {
        final String uriString = marker.getAttribute(EValidator.URI_ATTRIBUTE, null);
        final URI uri = uriString == null ? null : URI.createURI(uriString);
        if (uri == null) {
            return null;
        }
        return editor.getEditingDomain().getResourceSet().getEObject(uri, false);
    }

    @SuppressWarnings("unchecked")
    private IStatus convertMarker(IMarker marker, EObject target) {
        final String message = marker.getAttribute(IMarker.MESSAGE, "");
        final String constraintId = marker.getAttribute(MarkerUtil.RULE_ATTRIBUTE, null);
        final IConstraintDescriptor icd = constraintId == null ? null : ConstraintRegistry.getInstance().getDescriptor(
                constraintId);
        final IModelConstraint imc = icd == null ? null : ConstraintFactory.getInstance().newConstraint(icd);
        if (imc == null) {
            final int severity;
            switch (marker.getAttribute(IMarker.SEVERITY, -1)) {
            case IMarker.SEVERITY_INFO:
                severity = IStatus.INFO;
                break;
            case IMarker.SEVERITY_WARNING:
                severity = IStatus.WARNING;
                break;
            case IMarker.SEVERITY_ERROR:
                severity = IStatus.ERROR;
                break;
            default:
                severity = IStatus.OK;
            }
            return new Status(severity, Activator.PLUGIN_ID, message);
        }
        List<?> locus = new EditUIMarkerHelper().getTargetObjects(editor.getEditingDomain(), marker);
        for (Iterator<?> it = locus.iterator(); it.hasNext();) {
            if (!(it.next() instanceof EObject)) {
                it.remove();
            }
        }
        return new ConstraintStatus(imc, target, message, locus == null ? null : new LinkedHashSet<EObject>(
                (List<? extends EObject>) locus));
    }

}
