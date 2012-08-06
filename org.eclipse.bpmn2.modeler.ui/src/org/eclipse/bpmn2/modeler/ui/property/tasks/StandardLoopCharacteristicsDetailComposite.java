package org.eclipse.bpmn2.modeler.ui.property.tasks;

import org.eclipse.bpmn2.Expression;
import org.eclipse.bpmn2.StandardLoopCharacteristics;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.DefaultDetailComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class StandardLoopCharacteristicsDetailComposite extends
		DefaultDetailComposite {

	private Button addRemoveLoopConditionExpressionButton;
	private Button addRemoveLoopMaximumExpressionButton;

	public StandardLoopCharacteristicsDetailComposite(Composite parent,
			int style) {
		super(parent, style);
	}

	public StandardLoopCharacteristicsDetailComposite(
			AbstractBpmn2PropertySection section) {
		super(section);
	}

	@Override
	public void cleanBindings() {
		super.cleanBindings();
		addRemoveLoopConditionExpressionButton = null;
		addRemoveLoopMaximumExpressionButton = null;
	}

	public void createBindings(EObject be) {
		bindAttribute(be,"testBefore");
		
		if (be instanceof StandardLoopCharacteristics) {
			
			final StandardLoopCharacteristics standardLoop = (StandardLoopCharacteristics) be;
				
			addRemoveLoopConditionExpressionButton = new Button(this, SWT.PUSH);
			addRemoveLoopConditionExpressionButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
			addRemoveLoopConditionExpressionButton.addSelectionListener(new SelectionAdapter() {
				
				public void widgetSelected(SelectionEvent e) {
					@SuppressWarnings("restriction")
					TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							if (standardLoop.getLoopCondition() !=null)
								standardLoop.setLoopCondition(null);
							else {
								Expression exp = FACTORY.createFormalExpression();
								standardLoop.setLoopCondition(exp);
								ModelUtil.setID(exp);
							}
							setBusinessObject(standardLoop);
						}
					});
				}
			});

			addRemoveLoopMaximumExpressionButton = new Button(this, SWT.PUSH);
			addRemoveLoopMaximumExpressionButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
			addRemoveLoopMaximumExpressionButton.addSelectionListener(new SelectionAdapter() {
				
				public void widgetSelected(SelectionEvent e) {
					@SuppressWarnings("restriction")
					TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							if (standardLoop.getLoopMaximum() !=null)
								standardLoop.setLoopMaximum(null);
							else {
								Expression exp = FACTORY.createFormalExpression();
								standardLoop.setLoopMaximum(exp);
								ModelUtil.setID(exp);
							}
							setBusinessObject(standardLoop);
						}
					});
				}
			});

			Expression loopexp = (Expression) standardLoop.getLoopCondition();
			Expression maxexp = (Expression) standardLoop.getLoopMaximum();

			if (loopexp != null) {
				addRemoveLoopConditionExpressionButton.setText("Remove Loop Condition");
				this.businessObject = loopexp;
				super.createBindings(loopexp);
			}
			else {
				addRemoveLoopConditionExpressionButton.setText("Add Loop Condition");
			}

			if (maxexp != null) {
				addRemoveLoopMaximumExpressionButton.setText("Remove Loop Maximum");
				this.businessObject = maxexp;
				super.createBindings(maxexp);
			}
			else {
				addRemoveLoopMaximumExpressionButton.setText("Add Loop Maximum");
			}
		}
	}
}
