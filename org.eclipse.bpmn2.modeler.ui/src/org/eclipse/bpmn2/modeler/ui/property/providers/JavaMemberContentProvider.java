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

package org.eclipse.bpmn2.modeler.ui.property.providers;

import java.lang.reflect.Member;
import java.util.List;

/**
 * @author Bob Brodt
 *
 */
public class JavaMemberContentProvider extends AbstractContentProvider {
	
	@Override
	public void collectElements(Object input, List list)  {
		if (input instanceof Member) {
			list.add(input);
			return;
		}			
	}

}
