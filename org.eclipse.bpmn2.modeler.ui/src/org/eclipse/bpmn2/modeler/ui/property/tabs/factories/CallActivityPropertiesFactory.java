package org.eclipse.bpmn2.modeler.ui.property.tabs.factories;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.InType;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelFactory;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EObjectAttributeTableColumnDescriptor;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EditableTableDescriptor;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.TableColumnDescriptor;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class CallActivityPropertiesFactory extends AbstractPropertiesFactory {

	private static final String CALLED_ELEMENT = "Called Element";

	private static final String[] TABLE_HEADERS = { "source", "sourceExpression", "target" };

	private static final EStructuralFeature[] IN_FEATURES = { 
		ModelPackage.eINSTANCE.getInType_Source(),
		ModelPackage.eINSTANCE.getInType_SourceExpression(), 
		ModelPackage.eINSTANCE.getInType_Target() };

	private static final EStructuralFeature[] OUT_FEATURES = { 
		ModelPackage.eINSTANCE.getOutType_Source(),
		ModelPackage.eINSTANCE.getOutType_SourceExpression(), 
		ModelPackage.eINSTANCE.getOutType_Target() };
	
	public CallActivityPropertiesFactory(Composite parent, GFPropertySection section, final EObject bo) {
		super(parent, section, bo);

		System.out.println(TransactionUtil.getEditingDomain(bo));
		System.out.println(BPMN2Editor.getActiveEditor().getEditingDomain());
		
//		List<InType> values = ModelUtil.getExtensionAttributeValues(bo, InType.class);
//
//		if (values.size() == 0) {
//
//			final InType type1 = ModelFactory.eINSTANCE.createInType();
//			type1.setSource("ASDF");
//			type1.setTarget("ASDF");
//
//			final InType type2 = ModelFactory.eINSTANCE.createInType();
//			type2.setSourceExpression("${foo.bar}");
//			type2.setTarget("ASDF");
//
//			TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
//			editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
//
//				@Override
//				protected void doExecute() {
//					ModelUtil.addExtensionAttributeValue(bo, ModelPackage.eINSTANCE.getDocumentRoot_In(), type1);
//					ModelUtil.addExtensionAttributeValue(bo, ModelPackage.eINSTANCE.getDocumentRoot_In(), type2);
//				}
//			});
//		}
	}

	@Override
	public void create() {
		
		// FIXME: Add broken callActiviti_CalledElement control
		
//		PropertyUtil.createText(section, parent, CALLED_ELEMENT,
//				ModelPackage.eINSTANCE.getCallActivity_CalledElement(), bo);

		createAttributesTable(section, parent);
	}

	private void createAttributesTable(GFPropertySection section, Composite parent) {

		Composite composite = PropertyUtil.createStandardComposite(section, parent);

		EditableTableDescriptor<InType> tableDescriptor = new EditableTableDescriptor<InType>() {
			
			@Override
			public List<InType> getValues() {
				List<InType> values = ModelUtil.getExtensionAttributeValues(bo, InType.class);
				
				InType addDummy = getAddDummy();
				if (addDummy != null) {
					values.add(addDummy);
				}
				
				return values;
			}
		};
		
		tableDescriptor.setAddDummy(ModelFactory.eINSTANCE.createInType());
		
		List<TableColumnDescriptor> columns = new ArrayList<TableColumnDescriptor>();

		for (int i = 0; i < TABLE_HEADERS.length; i++) {
			String title = TABLE_HEADERS[i];
			EStructuralFeature feature = IN_FEATURES[i];

			EObjectAttributeTableColumnDescriptor<EObject> descriptor = 
					new EObjectAttributeTableColumnDescriptor<EObject>(feature, title, 200);

			columns.add(descriptor);
		}
		
		tableDescriptor.setColumns(columns);

		TableViewer viewer = tableDescriptor.createTableViewer(composite);
		
		Control control = viewer.getControl();

		control.setLayoutData(PropertyUtil.STANDARD_LAYOUT);

		// create label
		PropertyUtil.createLabel(section, composite, "Input Mapping", control);
	}
}
