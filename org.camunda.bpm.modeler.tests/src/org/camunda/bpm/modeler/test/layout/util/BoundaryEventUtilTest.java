package org.camunda.bpm.modeler.test.layout.util;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.location;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.core.layout.util.BoundaryEventUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.camunda.bpm.modeler.ui.features.event.BoundaryAttachment;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.junit.Test;

/**
 * Boundary event util tests
 * 
 * @author nico.rehwaldt
 */
public class BoundaryEventUtilTest {

	private static IRectangle RECT = 
		rectangle(40, 40, 100, 80);
	
	
	// GET ATTACHMENT TESTS /////////////////////////////////////////////
	
	@Test
	public void testGetAttachmentTopLeft() {
		
		// given
		ILocation boundaryCenter = location(40, 40);
		IRectangle attachedToBounds = RECT;
		
		Sector expectedSector = Sector.TOP_LEFT;
		double expectedPercentage = -1d;
		
		// when
		BoundaryAttachment attachement = BoundaryEventUtil.getAttachment(boundaryCenter, attachedToBounds);
		
		// then
		assertThat(attachement.getSector()).isEqualTo(expectedSector);
		assertThat(attachement.getPercentage()).isEqualTo(expectedPercentage);
	}
	
	@Test
	public void testGetAttachmentTopTolerance() {
		
		// given
		ILocation boundaryCenter = location(44, 44);
		IRectangle attachedToBounds = RECT;
		
		Sector expectedSector = Sector.TOP_LEFT;
		double expectedPercentage = -1d;
		
		// when
		BoundaryAttachment attachement = BoundaryEventUtil.getAttachment(boundaryCenter, attachedToBounds);
		
		// then
		assertThat(attachement.getSector()).isEqualTo(expectedSector);
		assertThat(attachement.getPercentage()).isEqualTo(expectedPercentage);
	}

	@Test
	public void testGetAttachmentBottomRight() {
		
		// given
		ILocation boundaryCenter = location(140, 120);
		IRectangle attachedToBounds = RECT;
		
		Sector expectedSector = Sector.BOTTOM_RIGHT;
		double expectedPercentage = -1d;
		
		// when
		BoundaryAttachment attachement = BoundaryEventUtil.getAttachment(boundaryCenter, attachedToBounds);
		
		// then
		assertThat(attachement.getSector()).isEqualTo(expectedSector);
		assertThat(attachement.getPercentage()).isEqualTo(expectedPercentage);
	}
	
	@Test
	public void testGetAttachmentTop() {
		
		// given
		ILocation boundaryCenter = location(50, 40);
		IRectangle attachedToBounds = RECT;
		
		Sector expectedSector = Sector.TOP;
		double expectedPercentage = 10.0 / 100;
		
		// when
		BoundaryAttachment attachement = BoundaryEventUtil.getAttachment(boundaryCenter, attachedToBounds);
		
		// then
		assertThat(attachement.getSector()).isEqualTo(expectedSector);
		assertThat(attachement.getPercentage()).isEqualTo(expectedPercentage);
	}
	
	@Test
	public void testGetAttachmentBottom() {
		
		// given
		ILocation boundaryCenter = location(80, 120);
		IRectangle attachedToBounds = RECT;
		
		Sector expectedSector = Sector.BOTTOM;
		double expectedPercentage = 40.0 / 100;
		
		// when
		BoundaryAttachment attachement = BoundaryEventUtil.getAttachment(boundaryCenter, attachedToBounds);
		
		// then
		assertThat(attachement.getSector()).isEqualTo(expectedSector);
		assertThat(attachement.getPercentage()).isEqualTo(expectedPercentage);
	}

	@Test
	public void testGetAttachmentLeft() {
		
		// given
		ILocation boundaryCenter = location(40, 100);
		IRectangle attachedToBounds = RECT;
		
		Sector expectedSector = Sector.LEFT;
		double expectedPercentage = 60.0 / 80;
		
		// when
		BoundaryAttachment attachement = BoundaryEventUtil.getAttachment(boundaryCenter, attachedToBounds);
		
		// then
		assertThat(attachement.getSector()).isEqualTo(expectedSector);
		assertThat(attachement.getPercentage()).isEqualTo(expectedPercentage);
	}

	@Test
	public void testGetAttachmentRight() {
		
		// given
		ILocation boundaryCenter = location(140, 70);
		IRectangle attachedToBounds = RECT;
		
		Sector expectedSector = Sector.RIGHT;
		double expectedPercentage = 30.0 / 80;
		
		// when
		BoundaryAttachment attachement = BoundaryEventUtil.getAttachment(boundaryCenter, attachedToBounds);
		
		// then
		assertThat(attachement.getSector()).isEqualTo(expectedSector);
		assertThat(attachement.getPercentage()).isEqualTo(expectedPercentage);
	}
	
	// GET POSITION TESTS //////////////////////////////////////////
	
	@Test
	public void testGetPositionLeft() {
		
		// given
		BoundaryAttachment attachment = new BoundaryAttachment(Sector.LEFT, 0.5);
		IRectangle attachedToBounds = RECT;
		
		Point expectedPosition = point(40, 80);
		
		// when 
		Point position = BoundaryEventUtil.getPosition(attachment, attachedToBounds);
		
		// then
		assertThat(position).isEqualTo(expectedPosition);
	}	

	@Test
	public void testGetPositionRight() {
		
		// given
		BoundaryAttachment attachment = new BoundaryAttachment(Sector.RIGHT, 0.25);
		IRectangle attachedToBounds = RECT;
		
		Point expectedPosition = point(140, 60);
		
		// when 
		Point position = BoundaryEventUtil.getPosition(attachment, attachedToBounds);
		
		// then
		assertThat(position).isEqualTo(expectedPosition);
	}
	

	@Test
	public void testGetPositionTop() {
		
		// given
		BoundaryAttachment attachment = new BoundaryAttachment(Sector.TOP, 0.1);
		IRectangle attachedToBounds = RECT;
		
		Point expectedPosition = point(50, 40);
		
		// when 
		Point position = BoundaryEventUtil.getPosition(attachment, attachedToBounds);
		
		// then
		assertThat(position).isEqualTo(expectedPosition);
	}

	@Test
	public void testGetPositionBottom() {
		
		// given
		BoundaryAttachment attachment = new BoundaryAttachment(Sector.BOTTOM, 0.7);
		IRectangle attachedToBounds = RECT;
		
		Point expectedPosition = point(110, 120);
		
		// when 
		Point position = BoundaryEventUtil.getPosition(attachment, attachedToBounds);
		
		// then
		assertThat(position).isEqualTo(expectedPosition);
	}

	@Test
	public void testGetPositionTopLeft() {
		
		// given
		BoundaryAttachment attachment = new BoundaryAttachment(Sector.TOP_LEFT, -1d);
		IRectangle attachedToBounds = RECT;
		
		Point expectedPosition = point(40, 40);
		
		// when 
		Point position = BoundaryEventUtil.getPosition(attachment, attachedToBounds);
		
		// then
		assertThat(position).isEqualTo(expectedPosition);
	}
	
	@Test
	public void testGetPositionBottomRight() {
		
		// given
		BoundaryAttachment attachment = new BoundaryAttachment(Sector.BOTTOM_RIGHT, -1d);
		IRectangle attachedToBounds = RECT;
		
		Point expectedPosition = point(140, 120);
		
		// when 
		Point position = BoundaryEventUtil.getPosition(attachment, attachedToBounds);
		
		// then
		assertThat(position).isEqualTo(expectedPosition);
	}
}
