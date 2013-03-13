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

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.Activator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

/**
 * ValidationStatusAdapter
 * 
 * <p/>
 * An adapter for holding validation errorList associated with an EObject.
 */
public class ValidationStatusAdapter extends AdapterImpl {

    public ValidationStatusAdapter() {
		super();
	}

	private List<IStatus> _validationStatus = new ArrayList<IStatus>();

    @Override
    public boolean isAdapterForType(Object type) {
        return type instanceof Class && getClass().isAssignableFrom((Class<?>) type);
    }

    /**
     * @return the validation errorList for the target object.
     */
    public IStatus getValidationStatus() {
        switch (_validationStatus.size()) {
        case 0:
            return Status.OK_STATUS;
        case 1:
            return _validationStatus.get(0);
        }
        return new MultiStatusWithMessage(_validationStatus.toArray(new IStatus[_validationStatus.size()]));
    }

    /**
     * Clears the errorList associated with the target.
     */
    public void clearValidationStatus() {
        _validationStatus.clear();
    }

    /**
     * @param errorList the errorList to add/associate with the target.
     */
    public void addValidationStatus(IStatus status) {
        _validationStatus.add(status);
    }

    private static class MultiStatusWithMessage extends MultiStatus {

        private String _message;

        public MultiStatusWithMessage(IStatus[] newChildren) {
            super(Activator.PLUGIN_ID, 0, newChildren, "", null);
        }

        @Override
        public String getMessage() {
            if (_message != null) {
                return _message;
            }
            if (getChildren().length == 0) {
                return super.getMessage();
            }
            StringBuffer sb = new StringBuffer();
            for (IStatus status : getChildren()) {
                if (status.isOK()) {
                    continue;
                }
                sb.append(" - ").append(status.getMessage()).append('\n');
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            _message = sb.toString();
            return _message;
        }

    }
}
