package org.eclipse.bpmn2.modeler.ui.property.diagrams;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Import;
import org.eclipse.bpmn2.impl.DefinitionsImpl;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2TableComposite;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite.AbstractPropertiesProvider;
import org.eclipse.bpmn2.modeler.ui.property.dialogs.SchemaImportDialog;
import org.eclipse.bpmn2.provider.Bpmn2ItemProviderAdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;

public class DefinitionsPropertyComposite extends DefaultPropertiesComposite  {

	public DefinitionsPropertyComposite(Composite parent, int style) {
		super(parent, style);
	}

	private AbstractPropertiesProvider definitionsPropertiesProvider;

	/**
	 * @param section
	 */
	public DefinitionsPropertyComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	@Override
	public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
		if (definitionsPropertiesProvider==null) {
			definitionsPropertiesProvider = new AbstractPropertiesProvider(object) {
				String[] properties = new String[] {
						"name",
						"targetNamespace",
						"typeLanguage",
						"expressionLanguage",
						"exporter",
						"exporterVersion",
						"imports",
						"relationships"
				};
				
				@Override
				public String[] getProperties() {
					return properties; 
				}

				@Override
				public boolean alwaysShowAdvancedProperties() {
					return true;
				}
			};
		}
		return definitionsPropertiesProvider;
	}

	@Override
	protected void bindList(EObject object, EStructuralFeature feature) {
		if ("imports".equals(feature.getName())) {
			ImportsTable importsTable = new ImportsTable(propertySection);
			importsTable.bind();
		}
		else if ("relationships".equals(feature.getName())) {
			AbstractBpmn2TableComposite table = new AbstractBpmn2TableComposite(propertySection, AbstractBpmn2TableComposite.DEFAULT_STYLE);
			table.bindList(getEObject(), feature);
		}
		else {
			super.bindList(object, feature);
		}
	}

	public class ImportsTable extends AbstractBpmn2TableComposite {

		/**
		 * @param parent
		 * @param style
		 */
		public ImportsTable(AbstractBpmn2PropertySection section) {
			super(section, DEFAULT_STYLE);
		}


		public void bind() {
			DefinitionsImpl definitions = (DefinitionsImpl)getEObject();
			EStructuralFeature imports = definitions.eClass().getEStructuralFeature("imports");
			
			super.bindList(definitions, imports);
		}

		
		@Override
		protected EObject editListItem(EObject object, EStructuralFeature feature) {
			return super.editListItem(object, feature);
		}


		@Override
		protected boolean removeListItem(EObject object, EStructuralFeature feature, Object item) {
			return super.removeListItem(object, feature, item);
		}

		@Override
		protected EObject addListItem(EObject object, EStructuralFeature feature) {
			SchemaImportDialog dialog = new SchemaImportDialog(
					this.getShell(), getEObject());
			dialog.configureAsWSDLImport();
			if (dialog.open() != Window.OK) {
				return null;
			}
			Object result[] = dialog.getResult();
			if (result.length < 1) {
				return null;
			}
			
			Import newItem = null;
			Definitions bpmn2Definitions = (Definitions)object;
			if (result[0] instanceof Definition) {
				newItem = MODEL_FACTORY.createImport();

				Definition wsdlDefinition = (Definition)result[0];
				newItem.setImportType("http://schemas.xmlsoap.org/wsdl/");
				newItem.setLocation(wsdlDefinition.getLocation());
				newItem.setNamespace(wsdlDefinition.getTargetNamespace());

				bpmn2Definitions.getImports().add(newItem);
				ModelUtil.addID(newItem);
			}
			else {
				// XSD
				// importType = "http://www.w3.org/2001/XMLSchema"
			}
			return newItem;
		}
		
	}
}
