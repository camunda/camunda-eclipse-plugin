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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;

/**
 * @deprecated This class is not needed, EMF will validate during commit
 * @author drobisch
 *
 */
public class LiveValidationContentAdapter extends EContentAdapter {

	public LiveValidationContentAdapter() {
	}
	
	public void notifyChanged(final Notification notification) {
	}
	
}
