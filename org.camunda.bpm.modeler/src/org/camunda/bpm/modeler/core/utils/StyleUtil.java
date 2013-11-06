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
 * @author Ivar Meikas
 ******************************************************************************/
package org.camunda.bpm.modeler.core.utils;

import org.camunda.bpm.modeler.core.preferences.Bpmn2Preferences;
import org.camunda.bpm.modeler.core.preferences.ShapeStyle;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.StyleContainer;
import org.eclipse.graphiti.mm.algorithms.AbstractText;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.styles.AdaptedGradientColoredAreas;
import org.eclipse.graphiti.mm.algorithms.styles.Font;
import org.eclipse.graphiti.mm.algorithms.styles.GradientColoredArea;
import org.eclipse.graphiti.mm.algorithms.styles.GradientColoredAreas;
import org.eclipse.graphiti.mm.algorithms.styles.LocationType;
import org.eclipse.graphiti.mm.algorithms.styles.Style;
import org.eclipse.graphiti.mm.algorithms.styles.StylesFactory;
import org.eclipse.graphiti.mm.algorithms.styles.StylesPackage;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;
import org.eclipse.graphiti.util.IGradientType;
import org.eclipse.graphiti.util.IPredefinedRenderingStyle;

public class StyleUtil {
	
	private static final String CLASS_ID = "E-CLASS";
	private static final String FILL_STYLE = "fill.style";
	
	public enum FillStyle {
		FILL_STYLE_NONE,
		FILL_STYLE_FOREGROUND,
		FILL_STYLE_BACKGROUND,
		FILL_STYLE_DEFAULT,
		FILL_STYLE_INVERT };
	
	public static final IColorConstant CLASS_FOREGROUND = new ColorConstant(0, 0, 0);
	public static final IColorConstant CLASS_BACKGROUND = new ColorConstant(255, 255, 255);
	
	public static Style getStyleForClass(Diagram diagram) {
		Style s = findStyle(diagram, CLASS_ID);
		
		if(s == null) {
			IGaService gaService = Graphiti.getGaService();
			s = gaService.createStyle(diagram, CLASS_ID);
			s.setForeground(gaService.manageColor(diagram, CLASS_FOREGROUND));
			s.setBackground(gaService.manageColor(diagram, CLASS_BACKGROUND));
			s.setLineWidth(1);
		}
		
		return s;
	}

	private static Style findStyle(StyleContainer container, String id) {
		if (container.getStyles() != null) {
			for (Style s : container.getStyles()) {
				if (s.getId().equals(id)) {
					return s;
				}
			}
		}
		return null;
	}

	public static Diagram findDiagram(GraphicsAlgorithm shape) {
		EObject container = shape.eContainer();
		while (container!=null && !(container instanceof Diagram)) {
			container = container.eContainer();
		}
		return (Diagram)container;
	}
	
	public static void setFillStyle(GraphicsAlgorithm ga, FillStyle fillStyle) {
		Graphiti.getPeService().setPropertyValue(ga, FILL_STYLE, fillStyle.toString());
	}
	
	public static void applyStyle(GraphicsAlgorithm ga, String styleId, ShapeStyle shapeStyle, boolean isConnection) {

		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();

		Diagram diagram = findDiagram(ga);
		
		IColorConstant foreground = ga instanceof AbstractText ? shapeStyle.getTextColor() : shapeStyle.getShapeForeground();
		IColorConstant background = shapeStyle.getShapeBackground();

		if (peService.getPropertyValue(ga, Bpmn2Preferences.PREF_SHAPE_STYLE) == null) {
			peService.setPropertyValue(ga, Bpmn2Preferences.PREF_SHAPE_STYLE, Boolean.toString(true));
		}

		if (isConnection) {
			if (ga instanceof AbstractText) {
				Font f = shapeStyle.getTextFont();
				((AbstractText)ga).setFont(gaService.manageFont(diagram, f.getName(), f.getSize(), f.isItalic(), f.isBold()));
			}
			if (ga.getForeground()==null)
				ga.setForeground(gaService.manageColor(diagram, foreground));
			// by default the fill color for connection decorators is the
			// connection line foreground color
			if (ga.getBackground()==null)
				ga.setBackground(gaService.manageColor(diagram, foreground));
		} else {
			// Style only used for drawing gradients
			Style s = findStyle(diagram, styleId);
			if (s == null) {
				s = gaService.createStyle(diagram, styleId);
			}
			
			if (ga instanceof AbstractText) {
				Font f = shapeStyle.getTextFont();
				((AbstractText)ga).setFont(gaService.manageFont(diagram, f.getName(), f.getSize(), f.isItalic(), f.isBold()));
				ga.setForeground(gaService.manageColor(diagram, foreground));
				// Text does not have a fill style (yet)
				return;
			}
			else {
				s.setForeground(gaService.manageColor(diagram, foreground));
				ga.setForeground(gaService.manageColor(diagram, foreground));
			}
			
			String fillStyle = peService.getPropertyValue(ga, FILL_STYLE);
			if (fillStyle==null || fillStyle.equals(FillStyle.FILL_STYLE_DEFAULT.name())) {
				// fill with gradient
				ga.setFilled(true);
				s.setFilled(true);
				AdaptedGradientColoredAreas gradient = getStyleAdaptions(styleId, shapeStyle);
				gaService.setRenderingStyle(s, gradient);
				ga.setStyle(s);
			} else if (fillStyle.equals(FillStyle.FILL_STYLE_FOREGROUND.name())) {
				ga.setFilled(true);
				ga.setBackground(gaService.manageColor(diagram, foreground));
			} else if (fillStyle.equals(FillStyle.FILL_STYLE_BACKGROUND.name())) {
				ga.setFilled(true);
				ga.setBackground(gaService.manageColor(diagram, background));
			} else if (fillStyle.equals(FillStyle.FILL_STYLE_INVERT.name())) {
				ga.setFilled(true);
				ga.setForeground(gaService.manageColor(diagram, background));
				ga.setBackground(gaService.manageColor(diagram, foreground));
			} else {
				ga.setFilled(false);
				ga.setBackground(gaService.manageColor(diagram, background));
			}
		}
	}
	
	public static void applyStyle(GraphicsAlgorithm ga, BaseElement be) {
	
		Assert.isNotNull(ga);
		Assert.isNotNull(be);
		
		Bpmn2Preferences pref = Bpmn2Preferences.getInstance(be);
		String styleId = Bpmn2Preferences.getShapeStyleId(be);
		boolean isConnection = BusinessObjectUtil.isConnection(be.eClass().getInstanceClass());
		
		ShapeStyle shapeStyle = pref.getShapeStyle(be);
		
		applyStyle(ga, styleId, shapeStyle, isConnection);
	}

	public static AdaptedGradientColoredAreas getStyleAdaptions(BaseElement be) {

		Bpmn2Preferences pref = Bpmn2Preferences.getInstance(be);
		
		ShapeStyle shapeStyle = pref.getShapeStyle(be);
		String styleId = Bpmn2Preferences.getShapeStyleId(be);

		return getStyleAdaptions(styleId, shapeStyle);
	}

	public static AdaptedGradientColoredAreas getStyleAdaptions(String styleId, ShapeStyle shapeStyle) {
		final AdaptedGradientColoredAreas agca = StylesFactory.eINSTANCE.createAdaptedGradientColoredAreas();
		
		agca.setDefinedStyleId(styleId);
		agca.setGradientType(IGradientType.VERTICAL);
		
		agca.getAdaptedGradientColoredAreas().add(IPredefinedRenderingStyle.STYLE_ADAPTATION_DEFAULT,
				getPreferenceDefaultAreas(shapeStyle));
		agca.getAdaptedGradientColoredAreas().add(IPredefinedRenderingStyle.STYLE_ADAPTATION_PRIMARY_SELECTED,
				getPreferencePrimarySelectedAreas(shapeStyle));
		agca.getAdaptedGradientColoredAreas().add(IPredefinedRenderingStyle.STYLE_ADAPTATION_SECONDARY_SELECTED,
				getPreferenceSecondarySelectedAreas(shapeStyle));
		
		return agca;
	}
	
	
	public static IColorConstant shiftColor(IColorConstant c, int amount) {
		int r = c.getRed() + amount;
		int g = c.getGreen() + amount;
		int b = c.getBlue() + amount;
		if (r>255) r = 255;
		if (r<0) r = 0;
		if (g>255) g = 255;
		if (g<0) g = 0;
		if (b>255) b = 255;
		if (b<0) b = 0;
		
		return new ColorConstant(r, g, b);
	}
	
	private static GradientColoredAreas getPreferenceDefaultAreas(ShapeStyle shapeStyle) {
		final GradientColoredAreas gradientColoredAreas = StylesFactory.eINSTANCE.createGradientColoredAreas();
		gradientColoredAreas.setStyleAdaption(IPredefinedRenderingStyle.STYLE_ADAPTATION_DEFAULT);
		final EList<GradientColoredArea> gcas = gradientColoredAreas.getGradientColor();

		IColorConstant c1 = shiftColor(shapeStyle.getShapeBackground(), -8);
		IColorConstant c2 = shiftColor(shapeStyle.getShapeBackground(), 64);

		addGradientColoredArea(gcas,
				c1, 0, LocationType.LOCATION_TYPE_ABSOLUTE_START,
				c2, 0, LocationType.LOCATION_TYPE_ABSOLUTE_END);
		return gradientColoredAreas;
	}
	private static GradientColoredAreas getPreferencePrimarySelectedAreas(ShapeStyle shapeStyle) {
		final GradientColoredAreas gradientColoredAreas = StylesFactory.eINSTANCE.createGradientColoredAreas();
		gradientColoredAreas.setStyleAdaption(IPredefinedRenderingStyle.STYLE_ADAPTATION_PRIMARY_SELECTED);
		final EList<GradientColoredArea> gcas = gradientColoredAreas.getGradientColor();

		IColorConstant c1 = shiftColor(shapeStyle.getShapePrimarySelectedColor(), -64);
		IColorConstant c2 = shiftColor(shapeStyle.getShapePrimarySelectedColor(), 64);

		addGradientColoredArea(gcas,
				c1, 0, LocationType.LOCATION_TYPE_ABSOLUTE_START,
				c2, 0, LocationType.LOCATION_TYPE_ABSOLUTE_END);
		return gradientColoredAreas;
	}

	private static GradientColoredAreas getPreferenceSecondarySelectedAreas(ShapeStyle shapeStyle) {
		final GradientColoredAreas gradientColoredAreas = StylesFactory.eINSTANCE.createGradientColoredAreas();
		gradientColoredAreas.setStyleAdaption(IPredefinedRenderingStyle.STYLE_ADAPTATION_SECONDARY_SELECTED);
		final EList<GradientColoredArea> gcas = gradientColoredAreas.getGradientColor();

		IColorConstant c1 = shiftColor(shapeStyle.getShapeSecondarySelectedColor(), -64);
		IColorConstant c2 = shiftColor(shapeStyle.getShapeSecondarySelectedColor(), 64);

		addGradientColoredArea(gcas,
				c1, 0, LocationType.LOCATION_TYPE_ABSOLUTE_START,
				c2, 0, LocationType.LOCATION_TYPE_ABSOLUTE_END);
		return gradientColoredAreas;
	}

	private static void addGradientColoredArea(EList<GradientColoredArea> gcas,
			IColorConstant colorStart, int locationValueStart, LocationType locationTypeStart,
			IColorConstant colorEnd, int locationValueEnd, LocationType locationTypeEnd) {
		final GradientColoredArea gca = StylesFactory.eINSTANCE.createGradientColoredArea();
		gcas.add(gca);
		gca.setStart(StylesFactory.eINSTANCE.createGradientColoredLocation());
		gca.getStart().setColor(StylesFactory.eINSTANCE.createColor());
		gca.getStart().getColor().eSet(StylesPackage.eINSTANCE.getColor_Blue(), colorStart.getBlue());
		gca.getStart().getColor().eSet(StylesPackage.eINSTANCE.getColor_Green(), colorStart.getGreen());
		gca.getStart().getColor().eSet(StylesPackage.eINSTANCE.getColor_Red(), colorStart.getRed());
		gca.getStart().setLocationType(locationTypeStart);
		gca.getStart().setLocationValue(locationValueStart);
		gca.setEnd(StylesFactory.eINSTANCE.createGradientColoredLocation());
		gca.getEnd().setColor(StylesFactory.eINSTANCE.createColor());
		gca.getEnd().getColor().eSet(StylesPackage.eINSTANCE.getColor_Blue(), colorEnd.getBlue());
		gca.getEnd().getColor().eSet(StylesPackage.eINSTANCE.getColor_Green(), colorEnd.getGreen());
		gca.getEnd().getColor().eSet(StylesPackage.eINSTANCE.getColor_Red(), colorEnd.getRed());
		gca.getEnd().setLocationType(locationTypeEnd);
		gca.getEnd().setLocationValue(locationValueEnd);
	}
}
