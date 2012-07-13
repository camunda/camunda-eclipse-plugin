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

import java.util.List;

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.validation.marker.MarkerUtil;
import org.eclipse.emf.validation.model.ConstraintStatus;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.emf.validation.model.ModelConstraint;
import org.eclipse.emf.validation.service.IValidationListener;
import org.eclipse.emf.validation.service.ValidationEvent;

/**
 * @author Bob Brodt
 *
 */
public class ProblemsReporter implements IValidationListener {
    public void validationOccurred(ValidationEvent event) {
        if (event.matches(IStatus.WARNING | IStatus.ERROR)) {
            // fabricate a multi-status for the MarkerUtil to consume
            List<IConstraintStatus> results = event.getValidationResults(); 
            MultiStatus multi = new MultiStatus(
                  Activator.getDefault().PLUGIN_ID, 1,
                  (IStatus[]) results.toArray(new IStatus[results.size()]),
                  "OCL validation errors found", null);

            try {
                // create problem markers on the appropriate resources
                MarkerUtil.createMarkers(multi);
            } catch (CoreException e) {
                // creation of problem markers failed for some reason
                Activator.getDefault().getLog().log(e.getStatus());
            }
        }
    }    
}