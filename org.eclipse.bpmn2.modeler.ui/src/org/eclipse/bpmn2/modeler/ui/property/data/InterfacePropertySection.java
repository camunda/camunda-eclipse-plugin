package org.eclipse.bpmn2.modeler.ui.property.data;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CallableElement;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Interface;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerFactory;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.AbstractListComposite;
import org.eclipse.bpmn2.modeler.ui.property.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.DefaultListComposite;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertySection;
import org.eclipse.bpmn2.modeler.ui.property.PropertiesCompositeFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ListDialog;

public class InterfacePropertySection extends DefaultPropertySection {

	static {
		PropertiesCompositeFactory.register(Interface.class, InterfaceDetailComposite.class);
	}

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new InterfaceSectionRoot(this);
	}

	public InterfacePropertySection() {
		super();
	}

	@Override
	protected EObject getBusinessObjectForPictogramElement(PictogramElement pe) {
		EObject bo = super.getBusinessObjectForPictogramElement(pe);
		if (bo instanceof Participant) {
			return bo;
		} else if (bo instanceof BPMNDiagram) {
			BaseElement be = ((BPMNDiagram)bo).getPlane().getBpmnElement();
			if (be instanceof Process)
				return be;
		} else if (bo instanceof CallableElement) {
			return bo;
		}
		
		return null;
	}
	
	public class InterfaceSectionRoot extends DefaultDetailComposite {

		DefinedInterfaceListComposite definedInterfacesTable;
		ProvidedInterfaceListComposite providedInterfacesTable;
		
		/**
		 * @param parent
		 * @param style
		 */
		public InterfaceSectionRoot(Composite parent, int style) {
			super(parent, style);
		}

		/**
		 * @param section
		 */
		public InterfaceSectionRoot(AbstractBpmn2PropertySection section) {
			super(section);
		}

		@Override
		public void createBindings(EObject be) {
			definedInterfacesTable = new DefinedInterfaceListComposite(this);
			definedInterfacesTable.bindList(be);

			if (be instanceof Participant) {
				providedInterfacesTable = new ProvidedInterfaceListComposite(this);
				providedInterfacesTable.bindList(be, getFeature(be, "interfaceRefs"));
			}
			else if (be instanceof CallableElement) {
				CallableElement ce = (CallableElement)be;
				providedInterfacesTable = new ProvidedInterfaceListComposite(this);
				providedInterfacesTable.bindList(be, getFeature(be, "supportedInterfaceRefs"));
			}
		}
	}
	
	public class DefinedInterfaceListComposite extends DefaultListComposite {
		
		/**
		 * @param section
		 * @param style
		 */
		public DefinedInterfaceListComposite(Composite parent) {
			super(parent,
					AbstractListComposite.SHOW_DETAILS |
					AbstractListComposite.ADD_BUTTON |
					AbstractListComposite.MOVE_BUTTONS |
					AbstractListComposite.DELETE_BUTTON);
		}

		@Override
		public EClass getListItemClass(EObject object, EStructuralFeature feature) {
			return listItemClass = Bpmn2Package.eINSTANCE.getInterface();
		}

		public void bindList(EObject theobject) {
			Definitions defs = ModelUtil.getDefinitions(theobject);
			super.bindList(defs, Bpmn2Package.eINSTANCE.getDefinitions_RootElements());
			setTitle("All Defined Interfaces");
		}

		@Override
		protected EObject addListItem(EObject object, EStructuralFeature feature) {
			Interface iface = Bpmn2ModelerFactory.create(Interface.class);
			Definitions defs = (Definitions)object;
			defs.getRootElements().add(iface);
			ModelUtil.setID(iface);
			iface.setName("New "+iface.getId());
			
			EList<EObject> list = (EList<EObject>)object.eGet(feature);
			list.add(iface);
			return iface;
		}
	}
	
	public class ProvidedInterfaceListComposite extends DefaultListComposite {
		
		/**
		 * @param section
		 * @param style
		 */
		public ProvidedInterfaceListComposite(Composite parent) {
			super(parent,
					// only allow details editing in DefinedInterfacesTable
//					AbstractListComposite.SHOW_DETAILS |
					AbstractListComposite.ADD_BUTTON |
					AbstractListComposite.MOVE_BUTTONS |
					AbstractListComposite.REMOVE_BUTTON);
		}
		
		public void bindList(final EObject theobject, final EStructuralFeature thefeature) {
			super.bindList(theobject, thefeature);
			if (theobject instanceof Participant)
				setTitle("Interfaces Provided by Participant");
			else if (theobject instanceof CallableElement)
				setTitle("Interfaces Provided by Process");
		}
		
		@Override
		protected EObject addListItem(EObject object, EStructuralFeature feature) {
			Definitions defs = ModelUtil.getDefinitions(object);
			final List<Interface>items = new ArrayList<Interface>();
			for (EObject o : defs.getRootElements()) {
				if (o instanceof Interface) {
					if (object instanceof Participant) {
						Participant participant = (Participant)object;
						if (!participant.getInterfaceRefs().contains(o))
							items.add((Interface)o);
					} else if (object instanceof CallableElement) {
						CallableElement callableElement = (CallableElement)object;
						if (!callableElement.getSupportedInterfaceRefs().contains(o))
							items.add((Interface)o);
					}
				}
			}
			Interface iface = null;
			ListDialog dialog = new ListDialog(getShell());
			if (items.size()>1) {
				dialog.setContentProvider(new IStructuredContentProvider() {
		
					@Override
					public void dispose() {
					}
		
					@Override
					public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
					}
		
					@Override
					public Object[] getElements(Object inputElement) {
						return items.toArray();
					}
					
				});
				dialog.setLabelProvider(new ILabelProvider() {
		
					@Override
					public void addListener(ILabelProviderListener listener) {
					}
		
					@Override
					public void dispose() {
					}
		
					@Override
					public boolean isLabelProperty(Object element, String property) {
						return false;
					}
		
					@Override
					public void removeListener(ILabelProviderListener listener) {
					}
		
					@Override
					public Image getImage(Object element) {
						return null;
					}
		
					@Override
					public String getText(Object element) {
						return ModelUtil.getName((BaseElement)element);
					}
					
				});
				dialog.setAddCancelButton(true);
				dialog.setHelpAvailable(false);
				dialog.setInput(new Object());

				if (dialog.open() == Window.OK) {
					iface = (Interface)dialog.getResult()[0];
				}
			}
			else if (items.size()==1) {
				iface = items.get(0);
			}
			else {
				MessageDialog.openInformation(getShell(), "No Defined Interfaces",
						"There are no new Interfaces to add.\n"+
						"Please create a new Interface in the \"Defined Interfaces\" first."
				);
			}
			
			if (iface!=null) {
				if (object instanceof Participant) {
					Participant participant = (Participant)object;
					participant.getInterfaceRefs().add(iface);
				} else if (object instanceof CallableElement) {
					CallableElement callableElement = (CallableElement)object;
					callableElement.getSupportedInterfaceRefs().add(iface);
				}
			}

			return iface;
		}
	}
	
}
