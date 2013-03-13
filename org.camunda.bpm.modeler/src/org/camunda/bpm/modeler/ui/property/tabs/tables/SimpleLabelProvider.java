package org.camunda.bpm.modeler.ui.property.tabs.tables;

import org.eclipse.jface.viewers.ColumnLabelProvider;

/**
 * A simple label provider which returns the string 
 * representation of the object
 * 
 * @author nico.rehwaldt
 */
public class SimpleLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if (element == null) {
			return "";
		} else {
			return String.valueOf(element);
		}
	}
}
