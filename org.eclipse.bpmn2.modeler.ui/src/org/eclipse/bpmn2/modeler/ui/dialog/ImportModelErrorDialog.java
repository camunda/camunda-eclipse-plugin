/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.ui.dialog;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.bpmn2.modeler.core.importer.ImportException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.custom.ScrolledComposite;

/**
 * Dialog displayed in case of an error.
 * 
 * @author Nico Rehwaldt
 */
public class ImportModelErrorDialog extends TitleAreaDialog {

	private ImportException exception;

	public ImportModelErrorDialog(Shell parentShell) {
		super(parentShell);
		setHelpAvailable(true);
		setShellStyle(SWT.MIN | SWT.TITLE | SWT.PRIMARY_MODAL);
	}

	public void setException(ImportException exception) {
		this.exception = exception;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("");
		
		// create the top level composite for the dialog area
		Composite outerComposite = new Composite(parent, SWT.NONE);
		outerComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		outerComposite.setFont(parent.getFont());
		
		GridLayout outerCompositeLayout = new GridLayout(1, false);
		outerCompositeLayout.verticalSpacing = 0;
		outerCompositeLayout.horizontalSpacing = 0;
		outerCompositeLayout.marginHeight = 0;
		outerCompositeLayout.marginWidth = 0;
		
		outerComposite.setLayout(outerCompositeLayout);
		
		// Build the separator line
		Label titleBarSeparator = new Label(outerComposite, SWT.HORIZONTAL | SWT.SEPARATOR);
		titleBarSeparator.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		
		Composite innerComposite = new Composite(outerComposite, SWT.NONE);
		GridData innerCompositeLayoutData = new GridData(SWT.FILL, SWT.TOP, true, false);
		innerCompositeLayoutData.minimumHeight = 200;
		innerComposite.setLayoutData(innerCompositeLayoutData);
		
		GridLayout innerCompositeLayout = new GridLayout(1, false);
		innerCompositeLayout.verticalSpacing = 10;
		innerCompositeLayout.horizontalSpacing = 0;
		innerCompositeLayout.marginRight = 5;
		innerCompositeLayout.marginLeft = 5;
		innerComposite.setLayout(innerCompositeLayout);

		String exceptionMessage = buildExceptionMessage(exception);
		
		Label exceptionSummary = new Label(innerComposite, SWT.NONE);
		exceptionSummary.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		exceptionSummary.setText(exceptionMessage);

		Link link = new Link(innerComposite, SWT.WRAP);
		link.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		link.setText("If you exported this diagram from another modeling tool the cause of this problem may be related to that tools export functionality. If not, please file a <a>bug report</a>.");
		
		Group stackTraceGroup = new Group(innerComposite, SWT.NONE);
		stackTraceGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		stackTraceGroup.setText("Stacktrace");

		GridLayout stackTraceGroupLayout = new GridLayout(1, false);
		stackTraceGroup.setLayout(stackTraceGroupLayout);
		
		String exceptionDetails = buildExceptionDetails(exception);
		
		TextViewer stackTraceViewer = new TextViewer(stackTraceGroup, SWT.FULL_SELECTION | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		stackTraceViewer.setEditable(false);
		
		StyledText stackTraceViewerStyledText = stackTraceViewer.getTextWidget();
		stackTraceViewerStyledText.setWrapIndent(10);
		GridData stackTraceViewerStyledTextData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		stackTraceViewerStyledTextData.heightHint = 130;
		
		stackTraceViewerStyledText.setLayoutData(stackTraceViewerStyledTextData);
		stackTraceViewerStyledText.setText(exceptionDetails);
		
		return outerComposite;
	}
	
	private String buildExceptionDetails(ImportException exception) {
		StringWriter writer = new StringWriter();
		exception.printStackTrace(new PrintWriter(writer));
		return writer.toString();
	}

	protected String buildExceptionMessage(ImportException exception) {
		
		String message = "Reason: " + exception.getMessage();
		EObject contextElement = exception.getContextElement();
		
		if (contextElement != null) {
			message += " at " + createTextualRepresentation(contextElement);
		}
		
		return message;
	}

	protected String createTextualRepresentation(EObject contextElement) {
		return contextElement.toString();
	}

	@Override
	public void create() {
		
		super.create();

		// Set the title
		setTitle("Could not import BPMN 2.0 model");

		String exceptionMessage = buildExceptionMessage(exception);
		
		// Set the message
		setErrorMessage(exceptionMessage);
	}
	
	@Override
	protected boolean isResizable() {
		return true;
	}
}
