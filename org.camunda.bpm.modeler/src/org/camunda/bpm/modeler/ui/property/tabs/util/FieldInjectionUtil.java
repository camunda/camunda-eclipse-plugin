package org.camunda.bpm.modeler.ui.property.tabs.util;

import static org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil.createLabel;
import static org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil.createSimpleMultiText;
import static org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil.createSimpleText;
import static org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil.createStandardComposite;

import org.camunda.bpm.modeler.ui.property.tabs.binding.FieldInjectionTextBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.FieldInjectionTextBinding.Binding;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author nico.rehwaldt
 */
public class FieldInjectionUtil {

	/**
	 * Create a text that maps to a field injection definition like the following: 
	 * 
	 * <pre>
	 * &lt;camunda:field name=&quot;$NAME&quot;&gt;
	 *   &lt;camunda:string&gt;
	 *     $VALUE
	 *   &lt;/camunda:string&gt;
	 * &lt;/camunda:field&gt;
	 * </pre>
	 * 
	 * @param section
	 * @param parent
	 * @param label
	 * @param fieldName
	 * @param bo
	 * @return
	 */
	public static Text createLongStringText(GFPropertySection section, Composite parent, String label, String fieldName, final EObject bo) {
		Composite composite = createStandardComposite(section, parent);

		Text text = createSimpleMultiText(section, composite, "");

		new FieldInjectionTextBinding(bo, fieldName, Binding.STRING, text).establish();

		createLabel(section, composite, label, text);
		
		return text;
	}
	
	/**
	 * Create a text that maps to a field injection definition like the following: 
	 * 
	 * <pre>
	 *   &lt;camunda:field name=&quot;$NAME&quot; stringValue=&quot;$VALUE&quot;/&gt;
	 * </pre>
	 * 
	 * @param section
	 * @param parent
	 * @param label
	 * @param fieldName
	 * @param bo
	 * @return
	 */
	public static Text createText(GFPropertySection section, Composite parent, String label, String fieldName, final EObject bo) {
		Composite composite = createStandardComposite(section, parent);

		Text text = createSimpleText(section, composite, "");
		
		new FieldInjectionTextBinding(bo, fieldName, Binding.INLINE_STRING, text).establish();

		createLabel(section, composite, label, text);
		
		return text;
	}
	
	/**
	 * Create a text that maps to a field injection definition like the following: 
	 * 
	 * <pre>
	 *   &lt;camunda:field name=&quot;$NAME&quot; expression=&quot;$VALUE&quot;/&gt;
	 * </pre>
	 * 
	 * @param section
	 * @param parent
	 * @param label
	 * @param fieldName
	 * @param bo
	 * @return
	 */
	public static Text createLongExpressionText(GFPropertySection section, Composite parent, String label, String fieldName, final EObject bo) {
		Composite composite = createStandardComposite(section, parent);

		Text text = createSimpleMultiText(section, composite, "");
		
		new FieldInjectionTextBinding(bo, fieldName, Binding.EXPRESSION, text).establish();

		createLabel(section, composite, label, text);
		
		return text;
	}

	/**
	 * Create a text that maps to a field injection definition like the following: 
	 * 
	 * <pre>
	 *   &lt;camunda:field name=&quot;$NAME&quot; expression=&quot;$VALUE&quot;/&gt;
	 * </pre>
	 * 
	 * @param section
	 * @param parent
	 * @param label
	 * @param fieldName
	 * @param bo
	 * @return
	 */
	public static Text createExpressionText(GFPropertySection section, Composite parent, String label, String fieldName, final EObject bo) {
		Composite composite = createStandardComposite(section, parent);

		Text text = createSimpleText(section, composite, "");
		
		new FieldInjectionTextBinding(bo, fieldName, Binding.INLINE_EXPRESSION, text).establish();

		createLabel(section, composite, label, text);
		
		return text;
	}
}
