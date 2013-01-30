/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.ui.dialog.importer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.importer.ImportException;
import org.eclipse.bpmn2.modeler.ui.util.Browser;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * Dialog displayed in case of an error.
 * 
 * @author Nico Rehwaldt
 */
public class ModelProblemsDialog extends TitleAreaDialog {

	private ImportException exception;
	private List<ImportException> warnings;

	private static final String FILE_BUG_REPORTS_LINK = "https://app.camunda.com/jira/secure/CreateIssue.jspa?pid=10111&issuetype=1&Create=Create";

	private static final String FILE_BUG_REPORTS_NOTES = "If you exported this diagram from another modeling tool, "
			+ "the cause of this problem may be related to "
			+ "the export functionality of that tool. If not, please file a <a href=\"" + FILE_BUG_REPORTS_LINK
			+ "\">bug report</a>.";

	private static final String DETAILS_NOTES = "Refer to the error log for details or use the context menu to copy the stack traces of an issue.";

	public ModelProblemsDialog(Shell parentShell) {
		super(parentShell);

		setShellStyle(SWT.MIN | SWT.TITLE | SWT.PRIMARY_MODAL);
		setHelpAvailable(false);
	}

	public void setException(ImportException exception) {
		this.exception = exception;
	}

	public void setWarnings(List<ImportException> warnings) {
		this.warnings = warnings;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		// Set the dialog header
		getShell().setText("Some problems occured");

		// top level composite for the dialog area
		Composite outerComposite = createOuterComposite(parent);

		createSeparatorLine(outerComposite);

		// inner composite for actual contents
		Composite innerComposite = createInnerComposite(outerComposite);

		createIntroducingLable(innerComposite);

		createProblemsTreeView(innerComposite);

		// description and link //////////
		createAuxiliaryInformation(innerComposite);

		createSupportNotes(innerComposite);

		return outerComposite;
	}

	private void createAuxiliaryInformation(Composite innerComposite) {
		Label label = new Label(innerComposite, SWT.WRAP);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_label.widthHint = 420;
		label.setLayoutData(gd_label);
		label.setText(DETAILS_NOTES);
	}

	private void createSupportNotes(Composite parent) {
		Link link = new Link(parent, SWT.WRAP);
		GridData gd_link = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_link.widthHint = 420;
		link.setLayoutData(gd_link);
		link.setText(FILE_BUG_REPORTS_NOTES);

		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Browser.open(e.text);
			}
		});
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

	private void createProblemsTreeView(Composite parent) {

		// clipboard to copy and paste from / to
		final Clipboard clipboard = new Clipboard(parent.getDisplay());

		Composite treeColumnComposite = new Composite(parent, SWT.NONE);
		GridData treeColumnCompositeGridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		treeColumnCompositeGridData.widthHint = 420;
		treeColumnCompositeGridData.heightHint = 128;
		treeColumnComposite.setLayoutData(treeColumnCompositeGridData);

		TreeColumnLayout treeColumnLayout = new TreeColumnLayout();
		treeColumnComposite.setLayout(treeColumnLayout);

		final TreeViewer treeViewer = new TreeViewer(treeColumnComposite, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		Tree tree = treeViewer.getTree();
		tree.setLinesVisible(true);

		treeViewer.setContentProvider(new ProblemsContentProvider());
		treeViewer.setLabelProvider(new ProblemLabelProvider());

		Menu menu = new Menu(parent.getShell(), SWT.POP_UP);
		final MenuItem copyItem = new MenuItem(menu, SWT.PUSH);
		copyItem.setText("Copy Stacktrace to Clipboard");

		String imageName = ISharedImages.IMG_TOOL_COPY;
		copyItem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(imageName));

		copyItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
				Problem problem = (Problem) selection.getFirstElement();

				copyToClipboard(clipboard, problem);
			}
		});

		treeViewer.getTree().setMenu(menu);

		List<Problem> problems = populateProblems();

		// Expand the tree
		treeViewer.setAutoExpandLevel(2);

		// Provide the input to the ContentProvider
		treeViewer.setInput(problems);
	}

	private void createIntroducingLable(Composite innerComposite) {
		Label exceptionSummary = new Label(innerComposite, SWT.NONE);
		exceptionSummary.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		exceptionSummary.setText("Identified issues: ");
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

	@Override
	public void create() {
		super.create();

		// Set the title
		setTitle("One or more problems found while opening the BPMN 2.0 model");

		// Set the message
		String summaryMessage = createSummaryMessage(exception, warnings);

		setMessage(summaryMessage, exception == null ? IMessageProvider.WARNING : IMessageProvider.ERROR);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	// Utilities ///////////////////////////////////////////////////////

	private void copyToClipboard(final Clipboard clipboard, Problem problem) {

		String exceptionString = buildExceptionDetails(problem.getException());

		TextTransfer textTransfer = TextTransfer.getInstance();
		clipboard.setContents(new Object[] { exceptionString }, new Transfer[] { textTransfer });
	}

	private List<Problem> populateProblems() {
		List<Problem> problems = new ArrayList<Problem>();
		if (exception != null) {
			problems.add(new Problem(exception, false));
		}

		if (warnings != null) {
			for (ImportException warning : warnings) {
				problems.add(new Problem(warning, true));
			}
		}
		return problems;
	}

	// Accessors //////////////////////////////////////////////////////////

	@Override
	protected boolean isResizable() {
		return true;
	}

	// Message building ///////////////////////////////////////////////////

	private String buildExceptionDetails(ImportException exception) {
		StringWriter writer = new StringWriter();
		if (exception != null) {
			exception.printStackTrace(new PrintWriter(writer));
		}
		return writer.toString();
	}

	private String createSummaryMessage(ImportException exception, List<ImportException> warnings) {
		StringBuilder builder = new StringBuilder();

		builder.append("Found ");

		if (exception != null) {
			builder.append(" 1 error");
			if (!warnings.isEmpty()) {
				builder.append(" and ");
			}
		}

		if (!warnings.isEmpty()) {
			int size = warnings.size();
			builder.append(size);

			if (size > 1) {
				builder.append(" warnings");
			} else {
				builder.append(" warning");
			}
		}

		return builder.toString();
	}
}
