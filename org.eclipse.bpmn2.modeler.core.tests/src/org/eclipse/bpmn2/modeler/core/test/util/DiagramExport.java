/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.test.util;

import java.io.ByteArrayOutputStream;

import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.internal.editor.GFFigureCanvas;
import org.eclipse.graphiti.ui.internal.fixed.FixedScaledGraphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * Utility class for exporting diagrams.
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 * 
 */
public class DiagramExport {

	public static byte[] exportAsPng(IDiagramTypeProvider diagramTypeProvider,
			Diagram diagram) {

		return new DiagramExport(diagram).exportAsPngBytes();
	}

	private Diagram diagram;

	private DiagramExport(Diagram diagram) {
		this.diagram = diagram;
	}

	private byte[] exportAsPngBytes() {
		GraphicalViewer graphicalViewer = BPMN2Editor.getActiveEditor()
				.getGraphicalViewer();
		Image image = getImage(graphicalViewer);

		return toImageBytes(image, SWT.IMAGE_PNG);
	}

	private Image getImage(GraphicalViewer graphicalViewer) {

		IFigure figure = getRootFigure(graphicalViewer);
		Image image = createScaledImage(figure, graphicalViewer, 1.0, 3000.0d);
		
		return image;
	}

	/**
	 * Converts an image to a {@link byte[]} representation using the specified
	 * format (e.g. {@link SWT.IMAGE_PNG}).
	 * 
	 * @param image
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public byte[] toImageBytes(Image image, int format) {

		ByteArrayOutputStream result = new ByteArrayOutputStream();

		try {
			ImageData imDat = image.getImageData();
			ImageLoader imageLoader = new ImageLoader();
			imageLoader.data = new ImageData[] { imDat };

			imageLoader.save(result, format);
		} finally {
			image.dispose();
		}

		return result.toByteArray();
	}

	@SuppressWarnings("restriction")
	private IFigure getRootFigure(GraphicalViewer viewer) {
		org.eclipse.swt.widgets.Control control = viewer.getControl();
		if (control instanceof GFFigureCanvas) {
			GFFigureCanvas canvas = (GFFigureCanvas) control;
			canvas.regainSpace();
		}

		EditPart rootEditPart = viewer.getRootEditPart();
		if (rootEditPart instanceof GraphicalEditPart) {
			GraphicalEditPart graphicalRootEditPart = (GraphicalEditPart) rootEditPart;
			IFigure figure = ((LayerManager) graphicalRootEditPart)
					.getLayer(LayerConstants.PRINTABLE_LAYERS);
			if (figure != null) {
				return figure;
			}
		}

		// figure could not be loaded
		return null;
	}

	@SuppressWarnings("restriction")
	private Image createScaledImage(final IFigure figure, GraphicalViewer viewer, double scaleFactor, double upperBoundPixels) {

		GC gc = null;
		Image image = null;
		
		// if the scale factor is too high, the operating system will
		// not be able to provide a handle,
		// because the Image would require too much space. "no more
		// Handles"-Exception or "out of Memory" Error
		// will be thrown
		int width = figure.getBounds().width;
		int height = figure.getBounds().height;
		
		if (scaleFactor * width > upperBoundPixels || scaleFactor * height > upperBoundPixels) {
			scaleFactor = Math.min(upperBoundPixels / width, upperBoundPixels / height);
		}
		
		image = new Image(Display.getDefault(),
				(int) (width * scaleFactor),
				(int) (height * scaleFactor));
		gc = new GC(image);
		
		Graphics graphics = new SWTGraphics(gc);
		
		// wrap with scale
		if (scaleFactor != 1.0) {
			FixedScaledGraphics fixedScaleGraphics = new FixedScaledGraphics(graphics);
			fixedScaleGraphics.scale(scaleFactor);
			graphics = fixedScaleGraphics;
		}
		
		/* move all figures into the positive region */
		EditPart contents = viewer.getContents();
		if (contents instanceof GraphicalEditPart) {
			IFigure contentsFigure = ((GraphicalEditPart) contents).getFigure();
			Rectangle contentBounds = contentsFigure.getBounds();
			graphics.translate(-contentBounds.x, -contentBounds.y);
		}
		
		final Graphics syncGaphics = graphics;
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				figure.paint(syncGaphics);
			}
		});

		if (gc != null) {
			gc.dispose();
		}
		
		if (graphics != null) {
			graphics.dispose();
		}
		
		return image;
	}
}
