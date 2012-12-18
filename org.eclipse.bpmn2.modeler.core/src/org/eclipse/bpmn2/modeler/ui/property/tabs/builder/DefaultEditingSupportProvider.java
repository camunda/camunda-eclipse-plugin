package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EObjectAttributeEditingSupport;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EObjectAttributeTableColumnDescriptor.EditingSupportProvider;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.SimpleLabelProvider;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

public class DefaultEditingSupportProvider implements EditingSupportProvider {

	@Override
	public EditingSupport getEditingSupport(final TableViewer viewer, EStructuralFeature feature) {

		final EClassifier eType = feature.getEType();

		String uri = feature.eResource().getURI().toString();
		
		final EFactory factory = EPackage.Registry.INSTANCE.getEFactory(uri);
		
		if (eType instanceof EEnum) {
			
			final EEnum enumeration = (EEnum) eType;
			final EList<EEnumLiteral> literals = enumeration.getELiterals();
			
			return new EObjectAttributeEditingSupport<EObject>(viewer, feature) {

				@Override
				protected CellEditor getCellEditor(Object element) {
					DropDownListCellEditor<?> cellEditor = new DropDownListCellEditor<EEnumLiteral>(viewer.getTable(), literals);
					return cellEditor;
				}

				@Override
				protected Object toEValue(Object value) {
					if (value instanceof String) {
						return factory.createFromString((EEnum) eType, (String) value);
					} else {
						return value;
					}
				}
			};
		} else
		
		if (eType instanceof EDataType) {
			if (!"String".equals(eType.getName())) {
				return new EObjectAttributeEditingSupport<EObject>(viewer, feature) {
					@Override
					protected Object toEValue(Object value) {
						if (value instanceof String) {
							return factory.createFromString((EDataType) eType, (String) value);
						} else {
							return value;
						}
					}
				};
			}
		}
		
		return new EObjectAttributeEditingSupport<EObject>(viewer, feature);
	}
	
	/**
	 * A cell editor providing a drop down for a number of elements.
	 * Which returns the string representation of the selected element.
	 * 
	 * @author nico.rehwaldt
	 *
	 * @param <T>
	 */
	private static class DropDownListCellEditor<T> extends ComboBoxCellEditor {

		private ILabelProvider labelProvider = new SimpleLabelProvider();
		
		private List<T> elements;
		
		private List<String> elementStrings;
		
		public DropDownListCellEditor(final Composite parent, final List<T> elements) {
			super(parent, new String[0]);
			
			this.elements = elements;
			this.elementStrings = elementsToString(elements);
			
			setItems(elementStrings.toArray(new String[0]));
			
			setValidator(new ICellEditorValidator() {
				@Override
				public String isValid(Object value) {
					System.out.println("IS VALID (" + value + ")");
					if (!elementStrings.contains(value)) {
						return "Not in list";
					} else {
						return null;
					}
				}
			});
		}

		@Override
		protected Object doGetValue() {
			int index = (Integer) super.doGetValue();
			if (index == -1) {
				return null;
			} else {
				return elementStrings.get(index);
			}
		}
		
		@Override
		protected void doSetValue(Object value) {
			super.doSetValue(elementStrings.indexOf(value));
		}
		
		protected List<String> elementsToString(List<T> elements) {
			List<String> strings = new ArrayList<String>();
			
			for (Object o: elements) {
				strings.add(labelProvider.getText(o));
			}
			
			return strings;
		}
	}
}