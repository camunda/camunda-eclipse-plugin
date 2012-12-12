package org.eclipse.bpmn2.modeler.ui.dialog.importer;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ProblemsContentProvider implements ITreeContentProvider {

  private List<Problem> problems;

  @Override
  public void dispose() {
  }

  @Override
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    this.problems = (List<Problem>) newInput;
  }

  @Override
  public Object[] getElements(Object inputElement) {
    return problems.toArray();
  }

  @Override
  public Object[] getChildren(Object parentElement) {
    return null;
  }

  @Override
  public Object getParent(Object element) {
    return null;
  }

  @Override
  public boolean hasChildren(Object element) {
	  return false;
  }
} 