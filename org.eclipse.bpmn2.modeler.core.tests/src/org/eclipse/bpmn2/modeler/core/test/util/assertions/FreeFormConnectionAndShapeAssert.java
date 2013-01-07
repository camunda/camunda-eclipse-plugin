/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.core.test.util.assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.bpmn2.modeler.core.test.layout.util.LayoutUtilTest;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.mm.pictograms.impl.AnchorImpl;
import org.eclipse.graphiti.services.Graphiti;
import org.fest.assertions.api.AbstractAssert;
import static org.fest.assertions.api.Assertions.*;

import static java.lang.String.format;

public class FreeFormConnectionAndShapeAssert extends AbstractAssert<FreeFormConnectionAndShapeAssert, FreeFormConnection> {

	private Shape connectedShape;
	
	private FreeFormConnectionAssert parentAssert;

	public FreeFormConnectionAndShapeAssert(FreeFormConnectionAssert parentAssert,
			FreeFormConnection actual, Shape connectedShape) {
		
		super(actual, FreeFormConnectionAndShapeAssert.class);
		
		assertThat(connectedShape).isNotNull();
		
		this.connectedShape = connectedShape;
		this.parentAssert = parentAssert;
	}
	
	public FreeFormConnectionAssert end() {
		return parentAssert;
	}

	public FreeFormConnectionAndShapeAssert exists() {
		Anchor anchor = getAnchor();
		
		assertThat(anchor).as("connecting anchor").isNotNull();
		
		return myself;
	}

	public FreeFormConnectionAndShapeAssert isLeftOfShape() {
		Anchor anchor = getAnchor();
		Sector sector = getVisibleAnchorSector(anchor);
		
		assertExpectedSector(sector, Sector.LEFT, Sector.BOTTOM_LEFT, Sector.TOP_LEFT);
		
		return myself;
	}

	public FreeFormConnectionAndShapeAssert isRightOfShape() {
		Anchor anchor = getAnchor();
		Sector sector = getVisibleAnchorSector(anchor);

		assertExpectedSector(sector, Sector.RIGHT, Sector.BOTTOM_RIGHT, Sector.TOP_RIGHT);
		
		return myself;
	}

	public FreeFormConnectionAndShapeAssert isAboveShape() {
		
		Anchor anchor = getAnchor();
		Sector sector = getVisibleAnchorSector(anchor);
		
		assertExpectedSector(sector, Sector.TOP_LEFT, Sector.TOP, Sector.TOP_RIGHT);
		
		return myself;
	}

	public FreeFormConnectionAndShapeAssert isAt(Sector expectedSector) {

		Anchor anchor = getAnchor();
		Sector sector = getVisibleAnchorSector(anchor);

		assertExpectedSector(sector, expectedSector);
		
		return myself;
	}
	
	public FreeFormConnectionAndShapeAssert isBeneathShape() {

		Anchor anchor = getAnchor();
		Sector sector = getVisibleAnchorSector(anchor);

		assertExpectedSector(sector, Sector.BOTTOM_LEFT, Sector.BOTTOM, Sector.BOTTOM_RIGHT);
		
		return myself;
	}
	
	private Sector getVisibleAnchorSector(Anchor anchor) {
		if (anchor.equals(LayoutUtil.getCenterAnchor(connectedShape))) {
			ILocation visibleAnchorPosition = LayoutUtil.getVisibleAnchorLocation(anchor, actual);
			return LayoutUtil.getSector(visibleAnchorPosition, LayoutUtil.getAbsoluteRectangle(connectedShape));
		} else {
			return LayoutUtil.getAnchorSector(anchor);
		}
	}
	
	// private helpers /////////////////////////////////////////////
	
	private void assertExpectedSector(Sector sector, Sector ... expectedSectors) {
		
		List<Sector> expected = Arrays.asList(expectedSectors);
		boolean contains = expected.contains(sector);
		
		if (!contains) {
			String message = format("expected anchor position to be any of: <%s> but was: <%s>", new Object[] { expected, sector });
			fail(message);
		}
	}
	
	private Anchor getAnchor() {
		ArrayList<Anchor> anchors = new ArrayList<Anchor>(actual.getAnchors());
		
		anchors.add(actual.getStart());
		anchors.add(actual.getEnd());
		
		for (Anchor anchor : anchors) {
			if (anchor.getParent().equals(connectedShape)) {
				return anchor;
			}
		}
		
		return null;
	}
}
