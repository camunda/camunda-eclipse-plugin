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

package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.property;

import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.DataItemsPropertySection;

/**
 * @author Bob Brodt
 *
 */
public class JbpmDataItemsPropertySection extends DataItemsPropertySection {

	public JbpmDataItemsPropertySection() {
		super();
	}

	@Override
	protected AbstractBpmn2PropertiesComposite createSectionRoot() {
		return new JbpmDataItemsPropertiesComposite(this);
	}

}
