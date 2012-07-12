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
 * @author Bob Brodt
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.ui.property.dialogs;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpel.wsil.model.inspection.Description;
import org.eclipse.bpel.wsil.model.inspection.Inspection;
import org.eclipse.bpel.wsil.model.inspection.Link;
import org.eclipse.bpel.wsil.model.inspection.Name;
import org.eclipse.bpel.wsil.model.inspection.Service;
import org.eclipse.bpel.wsil.model.inspection.TypeOfAbstract;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerResourceSetImpl;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.utils.JavaProjectClassLoader;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.Messages;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.property.providers.BPMN2DefinitionsTreeContentProvider;
import org.eclipse.bpmn2.modeler.ui.property.providers.JavaTreeContentProvider;
import org.eclipse.bpmn2.modeler.ui.property.providers.ModelLabelProvider;
import org.eclipse.bpmn2.modeler.ui.property.providers.ModelTreeLabelProvider;
import org.eclipse.bpmn2.modeler.ui.property.providers.ServiceTreeContentProvider;
import org.eclipse.bpmn2.modeler.ui.property.providers.TreeNode;
import org.eclipse.bpmn2.modeler.ui.property.providers.VariableTypeTreeContentProvider;
import org.eclipse.bpmn2.modeler.ui.property.providers.WSILContentProvider;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.SelectionStatusDialog;
import org.eclipse.ui.part.DrillDownComposite;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.xsd.XSDSchema;

/**
 * Browse for complex/simple types available in the process and choose that
 * simple type.
 * 
 */

public class SchemaImportDialog extends SelectionStatusDialog {

	// Button id for browsing the workspace
	protected final static int BID_BROWSE_WORKSPACE = IDialogConstants.CLIENT_ID + 1;
	// Button id for browsing URLs
	protected final static int BID_BROWSE_URL = IDialogConstants.CLIENT_ID + 2;
	// Button id for browse files
	protected final static int BID_BROWSE_FILE = IDialogConstants.CLIENT_ID + 3;
	// button id for browsing WSIL
	protected static final int BID_BROWSE_WSIL = IDialogConstants.CLIENT_ID + 5;
	// Browse button id
	protected static final int BID_BROWSE = IDialogConstants.CLIENT_ID + 4;
	// Button id for import XML file types
	protected static final int BID_IMPORT_XML = IDialogConstants.CLIENT_ID + 6;
	// Button id for import XSD file types
	protected static final int BID_IMPORT_XSD = IDialogConstants.CLIENT_ID + 7;
	// Button id for import WSDL file types
	protected static final int BID_IMPORT_WSDL = IDialogConstants.CLIENT_ID + 8;
	// Button id for import BPMN 2.0 file types
	protected static final int BID_IMPORT_BPMN2 = IDialogConstants.CLIENT_ID + 9;
	
	///////////////////////////////////////////////////////////////////////////////
	// TODO: we may want to use JavaUI.createTypeDialog(...) instead of cluttering
	// up this dialog with java types here...
	///////////////////////////////////////////////////////////////////////////////
	// Button id for import Java types
	protected static final int BID_IMPORT_JAVA = IDialogConstants.CLIENT_ID + 10;
	
	// the current import type
	private int fImportType = BID_IMPORT_XSD;
	// the current import source
	private int fImportSource = BID_BROWSE_WORKSPACE;
	// the import type setting, remembered in the dialog settings
	private static final String IMPORT_TYPE = "ImportType"; //$NON-NLS-1$
	// the import source setting, remembered in the dialog settings
	private static final String IMPORT_SOURCE = "ImportSource"; //$NON-NLS-1$

	private static final String EMPTY = ""; //$NON-NLS-1$

	private String[] FILTER_EXTENSIONS;
	private String[] FILTER_NAMES;
	private String resourceFilter;
	protected String fResourceKind;

	protected BPMN2Editor bpmn2Editor;
	protected EObject modelObject;

	protected Tree fTree;
	protected TreeViewer fTreeViewer;

	Text fLocation;
	String fLocationText;
	Label fLocationLabel;
	Label fStructureLabel;

	private Composite fLocationComposite;
	FileSelectionGroup fResourceComposite;

	// Import from WSIL constructs
	private Composite fWSILComposite;
	protected TreeViewer fWSILTreeViewer;
	protected Tree fWSILTree;
	protected Text filterText;
	String fFilter = ""; //$NON-NLS-1$

	Button fBrowseButton;

	private Group fTypeGroup;

	private Group fKindGroup;
	private Composite fKindButtonComposite;

	private IDialogSettings fSettings;

	private String fStructureTitle;

	private ITreeContentProvider fTreeContentProvider;

	protected Object fInput;

	protected Bpmn2ModelerResourceSetImpl fHackedResourceSet;

	long fRunnableStart;
	URI fRunnableLoadURI;
	Job fLoaderJob;

	IPreferenceStore fPrefStore = Activator.getDefault().getPreferenceStore();
	String fBasePath = fPrefStore.getString(Bpmn2Preferences.PREF_WSIL_URL);

	// The WSIL radio box is turned off if the WSIL document is not set in the
	// modelEnablement.
	Button fBtnWSIL;
	Button fBtnResource;

	/**
	 * Create a brand new shiny Schema Import Dialog.
	 * 
	 * @param parent
	 */
	public SchemaImportDialog(Shell parent) {

		super(parent);
		setStatusLineAboveButtons(true);
		int shellStyle = getShellStyle();
		setShellStyle(shellStyle | SWT.MAX | SWT.RESIZE);

		fSettings = Activator.getDefault().getDialogSettingsFor(this);

		try {
			fImportSource = fSettings.getInt(IMPORT_SOURCE);
			fImportType = fSettings.getInt(IMPORT_TYPE);
		} catch (java.lang.NumberFormatException nfe) {
			fImportSource = BID_BROWSE_WORKSPACE;
			fImportType = BID_IMPORT_XSD;
		}

		setDialogBoundsSettings(fSettings, getDialogBoundsStrategy());

		if (fImportType==BID_IMPORT_XML)
			configureAsXMLImport();
		else if (fImportType==BID_IMPORT_XSD)
			configureAsSchemaImport();
		else if (fImportType==BID_IMPORT_WSDL)
			configureAsWSDLImport();
		else if (fImportType==BID_IMPORT_BPMN2)
			configureAsBPMN2Import();
		else if (fImportType==BID_IMPORT_JAVA)
			configureAsJavaImport();
		
		bpmn2Editor = BPMN2Editor.getActiveEditor();
	}

	/**
	 * Create a brand new shiny Schema Import Dialog
	 * 
	 * @param parent
	 *            shell to use
	 * @param eObject
	 *            the model object to use as reference
	 */
	public SchemaImportDialog(Shell parent, EObject eObject) {
		this(parent);

		this.modelObject = eObject;

		ResourceSet rs =  bpmn2Editor.getResourceSet();
		fHackedResourceSet = ModelUtil.slightlyHackedResourceSet(rs);
	}

	/**
	 * 
	 * @see Dialog#createDialogArea(Composite)
	 * 
	 * @param parent
	 *            the parent composite to use
	 * @return the composite it created to be used in the dialog area.
	 */

	@Override
	public Control createDialogArea(Composite parent) {

		Composite contents = (Composite) super.createDialogArea(parent);

		createImportType(contents);
		createImportLocation(contents);
		createImportStructure(contents);

		buttonPressed(fImportSource, true);
		return contents;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case BID_BROWSE:
			if (fImportSource == BID_BROWSE_URL) {
				String loc = fLocation.getText();
				if (loc.length() > 0) {
					attemptLoad(loc);
				}
			}
			else {
				FileDialog fileDialog = new FileDialog(getShell());
				fileDialog.setFilterExtensions(FILTER_EXTENSIONS);
				fileDialog.setFilterNames(FILTER_NAMES);
				String path = fileDialog.open();
				if (path == null) {
					return;
				}
				fLocation.setText(path);
				attemptLoad(path);
			}
			break;

		case IDialogConstants.CANCEL_ID:
			if (fLoaderJob != null) {
				if (fLoaderJob.getState() == Job.RUNNING) {
					fLoaderJob.cancel();
				}
			}
			break;
		}

		super.buttonPressed(buttonId);
	}

	protected void buttonPressed(int id, boolean checked) {

		if (id==BID_BROWSE_FILE
				|| id==BID_BROWSE_WORKSPACE
				|| id==BID_BROWSE_URL
				|| id==BID_BROWSE_WSIL) {
			if (checked==false) {
				return;
			}
			if (id==BID_BROWSE_WSIL) {
				if (fBasePath==null || fBasePath.isEmpty()) {
					MessageDialog.openInformation(getShell(), "WSIL Browser",
							"In order to browse a WSIL registry, please configure a\n"+
							"WSIL Document URL in the BPMN2 Preferences.");
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							fBtnWSIL.setSelection(false);
							fBtnResource.setSelection(true);
							buttonPressed(BID_BROWSE_WORKSPACE, true);
						}
					});
					return;
				}
			}

			fImportSource = id;
			fSettings.put(IMPORT_SOURCE, fImportSource);
		}
		else if (id==BID_IMPORT_XML
				|| id==BID_IMPORT_XSD
				|| id==BID_IMPORT_WSDL
				|| id==BID_IMPORT_BPMN2
				|| id==BID_IMPORT_JAVA) {
			if (checked==false) {
				return;
			}
			if (id==BID_IMPORT_XML) {
				configureAsXMLImport();
				setVisibleControl(fKindButtonComposite,true);
			}
			else if (id==BID_IMPORT_XSD) {
				configureAsSchemaImport();
				setVisibleControl(fKindButtonComposite,true);
			}
			else if (id==BID_IMPORT_WSDL) {
				configureAsWSDLImport();
				setVisibleControl(fKindButtonComposite,true);
			}
			else if (id==BID_IMPORT_BPMN2) {
				configureAsBPMN2Import();
				setVisibleControl(fKindButtonComposite,true);
			}
			else if (id==BID_IMPORT_JAVA) {
				configureAsJavaImport();
				setVisibleControl(fKindButtonComposite,false);
			}
			
			fImportType = id;
			fSettings.put(IMPORT_TYPE, fImportType);
		}
		
		setVisibleControl(fResourceComposite, fImportSource==BID_BROWSE_WORKSPACE && fImportType != BID_IMPORT_JAVA);
		setVisibleControl(fLocationComposite, fImportSource==BID_BROWSE_URL || fImportSource==BID_BROWSE_FILE || fImportType==BID_IMPORT_JAVA);
		setVisibleControl(fWSILComposite, fImportSource==BID_BROWSE_WSIL && fImportType != BID_IMPORT_JAVA);
		if (fImportType==BID_IMPORT_JAVA) {
			setVisibleControl(fKindButtonComposite, false);
			setVisibleControl(fBrowseButton,false);
			fLocationLabel.setText(Messages.SchemaImportDialog_27);
		}
		else {
			setVisibleControl(fKindButtonComposite, true);
			setVisibleControl(fBrowseButton,true);
			fLocationLabel.setText(Messages.SchemaImportDialog_8);
			fBrowseButton.setText(fImportSource==BID_BROWSE_FILE || fImportSource==BID_BROWSE_WSIL ?
					Messages.SchemaImportDialog_9 : Messages.SchemaImportDialog_26);
		}
		fLocation.setText(EMPTY);
		fTypeGroup.getParent().layout(true);
		fKindGroup.getParent().layout(true);

		markEmptySelection();
	}

	protected void setVisibleControl(Control c, boolean b) {
		Object layoutData = c.getLayoutData();

		if (layoutData instanceof GridData) {
			GridData data = (GridData) layoutData;
			data.exclude = !b;
		}
		c.setVisible(b);
	}

	/**
	 * Create the dialog.
	 * 
	 */

	@Override
	public void create() {
		super.create();
		buttonPressed(fImportSource, true);
	}

	protected Button createRadioButton(Composite parent, String label, int id,
			boolean checked) {

		Button button = new Button(parent, SWT.RADIO);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData( Integer.valueOf( id ));
		button.setSelection(checked);

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Button b = (Button) event.widget;
				int bid = ((Integer) b.getData()).intValue();

				buttonPressed(bid, b.getSelection());
			}
		});

		return button;

	}

	protected void createImportType(Composite parent) {
		fTypeGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
		fTypeGroup.setText(Messages.SchemaImportDialog_3);
		GridLayout layout = new GridLayout(1, true);
		GridData data = new GridData();
		data.grabExcessVerticalSpace = false;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;

		fTypeGroup.setLayout(layout);
		fTypeGroup.setLayoutData(data);

		Composite container = new Composite(fTypeGroup, SWT.NONE);

		layout = new GridLayout();
		layout.makeColumnsEqualWidth = false;
		layout.numColumns = 4;
		container.setLayout(layout);
		data = new GridData();
		data.grabExcessVerticalSpace = false;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.CENTER;
		container.setLayoutData(data);

		Button button;
		
//		button = createRadioButton(container, Messages.SchemaImportDialog_20,
//				BID_IMPORT_XML, fImportType == BID_IMPORT_XML);
//		button.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		
		button = createRadioButton(container, Messages.SchemaImportDialog_21,
				BID_IMPORT_XSD, fImportType == BID_IMPORT_XSD);
		button.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		
		button = createRadioButton(container, Messages.SchemaImportDialog_22,
				BID_IMPORT_WSDL, fImportType == BID_IMPORT_WSDL);
		button.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		
		button = createRadioButton(container, Messages.SchemaImportDialog_28,
				BID_IMPORT_BPMN2, fImportType == BID_IMPORT_BPMN2);
		button.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		
		button = createRadioButton(container, Messages.SchemaImportDialog_23,
				BID_IMPORT_JAVA, fImportType == BID_IMPORT_JAVA);
		button.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
	}
	
	protected void createImportLocation(Composite parent) {

		fKindGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
		fKindGroup.setText(Messages.SchemaImportDialog_4);
		GridLayout layout = new GridLayout(1, true);
		GridData data = new GridData();
		data.grabExcessVerticalSpace = false;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;

		fKindGroup.setLayout(layout);
		fKindGroup.setLayoutData(data);

		fKindButtonComposite = new Composite(fKindGroup, SWT.NONE);

		layout = new GridLayout();
		layout.makeColumnsEqualWidth = true;
		layout.numColumns = 4;
		fKindButtonComposite.setLayout(layout);
		data = new GridData();
		data.grabExcessVerticalSpace = false;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.CENTER;
		fKindButtonComposite.setLayoutData(data);

		fBtnResource = createRadioButton(fKindButtonComposite, Messages.SchemaImportDialog_5,
				BID_BROWSE_WORKSPACE, fImportSource == BID_BROWSE_WORKSPACE);
		createRadioButton(fKindButtonComposite, Messages.SchemaImportDialog_6,
				BID_BROWSE_FILE, fImportSource == BID_BROWSE_FILE);
		createRadioButton(fKindButtonComposite, Messages.SchemaImportDialog_7,
				BID_BROWSE_URL, fImportSource == BID_BROWSE_URL);

		// Add WSIL option
		fBtnWSIL = createRadioButton(fKindButtonComposite, Messages.SchemaImportDialog_15,
				BID_BROWSE_WSIL, fImportSource == BID_BROWSE_WSIL);

		// Create location variant
		fLocationComposite = new Composite(fKindGroup, SWT.NONE);

		layout = new GridLayout();
		layout.numColumns = 3;
		fLocationComposite.setLayout(layout);
		data = new GridData();
		data.grabExcessVerticalSpace = true;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		fLocationComposite.setLayoutData(data);

		fLocationLabel = new Label(fLocationComposite, SWT.NONE);
		fLocationLabel.setText(Messages.SchemaImportDialog_8);

		fLocation = new Text(fLocationComposite, SWT.BORDER);
		fLocation.setText(EMPTY);
		data = new GridData();
		data.grabExcessVerticalSpace = false;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		fLocation.setLayoutData(data);
//		fLocation.addListener(SWT.FocusOut, new Listener() {
//
//			public void handleEvent(Event event) {
//				String loc = fLocation.getText();
//				if (loc.length() > 0) {
//					attemptLoad(loc);
//				}
//			}
//		});
		fLocation.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent event) {
				if (fImportType == BID_IMPORT_JAVA) {
				}
				else if (event.keyCode == SWT.CR) {
					attemptLoad(fLocation.getText());
					event.doit = false;
				}
			}

			public void keyReleased(KeyEvent e) {
				if (fImportType == BID_IMPORT_JAVA) {
					String s = fLocation.getText();
					if (s!=null && s.length()>1) {
						if (!s.equals(fLocationText)) {
							fLocationText = s;
							attemptLoad(s);
						}
					}
				}
			}

		});

		fBrowseButton = createButton(fLocationComposite, BID_BROWSE,
				Messages.SchemaImportDialog_9, false);

		// End of location variant

		// Start Resource Variant
		fResourceComposite = new FileSelectionGroup(fKindGroup, new Listener() {
			public void handleEvent(Event event) {
				IResource resource = fResourceComposite.getSelectedResource();
				if (resource != null && resource.getType() == IResource.FILE) {
					// only attempt to load a resource which is not a container
					attemptLoad((IFile) resource);
					return;
				}
				markEmptySelection();
			}
		}, Messages.SchemaImportDialog_10, resourceFilter); //$NON-NLS-1$

		TreeViewer viewer = fResourceComposite.getTreeViewer();
		viewer.setAutoExpandLevel(2);

		// End resource variant

		// create WSIL UI widgets
		createWSILStructure(fKindGroup);

	}

	protected Object createWSILStructure(Composite parent) {
		
        fWSILComposite = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        fWSILComposite.setLayout(layout);
        
		GridData data = new GridData();        
        data.grabExcessVerticalSpace = true;
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.minimumHeight = 220;
        fWSILComposite.setLayoutData(data);
        
		Label location = new Label(fWSILComposite, SWT.NONE);
	    location.setText( Messages.SchemaImportDialog_16 );
	    
	    data = new GridData();
	    data.grabExcessHorizontalSpace = true;
	    data.horizontalAlignment = SWT.LEFT;
	    location.setLayoutData(data);
	    
	    filterText = new Text(fWSILComposite, SWT.BORDER);
	    data = new GridData(GridData.FILL_HORIZONTAL);
	    filterText.setLayoutData(data);
	    
    	filterText.addKeyListener(new KeyListener() {
    		
    		public void keyPressed(KeyEvent e) {
    			
    		}
    		
    		public void keyReleased(KeyEvent e) {
    			// set the value of the filter.
    			fFilter = filterText.getText().trim().toLowerCase();
    			    		
       			if (fFilter.length() > 0) {
       				/* for the time being, only filter 3 levels deep 
       				 * since link references within WSIL are rare at 
       				 * this time.  when adoption of WSIL directories
       				 * take off, this needs to be rehashed */ 
       				fWSILTreeViewer.expandToLevel(3);
       			}
       			fWSILTreeViewer.refresh();
       			e.doit = false;
			}	
    	});
	    
	    DrillDownComposite wsilTreeComposite = new DrillDownComposite(fWSILComposite, SWT.BORDER);
		
		layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.verticalSpacing = 0;
        wsilTreeComposite.setLayout(layout);
        
        data = new GridData();        
        data.grabExcessVerticalSpace = true;
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        wsilTreeComposite.setLayoutData(data);
	        
		//	Tree viewer for variable structure ...
		fWSILTree = new Tree(wsilTreeComposite, SWT.NONE );
		data = new GridData();        
        data.grabExcessVerticalSpace = true;
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.minimumHeight = 200;
        fWSILTree.setLayoutData(data);
		  		
		fWSILTreeViewer = new TreeViewer( fWSILTree );
		fWSILTreeViewer.setContentProvider( new WSILContentProvider() );
		fWSILTreeViewer.setLabelProvider( new ModelLabelProvider() );
		
		Object wsilDoc = attemptLoad(URI.createURI(fBasePath),"wsil");
		fWSILTreeViewer.setInput ( 	wsilDoc ) ;
		if (wsilDoc == null || wsilDoc instanceof Throwable  ) {
//			fBtnWSIL.setEnabled(false);
			// that's always available.
			// delete fImportSource = BID_BROWSE_WORKSPACE; by Grid.Qian
			// because if not, the dialog always display the resource Control
			// regardless last time if user choose the resource button
				
			/*// that's always available.
			fImportSource = BID_BROWSE_WORKSPACE;*/
		}
		
		
		// set default tree expansion to the 2nd level
		fWSILTreeViewer.expandToLevel(2);
		fWSILTreeViewer.addFilter(new TreeFilter());
		fWSILTreeViewer.setComparator(new WSILViewerComparator());

		wsilTreeComposite.setChildTree(fWSILTreeViewer);

		fWSILTreeViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						// TODO Auto-generated method stub
						IStructuredSelection sel = (IStructuredSelection) event.getSelection();
						if (sel.getFirstElement() instanceof Service) {
							Service serv = (Service) sel.getFirstElement();
							Description descr = serv.getDescription().get(0);
							attemptLoad(descr.getLocation());
						} else {
							markEmptySelection();
						}
					}
				});
		// end tree viewer for variable structure

		return fWSILComposite;
	}

	protected Object createImportStructure(Composite parent) {

		fStructureLabel = new Label(parent, SWT.NONE);
		fStructureLabel.setText(fStructureTitle);

		// Tree viewer for variable structure ...
		fTree = new Tree(parent, SWT.BORDER);

		fTreeViewer = new TreeViewer(fTree);
		fTreeViewer.setContentProvider(fTreeContentProvider);
		fTreeViewer.setLabelProvider(new ModelTreeLabelProvider());
		fTreeViewer.setInput(null);
		fTreeViewer.setAutoExpandLevel(3);
		fTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection sel = (IStructuredSelection) event.getSelection();
				if (!sel.isEmpty()) {
					updateStatus(Status.OK_STATUS);		
				} else {
					markEmptySelection();
				}
			}
		});
		// end tree viewer for variable structure
		GridData data = new GridData();
		data.grabExcessVerticalSpace = true;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.minimumHeight = 200;
		fTree.setLayoutData(data);

		return fTree;
	}

	Object attemptLoad(URI uri, String kind) {

		Resource resource = null;
		if ("java".equals(kind)) {
			final String fileName = uri.lastSegment();
			final List<Class> results = new ArrayList<Class>();
			IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			for (IProject p : projects) {
				try {
					if (p.isOpen() && p.hasNature(JavaCore.NATURE_ID)) {
						final IJavaProject javaProject = JavaCore.create(p);
						JavaProjectClassLoader cl = new JavaProjectClassLoader(javaProject);
						results.addAll(cl.findClasses(fileName));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return results;
		}
		else {
			try {
				resource = fHackedResourceSet.getResource(uri, true, kind);
			} catch (Throwable t) {
				// BPELUIPlugin.log(t);
				return t;
			}
		
			if (resource!=null && resource.getErrors().isEmpty() && resource.isLoaded()) {
				return resource.getContents().get(0);
			}
		}
		return null;
	}
	
	Object  attemptLoad ( URI uri ) {
		return attemptLoad (uri, fResourceKind );
	}
	
	
	void attemptLoad ( IFile file ) {
		attemptLoad ( file.getFullPath().toString() );
	}
	
		
	void attemptLoad ( String path ) {
		
		if (fLoaderJob != null) {			
			if (fLoaderJob.getState() == Job.RUNNING) {
				fLoaderJob.cancel();
			}			
		}
		
		updateStatus ( Status.OK_STATUS );		

		// empty paths are ignored
		path = path.trim();
		if (path.length() == 0) {
			return ;
		}
		

		URI uri = convertToURI ( path );
		if (uri == null) {
			return ;
		}
		
		
		if (uri.isRelative()) {
			// construct absolute path	          
			String absolutepath = fBasePath.substring(0, fBasePath.lastIndexOf('/')+1) + path;
			uri = URI.createURI(absolutepath);
		}
		
		
		fRunnableLoadURI = uri;		
		final String msg = MessageFormat.format(Messages.SchemaImportDialog_17,fRunnableLoadURI);		 	    
		fLoaderJob = new Job(msg) {

			@Override
			protected IStatus run (IProgressMonitor monitor) {
				monitor.beginTask(msg, 1);				
				// Bug 290090 - move this to asyncExec() as below because the method will
				// modify UI parameter, if not, will have a invalid access error.

				/* fInput = attemptLoad(fRunnableLoadURI); */

				monitor.worked(1);
				if (fBrowseButton != null
						&& fBrowseButton.isDisposed() == false) {
					fBrowseButton.getDisplay().asyncExec(new Runnable() {
						public void run() {
							fInput = attemptLoad(fRunnableLoadURI);
							loadDone();
						}
					});
				}
				
				return Status.OK_STATUS;
			}			 		
		};	
		 
		fLoaderJob.schedule();		 
		fRunnableStart = System.currentTimeMillis();

		updateStatus ( new Status(IStatus.INFO, Activator.getDefault().getID(),0,msg,null));
	}

	
	 
	@SuppressWarnings("boxing")
	void loadDone () {				
		
		long elapsed = System.currentTimeMillis() - fRunnableStart;
		
		if (fInput == null || fInput instanceof Throwable) {
			markEmptySelection();
			
			updateStatus( new Status(IStatus.ERROR,Activator.getDefault().getID(),0,
					MessageFormat.format(Messages.SchemaImportDialog_19,fRunnableLoadURI,elapsed),(Throwable) fInput) );
			fInput = null;
			
		} else {
			
			updateStatus ( new Status(IStatus.INFO, Activator.getDefault().getID(),0,
					MessageFormat.format(Messages.SchemaImportDialog_18,fRunnableLoadURI,elapsed),null)) ;
				

			// display a warning if this import does not define a targetNamespace
			String type = null;
			String ns = null;
			String loc = null;
			if (fInput instanceof XSDSchema) {
				XSDSchema schema = (XSDSchema)fInput;
				ns = schema.getTargetNamespace();
				loc = schema.getSchemaLocation();
				type = "XSD Schema";
			}
			else if (fInput instanceof Definition) {
				Definition definition = (Definition)fInput;
				ns = definition.getTargetNamespace();
				loc = definition.getLocation();
				type = "WSDL";
			}
			else if (fInput instanceof org.eclipse.bpmn2.DocumentRoot) {
				DocumentRoot root = (DocumentRoot)fInput;
				org.eclipse.bpmn2.Definitions definitions = root.getDefinitions();
				ns = definitions.getTargetNamespace();
				loc = root.eResource().getURI().toString();
				type = "BPMN2";
				fInput = definitions;
			}
			else if (fInput instanceof List) {
				markEmptySelection();
			}
			if (type!=null) {
				if (ns==null || ns.isEmpty()) {
					updateStatus ( new Status(IStatus.WARNING, Activator.getDefault().getID(),0,
							"This "+type+" does not define a target namespace",null)) ;
				}
				if (loc==null || loc.isEmpty()) {
					updateStatus( new Status(IStatus.ERROR,Activator.getDefault().getID(),0,
							MessageFormat.format(Messages.SchemaImportDialog_19,fRunnableLoadURI,elapsed),null) );
					fInput = null;
				}
			}
			
			fTreeViewer.setInput(fInput);				
			fTree.getVerticalBar().setSelection(0);
		}
	}
	
	
	
	void markEmptySelection () {
		updateStatus ( Status.OK_STATUS );
		updateOK(false);
		fTreeViewer.setInput(null);
	}
	
	
	private URI convertToURI (String path ) {
		
		try {
			switch (fImportSource) {
			case BID_BROWSE_FILE : 
				return URI.createFileURI( path );				
			
			case BID_BROWSE_WORKSPACE :
				return URI.createPlatformResourceURI(path,false);				
			
			case BID_BROWSE_WSIL :
				//return URI.createFileURI( path );
			case BID_BROWSE_URL :
				return URI.createURI(path);
				

				
			default :
				return null;
			}
			
		} catch (Exception ex) {
			updateStatus ( new Status(IStatus.ERROR,Activator.getDefault().getID(),0,Messages.SchemaImportDialog_13,ex) );			
			return null;
		}
	}

	/**
	 * Update the state of the OK button to the state indicated.
	 * 
	 * @param state
	 *            false to disable, true to enable.
	 */

	public void updateOK(boolean state) {
		Button okButton = getOkButton();
		if (okButton != null && !okButton.isDisposed()) {
			okButton.setEnabled(state);
		}
	}

	/**
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#computeResult()
	 */

	@Override
	protected void computeResult() {
		Object object = fTreeViewer.getInput();
		if (object == null) {
			return;
		}
		if (fImportType == BID_IMPORT_JAVA) {
			IStructuredSelection sel = (IStructuredSelection)fTreeViewer.getSelection();
			if (!sel.isEmpty()) {
				TreeNode treeNode = (TreeNode)sel.getFirstElement();
				setSelectionResult(new Object[] { treeNode.getModelObject() });
			}
		}
		else {
			setSelectionResult(new Object[] { object });
		}
	}

	/**
	 * TODO: not implemented - do we need this?
	 */
	public void configureAsXMLImport() {
		setTitle(Messages.SchemaImportDialog_1);
		fStructureTitle = Messages.SchemaImportDialog_12;
		if (fStructureLabel!=null)
			fStructureLabel.setText(fStructureTitle);
		fTreeContentProvider = new VariableTypeTreeContentProvider(true, true);
		if (fTreeViewer!=null)
			fTreeViewer.setContentProvider(fTreeContentProvider);
		fResourceKind = "xml";

		String[] wsdl_FILTER_EXTENSIONS = {
				"*.xml",
				"*.xsd",
				"*.wsdl",
				"*.*"
		};
		FILTER_EXTENSIONS = wsdl_FILTER_EXTENSIONS;

		String[] wsdl_FILTER_NAMES = {
				"XML Files",
				"XML Schema Files",
				"WSDL Definition Files",
				"All"
		};
		FILTER_NAMES = wsdl_FILTER_NAMES;

		resourceFilter = ".xml";
		if (fResourceComposite!=null)
			fResourceComposite.setFileFilter(resourceFilter);
	}

	/**
	 * Configure the dialog as a schema import dialog. Set the title and the
	 * structure pane message.
	 * 
	 */

	public void configureAsSchemaImport() {
		setTitle(Messages.SchemaImportDialog_2);
		fStructureTitle = Messages.SchemaImportDialog_11;
		if (fStructureLabel!=null)
			fStructureLabel.setText(fStructureTitle);
		fTreeContentProvider = new VariableTypeTreeContentProvider(true, true);
		if (fTreeViewer!=null)
			fTreeViewer.setContentProvider(fTreeContentProvider);
		fResourceKind = "xsd";

		String[] wsdl_FILTER_EXTENSIONS = {
				"*.xml",
				"*.xsd",
				"*.wsdl",
				"*.*"
		};
		FILTER_EXTENSIONS = wsdl_FILTER_EXTENSIONS;

		String[] wsdl_FILTER_NAMES = {
				"XML Files",
				"XML Schema Files",
				"WSDL Definition Files",
				"All"
		};
		FILTER_NAMES = wsdl_FILTER_NAMES;

		resourceFilter = ".xsd";
		if (fResourceComposite!=null)
			fResourceComposite.setFileFilter(resourceFilter);
	}

	/**
	 * Configure the dialog as a WSDL import dialog. Set the title and the
	 * structure pane message.
	 * 
	 */

	public void configureAsWSDLImport() {

		setTitle(Messages.SchemaImportDialog_0);
		fStructureTitle = Messages.SchemaImportDialog_14;
		if (fStructureLabel!=null)
			fStructureLabel.setText(fStructureTitle);
		fTreeContentProvider = new ServiceTreeContentProvider(true);
		if (fTreeViewer!=null)
			fTreeViewer.setContentProvider(fTreeContentProvider);
		fResourceKind = "wsdl";

		String[] wsdl_FILTER_EXTENSIONS = {
				"*.wsdl",
				"*.*"
		};
		FILTER_EXTENSIONS = wsdl_FILTER_EXTENSIONS;

		String[] wsdl_FILTER_NAMES = {
				"WSDL Definition Files",
				"All"
		};
		FILTER_NAMES = wsdl_FILTER_NAMES;

		resourceFilter = ".wsdl";
		if (fResourceComposite!=null)
			fResourceComposite.setFileFilter(resourceFilter);
	}

	public void configureAsBPMN2Import() {

		setTitle(Messages.SchemaImportDialog_29);
		fStructureTitle = Messages.SchemaImportDialog_30;
		if (fStructureLabel!=null)
			fStructureLabel.setText(fStructureTitle);
		fTreeContentProvider = new BPMN2DefinitionsTreeContentProvider(true);
		if (fTreeViewer!=null)
			fTreeViewer.setContentProvider(fTreeContentProvider);
		fResourceKind = "";

		String[] wsdl_FILTER_EXTENSIONS = {
				"*.bpmn",
				"*.bpmn2",
				"*.*"
		};
		FILTER_EXTENSIONS = wsdl_FILTER_EXTENSIONS;

		String[] wsdl_FILTER_NAMES = {
				"BPMN 2.0 Diagram Files",
				"BPMN 2.0 Diagram Files",
				"All"
		};
		FILTER_NAMES = wsdl_FILTER_NAMES;

		resourceFilter = ".bpmn";
		if (fResourceComposite!=null)
			fResourceComposite.setFileFilter(resourceFilter);
	}

	public void configureAsJavaImport() {

		setTitle(Messages.SchemaImportDialog_24);
		fStructureTitle = Messages.SchemaImportDialog_25;
		if (fStructureLabel!=null)
			fStructureLabel.setText(fStructureTitle);
		fTreeContentProvider = new JavaTreeContentProvider(true);
		if (fTreeViewer!=null)
			fTreeViewer.setContentProvider(fTreeContentProvider);
		fResourceKind = "java";

		String[] java_FILTER_EXTENSIONS = {
				"*.java",
				"*.class",
				"*.jar",
				"*.*"
		};
		FILTER_EXTENSIONS = java_FILTER_EXTENSIONS;

		String[] wsdl_FILTER_NAMES = {
				"Java Source Files",
				"Compiled Java Files",
				"Java Archives",
				"All"
		};
		FILTER_NAMES = wsdl_FILTER_NAMES;

		// Resource selection widget not used (yet)
		resourceFilter = ".java";
		if (fResourceComposite!=null)
			fResourceComposite.setFileFilter(resourceFilter);
	}

	@Override
	public void setTitle(String title) {
		super.setTitle(title);
		if (getShell()!=null)
			getShell().setText(title);
	}
	
	/**
	 * 
	 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
	 * @date May 4, 2007
	 * 
	 */
	public class TreeFilter extends ViewerFilter {

		/**
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {

			if (fFilter == null || fFilter.length() == 0) {
				return true;
			}

			if (element instanceof Service) {
				String text = ""; //$NON-NLS-1$
				Service service = (Service) element;
				if (service.getName().size() > 0) {
					Name name = service.getName().get(0);
					text += name.getValue();
				}
				if (service.getAbstract().size() > 0) {
					TypeOfAbstract abst = service.getAbstract().get(0);
					text += abst.getValue();
				}
				return (text.toLowerCase().indexOf(fFilter) > -1);
			}

			return true;
		}
	}

	/**
	 * 
	 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
	 * @date May 10, 2007
	 * 
	 */
	public class WSILViewerComparator extends ViewerComparator {

		/**
		 * @see org.eclipse.jface.viewers.ViewerComparator#category(java.lang.Object)
		 */
		@Override
		public int category(Object element) {
			if (element instanceof Inspection)
				return 1;
			if (element instanceof Link)
				return 2;
			if (element instanceof Service)
				return 3;

			return 0;
		}
	}
}
