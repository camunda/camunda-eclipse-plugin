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

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * ValidateModelFeature
 * 
 * <p/>
 * Custom feature used for validating the current state of the model.
 */
public class ValidateModelFeature extends AbstractCustomFeature {

    /**
     * Create a new ValidateModelFeature.
     * 
     * @param fp the feature provider.
     */
    public ValidateModelFeature(IFeatureProvider fp) {
        super(fp);
    }

    @Override
    public String getDescription() {
        return "Validate the configuration.";
    }

    @Override
    public String getName() {
        return "Validate";
    }

    @Override
    public boolean canExecute(ICustomContext context) {
        return true;
    }

    @Override
    public void execute(ICustomContext context) {
        Set<EObject> selectedBOs = new LinkedHashSet<EObject>();
        List<? extends PictogramElement> elements = Collections.singletonList(getDiagram());
        // if (context.getPictogramElements() != null &&
        // context.getPictogramElements().length > 0) {
        // elements = Arrays.asList(context.getPictogramElements());
        // }

        for (PictogramElement element : elements) {
            Object bo = getBusinessObjectForPictogramElement(element);
            if (bo instanceof EObject) {
                selectedBOs.add((EObject) bo);
            }
        }

        if (selectedBOs.isEmpty()) {
            return;
        }

        IBatchValidator validator = ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
        validator.setOption(IBatchValidator.OPTION_REPORT_SUCCESSES, true);

        Set<EObject> seenTargets = new LinkedHashSet<EObject>();
        updateValidationStatus(validator.validate(selectedBOs), seenTargets);

        for (EObject target : seenTargets) {
            PictogramElement pe = getFeatureProvider().getPictogramElementForBusinessObject(target);
            if (pe != null) {
                getDiagramEditor().refreshRenderingDecorators(pe);
            }
        }
    }

    @Override
    public boolean hasDoneChanges() {
        // we never do any work
        return false;
    }

    private void updateValidationStatus(IStatus status, Set<EObject> seenTargets) {
        if (status.isMultiStatus()) {
            for (IStatus child : status.getChildren()) {
                updateValidationStatus(child, seenTargets);
            }
        } else if (status instanceof IConstraintStatus) {
            EObject target = ((IConstraintStatus) status).getTarget();
            ValidationStatusAdapter statusAdapter = (ValidationStatusAdapter) EcoreUtil.getRegisteredAdapter(target,
                    ValidationStatusAdapter.class);
            if (statusAdapter == null) {
                return;
            }
            if (seenTargets.add(target)) {
                statusAdapter.clearValidationStatus();
            }
            statusAdapter.addValidationStatus(status);
        }
    }
}
