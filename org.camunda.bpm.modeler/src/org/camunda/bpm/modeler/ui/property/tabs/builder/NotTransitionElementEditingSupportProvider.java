package org.camunda.bpm.modeler.ui.property.tabs.builder;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.ui.property.tabs.tables.EObjectAttributeEditingSupport;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

public class NotTransitionElementEditingSupportProvider extends DefaultEditingSupportProvider {

	@Override
	public EditingSupport getEditingSupport(final TableViewer viewer, EStructuralFeature feature) {
		final EClassifier eType = feature.getEType();

		String uri = feature.eResource().getURI().toString();
		
		final EFactory factory = EPackage.Registry.INSTANCE.getEFactory(uri);
		
		if (eType instanceof EEnum) {
			
			final EEnum enumeration = (EEnum) eType;
			
			final List<EEnumLiteral> literals = new ArrayList<EEnumLiteral>();
			
			for (EEnumLiteral eEnumLiteral : enumeration.getELiterals()) {
				if (!eEnumLiteral.getName().equals("take")) {
					literals.add(eEnumLiteral);
				}				
			}
			
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
		}
		
		return super.getEditingSupport(viewer, feature);
	}

}
