package org.eclipse.bpmn2.modeler.core.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class TristateCheckboxFieldEditor extends BooleanFieldEditor {

	private List<TristateCheckboxFieldEditor> fields = null;
	private Composite parent;
	private int value = 0;
	private int oldValue = 0;
	private SelectionListener selectionListener = null;
	
	public TristateCheckboxFieldEditor(String name, String label, Composite parent) {
		super(name, label, parent);
		this.parent = parent;
	}
	
	public Button getCheckbox() {
		return getChangeControl(parent);
	}
	
	protected List<TristateCheckboxFieldEditor> getFields() {
		if (fields==null)
			fields = new ArrayList<TristateCheckboxFieldEditor>();
		return fields;
	}
	
	public void addField(final TristateCheckboxFieldEditor field) {
		getFields().add(field);
		if (selectionListener==null) {
			selectionListener = new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int newValue = getCheckbox().getSelection() ? 1 : 0;
					if (getCheckbox().getGrayed()) {
						newValue = 2;
					}
					valueChanged(oldValue, newValue);
					oldValue = newValue;
					for (TristateCheckboxFieldEditor field : fields) {
						field.setValue(newValue);
					}
					updateCheckState();
				}
			};
			getCheckbox().addSelectionListener(selectionListener);
		}
		
		field.getCheckbox().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int newValue = getCheckbox().getSelection() ? 2 : 0;
				valueChanged(field.oldValue, newValue);
				field.oldValue = newValue;
				if (field.fields!=null) {
					for (TristateCheckboxFieldEditor f : field.fields) {
						f.setValue(newValue);
					}
				}
				updateCheckState();
			}
		});
	}
	
	protected void doLoad() {
		if (getCheckbox() != null) {
			if (fields==null) {
				super.doLoad();
			}
			else {
				int value = getPreferenceStore().getInt(getPreferenceName());
				setValue(value);
				oldValue = value;
			}
		}
	}
	protected void doLoadDefault() {
		if (getCheckbox() != null) {
			if (fields==null) {
				super.doLoad();
			}
			else {
				int value = getPreferenceStore().getDefaultInt(getPreferenceName());
				setValue(value);
				oldValue = value;
			}
		}
	}
	protected void doStore() {
		if (fields==null) {
			super.doStore();
		}
		else {
			getPreferenceStore().setValue(getPreferenceName(), getValue());
		}
	}

	protected void setValue(int newValue) {
		if (fields==null) {
			getCheckbox().setSelection(newValue==0 ? false : true);
		}
		else {
			int oldValue = value;
			if (newValue==0) {
				getCheckbox().setSelection(false);
				getCheckbox().setGrayed(false);
				value = 0;
			}
			else if (newValue==1) {
				getCheckbox().setSelection(true);
				getCheckbox().setGrayed(true);
				value = 1;
			}
			else if (newValue==2) {
				getCheckbox().setSelection(true);
				getCheckbox().setGrayed(false);
				value = 2;
			}
			valueChanged(oldValue,newValue);
		}
	}
	
	protected void valueChanged(int oldValue, int newValue) {
		if (fields==null) {
			super.valueChanged(oldValue==0 ? false : true, newValue==0 ? false : true);
		}
		else {
			setPresentsDefaultValue(false);
			if (oldValue != newValue) {
				fireValueChanged(VALUE, oldValue, newValue);
				value = newValue;
			}
		}
	}
	
	public void updateCheckState() {
		int trueCount = 0;
		for (TristateCheckboxFieldEditor field : fields) {
			if (field.getBooleanValue())
				++trueCount;
		}
		
		if (trueCount == fields.size()) {
			setValue(2);
		}
		else if (trueCount == 0) {
			setValue(0);
		}
		else {
			setValue(1);
		}
	}
	
	public int getValue() {
		return value;
	}
}