package org.eclipse.bpmn2.modeler.runtime.activiti.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.ui.internal.editor.GFFigureCanvas;
import org.eclipse.graphiti.ui.internal.fixed.FixedScaledGraphics;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

public class DiagramExport extends AbstractCustomFeature {
	
	// initial values
	protected GraphicalViewer _graphicalViewer;
	protected final String folderName = "";
	protected final String fileName = "preview.jpg";
	protected final double scale = 1.0;

	/**
	 * <code>_allFigure</code> represents a figure that contains all printable
	 * layers
	 */
	protected IFigure _allFigure;
	protected boolean _insideInternalModify = false;

	// selected values
	/**
	 * Image corresponding to the whole diagram (scaled version)
	 */
	private Image _image;

	public DiagramExport(IFeatureProvider fp) {
		super(fp);
	}

	@Override

	public String getDescription() {
		return "Export preview to " + folderName + "/" + fileName;
	}

	@Override
	public String getName() {
		return "&Preview";
	}

	///////////////////////
	// 
	// Suffix handling / copied from org.activiti.engine.impl.bpmn.deployer.BpmnDeployer (camunda-engine)
	// and altered for use in bpmn modeler
	//
	///////////////////////
	
	protected static final String[] BPMN_RESOURCE_SUFFIXES = new String[] { ".bpmn20", ".bpmn", ".bpmn20.xml" };
  
	protected String stripBpmnFileSuffix(String bpmnFileResource) {
		for (String suffix : BPMN_RESOURCE_SUFFIXES) {
			if (bpmnFileResource.endsWith(suffix)) {
				return bpmnFileResource.substring(0, bpmnFileResource.length() - suffix.length());
			}
		}
		return bpmnFileResource;
	}

	@SuppressWarnings("restriction")
	public void execute(ICustomContext context) {
		
		try {
			Resource diagramResource = BPMN2Editor.getActiveEditor().getDiagramResource();
			if (diagramResource == null) {
				logStatus(Status.WARNING, "Could not export diagram image: Could not determine diagram file", null);
				return;
			}
			
			IFile diagramImageFile = createDiagramImageFile(diagramResource);
			if (diagramImageFile == null) {
				logStatus(Status.WARNING, "Could not export diagram image: Could not determine diagram file", null);
				return;
			}
			
			// get the graphical Viewer
			_graphicalViewer = BPMN2Editor.getActiveEditor().getGraphicalViewer();
			
			_allFigure = determineRootFigure();
			// create the image
			setScaledImage(scale);
			startSaveAsImageWithoutDialog(_image, diagramImageFile);
		} catch (Exception e) {
			logStatus(Status.ERROR, "Could not export diagram image", e);
		}
	}

	private IFile createDiagramImageFile(Resource diagramResource) {
		String resourceName = diagramResource.getURI().lastSegment();
		
		// the resource obtained may be a temp file.
		// that said, eclipse suffixes it with a random string, e.g. mydiagram.bpmn20._1GPVcVmjEeKNldb_3G1Fsw
		// we need to get rid of that string
		String diagramFileName = resourceName.substring(0, resourceName.lastIndexOf("."));
		
		String pictureFileName = stripBpmnFileSuffix(diagramFileName);
		
		return createDirectoryResource(diagramResource, folderName).getFile(new Path(pictureFileName + ".png"));
	}

	/**
	 * Log diagram export status
	 * 
	 * @param statusCode
	 * @param message
	 * @param error
	 */
	private void logStatus(int statusCode, String message, Throwable error) {
		Activator.logStatus(new Status(statusCode, Activator.PLUGIN_ID, message, error));
	}
	
	/**
	 * Creates or returns a folder in the root directory of the current project
	 * (as given by the resource)
	 * 
	 * @param diagramResource
	 *            used only to determine the current project.
	 * @param folderName
	 *            name of the (top level) folder in the project.
	 *            
	 * @return the folder, which is created if it did not exist.
	 */
	private IContainer createDirectoryResource(Resource diagramResource, String folderName) {
		
	  // this is the "real" resource,
	  // TODO check for correct type
	  Resource modelResource = diagramResource.getResourceSet().getResources().get(1);
	  URI resolvedFile = CommonPlugin.resolve(modelResource.getURI());
	  IContainer fileParent = null;
	  
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project = root.getProject(modelResource.getURI().segment(1));

		try {
			IFile file = root.findFilesForLocationURI(URIUtil.fromString(resolvedFile.toString()))[0];
			fileParent = file.getParent();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// at this point, no resources have been created
		if (!project.exists()) {
			return null;
		}
		
		if (!project.isOpen()) {
			try {
				project.open(null);
			} catch (CoreException e) {
				throw new IllegalStateException("Cannot open project", e);
			}
		}

		return fileParent;
	}

	/*
	 * copied from UiService and modified.
	 */
	public byte[] createImage(Image image, int format) throws Exception {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		try {
			ImageData imDat = image.getImageData();
			ImageLoader imageLoader = new ImageLoader();
			imageLoader.data = new ImageData[] { imDat };
			try {
				imageLoader.save(result, format);
			} catch (SWTException e) {
				String error = "Depth: " + Integer.toString(image.getImageData().depth) + "\n" + "X: " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						+ Integer.toString(image.getImageData().x)
						+ "\n" + "Y: " + Integer.toString(image.getImageData().y); //$NON-NLS-1$ //$NON-NLS-2$
				throw new IllegalStateException(error, e);
			}
		} finally {
			image.dispose();
		}
		return result.toByteArray();
	}

	/*
	 * copied from UiService
	 */
	public void startSaveAsImageWithoutDialog(Image im, IFile destination) {
		final Shell shell = BPMN2Editor.getActiveEditor().getSite().getShell();
		try {
			int imageFormat = SWT.IMAGE_PNG;
			byte image[] = createImage(im, imageFormat);
			final WorkspaceModifyOperation operation = getSaveToFileOp(destination, image);
			Display.getDefault().syncExec(new Runnable() {
				
				@Override
				public void run() {
					try {
						new ProgressMonitorDialog(shell).run(false, false, operation);
					} catch (Exception e) {
						throw new RuntimeException(e);
					} 
				}
			});
			
		} catch (Exception e) {
			String message = "Can not save image: "; //$NON-NLS-1$
			MessageDialog.openError(shell,
					"Can not save image", message + e.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 * Returns a WorkspeceModifyOperation, which saves the given contents to a
	 * File with the given filename.
	 * 
	 * @param filename
	 *            The name of the file, where to save the contents.
	 * @param contents
	 *            The contents to save into the file.
	 * @throws Exception
	 *             On any errors that occur. copied from UiService
	 */
	private WorkspaceModifyOperation getSaveToFileOp(final IFile destination,
			final byte contents[]) throws Exception {
		WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
			
			@Override
			protected void execute(IProgressMonitor monitor)
					throws CoreException {
				try {
				  destination.refreshLocal(1, monitor);
					InputStream source = new ByteArrayInputStream(contents);
					if (!destination.exists()) {
						destination.create(source, IResource.NONE, null);
					} else {
						destination.setContents(source, IResource.NONE, null);
					}
				} catch (Exception e) {
					// convert exceptions to CoreExceptions
					Status status = new Status(
							IStatus.ERROR,
							"Diagram Export", IStatus.ERROR, "Can not save image as file: " //$NON-NLS-1$
									+ e.getMessage(), e);
					throw new CoreException(status);
				}
			}
		};
		return operation;
	}

	@SuppressWarnings("restriction")
	private IFigure determineRootFigure() {
		// Code snipped copied from AbstractFigureSelectionDialog
		org.eclipse.swt.widgets.Control control = _graphicalViewer.getControl();
		if (control instanceof GFFigureCanvas) {
			GFFigureCanvas canvas = (GFFigureCanvas) control;
			canvas.regainSpace();
		}
		EditPart rootEditPart = _graphicalViewer.getRootEditPart();
		if (!(rootEditPart instanceof GraphicalEditPart))
			return null;
		// determine _allFigure
		GraphicalEditPart graphicalRootEditPart = (GraphicalEditPart) rootEditPart;
		IFigure rootFigure = ((LayerManager) graphicalRootEditPart)
				.getLayer(LayerConstants.PRINTABLE_LAYERS);
		if (rootFigure == null)
			return null;
		return rootFigure;
	}

	// Code snipped copied from AbstractFigureSelectionDialog
	public void setScaledImage(double scaleFactor) {
		cleanUp();
		_image = null;
		double upperBoundPixels = 3000.0d;
		initScaledImage(scaleFactor, upperBoundPixels);
	}

	@SuppressWarnings("restriction")
	private void initScaledImage(double scaleFactor, double upperBoundPixels) {
		GC gc = null;
		// if the scale factor is too high, the operating system will
		// not be able to provide a handle,
		// because the Image would require too much space. "no more
		// Handles"-Exception or "out of Memory" Error
		// will be thrown
		if (scaleFactor * _allFigure.getBounds().width > upperBoundPixels
				|| scaleFactor * _allFigure.getBounds().height > upperBoundPixels) {
			scaleFactor = Math.min(upperBoundPixels
					/ _allFigure.getBounds().width, upperBoundPixels
					/ _allFigure.getBounds().height);
		}
		_image = new Image(Display.getDefault(),
				(int) (_allFigure.getBounds().width * scaleFactor),
				(int) (scaleFactor * _allFigure.getBounds().height));
		gc = new GC(_image);
		Graphics graphics = null;
		if (scaleFactor != 1.0) {
			FixedScaledGraphics fsg = new FixedScaledGraphics(new SWTGraphics(
					gc));
			fsg.scale(scaleFactor);
			graphics = fsg;
		} else {
			graphics = new SWTGraphics(gc);
		}
		/* move all figures into the positive region */
		EditPart contents = _graphicalViewer.getContents();
		if (contents instanceof GraphicalEditPart) {
			IFigure contentsFigure = ((GraphicalEditPart) contents).getFigure();
			Rectangle contentBounds = contentsFigure.getBounds();
			graphics.translate(-contentBounds.x, -contentBounds.y);
		}
		final Graphics syncGaphics = graphics;
		
		Display.getDefault().syncExec( new Runnable() {  public void run() { 
			_allFigure.paint(syncGaphics);
		} });
		
		if (gc != null)
			gc.dispose();
		if (graphics != null)
			graphics.dispose();
	}

	public Image getScaledImage() {
		return _image;
	}

	public void cleanUp() {
		if (_image != null)
			_image.dispose();
	}

}
