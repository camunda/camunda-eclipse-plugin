package org.camunda.bpm.modeler.ui.dialog.namespace;

import org.camunda.bpm.modeler.ui.util.Browser;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

public class DeprecatedNamespaceDialog extends TitleAreaDialog {

	protected static final String ECLIPSE_PLUGIN_SITES_LINK = "https://docs.camunda.org/manual/7.4/modeler/eclipse-plugin/update-sites/";
	
	protected static final String DIALOG_HEADER = "Warning";
	
	protected static final String DIALOG_TITLE = "Deprecated <activiti> namespace detected";
	
	protected static final String CONVERT_TO_CAMUNDA_NAMESPACE_SNIPPET = "Would you like to convert your diagram to the <camunda> namespace?";
	
	protected static final String PROPERTIES_SNIPPET = "If you do not convert, you will not be able to edit all properties in the properties panel.";
	
	protected static final String SUPPORTED_VERSIONS_SNIPPET = "<camunda> namespace support works from Camunda BPM version 7.4.0, 7.3.3, 7.2.6 onwards.";

	protected static final String RECOMMENDATION_SNIPPET = "If you are using Camunda BPM versions 7.3.0, 7.2.0 or lower "
			+ "we recommend you to read <a href=\"" + ECLIPSE_PLUGIN_SITES_LINK
			+ "\">how to downgrade your BPMN Plugin to version 2.7.0 again</a>.";
	
	protected static final String AUTOMATICALLY_SAVE_SNIPPET = "If you click \"Yes\", the changes will be saved automatically.";
	
	public DeprecatedNamespaceDialog(Shell parentShell) {
		super(parentShell);
		
		setShellStyle(SWT.MIN | SWT.TITLE | SWT.PRIMARY_MODAL);
		setHelpAvailable(false);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(575, super.getInitialSize().y);
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);

		// Set the title
		setTitle(DIALOG_TITLE);
		
		return contents;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		// Set the dialog header
		getShell().setText(DIALOG_HEADER);

		// top level composite for the dialog area
		Composite outerComposite = createOuterComposite(parent);

		createSeparatorLine(outerComposite);

		// inner composite for actual contents
		Composite innerComposite = createInnerComposite(outerComposite);

		createDialogContent(innerComposite);

		return outerComposite;
	}

	private void createSeparatorLine(Composite parent) {
		// Build the separator line
		Label titleBarSeparator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		titleBarSeparator.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
	}

	private Composite createOuterComposite(Composite parent) {
		Composite outerComposite = new Composite(parent, SWT.NONE);
		outerComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		outerComposite.setFont(parent.getFont());

		GridLayout outerCompositeLayout = new GridLayout(1, false);
		outerCompositeLayout.verticalSpacing = 0;
		outerCompositeLayout.horizontalSpacing = 0;
		outerCompositeLayout.marginHeight = 0;
		outerCompositeLayout.marginWidth = 0;

		outerComposite.setLayout(outerCompositeLayout);
		return outerComposite;
	}

	private Composite createInnerComposite(Composite outerComposite) {
		Composite innerComposite = new Composite(outerComposite, SWT.NONE);
		GridData innerCompositeLayoutData = new GridData(SWT.FILL, SWT.TOP, false, false);
		innerCompositeLayoutData.widthHint = 440;
		innerCompositeLayoutData.minimumHeight = 200;
		innerComposite.setLayoutData(innerCompositeLayoutData);

		GridLayout innerCompositeLayout = new GridLayout(1, false);
		innerCompositeLayout.verticalSpacing = 10;
		innerCompositeLayout.horizontalSpacing = 0;
		innerCompositeLayout.marginRight = 5;
		innerCompositeLayout.marginLeft = 5;

		innerComposite.setLayout(innerCompositeLayout);
		return innerComposite;
	}
	
	private void createDialogContent(Composite innerComposite) {
		createLabel(innerComposite, CONVERT_TO_CAMUNDA_NAMESPACE_SNIPPET);
		
		createLabel(innerComposite, PROPERTIES_SNIPPET);
		
		createLabel(innerComposite, SUPPORTED_VERSIONS_SNIPPET);
		
		createLink(innerComposite, RECOMMENDATION_SNIPPET);
		
		createLink(innerComposite, AUTOMATICALLY_SAVE_SNIPPET);
	}
	
	protected void createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.WRAP);
		applyDefaultLayout(label);
		label.setText(text);
	}
	
	protected void createLink(Composite parent, String text) {
		Link link = new Link(parent, SWT.WRAP);
		applyDefaultLayout(link);
		link.setText(text);

		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Browser.open(e.text);
			}
		});
	}
	
	protected void applyDefaultLayout(Control control) {
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_label.widthHint = 400;
		control.setLayoutData(gd_label);
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.NO_LABEL, false);
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.YES_LABEL, false);
	}
	
}
