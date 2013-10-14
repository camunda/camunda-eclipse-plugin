package org.camunda.bpm.modeler.core.utils.transform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EObjectEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.ecore.util.EcoreUtil.UsageCrossReferencer;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * A utility to perform advanced modification operations on {@link EObject}s. 
 * 
 * <ul>
 * <li>{@link #morph(EClass)} changes the type of the target object.</li>
 * <li>{@link #swapCrossReferences(EObject)} swaps references to the target object with references to another object</li>
 * <li>{@link #copyChangeCls(EClass)} copies the target object and changes the objects class</li>
 * </ul>
 * 
 * @see DeepCopier
 * @see MorphingCopier
 * 
 * @author nico.rehwaldt
 */
@SuppressWarnings({ "unchecked", "serial" })
public class Transformer {

	private CopyProblemLog log = new CopyProblemLog();
	
	private EObject target;

	/**
	 * Creates a new transformer with the given {@link EObject} as the target.
	 * 
	 * @param target
	 */
	public Transformer(EObject target) {
		this.target = target;
	}
	
	/**
	 * Transforms the target object into an {@link EObject} with the given 
	 * type.
	 * 
	 * Problems occuring during the transformation are logged in the {@link CopyProblemLog} 
	 * and may be queried via {@link #getRecordedWarnings()}.
	 * 
	 * @param newCls
	 * 
	 * @return the morphed object
	 */
	public EObject morph(EClass newCls) {
		EObject newObj = copyChangeCls(newCls);
		
		swapCrossReferences(newObj);
		
		replace(target, newObj);
		
		return newObj;
	}
	
	/**
	 * Swap external references to the target with references 
	 * to the given replacement {@link EObject}.
	 * 
	 * @param replacement
	 */
	public void swapCrossReferences(EObject replacement) {
		
		// replace references
		Collection<Setting> references = UsageCrossReferencer.find(target, ((Resource) target.eResource()).getResourceSet());
		for (Setting reference : references) {
			if (reference instanceof EObjectEList) {
				// update is-many ref
				EObjectEList<EObject> multiReference = (EObjectEList<EObject>) reference;
				int idx = multiReference.indexOf(target);
				if (idx == -1) {
					throw new IllegalStateException(String.format("Multi-reference does not contain <%s>", target));
				}
				multiReference.set(idx, replacement);
			} else {
				// update simple ref
				reference.set(replacement);
			}
		}
	}

	/**
	 * Copies the target with all (compatible) properties to 
	 * a new {@link EObject} with the given {@link EClass}.
	 * 
	 * Problems occuring during the copy operation are logged in 
	 * the {@link CopyProblemLog} and may be queried via 
	 * {@link #getRecordedWarnings()}.
	 * 
	 * @param newCls
	 * @return
	 */
	public EObject copyChangeCls(EClass newCls) {

		// morph-copy object and all attributes
		MorphingCopier copier = new MorphingCopier(target, newCls);
		copier.setWarningHandler(log);

		return copier.copy(target);
	}
	
	protected void replace(EObject object, EObject replacement) {
		EcoreUtil.replace(object, replacement);
	}
	
	/**
	 * Return the warnings recorded during transformation and copy operations.
	 * 
	 * @see #copyChangeCls(EClass)
	 * @see #morph(EClass)
	 * @see #swapCrossReferences(EObject)
	 * 
	 * @return
	 */
	public List<CopyProblem> getRecordedWarnings() {
		return log.getProblems();
	}

	public static class MorphingCopier extends DeepCopier {

		private EObject target;
		private EClass newCls;

		public MorphingCopier(EObject target, EClass newCls) {
			this.target = target;
			this.newCls = newCls;
		}

		protected EObject createCopy(EObject eObject) {
			EClass eClass = getTarget(eObject.equals(target) ? newCls : eObject.eClass());
			return EcoreUtil.create(eClass);
		}
	}

	public static class DeepCopier extends Copier {

		private NotificationHandler<CopyProblem> problemsHandler;

		public DeepCopier() {
			super(true, true);
		}

		protected EStructuralFeature getTarget(EObject copyEObject, EStructuralFeature sourceFeature) {
			if (copyEObject.eClass().getEAllStructuralFeatures().contains(sourceFeature)) {
				return sourceFeature;
			}

			return null;
		}

		public void setWarningHandler(NotificationHandler<CopyProblem> handler) {
			this.problemsHandler = handler;
		}

		/**
		 * Called to handle the copying of an attribute; this adds a list of
		 * values or sets a single value as appropriate for the multiplicity.
		 * 
		 * @param eAttribute
		 *            the attribute to copy.
		 * @param eObject
		 *            the object from which to copy.
		 * @param copyEObject
		 *            the object to copy to.
		 */
		protected void copyAttribute(EAttribute eAttribute, EObject eObject, EObject copyEObject) {
			
			// need to handle set attributes only
			if (!eObject.eIsSet(eAttribute)) {
				return;
			}
			
			// we need to skip attributes that are not
			// supported by the copy target
			EStructuralFeature eAttributeTarget = getTarget(copyEObject, eAttribute);
			if (eAttributeTarget == null) {
				handleProblem(new IgnoredStructuralFeature(eObject, eAttribute, eObject.eGet(eAttribute), copyEObject));
				return;
			}

			if (FeatureMapUtil.isFeatureMap(eAttribute)) {

				FeatureMap copyFeatureMap = (FeatureMap) copyEObject.eGet(eAttributeTarget);

				FeatureMap featureMap = (FeatureMap) eObject.eGet(eAttribute);
				for (int i = 0, size = featureMap.size(); i < size; ++i) {
					Object copyValue = null;
					EStructuralFeature feature = featureMap.getEStructuralFeature(i);
					Object value = featureMap.getValue(i);
					if (value == null) {
						continue;
					}

					// copy references of extension elements
					if (feature instanceof EReference && ((EReference) feature).isContainment()) {
						copyValue = copy((EObject) value);
					} else
					// copy attribute values of extension elements
					if (feature instanceof EAttribute) {
						copyValue = value;
					}

					copyFeatureMap.add(getTarget(feature), copyValue);
				}
			} else if (eAttribute.isMany()) {
				List<?> source = (List<?>) eObject.eGet(eAttribute);
				List<Object> target = (List<Object>) copyEObject.eGet(eAttributeTarget);
				if (source.isEmpty()) {
					target.clear();
				} else {
					target.addAll(source);
				}
			} else {
				copyEObject.eSet(eAttributeTarget, eObject.eGet(eAttribute));
			}
		}

		/**
		 * Returns a copy of the given eObject.
		 * 
		 * @param eObject
		 *            the object to copy.
		 * @return the copy.
		 */
		public EObject copy(EObject eObject) {
			if (eObject == null) {
				return null;
			}
			
			EObject copyEObject = createCopy(eObject);
			put(eObject, copyEObject);
			EClass eClass = eObject.eClass();
			for (int i = 0, size = eClass.getFeatureCount(); i < size; ++i) {
				EStructuralFeature eStructuralFeature = eClass.getEStructuralFeature(i);
				if (eStructuralFeature.isChangeable() && !eStructuralFeature.isDerived()) {
					if (eStructuralFeature instanceof EAttribute) {
						copyAttribute((EAttribute) eStructuralFeature, eObject, copyEObject);
					} else {
						EReference eReference = (EReference) eStructuralFeature;
						if (eReference.isContainment()) {
							copyContainment(eReference, eObject, copyEObject);
						} else {
							copyReference(eReference, eObject, copyEObject);
						}
					}
				}
			}

			copyProxyURI(eObject, copyEObject);

			return copyEObject;
		}

		/**
		 * Called to handle the copying of a cross reference; this adds values
		 * or sets a single value as appropriate for the multiplicity while
		 * omitting any bidirectional reference that isn't in the copy map.
		 * 
		 * @param eReference
		 *            the reference to copy.
		 * @param eObject
		 *            the object from which to copy.
		 * @param copyEObject
		 *            the object to copy to.
		 */
		protected void copyReference(EReference eReference, EObject eObject, EObject copyEObject) {
			
			// need to copy existing references only
			if (!eObject.eIsSet(eReference)) {
				return;
			}
				
			// we need to skip references that are not
			// supported by the copy target
			EStructuralFeature eReferenceTarget = getTarget(copyEObject, eReference);
			if (eReferenceTarget == null) {
				handleProblem(new IgnoredStructuralFeature(eObject, eReference, eObject.eGet(eReference), copyEObject));
				return;
			}
			
			if (eReference.isMany()) {
				InternalEList<EObject> source = (InternalEList<EObject>) eObject.eGet(eReference);
				InternalEList<EObject> target = (InternalEList<EObject>) copyEObject.eGet(eReferenceTarget);
				if (source.isEmpty()) {
					target.clear();
				} else {
					boolean isBidirectional = eReference.getEOpposite() != null;
					int index = 0;
					for (Iterator<EObject> k = resolveProxies ? source.iterator() : source.basicIterator(); k
							.hasNext();) {
						EObject referencedEObject = k.next();
						EObject copyReferencedEObject = get(referencedEObject);
						if (copyReferencedEObject == null) {
							if (useOriginalReferences && !isBidirectional) {
								target.addUnique(index, referencedEObject);
								++index;
							}
						} else {
							if (isBidirectional) {
								int position = target.indexOf(copyReferencedEObject);
								if (position == -1) {
									target.addUnique(index, copyReferencedEObject);
								} else if (index != position) {
									target.move(index, copyReferencedEObject);
								}
							} else {
								target.addUnique(index, copyReferencedEObject);
							}
							++index;
						}
					}
				}
			} else {
				Object referencedEObject = eObject.eGet(eReference, resolveProxies);
				if (referencedEObject == null) {
					copyEObject.eSet(eReferenceTarget, null);
				} else {
					Object copyReferencedEObject = get(referencedEObject);
					if (copyReferencedEObject == null) {
						if (useOriginalReferences && eReference.getEOpposite() == null) {
							copyEObject.eSet(eReferenceTarget, referencedEObject);
						}
					} else {
						copyEObject.eSet(eReferenceTarget, copyReferencedEObject);
					}
				}
			}
		}

		protected void handleProblem(CopyProblem problem) {
			if (problemsHandler != null) {
				problemsHandler.handle(problem);
			} else {
				throw new CopyException(problem);
			}
		}
	}

	public static class CopyProblemLog implements NotificationHandler<CopyProblem> {

		private List<CopyProblem> problems = new ArrayList<CopyProblem>();

		@Override
		public void handle(CopyProblem error) {
			problems.add(error);
		}

		public List<CopyProblem> getProblems() {
			return problems;
		}
	}

	public static class IgnoredStructuralFeature implements CopyProblem {

		private EObject src;
		private EStructuralFeature srcFeature;
		private Object srcAttrValue;
		private EObject target;

		public IgnoredStructuralFeature(EObject src, EStructuralFeature srcFeature, Object srcFeatureValue, EObject target) {

			this.src = src;
			this.srcFeature = srcFeature;
			this.srcAttrValue = srcFeatureValue;
			this.target = target;
		}

		@Override
		public String toString() {
			return String.format("Skipping %s %s=%s: Not compatible with target: %s", src, srcFeature, srcAttrValue,
					target);
		}
	}
}
