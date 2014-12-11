package org.camunda.bpm.modeler.ui.property.tabs.builder;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.utils.ExtensionUtil;
import org.camunda.bpm.modeler.runtime.engine.model.ConnectorType;
import org.camunda.bpm.modeler.runtime.engine.model.InputOutputType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.ParameterType;
import org.camunda.bpm.modeler.ui.change.filter.AnyNestedChangeFilter;
import org.camunda.bpm.modeler.ui.change.filter.ExtensionChangeFilter;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EditableEObjectTableBuilder;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.AbstractDeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.ContentProvider;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.DeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.EditRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.dialog.ParameterDialog;
import org.camunda.bpm.modeler.ui.property.tabs.tables.CreateViaDialogElementFactory;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EditableTableDescriptor.ElementFactory;
import org.camunda.bpm.modeler.ui.property.tabs.util.HelpText;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * @author Roman Smirnov
 */
public class InputOutputPropertiesBuilder extends AbstractPropertiesBuilder<BaseElement> {

	protected static final String INPUT_PARAMTERS_LABEL = "Input Parameters";
	protected static final String OUTPUT_PARAMTERS_LABEL = "Output Parameters";
	
	protected boolean insideConnector;

	public InputOutputPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		this(parent, section, bo, false);
	}
	
	public InputOutputPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo, boolean insideConnector) {
		super(parent, section, bo);
		this.insideConnector = insideConnector; 
	}
	
	private static final String[] TABLE_HEADERS = { "name"};
	
	private static final EStructuralFeature[] PARAMETER_FEATURES = {
		ModelPackage.eINSTANCE.getParameterType_Name()
	};

	private TableViewer viewer;
	
	@Override
	public void create() {
		// use correct inputOuputFeature
		EStructuralFeature inputOutputfeature = null;
		if (!insideConnector) {
			inputOutputfeature = ModelPackage.eINSTANCE.getDocumentRoot_InputOutput();
		} else {
			inputOutputfeature = ModelPackage.eINSTANCE.getConnectorType_InputOutput();
		}
		
		// input parameters
		EStructuralFeature inputParameterFeature = ModelPackage.eINSTANCE.getInputOutputType_InputParameters();
		createParameterTable(section, parent, INPUT_PARAMTERS_LABEL, inputOutputfeature, inputParameterFeature, PARAMETER_FEATURES, TABLE_HEADERS);
		
		if (insideConnector
				|| (!(bo instanceof EndEvent) && !bo.eIsSet(Bpmn2Package.eINSTANCE.getActivity_LoopCharacteristics()))) {
			
			// output parameters
			EStructuralFeature outputParameterFeature = ModelPackage.eINSTANCE.getInputOutputType_OutputParameters();
			createParameterTable(section, parent, OUTPUT_PARAMTERS_LABEL, inputOutputfeature, outputParameterFeature, PARAMETER_FEATURES, TABLE_HEADERS);
		}
	}

	protected void createParameterTable(
			final GFPropertySection section, final Composite parent, String label, 
			final EStructuralFeature inputOutputFeature, final EStructuralFeature parameterFeature,
			EStructuralFeature[] columnFeatures, String[] columnLabels) {

		// composite for mappings table
		final Composite composite = PropertyUtil.createStandardComposite(section, parent);
		
		final ElementFactory<ParameterType> elementFactory = new CreateViaDialogElementFactory<ParameterType>(TransactionUtil.getEditingDomain(bo)) {

			@Override
			protected ParameterType createType() {
				return createParameterType(parameterFeature);
			}

			@Override
			protected int editInDialog(ParameterType element) {
				return openEditDialog(element);
			}
		};
		
		ContentProvider<ParameterType> contentProvider = new ContentProvider<ParameterType>() {

			@Override
			public List<ParameterType> getContents() {
				List<ParameterType> contents = new ArrayList<ParameterType>();

				InputOutputType inputOutput = getInputOutput();
				
				if (inputOutput != null) {
					
					EList<ParameterType> parameters = getParameters(inputOutput, parameterFeature);
					contents.addAll(parameters);
				}

				return contents;
			}
		};
		
		DeleteRowHandler<ParameterType> deleteHandler = new AbstractDeleteRowHandler<ParameterType>() {
			@Override
			public void rowDeleted(ParameterType element) {
				transactionalRemoveMapping(element, parameterFeature);
			}
		};
		
		EditRowHandler<ParameterType> editRowHandler = new EditRowHandler<ParameterType>() {

			@Override
			public void rowEdit(ParameterType element) {
				openEditDialog(element);
			}

			@Override
			public boolean canEdit(ParameterType element) {
				return true;
			}
		};
		
		EditableEObjectTableBuilder<ParameterType> builder = new EditableEObjectTableBuilder<ParameterType>(section, composite, ParameterType.class);
		
		builder
			.doubleClickEditRowHandler(editRowHandler)
			.elementFactory(elementFactory)
			.contentProvider(contentProvider)
			.columnFeatures(columnFeatures)
			.columnLabels(columnLabels)
			.deleteRowHandler(deleteHandler)
			.attachTableEditNote(false)
			.model(bo)
			.changeFilter(new ExtensionChangeFilter(bo, inputOutputFeature).or(new AnyNestedChangeFilter(bo, inputOutputFeature) {
				
				@Override
				protected EObject getValue(EObject object, EStructuralFeature feature) {
					if (!insideConnector) {
						return super.getValue(object, feature);
					}
					
					ConnectorType connector = getConnector();
					InputOutputType inputOutput = null;
					
					if (connector != null) {
						inputOutput = connector.getInputOutput();
					}
					
					return inputOutput;
				}
				
			}));
		
		viewer = builder.build();

		Composite tableComposite = viewer.getTable().getParent();
		PropertyUtil.attachNote(tableComposite, HelpText.TABLE_HELP + " " + HelpText.SUPPORTED_VERSION_NOTE_7_2);

		// create label
		PropertyUtil.createLabel(section, composite, label, tableComposite);
	}
	
	protected void transactionalRemoveMapping(final EObject element, final EStructuralFeature parameterFeature) {
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
			
			@Override
			protected void doExecute() {
				InputOutputType inputOutput = getInputOutput();
				
				if (inputOutput != null) {
					EList<ParameterType> parameters = getParameters(inputOutput, parameterFeature);
					if (parameters != null && !parameters.isEmpty()) {
						parameters.remove(element);
					}
					
					if ((inputOutput.getInputParameters() == null || inputOutput.getInputParameters().isEmpty())
							&& (inputOutput.getOutputParameters() == null || inputOutput.getOutputParameters().isEmpty())) {
						
						if (!insideConnector) {
							ExtensionUtil.removeExtensionByFeature(bo, ModelPackage.eINSTANCE.getDocumentRoot_InputOutput());
						} else {
							ConnectorType connector = getConnector();
							
							if (connector != null) {
								connector.eUnset(ModelPackage.eINSTANCE.getConnectorType_InputOutput());
								
								String connectorId = connector.getConnectorId();
								if (connectorId == null || connectorId.isEmpty()) {
									ExtensionUtil.removeExtensionByFeature(bo, ModelPackage.eINSTANCE.getDocumentRoot_Connector());
								}
							}
						}
					}
				}
				
				// remove empty extension element
				EStructuralFeature extensionValuesFeature = bo.eClass().getEStructuralFeature("extensionValues");
				ExtensionAttributeValue values = ExtensionUtil.getExtensionAttributeValue(bo);
				if (values != null && (values.getValue() == null || values.getValue().isEmpty())) {
					bo.eUnset(extensionValuesFeature);
				}
			}
		});
	}
	
	private ParameterType createParameterType(final EStructuralFeature parameterFeature) {
		
		final ParameterType newParameter = ModelFactory.eINSTANCE.createParameterType();
		
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
		 
			@Override
			protected void doExecute() {

				InputOutputType inputOutput = getInputOutput();
				
				if (inputOutput == null) {
					inputOutput = ModelFactory.eINSTANCE.createInputOutputType();
					
					if (!insideConnector) {
						ExtensionUtil.addExtension(bo, ModelPackage.eINSTANCE.getDocumentRoot_InputOutput(), inputOutput);
						
					} else {
						ConnectorType connector = getConnector();
						
						if (connector == null) {
							connector = ModelFactory.eINSTANCE.createConnectorType();
							ExtensionUtil.addExtension(bo, ModelPackage.eINSTANCE.getDocumentRoot_Connector(), connector);
						}
						connector.setInputOutput(inputOutput);
					}
				}
				
				EList<ParameterType> parameters = getParameters(inputOutput, parameterFeature);
				parameters.add(newParameter);
			}
		});
		
		return newParameter;
	}
	
	protected int openEditDialog(ParameterType parameter) {
		// create dialog with ok and cancel button and warning icon
		ParameterDialog parameterDialog = new ParameterDialog(section, Display.getDefault().getActiveShell(), bo, parameter);
		
		// open dialog and await user selection
		return parameterDialog.open();
	}
	
	protected InputOutputType getInputOutput() {
		if (!insideConnector) {
			List<InputOutputType> extensions = ExtensionUtil.getExtensions(bo, InputOutputType.class);
			if(extensions != null && !extensions.isEmpty()) {
				// inputOutput exists only once in the extension value
				return extensions.get(0);
			}
		} else {
			ConnectorType connector = getConnector();
			if (connector != null) {
				return connector.getInputOutput();
			}
		}
		
		return null;
	}
	
	protected ConnectorType getConnector() {
		List<ConnectorType> extensions = ExtensionUtil.getExtensions(bo, ConnectorType.class);
		if (extensions != null && !extensions.isEmpty()) {
			// connector exists only once in the extension value
			return extensions.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected EList<ParameterType> getParameters(InputOutputType inputOutput, EStructuralFeature parameterFeature) {
		return (EList<ParameterType>) inputOutput.eGet(parameterFeature);
	}
	
}
