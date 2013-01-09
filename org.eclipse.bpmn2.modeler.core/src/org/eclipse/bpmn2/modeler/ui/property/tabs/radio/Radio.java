/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.ui.property.tabs.radio;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Button;


/**
 * 
 * @author Nico Rehwaldt
 */
public class Radio {

	public static class RadioGroup<T> {

		private Map<T, Button> memberMap = new HashMap<T, Button>();
		
		/**
		 * Returns the radio control for the given value
		 * 
		 * @param value
		 * @return
		 */
		public Button getRadioControl(T value) {
			return memberMap.get(value);
		}
		
		/**
		 * Adds a radio button to the group
		 * @param radio
		 */
		public void add(final Button radio, final T value) {
			memberMap.put(value, radio);
		}
	}
}
