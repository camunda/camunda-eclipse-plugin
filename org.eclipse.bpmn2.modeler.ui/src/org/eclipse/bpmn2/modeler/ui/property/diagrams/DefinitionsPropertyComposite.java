package org.eclipse.bpmn2.modeler.ui.property.diagrams;

import java.util.Map.Entry;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.Import;
import org.eclipse.bpmn2.impl.DefinitionsImpl;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.NamespaceUtil;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2TableComposite;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2TableComposite.AbstractTableProvider;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2TableComposite.TableColumn;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.dialogs.SchemaImportDialog;
import org.eclipse.bpmn2.modeler.ui.property.editors.ObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.TextAndButtonObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.TextObjectEditor;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.xsd.XSDSchema;

public class DefinitionsPropertyComposite extends DefaultPropertiesComposite  {

	public DefinitionsPropertyComposite(Composite parent, int style) {
		super(parent, style);
	}

	private AbstractPropertiesProvider propertiesProvider;

	/**
	 * @param section
	 */
	public DefinitionsPropertyComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	@Override
	public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
		if (propertiesProvider==null) {
			propertiesProvider = new AbstractPropertiesProvider(object) {
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
			};
		}
		return propertiesProvider;
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

		private AbstractTableProvider tableProvider;
		
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
		public AbstractTableProvider getTableProvider(EObject object, EStructuralFeature feature) {
			if (tableProvider==null) {
				tableProvider = new AbstractTableProvider() {
					@Override
					public boolean canModify(EObject object, EStructuralFeature feature, EObject item) {
						return false;
					}
				};
				
				// add a namespace prefix column that does NOT come from the Import object
				TableColumn tableColumn = new TableColumn(object,null) {
					@Override
					public String getHeaderText() {
						return "Namespace Prefix";
					}
	
					@Override
					public String getText(Object element) {
						Import imp = (Import)element;
						String prefix = NamespaceUtil.getPrefixForNamespace(imp, imp.getNamespace());
						if (prefix!=null)
							return prefix;
						return "";
					}
				};
				tableProvider.add(tableColumn);
				// add remaining columns
				EClass eClass = Bpmn2Package.eINSTANCE.getImport();
				tableProvider.add(new TableColumn(object,
						(EAttribute)eClass.getEStructuralFeature("namespace")));
				tableProvider.add(new TableColumn(object,
						(EAttribute)eClass.getEStructuralFeature("location")));
				tableProvider.add(new TableColumn(object,
						(EAttribute)eClass.getEStructuralFeature("importType")));
	
				setTableProvider(tableProvider);
			}
			return tableProvider;
		}


		@Override
		protected EObject editListItem(EObject object, EStructuralFeature feature) {
			return super.editListItem(object, feature);
		}


		@Override
		protected boolean removeListItem(EObject object, EStructuralFeature feature, Object item) {
			Definitions defs = (Definitions)object;
			Import imp = (Import)item;
			boolean canRemoveNamespace = true;
			for (Import i : defs.getImports()) {
				if (i!=imp) {
					String loc1 = i.getLocation();
					String loc2 = imp.getLocation();
					String ns1 = i.getNamespace();
					String ns2 = imp.getNamespace();
					// different import locations, same namespace?
					if (loc1!=null && loc2!=null && !loc1.equals(loc2) &&
							ns1!=null && ns2!=null && ns1.equals(ns2)) {
						// this namespace is still in use by another import!
						canRemoveNamespace = false;
						break;
					}
				}
			}
			if (canRemoveNamespace)
				NamespaceUtil.removeNamespace(imp, imp.getNamespace());
			return super.removeListItem(object, feature, item);
		}

		@Override
		protected EObject addListItem(EObject object, EStructuralFeature feature) {
			SchemaImportDialog dialog = new SchemaImportDialog(getShell(), object);
			if (dialog.open() == Window.OK) {
				Object result[] = dialog.getResult();
				if (result.length == 1) {
					return ModelHandler.addImport(object, result[0]);
				}
			}
			return null;
		}
	}
	
	public class ImportPropertiesComposite extends DefaultPropertiesComposite {

		private Text text;
		private Button button;
		
		public ImportPropertiesComposite(Composite parent, int style) {
			super(parent, style);
		}

		/**
		 * @param section
		 */
		public ImportPropertiesComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}
		
		@Override
		public void createBindings(EObject be) {
			final Import imp = (Import)be;
			
			Composite composite = getAttributesParent();
			TextAndButtonObjectEditor editor = new TextAndButtonObjectEditor(this,be,null) {

				@Override
				protected void buttonClicked() {
					IInputValidator validator = new IInputValidator() {

						@Override
						public String isValid(String newText) {
							String ns = NamespaceUtil.getNamespaceForPrefix(imp, newText);
							if (ns==null)
								return null;
							return "Prefix "+newText+" is already used for namespace\n"+ns;
						}
						
					};
					InputDialog dialog = new InputDialog(
							getShell(),
							"Namespace Prefix",
							"Enter a namespace prefix",
							getNamespacePrefix(),
							validator);
					if (dialog.open()==Window.OK){
						updateObject(dialog.getValue());
					}
				}
				
				protected boolean updateObject(final Object value) {
					// remove old prefix
					String prefix = text.getText();
					NamespaceUtil.removeNamespaceForPrefix(imp, prefix);
					// and add new
					NamespaceUtil.addNamespace(imp, (String)value, imp.getNamespace());
					updateText(value);
					return true;
				}
				
				protected String getTextValue(Object value) {
					if (value==null) {
						return getNamespacePrefix();
					}
					return (String)value;
				}
			};
			editor.createControl(composite,"Namespace Prefix",SWT.NONE);
			
			super.createBindings(be);
		}
		
		private String getNamespacePrefix() {
			Import imp = (Import)be;
			String prefix = NamespaceUtil.getPrefixForNamespace(imp, imp.getNamespace());
			if (prefix==null)
				prefix = "";
			return prefix;
		}
	}
}
