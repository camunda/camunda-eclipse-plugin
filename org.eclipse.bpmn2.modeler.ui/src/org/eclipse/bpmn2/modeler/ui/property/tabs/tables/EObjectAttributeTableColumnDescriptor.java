package org.eclipse.bpmn2.modeler.ui.property.tabs.tables;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

/**
 * A column descriptor which maps to an eAttribute of a eObject.
 * 
 * @author nico.rehwaldt
 *
 * @param <T>
 */
public class EObjectAttributeTableColumnDescriptor<T extends EObject> extends TableColumnDescriptor {

	private EStructuralFeature feature;

	public EObjectAttributeTableColumnDescriptor(EStructuralFeature feature, String title, int weight) {
		super(title, weight);

		this.feature = feature;
	}

	/**
	 * Returns the string value of an object
	 * 
	 * @return
	 */
	public ColumnLabelProvider getColumnLabelProvider() {
		ColumnLabelProvider labelProvider = new TypedColumnLabelProvider<T>() {

			@Override
			public String getText(T element) {
				Object value = element.eGet(feature);
				if (value == null) {
					return "";
				} else {
					return String.valueOf(element.eGet(feature));
				}
			}
		};

		return labelProvider;
	}
	
	@Override
	public EditingSupport getEditingSupport(TableViewer viewer) {
		return new EObjectAttributeEditingSupport<EObject>(viewer, feature);
	}
}