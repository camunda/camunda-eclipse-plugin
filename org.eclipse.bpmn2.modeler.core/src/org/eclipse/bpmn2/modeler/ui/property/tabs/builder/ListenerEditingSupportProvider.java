package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EObjectAttributeEditingSupport;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EObjectAttributeTableColumnDescriptor.EditingSupportProvider;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

public class ListenerEditingSupportProvider implements EditingSupportProvider {
	
	private Class<?> eventTypeCls;
	
	private Enumerator[] elements;
	
	public ListenerEditingSupportProvider(Class<?> eventTypeCls, Enumerator[] elements) {
		this.eventTypeCls = eventTypeCls;
		this.elements = elements;
	}
	
	@Override
	public EditingSupport getEditingSupport(final TableViewer viewer, EStructuralFeature feature) {
		
		final EClassifier eType = feature.getEType();
		
		if (eType instanceof EDataType) {
			
			if (eventTypeCls.getSimpleName().equals(eType.getName())) {
				return new EObjectAttributeEditingSupport<EObject>(viewer, feature) {
					
					@Override
					protected CellEditor getCellEditor(Object element) {
						final String[] typeStrings = new String[elements.length];
						
						for (int i = 0; i < elements.length; i++) {
							typeStrings[i] = elements[i].toString();
						}
						
						return new ComboBoxCellEditor(viewer.getTable(), typeStrings) {
							@Override
							protected Object doGetValue() {
								int selection = (Integer) super.doGetValue();
								return typeStrings[selection];
							}
							
							@Override
							protected void doSetValue(Object value) {
								String str = (String) value;
								for (int i = 0; i < typeStrings.length; i++) {
									if (typeStrings[i].equals(str)) {
										super.doSetValue(i);
										return;
									}
								}
								
								return;
							}
						};
					}
					
					@Override
					protected Object toEValue(String value) {
						return ModelFactory.eINSTANCE.createFromString((EDataType) eType, value);
					}
				};
			}
			
			if ("TExpression".equals(eType.getName())) {
				return new EObjectAttributeEditingSupport<EObject>(viewer, feature) {
					@Override
					protected Object toEValue(String value) {
						return ModelFactory.eINSTANCE.createFromString((EDataType) eType, value);
					}
				};
			}
		}
		
		return new EObjectAttributeEditingSupport<EObject>(viewer, feature);
	}
}