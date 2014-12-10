package org.camunda.bpm.modeler.ui.property.tabs.builder;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.utils.ExtensionUtil;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.PropertiesType;
import org.camunda.bpm.modeler.runtime.engine.model.PropertyType;
import org.camunda.bpm.modeler.ui.change.filter.AnyNestedChangeFilter;
import org.camunda.bpm.modeler.ui.change.filter.ExtensionChangeFilter;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.AbstractDeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.ContentProvider;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.DeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EditableEObjectTableBuilder;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EditableTableDescriptor.ElementFactory;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * Builder for the extensions properties.
 * 
 * @author kristin.polenz
 */
public class ExtensionsPropertiesBuilder extends AbstractPropertiesBuilder<BaseElement> {

	private static final String[] PROPERTIES_TABLE_HEADERS = { "name", "value"};

	private static final EStructuralFeature[] PROPERTY_FEATURES = {
		ModelPackage.eINSTANCE.getPropertyType_Name(),
		ModelPackage.eINSTANCE.getPropertyType_Value() 
	};

	/**
	 * Creates a new factory from the given arguments
	 * 
	 * @param parent
	 * @param section
	 * @param bo
	 */
	public ExtensionsPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		super(parent, section, bo);
	}

	/**
	 * Creates the extensions for the given bpmn element
	 */
	@Override
	public void create() {
		createMappingTable(section, parent, "Extensions", ModelPackage.eINSTANCE.getDocumentRoot_Properties(), PROPERTY_FEATURES, PROPERTIES_TABLE_HEADERS);
	}

	protected void createMappingTable(final GFPropertySection section, final Composite parent, String label, final EStructuralFeature feature, EStructuralFeature[] columnFeatures, String[] columnLabels) {

			// composite for mappings table
			final Composite composite = PropertyUtil.createStandardComposite(section, parent);

			final ElementFactory<PropertyType> elementFactory = new ElementFactory<PropertyType>() {

				@Override
				public PropertyType create() {
					return (PropertyType) transactionalCreateType(feature);
				}
			};

			ContentProvider<PropertyType> contentProvider = new ContentProvider<PropertyType>() {

				@Override
				public List<PropertyType> getContents() {
					List<PropertyType> contents = new ArrayList<PropertyType>();

					List<PropertiesType> propertiesList = ExtensionUtil.getExtensions(bo, PropertiesType.class);
					if(propertiesList != null && !propertiesList.isEmpty()) {
						PropertiesType propertiesType = propertiesList.get(0);
						contents.addAll((List<PropertyType>) propertiesType.getProperty());
					}
					return contents;
				}
			};

			DeleteRowHandler<PropertyType> deleteHandler = new AbstractDeleteRowHandler<PropertyType>() {
				@Override
				public void rowDeleted(PropertyType element) {
					transactionalRemoveMapping(element, feature);
				}
			};

			EditableEObjectTableBuilder<PropertyType> builder = new EditableEObjectTableBuilder<PropertyType>(section, composite, PropertyType.class);

			builder
				.elementFactory(elementFactory)
				.contentProvider(contentProvider)
				.columnFeatures(columnFeatures)
				.columnLabels(columnLabels)
				.deleteRowHandler(deleteHandler)
				.model(bo)
				.changeFilter(new ExtensionChangeFilter(bo, feature).or(
							  new AnyNestedChangeFilter(bo, feature)));

			final TableViewer viewer = builder.build();

			// table composite ////////////
			final Composite tableComposite = viewer.getTable().getParent();

			// create label
			PropertyUtil.createLabel(section, composite, label, tableComposite);
		}

		protected void transactionalRemoveMapping(final EObject element, final EStructuralFeature feature) {
			TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
			editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {

				@Override
				protected void doExecute() {
					// check whether extension properties exists
					PropertiesType propertiesType = null;
					List<PropertiesType> propertiesList = ExtensionUtil.getExtensions(bo, PropertiesType.class);
					if(propertiesList.isEmpty()) {
						ExtensionUtil.removeExtensionByFeature(bo, feature);
					} else {
						// extension properties exists only once
						propertiesType = propertiesList.get(0);
						propertiesType.getProperty().remove(element);
						// update model
						if (propertiesType.getProperty().isEmpty()) {
							ExtensionUtil.removeExtensionByFeature(bo, feature);
							// remove empty extension element
							EStructuralFeature extensionValuesFeature = bo.eClass().getEStructuralFeature("extensionValues");
							ExtensionAttributeValue values = ExtensionUtil.getExtensionAttributeValue(bo);
							if (values != null && (values.getValue() == null || values.getValue().isEmpty())) {
								bo.eUnset(extensionValuesFeature);
							}
						} else {
							ExtensionUtil.updateExtension(bo, feature, propertiesType);
						}
					}
				}
			});
		}

		private PropertyType transactionalCreateType(final EStructuralFeature feature) {

			final PropertyType propertyType = ModelFactory.eINSTANCE.createPropertyType();

			TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
			editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {

				@Override
				protected void doExecute() {
					PropertiesType propertiesType = null;
					List<PropertiesType> propertiesList = ExtensionUtil.getExtensions(bo, PropertiesType.class);
					if (propertiesList.isEmpty()) {
						propertiesType = ModelFactory.eINSTANCE.createPropertiesType();
						ExtensionUtil.addExtension(bo, feature, propertiesType);
					} else {
						propertiesType = propertiesList.get(0);
					}

					propertiesType.getProperty().add(propertyType);
				}
			});

			return propertyType;
		}
}
