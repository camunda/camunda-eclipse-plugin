package org.camunda.bpm.modeler.ui.property.tabs.tables;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ColumnLabelProvider;

/**
 * A typed column label provider
 * 
 * @author nico.rehwaldt
 *
 * @param <T>
 */
public abstract class TypedColumnLabelProvider<T extends EObject> extends ColumnLabelProvider {

	@Override
	public final String getText(Object element) {
		return getText((T) element);
	}

	/**
	 * Returns the string of the column cell for a given object.
	 * 
	 * @param element
	 * @return
	 */
	public abstract String getText(T element);
}