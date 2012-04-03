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
package org.eclipse.bpmn2.modeler.core.utils;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.preferences.ShapeStyle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.StyleContainer;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
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
	private static final String TEXT_ID = "E-CLASS-TEXT";
	private static final String STYLE_ID = "style-id";
	
	public static final IColorConstant CLASS_TEXT_FOREGROUND = IColorConstant.BLACK;
	//public static final IColorConstant CLASS_FOREGROUND = new ColorConstant(146, 146, 208);
	//public static final IColorConstant CLASS_FOREGROUND = new ColorConstant(102, 175, 232);
	public static final IColorConstant CLASS_FOREGROUND = new ColorConstant(116, 143, 165);
	
	//public static final IColorConstant CLASS_BACKGROUND = new ColorConstant(220, 220, 255);
	public static final IColorConstant CLASS_BACKGROUND = new ColorConstant(220, 233, 255);
	
	public static Style getStyleForClass(Diagram diagram) {
		Style s = findStyle(diagram, CLASS_ID);
		
		if(s == null) {
			IGaService gaService = Graphiti.getGaService();
			s = gaService.createStyle(diagram, CLASS_ID);
			s.setForeground(gaService.manageColor(diagram, CLASS_FOREGROUND)); // border color for shapes and connections
			s.setBackground(gaService.manageColor(diagram, CLASS_BACKGROUND)); // ??
			s.setLineWidth(1);
		}
		
		return s;
	}

	public static Style getStyleForText(Diagram diagram) {
		Style parentStyle = getStyleForClass(diagram);
		Style s = findStyle(parentStyle, TEXT_ID);
		
		if(s == null) {
			IGaService gaService = Graphiti.getGaService();
			s = gaService.createStyle(diagram, TEXT_ID);
			s.setForeground(gaService.manageColor(diagram, CLASS_TEXT_FOREGROUND));
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

	private static Diagram findDiagram(GraphicsAlgorithm shape) {
		EObject container = shape.eContainer();
		while (container!=null && !(container instanceof Diagram)) {
			container = container.eContainer();
		}
		return (Diagram)container;
	}
	
	public static void applyStyle(GraphicsAlgorithm ga, BaseElement be) {
		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();

		Bpmn2Preferences pref = Bpmn2Preferences.getInstance(be.eResource());
		ShapeStyle ss = pref.getShapeStyle(be);
		IColorConstant c1 = shiftColor(ss.getShapeDefaultColor(), -64);

		Diagram diagram = findDiagram(ga);
		if (peService.getPropertyValue(ga, Bpmn2Preferences.PREF_SHAPE_STYLE)==null) {
			peService.setPropertyValue(ga, Bpmn2Preferences.PREF_SHAPE_STYLE, Boolean.toString(true));
		}

		if (ga instanceof Text) {
			Font f = ss.getTextFont();
			((Text) ga).setFont(gaService.manageFont(diagram, f.getName(), f.getSize(), f.isItalic(), f.isBold()));
			((Text) ga).setForeground(gaService.manageColor(diagram, ss.getTextColor()));
		}
		else {
			String id = Bpmn2Preferences.getShapeStyleId(be);
			Style s = findStyle(diagram, id);
			if(s == null) {
				s = gaService.createStyle(diagram, id);
			}
			s.setForeground(gaService.manageColor(diagram, c1));
			s.setBackground(gaService.manageColor(diagram, c1));
			
			ga.setStyle(s);
			ga.setFilled(true);
			AdaptedGradientColoredAreas gradient = getStyleAdaptions(be);
			gaService.setRenderingStyle(ga, gradient);
		}
	}

	public static AdaptedGradientColoredAreas getStyleAdaptions(BaseElement be) {
		final AdaptedGradientColoredAreas agca = StylesFactory.eINSTANCE.createAdaptedGradientColoredAreas();

		agca.setDefinedStyleId(STYLE_ID);
		agca.setGradientType(IGradientType.VERTICAL);
		agca.getAdaptedGradientColoredAreas().add(IPredefinedRenderingStyle.STYLE_ADAPTATION_DEFAULT,
				getPreferenceDefaultAreas(be));
		agca.getAdaptedGradientColoredAreas().add(IPredefinedRenderingStyle.STYLE_ADAPTATION_PRIMARY_SELECTED,
				getPreferencePrimarySelectedAreas(be));
		agca.getAdaptedGradientColoredAreas().add(IPredefinedRenderingStyle.STYLE_ADAPTATION_SECONDARY_SELECTED,
				getPreferenceSecondarySelectedAreas(be));
		return agca;
	}
	
	private static IColorConstant shiftColor(IColorConstant c, int amount) {
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
	
	private static GradientColoredAreas getPreferenceDefaultAreas(BaseElement be) {
		final GradientColoredAreas gradientColoredAreas = StylesFactory.eINSTANCE.createGradientColoredAreas();
		gradientColoredAreas.setStyleAdaption(IPredefinedRenderingStyle.STYLE_ADAPTATION_DEFAULT);
		final EList<GradientColoredArea> gcas = gradientColoredAreas.getGradientColor();

		Bpmn2Preferences pref = Bpmn2Preferences.getInstance(be.eResource());
		ShapeStyle ss = pref.getShapeStyle(be);
		IColorConstant c1 = shiftColor(ss.getShapeDefaultColor(), -8);
		IColorConstant c2 = shiftColor(ss.getShapeDefaultColor(), 64);

		addGradientColoredArea(gcas,
				c1, 0, LocationType.LOCATION_TYPE_ABSOLUTE_START,
				c2, 0, LocationType.LOCATION_TYPE_ABSOLUTE_END);
		return gradientColoredAreas;
	}
	private static GradientColoredAreas getPreferencePrimarySelectedAreas(BaseElement be) {
		final GradientColoredAreas gradientColoredAreas = StylesFactory.eINSTANCE.createGradientColoredAreas();
		gradientColoredAreas.setStyleAdaption(IPredefinedRenderingStyle.STYLE_ADAPTATION_PRIMARY_SELECTED);
		final EList<GradientColoredArea> gcas = gradientColoredAreas.getGradientColor();

		Bpmn2Preferences pref = Bpmn2Preferences.getInstance(be.eResource());
		ShapeStyle ss = pref.getShapeStyle(be);
		IColorConstant c1 = shiftColor(ss.getShapePrimarySelectedColor(), -64);
		IColorConstant c2 = shiftColor(ss.getShapePrimarySelectedColor(), 64);

		addGradientColoredArea(gcas,
				c1, 0, LocationType.LOCATION_TYPE_ABSOLUTE_START,
				c2, 0, LocationType.LOCATION_TYPE_ABSOLUTE_END);
		return gradientColoredAreas;
	}

	private static GradientColoredAreas getPreferenceSecondarySelectedAreas(BaseElement be) {
		final GradientColoredAreas gradientColoredAreas = StylesFactory.eINSTANCE.createGradientColoredAreas();
		gradientColoredAreas.setStyleAdaption(IPredefinedRenderingStyle.STYLE_ADAPTATION_SECONDARY_SELECTED);
		final EList<GradientColoredArea> gcas = gradientColoredAreas.getGradientColor();

		Bpmn2Preferences pref = Bpmn2Preferences.getInstance(be.eResource());
		ShapeStyle ss = pref.getShapeStyle(be);
		IColorConstant c1 = shiftColor(ss.getShapeSecondarySelectedColor(), -64);
		IColorConstant c2 = shiftColor(ss.getShapeSecondarySelectedColor(), 64);

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
