package org.camunda.bpm.modeler.ui.diagram.editor;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.editor.DefaultPersistencyBehavior;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;

public class Bpmn2PersistencyBehavior extends DefaultPersistencyBehavior {

	public Bpmn2PersistencyBehavior(DiagramBehavior diagramBehavior) {
		super(diagramBehavior);
	}

	@Override
	protected Set<Resource> save(TransactionalEditingDomain editingDomain,
			Map<Resource, Map<?, ?>> saveOptions, IProgressMonitor monitor) {

		Diagram diagram = diagramBehavior.getDiagramTypeProvider().getDiagram();
		final Definitions definitions = BusinessObjectUtil.getFirstElementOfType(diagram, Definitions.class);

		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				Dictionary<String, String> bundleHeaders = Platform.getBundle("org.camunda.bpm.modeler").getHeaders();
				if (definitions.getExporter() == null) {
					definitions.setExporter(bundleHeaders.get("Bundle-Name"));
				}
				if (definitions.getExporterVersion() == null) {
					definitions.setExporterVersion(bundleHeaders.get("Bundle-Version"));
				}
			}
		});

		return super.save(editingDomain, saveOptions, monitor);
	}

	@Override
	protected Map<Resource, Map<?, ?>> createSaveOptions() {
		// Save only resources that have actually changed.
		final Map<Object, Object> saveOption = new HashMap<Object, Object>();

		saveOption.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);

		// Use CDATA to escape characters like '<' etc.
		saveOption.put(XMLResource.OPTION_ESCAPE_USING_CDATA, Boolean.TRUE);

		EList<Resource> resources = diagramBehavior.getEditingDomain().getResourceSet().getResources();
		final Map<Resource, Map<?, ?>> saveOptions = new HashMap<Resource, Map<?, ?>>();
		for (Resource resource : resources) {
			saveOptions.put(resource, saveOption);
		}
		return saveOptions;
	}
}
