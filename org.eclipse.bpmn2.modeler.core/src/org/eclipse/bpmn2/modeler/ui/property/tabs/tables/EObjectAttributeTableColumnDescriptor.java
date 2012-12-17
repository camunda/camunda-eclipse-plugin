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

	private EditingSupportProvider editingSupportProvider;
	
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
		if (editingSupportProvider != null) {
			return editingSupportProvider.getEditingSupport(viewer, feature);
		} else {
			return new EObjectAttributeEditingSupport<EObject>(viewer, feature);
		}
	}

	/**
	 * Set a custom editing support provider for this table column descriptor
	 * 
	 * @param editingSupportProvider
	 */
	public void setEditingSupportProvider(EditingSupportProvider editingSupportProvider) {
		this.editingSupportProvider = editingSupportProvider;
	}
	
	/**
	 * Provider of custom editing support for an EObject attribute
	 * 
	 * @author nico.rehwaldt
	 */
	public static interface EditingSupportProvider {
		
		/**
		 * Provides editing support for the table
		 * 
		 * @param viewer
		 * @param feature
		 * @return
		 */
		public EditingSupport getEditingSupport(TableViewer viewer, EStructuralFeature feature);
	}
}