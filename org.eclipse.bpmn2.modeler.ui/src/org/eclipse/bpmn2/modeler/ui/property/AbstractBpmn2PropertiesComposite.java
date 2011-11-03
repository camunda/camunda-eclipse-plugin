/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 * IBM Corporation - http://dev.eclipse.org/viewcvs/viewvc.cgi/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet19.java
 *
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.property;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.GatewayDirection;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.preferences.ToolEnablementPreferences;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.PropertyUtil;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.adapters.AdapterUtil;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.property.editors.ComboObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.IntObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.ListObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.ObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.BooleanObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.TextObjectEditor;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.ui.celleditor.FeatureEditorDialog;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;


/**
 * This is a base class for all Property Sheet Sections. The Composite is used to render
 * the "structural features" for an EObject, presumably a subclass of BaseElement or some
 * other BPMN2 metamodel object.
 * 
 * The Composite uses a GridLayout with 3 columns: the leftmost column is designated for a
 * label; the two rightmost columns are designated for input or editing widgets, depending
 * on the type of structural feature being rendered.
 * 
 * EAttribute and EReference type of structural features are collected and rendered within
 * a non-collapsible FormSection given the title "Attributes". List features are rendered in
 * collapsible AbstractBpmn2TableComposite table widgets. 
 * 
 * Subclasses must implement the abstract createBindings() method to construct their editing
 * widgets. These widgets are torn down and reconstructed when the editor selection changes.
 */
public abstract class AbstractBpmn2PropertiesComposite extends Composite implements ResourceSetListener {

	protected AbstractBpmn2PropertySection propertySection;
	protected EObject be;
	protected FormToolkit toolkit;
	protected ToolEnablementPreferences preferences;
	protected ModelHandler modelHandler;
	protected TransactionalEditingDomain domain;

	protected Section attributesSection = null;
	protected Composite attributesComposite = null;
	protected Font descriptionFont = null;
	
	protected ChildObjectStack objectStack = new ChildObjectStack();

	/**
	 * Constructor for embedding this composite in an AbstractBpmn2PropertySection.
	 * This is the "normal" method of creating this composite.
	 * 
	 * @param section
	 */
	public AbstractBpmn2PropertiesComposite(AbstractBpmn2PropertySection section) {
		super(section.getParent(), SWT.NONE);
		propertySection = section;
		toolkit = propertySection.getWidgetFactory();
		initialize();
	}
	
	/**
	 * Constructor for embedding this composite in a nested property section,
	 * e.g. the AdvancedPropertySection uses this.
	 * 
	 * @param parent
	 * @param style
	 */
	public AbstractBpmn2PropertiesComposite(Composite parent, int style) {
		super(parent,style);
		toolkit = new FormToolkit(Display.getCurrent());
		initialize();
	}

	private void initialize() {
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new GridLayout(3, false));
		addDomainListener();
	}
	
	private void addDomainListener() {
		if (domain==null) {
			domain = BPMN2Editor.getActiveEditor().getEditingDomain();
			domain.addResourceSetListener(this);
		}
	}

	private void removeDomainListener() {
		if (domain!=null) {
			domain.removeResourceSetListener(this);
		}
	}
	
	@Override
	public void dispose() {
		removeDomainListener();
		PropertyUtil.disposeChildWidgets(AbstractBpmn2PropertiesComposite.this);
		super.dispose();
	}

	public void setPropertySection(AbstractBpmn2PropertySection section) {
		propertySection = section;
	}
	
	/**
	 * This method is called by the when the property sheet tab to update the UI
	 * after a new selection is made. Updating consists of a full teardown of the
	 * widget tree and then rebuilding it for the newly selected EObject. Since the
	 * same composite MAY be used for different EObject types, the widgets may be
	 * completely different, hence the need for teardown and setup for each new selection.
	 * 
	 * @param bpmn2Editor
	 * @param object
	 */
	public void setEObject(BPMN2Editor bpmn2Editor, final EObject object) {
		IProject project = bpmn2Editor.getModelFile().getProject();
		preferences = ToolEnablementPreferences.getPreferences(project);
		try {
			modelHandler = ModelHandlerLocator.getModelHandler(bpmn2Editor.getDiagramTypeProvider().getDiagram()
					.eResource());
		} catch (IOException e1) {
			Activator.showErrorWithLogging(e1);
		}
		
		setEObject(object);
	}
	
	public final EObject getEObject() {
		return be;
	}

	protected final void setEObject(final EObject object) {
		cleanBindings();
		be = object;
		if (be != null) {
			createBindings(be);
			getParent().layout();
		}
	}
	
	/**
	 * Querries the owning AbstractBpmn2PropertySection for its owning BPMN2Editor.
	 * If this composite is not owned by a AbstractBpmn2PropertySection, then return
	 * the currently active editor.
	 * 
	 * @return the BPMN2Editor that owns this property section.
	 */
	public BPMN2Editor getDiagramEditor() {
		if (propertySection!=null)
			return (BPMN2Editor)propertySection.getDiagramEditor();
		return BPMN2Editor.getActiveEditor();
	}

	/**
	 * Tear down all child widgets
	 */
	protected void cleanBindings() {
		PropertyUtil.disposeChildWidgets(this);
	}

	public FormToolkit getToolkit() {
		return toolkit;
	}
	
	/**
	 * Returns the composite that is used to contain all EAttributes for the
	 * current selection. The default behavior is to construct a non-collapsible
	 * Form Section and create a Composite within that section.
	 * 
	 * @return the Composite root for the current selection's EAttributes
	 */
	protected Composite getAttributesParent() {
		if (attributesSection==null || attributesSection.isDisposed()) {

			if (objectStack.peek()==be)
				attributesSection = createSection(objectStack.getAttributesParent(), "Attributes");
			else
				attributesSection = createSubSection(objectStack.getAttributesParent(),
						ModelUtil.getObjectDisplayName(objectStack.peek()));
			
			attributesSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
			attributesComposite = toolkit.createComposite(attributesSection);
			attributesSection.setClient(attributesComposite);
			attributesComposite.setLayout(new GridLayout(3,false));
		}
		return attributesComposite;
	}

	/**
	 * This method is called when setEObject is called and this should recreate
	 *  all bindings and widgets for the current selection.
	 *  
	 * @param be the business object linked to the currently selected EditPart
	 * through the Graphiti DiagramEditor framework.
	 */
	public abstract void createBindings(EObject be);
	
	/**
	 * Convenience method to look up an EObject's feature by name.
	 * 
	 * @param object the EObject
	 * @param name the feature name string
	 * @return the EStructuralFeature or null if the feature does not exist for this object
	 */
	protected EStructuralFeature getFeature(EObject object, String name) {
		EStructuralFeature feature = ((EObject)object).eClass().getEStructuralFeature(name);
		if (feature==null) {
			// maybe it's an anyAttribute?
			List<EStructuralFeature> anyAttributes = ModelUtil.getAnyAttributes(object);
			for (EStructuralFeature f : anyAttributes) {
				if (f.getName().equals(name))
					return f;
			}
		}
		return feature;
	}

	/**
	 * Convenience method to check if a feature is an EAttribute
	 * @param feature
	 * @return
	 */
	protected boolean isAttribute(EObject object, EStructuralFeature feature) {
		// maybe it's an anyAttribute?
		if (feature instanceof EAttribute)
			return true;

		List<EStructuralFeature> anyAttributes = ModelUtil.getAnyAttributes(object);
		for (EStructuralFeature f : anyAttributes) {
			if (f.getName().equals(feature.getName()))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Convenience method to check if a feature is an EList
	 * @param object
	 * @param feature
	 * @return
	 */
	protected boolean isList(EObject object, EStructuralFeature feature) {
		if (feature!=null) {
			Object list = object.eGet(feature);
			return (list instanceof EObjectContainmentEList);
		}
		return false;
	}

	/**
	 * Convenience method to check if a feature is an EReference
	 * @param feature
	 * @return
	 */
	protected boolean isReference(EObject object, EStructuralFeature feature) {
		if (feature!=null) {
			Object list = object.eGet(feature);
			if (list instanceof EList && !(list instanceof EObjectContainmentEList))
				return true;
		}
		return (feature instanceof EReference);
	}

	protected Label createLabel(Composite parent, String name) {
		Label label = toolkit.createLabel(parent, name);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		return label;
	}

	public Font getDescriptionFont() {
		if (descriptionFont==null) {
			Display display = Display.getCurrent();
		    FontData data = display.getSystemFont().getFontData()[0];
		    descriptionFont = new Font(display, data.getName(), data.getHeight() + 1, SWT.NONE);
		}
		return descriptionFont;
	}

	protected StyledText createDescription(Composite parent, String description) {
		Display display = Display.getCurrent();
		final StyledText styledText = new StyledText(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.READ_ONLY);
		styledText.setText(description);

	    styledText.setFont(getDescriptionFont());
		
		styledText.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		styledText.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		
		GridData d = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1);
		d.horizontalIndent = 4;
		d.verticalIndent = 4;
		d.heightHint = 4 * getDescriptionFont().getFontData()[0].getHeight();
		d.widthHint = 100;
		styledText.setLayoutData(d);

		return styledText;
	}

	protected Section createSection(Composite parent, String title) {
		Section section = toolkit.createSection(parent,
				ExpandableComposite.TWISTIE |
				ExpandableComposite.EXPANDED |
				ExpandableComposite.TITLE_BAR);
		section.setText(title);
		return section;
	}

	protected Section createSubSection(Composite parent, String title) {
		Section section = toolkit.createSection(parent,
				ExpandableComposite.EXPANDED |
				ExpandableComposite.TITLE_BAR);
		section.setText(title);
		section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,true, 3,1));
		return section;
	}

	protected void bindAttribute(EObject object, String name) {
		EStructuralFeature feature = getFeature(object,name);
		if (isAttribute(object,feature)) {
			bindAttribute(object,(EAttribute)feature);
		}
	}

	protected void bindAttribute(EObject object, EAttribute attribute) {
		bindAttribute(null,object,attribute);
	}
	
	protected void bindAttribute(Composite parent, EObject object, EAttribute attribute) {

		if (preferences.isEnabled(object.eClass(), attribute)) {

			if (parent==null)
				parent = getAttributesParent();
			
			String displayName = getDisplayName(object, attribute);
			Collection choiceOfValues = getChoiceOfValues(object, attribute);
			
			Class eTypeClass = attribute.getEType().getInstanceClass();
			if (String.class.equals(eTypeClass)) {
				int style = SWT.NONE;
				if (getIsMultiLine(object,attribute))
					style |= SWT.MULTI;
				ObjectEditor editor = new TextObjectEditor(this,object,attribute);
				editor.createControl(parent,displayName,style);
			} else if (boolean.class.equals(eTypeClass)) {
				ObjectEditor editor = new BooleanObjectEditor(this,object,attribute);
				editor.createControl(parent,displayName);
			} else if (int.class.equals(eTypeClass) ||
					Integer.class.equals(eTypeClass) ||
					BigInteger.class.equals(eTypeClass) ) {
				ObjectEditor editor = new IntObjectEditor(this,object,attribute);
				editor.createControl(parent,displayName);
			} else if (choiceOfValues != null) {
				ObjectEditor editor = new ComboObjectEditor(this,object,attribute);
				editor.createControl(parent,displayName);
			} else if ("anyAttribute".equals(attribute.getName())) {
				List<Entry> basicList = ((BasicFeatureMap) object.eGet(attribute)).basicList();
				for (Entry entry : basicList) {
					EStructuralFeature feature = entry.getEStructuralFeature();
					if (isAttribute(object, feature))
						bindAttribute(parent,object,(EAttribute)feature);
					else if (isReference(object, feature))
						bindReference(parent,object,(EReference)feature);
					else if (isList(object,feature))
						bindList(object,feature);
				}
			}
		}
	}
	
	protected void bindReference(EObject object, String name) {
		EStructuralFeature feature = getFeature(object,name);
		if (isReference(object,feature)) {
			bindReference(object,(EReference)feature);
		}
	}
	
	protected void bindReference(EObject object, EReference reference) {
		bindReference(null, object, reference);
	}
	
	protected void bindReference(Composite parent, EObject object, EReference reference) {
		if (preferences.isEnabled(object.eClass(), reference)) {
			if (parent==null)
				parent = getAttributesParent();
			
			Object eGet = object.eGet(reference);
			String displayName = getDisplayName(object, reference);
	
			ObjectEditor editor;
			if (eGet instanceof List) {
				editor = new ListObjectEditor(this,object,reference);
			} else {
				editor = new ComboObjectEditor(this,object,reference);
			}
			editor.createControl(parent,displayName);
		}
	}
	
	protected void bindChild(final EObject object, String name) {
		final EStructuralFeature feature = ((EObject)object).eClass().getEStructuralFeature(name);
		if (feature instanceof EReference) {
			Object value = object.eGet(feature);
			if (value==null) {
				TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						Object newValue = Bpmn2Factory.eINSTANCE.create(((EReference) feature).getEReferenceType());
						object.eSet(feature, newValue);
						ModelUtil.setID((EObject)newValue);
					}
				});
				value = object.eGet(feature);
			}
			if (value instanceof EObject) {
				objectStack.push((EObject)value);
				createBindings((EObject)value);
				objectStack.pop();
			}
		}
	}

	protected void bindList(EObject object, String name) {
		EStructuralFeature feature = getFeature(object,name);
		if (isList(object,feature)) {
			bindList(object,feature);
		}
	}
	
	protected void bindList(EObject object, EStructuralFeature feature) {
		bindList(object,feature,null);
	}
	
	protected void bindList(EObject object, EStructuralFeature feature, EClass listItemClass) {

		if (preferences.isEnabled(object.eClass(), feature)) {

			AbstractBpmn2TableComposite tableComposite = null;
			if (propertySection!=null)
				tableComposite = new AbstractBpmn2TableComposite(propertySection, AbstractBpmn2TableComposite.DEFAULT_STYLE);
			else
				tableComposite = new AbstractBpmn2TableComposite(this, AbstractBpmn2TableComposite.DEFAULT_STYLE);
			tableComposite.setListItemClass(listItemClass);
			tableComposite.bindList(object, feature);
		}
	}
	
	// TODO: create an adapter for this stuff in the AdapterRegistry
	protected String getDisplayName(EObject object, EStructuralFeature feature) {
		IItemPropertyDescriptor propertyDescriptor = getPropertyDescriptor(object, feature);
		
		String displayName;

		if (propertyDescriptor!=null) {
			displayName = propertyDescriptor.getDisplayName(object);
		}
		else {
			displayName = ModelUtil.toDisplayName(feature.getName());
		}
		return displayName;
	}

	private boolean getIsMultiLine(EObject object, EStructuralFeature feature) {
		IItemPropertyDescriptor propertyDescriptor = getPropertyDescriptor(object, feature);
		
		boolean isMultiLine = false;

		if (propertyDescriptor!=null) {
			isMultiLine = propertyDescriptor.isMultiLine(object);
		}
		return isMultiLine;
	}

	private Collection getChoiceOfValues(EObject object, EStructuralFeature feature) {
		IItemPropertyDescriptor propertyDescriptor = getPropertyDescriptor(object, feature);
		
		if (propertyDescriptor!=null) {
			return propertyDescriptor.getChoiceOfValues(object);
		}
		return null;
	}

	public static IItemPropertyDescriptor getPropertyDescriptor(EObject object, EStructuralFeature feature) {
		AdapterFactory factory;
		ItemProviderAdapter adapter;

		adapter = (ItemProviderAdapter) AdapterUtil.adapt((Notifier)object, ItemProviderAdapter.class);
		if (adapter!=null)
			return adapter.getPropertyDescriptor(object, feature);
		
		return null;
	}
	
	public class ChildObjectStack {
		private Stack<EObject> objectStack = new Stack<EObject>();
		private Stack<Composite> attributesCompositeStack = new Stack<Composite>();
		private Stack<Section> attributesSectionStack = new Stack<Section>();
		
		public void push(EObject object) {
			attributesCompositeStack.push(AbstractBpmn2PropertiesComposite.this.getAttributesParent());
			attributesComposite = null;
			attributesSectionStack.push(attributesSection);
			attributesSection = null;
			objectStack.push(object);
		}
		
		public EObject pop() {
			if (objectStack.size()>0) {
				attributesComposite = attributesCompositeStack.pop();
				
				return objectStack.pop();
			}
			return null;
		}
		
		public EObject peek() {
			if (objectStack.size()>0) {
				return objectStack.peek();
			}
			return AbstractBpmn2PropertiesComposite.this.be;
		}
		
		public Composite getAttributesParent() {
			if (objectStack.size()>0) {
				return attributesCompositeStack.peek();
			}
			return AbstractBpmn2PropertiesComposite.this;
		}
		
		public EObject get(int i) {
			return objectStack.get(i);
		}
		
		public int size() {
			return objectStack.size();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetListener#resourceSetChanged(org.eclipse.emf.transaction.ResourceSetChangeEvent)
	 */
	@Override
	public void resourceSetChanged(ResourceSetChangeEvent event) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetListener#getFilter()
	 */
	@Override
	public NotificationFilter getFilter() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetListener#transactionAboutToCommit(org.eclipse.emf.transaction.ResourceSetChangeEvent)
	 */
	@Override
	public Command transactionAboutToCommit(ResourceSetChangeEvent event) throws RollbackException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetListener#isAggregatePrecommitListener()
	 */
	@Override
	public boolean isAggregatePrecommitListener() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetListener#isPrecommitOnly()
	 */
	@Override
	public boolean isPrecommitOnly() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetListener#isPostcommitOnly()
	 */
	@Override
	public boolean isPostcommitOnly() {
		return false;
	}
}