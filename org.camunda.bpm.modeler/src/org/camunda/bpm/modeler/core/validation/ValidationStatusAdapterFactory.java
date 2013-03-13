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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

/**
 * ValidationStatusAdapterFactory
 * 
 * <p/>
 * Adds a validation errorList holder to an EObject.
 */
public class ValidationStatusAdapterFactory extends AdapterFactoryImpl {

    @Override
    public boolean isFactoryForType(Object type) {
        return type instanceof Class && ValidationStatusAdapter.class.isAssignableFrom((Class<?>) type);
    }

    @Override
    protected Adapter createAdapter(Notifier target) {
        return new ValidationStatusAdapter();
    }

}
