package org.camunda.bpm.modeler.ui.property.tabs.builder;

import static org.camunda.bpm.modeler.core.utils.ExtensionUtil.addExtension;
import static org.camunda.bpm.modeler.core.utils.ExtensionUtil.removeExtensionByValue;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.utils.ExtensionUtil;
import org.camunda.bpm.modeler.runtime.engine.model.FieldType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.change.filter.ExtensionChangeFilter;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.AbstractDeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.ContentProvider;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.DeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EditableEObjectTableBuilder;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EObjectAttributeTableColumnDescriptor.EditingSupportProvider;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EditableTableDescriptor.ElementFactory;
import org.camunda.bpm.modeler.ui.property.tabs.util.HelpText;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * Builder for field injections
 * 
 * @author kristin.polenz
 */
public class FieldInjectionsPropertiesBuilder extends AbstractPropertiesBuilder<BaseElement> {

	private static final String[] FIELD_INJECTIONS_TYPE_FEATURE_NAMES = { 
		"Name",
		"String",
		"Expression" 
	};
	
	private static final EStructuralFeature[] FIELD_INJECTIONS_TYPE_FEATURES = { 
		ModelPackage.eINSTANCE.getFieldType_Name(),
		ModelPackage.eINSTANCE.getFieldType_String(),
		ModelPackage.eINSTANCE.getFieldType_Expression()
	};
	
	public FieldInjectionsPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		super(parent, section, bo);
	}
	
	@Override
	public void create() {
		createMappingsTable(
			section, parent, 
			FieldType.class, "Field Injections",
			ModelPackage.eINSTANCE.getFieldType(),
			ModelPackage.eINSTANCE.getDocumentRoot_Field(),
			FIELD_INJECTIONS_TYPE_FEATURES,
			FIELD_INJECTIONS_TYPE_FEATURE_NAMES
		);
	}
	
	private <T extends EObject> void createMappingsTable(
			GFPropertySection section, Composite parent, 
			final Class<T> typeCls, String label, 
			final EClass typeECls, final EStructuralFeature feature, EStructuralFeature[] columnFeatures, String[] columnLabels) {

		// composite for mappings table
		Composite composite = PropertyUtil.createStandardComposite(section, parent);
		
		ElementFactory<T> elementFactory = new ElementFactory<T>() {
			@Override
			public T create() {
				T instance = (T) transactionalCreateType(typeECls, feature);
				return instance;
			}
		};

		ContentProvider<T> contentProvider = new ContentProvider<T>() {

			@Override
			public List<T> getContents() {
				List<FieldType> contents = new ArrayList<FieldType>();
				
				List<T> extensions = ExtensionUtil.getExtensions(bo, typeCls);
				if (extensions != null && !extensions.isEmpty()) {
					for (EObject eObject : extensions) {
						FieldType fieldType = (FieldType) eObject;
						/** 
						 *  support only field injections format with child-elements
						 *  e.g.
						 *  <camunda:field name="element"> 
						 *  	<camunda:string>myString</camunda:string>
						 *  </camunda:field>
						 *  ignore field injections format with attributes
						 *  e.g.
						 *  <camunda:field name="attribute" string="myString" /> 
						 */
						if (fieldType.getString() != null || fieldType.getExpression() != null) {
							contents.add(fieldType);
						}
					}
				}
				return (List<T>) contents;
			}
		};
		
		DeleteRowHandler<T> deleteHandler = new AbstractDeleteRowHandler<T>() {
			@Override
			public void rowDeleted(T element) {
				transactionalRemoveMapping(element);
			}
		};
		
		EditingSupportProvider editingSupportProvider = new DefaultEditingSupportProvider();
		
		EditableEObjectTableBuilder<T> builder = new EditableEObjectTableBuilder<T>(section, composite, typeCls);
		
		builder
			.elementFactory(elementFactory)
			.contentProvider(contentProvider)
			.editingSupportProvider(editingSupportProvider)
			.columnFeatures(columnFeatures)
			.columnLabels(columnLabels)
			.deleteRowHandler(deleteHandler)
			.attachTableEditNote(false)
			.model(bo)
			.changeFilter(new ExtensionChangeFilter(bo, feature));
		
		final TableViewer viewer = builder.build();
		
		// table composite ////////////
		final Composite tableComposite = viewer.getTable().getParent();
		PropertyUtil.attachNoteWithLink(section, tableComposite, HelpText.TABLE_HELP + " " + HelpText.FIELD_INJECTIONS);

		// create label
		PropertyUtil.createLabel(section, composite, label, tableComposite);
	}
	
	protected void transactionalRemoveMapping(final EObject element) {
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
			
			@Override
			protected void doExecute() {
				removeExtensionByValue(bo, element);
			}
		});
	}
	
	private EObject transactionalCreateType(EClass typeCls, final EStructuralFeature feature) {
		final FieldType fieldType = (FieldType) ModelFactory.eINSTANCE.create(typeCls);
		
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
			
			@Override
			protected void doExecute() {
				addExtension(bo, feature, fieldType);
			}
		});
		
		return fieldType;
	}
}
