/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.features.event;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;

public class BoundaryAttachment {

	private Sector sector;
	private double percentage = -1d;
	
	public BoundaryAttachment(Sector sector, double percentage) {
		this.sector = sector;
		this.percentage = percentage;
	}

	public Sector getSector() {
		return sector;
	}
	
	public double getPercentage() {
		return percentage;
	}
	
	public boolean isLegal() {
		return sector != Sector.UNDEFINED;
	}

	public static BoundaryAttachment fromString(String s) {
		if (s == null) {
			return null;
		}
		String[] split = s.split(":");
		if (!split[0].equals(BoundaryAttachment.class.getSimpleName().toLowerCase())) {
			return null;
		}

		return new BoundaryAttachment(Sector.valueOf(split[1]), Double.valueOf(split[2]));
	}

	@Override
	public String toString() {
		String prefix = BoundaryAttachment.class.getSimpleName().toLowerCase();
		
		return String.format("%s:%s:%s", prefix, this.sector, this.percentage);
	}

	/**
	 * Creates a new boundary event from the parent bounds and the boundary center.
	 * 
	 * Coordinates may be absolute or relative / must not be mixed though. 
	 * 
	 * A tolerance may be given to match boundary events which are not exactly on any of the lines.
	 * 
	 * @param attachedToBounds
	 * @param boundaryCenter
	 * @param tolerance in pixels
	 * 
	 * @return the boundary attachment
	 */
	public static BoundaryAttachment fromAttachCoordinates(IRectangle attachedToBounds, ILocation boundaryCenter, int tolerance) {
		
		int x = boundaryCenter.getX();
		int y = boundaryCenter.getY();
		
		int bwidth = attachedToBounds.getWidth();
		int bheight = attachedToBounds.getHeight();
		
		int bx1 = attachedToBounds.getX();
		int bx2 = bx1 + bwidth;
		
		int by1 = attachedToBounds.getY();
		int by2 = by1 + bheight;
		
		Sector sector = Sector.fromBooleans(
				Math.abs(x - bx1) < tolerance, 
				Math.abs(x - bx2) < tolerance, 
				Math.abs(y - by1) < tolerance,
				Math.abs(y - by2) < tolerance);
		
		double percentage = -1d;
		
		switch (sector) {
		case LEFT:
		case RIGHT:
			percentage = 1.0 * (y - by1) / bheight;
			break;
		case TOP:
		case BOTTOM:
			percentage = 1.0 * (x - bx1) / bwidth;
			break;
		}
		
		return new BoundaryAttachment(sector, percentage);
	}
}