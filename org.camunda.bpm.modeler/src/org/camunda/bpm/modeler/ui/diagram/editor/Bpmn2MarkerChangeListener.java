package org.camunda.bpm.modeler.ui.diagram.editor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.modeler.core.Bpmn2TabbedPropertySheetPage;
import org.camunda.bpm.modeler.core.validation.BpmnValidationStatusLoader;
import org.camunda.bpm.modeler.core.validation.ValidationStatusAdapter;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.properties.PropertySheet;

public class Bpmn2MarkerChangeListener implements IResourceChangeListener {

	Bpmn2Editor editor;

	public Bpmn2MarkerChangeListener(Bpmn2Editor editor) {
		this.editor = editor;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {

		IPath diagramPath = getModelPath();
		
		final IResourceDelta modelFileDelta = event.getDelta().findMember(diagramPath);
		if (modelFileDelta == null) {
			return;
		}
		
		final IMarkerDelta[] markerDeltas = modelFileDelta.getMarkerDeltas();
		if (markerDeltas == null || markerDeltas.length == 0) {
			return;
		}

		final List<IMarker> newMarkers = new ArrayList<IMarker>();
		final Set<String> deletedMarkers = new HashSet<String>();
		for (IMarkerDelta markerDelta : markerDeltas) {
			switch (markerDelta.getKind()) {
			case IResourceDelta.ADDED:
				newMarkers.add(markerDelta.getMarker());
				break;
			case IResourceDelta.CHANGED:
				newMarkers.add(markerDelta.getMarker());
				// fall through
			case IResourceDelta.REMOVED:
				final String uri = markerDelta.getAttribute(EValidator.URI_ATTRIBUTE, null);
				if (uri != null) {
					deletedMarkers.add(uri);
				}
			}
		}

		final Set<EObject> updatedObjects = new LinkedHashSet<EObject>();
		for (String uri : deletedMarkers) {
			final EObject eobject = editor.getEditingDomain().getResourceSet().getEObject(URI.createURI(uri), false);
			if (eobject == null) {
				continue;
			}
			final ValidationStatusAdapter adapter = (ValidationStatusAdapter) EcoreUtil.getRegisteredAdapter(eobject,
					ValidationStatusAdapter.class);
			if (adapter == null) {
				continue;
			}
			adapter.clearValidationStatus();
			updatedObjects.add(eobject);
		}

		BpmnValidationStatusLoader vsl = new BpmnValidationStatusLoader(editor);
		updatedObjects.addAll(vsl.load(newMarkers));
		editor.getEditorSite().getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// Refresh editor's graphical viewer...
				editor.getDiagramBehavior().refresh();

				// ...and property pages in case there are errors that
				// do not appear as figure decorators on the canvas
				IWorkbenchPage page = editor.getEditorSite().getPage();
				String viewID = "org.eclipse.ui.views.PropertySheet";
				try {
					IViewReference[] views = page.getViewReferences();
					for (IViewReference v : views) {
						if (viewID.equals(v.getId())) {
							PropertySheet ps = (PropertySheet) v.getView(true);
							IPage pp = ps.getCurrentPage();
							if (pp instanceof Bpmn2TabbedPropertySheetPage) {
								((Bpmn2TabbedPropertySheetPage) pp).refresh();
							}
						}
					}
				} catch (Exception e) {
				}
			}
		});
	}

	private IPath getModelPath() {
		Bpmn2DiagramEditorInput editorInput = editor.getEditorInput();
		URI modelUri = editorInput.getModelUri();
		
		String modelPath = modelUri.toPlatformString(true);
		return new Path(modelPath);
	}

}
